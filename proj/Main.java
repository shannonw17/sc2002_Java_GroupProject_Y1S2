import java.util.*;

/**
 * The {@code Main} class serves as the entry point for the HDB application management system.
 * <p>
 * It performs the following tasks:
 * <ul>
 *     <li>Loads user and project data from Excel files.</li>
 *     <li>Initializes the system's user and project managers.</li>
 *     <li>Enters a login loop that allows different users (Applicant, HDBOfficer, HDBManager) to log in.</li>
 *     <li>Redirects logged-in users to their respective dashboards.</li>
 * </ul>
 */
public class Main {
    /**
     * The main method initializes the application, loads data, and provides a login interface for users.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Load all user data from Excel files
        HashMap<String, List<String>> applicant_data = UserDatabase.excelToHashmap("proj/ApplicantList.xlsx");
        HashMap<String, List<String>> officer_data = UserDatabase.excelToHashmap("proj/OfficerList.xlsx");
        HashMap<String, List<String>> manager_data = UserDatabase.excelToHashmap("proj/ManagerList.xlsx");
        // Combine all user data into one unified database
        HashMap<String, HashMap<String, List<String>>> data_base = UserDatabase.combinedHashmap(applicant_data, officer_data, manager_data);
        UserManager.create_object_lists(data_base);
        //jump to line 95 for login

        // Load project data
        ProjectDatabase.excelToHashmap("proj/ProjectList.xlsx");

        // === L O G I N ===
        // system will keep running to allow for different logins until "endprogram" --> this ends the entire system
        while(true) {
            Input input = new Input();
            System.out.println("Enter role (Applicant, HDBOfficer, HDBManager) or type endprogram to exit program");
            String user_role = input.readLine().toLowerCase();

            while (!(user_role.equals("applicant") || user_role.equals("hdbofficer") || user_role.equals("hdbmanager")||user_role.equals("endprogram"))) {
                System.out.println("Role does not exist");
                user_role = input.readLine().toLowerCase();
            }
            if (user_role.equals("endprogram")) {
                break;
            }
            System.out.println("type in change to reselect your role");
            while (true) {
                System.out.println("Enter NRIC: ");
                String user_nric = input.readLine().toUpperCase();
                if (user_nric.equals("CHANGE"))
                    break;

                System.out.println("Enter password (case-sensitive): ");
                String user_pwd = input.readLine();

                User temp = new User(user_nric, user_pwd, user_role);
                boolean success = temp.login(data_base);

                if (success) {
                    switch (user_role) { //get object from existing arraylists of objects of UserManager class
                        case "applicant":
                            Applicant user_app = null;
                            for (Applicant appl : UserManager.all_applicants) {
                                if (appl.get_nric().equals(user_nric)) {
                                    user_app = appl;
                                }
                            }
                            ApplicantDisplay.start(user_app, applicant_data, data_base); // goes to respective dashboards
                            break;
                        case "hdbofficer":
                            HDBOfficer user_off = null;
                            for (HDBOfficer offi : UserManager.all_officers) {
                                if (offi.get_nric().equals(user_nric)) {
                                    user_off = offi;
                                }
                            }
                            HDBOfficerDisplay.start(user_off, officer_data, data_base);
                            break;
                        case "hdbmanager":
                            HDBManager user_man = null;
                            for (HDBManager mana : UserManager.all_managers) {
                                if (mana.get_nric().equals(user_nric)) {
                                    user_man = mana;
                                }
                            }
                            HDBManagerDisplay.start(user_man, manager_data, data_base);
                            break;
                    }
                    break;
                }
            }
        }
        System.out.println("Exiting program...");
    }
}


