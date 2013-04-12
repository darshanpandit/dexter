/**
 * 
 */
package writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import compression.MyCompression;

/**
 * @author Darshan
 * 
 */
public class OffsetInfoReader {
	private RandomAccessFile file;

	/**
	 * .
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 */
	public OffsetInfoReader(String path) throws FileNotFoundException {
		this.file = new RandomAccessFile(new File(path), "r");
	}

	public OffsetInfo read(OffsetBlock offsetBlock) throws IOException {

		// Reading the compressed block from the list
		byte[] mainBytes = new byte[(offsetBlock.getEndOffset() - offsetBlock
				.getStartOffset())];
		int bytesRead;
		try {
			file.seek(offsetBlock.getStartOffset());
			bytesRead = file.read(mainBytes);
			// If the read bytes where the same length as mentioned in blockInfo
			if (bytesRead == (offsetBlock.getEndOffset() - offsetBlock
					.getStartOffset())) {

				// Decompressing the byte array and putting the returned list to
				// an int[]
				List<Integer> decompressedList = MyCompression
						.decode(mainBytes);
				int[] decompressedArray = new int[decompressedList.size()];
				for (int i = 0; i < decompressedList.size(); i++)
					decompressedArray[i] = decompressedList.get(i);

				int[] offsetArray = new int[((decompressedList.size() / 2))];
				int[] contextArray = new int[((decompressedList.size() / 2))];

				// Offsets are delta Compressed, so we decompress them
				List<Integer> tempList = MyCompression.deltaDeCompress(Arrays
						.copyOfRange(decompressedArray, 0,
								decompressedArray.length / 2));
				for (int i = 0; i < tempList.size(); i++)
					offsetArray[i] = tempList.get(i);

				// Copying array to contextArray
				System.arraycopy(decompressedArray,
						(decompressedList.size() / 2), contextArray, 0,
						(decompressedList.size() / 2));

				// Voila we now have seperated context and offsets and we make a
				// pojo to return the arrays.
				return new OffsetInfo(offsetArray, contextArray);
			}
			// else we throw an exception
			else{
				throw new IOException(("Improper Offsets. Error Reading for "+offsetBlock.toString() ));
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(("Unable to read OffsetBlocks from the disk for "+offsetBlock.toString() ), e);
		}

	}

}
