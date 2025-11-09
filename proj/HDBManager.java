
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Represents an HDB Manager in the BTO booking system.
 * <p>
 * This class extends {@link User} and manages multiple {@link Project} instances.
 * Each HDB Manager can handle only one active project at a time and can generate
 * reports on successful applicants using customizable filters.
 */
public class HDBManager extends User{
    /** A list of all projects created or managed by this HDBManager. */
    private ArrayList<Project> projs; //list of those created by this.HDBManager
    /** The currently active project managed by this HDBManager. */
    private Project activeProject; //HDBManager can only handle 1 project at the time
    /**
     * Constructs a new {@code HDBManager} object using data from the provided user data map.
     *
     * @param nric         the NRIC of the manager
     * @param pwd          the password of the manager
     * @param role         the role (should be "hdbmanager")
     * @param correct_map  the HashMap containing NRIC keys mapped to user details
     */
    public HDBManager(String nric, String pwd, String role, HashMap<String, List<String>> correct_map) {
        super(nric, pwd, role, correct_map);
        this.projs = new ArrayList<>();
        this.activeProject = null;
    }
    /**
     * Adds a project to this manager's list of managed projects.
     *
     * @param p the {@code Project} to be added
     */
    public void addToProjList(Project p) {
        projs.add(p);
    }
    /**
     * Returns the project currently being actively managed.
     *
     * @return the active {@code Project}
     */
    public Project getProject() {
        return activeProject;
    }

    //setter method for Project array;
    /**
     * Sets the currently active project for this manager.
     *
     * @param proj the {@code Project} to set as active
     */
    public void setProject(Project proj) {
        activeProject = proj;
    }
    /**
     * Returns the list of all projects this manager has created or managed.
     *
     * @return an {@code ArrayList} of {@code Project} objects
     */
    public ArrayList<Project> getProjList() {return projs;}

    /**
     * Removes a project from this manager's list of projects.
     *
     * @param p the {@code Project} to be removed
     */
    public void delProjListItem(Project p) {
        projs.remove(p);
    }

    //generate report of list of applicant with respecitve flat booking + filters
        //filters to generate list based on various categories --> eg. print only married couples with type of flat
        //print flat type, project name, age, marital status

    /**
     * Generates and prints a report of successful applicants from all managed projects.
     * The report output is filtered according to the conditions specified in the given {@link Filter}.
     * <p>
     * The report can include filters for age range, marital status, flat type, and can selectively
     * display various details like project name, flat type, age, and marital status.
     *
     * @param filter a {@code Filter} object specifying which applicants to include and what information to display
     */
    public void generate_report(Filter filter) {
        //for each proj in projs
        for (Project proj : projs) {
            for (Application app : proj.get_successful()) {
                Applicant applicant = app.getApplicant();
                boolean match = true;
                if (filter.flatType != null && !app.getRoomType().equalsIgnoreCase(filter.flatType)) match = false;
                if (filter.filter_marital != null && !(applicant.get_marital_stat()==(filter.filter_marital=="married"))) match = false;
                if (filter.minAge != null && applicant.get_age()<filter.minAge) match = false;
                if (filter.maxAge != null && applicant.get_age()>filter.maxAge) match = false;

                if (match) {
                    if (filter.showProjectName) {
                        System.out.print("Project name: " + proj.get_title() +"; ");
                    }
                    if (filter.showFlatType) {
                        System.out.print("Flat type: " + app.getRoomType() + "-room; "); //to check getter method name in Application class
                    }
                    if (filter.showMarital) {
                        System.out.print("Marital Status: ");
                        if (applicant.get_marital_stat())
                            System.out.print("Married; ");
                        else
                            System.out.print("Single; ");
                    }
                    if (filter.showAge) {
                        System.out.println("Age: " + applicant.get_age() + "; ");
                    }
                }
                System.out.println();
            }
            System.out.println();

        }
        //in Main.java --> Filter filter = new Filter();
        //specify and set filters ie. filter.element = ...
        //have an option on whether want to set filters or not; else can skip setting filters and print default report (have all elements)
    }
}

