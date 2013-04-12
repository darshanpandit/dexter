/**
 * 
 */
package parsing;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author Darshan
 * 
 */
public class NZFileReader {
    private File indexFile, dataFile;
    private StringBuilder indexStream, dataStream;
    BufferedReader indexFileReader;
    InputStreamReader dataInputStreamReader;
    private int offset = 0;
    public List<String> validCodes;
    public static int totalFile=0;
    public static int rejectFile=0;

    /**
     * @param indexFile
     * @param dataFile
     * @throws IOException
     * @throws FileNotFoundException
     */
    public NZFileReader(File indexFile, File dataFile)
	    throws FileNotFoundException, IOException {
	super();
	validCodes = new LinkedList<String>();
	this.indexFile = indexFile;
	this.dataFile = dataFile;
	try{
	this.dataInputStreamReader = new InputStreamReader(new GZIPInputStream(
		new FileInputStream(dataFile)));

	this.indexFileReader = new BufferedReader(new InputStreamReader(
		new GZIPInputStream(new FileInputStream(indexFile))));
	}catch(EOFException e){
		throw e;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
	this.close();
    }

    public void close() throws IOException {
	if (this.indexFileReader != null)
	    this.indexFileReader.close();
	if (this.dataInputStreamReader != null)
	    this.dataInputStreamReader.close();
    }

    public Page next() throws IOException {
	totalFile++;
	String indexLine = indexFileReader.readLine();
	if (indexLine != null) {
	    String[] indexParams = indexLine.split(" ");
	    String domain = indexParams[0];
	    String ip = indexParams[4];
	    String port = indexParams[5];
	    String page = indexParams[3];
	    String status = indexParams[6];
	    int charlength = Integer.parseInt(indexParams[3]);
	    //int charlength = Integer.parseInt(indexParams[5]);
	    // System.out.println(dataInputStreamReader.skip(offset));
	    char[] dataBuffer = new char[charlength];
	    int readStatus = dataInputStreamReader.read(dataBuffer, 0,
		    charlength);
	    // System.out.println(new String(dataBuffer));

	    // Call parser here
	    String pageString = new String(dataBuffer);
	    offset += charlength;

	    // if page was fetched. Else we move to the next page.
	    if (status.equalsIgnoreCase("ok") && pageString.length()>0
		    && pageString.substring(9, 10).equals("2")) {
		return new Page((domain + page), pageString,
			pageString.substring(9, 12));

	    }
	    // A page with null as pagestring is returned to indicate improper
	    // download or fetching
	    rejectFile++;
	    return new Page((domain + page), null, null);
	}
	// Null Page is returned at the end of file to indicate end of file
	else
	    return null;
    }

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException,
	    IOException {
	File indexFile = new File("D:\\smalldataset\\down1_1800_index.gz");
	System.out.println(indexFile.getAbsolutePath());
	File dataFile = new File("D:\\smalldataset\\down1_1800.gz");
	NZFileReader nzFileReader = new NZFileReader(indexFile, dataFile);
	Page page;
	int counter = 0;

	Long start = System.currentTimeMillis();
	while (((page = nzFileReader.next()) != null)) {
	    if (page.getPage() != null) {
		StringBuilder stringBuilder = new StringBuilder();
		Parser.parseDoc(page.getUrl(), page.getPage(), stringBuilder);
		counter++;
		
	    }
	    /*
	     * else System.out.println(page.getUrl() +
	     * " was not parsed due to errors in it.");
	     */
	}
	System.out.println((System.currentTimeMillis() - start));
	System.out.println("Done. Parsed " + counter + " files.");
	
	
    }

}
