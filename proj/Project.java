import java.time.LocalDate;
import java.util.ArrayList; 
/**
 * Represents a public housing project managed by HDB.
 * Contains attributes related to housing types, availability, pricing, and application management.
 */
public class Project {
    //attributes: name, neighbourhood, numof2room, price2room, numof3room, price3room, opening date, closing date, manager, officer[slot_num], pending[999]
    //methods: setter and getter
    private String title;
    private String neighbourhood;
    private int numof2room;
    private int numof2room_success;
    private int price2room; 
    private int numof3room;
    private int numof3room_success;
    private int price3room;
    private LocalDate opening_date; // LocalDate openingDate = LocalDate.of(2025, 3, 29);  // YYYY, MM, DD
    private LocalDate closing_date;
    private boolean visibility; //update according to opening_date and closing_date, can use setter to update manually also
    private HDBManager managerIC;
    private int numOfOfficerSlots;
    private ArrayList<HDBOfficer> officerList;
    private ArrayList<HDBOfficer> pendingList;
    //list of pending application --> all submitted applications
    private ArrayList<Application> submissions;
    //list of withdrawal request --> application objects --> add application object to list, then if approve withdrawal, delete application object from both withdraw list and pending list; if reject withdrawl, just delete application object from withdrawal list
    private ArrayList<Application> withdrawals;
    //list of succesful application --> if approve, moce application object from pending to successful list, else if reject, delete from pending list
    private ArrayList<Application> successful;
     /**
     * Constructs a new Project with the specified attributes.
     *
     * @param title Title of the project.
     * @param neighbourhood Neighbourhood where the project is located.
     * @param numof2room Number of 2-room flats available.
     * @param price2room Price of a 2-room flat.
     * @param numof3room Number of 3-room flats available.
     * @param price3room Price of a 3-room flat.
     * @param opening_date Opening date for applications.
     * @param closing_date Closing date for applications.
     * @param managerIC HDBManager in charge of the project.
     * @param numOfOfficerSlots Number of officers allowed to manage the project.
     */
    public Project(String title, String neighbourhood, int numof2room, int price2room, int numof3room, int price3room, LocalDate opening_date, LocalDate closing_date, HDBManager managerIC, int numOfOfficerSlots) {
        this.title = title;
        this.neighbourhood = neighbourhood;
        this.numof2room = numof2room;
        this.numof2room_success = 0;
        this.price2room = price2room;
        this.numof3room = numof3room;
        this.numof3room_success = 0;
        this.price3room = price3room;
        this.opening_date = opening_date;
        this.closing_date = closing_date;
        updateVisibility(); 
        this.managerIC = managerIC;
        //not sure if this is the right way to intialise array of objects in constructors...
        this.officerList = new ArrayList<HDBOfficer>();
        this.pendingList = new ArrayList<HDBOfficer>();
        this.submissions = new ArrayList<Application>();
        this.withdrawals = new ArrayList<Application>();
        this.successful  = new ArrayList<Application>();
        this.numOfOfficerSlots = numOfOfficerSlots;
    }

    //setters
    public void set_title(String title) {this.title = title;}
    public void set_neighbourhood(String neighbourhood) {this.neighbourhood = neighbourhood;}
    public void set_numof2room(int numof2room) {this.numof2room = numof2room;}
    public void set_numof2room_success(int numof2room_success) {this.numof2room_success = numof2room_success;}
    public void set_price2room(int price2room) {this.price2room = price2room;}
    public void set_numof3room(int numof3room) {this.numof3room = numof3room;}
    public void set_numof3room_success(int numof3room_success) {this.numof3room_success = numof3room_success;}
    public void set_price3room(int price3room) {this.price3room = price3room;}
    public void set_opening_date(LocalDate opening_date) {this.opening_date = opening_date;}
    public void set_closing_date(LocalDate closing_date) {this.closing_date = closing_date;}
    //allow manual update of visibility if not auto on/off from timeline
    public void set_visibility(boolean visibility) {this.visibility = visibility;}
    public void set_managerIC(HDBManager managerIC) {this.managerIC = managerIC;}
    public void set_numOfOfficerSlots(int numOfOfficerSlots) {this.numOfOfficerSlots = numOfOfficerSlots;}
    public void set_officerList(ArrayList<HDBOfficer> officerList) {this.officerList = officerList;}
    public void set_pendingList(ArrayList<HDBOfficer> pendingList) {this.pendingList = pendingList;}
    public void set_submissions(ArrayList<Application> submissions) {this.submissions = submissions;}
    public void set_withdrawals(ArrayList<Application> withdrawals) {this.withdrawals = withdrawals;}
    public void set_successful(ArrayList<Application> successful) {this.successful = successful;}

    //getters
    public String get_title() {return title;}
    public String get_neighbourhood() {return neighbourhood;}
    public int get_numof2room() {return numof2room;}
    public int get_numof2room_success() {return numof2room_success;}
    public int get_price2room() {return price2room;}
    public int get_numof3room() {return numof3room;}
    public int get_numof3room_success() {return numof3room_success;}
    public int get_price3room() {return price3room;}
    public LocalDate get_opening_date() {return opening_date;}
    public LocalDate get_closing_date() {return closing_date;}
    public boolean get_visibility() {return visibility;}
    public HDBManager get_managerIC() {return managerIC;}
    public int get_numOfOfficerSlots() {return numOfOfficerSlots;}
    public ArrayList<HDBOfficer> get_officerList() {return officerList;}
    public ArrayList<HDBOfficer> get_pendingList() {return pendingList;}
    public ArrayList<Application> get_submissions() {return submissions;}
    public ArrayList<Application> get_withdrawals() {return withdrawals;}
    public ArrayList<Application> get_successful() {return successful;}   

    //automatically updating visibility based on opening_date and closing_date
    /**
     * Updates the visibility status based on current date, opening date, and closing date.
     * Visibility is true only if today is within the opening and closing dates.
     */
    public void updateVisibility() {
        LocalDate today = LocalDate.now();
        this.visibility = (today.isEqual(opening_date) || today.isAfter(opening_date)) && (today.isEqual(closing_date)||today.isBefore(closing_date));
    }
    /**
     * Adds an application to the withdrawal list.
     *
     * @param a The application to be added for withdrawal processing.
     */
    public void addWithdrawApplication(Application a) {
        withdrawals.add(a);
    }
    //when calling constructor in Main class or project manager class
    // .. = new Constructor(... LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 30)...)

    
}

