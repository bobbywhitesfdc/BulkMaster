package bobby.sfdc.prototype.util;

import java.io.File;

import bobby.sfdc.prototype.BulkMaster;
import bobby.sfdc.prototype.BulkMaster.Commands;
import bobby.sfdc.prototype.BulkMaster.Flags;

public class CommandlineHelper {
	private final BulkMaster master;

	public CommandlineHelper(BulkMaster master) {
		this.master=master;
	}

	/**
	 * Process the Commandline Flags to set parameters BulkMaster utility
	 * @param args
	 */
	public void setOptionsFromCommandlineFlags(String[] args) {
		try {
			processFlags(args); 
		} catch (IllegalArgumentException ex) {
			printSyntaxStatement();
			throw ex;
		}
		return;
	}

	private void processFlags(String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("Invalid commandline arguments");
		}
		
		/**
		 * Skip the initial non-flag arguments
		 */
		int startingPlace=0;
		while (startingPlace < args.length && !isFlag(args[startingPlace])) {
			startingPlace++;
		}
		
		/**
		 * Iterate through the Flags
		 */
		for (int i=startingPlace; i < args.length ;i++) {
			String flagPart = args[i];
			if (!isFlag(flagPart)) {
				throw new IllegalArgumentException("Expected a flag starting with -");
			} 
			
			if (!BulkMaster.Flags.isValidFlag(flagPart)) {
				throw new IllegalArgumentException("Invalid flag: " + flagPart);	
			}
			
			String valuePart = "";
			if (i+1 < args.length && !isFlag(args[i+1])) { 
				valuePart=args[++i];  // Pre-increment
			}
			
			
			
			if (Flags.JOBID.isFlagSet(flagPart)) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Invalid JobID!"+valuePart);
				} else {
					master.setJobId(valuePart);	
				}
			}
			
			if (Flags.LIST.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.LIST);
			}
			
			if (Flags.STATUS.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.STATUS);
			}
			if (Flags.CLOSEJOB.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.CLOSEJOB);
			}
			if (Flags.ABORTJOB.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.ABORTJOB);
			}
			
			if (Flags.INSERT.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.INSERT);

			}
			if (Flags.DELETE.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.DELETE);
			}
	
			if (Flags.RESULTS.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.RESULTS);
			}
			
			if (Flags.OBJECTNAME.isFlagSet(flagPart)) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing Objectname!");
				} else {
					master.setObjectName(valuePart);	
				}				
			}
			if (Flags.EXTERNALID.isFlagSet(flagPart)) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing ExternalID fieldname!");
				} else {
					master.setExternalIdFieldName(valuePart);	
				}				
			}
			
			if (Flags.INPUTFILE.isFlagSet(flagPart)) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing filename!");
				} else {
					validateFile(valuePart);
					master.setInputFileName(valuePart);
				}				
			}
			if (Flags.OUTPUTDIR.isFlagSet(flagPart)) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing output directory name!");
				} else {
					validateDirectory(valuePart);
					master.setOutputDir(valuePart);
				}				
			}
			if (Flags.POLL.isFlagSet(flagPart)) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing polling interval!");
				} else {
					master.setPollingInterval(Integer.parseInt(valuePart));
				}				
			}
			
			if (Flags.QUERY.isFlagSet(flagPart)) {
				master.setCurrentCommand(Commands.QUERY);
				
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing query string!");
				} else {
					master.setQueryString(valuePart);
				}				
			}
			if (Flags.PKCHUNKING.isFlagSet(flagPart)) {
				master.setPkChunkingEnabled(true);			
			}			
		}
	}

	private boolean isFlag(String arg) {
		return arg != null && arg.startsWith("-");
	}

	/**
	 * Validate that an input file exists, is a file, and is readable
	 * @param inputFileName
	 * @throws IllegalArgumentException
	 */
	public void validateFile(String inputFileName) throws IllegalArgumentException {
		File fileToValidate = new File(inputFileName);
		if (!fileToValidate.exists()) {
			throw new IllegalArgumentException("File does not exist: " + inputFileName);
		} else if (fileToValidate.isDirectory()) {
			throw new IllegalArgumentException("File name refers to a Directory: " + inputFileName);			
		} else if (!fileToValidate.canRead()) {
			throw new IllegalArgumentException("File is not readable: " + inputFileName);
		}
	}

	/**
	 * Validates that the output Directory exists, is a directory, and is writable
	 * @param outputDir
	 * @throws IllegalArgumentException
	 */
	public void validateDirectory(String outputDir) throws IllegalArgumentException {
		File dir = new File(outputDir);
		if (!dir.exists()) {
			throw new IllegalArgumentException("Directory does not exist: " + outputDir);
		} else if (!dir.isDirectory()) {
			throw new IllegalArgumentException("Directory name refers to a file: " + outputDir);			
		} else if (!dir.canWrite()) {
			throw new IllegalArgumentException("Directory is not writable: " + outputDir);
		}	
	}

	/**
	 * 
	 */
	public static void printSyntaxStatement() {
		System.out.println("userid and password are required parameters!:\n  myuser@example.com mypassword");
		System.out.println("Syntax:  BulkMaster <username> <password> [loginURL] [ccEmailAddress] [FLAGS]");
		System.out.println("\nIf omitted, default Login URL=" + BulkMaster.DEFAULT_LOGIN_URL);
		
		System.out.println("\n\nFlags:");
		
		for (BulkMaster.Flags flag : BulkMaster.Flags.values()) {
			System.out.println("-"+ flag.getLabel() + " " + flag.getDescription());
		}
	}

}
