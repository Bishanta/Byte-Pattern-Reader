import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Models an object which checks the user selected file against each pattern value and prints 
 * value in the gui TextArea.
 * @author Bishanta
 *
 */
public class BytePattern implements BytePatternCheck{
	static int countfiles;
	/**
	 * Stores pattern location within the file which is being scanned.
	 */
    public int offset;
    /**
     * Checking postion for pattern value, for each pattern in multidimensional way.
     */
	int[] checkPos = new int[100];
	/**
	 * Stores true for every pattern with a file.
	 */
	public boolean pattern;
	/**
	 * Stores true if atleast one pattern is found within a file.
	 */
	public boolean patternFound;
	/**
	 * Stores number of files being scanned.
	 */
	public int file_num;
	/**
	 * Stores number of pattern inside a file or directory.
	 */
	public int pattern_num=0;
	/**
	 * Stores path of the file being scanned.
	 */
	Path path;
	/**
	 * Stores name of the file being scanned.
	 */
	Path fileName;
	/**
	 * Stores all the charecters of hexadecimal format.
	 */
	private char[] HEX_ARRAY;
    
	/**
	 * Stores all the patterns from file.
	 */
    ArrayList<byte[]> patterns = new ArrayList<byte[]>();

    /**
     * Stores count of number of bytes checked inside a file.
     */
	int count;

    /**
     * Checks if the given value matches the next byte to be checked within the pattern.
     *
     * Each time this method is called it progresses to the next byte within the pattern, until the value does not match
     * or the end of the pattern is reached (in which case a match has been found).
     *
     * @param value the value to be checked against the next byte in the pattern.
     * @return true if the pattern has been matched, false if the pattern has not (yet) matched.
     */
	
    public boolean checkNext(byte value) {

    	//increase count for detemining offset.
    	count++;

    	for(int i = 0; i < patterns.size(); i++) {
    		
    		
    		if(patterns.get(i)[checkPos[i]] == value) {


    			checkPos[i]++;
    			if(checkPos[i] == patterns.get(i).length) {

    				checkPos[i] = 0;

    				offset = count - patterns.get(i).length;
    				
    				MainPanel.display_output.append("        Pattern found: " + bytesToHex(patterns.get(i))+", at offset: " + offset +" (x"+Integer.toHexString(offset) + ") within the file.\n");

    				patternFound = true;
    				return true;
    				
    			}


    		}
    		else if(patterns.get(i)[0] == value) {
    			checkPos[i] = 1;

    		}
    		else {
    			checkPos[i] = 0;
    		}

    	}
    	return false;
    }

    /**
     * Stores default values for pattern to be checked against a file.
     */
    BytePattern() {
    	//default constructor
    	byte [] patternABC = new byte [] {65,66,67};
    	byte [] patternXYZ = new byte [] {88,89,90}; 
    	patterns.add(patternABC);
    	patterns.add(patternXYZ);

    }
    
    /**
     * Takes path of a directory and scan all the folders and well as sub-folder using recursion.
     * 
     * Goes through each folder and directory and calls the method checkFile while again checks all the bytes
     * in that particular files.
     * @param startDir path string value of a directory.
     * @param bytepattern Obj of the bytepattern for accessing it's methods.
     */
	public void checkDirectory(String startDir, BytePattern bytepattern) {
        File dir = new File(startDir);
        File[] files = dir.listFiles();


        if (files != null && files.length > 0) {
            for (File file : files) {
            	
            	if(MainPanel.result == 0) break; //breaks the loop if the process is canceled.
            	
                // Check if the file is a directory
                if (file.isDirectory() && !file.isHidden()) {
                    // We will not print the directory name, just use it as a new
                    // starting point to list files from
                	MainPanel.display_output.append("\n****************************************************************************************************************\n****************************************************************************************************************\n\t\t                Directory: " + file.getName() + "(" + file.listFiles().length + " files)\n****************************************************************************************************************\n****************************************************************************************************************\n");
                    bytepattern.checkDirectory(file.getAbsolutePath(), bytepattern);
                } else if (!file.isHidden()) {
                	BytePattern.countfiles++; 
                    // We can use .length() to get the file size
                	bytepattern.checkFile(file.getPath());

                }
            }
        }

	}
    
    /**
     * Checks the file for each byte against the pattern and displays the pattern that match.
     * 
     * Reads each byte of the byte and calls checkNext method on them which checks those bytes against the pattern
     * value.
     * @param filepath Path of the file as a string. 
     */
	public void checkFile(String filepath) {
		try {
			@SuppressWarnings("resource")
			InputStream br = new BufferedInputStream(new FileInputStream(filepath));
			path = Paths.get(filepath);
			fileName = path.getFileName();
			MainPanel.display_output.append("\n****************************************************************************************************************\n    Filename: " + fileName.toString() + "\n****************************************************************************************************************\n");
			
			
			int next = 0;
			count = 0;
			patternFound = false;

			while((next = br.read()) != -1) {

				byte nextByte = (byte)next;
				
				pattern = checkNext(nextByte);
				
				if(pattern) 
					pattern_num++;//increases pattern_num value if pattern is found!


				if(MainPanel.result == 0) break;//breaks the loop if user decides to discontinue.

			}
			
			file_num++;
			MainPanel.fileNum.setText(Integer.toString(file_num));
			MainPanel.patternNum.setText(Integer.toString(pattern_num));

			//if checkNext never returns true prints no pattern found
			if(!patternFound) {
				MainPanel.display_output.append("        No patterns found");
			}

		}
		catch(FileNotFoundException ex) {
			MainPanel.display_output.append("        file not found\n\n");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		MainPanel.display_output.append("    \n---------------\n");
	}

	/**
	 * Converts Bytes array into hexadecimal character format.
	 * @param bytes Byte array of pattern 
	 * @return String of hex characters derived from byte array.
	 */
    public String bytesToHex(byte[] bytes) {
    	HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    

}