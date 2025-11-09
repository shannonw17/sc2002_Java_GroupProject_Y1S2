import java.util.HashMap;
import java.util.List;
/**
 * This class handles the user interface for the HDBManager, displaying options for the manager
 * to interact with and manage their HDB projects, applications, enquiries, and other related operations.
 * It provides a text-based dashboard allowing the HDBManager to select various actions, such as
 * creating projects, viewing projects, processing applications, and generating reports.
 */
public class HDBManagerDisplay {
    /**
     * Starts the HDBManager dashboard, displaying a menu for the user to choose from different operations.
     * The available options differ based on whether the manager is currently handling an active project.
     *
     * @param manager The current HDBManager who is logged in.
     * @param man_database The database of managers.
     * @param data_base The overall database containing all project and user data.
     */
    public static void start(HDBManager manager, HashMap<String, List<String>> man_database, HashMap<String, HashMap<String, List<String>>> data_base) {
        Input man_input = new Input();
        while (true) {
            System.out.println("Welcome " + manager.get_name() + ", what would you like to do:" );
            EnquiryInterface enquiryInterface = new EnquiryManager();
            if (manager.getProject() == null) { //HDBManager not handling any active projects currently, show a different dashboard to limit operations available
                System.out.println("============================================");
                System.out.println("Enter a number corresponding to the operation");
                System.out.println("1. Create a project");
                System.out.println("2. View all projects");
                System.out.println("3. View my projects");
                System.out.println("4. View application");
                System.out.println("5. Process application");
                System.out.println("6. View enquiries - (for all projects)");
                System.out.println("7. Change password");
                System.out.println("8. Logout");
                System.out.println("============================================");
                int choice = man_input.readInt();
                switch (choice) {
                    case 1:
                        ProjectManager.createProject(manager);
                        break;
                    case 2:
                        System.out.println("Set filters? (Y/N)");
                        char set_filter = Character.toUpperCase(man_input.readWord().charAt(0));
                        Filter print_Filter = new Filter();
                        if (set_filter == 'Y') {
                            System.out.println("1. Filter location");
                            System.out.println("2. Filter minimum housing price");
                            System.out.println("3. Filter maximum housing price");
                            System.out.println("4. Reset filters");
                            System.out.println("5. Finish selecting filters");
                            System.out.println("Choose filtering option: ");
                            int filter_choice = man_input.readInt();
                            while (filter_choice < 4) {
                                switch(filter_choice) {
                                    case 1:
                                        System.out.println("Enter specific location: ");
                                        ProjectManager.printLocations();
                                        System.out.println();
                                        print_Filter.location = man_input.readLine();
                                        break;
                                    case 2:
                                        System.out.println("Enter specific minimum housing price: ");
                                        print_Filter.minPrice = man_input.readInt(); //autoboxing from primitive type to wrapper class type
                                        break;
                                    case 3:
                                        System.out.println("Enter specific maximum housing price: ");
                                        print_Filter.maxPrice = man_input.readInt();
                                        break;
                                    case 4: print_Filter.location = null;
                                        print_Filter.minPrice = null;
                                        print_Filter.maxPrice = null;
                                        System.out.println("Successfully set filter.");
                                        break;
                                    default: System.out.println("Invalid option!");
                                }
                                System.out.println("Choose filtering option: ");
                                filter_choice = man_input.readInt();
                            }
                        }
                        ProjectManager.viewAllProject(print_Filter); //filter elements default to null ie. wont have any filters if none is set so should be able to print all
                        break;
                    case 3:
                        System.out.println("Set filters? (Y/N)");
                        char set_filter2 = Character.toUpperCase(man_input.readWord().charAt(0));
                        Filter print_Filter2 = new Filter();
                        if (set_filter2 == 'Y') {
                            System.out.println("1. Filter location");
                            System.out.println("2. Old and upcoming projects only");
                            System.out.println("3. Current active project only");
                            System.out.println("4. Hide location");
                            System.out.println("5. Hide visibility");
                            System.out.println("6. Reset filters");
                            System.out.println("7. Finish selecting filters");

                            System.out.println("Choose filtering option: ");
                            int filter_choice1 = man_input.readInt();
                            while (filter_choice1 < 6) {
                                switch(filter_choice1) {
                                    case 1:
                                        System.out.println("Enter specific location: ");
                                        ProjectManager.printLocations();
                                        System.out.println();
                                        print_Filter2.location = man_input.readLine();
                                        break;
                                    case 2:
                                        print_Filter2.check_old_upcoming = true; //filter away active projects
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 3:
                                        print_Filter2.checkvisibility = true;
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 4:
                                        print_Filter2.showLocation = false;
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 5:
                                        print_Filter2.showVisibility = false;
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 6:
                                        print_Filter2.location = null;
                                        print_Filter2.check_old_upcoming = false;
                                        print_Filter2.checkvisibility = false;
                                        print_Filter2.showLocation = true;
                                        print_Filter2.showVisibility = true;
                                        System.out.println("Successfully reset filter.");
                                    default: System.out.println("Invalid choice");
                                }
                                System.out.println("Choose filtering option: ");
                                filter_choice1 = man_input.readInt();
                            }
                        }
                        ProjectManager.viewOwnProject(manager, print_Filter2);
                        break;
                    case 4:
                        ApplicationManager.listApplicants(manager);
                        break;
                    case 5:
                        ApplicationManager.processApplication(manager);
                        break;
                    case 6:
                        enquiryInterface.viewEnquiries(manager);
                        break;
                    case 7:
                        String new_pwd = man_input.readLine("Enter new password: ");
                        manager.change_pwd(man_database, new_pwd);
                        UserManager.create_object_lists(data_base); //update objects to have new password
                        return; //relogin after change password
                    case 8 : return;
                    default: System.out.println("Invalid choice!");
                }
            }
            else { //cannot create new project when handling a project under activeProject currently
                System.out.println("============================================");
                System.out.println("Enter a number corresponding to the operation");
                System.out.println("1. Edit project");
                System.out.println("2. Delete project");
                System.out.println("3. View all projects");
                System.out.println("4. View my projects");
                System.out.println("5. View application");
                System.out.println("6. Process application");
                System.out.println("7. View pending HDB officer registration");
                System.out.println("8. Process HDB officer registration");
                System.out.println("9. Process withdrawal request");
                System.out.println("10. View enquiries");
                System.out.println("11. Process enquiries");
                System.out.println("12. Generate report");
                System.out.println("13. Change password");
                System.out.println("14. Logout");
                int choice = man_input.readInt();
                switch (choice) {
                    case 1:
                        ProjectManager.editProject(manager);
                        break;
                    case 2:
                        ProjectManager.deleteProject(manager);
                        break;
                    case 3:
                        System.out.println("Set filters? (Y/N)");
                        char set_filter = Character.toUpperCase(man_input.readWord().charAt(0));
                        Filter print_Filter = new Filter();
                        if (set_filter == 'Y') {
                            System.out.println("1. Filter location");
                            System.out.println("2. Filter minimum housing price");
                            System.out.println("3. Filter maximum housing price");
                            System.out.println("4. Reset filters");
                            System.out.println("5. Finish selecting filters");

                            System.out.println("Choose filtering option: ");
                            int filter_choice = man_input.readInt();
                            while (filter_choice < 4) {
                                switch(filter_choice) {
                                    case 1:
                                        System.out.println("Enter specific location: ");
                                        ProjectManager.printLocations();
                                        System.out.println();
                                        print_Filter.location = man_input.readLine();
                                        break;
                                    case 2:
                                        System.out.println("Enter specific minimum housing price: ");
                                        print_Filter.minPrice = man_input.readInt(); //autoboxing from primitive type to wrapper class type
                                        break;
                                    case 3:
                                        System.out.println("Enter specific maximum housing price: ");
                                        print_Filter.maxPrice = man_input.readInt();
                                        break;
                                    case 4: print_Filter.location = null;
                                        print_Filter.minPrice = null;
                                        print_Filter.maxPrice = null;
                                        System.out.println("Successfully reset filter.");
                                        break;
                                    default: System.out.println("Invalid option!");
                                }
                                System.out.println("Choose filtering option: ");
                                filter_choice = man_input.readInt();
                            }
                        }
                        ProjectManager.viewAllProject(print_Filter); //filter elements default to null ie. wont have any filters if none is set so should be able to print all
                        break;
                    case 4:
                        System.out.println("Set filters? (Y/N)");
                        char set_filter2 = Character.toUpperCase(man_input.readWord().charAt(0));
                        Filter print_Filter2 = new Filter();
                        if (set_filter2 == 'Y') {
                            System.out.println("1. Filter location");
                            System.out.println("2. Old and upcoming projects only");
                            System.out.println("3. Current active project only");
                            System.out.println("4. Hide location");
                            System.out.println("5. Hide visibility");
                            System.out.println("6. Reset filters");
                            System.out.println("7. Finish selecting filters");

                            System.out.println("Choose filtering option: ");
                            int filter_choice1 = man_input.readInt();
                            while (filter_choice1 < 6) {
                                switch(filter_choice1) {
                                    case 1:
                                        System.out.println("Enter specific location: ");
                                        ProjectManager.printLocations();
                                        System.out.println();
                                        print_Filter2.location = man_input.readLine();
                                        break;
                                    case 2:
                                        print_Filter2.check_old_upcoming = true; //filter away active projects
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 3:
                                        print_Filter2.checkvisibility = true;
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 4:
                                        print_Filter2.showLocation = false;
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 5:
                                        print_Filter2.showVisibility = false;
                                        System.out.println("Successfully set filter.");
                                        break;
                                    case 6:
                                        print_Filter2.location = null;
                                        print_Filter2.check_old_upcoming = false;
                                        print_Filter2.checkvisibility = false;
                                        print_Filter2.showLocation = true;
                                        print_Filter2.showVisibility = true;
                                        System.out.println("Successfully reset filter.");
                                    default: System.out.println("Invalid choice");
                                }
                                System.out.println("Choose filtering option: ");
                                filter_choice1 = man_input.readInt();
                            }
                        }
                        ProjectManager.viewOwnProject(manager, print_Filter2);
                        break;
                    case 5:
                        ApplicationManager.listApplicants(manager);
                        break;
                    case 6:
                        ApplicationManager.processApplication(manager);
                        break;
                    case 7:
                        ProjectManager.viewOfficerRegistration(manager);
                        break;
                    case 8:
                        ProjectManager.processOfficerRegistration(manager);
                        break;
                    case 9:
                        ApplicationManager.processWithdrawApplication(manager);
                        break;
                    case 10:
                        enquiryInterface.viewEnquiries(manager);
                        break;
                    case 11:
                        enquiryInterface.replyEnquiry(manager);
                        break;
                    case 12:
                        System.out.println("Set filters? (Y/N)");
                        char set_filter3 = Character.toUpperCase(man_input.readWord().charAt(0));
                        Filter report_Filter = new Filter();
                        if (set_filter3 == 'Y') {
                            System.out.println("1. Filter type of flat (2/3-room)");
                            System.out.println("2. Filter marital status");
                            System.out.println("3. Filter minimum age");
                            System.out.println("4. Filter maximum age");
                            System.out.println("5. Hide type of flat");
                            System.out.println("6. Hide marital status");
                            System.out.println("7. Hide age");
                            System.out.println("8. Hide Project title");
                            System.out.println("9. Reset filters");
                            System.out.println("10. Finish selecting filters");

                            System.out.println("Choose filtering option: ");
                            int filter_option = man_input.readInt();
                            while (filter_option < 10) {
                                switch(filter_option) {
                                    case 1:
                                        System.out.println("Choose type of flat (two / three):");
                                        report_Filter.flatType = man_input.readLine().toLowerCase();
                                        break;
                                    case 2:
                                        System.out.println("Choose marital status: ");
                                        report_Filter.filter_marital = man_input.readLine().toLowerCase();
                                        break;
                                    case 3:
                                        System.out.println("Enter minimum age: ");
                                        report_Filter.minAge = man_input.readInt();
                                        break;
                                    case 4:
                                        System.out.println("Enter maximum age: ");
                                        report_Filter.maxAge = man_input.readInt();
                                        break;
                                    case 5:
                                        System.out.println("Show type of flat? (Y/N)");
                                        char input_flat = Character.toUpperCase(man_input.readWord().charAt(0));
                                        report_Filter.showFlatType = (input_flat == 'Y');
                                        break;
                                    case 6:
                                        System.out.println("Show marital status? (Y/N)");
                                        char input_martial = Character.toUpperCase(man_input.readWord().charAt(0));
                                        report_Filter.showMarital = (input_martial == 'Y');
                                        break;
                                    case 7:
                                        System.out.println("Show age? (Y/N)");
                                        char input_age = Character.toUpperCase(man_input.readWord().charAt(0));
                                        report_Filter.showAge = (input_age == 'Y');
                                        break;
                                    case 8:
                                        System.out.println("Show project title? (Y/N)");
                                        char input_title = Character.toUpperCase(man_input.readWord().charAt(0));
                                        report_Filter.showProjectName = (input_title == 'Y');
                                        break;
                                    case 9:
                                        report_Filter.flatType = null;
                                        report_Filter.filter_marital = null;
                                        report_Filter.minAge = null;
                                        report_Filter.maxAge = null;
                                        report_Filter.showFlatType = true;
                                        report_Filter.showMarital = true;
                                        report_Filter.showAge = true;
                                        report_Filter.showProjectName = true;
                                        System.out.println("Successfully reset filters");
                                    default: System.out.println("Invalid option");
                                }
                                System.out.println("Choose filtering option: ");
                                filter_option = man_input.readInt();
                            }
                        }
                        manager.generate_report(report_Filter);
                        break;
                    case 13:
                        System.out.println("Enter new password: ");
                        String new_pwd = man_input.readLine();
                        manager.change_pwd(man_database, new_pwd);
                        UserManager.create_object_lists(data_base); //update objects to have new password
                        return; //relogin after change password
                    case 14 : return;
                    default: System.out.println("Invalid choice!");
                }
            }
        }
    }
}