package spreadsheet;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  

public class Spreadsheet {
	
	XSSFWorkbook WB; //Workbook
	
	public Spreadsheet(String filename) {
		try {
		    File file = new File(filename); //creating a new file instance  
		    FileInputStream fis = new FileInputStream(file); //obtaining bytes from the file  
		    
		    WB = new XSSFWorkbook(fis); //creating Workbook instance that refers to .xlsx file
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public XSSFWorkbook getWB() {
		return WB;
	}
}