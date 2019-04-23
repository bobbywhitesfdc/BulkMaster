package bobby.sfdc.prototype.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class SizeLimitedWriter extends BufferedWriter {
	public static final String SIZE_LIMIT_WOULD_BE_EXCEEDED = "Size Limit would be exceeded";
	public static final long MEGABYTE = 1024*1024;
	long bytesWritten=0;
	long lastMBAnnounced=0;
	final long maxBytes;
	public static final String NEWLINE_SEPARATOR = System.getProperty("line.separator");
	public static final int NEWLINE_SEPARATOR_LEN = NEWLINE_SEPARATOR.length();

	public SizeLimitedWriter(Writer out) {
		super(out);
		maxBytes=100 * MEGABYTE;
	}

	public SizeLimitedWriter(final Writer out, final int sz) {
		super(out, sz);
		maxBytes=100 * MEGABYTE;
	}
	
	public SizeLimitedWriter(final Writer out, final long maxBytes) {
		super(out);
		this.maxBytes=maxBytes;
	}
	
	/**
	 * Convenience method that writes the line and terminates it
	 * @param line
	 * @throws IOException
	 */
	public void writeLine(final String line) throws IOException {
		checkLimit(line);
		write(line);
		newLine();
	}
	
	private void checkLimit(final String line) throws SizeLimitExceeded {
		if (line==null) {
			throw new IllegalArgumentException("Null line");
		} else {
			long targetLength = bytesWritten + line.length();
			if ((targetLength) > maxBytes) {
				throw new SizeLimitExceeded(SIZE_LIMIT_WOULD_BE_EXCEEDED);
			}
		}
	}

	/**
	 * Keep track of bytes written
	 * @param line the line of text to write
	 **/
	@Override
	public void write(final String line) throws IOException {
		bytesWritten += line.length();
		super.write(line);
	}
	/**
	 * Keep track of bytes written
	 **/
	@Override
	public void newLine() throws IOException {
		bytesWritten += NEWLINE_SEPARATOR_LEN;
		super.newLine();
	}

	public double getBytesWritten() {
		return bytesWritten;
	}
	
	public class SizeLimitExceeded extends IOException {
		private static final long serialVersionUID = -6195202197503740493L;	

		public SizeLimitExceeded(String msg) {
			super(msg);
		}
	}

}
