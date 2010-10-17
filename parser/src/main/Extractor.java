package main;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import tar.TarEntry;
import tar.TarInputStream;

public class Extractor {
	
	// Idea from http://forums.sun.com/thread.jspa?threadID=5353548
	public void untar(InputStream stream, File dest) throws IOException {
	
		// assuming the file you pass in is not a dir
		dest.mkdirs();

		// create tar input stream from a .tar.gz file
		TarInputStream tin = new TarInputStream(new GZIPInputStream(stream));

		// get the first entry in the archive
		TarEntry tarEntry = tin.getNextEntry();

		while (tarEntry != null) {// create a file with the same name as the tarEntry

			File destPath = new File(dest.toString() + File.separatorChar + tarEntry.getName());

			if (tarEntry.isDirectory()) {

				destPath.mkdirs();

			} else {

				FileOutputStream fout = new FileOutputStream(destPath);

				tin.copyEntryContents(fout);

				fout.close();
			}
			tarEntry = tin.getNextEntry();
		}
		
		tin.close();
	}
}
