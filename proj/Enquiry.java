import java.util.ArrayList;
import java.util.List;
/**
 * Represents an enquiry made by an {@link Applicant} regarding a {@link Project}.
 * Each enquiry can contain a list of messages and corresponding replies.
 */
public class Enquiry {
	private Applicant sender;
	private List<String[]> messageReplyPairs;
	private Project project;
	 /**
     * Constructs a new Enquiry with the specified applicant and project.
     * Initializes an empty list of message-reply pairs.
     *
     * @param applicant the applicant who sends the enquiry
     * @param pro the project that the enquiry is about
     */
	public Enquiry(Applicant applicant,Project pro) {
		this.sender=applicant;
		this.messageReplyPairs = new ArrayList<>();
		this.project=pro;
	}
	/**
     * Returns the list of message-reply pairs.
     * Each element is a String array of length 2: [0] = message, [1] = reply.
     *
     * @return the list of message-reply pairs
     */
	 public List<String[]> getMessageReplyPairs() {
	        return messageReplyPairs;
	    }
		/**
     * Retrieves the message at a specified index.
     *
     * @param index the index of the message
     * @return the message if index is valid, otherwise "Invalid index"
     */
    public String getMessage(int index) {
        if (index >= 0 && index < messageReplyPairs.size()) {
            return messageReplyPairs.get(index)[0];
        }
        return "Invalid index";
    }
    /**
     * Retrieves the reply corresponding to the message at a specified index.
     *
     * @param index the index of the reply
     * @return the reply if index is valid, otherwise "Invalid index"
     */
    public String getReply(int index) {
        if (index >= 0 && index < messageReplyPairs.size()) {
            return messageReplyPairs.get(index)[1];
        }
        return "Invalid index";
    }
	/**
     * Returns the project associated with this enquiry.
     *
     * @return the project
     */
	public Project getProject() {
		return project;
	}
	/**
     * Returns the applicant who sent this enquiry.
     *
     * @return the sender
     */
	public Applicant getSender() {
		return sender;
	}
	
	/**
     * Adds a new message to the enquiry.
     * The reply is initialized to {@code null}.
     *
     * @param message the message to add
     */
	public void addMessage(String message) {
	        this.messageReplyPairs.add(new String[]{message, null});
	}
	/**
     * Sets the reply for a specific message in the enquiry.
     *
     * @param index the index of the message to reply to
     * @param reply the reply to be set
     */
	public void setReply(int index, String reply) {
	        if (index >= 0 && index < messageReplyPairs.size()) {
	            messageReplyPairs.get(index)[1] = reply;
	        }
	}
	/**
     * Sets the sender of the enquiry.
     *
     * @param applicant the new sender
     */
	public void setSender(Applicant applicant){
		this.sender=applicant;
	}
	/**
     * Sets the project associated with the enquiry.
     *
     * @param pro the new project
     */
	public void setProject(Project pro){
		this.project=pro;
	}
	
	
}
