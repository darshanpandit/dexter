/**
 * 
 */
package writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import compression.MyCompression;

/**
 * @author Darshan
 *
 */
public class OffsetInfoWriter {
	private RandomAccessFile file;
	private int currentOffset;
	
	/**
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 */
	public OffsetInfoWriter(String path) throws FileNotFoundException {
		this.file = new RandomAccessFile(new File(path), "w");
		this.currentOffset = 0;
		// TODO Auto-generated constructor stub
	}

	public OffsetBlock write(List<Integer> offsetVector, List<Integer> contextVector) throws IOException{
		
		int[] finalArray = new int[(offsetVector.size()*2)];
		
		
		//Offsets are dgapped. We assume that we will have sequential offsets.
		int[] offsetDgap = (MyCompression.deltaCompress(offsetVector));
		System.arraycopy(offsetDgap, 0, finalArray, 0, offsetDgap.length);
		
		//Contexts are stored as they are
		int[] contextArray = new int[contextVector.size()];
		for(int i =0;i<contextVector.size();i++)
			contextArray[i]= contextVector.get(i);
		System.arraycopy(contextArray, 0, finalArray, offsetDgap.length, contextArray.length);
		
		
		//Final array of integers is compressed using var-byte compression
		byte[] finalbytes = MyCompression.encode(finalArray);
		int startOffset = this.currentOffset;
		try{
			file.write(finalbytes);
			this.currentOffset+=finalbytes.length;
			return new OffsetBlock(startOffset, currentOffset);
		}catch(IOException e){
			throw new IOException("Unable to write properly for offsetblocks",e);
		}
		
	}
	
	


	
}
