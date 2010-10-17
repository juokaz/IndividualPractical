package main;

import java.io.File;

public class Main {
	
	private static final String URL = "http://www.timetab.ed.ac.uk/tt.tar.gz";
	
	private static final String FOLDER = System.getProperty("java.io.tmpdir") + File.separatorChar + "parser";
	
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
	
	private static void parseData() {
		
		Parser parser = new Parser(new File(FOLDER));
		parser.parse();
	}
}
