/**
 * Represents a BTO application submitted by an {@link Applicant} for a {@link Project}.
 * Stores information about the applicant, the selected project, room type, and application status.
 */
public class Application {
	private Applicant applicant;
	private Project project;
	private String status; //Pending, Successful, Unsuccessful, or Booked
	private String roomtype; //will set to "two" or "three" depending on applicant's marital status
	
	/**
     * Constructs an Application with the specified applicant, project, and room type.
     * The application is initially set to "Pending".
     *
     * @param applicant the applicant submitting the application
     * @param project the project the applicant is applying for
     * @param num_room the type of room requested ("two" or "three")
     */
	public Application(Applicant applicant,Project project, String num_room) {
		this.applicant=applicant;
		this.project=project;
		this.status="Pending";
		this.roomtype=num_room;
		
	}
	/**
     * Gets the project associated with this application.
     *
     * @return the project
     */
	public Project getProject() {
		return project;
	}
	/**
     * Gets the applicant who submitted this application.
     *
     * @return the applicant
     */
	public Applicant getApplicant() {
		return applicant;
	}
	/**
     * Gets the current status of the application.
     *
     * @return the application status
     */
	public String getStatus() {
		return status;
	}
	/**
     * Gets the type of room applied for.
     *
     * @return the room type ("two" or "three")
     */
	public String getRoomType() {
		return roomtype;
	}
	
	/**
     * Sets the room type for this application.
     *
     * @param flat the new room type ("two" or "three")
     */
	public void setFlat(String flat) {
		this.roomtype=flat;
	}
	/**
     * Sets the current status of the application.
     *
     * @param status the new application status ("Pending", "Successful", "Unsuccessful", "Booked")
     */
	public void setStatus(String status) {
		this.status=status;
	}
	
	
}
