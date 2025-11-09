import java.util.HashMap;
import java.util.List;
/**
 * Handles the user interface logic for an {@link Applicant}.
 * Displays options and executes commands based on the applicant's profile and application status.
 */
public class ApplicantDisplay {
        /**
     * Starts the display logic for an applicant, allowing them to interact with the system.
     * Options shown depend on their age, marital status, and whether they have applied or booked a BTO.
     *
     * @param applicant the {@link Applicant} currently logged in
     * @param app_database the user database containing credentials
     * @param data_base the full application data grouped by roles
     */
    public static void start(Applicant applicant, HashMap<String, List<String>> app_database, HashMap<String, HashMap<String, List<String>>> data_base) {
        Input input = new Input();
        if ((applicant.get_age() < 21) || (applicant.get_age() < 35 && applicant.get_marital_stat()!=true) ) { //wont be able to see at all if below 21
                System.out.println("Not of age to book BTO");
                System.out.println("Change password? ");
                char ch_pwd = Character.toUpperCase(input.readWord().charAt(0));
                if (ch_pwd == 'Y') {
                        String new_pwd = input.readLine("Enter new password: ");
                        applicant.change_pwd(app_database, new_pwd); 
                        UserManager.create_object_lists(data_base); //update objects to have new password
                }//only allow these users to change password, else not able to perform any operations in this system
        }
        while (true) {
                EnquiryInterface enquiryInterface = new EnquiryManager();
                System.out.println("==============================");
                System.out.println("Welcome " + applicant.get_name() +", what would you like to do:");

                String options =
                        "1. View available projects \n" +
                        "2. Request to withdraw current application \n" +
                        "3. Submit enquiry \n" + //all these are from EnquiryManager
                        "4. Edit enquiry \n" + //editEnquiry
                        "5. Delete enquiry \n" +
                        "6. Delete specific message \n" +
                        "7. View enquiries \n" +
                        "8. Change password \n" +
                        "9. View Application Status\n"+
                        "10. Logout\n";

                String options2 =
                        "1. View available projects\n" +
                        "2. Apply for new application\n" +
                        "3. Change password\n" +
                        "4. Logout\n";

                String options3 =
                                "1. View available projects \n" +
                                "2. Submit enquiry \n" + //all these are from EnquiryManager
                                "3. Edit enquiry \n" + //editEnquiry
                                "4. Delete enquiry \n" +
                                "5. Delete specific message \n" +
                                "6. View enquiries \n" +
                                "7. Change password \n" +
                                "8. View Application Status\n"+
                                "9. Logout\n";

                if (applicant.get_application() == null){
                        int choice = input.readInt(options2);
                                switch (choice) {
                                        case 1 :
                                                System.out.println("Set filters? (Y/N)");
                                                char set_filter = Character.toUpperCase(input.readWord().charAt(0));
                                                Filter print_Filter = new Filter();
                                                if (set_filter == 'Y') {
                                                        System.out.println("1. Filter location");
                                                        System.out.println("2. Filter minimum housing price");
                                                        System.out.println("3. Filter maximum housing price");
                                                        System.out.println("4. Reset filters");
                                                        System.out.println("5. Finish selecting filters");

                                                        int filter_choice = input.readInt("Choose filtering option: ");
                                                        while (filter_choice < 5) {
                                                                switch(filter_choice) {
                                                                        case 1:
                                                                                System.out.println("Enter preferred location: ");
                                                                                ProjectManager.printLocations();
                                                                                System.out.println();
                                                                                print_Filter.location = input.readLine();
                                                                                break;
                                                                        case 2:
                                                                                print_Filter.minPrice = input.readInt("Enter preferred minimum housing price: "); //autoboxing from primitive type to wrapper class type
                                                                                break;
                                                                        case 3:
                                                                                print_Filter.maxPrice = input.readInt("Enter preferred maximum housing price: ");
                                                                                break;
                                                                        case 4: print_Filter.location = null;
                                                                                print_Filter.minPrice = null;
                                                                                print_Filter.maxPrice = null;
                                                                                System.out.println("Successfully reset filters");
                                                                                break;

                                                                        default: System.out.println("Invalid option!");
                                                                }
                                                                filter_choice = input.readInt("Choose filtering option: ");
                                                        }
                                                }
                                                // additional filter such that applicant can only view visibility "ON" projects available to their user group (according to marital status)
                                                print_Filter.checkvisibility = true;
                                                //check if applicant is single or married
                                                //singles can only apply for 2-room
                                                if (applicant.get_marital_stat() != true) {
                                                        print_Filter.check2room = true; //turn on filter to check for num of 2 rooms, if no 2 rooms, singles cannot apply for project thus not displayed to them
                                                }
                                                //we only allow married applicants can only apply for 3-rooms for simplicity
                                                if (applicant.get_marital_stat() == true) {
                                                        print_Filter.check3room = true;
                                                }

                                                ProjectManager.viewAllProject(print_Filter);
                                                break;
                                        case 2 :
                                                ApplicationManager.newApplication(applicant);
                                                break;

                                        case 3 : String new_pwd = input.readLine("Enter new password: ");
                                                applicant.change_pwd(app_database, new_pwd);
                                                UserManager.create_object_lists(data_base); //update objects to have new password
                                                return; //prompt relogin after change password

                                        case 4:
                                                return;
                                        default: System.out.println("Invalid choice!");
                                        }

                }
                else if (applicant.get_application().getStatus().equals("Booked")){
                        int choice = input.readInt(options3);
                                switch (choice) {
                                        case 1 :
                                                System.out.println("Set filters? (Y/N)");
                                                char set_filter = Character.toUpperCase(input.readWord().charAt(0));
                                                Filter print_Filter = new Filter();
                                                if (set_filter == 'Y') {
                                                        System.out.println("1. Filter location");
                                                        System.out.println("2. Filter minimum housing price");
                                                        System.out.println("3. Filter maximum housing price");
                                                        System.out.println("4. Reset filters");
                                                        System.out.println("5. Finish selecting filters");

                                                        int filter_choice = input.readInt("Choose filtering option: ");
                                                        while (filter_choice < 5) {
                                                                switch(filter_choice) {
                                                                        case 1:
                                                                                System.out.println("Enter preferred location: ");
                                                                                ProjectManager.printLocations();
                                                                                System.out.println();
                                                                                print_Filter.location = input.readLine();
                                                                                break;
                                                                        case 2:
                                                                                print_Filter.minPrice = input.readInt("Enter preferred minimum housing price: "); //autoboxing from primitive type to wrapper class type
                                                                                break;
                                                                        case 3:
                                                                                print_Filter.maxPrice = input.readInt("Enter preferred maximum housing price: ");
                                                                                break;
                                                                        case 4: print_Filter.location = null;
                                                                                print_Filter.minPrice = null;
                                                                                print_Filter.maxPrice = null;
                                                                                System.out.println("Successfully reset filters");
                                                                                break;

                                                                        default: System.out.println("Invalid option!");
                                                                }
                                                                filter_choice = input.readInt("Choose filtering option: ");
                                                        }
                                                }
                                                // additional filter such that applicant can only view visibility "ON" projects available to their user group (according to marital status)
                                                print_Filter.checkvisibility = true;
                                                //check if applicant is single or married
                                                if (applicant.get_marital_stat() != true) {
                                                        print_Filter.check2room = true; //turn on filter to check for num of 2 rooms, if no 2 rooms, singles cannot apply for project thus not displayed to them
                                                }
                                                ProjectManager.viewAllProject(print_Filter);
                                                break;

                                        case 2 : enquiryInterface.submitEnquiry(applicant, applicant.get_application().getProject());
                                                break;

                                        case 3 : enquiryInterface.editEnquiry(applicant);
                                                break;

                                        case 4 : enquiryInterface.deleteEnquiry(applicant);
                                                break;

                                        case 5 : enquiryInterface.deleteMessage(applicant);
                                                break;

                                        case 6 :enquiryInterface.viewEnquiries(applicant);
                                                break;

                                        case 7 : String new_pwd = input.readLine("Enter new password: ");
                                                applicant.change_pwd(app_database, new_pwd);
                                                UserManager.create_object_lists(data_base); //update objects to have new password
                                                return; //prompt relogin after change password

                                        case 8 : System.out.println("Current application status: " + applicant.get_application().getStatus());
                                                break;

                                        case 9: return;

                                        default: System.out.println("Invalid choice!");
                                }
                }
                else{
                        int choice = input.readInt(options);
                                switch (choice) {
                                        case 1 :
                                                System.out.println("Set filters? (Y/N)");
                                                char set_filter = Character.toUpperCase(input.readWord().charAt(0));
                                                Filter print_Filter = new Filter();
                                                if (set_filter == 'Y') {
                                                        System.out.println("1. Filter location");
                                                        System.out.println("2. Filter minimum housing price");
                                                        System.out.println("3. Filter maximum housing price");
                                                        System.out.println("4. Reset filters");
                                                        System.out.println("5. Finish selecting filters");

                                                        int filter_choice = input.readInt("Choose filtering option: ");
                                                        while (filter_choice < 5) {
                                                                switch(filter_choice) {
                                                                        case 1:
                                                                                System.out.println("Enter preferred location: ");
                                                                                ProjectManager.printLocations();
                                                                                System.out.println();
                                                                                print_Filter.location = input.readLine();
                                                                                break;
                                                                        case 2:
                                                                                print_Filter.minPrice = input.readInt("Enter preferred minimum housing price: "); //autoboxing from primitive type to wrapper class type
                                                                                break;
                                                                        case 3:
                                                                                print_Filter.maxPrice = input.readInt("Enter preferred maximum housing price: ");
                                                                                break;
                                                                        case 4: print_Filter.location = null;
                                                                                print_Filter.minPrice = null;
                                                                                print_Filter.maxPrice = null;
                                                                                System.out.println("Successfully reset filters");
                                                                                break;

                                                                        default: System.out.println("Invalid option!");
                                                                }
                                                                filter_choice = input.readInt("Choose filtering option: ");
                                                        }
                                                }
                                                // additional filter such that applicant can only view visibility "ON" projects available to their user group (according to marital status)
                                                print_Filter.checkvisibility = true;
                                                //check if applicant is single or married
                                                if (applicant.get_marital_stat() != true) {
                                                        print_Filter.check2room = true; //turn on filter to check for num of 2 rooms, if no 2 rooms, singles cannot apply for project thus not displayed to them
                                                }
                                                ProjectManager.viewAllProject(print_Filter);
                                                break;

                                        case 2 : ApplicationManager.requestWithdrawApplication(applicant.get_application());
                                                break;

                                        case 3 : enquiryInterface.submitEnquiry(applicant, applicant.get_application().getProject());
                                                break;

                                        case 4 : enquiryInterface.editEnquiry(applicant);
                                                break;

                                        case 5 : enquiryInterface.deleteEnquiry(applicant);
                                                break;

                                        case 6 : enquiryInterface.deleteMessage(applicant);
                                                break;

                                        case 7 :enquiryInterface.viewEnquiries(applicant);
                                                break;

                                        case 8 : String new_pwd = input.readLine("Enter new password: ");
                                                applicant.change_pwd(app_database, new_pwd);
                                                UserManager.create_object_lists(data_base); //update objects to have new password
                                                return; //prompt relogin after change password

                                        case 9 : System.out.println("Current application status: " + applicant.get_application().getStatus());
                                                break;

                                        case 10: return;

                                        default: System.out.println("Invalid choice!");
                                }
                }
        }
    }
}