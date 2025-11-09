import java.util.HashMap;
import java.util.List;
/**
 * Represents an applicant in the housing application system.
 * Inherits from the {@link User} class and contains additional information
 * related to BTO application and enquiry.
 */
public class Applicant extends User{
    /** The BTO application submitted by the applicant */
    private Application applyBTO; 

    /** The enquiry made by the applicant */
    private Enquiry qns;

    /** The type of flat (e.g., two-room, three-room) the applicant has booked */
    private String typeOf_flat; //initially null, set as two/three AFTER successfully booked
    /**
     * Constructs an {@code Applicant} with the specified NRIC, password, role,
     * and a correctness mapping.
     *
     * @param nric the NRIC of the applicant
     * @param pwd the password for the applicant
     * @param role the role of the user (should be "Applicant")
     * @param correct_map a map used for correctness checking in the application
     */
    
    public Applicant(String nric, String pwd, String role, HashMap<String, List<String>> correct_map) {
        super(nric, pwd, role, correct_map);
        this.applyBTO = null;
        this.qns = null;
        this.typeOf_flat = null;
    }

    //setter methods
    /**
     * Sets the BTO application for the applicant.
     *
     * @param app the {@link Application} object to assign
     */
    public void set_application(Application app) {this.applyBTO = app;}
    /**
     * Sets the enquiry made by the applicant.
     *
     * @param enq the {@link Enquiry} object to assign
     */
    public void set_enquiry(Enquiry enq) {this.qns = enq;}
    /**
     * Sets the type of flat the applicant has booked.
     *
     * @param typeFlat the flat type (e.g., "two", "three")
     */
    public void set_typeOf_flat(String typeFlat) {this.typeOf_flat = typeFlat;}

    //getter methods
    /**
     * Returns the BTO application of the applicant.
     *
     * @return the {@link Application} object
     */
    public Application get_application() {return applyBTO;}
    /**
     * Returns the enquiry submitted by the applicant.
     *
     * @return the {@link Enquiry} object
     */
    public Enquiry get_enquiry() {return qns;}
     /**
     * Returns the type of flat the applicant has booked.
     *
     * @return the flat type as a string
     */
    public String get_typeOf_flat() {return typeOf_flat;}
     /**
     * Generates and prints a receipt containing applicant details and
     * the type of flat applied for. Called from {@code ApplicationManager.printReceipt()}.
     */
    public void generate_receipt() { //will be used in printReceipt() in ApplicationManager
        System.out.println("Name: " + get_name() +", NRIC: " + get_nric() + ", Age: " + get_age() + ", Marital Status: " + get_marital_stat() + ", Flat Type: " + applyBTO.getRoomType() + "-room");
    }
}
