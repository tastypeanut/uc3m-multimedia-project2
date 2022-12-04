package app;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scrapers.AttributeScrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet; 

import spreadsheet.Spreadsheet;

public class App {

    public static void main( String[] args )
    {
    	if(args.length < 2) {
    		
    		System.out.println("If you are running this with eclipse, you need to set the excel file path as an argument in your run configurations");
    		System.out.println("To use this program correctly from the command line, please run it the following way: ");
    		System.out.println("java -jar " + System.getProperty("java.class.path") + "[number of threads] [path to excel file] [output file name]");
    		
    	} else {
    		
    		int threads = Integer.parseInt(args[0]);
    		String sheet_filename = args[1];
    		String output_filename = args[2];
    		
    		//Multithreading code
    		ExecutorService pool = Executors.newFixedThreadPool(threads);
    		Semaphore sem = new Semaphore(1);
    		
    		//Output file FileWriter
    		File file = new File(output_filename);
    		FileWriter wrt = null;
    		try {
    			//file.createNewFile();
    			wrt = new FileWriter(file, false);
   			} catch (IOException e1) {
   				System.out.println("There was a problem selecting the output file.");
   				e1.printStackTrace();
    		}

    		//Sheet read code
    		Spreadsheet spreadsheet = new Spreadsheet(sheet_filename);
    		XSSFWorkbook wb = spreadsheet.getWB();
    		Iterator<Sheet> sheetitr = wb.iterator();
    		while(sheetitr.hasNext()) { 
    			//Sheet iterator goes through all sheets
    			Sheet sheet = sheetitr.next();
    			Iterator<Row> rowitr = sheet.iterator();
    			while (rowitr.hasNext()) { 
    				//This skips first row with column names
    				rowitr.next(); 
    				if(rowitr.hasNext()) {
    					//Row iterator goes through all rows in each sheet
    					Row row = rowitr.next();
        				String link = row.getCell(1).toString();
        				//Creating a multithreadable scrapper
        				Runnable scrapper = new AttributeScrapper(link, sem, wrt); 
        				//Multithreading start for that object (will execute run() method)
        				pool.execute(scrapper);
    				}
    			}
    		}
    		
    		//Closing thread pool
    		pool.shutdown();

    	}
    }
}
