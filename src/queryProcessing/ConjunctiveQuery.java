package queryProcessing;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import lexicon.Lexicon;

import structures.DocumentInfo;
import structures.WordRegistrar;


import indexing.BasicIndexEntry;
import indexing.CorpusStatistics;
import indexing.IndexReader;
import indexing.WrapperIndexEntry;

/**
 * 
 */

/**
 * @author Darshan
 *
 */
public class ConjunctiveQuery implements Query{
	IndexReader indexReader;
	Lexicon lexicon;
	Map<String,Integer> wordMap ;
	Map<Integer, DocumentInfo> documentMap ;
	double avgDocumentLength;
	
	
	

	
	/**
	 * @param indexReader
	 * @param lexiconSingleton
	 */
	public ConjunctiveQuery(IndexReader indexReader,
			Lexicon lexicon, Map<String,Integer> wordMap,Map<Integer, DocumentInfo> documentMap, double avgDocumentLength) {
		super();
		this.indexReader = indexReader;
		this.lexicon = lexicon;
		this.wordMap = wordMap;
		this.documentMap = documentMap;
		this.avgDocumentLength = avgDocumentLength;
	}
	
	@Override
	public DocumentScore[] search(String[] tokens, int numberOfResults) throws FileNotFoundException, IOException {
		List<TokenFrequencyTuple> tokenList = new ArrayList<TokenFrequencyTuple>(tokens.length);
		
		for(String token:tokens){
			int wordID = wordMap.get(token);
			BasicIndexEntry indexEntry = indexReader.openList(wordID);
			tokenList.add(new TokenFrequencyTuple(wordID, indexEntry.getTermFrequency()));
		}
		Collections.sort(tokenList);
		return processQuery(tokenList, numberOfResults);
	}


	private DocumentScore[] processQuery(List<TokenFrequencyTuple> tokenFrequencyTuples, int numberOfResults) throws FileNotFoundException, IOException{
		PriorityQueue<DocumentScore> documentHeap = new PriorityQueue<DocumentScore>(
				numberOfResults, new Comparator<DocumentScore>() {

					@Override
					public int compare(DocumentScore o1, DocumentScore o2) {
						if(o1.getScore()==o2.getScore())
							return 0;
						return(  (o1.getScore()-o2.getScore())>0	)?1:-1;
						
					}
				});
		
		
		//Assumes a sorted array of termIDs
		ArrayList<WrapperIndexEntry> indexEntries = new ArrayList<WrapperIndexEntry>(tokenFrequencyTuples.size());
		for( TokenFrequencyTuple tokenFrequencyTuple : tokenFrequencyTuples ){
			int termID = tokenFrequencyTuple.getTermID();
			BasicIndexEntry temp =  indexReader.openList(termID);
			if(temp!=null){
				indexEntries.add(new WrapperIndexEntry(temp) );
			}
		}
			
		int maxdocID  = indexEntries.get(0).getMaxDocID();
		
		//for(WrapperIndexEntry indexEntry:indexEntries)
		//	System.out.println(indexEntry.toString());
		
		int did = 0; 
		 while (did <= maxdocID) 
		 { 
		 /* get next post from shortest list */ 
			 did = WrapperIndexEntry.nextGEQ(indexEntries.get(0), did, true); 
		 /* see if you find entries with same docID in other lists */ 
			 int d=0;
			for (int i=1; (i<indexEntries.size()) && ((d=WrapperIndexEntry.nextGEQ(indexEntries.get(i), did, true)) == did); i++); 
		
			 if (d > did){
				 did = d; /* not in intersection and we move to the next possible id*/
			 }
			 
			 else 
			 	{ 
				 	List<Integer> frequency = new ArrayList<Integer>(indexEntries.size()) ;
				 	List<Integer> globalFrequencies = new ArrayList<Integer>(indexEntries.size());
				 	Scorer scorer = new BM25Scorer(1.2, 0.75);
				 	/* docID is in intersection; now get all frequencies */ 
				 	for (int i=0; i<indexEntries.size(); i++) {
				 		
				 		//frequency[i] = indexEntries.get(i).getFrequency(did);}
				 		frequency.add( WrapperIndexEntry.currentFrequency(indexEntries.get(i)));
				 		globalFrequencies.add(indexEntries.get(i).getTermFrequency());
				 		}
				 	
				 	/* compute BM25 score from frequencies and other data */ 
				 	double score = scorer.score(documentMap.size(), globalFrequencies, frequency, documentMap.get(did).getDocumentLength(), avgDocumentLength);	
				 	//Get Total Documents N from the documentRegistrar
				 	//int globalFrequency = lexiconSingleton.ge
				 	//Get Totol Frequency F from the LexiconRegistrar or the indexEntity
				 	//Length of document d from the documentRegistrar
				 	//AverageDocumentLenght |d| from the documentRegistrar
				 	//k & b are constants
				 	
				 	//add it to the heap
				 	documentHeap.offer(new DocumentScore(did, score));
				 	if(documentHeap.size()>numberOfResults)
				 		documentHeap.remove();
				 		did++; /* and increase did to search for next post */ 
			 	} 
		 } 
		 DocumentScore[] a = {};
		 return documentHeap.toArray(a);
		 // return heap results.
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException{
		
		ObjectInputStream inputStream = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(
						"D:\\darshan\\temp\\FinalIndexLexicon")));
		Lexicon lexicon = (Lexicon) inputStream
				.readObject();
		inputStream.close();
		
		Map<String,Integer> wordMap = new HashMap<String, Integer>(50000);
		Map<Integer, DocumentInfo> documentMap = new HashMap<Integer, DocumentInfo>(500000);
		
		BufferedReader fileReader = new BufferedReader( new FileReader("D:\\darshan\\temp\\WordRegistrar") );
		BufferedReader fileReader2 = new BufferedReader( new FileReader("D:\\darshan\\temp\\DocumentRegistrar") );
		
		String wordLine;
		while((wordLine = fileReader.readLine() ) != null){
			String[] split = wordLine.split("\\|@\\|");
			wordMap.put(split[0], Integer.valueOf(split[1]));
		}
		
		while((wordLine = fileReader2.readLine() ) != null){
			String[] split = wordLine.split("\\|@\\|");
			documentMap.put(Integer.valueOf(split[1]),new DocumentInfo(split[0],Integer.valueOf(split[2])));
		}
		 
		inputStream = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(
						"D:\\darshan\\temp\\IndexStatistics")));
		CorpusStatistics corpusStatistics = (CorpusStatistics) inputStream.readObject();
		
		double avgDocumentLength =( (double)corpusStatistics.getTotalPostings())/((double) corpusStatistics.getTotalDocuments());
		
		IndexReader indexReader = new IndexReader(lexicon, 2, "D:\\darshan\\temp\\FinalIndex", true);
		ConjunctiveQuery conjunctiveQuery = new ConjunctiveQuery(indexReader, lexicon, wordMap,documentMap,avgDocumentLength);
		boolean toContinue = true;
		System.out.println("Welcome to DexterV3");
		while(toContinue){
			try{
				System.out.println("Enter You Query : ");
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
			    String string = bufferReader.readLine();
			    String[] tokens = string.split(" ");
			    int[] termIDArray = new int[0];
			    System.out.println("How many pages do you want?");
			    string = bufferReader.readLine();
			    int numberOfResults =Integer.valueOf(string);
				Long startTime = System.currentTimeMillis();
				DocumentScore[] documentScores = conjunctiveQuery.search(tokens, numberOfResults);
				Long stopTime= System.currentTimeMillis();
				
				if(documentScores.length>0){
					for(DocumentScore documentScore: documentScores){
					}
				System.out.println("Found "+documentScores.length+" matching documents in "+(stopTime-startTime)+" milliSeconds");
				System.out.println("**********************************************************************************");
				for(DocumentScore documentScore:documentScores){
					System.out.println(documentMap.get(documentScore.getDocID()).getUrl()+ "     :   "+documentScore.getScore());
				}
				}
				else
					System.out.println("Sorry No Match Found");
				System.out.println("**********************************************************************************");
				System.out.println("Do you want to try some more queries[y]?");
				string = bufferReader.readLine();
			    if(!(string.equalsIgnoreCase("yes")|| string.equalsIgnoreCase("y"))){
			    	System.out.println("Invalid Input or you chose not to query further. We cant loose customers when Google is there. We need a better gui. Where's Stave Jobs?");
			    	toContinue =false;
			    }
			    	
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		
		
		
		
	}




	
}
