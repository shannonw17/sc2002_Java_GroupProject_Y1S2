import java.util.ArrayList;
/**
 * Manages the application process for HDB housing projects.
 */
public class ApplicationManager{
    /**
     * Allows an applicant to apply for a visible housing project based on their eligibility.
     *
     * @param applicant the applicant who wants to apply for a housing project
     */
    public static void newApplication(Applicant applicant) {
        Input input = new Input();
        ArrayList<Project> projectList = ProjectManager.getProjectList(); // getting the price of projects
        for (Project project : projectList) {
            if (project.get_visibility()) { //if project not visible, do not print out, if not list
                System.out.println(project.get_title());
            }
        }
        Project project = null;
        do{
            String projTitle = input.readLine("Enter project title that you wish to apply for: ").toLowerCase();
            project = projectList.stream().filter(p -> p.get_title()
                .equalsIgnoreCase(projTitle)).findFirst().orElse(null);} //filtering and retrieving the project
        while (project == null);

        if (applicant.get_age() > 34 && applicant.get_marital_stat() == false) {
            System.out.println("You can only apply for 2-room flat! Applying now..");
            Application application = new Application(applicant, project, "two");
            applicant.set_application(application);
            ArrayList<Application> applicationList = project.get_submissions();
            applicationList.add(application);
            project.set_submissions(applicationList);
            System.out.println("Successfully applied for 2 room!");
        } else if (applicant.get_age() > 21 && applicant.get_marital_stat() == true) {
            int choice = input.readInt("You can apply for a 1) 2-room or 2) 3-room flat! Enter 1 or 2:");
            if (choice == 1) {
                Application application = new Application(applicant, project, "two");
                applicant.set_application(application);
                ArrayList<Application> applicationList = project.get_submissions();
                applicationList.add(application);
                project.set_submissions(applicationList);
                System.out.println("Successfully applied for 2 room!");
            } else if (choice == 2) {
                Application application = new Application(applicant, project, "three");
                ArrayList<Application> applicationList = project.get_submissions();
                applicant.set_application(application);
                applicationList.add(application);
                project.set_submissions(applicationList);
                System.out.println("Successfully applied for 3 room!");
            }
        }
        else {
                System.out.println("You are not eligible to apply for any projects!");
            }
    }
    /**
     * Lists all applicants who have submitted an application for a particular project
     * managed by the given HDBManager.
     *
     * @param manager the HDBManager responsible for the project
     */
    public static void listApplicants(HDBManager manager){
        Project project = manager.getProject();
        ArrayList<Application> applicationList = project.get_submissions();
        for(Application application: applicationList){
            int count = 1;
            System.out.println("Applicant number " + count + ": " + application.getApplicant().get_name());
        }
    }
    /**
     * Allows an HDBManager to approve or reject an application for a specific project
     * once the application window has closed (i.e., project visibility is false).
     *
     * @param manager the HDBManager who will process the applications
     */
    public static void processApplication(HDBManager manager) {
        Input input = new Input();
        ArrayList<Project> projectList = manager.getProjList();
        System.out.println("Project list:");
        for (Project project : projectList) { //print out the name of every project and let the manager choose
            System.out.println(project.get_title());
        }
        String projTitle = input.readLine("Enter project title you wish to access: ");
        Project project = projectList.stream().filter(p -> p.get_title().equalsIgnoreCase(projTitle)).findFirst().orElse(null);
        int numof2room_success = project.get_numof2room_success();
        int numof3room_success = project.get_numof3room_success();
        if (project.get_visibility()) {//if project still visible, don't let manager touch
            System.out.println("Project still ongoing! Unable to process applications yet!");
            return;
        }
        ArrayList<Application> applicationList = project.get_submissions();
        System.out.println("Applicant list:");
        for (Application application : applicationList) { //print out the name of every applicant and let the manager choose
            System.out.println(application.getApplicant().get_name());
        }
        String name = input.readLine("Enter name of the applicant: ");
        Application application = applicationList.stream().filter(a -> a.getApplicant().get_name()
                .equalsIgnoreCase(name)).findFirst().orElse(null);
        int choice = input.readInt("Do you want to 1.approve or 2.reject them? Enter your choice: ");
        if (choice == 1) { //need to check which room they are allocated.
            if (application.getRoomType().equalsIgnoreCase("two")) {
                if (numof2room_success < project.get_numof2room()) { //Check if still have 2 room
                    application.setStatus("Successful");
                    applicationList.remove(application); //removes application from submission list
                    project.set_submissions(applicationList); //updates the submission list
                    ArrayList<Application> successList = project.get_successful(); //retrieve old successful list
                    successList.add(application); //adding application to successful
                    project.set_successful(successList); //overwriting old success list with the newly added application
                    project.set_numof2room_success(numof2room_success + 1);
                    System.out.println("Approval successful!");
                } else {
                    System.out.println("Number of 2 rooms fully applied for! Unable to approve.");
                    return;
                }
            }
            if (application.getRoomType().equalsIgnoreCase("three")) {
                if (numof3room_success < project.get_numof3room()) {
                    application.setStatus("Successful");
                    applicationList.remove(application); //removes application from submission list
                    project.set_submissions(applicationList); //updates the submission list
                    ArrayList<Application> successList = project.get_successful(); //retrieve old successful list
                    successList.add(application); //adding application to successful
                    project.set_successful(successList); //overwriting old success list with the newly added application
                    project.set_numof3room_success(numof3room_success + 1);
                    System.out.println("Approval successful!");
                } else {
                    System.out.println("Number of 3 rooms fully applied for! Unable to approve.");
                    return;
                }
            }
        }
        else if (choice == 2){
            applicationList.remove(application); //removes application from submission list
            project.set_submissions(applicationList); //updates the submission list
            application.setStatus("Failed");
            System.out.println("Rejection successful!");
        }
    }
    /**
     * Submits a request to withdraw a housing application.
     *
     * @param application the application that the applicant wants to withdraw
     */
    public static void requestWithdrawApplication(Application application){
        Project project = application.getProject(); //get project associated with the application
        project.addWithdrawApplication(application); //adds application to withdrawal list in project
        System.out.println("Request for withdrawal successful!");
    }
    /**
     * Allows an HDBManager to process withdrawal requests for a specific project.
     * The manager can approve or reject each withdrawal request.
     *
     * @param manager the HDBManager who will process the withdrawal requests
     */
    public static void processWithdrawApplication(HDBManager manager){
        Input input = new Input();
        ArrayList<Project> projList = manager.getProjList();
        if (projList.isEmpty()) {
            System.out.println("No project created by manager");
            return;
        }
        for(Project project: projList){
            System.out.println(project.get_title());
        }
        String projName = input.readLine("Enter title of project you wish to process withdrawals for:");
        Project project = projList.stream().filter(p-> p.get_title()
                .equalsIgnoreCase(projName)).findFirst().orElse(null); //Gets the project from the proj list
        ArrayList<Application> withdrawalList = project.get_withdrawals(); //retrieve project's withdrawalList
        ArrayList<Application> submissionList = project.get_submissions(); //retrieve project's submissionList
        for (Application applicantObj: withdrawalList){
            System.out.println(applicantObj.getApplicant().get_name()); //print out every applicant by calling applicant's getName() method
        }
        String name = input.readLine("Enter name of applicant you wish to process:");
        Application app = withdrawalList.stream().filter(application->application.getApplicant().get_name()
                .equalsIgnoreCase(name)).findFirst().orElse(null);
        Applicant applicant = app.getApplicant(); //getting the applicant that we want to process
        int choice = input.readInt("Do you wish to 1.approve or 2.reject " + name + "'s withdrawal?");
        withdrawalList.removeIf(application -> application.getApplicant().get_name().equalsIgnoreCase(name)); //removes application matching name from withdrawalList
        if (choice == 1) {
            submissionList.removeIf(application -> application.getApplicant().get_name().equalsIgnoreCase(name));//removes application matching name from submissionList
            project.set_submissions(submissionList); //updates project's submissionList
            project.set_withdrawals(withdrawalList); //updates project's withdrawalList
            applicant.set_application(null); //deletes the application from applicant
            System.out.println("Successfully approved!");
            }

        else{
            project.set_withdrawals(withdrawalList);
            System.out.println("Successfully rejected!");
        }
    }
    /**
     * Allows an HDBOfficer to book a flat for an applicant whose application has been approved.
     * Updates the flat availability in the project and the applicant’s profile.
     *
     * @param officer the HDBOfficer in charge of the project
     */
    public static void bookingFlat(HDBOfficer officer){
        Input input = new Input();
        Project project = officer.getProjectInCharge();
        String applicantNRIC = input.readLine("Enter NRIC of applicant: ");
        Application application = project.get_successful().stream().filter(a->a.getApplicant().get_nric().equalsIgnoreCase(applicantNRIC)).findFirst().orElse(null);
        if (application == null || !application.getStatus().equalsIgnoreCase("Successful")){
            System.out.println("Unable to book flat because application status is not yet successful.");
            return;
        }
        String flat = application.getRoomType();
        application.getApplicant().set_typeOf_flat(flat); //update the applicant's profile with flat
        application.getApplicant().get_application().setStatus("Booked");
        if (flat.equalsIgnoreCase("two")){
            int num = project.get_numof2room();
            project.set_numof2room(num - 1);
            System.out.println("Successfully booked 2 room!");
        }
        else{
            int num = project.get_numof3room();
            project.set_numof3room(num - 1);
            System.out.println("Successfully booked 3 room!");
        }
    }
    /**
     * Prints a receipt for all applicants with successful applications for the project
     * managed by the given officer.
     *
     * @param officer the HDBOfficer managing the project
     */
    public static void printReceipt(HDBOfficer officer){
        Project project = officer.getProjectInCharge();
        if (project == null){
            System.out.println("Officer not in charge of any project!");
            return;
        }
        ArrayList<Application> applicantList = project.get_successful(); //only print for after booking ie. successful application
        for (Application app: applicantList){
            app.getApplicant().generate_receipt();
            System.out.println("Project title: " + project.get_title() + ", Location: " + project.get_neighbourhood());
        }
    }

    //enable officers to apply for BTO they are not in charge of
    /**
     * Allows an HDBOfficer to apply for a housing project that they are not managing,
     * based on their eligibility.
     *
     * @param officer the officer who wants to apply for a housing project
     */
    public static void newApplication(HDBOfficer officer) {
        Input input = new Input();
        ArrayList<Project> projectList = ProjectManager.getActiveList(); // getting the list of projects
        for (Project project : projectList) {
            if (project.get_visibility()&& !officer.getProjectInCharge().equals(project)) { //if project not visible, do not print out, if not list
                System.out.println(project.get_title());
            }
        }
        String projTitle = input.readLine("Enter project title that you wish to apply for:");
        Project project = projectList.stream().filter(p -> p.get_title()
                .equalsIgnoreCase(projTitle)).findFirst().orElse(null);; //filtering and retrieving the project
        if (officer.get_age() > 34 && officer.get_marital_stat() == false) {
            System.out.println("You can only apply for 2-room flat! Applying now..");
            Application application = new Application(officer, project, "2");
            ArrayList<Application> applicationList = project.get_submissions();
            applicationList.add(application);
            project.set_submissions(applicationList);
            System.out.println("Successfully applied for 3 room!");
        } else if (officer.get_age() > 21 && officer.get_marital_stat() == true) {
            int choice = input.readInt("You can apply for a 1) 2-room or 2) 3-room flat! Enter 1 or 2:");
            if (choice == 1) {
                Application application = new Application(officer, project, "2");
                ArrayList<Application> applicationList = project.get_submissions();
                applicationList.add(application);
                project.set_submissions(applicationList);
                System.out.println("Successfully applied for 2 room!");
            } else if (choice == 2) {
                Application application = new Application(officer, project, "3");
                System.out.println("Successfully applied for 3 room!");
                ArrayList<Application> applicationList = project.get_submissions();
                applicationList.add(application);
                project.set_submissions(applicationList);
            }
        }
        else {
                System.out.println("You are not eligible to apply for any projects!");
            }

    }

}