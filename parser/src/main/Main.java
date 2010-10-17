package main;

import java.io.File;

public class Main {
	
	private static final String URL = "http://www.timetab.ed.ac.uk/tt.tar.gz";
	
	private static final String FOLDER = System.getProperty("java.io.tmpdir") + "parser";
	
	private static final String OUTPUT = FOLDER + File.separatorChar + "storage.db";
	
	private static final boolean CHECK_FOR_NEW_FILES = false;
	
	public static final boolean DEBUG = false;
	
	public static void main (String args[]) {

		extractArchive();
		parseData();
	}
	
	private static void extractArchive() {
		
		File dir = new File(FOLDER);
		
		if (dir.exists() && !CHECK_FOR_NEW_FILES)
			return;
		
		
		Downloader downloader = new Downloader(URL);
		Extractor extractor = new Extractor();
		
		try {
			if (DEBUG) {
				System.out.println("Extracting to: " + dir.toString());
			}
			extractor.untar(downloader.fetch(), dir);
			if (DEBUG) {
				System.out.println("Extracted!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private static void parseData()
	{
		DBAdapter adapter = null;
		
		try
		{
			File file = new File(OUTPUT);
			
			if (file.exists()) {
				file.delete();
			}
			
			adapter = new DBAdapter(file.toString());
		}
		catch (Exception e)
		{
			System.out.println("Database connection cannot be created");
		}
		
		Parser parser = new Parser(new File(FOLDER));
		parser.parse(adapter);
		
		System.out.println("Output file generated at: " + OUTPUT);
	}
}
