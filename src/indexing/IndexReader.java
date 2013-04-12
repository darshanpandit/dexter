/**
 * 
 */
package indexing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;

import lexicon.BlockInfo;
import lexicon.Lexicon;


/**
 * @author Darshan
 *
 */
public class IndexReader {
	
	private static int defaultCacheSize=1334;
	private static Float defaultLoadFactor =  (float) 0.75;
	private static boolean defaultLastAccessOrder = true;
	
	private  int indexNumber;
	private String rootDirectory;
	private boolean cacheEnabled;
	
	

	private  int cacheSize;
	private  Float loadFactor;
	private  boolean lastAccessOrder; //When set to false insertion order is maintained in map
	
	private Lexicon lexicon;
	private LinkedHashMap<Integer, BasicIndexEntry> lruCache;
	private RandomAccessFile file;
	
	
	
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void init() throws IOException {
		this.file = new RandomAccessFile((rootDirectory+"\\"+String.valueOf(indexNumber)),"r");
		if(cacheEnabled)
			lruCache = new LinkedHashMap<Integer, BasicIndexEntry>(cacheSize, loadFactor, lastAccessOrder);
	}

	//Opens an IndexEntry
	public BasicIndexEntry openList(int termID) throws FileNotFoundException, IOException
    {
        BasicIndexEntry indexEntry;
        if(cacheEnabled){
        	//Cache Hit to be returned from here
        	if(lruCache.containsKey(termID)){
        		indexEntry = lruCache.remove(termID);
        		//Least Recently Used
        		lruCache.put(termID, indexEntry);
        		return indexEntry;
        	}
        		
        }
        	//Lookonto Disk if Cache is disabled or was not found in cache
            BlockInfo blockInfo = lexicon.get(termID, indexNumber);
            	//System.out.println(blockInfo.toString());
            if(blockInfo!=null){
            	file.seek(blockInfo.getStartOffset());
            	int size= (blockInfo.getEndOffset()-blockInfo.getStartOffset());
            	byte[] bytes = new byte[size];
            	file.read(bytes, 0, size);
            	indexEntry = new BasicIndexEntry(termID, blockInfo, bytes);
            	if(cacheEnabled){
            		if(lruCache.size()<cacheSize)
            			lruCache.put(termID,indexEntry);
            		else{
            			//Removing the Least Recently used block
            			int toRemove = lruCache.keySet().iterator().next();
            			lruCache.remove(toRemove);
            			lruCache.put(termID, indexEntry);
            		}
            	}
            	return indexEntry;
            }
            else return null;
    }

   public void close() throws IOException{
	   file.close();
   }

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + indexNumber;
	result = prime * result
			+ ((rootDirectory == null) ? 0 : rootDirectory.hashCode());
	return result;
}

/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	IndexReader other = (IndexReader) obj;
	if (indexNumber != other.indexNumber)
		return false;
	if (rootDirectory == null) {
		if (other.rootDirectory != null)
			return false;
	} else if (!rootDirectory.equals(other.rootDirectory))
		return false;
	return true;
}
    
/**
 * @param indexNumber
 * @param rootDirectory
 * @param cacheEnabled
 * @throws IOException 
 */
public IndexReader(Lexicon lexicon, int indexNumber, String rootDirectory,
		boolean cacheEnabled) throws IOException {
	super();
	this.indexNumber = indexNumber;
	this.rootDirectory = rootDirectory;
	this.cacheEnabled = cacheEnabled;
	this.cacheSize = defaultCacheSize;
	this.loadFactor = defaultLoadFactor;
	this.lastAccessOrder = defaultLastAccessOrder;
	this.lexicon = lexicon;
	init();
}

/**
 * @param indexNumber
 * @param rootDirectory
 * @param cacheEnabled
 * @param cacheSize
 * @param loadFactor
 * @param lastAccessOrder
 * @throws IOException 
 */
public IndexReader(Lexicon lexicon, int indexNumber, String rootDirectory, boolean cacheEnabled,
		int cacheSize, Float loadFactor, boolean lastAccessOrder) throws IOException {
	super();
	this.indexNumber = indexNumber;
	this.rootDirectory = rootDirectory;
	this.cacheEnabled = cacheEnabled;
	this.cacheSize = cacheSize;
	this.loadFactor = loadFactor;
	this.lastAccessOrder = lastAccessOrder;
	this.lexicon = lexicon;
	init();
}

}
