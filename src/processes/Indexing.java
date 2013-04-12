/**
 * 
 */
package processes;

import indexing.CorpusStatistics;
import indexing.Posting;
import indexing.PostingWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import lexicon.Lexicon;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import parsing.NZFileReader;
import parsing.NZFolderReader;
import parsing.Page;
import parsing.Parser;
import structures.ContextRegistrar;
import structures.DocumentRegistrar;
import structures.WordRegistrar;

/**
 * @author Darshan
 *
 */
public class Indexing {
	private String inputPath;
	private String outputPath;
	private int termBatchSize;
	
	
	
	
	/**
	 * @param inputPath
	 * @param outputPath
	 * @param termBatchSize
	 */
	public Indexing(String inputPath, String outputPath, int termBatchSize) {
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.termBatchSize = termBatchSize;
	}




	public boolean index() throws FileNotFoundException, IOException{
		//Create outputPathFolder First
		boolean outputPathExists = new File(outputPath).mkdir();
		
		File  tempFolder= new File(outputPath,"temp");
	
		tempFolder.mkdir();
		String tempFolderPath = tempFolder.getAbsolutePath();

		File indexFolder = new File(outputPath,"index");
		indexFolder.mkdir();
		String indexPath = indexFolder.getAbsolutePath();
		//Initiate Registrars
		DocumentRegistrar documentRegistrar = new DocumentRegistrar( ( outputPath + "\\DocumentRegistrar") );
		WordRegistrar wordRegistrar = new WordRegistrar(100000, (outputPath));
		ContextRegistrar contextRegistrar = new ContextRegistrar(20, ( outputPath + "\\ContextRegistrar"));

		Page page;
		int validPagecounter = 0;
		int badPageCount = 0;

		Pattern newLineSplitPattern = Pattern.compile("\n");
		Pattern contextSplitPattern = Pattern.compile(" ");

		String[] myArray;
		int wordID,  offset;
		int tcount = 0;
		
		Lexicon lexicon = new Lexicon();

		Long start = System.currentTimeMillis();

		Posting posting;
		int myloop=0;
		List<Posting> postingList = new ArrayList<Posting>(termBatchSize);
		
		//Unpack the tar
		File folder = new File(inputPath);
		Set<Integer> activeTaskSet = new HashSet<Integer>();
		
		for (File temp : folder.listFiles()) {
			TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(
					new FileInputStream(temp));
			TarArchiveEntry archiveEntry;
			archiveInputStream.getNextTarEntry();
			// Now to copy the tar data to a temporary folder
			while ((archiveEntry = archiveInputStream.getNextTarEntry()) != null) {
				if (archiveEntry.isFile()) {
					System.out.println(archiveEntry.getName());
					String[] folds = archiveEntry.getName().split("/");
					File file = new File(tempFolderPath, folds[(folds.length - 1)]);
					//file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(
							file);
					IOUtils.copy(archiveInputStream, fileOutputStream);
					fileOutputStream.close();

					// file.delete();
				}
			}
			archiveInputStream.close();
			System.out.println("Done Uncompressing " + temp);

			// Call NZFOlderReader from here
			NZFolderReader nzFolderReader = new NZFolderReader(new File(
					tempFolderPath));
			while ((page = nzFolderReader.next()) != null) {
				if (page.getPage() != null) {
					StringBuilder stringBuilder = new StringBuilder();
					try {
						Parser.parseDoc(page.getUrl(), page.getPage(),
								stringBuilder);
						offset = 0;
						validPagecounter++;
						String[] lines = newLineSplitPattern.split(stringBuilder);
						int docID = documentRegistrar.register(page.getUrl(), lines.length);
						for (String parsedLine : lines) {
							myArray = contextSplitPattern.split(parsedLine);
							
							if (myArray.length > 1) {
								tcount++;
								offset++;
								posting = new Posting(
										wordRegistrar.register(myArray[0]),
										docID);
								
								postingList.add(posting);
								if(postingList.size()==termBatchSize){
									
									System.gc();	
									//String tempIndexPath = new File(tempFolderPath,String.valueOf(myloop)).getAbsolutePath();
									new PostingWriter(postingList,lexicon, indexPath).write();
									System.out.println("So far "+ (System.currentTimeMillis()-start)+" milliSec have passed since execution.");
									postingList = null;
									System.gc();	
									
									postingList= new ArrayList<Posting>(termBatchSize);
									myloop++;
									System.out.println(myloop+" into the batchSize yet executed.");
									System.out.println(activeTaskSet);
								}
								
								
							}

						}
					} catch (StringIndexOutOfBoundsException e) {
						badPageCount++;
					}

				}
				// else System.out.println(page.getUrl() +
				// " was not parsed due to errors in it.");
			}
			
			
			
			//System.out.println(PostingWriter.getIndexCounter());
			if(postingList.size()!=0){
				//String tempIndexPath = new File(tempFolderPath,String.valueOf(myloop)).getAbsolutePath();
				new PostingWriter(postingList,lexicon, indexPath).write();
				System.gc();	
				myloop++;
			}
				
			
			// Deleting all files from the temp folder
			File tempDir = new File(tempFolderPath);
			if (tempDir.listFiles() != null)
				for (File file : tempDir.listFiles()) {
					file.delete();
				}
			// tempDir.delete();
			// new File(tempPath).createNewFile();;
		}
		
		
		File lexiconObjectFile = new File(outputPath,"Lexicon");
		File wordRegistrarFile = new File(outputPath,"WordRegistrar");
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(lexiconObjectFile));
		objectOutputStream.writeObject(lexicon);
		objectOutputStream.close();
		
		//objectOutputStream = new ObjectOutputStream(new FileOutputStream(wordRegistrarFile));
		//objectOutputStream.writeObject(wordRegistrar);
		//objectOutputStream.close();
		
		documentRegistrar.close();
		wordRegistrar.close();
		contextRegistrar.close();
		
		// invertedIndex.makeRelative();
		System.out.println("Total Postings"+tcount);
		System.out.println("Total Time taken:"+(System.currentTimeMillis() - start));
		System.out.println("Done. Parsed " + badPageCount + " files.");
		System.out.println(NZFileReader.totalFile);
		System.out.println(NZFileReader.rejectFile+" Rejected");
		System.out.println("BadPageCount : " + badPageCount);
		
		File file = new File(outputPath,"IndexStatistics");
		objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
		objectOutputStream.writeObject(new CorpusStatistics(NZFileReader.totalFile, tcount));
		objectOutputStream.close();
		
		
		return true;
		
	}
	
	
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String inputPath = "D:\\1NZ";
		String outputPath = "D:\\darshan\\temp";
		int batchSize= 5000000;
		new Indexing(inputPath,outputPath,batchSize).index();
	}

}
