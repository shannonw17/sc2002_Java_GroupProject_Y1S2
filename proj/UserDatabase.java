import org.apache.poi.ss.usermodel.*; //to settle this like idk download or sth ig
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 * The {@code UserDatabase} class provides utility methods to load user data from Excel spreadsheets
 * and convert them into structured {@code HashMap} formats for use in the application.
 * <p>
 * It supports loading user details for different roles (Applicant, HDB Officer, HDB Manager)
 * and combining them into a single nested map for authentication and data access.
 */
public class UserDatabase {
    /**
     * Reads an Excel file (.xlsx) and converts the data into a {@code HashMap}.
     * Each row represents a user, with the NRIC (second column) as the key and
     * a list of the remaining details (name, age, marital status, password) as the value.
     *
     * @param filepath the path to the Excel file
     * @return a {@code HashMap} mapping NRICs to lists of user details
     */
    public static HashMap<String, List<String>> excelToHashmap (String filepath) {
        HashMap<String, List<String>> dataMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(new File(filepath));
            Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0); //read first sheet
                boolean isFirstRow = true; //to skip 1st row (header row)
                for (Row row:sheet) {
                    if (isFirstRow) {
                        isFirstRow = false; //skip 1st row only
                        continue;
                    }                                       
                    //int rowNum = row.getRowNum() - 1; //row number = key for each user data
                    String key = row.getCell(1).toString(); //2nd column ie. nric is key, rest is value
                    List<String> rowData = new ArrayList<>();

                    //for (Cell cell: row)
                        //rowData.add(cell.toString()); //convert each cell to string and store in list eg. name, age, marital status
                    for (int i=0; i<row.getLastCellNum(); i++) {
                        if (i==1) continue; //skip nric column
                        Cell cell = row.getCell(i);
                        rowData.add(cell.toString());
                    }
                    
                    dataMap.put(key, rowData); //key, value
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dataMap; //ie. nric -> [name, age, marital status, password]
    }

    /**
     * Combines individual role-based user maps into a nested map,
     * allowing easy access based on user roles.
     * <p>
     * The resulting map uses role names as keys ("applicant", "hdbofficer", "hdbmanager")
     * and maps them to their respective data maps.
     *
     * @param sheet1 the map for applicants
     * @param sheet2 the map for HDB officers
     * @param sheet3 the map for HDB managers
     * @return a nested {@code HashMap} where each role maps to its corresponding user data map
     */
    //eg. applicant -> (nric -> [...details...])
    public static HashMap<String, HashMap<String, List<String>>> combinedHashmap(HashMap<String, List<String>> sheet1, HashMap<String, List<String>> sheet2, HashMap<String, List<String>> sheet3) {
        HashMap<String, HashMap<String, List<String>>> nestedMap = new HashMap<>(); ///declare and allocate memory for new hashmap
        nestedMap.put("applicant", sheet1); 
        nestedMap.put("hdbofficer", sheet2);
        nestedMap.put("hdbmanager", sheet3);

        return nestedMap;
    }
    /**
     * Retrieves the map corresponding to a specific user role from the nested map.
     *
     * @param nest_map the nested map containing role-based maps
     * @param nest_key the role to retrieve (e.g., "applicant", "hdbofficer", "hdbmanager")
     * @return the corresponding map of user data, or {@code null} if the role is not found
     */
    public static HashMap<String, List<String>> chooseHashmap(HashMap<String, HashMap<String, List<String>>> nest_map, String nest_key) {
        if (nest_map.containsKey(nest_key)) {
            return nest_map.get(nest_key);
        }
        return null;
    }

}
