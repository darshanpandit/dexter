package indexing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lexicon.BlockInfo;
import lexicon.Lexicon;


import compression.MyCompression;

public class PostingWriter {
	private int  mycounter, currentOffset;
	private List<Posting> postingList;
	//private OffsetInfoWriter offsetInfoWriter;
	private Lexicon lexicon;
	private OutputStream stream;
	private static int indexCounter = 0;
	
	public static int getIndexCounter() {
		return indexCounter;
	}

	private synchronized int incrIndexCounter() {
		indexCounter++;
		return indexCounter;
	}

	
	
	
	/**
	 * @param indexCounter
	 * @param currentOffset
	 * @param postingList
	 * @param lexicon
	 * @param stream
	 * @throws FileNotFoundException 
	 */
	public PostingWriter(
			List<Posting> postingList, Lexicon lexicon, String path) throws FileNotFoundException {
		super();
		this.mycounter = incrIndexCounter();
		this.currentOffset = 0;
		
		this.postingList = postingList;
		this.lexicon = lexicon;
		this.stream = new FileOutputStream(new File(path,(String.valueOf(mycounter))));
	}



	/**
	 * 
	 */
	public  void write() {
		System.out.println("Now Writing " + mycounter + " Index");
		Collections.sort(postingList);

		Integer prevTermID = postingList.get(0).getTermID();
		Integer prevDocID = postingList.get(0).getDocID();

		List<Integer> docVector = new ArrayList<Integer>(10000);
		List<Integer> freqVector = new ArrayList<Integer>(10000);
		//List<Integer> offsetInfoStart = new LinkedList<Integer>();
		//List<Integer> offsetInfoLength = new LinkedList<Integer>();
		
		//List<Integer> offsetVector = new LinkedList<Integer>();
		//List<Integer> contextVector = new LinkedList<Integer>();


		int freqCounter = 0;
		//List<Integer> tempOffsetList = new ArrayList<Integer>();
		int im = 0;
		Posting currentPosting;

		while (im < postingList.size()) {
			currentPosting = postingList.get(im);
			im++;
			if (currentPosting.getTermID() == prevTermID) {
				if (currentPosting.getDocID() != prevDocID) {
					docVector.add(prevDocID);
					freqVector.add(freqCounter);
					
					//Write the offset and contextvectors to the offsetfile here
					//OffsetBlock offsetBlock = offsetInfoWriter.write(offsetVector, contextVector);
					//offsetInfoStart.add(offsetBlock.getStartOffset());
					//offsetInfoLength.add((offsetBlock.getEndOffset()-offsetBlock.getStartOffset()));
					freqCounter = 0;
					prevDocID = currentPosting.getDocID();
					//offsetVector.clear();
					//contextVector.clear();
				}
				freqCounter++;
				//offsetVector.add(currentPosting.getOffset());
				//contextVector.add(currentPosting.getContextID());

			} else {
				try {
					docVector.add(prevDocID);
					freqVector.add(freqCounter);
					
					//Write the offset and contextvectors to the offsetfile here
					//OffsetBlock offsetBlock = offsetInfoWriter.write(offsetVector, contextVector);
					//offsetInfoStart.add(offsetBlock.getStartOffset());
					//offsetInfoLength.add((offsetBlock.getEndOffset()-offsetBlock.getStartOffset()));
					freqCounter = 0;
					prevDocID = currentPosting.getDocID();
					//offsetVector.clear();
					//contextVector.clear();

					currentOffset = PostingWriter.writePosting(prevTermID,
							docVector, freqVector, stream, currentOffset, indexCounter, lexicon);
					prevTermID = currentPosting.getTermID();
					freqCounter++;
					//offsetVector.add(currentPosting.getOffset());
					//contextVector.add(currentPosting.getContextID());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			System.out.println("Size of Lexicon(in No.of Terms) :"+ lexicon.size());
			stream.close();
			System.out.println("Writing " + indexCounter + " Done.");
			
			//System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @param termID
	 * @param docVector
	 * @param freqVector
	 * @param offsetVector
	 * @param contextVector
	 * @param stream
	 * @param currentOffset
	 * @param fileNumber
	 * @param lexicon
	 * @return
	 * @throws IOException
	 */
	public static int writePosting(int termID, List<Integer> docVector,
			List<Integer> freqVector, OutputStream stream,
			int currentOffset, Integer fileNumber,
			Lexicon lexicon) throws IOException {
		
		int start = currentOffset;

		List<Integer> tempList = new ArrayList<Integer>(
				(docVector.size() * 2));
		
		//To Add DeltaCompressed docIDs
		for(int temp:MyCompression.deltaCompress(docVector))
			tempList.add(temp);
		
		//To Add Frequencies
		tempList.addAll(freqVector);

		byte[] bytes = MyCompression.encode(tempList);
		stream.write(bytes, 0, bytes.length);
		currentOffset += bytes.length;
		int mid = currentOffset;
		
		tempList = new LinkedList<Integer>();
		
		//To add all startOffset
		//tempList.addAll(offsetInfoStart);
		
		//To Add all lengthOffsets
		//tempList.addAll(offsetInfoLength);
		/*
		bytes = MyCompression.encode(tempList);
		stream.write(bytes, 0, bytes.length);
		currentOffset += bytes.length;
		*/
		
		int sum = 0;
		for(int i=0;i<freqVector.size();i++)
			sum+=freqVector.get(i);
		
		lexicon.put(termID, sum, new BlockInfo(
				fileNumber, start, mid));
		
		// Reinitialize all the vectors and prev values.
		docVector.clear();
		freqVector.clear();
		//offsetVector.clear();
		//contextVector.clear();
		
		return currentOffset;
	}
}
