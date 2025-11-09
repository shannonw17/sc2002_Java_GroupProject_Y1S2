import java.util.List;
import java.util.HashMap;

/**
 * The {@code User} class serves as a superclass for all user types in the system,
 * including Applicants, HDB Officers, and HDB Managers.
 * <p>
 * It encapsulates common attributes such as name, NRIC, age, marital status, password,
 * and role. It also provides basic user operations like login and password change.
 */
public class User {
    //attributes: role, NRIC, name, marital status, age
    //methods: login, change password --> move login function to outside user class, only implement change password //inherited by all subclass
    private String name; 
    private String nric;
    private int age;
    private boolean marital_stat; //true if married, else false
    private String password;
    private String role;
    
    //constructor with 3 parameters
    //specific role database eg. nric -> [name, age, marital_status, pwd]
    //superclass constructor that will be called when creating Applicant/HDBOfficer/HDBManager class objects
    /**
     * Constructs a {@code User} with only NRIC, password, and role.
     * Used typically during login before full user data is loaded.
     *
     * @param nric the NRIC of the user
     * @param pwd  the password
     * @param role the role of the user (e.g., "applicant", "hdbofficer", "hdbmanager")
     */
    public User(String nric, String pwd, String role) {
        this.name = null;
        this.nric = nric;
        this.age = 0;   
        this.marital_stat = false; //default as false
        this.password = pwd;
        this.role = role;
    }
    /**
     * Constructs a fully initialized {@code User} using details from a role-specific map.
     * Called when creating subclasses such as Applicant, HDBOfficer, or HDBManager.
     *
     * @param nric        the NRIC of the user
     * @param pwd         the password
     * @param role        the role of the user
     * @param correct_map the map containing user details for the specific role
     */
    public User(String nric, String pwd, String role, HashMap<String, List<String>> correct_map) {
        this.name = correct_map.get(nric).get(0);
        this.nric = nric;
        this.age = (int) Double.parseDouble(correct_map.get(nric).get(1));
        this.marital_stat = correct_map.get(nric).get(2).equalsIgnoreCase("married");
        this.password = pwd;
        this.role = role;
    }

    //getter methods
    /**
     * @return the user's name
     */
    public String get_name(){return name;}
    /**
     * @return the user's NRIC
     */
    public String get_nric() {return nric;};
    /**
     * @return the user's age
     */
    public int get_age() {return age;}
    /**
     * @return {@code true} if the user is married, {@code false} otherwise
     */
    public boolean get_marital_stat() {return marital_stat;} //true if married, else false
    /**
     * @return the user's password
     */
    public String get_password() {return password;}
    /**
     * @return the user's role
     */
    public String get_role() {return role;}
    /**
     * Verifies the login credentials against the provided dataset.
     *
     * @param all_data a combined map of all user roles with their respective data
     * @return {@code true} if the credentials are correct, {@code false} otherwise
     */
    public boolean login(HashMap<String, HashMap<String, List<String>>> all_data) {
        //check if NRIC is present in specified role's database (3 separate datebases for Applicant, HDBOfficer, HDBManager)
        //compare role attribute and data_base keys --> access correct userdatabase hashtable in data_base 
        HashMap<String, List<String>> specific_map = UserDatabase.chooseHashmap(all_data, this.role);
        if (specific_map != null) {
            //for (int i=1; i <= specific_map.size(); i++) { //for loop according to size of hashmap
                //if (specific_map.get(i).get(1).equalsIgnoreCase(this.nric)) //compare 2nd element of hashmap value array with nric; not case-sensitive
            if (specific_map.containsKey(this.nric)) {        
                if (specific_map.get(this.nric).get(3).equals(this.password)) //compare 4th element of hashmap value array with password; case-sensitive
                        return true; 
            }
        }
        System.out.println("NRIC Username/Password is incorrect."); //not found
        return false;
    }

    /**
     * Changes the password of the user and updates it in the provided user data map.
     * This should be called only after successful login.
     *
     * @param exact_map the specific map (role-based) where the user data is stored
     * @param new_pwd   the new password to be set
     */
    public void change_pwd(HashMap<String, List<String>> exact_map, String new_pwd) { //setter method for password + update database
        this.password = new_pwd;
        //update database - check nric and pwd again to go to specifc database hashtable key and access value
        exact_map.get(this.nric).set(3, new_pwd); //make sure in Main class, can only change password if user login is successful
        System.out.println("Password changed successfully!");
    }
}
