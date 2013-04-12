/**
 * 
 */
package parsing;



import indexing.Posting;
import indexing.PostingWriter;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import lexicon.Lexicon;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;


import structures.ContextRegistrar;
import structures.DocumentRegistrar;
import structures.WordRegistrar;

/**
 * @author Darshan
 * 
 */
public class NZFolderReader {
	private String outputDirectoryPath;
	private File folderFile;
	private Map<String, File> indexFileMap, dataFileMap;
	private NZFileReader nzFileReader;

	public Page next() throws IOException {
		Page page;
		if ((page = nzFileReader.next()) != null)
			return page;
		else {
			// switch to the next file pair and call next() recursively over it.
			// will return null condition mentioned below when reaches end of
			// fileset

			if (openNZFileReader())
				return next();
		}
		return null;
	}

	private boolean openNZFileReader() throws FileNotFoundException,
			IOException {
		Object[] dataFiles = dataFileMap.keySet().toArray();
		if (dataFiles.length == 0)
			return false;
		// System.out.println(dataFiles[0]);
		if (nzFileReader != null)
			nzFileReader.close();
		try {
			nzFileReader = new NZFileReader(indexFileMap.remove(dataFiles[0]),
					dataFileMap.remove(dataFiles[0]));
		} catch (EOFException e) {
			return openNZFileReader();
		}

		return true;
	}

	/**
	 * @param folderFile
	 * @throws IOException
	 */
	public NZFolderReader(File folderFile) throws IOException {
		super();
		this.folderFile = folderFile;
		if (folderFile == null)
			throw new IOException("Cannot take a null parameter as folderFile");
		indexFileMap = new HashMap<String, File>();
		dataFileMap = new HashMap<String, File>();
		for (File temp : folderFile.listFiles()) {
			if (temp.getPath().contains("_index"))
				indexFileMap.put(temp.getPath().replaceAll("_index", ""), temp);
			else if (temp.getPath().contains("_data"))
				dataFileMap.put(temp.getPath().replaceAll("_data", ""), temp);

		}
		if (indexFileMap.isEmpty() || dataFileMap.isEmpty()
				|| indexFileMap.size() != dataFileMap.size())
			throw new IOException(
					"There must be equal number of non-zero index and data files in the folder");
		if (!openNZFileReader())
			throw new IOException("Unable to detect any relevant files");
	}

	/**
	 * @return the folderFile
	 */
	public File getFolderFile() {
		return folderFile;
	}

	/**
	 * @param folderFile
	 *            the folderFile to set
	 */
	public void setFolderFile(File folderFile) {
		this.folderFile = folderFile;
	}

	/**
	 * @return the indexFileMap
	 */
	public Map<String, File> getIndexFileMap() {
		return indexFileMap;
	}

	/**
	 * @param indexFileMap
	 *            the indexFileMap to set
	 */
	public void setIndexFileMap(Map<String, File> indexFileMap) {
		this.indexFileMap = indexFileMap;
	}

	/**
	 * @return the dataFileMap
	 */
	public Map<String, File> getDataFileMap() {
		return dataFileMap;
	}

	/**
	 * @param dataFileMap
	 *            the dataFileMap to set
	 */
	public void setDataFileMap(Map<String, File> dataFileMap) {
		this.dataFileMap = dataFileMap;
	}

	/**
	 * @return the nzFileReader
	 */
	public NZFileReader getNzFileReader() {
		return nzFileReader;
	}

	/**
	 * @param nzFileReader
	 *            the nzFileReader to set
	 */
	public void setNzFileReader(NZFileReader nzFileReader) {
		this.nzFileReader = nzFileReader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NZFolderReader [folderFile=" + folderFile + ", indexFileMap="
				+ indexFileMap + ", dataFileMap=" + dataFileMap
				+ ", nzFileReader=" + nzFileReader + "]";
	}

	

}
