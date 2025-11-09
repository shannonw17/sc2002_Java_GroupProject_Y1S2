/**
 * Represents a set of filtering options used for generating reports, 
 * viewing project details, or controlling visibility in the HDB system.
 */
public class Filter {
    /**
     * Flat type filter. Used to filter applications or projects by flat type.
     * Expected values are "two" or "three", corresponding to 2-room and 3-room flats.
     */
    String flatType; //"two" or "three" --> Application attribute is String type_of_room
    /**
     * Marital status filter. If set (e.g., "single" or "married"),
     * only applications or users with the matching marital status are included.
     */
    String filter_marital; //include in report if eg. "single", all singles included, not filtered away
    /**
     * Minimum age filter. Only applicants with age above this value are included.
     * Uses Integer wrapper to allow null (no minimum age specified).
     */
    Integer minAge; //include in report if above minAge
    /**
     * Maximum age filter. Only applicants with age below this value are included.
     * Uses Integer wrapper to allow null (no maximum age specified).
     */
    Integer maxAge; //include in report if below maxAge
    //use wrapper class so can set as null to check if there is any specified value set to filter

    //HDBManager report filters --> initially all true to allow printing of all details if no filters are set
    /**
     * Controls whether flat type information should be shown in reports.
     * Default is true (show all).
     */
    boolean showFlatType = true;
    /**
     * Controls whether project name should be shown in reports.
     * Default is true (show all).
     */
    boolean showProjectName = true;
    /**
     * Controls whether applicant age should be shown in reports.
     * Default is true (show all).
     */
    boolean showAge = true;
    /**
     * Controls whether applicant marital status should be shown in reports.
     * Default is true (show all).
     */
    boolean showMarital = true;
    
    //User (+subclasses) view project details filters
    /**
     * Location filter for projects. Typically refers to the neighborhood.
     */
    String location; //neighbourhood
    /**
     * Minimum price filter. Only projects with a price >= minPrice are shown.
     */
    Integer minPrice;
    /**
     * Maximum price filter. Only projects with a price <= maxPrice are shown.
     */
    Integer maxPrice;
     /**
     * Whether to include only projects that have 2-room flats.
     * Automatically enabled for single applicants.
     */
    boolean check2room = false; //auto-set filter to true for singles, if project does not have 2-rooms then not shown to singles
    /**
     * Whether to include only projects that have 3-room flats.
     * Automatically enabled for married applicants.
     */
    boolean check3room = false; //auto-set filter to true for married, if project does not have 3-rooms then not shown to married
    /**
     * Controls whether project visibility should be taken into account.
     * Enabled for applicants to prevent showing hidden projects.
     */
    boolean checkvisibility = false; //turn on for applicants only
    //check for each Project and print name if pass filters

    //ProjectManager - viewOwnProject()
    /**
     * Controls whether project visibility is shown in manager views.
     * Default is true (visibility shown).
     */
    boolean showVisibility = true;
    /**
     * Controls whether project location is shown in manager views.
     * Default is true (location shown).
     */
    boolean showLocation = true;
    /**
     * If true, shows both old and upcoming projects including those with visibility off.
     */
    boolean check_old_upcoming = false; //see projects with visibility off

    //HDBOfficer - cannot apply for projects they are in charge of
    /**
     * Officer's own project IC. Used to prevent officers from viewing or applying to their own project.
     */
    String myproj_ic;
    //in Main.java --> can choose which filters to apply or specify
}
