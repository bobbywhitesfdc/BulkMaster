package bobby.sfdc.prototype.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class to split a CSV file into smaller chunks if required by the Bulk API limit
 * @author bobby.white
 *
 */
public class CSVSplitManager {
	public static final int DEFAULT_SIZE_LIMIT_MB = 100;
	private int sizeLimit=DEFAULT_SIZE_LIMIT_MB;
	
	private int partId;
	private File original;
	private File outputDir;
	
	public static void main(String[] args) {
		try {
			CSVSplitManager mgr = new CSVSplitManager();
			System.out.println(mgr.needsSplitting(args[0]));
			mgr.splitFile(new File(args[0]),new File(args[1]));
			
		} catch (IOException ex) {
			
		}

	}

	public CSVSplitManager() {
	}
	
	/**
	 * Determine if the file needs to be split
	 * @param inputFileName
	 * @return true if it exceeds the size limit
	 */
	public boolean needsSplitting(String inputFileName) {
		File inputFile = new File(inputFileName);
		return inputFile.exists() && (inputFile.length() > getSizeLimitBytes());
	}
	

	/**
	 * Split the original file creating 1 or more files that contain the original records
	 * and are valid CSV files.
	 * Name the split files sequentially and place them in the output directory
	 * 
	 * @param original
	 * @param outputDir
	 * @return
	 * @throws IOException 
	 */
	public void splitFile(File original, File outputDir) throws IOException {
		if (!(original.isFile() && original.exists() && original.canRead())) {
			throw new IllegalArgumentException("Original file argument is not a valid readable file");
		}
		
		if (!(outputDir.isDirectory() && outputDir.exists() && outputDir.canWrite())) {
			throw new IllegalArgumentException("Output dir argument is not a valid writable directory");
		}
		
		this.outputDir = outputDir;
		
		BufferedReader reader=null;
		SizeLimitedWriter writer=null;
		
		partId = 1;
		this.original = original;
		
		try {
			reader = new BufferedReader(new FileReader(original));
			final String header = reader.readLine();
			
			writer = getWriter(original,header);		
			
			String line = header;
			
			do {
				line = reader.readLine();
				
				try {
					if (line != null)
						writer.writeLine(line);
					
				} catch(SizeLimitedWriter.SizeLimitExceeded sle) {
					// Close the current File and open a new one
					writer.close();
					writer = getWriter(original,header);
					writer.writeLine(line);
				}
				
			} while (line != null);
			 
			
		} finally {
			if (reader != null)
				reader.close();
			
			if (writer != null) {
				writer.close();
			}
		}
	}

	private SizeLimitedWriter getWriter(File original, final String header) throws IOException {
		SizeLimitedWriter writer = new SizeLimitedWriter(new FileWriter(getPartFile()),getSizeLimitBytes());

		writer.writeLine(header);
		
		return writer;
	}
	
	private File getPartFile() {
		String outputFileName = this.outputDir.getPath() + File.separator + original.getName()+"." + partId++;
		return new File(outputFileName);
	}

	/*
	 * Returns the size limit in bytes (not MB)
	 */
	private long getSizeLimitBytes() {
		return getSizeLimit() * 1024 * 1024;
	}

	/**
	 * @return the sizeLimit
	 */
	public int getSizeLimit() {
		return sizeLimit;
	}

	/**
	 * @param sizeLimit the sizeLimit to set
	 */
	public void setSizeLimit(int sizeLimit) {
		this.sizeLimit = sizeLimit;
	}
}
