import org.apache.poi.ss.usermodel.*; //to settle this like idk download or sth ig
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The {@code ProjectDatabase} class is responsible for reading project data from an Excel spreadsheet,
 * converting that data into {@link Project} objects, and populating the {@link ProjectManager} with
 * categorized lists of active, inactive, and expired projects.
 */
public class ProjectDatabase {
    /**
     * Reads data from an Excel file at the specified filepath, parses it into structured {@link Project} objects,
     * and categorizes them into active, inactive, and expired based on their open/close dates.
     * <p>
     * The method also establishes relationships between projects and {@link HDBManager} and {@link HDBOfficer} instances.
     * It updates the {@link ProjectManager} to make all project data accessible throughout the application.
     *
     * @param filepath the path to the Excel file containing project information
     */
    public static void excelToHashmap (String filepath) {
        HashMap<String, List<String>> project_data = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(new File(filepath));
            Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0); //read first sheet
                boolean isFirstRow = true; 
                for (Row row:sheet) {
                    if (isFirstRow) {
                        isFirstRow = false;
                        continue;
                    }                                       
                    String key = row.getCell(0).toString(); 
                    List<String> rowData = new ArrayList<>();

                    for (int i=1; i<row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        rowData.add(cell.toString());
                    }
                    
                    project_data.put(key, rowData); //key, value
                }
            } catch (IOException e) { //??
                e.printStackTrace();
            } 
            // Convert project data into Project objects and store in ProjectManager
        ArrayList<Project> loadedProjects = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
        for (Map.Entry<String, List<String>> set : project_data.entrySet()) {
            String name = set.getKey();
            List<String> values = set.getValue();
            String neighbourhood = values.get(0);
            int numOf2Room = (int) Double.parseDouble(values.get(2));
            int price2Room = (int) Double.parseDouble(values.get(3));
            int numOf3Room = (int) Double.parseDouble(values.get(5));
            int price3Room = (int) Double.parseDouble(values.get(6));
            LocalDate openingDate = LocalDate.parse(values.get(7), formatter);
            LocalDate closingDate = LocalDate.parse(values.get(8), formatter);
            HDBManager manager_IC = null;
            
            for (HDBManager manager : UserManager.all_managers) {
                if (values.get(9).equals(manager.get_name())) {
                    manager_IC = manager;
                    break;
                }
            }
            
            int numOfOfficerSlots = (int) Double.parseDouble(values.get(10));
            ArrayList<HDBOfficer> ExistingOfficers = new ArrayList<>();
            String[] officers = null;
            if (values.size() > 11 && values.get(11) != null && !values.get(11).trim().isEmpty()) {
                officers = values.get(11).split(",");
            }
            if (officers != null){
                for (String officerName : officers){
                    for (HDBOfficer officer : UserManager.all_officers){
                        if (officer.get_name().equals(officerName)){
                            ExistingOfficers.add(officer);
                            break;
                        }
                    }
                }
            }
            Project proj = new Project(name, neighbourhood, numOf2Room, price2Room, numOf3Room, price3Room, openingDate, closingDate, manager_IC, numOfOfficerSlots); // manager is null for now
            for (HDBOfficer officer : ExistingOfficers) {
                officer.setProjectInCharge(proj);
            }
            proj.set_officerList(ExistingOfficers);
            manager_IC.addToProjList(proj);
            if (proj.get_closing_date().plusDays(7).isAfter(LocalDate.now())){ //7 day period to process application once project expired
                manager_IC.setProject(proj);
            }
            loadedProjects.add(proj);
        }
        ProjectManager.setProjectList(loadedProjects); // key step to allow system access

        // add projects to lists correspond to their status i.e active, inactive....
        ArrayList<Project> expiredProjects = new ArrayList<>();
        ArrayList<Project> inactiveProjects = new ArrayList<>();
        ArrayList<Project> activeProjects = new ArrayList<>();
        for (Project p : ProjectManager.getProjectList()) {
            if (p.get_opening_date().isAfter(LocalDate.now())) {
                inactiveProjects.add(p);
            }
            else if (p.get_closing_date().isBefore(LocalDate.now())) { 
                expiredProjects.add(p);
            }
            else {
                activeProjects.add(p);
            }
        }
        ProjectManager.setInactiveList(inactiveProjects);
        ProjectManager.setActiveList(activeProjects);
        ProjectManager.setExpiredList(expiredProjects);
    }
}
