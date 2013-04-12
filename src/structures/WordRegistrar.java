/**
 * 
 */
package structures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Darshan
 * Singleton Class to register documents and provide docID for a document.
 *
 */
public class WordRegistrar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4130462370228887912L;
	public int wordRegistrarBaseSize = 1000000;
    private Map<String, Integer> invertedWordMap;
    private int wordIDCount;
    private String path;
    private FileWriter fileWriter;
    
    
    /**
	 * @param wordRegistrarBaseSize
	 * @param path
     * @throws IOException 
	 */
	public WordRegistrar(int wordRegistrarBaseSize, String path) throws IOException {
		super();
		this.wordRegistrarBaseSize = wordRegistrarBaseSize;
		this.path = path;
		wordIDCount = 0;
		File file = new File(path,"WordRegistrar");
		fileWriter = new FileWriter(file);
		invertedWordMap = new HashMap<String, Integer>(wordRegistrarBaseSize);
	}

	public void close() throws IOException{
		for(String word: invertedWordMap.keySet() ){
			fileWriter.write((word+"|@|"+invertedWordMap.get(word)));
			fileWriter.write("\n");
		}
		fileWriter.close();
	}



	/**
     * registers a word and return wordID. The method is synchronized.
     * @param url
     * @return
     */
    public synchronized int register(String word){
	if(!invertedWordMap.containsKey(word)){
	    wordIDCount++;
	//    wordMap.put(wordIDCount, word);
	    invertedWordMap.put(word, wordIDCount);
	    return wordIDCount;
	}
	return invertedWordMap.get(word);
    }
    
    
    
    
   
}
