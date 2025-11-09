import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * The {@code UserManager} class manages collections of all user objects (Applicants, HDB Officers,
 * and HDB Managers) loaded from external Excel data sources. It serves as the central point
 * for storing instantiated user objects that interact with the BTO booking system.
 * <p>
 * The user objects are created based on the parsed data from Excel files and are accessible
 * via the static lists provided.
 */
public class UserManager {
    //stores list of objects from excel sheets as login creates particular object/user using the BTO booking system; this database allows our system functions to be used
    
    /**
     * List storing all {@code Applicant} objects created from the Excel database.
     */
    public static ArrayList<Applicant> all_applicants = new ArrayList<>();
    /**
     * List storing all {@code HDBOfficer} objects created from the Excel database.
     */
    public static ArrayList<HDBOfficer> all_officers = new ArrayList<>();
    /**
     * List storing all {@code HDBManager} objects created from the Excel database.
     */
    public static ArrayList<HDBManager> all_managers = new ArrayList<>();

    /**
     * Creates user objects (Applicant, HDBOfficer, HDBManager) from the provided nested user data map,
     * and stores them in the corresponding lists.
     * <p>
     * Each user is created using their NRIC and password from the Excel sheet, along with their full
     * role-specific detail map. This enables login functionality and system role-based access later.
     *
     * @param data_base a nested {@code HashMap} where each key is a user role
     *                  ("applicant", "hdbofficer", "hdbmanager") and the value is another map
     *                  that maps NRICs to user details
     */
    public static void create_object_lists(HashMap<String, HashMap<String, List<String>>> data_base) {
        HashMap<String, List<String>> applicant_data =  UserDatabase.chooseHashmap(data_base, "applicant");
        HashMap<String, List<String>> officer_data = UserDatabase.chooseHashmap(data_base, "hdbofficer");
        HashMap<String, List<String>> manager_data = UserDatabase.chooseHashmap(data_base, "hdbmanager");

        for (String a_key: applicant_data.keySet()) {
            String a_pwd = applicant_data.get(a_key).get(3);
            Applicant a_obj = new Applicant(a_key, a_pwd, "applicant", applicant_data);
            all_applicants.add(a_obj);
        }

        for (String o_key: officer_data.keySet()) {
            String o_pwd = officer_data.get(o_key).get(3);
            HDBOfficer o_obj = new HDBOfficer(o_key, o_pwd, "hdbofficer", officer_data);
            all_officers.add(o_obj);
        }

        for (String m_key: manager_data.keySet()) {
            String m_pwd = manager_data.get(m_key).get(3);
            HDBManager m_obj = new HDBManager(m_key, m_pwd, "hdbmanager", manager_data);
            all_managers.add(m_obj);
        }
    }
}
