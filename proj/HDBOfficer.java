import java.util.HashMap;
import java.util.List;

/**
 * The HDBOfficer class represents an officer who is responsible for managing a specific project
 * and tracking the status of their application (successful or failed).
 * This class extends the Applicant class and adds additional functionality for managing
 * the project they are in charge of and their application status.
 */
public class HDBOfficer extends Applicant{
	
	private Project project_in_charge;
	private String status; //Successful or failed, null if never apply any

    /**
     * Constructs a new HDBOfficer with the specified NRIC, password, role, and a map of correct data.
     * The officer starts without being assigned a project and their application status is null.
     *
     * @param nric The NRIC of the officer.
     * @param pwd The password for the officer's account.
     * @param role The role of the officer (should be "HDBOfficer").
     * @param correct_map A map containing correct data for validation.
     */
	public HDBOfficer(String nric, String pwd, String role, HashMap<String, List<String>> correct_map) {
		super(nric,pwd,role,correct_map);
		this.project_in_charge=null;
		this.status=null;
		
	}
	
    /**
     * Gets the project that the officer is currently in charge of.
     *
     * @return The project that the officer is responsible for, or null if not assigned.
     */
	public Project getProjectInCharge(){
		return project_in_charge;
	}
	public String getStatus() {
		return status;
	}

	
	public void setProjectInCharge(Project project) {
		this.project_in_charge=project;
	}
	public void setStatus(String s) {
		this.status=s;
	}
	
	
}
