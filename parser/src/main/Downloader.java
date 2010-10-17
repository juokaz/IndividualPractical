package main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {
	
	private String url = "";

	public Downloader(String url)
	{
		this.url = url;
	}
	
	// Idea from http://www.java2s.com/Tutorial/Java/0320__Network/SavebinaryfilefromURL.htm
	public InputStream fetch() throws Exception
	{
		URL u = new URL(this.url);
	    URLConnection uc = u.openConnection();
	    String contentType = uc.getContentType();
	    int contentLength = uc.getContentLength();
	    
	    if (contentType.startsWith("text/") || contentLength == -1) {
	      throw new IOException("This is not a binary file.");
	    }
	    
	    InputStream raw = uc.getInputStream();
	    
	    return raw;
	}
}
