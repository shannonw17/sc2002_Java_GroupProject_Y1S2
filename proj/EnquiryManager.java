import java.util.ArrayList;
import java.util.List;

/**
 * Manages all enquiry-related operations within the HDB system.
 * Implements the EnquiryInterface to allow Applicants, HDB Officers,
 * and HDB Managers to interact with enquiries.
 */
public class EnquiryManager implements EnquiryInterface{
    private static List<Enquiry> enquiries = new ArrayList<>();
    Input input=new Input();

    /**
     * Submits a new enquiry for a given applicant and project.
     * @param applicant the applicant submitting the enquiry
     * @param project the project the enquiry is about
     */
    public void submitEnquiry(Applicant applicant,Project project) {
        if (applicant.get_application()==null){
            System.out.println("Error");
            return;
        }
    
        if (applicant.get_enquiry()==null) {
    
            Enquiry enquiry = new Enquiry(applicant, project);
            String message= input.readLine("Enter your enquiry message:");
            enquiry.addMessage(message);
    
            applicant.set_enquiry(enquiry);
            if (enquiries==null){
                ArrayList<Enquiry> enquiries=new ArrayList<>();
            }
            enquiries.add(enquiry);
            System.out.println("Enquiry submitted!");
        }
        else {
            Enquiry enquiry = applicant.get_enquiry();
    
            String message = input.readLine("Enter your enquiry message:");
            enquiry.addMessage(message);
    
            applicant.set_enquiry(enquiry);
            if (enquiries == null) {
                ArrayList<Enquiry> enquiries = new ArrayList<>();
            }
    
            System.out.println("Enquiry submitted!");
        }
    }

    /**
     * Allows the applicant to edit one of their submitted messages.
     * @param applicant the applicant editing their enquiry
     */
    public void editEnquiry(Applicant applicant) {

        if(applicant.get_enquiry()==null){
            System.out.println("No enquiry found.");
            return;
        }



        Enquiry applicantEnquiries=new Enquiry(null,null);
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getSender().get_nric().equals(applicant.get_nric())) {
                applicantEnquiries=enquiry;
                break;
            }
        }

        if (applicantEnquiries.getSender()==null) {
            System.out.println("No enquiries found for this applicant.");
            return;
        }
        System.out.println("Here are all the messages you have submitted:");
        for (int j = 0; j < applicantEnquiries.getMessageReplyPairs().size(); j++) {
            System.out.println("Message " + (j+1)+ ": " + applicantEnquiries.getMessage(j));
            System.out.println("Reply   " + (j+1) + ": " + (applicantEnquiries.getReply(j) == null ? "No reply yet" : applicantEnquiries.getReply(j)));
        }


        int Index = input.readInt("Select an message number to edit (1 - " + applicantEnquiries.getMessageReplyPairs().size() + "):") - 1;

        if (Index < 0 || Index >= applicantEnquiries.getMessageReplyPairs().size()) {
            System.out.println("Invalid selection.");
            return;
        }



        String newMessage = input.readLine("Enter new message:");
        applicantEnquiries.getMessageReplyPairs().get(Index)[0] = newMessage;

        System.out.println("Message edited successfully!");

    }

    /**
     * Deletes all enquiries from the specified applicant.
     * @param applicant the applicant whose enquiries will be deleted
     */
    public void deleteEnquiry(Applicant applicant) {

        boolean deleted = false;
        for (int i = 0; i < enquiries.size(); i++) {
            if (enquiries.get(i).getSender().equals(applicant)) {
                enquiries.remove(i);
                i--;
                deleted = true;
                applicant.set_enquiry(null);
            }
        }
        if (deleted) {
            System.out.println("All enquiries from the applicant have been deleted.");
        }
        else {
            System.out.println("No enquiries found for this applicant.");
        }
    }
    /**
     * Deletes a specific message from an applicant's enquiry.
     * @param applicant the applicant deleting a message
     */
    public void deleteMessage(Applicant applicant) {
        if(applicant.get_enquiry()==null){
            System.out.println("No enquiries found for this applicant");
            return;
        }

        Enquiry applicantEnquiries = new Enquiry(null, null);
        for (int i=0;i<enquiries.size();i++) {
            if(enquiries.get(i).getSender().equals(applicant)) {
                applicantEnquiries=enquiries.get(i);
            }
        }
        System.out.println("Here are all the messages you have submitted:");
        for (int j = 0; j < applicantEnquiries.getMessageReplyPairs().size(); j++) {
            System.out.println("Message " + (j+1) + ": " + applicantEnquiries.getMessage(j));
            System.out.println("Reply   " + (j+1) + ": " + (applicantEnquiries.getReply(j) == null ? "No reply yet" : applicantEnquiries.getReply(j)));
        }

        int index=input.readInt("Choose the message you want to delete:") -1;
        if (index<0||index>=applicantEnquiries.getMessageReplyPairs().size()) {
            System.out.println("Invalid selection.");
            return;
        }
        applicantEnquiries.getMessageReplyPairs().remove(index);
        System.out.println("Message successfully deleted!");
        if (applicantEnquiries.getMessageReplyPairs().isEmpty()){
            applicant.set_enquiry(null);
        }

    }
    /**
     * Allows an HDB Manager to reply to enquiries related to their projects.
     * @param manager the manager replying to enquiries
     */
    public void replyEnquiry(HDBManager manager) {



        List<Enquiry> projectEnquiries = new ArrayList<>();
        for (Enquiry e : enquiries) {
            ArrayList<Project> p=manager.getProjList();
            for (Project project : p) {
                if(project.equals(e.getProject())) {
                    projectEnquiries.add(e);
                }
            }

        }
        if (projectEnquiries.isEmpty()) {
            System.out.println("No enquiries for your project.");
            return;
        }

        System.out.println("Available applicants:");
        for (int i = 0; i < projectEnquiries.size(); i++) {
            System.out.println((i + 1) + ". " + projectEnquiries.get(i).getSender().get_name()+": "+ projectEnquiries.get(i).getSender().get_nric());
        }


        int applicantIndex = input.readInt("Select which applicant you want to reply (index):") - 1;



        if (applicantIndex < 0 || applicantIndex >= projectEnquiries.size()) {
            System.out.println("Invalid selection.");
            return;
        }


        Enquiry selected = projectEnquiries.get(applicantIndex);
        List<String[]> pairs = selected.getMessageReplyPairs();

        System.out.println("Messages from applicant:");
        for (int i = 0; i < pairs.size(); i++) {
            System.out.println((i+1) + ": " + pairs.get(i)[0]);
            System.out.println("   Current reply: " + (pairs.get(i)[1] == null ? "No reply yet" : pairs.get(i)[1]));
        }


        int msgIndex = input.readInt("Select a message number to reply:") -1;


        if (msgIndex < 0 || msgIndex >= pairs.size()) {
            System.out.println("Invalid message index.");
            return;
        }

        String reply = input.readLine("Enter your reply:");
        selected.setReply(msgIndex, reply);

        System.out.println("Reply added successfully!");

    }
    /**
     * Allows an HDB Officer to reply to enquiries for their project in charge.
     * @param officer the officer replying to enquiries
     */
    public void replyEnquiry(HDBOfficer officer) {



        List<Enquiry> projectEnquiries = new ArrayList<>();
        for (Enquiry e : enquiries) {
            if (e.getProject().equals(officer.getProjectInCharge())) {
                projectEnquiries.add(e);
            }
        }
        if (projectEnquiries.isEmpty()) {
            System.out.println("No enquiries for your project.");
            return;
        }

        System.out.println("Available applicants:");
        for (int i = 0; i < projectEnquiries.size(); i++) {
            System.out.println((i + 1) + ". " + projectEnquiries.get(i).getSender().get_name()+": "+ projectEnquiries.get(i).getSender().get_nric());
        }

        int applicantIndex = input.readInt("Select which applicant you want to reply (index):") - 1;


        if (applicantIndex < 0 || applicantIndex >= projectEnquiries.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Enquiry selected = projectEnquiries.get(applicantIndex);
        List<String[]> pairs = selected.getMessageReplyPairs();

        System.out.println("Messages from applicant:");
        for (int i = 0; i < pairs.size(); i++) {
            System.out.println((i+1) + ": " + pairs.get(i)[0]);
            System.out.println("   Current reply: " + (pairs.get(i)[1] == null ? "No reply yet" : pairs.get(i)[1]));
        }


        int msgIndex = input.readInt("Select a message number to reply:")-1;


        if (msgIndex < 0 || msgIndex >= pairs.size()) {
            System.out.println("Invalid message index.");
            return;
        }

        String reply = input.readLine("Enter your reply:");
        selected.setReply(msgIndex, reply);

        System.out.println("Reply added successfully!");

    }

    /**
     * Allows an applicant to view all their submitted enquiries.
     * @param applicant the applicant viewing their enquiries
     */
    public void viewEnquiries(Applicant applicant) {
        if(applicant.get_enquiry()==null){
            System.out.println("No enquiry for this applicant.");
            return;
        }
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getSender().equals(applicant)) {
                System.out.println("Project: " + enquiry.getProject().get_title());
                List<String[]> pairs = enquiry.getMessageReplyPairs();
                for (int j = 0; j < pairs.size(); j++) {
                    System.out.println("Message " + (j + 1) + ": " + pairs.get(j)[0]);
                    System.out.println("Reply   " + (j + 1) + ": " + (pairs.get(j)[1] == null ? "No reply yet" : pairs.get(j)[1]));
                }
                System.out.println();
            }
        }
    }

    /**
     * Allows an HDB Officer to view all enquiries related to their assigned project.
     * @param officer the officer viewing the enquiries
     */
    public void viewEnquiries(HDBOfficer officer) {
        boolean found=false;
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getProject().equals(officer.getProjectInCharge())) {
                found=true;
                System.out.println("Applicant: " + enquiry.getSender().get_name());
                List<String[]> pairs = enquiry.getMessageReplyPairs();
                for (int j = 0; j < pairs.size(); j++) {
                    System.out.println("Message " + (j + 1) + ": " + pairs.get(j)[0]);
                    System.out.println("Reply   " + (j + 1) + ": " + (pairs.get(j)[1] == null ? "No reply yet" : pairs.get(j)[1]));
                }
                System.out.println("Project: " + enquiry.getProject().get_title());
                System.out.println();
            }
        }
        if(!found){
            System.out.println("No enquiries for project: " + officer.getProjectInCharge().get_title());
        }

    }

    /**
     * Allows an HDB Manager to view enquiries from either all projects or only their assigned projects.
     * @param manager the manager viewing the enquiries
     */
    public void viewEnquiries(HDBManager manager) {
        boolean found = false;
        System.out.println("Do you want to view enquiries of all projects or only your project?");

        int choice = input.readInt("1 - All projects, 2 - Your project");

        for (Enquiry enquiry : enquiries) {
            ArrayList<Project> projects = manager.getProjList();
            if (choice == 1) {
                if (projects.isEmpty() != true) {
                    found = true;
                    System.out.println("Project:" + enquiry.getProject().get_title());
                    System.out.println("Applicant: " + enquiry.getSender().get_name());
                    List<String[]> pairs = enquiry.getMessageReplyPairs();
                    for (int j = 0; j < pairs.size(); j++) {
                        System.out.println("Message " + (j + 1) + ": " + pairs.get(j)[0]);
                        System.out.println("Reply   " + (j + 1) + ": " + (pairs.get(j)[1] == null ? "No reply yet" : pairs.get(j)[1]));
                    }
                    System.out.println();
                }
            } else if (choice == 2) {
                for (Project p : projects) {
                    if (enquiry.getProject().equals(p)) {
                        found = true;
                        System.out.println("Project:" + enquiry.getProject().get_title());
                        System.out.println("Applicant: " + enquiry.getSender().get_name());
                        List<String[]> pairs = enquiry.getMessageReplyPairs();
                        for (int j = 0; j < pairs.size(); j++) {
                            System.out.println("Message " + (j + 1) + ": " + pairs.get(j)[0]);
                            System.out.println("Reply   " + (j + 1) + ": " + (pairs.get(j)[1] == null ? "No reply yet" : pairs.get(j)[1]));
                        }
                        System.out.println();
                        break;
                    }
                }
            }
        }
    }
}