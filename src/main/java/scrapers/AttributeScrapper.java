package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

public class AttributeScrapper extends Thread implements Runnable  {
	
	//Values set in constructor
	Semaphore SEM;
	String URL;
	FileWriter WRT;
	
	//Values set within class methods
	Document DOC;
	String Title;
	String Year;
	String Director;
	String Genres;
	String Description;
	
	
	public AttributeScrapper(String url, Semaphore sem, FileWriter wrt) {
		URL = url;
		SEM = sem;
		WRT = wrt;
	}
	
	//This method is used in multithreading
	public void run() { 
		try {
			DOC = Jsoup.connect(URL)
					//Attempt to bypass rate limiting
					.header("X-Forwarded-For", "127.0.0.1")
					.header("X-Forwarded-Host", "127.0.0.1")
					.header("X-Client-IP", "127.0.0.1")
					.header("X-Remote-IP", "127.0.0.1")
					.header("X-Remote-Addr", "127.0.0.1")
					.header("X-Host", "127.0.0.1")
					.get();
			this.getTitle();
			this.getYear();
			this.getDirector();
			this.getGenres();
			this.getDescription();
			SEM.acquire();
			WRT.write(this.Title + " || " + this.Year + " || " + this.Director + " || " + this.Genres + " || " + this.Description + "\n");
			System.out.print(this.Title + " || " + this.Year + " || " + this.Director + " || " + this.Genres + " || " + this.Description + "\n");
			WRT.flush();
			SEM.release();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public String getTitle(){
		if(DOC == null) {
			Title = "";
		} else {
			Title = DOC.getElementsByAttributeValue("data-testid", "hero-title-block__title").text();
		}
		return Title;
	}
	
	public String getYear() {
		if(DOC == null) {
			Year = "";
		} else {
			Year = DOC.getElementsByAttributeValue("data-testid", "hero-title-block__metadata").select("li:nth-child(1) > span").text();
		}
		return Year;
	}
	
	public String getDirector() {
		if(DOC == null) {
			Director = "";
		} else {
			Director = DOC.select("#__next > main > div > section.ipc-page-background.ipc-page-background--base.sc-9b716f3b-0.hWwhTB > div > section > div > div.sc-b1d8602f-1.fuYOtZ.ipc-page-grid__item.ipc-page-grid__item--span-2 > section:nth-child(26) > div.sc-132205f7-0.bJEfgD > div.ipc-overflowText.ipc-overflowText--pageSection.ipc-overflowText--height-long.ipc-overflowText--long.ipc-overflowText--click.ipc-overflowText--base > div > div").text();
		}
		return Director;
	}
	
	public String getGenres() {
		if(DOC == null) {
			Genres = "";
		} else {
			Genres = DOC.select(".ipc-chip-list--baseAlt > div:nth-child(2)").text();
		}
		return Genres;
	}
	
	public String getDescription() {
		if(DOC == null) {
			Description = "";
		} else {
			Description = DOC.select(".sc-16ede01-1").text();
		}
		return Description;
	}
}
