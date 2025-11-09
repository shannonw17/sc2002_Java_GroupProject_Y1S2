/**
 * Interface defining the operations for managing enquiries within the HDB system.
 * This includes functionalities for submitting, editing, deleting, replying to,
 * and viewing enquiries by various user roles such as Applicant, HDBOfficer, and HDBManager.
 */
public interface EnquiryInterface {
    /**
     * Submits a new enquiry made by the specified applicant for a given project.
     *
     * @param applicant the applicant who is submitting the enquiry
     * @param project the project the enquiry is related to
     */
	void submitEnquiry(Applicant applicant, Project project);
    /**
     * Edits an existing enquiry submitted by the specified applicant.
     *
     * @param applicant the applicant who wishes to edit their enquiry
     */
    void editEnquiry(Applicant applicant);
    /**
     * Deletes an existing enquiry submitted by the specified applicant.
     *
     * @param applicant the applicant who wishes to delete their enquiry
     */
    void deleteEnquiry(Applicant applicant);
    /**
     * Deletes a specific message from an enquiry submitted by the applicant.
     *
     * @param applicant the applicant requesting to delete a message
     */
    void deleteMessage(Applicant applicant);
    /**
     * Allows an HDB Officer to view and reply to submitted enquiries.
     *
     * @param officer the HDB officer handling enquiry replies
     */
    void replyEnquiry(HDBOfficer officer);
    /**
     * Allows an HDB Manager to view and reply to submitted enquiries.
     *
     * @param manager the HDB manager handling enquiry replies
     */
    void replyEnquiry(HDBManager manager);
    /**
     * Displays the list of enquiries made by the applicant.
     *
     * @param applicant the applicant viewing their enquiries
     */
    void viewEnquiries(Applicant applicant);
     /**
     * Displays the list of all enquiries for an HDB Officer to view.
     *
     * @param officer the HDB officer viewing the enquiries
     */
    void viewEnquiries(HDBOfficer officer);
    /**
     * Displays the list of all enquiries for an HDB Manager to view.
     *
     * @param manager the HDB manager viewing the enquiries
     */
    void viewEnquiries(HDBManager manager);

}
