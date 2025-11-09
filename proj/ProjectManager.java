
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * The ProjectManager class handles the creation, management, and processing of HDB housing projects,
 * including project registrations, filtering, editing, and deletion.
 * It maintains lists for all projects, active, inactive, and expired projects.
 */
public class ProjectManager {
	//array list of all projects
	private static ArrayList<Project> project_list;
	private static ArrayList<Project> active_list;
	private static ArrayList<Project> expired_list;
	private static ArrayList<Project> inactive_list;
	/**
    * Constructs a new ProjectManager with empty project lists.
    */
	public ProjectManager() {
		project_list = new ArrayList<Project>();
		active_list = new ArrayList<Project>();
		expired_list = new ArrayList<Project>();
		inactive_list = new ArrayList<Project>();
	}
	/**
     * @return The list of all projects.
     */
	static ArrayList<Project> getProjectList() {
		return project_list;
	}
    /**
     * @return The list of active projects.
     */
	static ArrayList<Project> getActiveList() {
		return active_list;
	}
	/**
     * @return The list of expired projects.
     */
	static ArrayList<Project> getExpiredList() {
		return expired_list;
	}
    /**
     * @return The list of inactive projects.
     */	
	static ArrayList<Project> getInactiveList() {
		return inactive_list;
	}
    /**
     * Sets the list of all projects.
     * @param p The list of projects to set.
     */
	static void setProjectList(ArrayList<Project> p) {
		project_list = p;
	}
    /**
     * Sets the list of active projects.
     * @param a The list of active projects to set.
     */
	static void setActiveList(ArrayList<Project> a) {
		active_list = a;
	}
    /**
     * Sets the list of expired projects.
     * @param e The list of expired projects to set.
     */
	static void setExpiredList(ArrayList<Project> e) {
		expired_list = e;
	}
    /**
     * Sets the list of inactive projects.
     * @param i The list of inactive projects to set.
     */
	static void setInactiveList(ArrayList<Project> i) {
		inactive_list = i;
	}
	static Input input=new Input();

    /**
     * Registers an HDB officer to a selected project if they meet the conditions and are not already assigned or applying.
     * @param officer The HDB officer attempting to register.
     */
	public static void registerAsOfficer(HDBOfficer officer) {
		//project class have an array list of officers that register to be officer for that particular project
		if (officer.getProjectInCharge() != null){
			System.out.println("Invalid registration, already handling another project");
			return;
		}
		boolean found = false;
		Project proj = null;
		viewAllProject(new Filter());
		String projName = input.readLine("Select a project to register as officer (Enter project name): ");
		for (Project p : project_list) {
			if (p.get_title().equalsIgnoreCase(projName)) {
				found = true; //if project name input match existing project
				proj = p;
				break;
			}
		}
		while (found == false) {
			System.out.println("Project not found, try again");
			viewAllProject(new Filter());
			projName = input.readLine("Select a project to register as officer (Enter project name): ");
			for (Project p : project_list) {
				if (p.get_title().equalsIgnoreCase(projName)) {
					found = true;
					proj = p;
					break;
				}
			}
		}
		ArrayList<Application> proj_app = proj.get_submissions();
		ArrayList<HDBOfficer> off_list = proj.get_officerList();
		if (off_list.size() >= proj.get_numOfOfficerSlots()) {
			System.out.println("Maximum number of officer slots reached!");
			return;
		}
		boolean non_valid = false;
		for (Application app : proj_app) {
			if (app.getApplicant() == officer) {
				non_valid = true;
			}
		}
		if (non_valid == false) { //condition to register
			//add to pendinglist
			ArrayList<HDBOfficer> pending_reg = proj.get_pendingList();
			pending_reg.add(officer);
			officer.setStatus("Pending");
			System.out.println("Successful registration");
		}
		else {
			System.out.println("Invalid registration, already apply the project as an applicant");
		}
	}
    /**
     * Processes pending officer registration requests by approving or rejecting them.
     * @param manager The HDB manager overseeing the project.
     */
	public static void processOfficerRegistration(HDBManager manager) { //approve or reject pending officer registration for particular active project
		Project project = manager.getProject();
		if (project == null){
			System.out.println("Manager is not handling any project");
			return;
		}
		ArrayList<HDBOfficer> officers = project.get_officerList();
		ArrayList<HDBOfficer> pending_reg = project.get_pendingList();
		if (pending_reg.isEmpty()) {
			System.out.println("No registration found");
			return;
		}
		viewOfficerRegistration(manager);
		int choice = input.readInt("Enter the number corresponding to the officer to process: ");
		System.out.println("1. Approve");
		System.out.println("2. Reject");
		int decision = input.readInt();
		HDBOfficer newOfficer = null;
		if (decision == 1) {
			//move from pending list to officer list
			newOfficer = pending_reg.get(choice - 1);
			pending_reg.remove(choice - 1);
			newOfficer.setStatus("Successful");
			newOfficer.setProjectInCharge(project);
			officers.add(newOfficer);
			System.out.println("Registration approved");
		}
		else if (decision == 2) {
			newOfficer = pending_reg.remove(choice - 1);
			pending_reg.remove(choice - 1);
			newOfficer.setStatus("Failed");
			newOfficer.setProjectInCharge(null);
			System.out.println("Registration rejected");
			//remove from pending
		}
	}
    /**
     * Displays all pending officer registration requests for the manager's project.
     * @param manager The HDB manager handling the project.
     */
	public static void viewOfficerRegistration(HDBManager manager) {
		//view pending
		AtomicInteger index = new AtomicInteger(1);
		Project project = manager.getProject();
		if (project == null){
			System.out.println("Manager is not handling any project");
			return;
		}
		ArrayList<HDBOfficer> pending_reg = project.get_pendingList();
		if (pending_reg.isEmpty()) {
			System.out.println("No registration found");
			return;
		}
		pending_reg.stream().forEach(officer -> System.out.println(index.getAndIncrement() +". " + "Name: " + officer.get_name()));
	}
    /**
     * Displays all available projects filtered by the specified criteria.
     * @param filter A Filter object specifying criteria such as location, price, and visibility.
     */
	public static void viewAllProject(Filter filter) { //include those with visibility off and created by other HDBManagers
		AtomicInteger index = new AtomicInteger(1);
		project_list.stream().filter(project -> {
			boolean match = true;
			if (filter.location != null && !project.get_neighbourhood().equalsIgnoreCase(filter.location)) {match = false;}
			//2 room flats are cheaper than 3 room flats, still print project if price of either not filtered out
			if (filter.minPrice != null && project.get_price3room()<filter.minPrice) {match = false;} //ie. although 2 room below min price, 3 room may be above min price --> still list out
			if (filter.maxPrice != null && project.get_price2room()>filter.maxPrice) {match = false;} //ie. although 3 room above max price, but 2 room below max price --> still list out
			if (filter.check2room == true && project.get_numof2room()<=0) {match = false;} //check if there are two rooms available when this filter is on for singles, else dont show project
			if (filter.check3room == true && project.get_numof3room()<=0) {match = false;} //check if there are three rooms available when this filter is on for married, else dont show project
			if (filter.checkvisibility == true && project.get_visibility() != true) {match = false;} //only allow applicants to view projects with visbility on
			if (filter.myproj_ic != null & project.get_title().equalsIgnoreCase(filter.myproj_ic)) {match = false;} //officer cannot see project they in charge of under list of projects they can apply for
			return match;
		}).forEach(project -> System.out.println(index.getAndIncrement() + ". Name: " + project.get_title())); // if define in ProjectManager class
		if (index.get()==1) {
			System.out.println("No projects matched filter criteria. Please reset filters and try again.");
		}
	}
    /**
     * Displays projects created by the manager, filtered by the specified criteria.
     * @param manager The HDB manager.
     * @param filter The filter object to apply to the project list.
     */
	public static void viewOwnProject(HDBManager manager, Filter filter) { //can filter by location, view + details to print out
		ArrayList<Project> my_proj = manager.getProjList();
		boolean found = false;
		if (my_proj.size() == 0){
			System.out.println("You have no projects created yet");
			return;
		}
		for (Project p : my_proj) {
			boolean match = true;
			if (filter.location != null && !p.get_neighbourhood().equalsIgnoreCase(filter.location)) {match = false;}
			if (filter.checkvisibility == true && p.get_visibility() != true) {match = false;} //view current active project with visiblity "ON"
			if (filter.check_old_upcoming == true && p.get_visibility() == true) {match = false;} //view past and upcoming projects with visibility "OFF"
			if (match) {
				found = true;
				System.out.print("Project title: " + p.get_title()+ "; ");
				if (filter.showVisibility) {
					System.out.print("Visibility: ");
					if (p.get_visibility()) //true = ON
						System.out.print("ON; ");
					else
						System.out.print("OFF; ");
				}
				if (filter.showLocation) {
					System.out.print("Location: " + p.get_neighbourhood() +"; ");
				}
			}
			System.out.println();
		}
		if (!found) {
			System.out.println("No projects matched filter criteria. Please reset filters and try again.");
		}
	}
    /**
     * Creates a new housing project with user input and assigns it to the given manager.
     * Also places it in the appropriate project list based on application dates.
     * @param manager The HDB manager creating the project.
     */
	public static void createProject(HDBManager manager) {
		if (manager.getProject() != null) {
			System.out.println("Unsuccessful, manager is handling another project");
			return;
		}
		System.out.println("Project name: ");
		String name = input.readLine();
		System.out.println("Neighborhood: ");
		String neighborhood = input.readLine();
		System.out.println("Number of 2 room units: ");
		int numOf2Room = input.readInt();
		System.out.println("Price of 2 room units: ");
		int priceOf2Room = input.readInt();
		System.out.println("Number of 3 room units: ");
		int numOf3Room = input.readInt();
		System.out.println("Price of 3 room units: ");
		int priceOf3Room = input.readInt();
		System.out.println("Application Open Date: ");
		System.out.println("Year: ");
		int yearInput1 = input.readInt();
		System.out.println("Month: ");
		int monthInput1 = input.readInt();
		System.out.println("Day: ");
		int dayInput1 = input.readInt();
		LocalDate appOpenDate = LocalDate.of( yearInput1 , monthInput1 , dayInput1);
		System.out.println("Application Close Date: ");
		System.out.println("Year: ");
		int yearInput2 = input.readInt();
		System.out.println("Month: ");
		int monthInput2 = input.readInt();
		System.out.println("Day: ");
		int dayInput2 = input.readInt();
		LocalDate appCloseDate = LocalDate.of( yearInput2 , monthInput2 , dayInput2);
		while (appCloseDate.isBefore(appOpenDate)) {
			System.out.println("Invalid, closing date cannot set before open date");
			System.out.println("Application Open Date: ");
			System.out.println("Year: ");
			yearInput1 = input.readInt();
			System.out.println("Month: ");
			monthInput1 = input.readInt();
			System.out.println("Day: ");
			dayInput1 = input.readInt();
			appOpenDate = LocalDate.of( yearInput1 , monthInput1 , dayInput1);
			System.out.println("Application Close Date: ");
			System.out.println("Year: ");
			yearInput2 = input.readInt();
			System.out.println("Month: ");
			monthInput2 = input.readInt();
			System.out.println("Day: ");
			dayInput2 = input.readInt();
			appCloseDate = LocalDate.of( yearInput2 , monthInput2 , dayInput2);
		}
		System.out.println("Number of officer slots: ");
		int officerSlotsNum = input.readInt();
		while (officerSlotsNum > 10) {
			System.out.println("Invalid input, enter number smaller than 10");
			officerSlotsNum = input.readInt();
		}
		Project project = new Project(name, neighborhood, numOf2Room, priceOf2Room, numOf3Room, priceOf3Room, appOpenDate, appCloseDate, manager, officerSlotsNum);
		project_list.add(project);
		if (appOpenDate.isBefore(LocalDate.now())) {
			inactive_list.add(project);
		}
		else {
			active_list.add(project);
		}
		manager.setProject(project);
		manager.addToProjList(project);
	}
    /**
     * Allows editing of specific attributes of a project managed by the HDB manager.
     * @param manager The HDB manager editing the project.
     */
	public static void editProject(HDBManager manager) {
		System.out.println("Attributes to edit");
		System.out.println("1. Name");
		System.out.println("2. Neighborhood");
		System.out.println("3. Flat type");
		System.out.println("4. Number of units");
		System.out.println("5. Application Open Date");
		System.out.println("6. Application Close Date");
		System.out.println("7. Number of officer slots");
		System.out.println("8. Visibility");
		int attribute = input.readInt();
		while (attribute > 8 || attribute < 1) {
			System.out.println("Invalid choice, try again");
			attribute = input.readInt();
		}
		switch (attribute) {
			case 1:
				System.out.println("Project name: ");
				String name = input.readLine();
				manager.getProject().set_title(name);
				break;
			case 2:
				System.out.println("Neighborhood: ");
				String neighborhood = input.readLine();
				manager.getProject().set_neighbourhood(neighborhood);
				break;
			case 3:
				System.out.println("Number of 2 room units: ");
				int numOf2Room = input.readInt();
				manager.getProject().set_numof2room(numOf2Room);
				break;
			case 4:
				System.out.println("Number of 3 room units: ");
				int numOf3Room = input.readInt();
				manager.getProject().set_numof3room(numOf3Room);
				break;
			case 5:
				System.out.println("Application Open Date: ");
				System.out.println("Year: ");
				int yearInput1 = input.readInt();
				System.out.println("Month: ");
				int monthInput1 = input.readInt();
				System.out.println("Day: ");
				int dayInput1 = input.readInt();
				LocalDate appOpenDate = LocalDate.of( yearInput1 , monthInput1 , dayInput1);
				LocalDate appCloseDate1 = manager.getProject().get_closing_date();
				while (appOpenDate.isAfter(appCloseDate1)) {
					System.out.println("Invalid, closing date cannot set before open date");
					System.out.println("Application Open Date: ");
					System.out.println("Year: ");
					yearInput1 = input.readInt();
					System.out.println("Month: ");
					monthInput1 = input.readInt();
					System.out.println("Day: ");
					dayInput1 = input.readInt();
				}
				manager.getProject().set_opening_date(appOpenDate);
				break;
			case 6:
				System.out.println("Application Close Date: ");
				System.out.println("Year: ");
				int yearInput2 = input.readInt();
				System.out.println("Month: ");
				int monthInput2 = input.readInt();
				System.out.println("Day: ");
				int dayInput2 = input.readInt();
				LocalDate appCloseDate2 = LocalDate.of( yearInput2 , monthInput2 , dayInput2);
				LocalDate appOpenDate2 = manager.getProject().get_opening_date();
				while (appOpenDate2.isAfter(appCloseDate2)) {
					System.out.println("Invalid, closing date cannot set before open date");
					System.out.println("Application Close Date: ");
					System.out.println("Year: ");
					yearInput2 = input.readInt();
					System.out.println("Month: ");
					monthInput2 = input.readInt();
					System.out.println("Day: ");
					dayInput2 = input.readInt();
					appCloseDate2 = LocalDate.of( yearInput2 , monthInput2 , dayInput2);
				}
				manager.getProject().set_closing_date(appCloseDate2);
				break;
			case 7:
				System.out.println("Number of officer slots: ");
				int officerSlotsNum = input.readInt();
				while (officerSlotsNum > 10) {
					System.out.println("Invalid input, enter number smaller than 10");
					officerSlotsNum = input.readInt();
				}
				manager.getProject().set_numOfOfficerSlots(officerSlotsNum);
				break;
			case 8:
				System.out.println("Current Visibility: " + manager.getProject().get_visibility());
				System.out.println("Visibility set to (enter true or false): ");
				boolean visiBool = false;
				String visibility = input.readLine().toLowerCase();
				while (!(visibility.equals("true") || visibility.equals("false"))){
					System.out.println("Invalid input");
					visibility = input.readLine().toLowerCase();
				}
				if (visibility.equals("true")) {
					visiBool = true;
				}
				else if (visibility.equals("false")) {
					visiBool = false;
				}
				manager.getProject().set_visibility(visiBool);
				System.out.println("Current Visibility: " + manager.getProject().get_visibility());
				break;
		}

	}
    /**
     * Deletes the current project being managed and removes it from all project lists.
     * @param manager The HDB manager deleting the project.
     */
	public static void deleteProject(HDBManager manager) {
		Project deletedProject = manager.getProject();
		if (deletedProject == null){
			System.out.println("Manager is not handling any project");
			return;
		}
		manager.delProjListItem(deletedProject);
		manager.setProject(null);
		project_list.remove(deletedProject);
		if (active_list.contains(deletedProject)) {
			active_list.remove(deletedProject);
		}
		else if (inactive_list.contains(deletedProject)){
			inactive_list.remove(deletedProject);
		}
		System.out.println("Project deleted");
	}
    /**
     * Displays full project details of the project an HDB officer is currently managing.
     * @param officer The officer viewing the project.
     */
	public static void viewProjectDetails(HDBOfficer officer) { //officer can view details of project in charge of regardless of visbility
		Project project = officer.getProjectInCharge();
		if (project == null){
			System.out.println("Not handling any project at the moment");
			return;
		}
		System.out.println("Project name: " + project.get_title());
		System.out.println("Neighborhood: " + project.get_neighbourhood());
		System.out.println("Number of 2 room units: " + project.get_numof2room());
		System.out.println("Number of 3 room units: " + project.get_numof3room());
		System.out.println("Application Open Date: " + project.get_opening_date());
		System.out.println("Application Close Date: " + project.get_closing_date());
		System.out.println("List of officers:");
		AtomicInteger index = new AtomicInteger(1);
		project.get_officerList().stream().forEach(off -> System.out.println(index.getAndIncrement() + ". " + off.get_name()));
		System.out.println("HDB Manager: " + project.get_managerIC().get_name());
	}

	public static void printLocations() {
		//display all available and unique locations when choosing location filters
		ArrayList<String> locations = new ArrayList<String>();
		for (Project proj : project_list) {
			String loc = proj.get_neighbourhood();
			if (!locations.contains(loc)) {
				locations.add(loc);
			}
		}
		for (String loc : locations) {
			System.out.print(loc + "; ");
		}
	}
}


