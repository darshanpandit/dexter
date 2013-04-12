/**
 * 
 */
package indexing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import lexicon.BlockInfo;
import lexicon.Lexicon;

/**
 * @author Darshan
 * 
 */
public class IndexMerger {
	private String rootDirectory, outputDirectory;
	private Lexicon inputLexicon, outputLexicon;
	private Map<Integer, IndexReader> readerMap;

	/**
	 * @param rootDirectory
	 * @param outputDirectory
	 * @param lexiconSingleton
	 * 
	 */
	public IndexMerger(String rootDirectory, String outputDirectory,
			Lexicon lexicon)  {
		super();
		this.rootDirectory = rootDirectory;
		this.outputDirectory = outputDirectory;
		this.inputLexicon = lexicon;
		this.outputLexicon = new Lexicon();
		
		File folder = new File(rootDirectory);
		String[] filenames = folder.list();
		readerMap = new HashMap<Integer, IndexReader>(filenames.length);
		for(String temp: filenames){
			try{
				readerMap.put(Integer.valueOf(temp),new IndexReader(lexicon, Integer.valueOf(temp), rootDirectory, false)) ;
				System.out.println(temp);
			}catch(NumberFormatException e){} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			System.out.println("Done Loading the readers.");
			
	}

	public boolean merge() throws FileNotFoundException, IOException {
		
		//Select your favourate Jersey No to assign it to the final Index. :)
		//Ok we need similarity and we reuse indexreaders for merging so using the same constructor.
		int fileNumber = 2;
		
		//Modify here to expand for a distributed index structure.
		//Distributed index, would give you the advantage of higher throughput.
		File folder = new File(outputDirectory, "FinalIndex");
		folder.mkdir();
		outputDirectory = folder.getAbsolutePath();
		File outputFile = new File(outputDirectory,String.valueOf(fileNumber));
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outputFile));
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(outputDirectory+"Lexicon")));
		int currentFileOffset = 0;
		
		List<Integer> docVector = new LinkedList<Integer>();
		List<Integer> freqVector = new LinkedList<Integer>();
		//List<Integer> offsetVector = new LinkedList<Integer>();
		//List<Integer> contextVector = new LinkedList<Integer>();
		
		// Iterate for all terms discussed in lexicon
		for (Integer termID : inputLexicon.keySet()) {
			System.out.println("Now Merging for term :"+termID);
			List<BlockInfo> list = inputLexicon.get(termID);
			PriorityQueue<WrapperIndexEntry> pQueue = new PriorityQueue<WrapperIndexEntry>(
					list.size(), new Comparator<WrapperIndexEntry>() {

						@Override
						public int compare(WrapperIndexEntry o1, WrapperIndexEntry o2) {
							if(o1.getDocID(o1.getCurrentPosition())==o2.getDocID(o2.getCurrentPosition()))
								return 0;
							return( ( (o1.getDocID(o1.getCurrentPosition())-o2.getDocID(o2.getCurrentPosition())) )>0)?1:-1;
							
						}
					});
			int i=0;
			for(BlockInfo blockInfo:list){
				WrapperIndexEntry indexEntry = new WrapperIndexEntry(readerMap.get(blockInfo.getFileNumber()).openList(termID));
				pQueue.add(indexEntry);
				if(pQueue.size()>=2)
					pQueue.remove();
				i++;
				
			}
			
			while(!pQueue.isEmpty()){
				
				WrapperIndexEntry indexEntry = pQueue.poll();
					docVector.add(WrapperIndexEntry.currentDoc(indexEntry));
					freqVector.add(WrapperIndexEntry.currentFrequency(indexEntry));
					//offsetVector.addAll(WrapperIndexEntry.currentOffsets(indexEntry));
					//contextVector.addAll(WrapperIndexEntry.currentContexts(indexEntry));
					
					//If there is another docid in indexentry, we add the the indexentry back into the pQueue
					// Notice the fact that now the lastDocChecked has incremented and hence, would the values for docId as well
					// This helps us find the lowest docIds from a list of IndexEntries, and then we continue th process till we have pQueue emptied up
					
					
					if(WrapperIndexEntry.incrIndex(indexEntry))
						pQueue.add(indexEntry);
			}
			
			//Now we write the vectors to a block and store the info back into the lexicon.
			//The merging is complete for a term, now we flush it and update lexicon :)
			//System.out.println("Royal Flush *****************"+docVector.size());
			currentFileOffset = PostingWriter.writePosting(termID, docVector, freqVector, stream, currentFileOffset, fileNumber, outputLexicon);
			
		}
		
		objectOutputStream.writeObject(outputLexicon);
		objectOutputStream.close();
		stream.close();
		System.gc();
		return true;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		System.out.println("Now Loading the Lexicon");
		
		ObjectInputStream inputStream = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(
						"D:\\darshan\\temp\\Lexicon")));
		Lexicon lexicon = (Lexicon) inputStream
				.readObject();
		inputStream.close();
		System.out.println("Done Loading the Lexicon");
		IndexMerger indexMerger = new IndexMerger("D:\\darshan\\temp\\index",
				"D:\\darshan\\temp", lexicon);
		Long start = System.currentTimeMillis();
		indexMerger.merge();
		Long stop  = System.currentTimeMillis();
		
		/*
		ObjectInputStream objectInputStream =new ObjectInputStream(new FileInputStream("D:\\darshan\\temp\\FinalIndex\\2_Lexicon"));
		lexicon = (Lexicon) objectInputStream.readObject();
		objectInputStream.close();
		//System.out.println(lexiconSingleton.toString());
		*/
		System.out.println("Merging done in "+(stop-start)+" milliSeconds");
		

	}

}
