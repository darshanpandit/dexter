/**
 * 
 */
package structures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SingleSelectionModel;

/**
 * @author Darshan
 * 
 * Singleton Class to register documents and provide docID for a document.
 *
 */
public class DocumentRegistrar implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8185971755322827196L;
	private String path;
    private int docCounter;
    private FileWriter fileWriter;
	
    
    
	/**
	 * @param path
	 * @throws IOException 
	 */
	public DocumentRegistrar(String path) throws IOException {
		super();
		this.path = path;
		this.fileWriter = new FileWriter(path);
		docCounter = 0;
	}

	public void  close(){
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized int register(String url, int documentLength) throws IOException{
		docCounter++;
		fileWriter.write(url+"|@|"+String.valueOf(docCounter)+"|@|"+documentLength);
		fileWriter.write("\n");
		return docCounter;
		
	}
	
	

}
