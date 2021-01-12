

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Models an object which allows user to select a pattern file which is then parsed to byte value
 * and stored in an arraylist.
 * @author Bishanta
 *
 */
public class LoadPattern {
	
	/**
	 * Stores true if pattern file contains any error.
	 */
    private boolean error;
    /**
     *Let's user choose pattern file.
     */
	private JFileChooser patternloader;
	/**
	 * Stores the filepath of the pattern file.
	 */
	private String filepath;
	/**
	 * Stores the length of the pattern.
	 */
	int len;
	/**
	 * Stores the byte value of the pattern.
	 */
	byte[] data;
	/**
	 * Stores the the value after spliting pattern value as tokens.
	 */
	String[] linesplit;
	/**
	 * Stores number of errors found inside a pattern file.
	 */
	int error_num;
	/**
	 * Returns a byte array of patterns and will also request to select a pattern file.
	 * 
	 * Let's user to choose a pattern file then parses the select file in an array of bytes.
	 * Ignores the patterns that are formatted wrong and keeps the valid pattern patterns.
	 *  
	 * @return Byte ArrayList containing byte value of each line in pattern file.
	 */
	@SuppressWarnings("resource")
	public ArrayList<byte[]> loadPattern() {
		/**
		 * Stores the byte value of each line in the pattern file.
		 */
		ArrayList<byte[]> value = new ArrayList<byte[]>();
		patternloader = new JFileChooser("patternfiles");
		int i = patternloader.showOpenDialog(null);
		if(i == JFileChooser.APPROVE_OPTION) {
			File f = patternloader.getSelectedFile();
			filepath = f.getPath();
			MainPanel.display_pattern.setText("");
			try {
				BufferedReader br = new BufferedReader(new FileReader(filepath));
				String next = "";
				error_num=0;//reseting error_num value for each file selected.
				//Loops continues until each line of the pattern file is read
				while((next = br.readLine()) !=null) {

					if(!next.isEmpty()) {
						//resetting error to false
						error = false;
				    	linesplit = next.split(" ");
				    	len = linesplit.length;
				    	data = new byte[len];
				    	int j=0;
				    	//for loop for each tokens of a line
				    	for(String linebyte : linesplit) {
				    		
				    		if(linebyte.length() !=2) {
				    			error = true;
				    			error_num++;
				    			//pattern should be of two length hex with spaces between
				    		}
				    		try {
				    			data[j] = (byte) Integer.parseInt(linebyte,16);

				    		}
				    		catch(NumberFormatException e) {
				    			System.out.println("numberformatexception");
				    			error = true;
				    			error_num++;
				    		}
				    		j++;
				    	}//for loop closes.
				    	

				    	
					    	if(!error) {
					    		String pattern = next.replace(" ", "");
								MainPanel.display_pattern.append("\n  " + pattern + "\n");
								System.out.println("\n    " + pattern + "\n");
					    	}
	  
				    	 }//line empty check condition closes
				    	  
				    	  value.add(data);

					}//while loop closes
				if(error_num > 0) {
		    		JOptionPane.showMessageDialog(null, "Total error in the file: " + error_num, "Error", -1);
				}
	
			}catch(Exception e) {
				e.printStackTrace();
			}
		}//JFileChooser approve option condition closes.
		return value;
		
	}

}
