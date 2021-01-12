
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Models an object which sets up the Panel containing entire gui component for user to interact.
 * @author Bishanta
 *
 */
public class MainPanel extends JPanel{
	

	private static final long serialVersionUID = 1L;
	/**
	 *Menu item that let's user load pattern.
	 */
	private JMenuItem File_loadPattern;
	/**
	 * Menu Item that let's user exit the program 
	 */
	private JMenuItem File_exitItem;
	/**
	 * Menu item that let's user select file.
	 */
	private JMenuItem File_selectFile;
	/**
	 * Menu item that let's user select a directory.
	 */
	private JMenuItem File_selectDirectory;
	/**
	 * Reference to the object of BytePattern Class.
	 */
	private BytePattern bytepattern;
	/**
	 * Menu Item That lets user view about the program.
	 */
	private JMenuItem Help_about;
	/**
	 * Reference to the object of LoadPattern class.
	 */
	private LoadPattern loadpattern;
	/**
	 * TextArea for displaying patterns found inside files.
	 */
	public static JTextArea display_output;
	/**
	 * TextArea for displaying pattern of selected pattern file.
	 */
	public static JTextArea display_pattern;
	/**
	 * Gui component for making Textarea scrollable.
	 */
	public static JScrollPane scroll1;
	/**
	 * Gui component for making Textarea scrollable.
	 */
	public static JScrollPane scroll2;
	/**
	 * Stores paths of all files that are to be scanned. 
	 */
	public String filepath;

	/**
	 * Displays total number of files that where selected.
	 */
	static JTextField fileNum;
	/**
	 * Displays total number of pattern found after selecting files.
	 */
	static JTextField patternNum;
	/**
	 * Allows user to select file for scanning.
	 */
	private JFileChooser fileloader;

	/**
	 * Stores single file to be read.
	 */
	private File f;

	/**
	 * Button option for custom JOptionPane dialogbox.
	 */
	String[] buttons = { "Yes","Cancel" };
	/**
	 * Checks where any process is running or not.
	 */
	boolean scan;
	/**
	 * Stores the result of Custom JOptionPane dialogbox.
	 */
	static int result=5;
	/**
	 * Stores name of the directory selected.
	 */
	String directory_name;
	
	/**
	 * Sets up all the necessary Menu component in the as menubar.
	 * 
	 */
	JMenuBar setupMenu() {
		//adding a menubar
		
		ActionListener listener = new ConvertListener();
		
		ImageIcon aboutIcon = new ImageIcon("img/about.png");
		ImageIcon loadIcon = new ImageIcon("img/load.png");
		ImageIcon exitIcon = new ImageIcon("img/exit.png");
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		Image file_icon = new ImageIcon(this.getClass().getResource("file2.png")).getImage();
		fileMenu.setIcon(new ImageIcon(file_icon));
		Image help_icon = new ImageIcon(this.getClass().getResource("help.png")).getImage();
		helpMenu.setIcon(new ImageIcon(help_icon));
		
		Help_about = new JMenuItem("About", aboutIcon);
		Help_about.setMnemonic(KeyEvent.VK_A);
		Help_about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
	    		JOptionPane.showMessageDialog(null, "This program is created to scan files for patterns.\n\n"
	    				+ "<html><p>Author: Bishanta Bhattarai</p></br><hr></html>\n" + "<html><i><small>copyright ©2020 The British College</small></i></html>" , "About",-1);
				
			}
			
		});
		helpMenu.add(Help_about);
		
		/*
		 * Adding Menu Item to load file to read pattern from.
		 */
		File_loadPattern = new JMenuItem("Load Pattern File", loadIcon);
		File_loadPattern.addActionListener(listener);
		File_loadPattern.setMnemonic(KeyEvent.VK_P);
		
		File_selectFile = new JMenuItem("Select File", loadIcon);
		File_selectFile.addActionListener(listener);
		File_selectFile.setMnemonic(KeyEvent.VK_S);
		
		File_selectDirectory = new JMenuItem("Select Directory", loadIcon);
		File_selectDirectory.addActionListener(listener);
		File_selectDirectory.setMnemonic(KeyEvent.VK_D);
		
		File_exitItem = new JMenuItem("Exit", exitIcon);
		File_exitItem.setMnemonic(KeyEvent.VK_E);
		File_exitItem.setToolTipText("Exits the program");
		File_exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//confirmation if a scan is still running. 
				if(scan) {
					MainPanel.result = JOptionPane.showOptionDialog(null, "Do you want to cancel current scan?", "Confirmation",
					        JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);
					
					if(MainPanel.result == 0) System.exit(0);
				}
				else
				System.exit(0);
				
			}
			
		});
		
		fileMenu.add(File_loadPattern);
		fileMenu.add(File_selectFile);
		fileMenu.add(File_selectDirectory);
		fileMenu.add(File_exitItem);
		
		return menuBar;

	}
	
	/**
	 * Sets up all the necessary component inside contentPane of the JFrame.
	 */
	MainPanel() {
		bytepattern = new BytePattern();
		
		//heading label 
		JLabel lbl_heading = new JLabel("<html><h2>Byte Pattern Reader</h2></br></html>");
		lbl_heading.setForeground(Color.WHITE);
		lbl_heading.setFont(new Font("Tekton Pro", Font.BOLD, 20));
		lbl_heading.setBounds(251, 42, 193, 30);
		add(lbl_heading);
		
		//textarea for display
		display_output = new JTextArea();
		display_output.setEditable(false);
		display_output.setFont(new Font("Tekton Pro", Font.PLAIN, 12));
		display_output.setBackground(new Color(206, 227, 151));
		display_output.setVisible(true);
		display_output.setToolTipText("Displays the output of all patterns found!");
		display_output.setBounds(10, 114, 570, 362);

		//scroll bar for textarea
		scroll1 = new JScrollPane(display_output);
		scroll1.setBounds(10, 114, 570, 362);
		add(scroll1);
		
		//text area for rendering content of pattern file
		display_pattern = new JTextArea();
		display_pattern.setFont(new Font("Tekton Pro", Font.PLAIN, 12));
		display_pattern.setEditable(false);
		display_pattern.setBackground(new Color(206, 227, 151));
		display_pattern.setBounds(602, 114, 242, 210);
		display_pattern.setVisible(true);
		display_pattern.setToolTipText("Displays the output of pattern from pattern file");
		display_pattern.setText("\n  414243\n  58595A");

		//scroll bar for textarea
		scroll2 = new JScrollPane(display_pattern);
		scroll2.setBounds(602, 114, 250, 205);
        add(scroll2);
		
		//JLabel for writing found pattern
		JLabel patternFoundlbl = new JLabel("Found Pattern");
		patternFoundlbl.setForeground(Color.WHITE);
		patternFoundlbl.setFont(new Font("Tekton Pro", Font.BOLD, 14));
		patternFoundlbl.setBounds(10, 86, 112, 22);
		add(patternFoundlbl);
		
		//JLabel for writing pattern found:
		JLabel selectPatternlbl = new JLabel("Selected Pattern");
		selectPatternlbl.setFont(new Font("Tekton Pro", Font.BOLD, 14));
		selectPatternlbl.setForeground(Color.WHITE);
		selectPatternlbl.setBounds(602, 86, 112, 22);
		add(selectPatternlbl);
		
		//label for logo.
		JLabel logolbl = new JLabel();
		logolbl.setIcon(new ImageIcon("img/bytepatternicon.png"));
		logolbl.setBounds(200, 42, 41, 29);
		add(logolbl);
		
		//clear button for clearing informations.
		JButton btnClear = new JButton("Clear");
		btnClear.setBackground(new Color(206,227,151));
		btnClear.setBounds(740, 455, 78, 23);
		btnClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
				
			}
			
		});
		add(btnClear);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBackground(new Color(206,227,151));
		btnStop.setBounds(653, 455, 78, 23);
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainPanel.result = 0;
				
			}
			
		});
		add(btnStop);
		
		//Jlabel writing files selected:
		JLabel lblFilesSelected = new JLabel("Flies Selected:");
		lblFilesSelected.setForeground(Color.WHITE);
		lblFilesSelected.setFont(new Font("Tekton Pro", Font.BOLD, 14));
		lblFilesSelected.setBounds(685, 345, 90, 22);
		add(lblFilesSelected);
		
		//JLabel for writing Pattern found:
		JLabel lblPatternFound = new JLabel("Pattern found:");
		lblPatternFound.setForeground(Color.WHITE);
		lblPatternFound.setFont(new Font("Tekton Pro", Font.BOLD, 14));
		lblPatternFound.setBounds(685, 399, 90, 22);
		add(lblPatternFound);
		
		//TextField for displaying number of total files selected
		fileNum = new JTextField();
		fileNum.setBounds(708, 367, 53, 20);
		fileNum.setEditable(false);
		fileNum.setHorizontalAlignment(JTextField.CENTER);
		add(fileNum);
		fileNum.setColumns(10);
		
		//Textfield for displaying number of pattern files in all selected files.
		patternNum = new JTextField();
		patternNum.setBounds(708, 424, 53, 20);
		patternNum.setEditable(false);
		patternNum.setHorizontalAlignment(JTextField.CENTER);
		add(patternNum);
		patternNum.setColumns(10);
		
		//Banner for heading.
		JLabel banner_label = new JLabel("");
		banner_label.setIcon(new ImageIcon("img/bannerjavasized.jpg"));
		banner_label.setBounds(140, 33, 326, 50);
		add(banner_label);
		
		//Label for background image.
		JLabel bg_lbl = new JLabel("New label");
		bg_lbl.setIcon(new ImageIcon("img/polygonbg.jpg"));
		bg_lbl.setBounds(0, 0, 860, 486);
		add(bg_lbl);
	}
	
	/**
	 * Clears all the information in the screen which are useless.
	 * 
	 * Resets all the counts and remove any displayed information that may not be useful anymore.
	 */
	public void clear() {
		fileNum.setText("0");
		patternNum.setText("0");
		display_output.setText("");
		bytepattern.file_num=0;
		bytepattern.pattern_num=0;
	}
	


	/**
	 * Implements action listener for all the user events in the gui.
	 * @author Bishanta
	 *
	 */
	private class ConvertListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//IF user chooses to select a file
			if(e.getSource() == File_selectFile) {
				//if scan is still in progress
				MainPanel.result = 5;
				if(scan) {
					MainPanel.result = JOptionPane.showOptionDialog(null, "Do you want to cancel current scan?", "Confirmation",
					        JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);
					
				}
				//checking if cancel button is pressed.
				if(MainPanel.result !=1) {
					filepath = "";
					fileloader = new JFileChooser("testfiles");
					fileloader.setMultiSelectionEnabled(true);
					int i = fileloader.showOpenDialog(null);
					if(i == JFileChooser.APPROVE_OPTION) {
						f = fileloader.getSelectedFile();
						filepath = f.getPath();

					}
					//preventing checkFile method to be called if user doesn't select file.
					if(!filepath.isEmpty()) {

							scan = true;//signifies start of the scan.
							
							Thread t = new Thread() {
								@Override
								public void run() {
									
									try {
										bytepattern.checkFile(filepath);
									}

									catch(Exception ex) {
										ex.printStackTrace();
										MainPanel.result = 0;//stops the scan
										JOptionPane.showMessageDialog(null, "File not suitable for scanning!" , "Error", -1);							
									}

									
									if(MainPanel.result == 0) 
									MainPanel.display_output.append("\nScan has been canceled!!\n");	
								}
								};
								t.start();
								

								

								scan = false;//for signifying end of directory scan

					}


					
					if(MainPanel.result == 0) 
					MainPanel.display_output.append("\nScan has been canceled!!\n");			

					scan = false;//for signifying end of directory scan
					MainPanel.result = 5;//restoring result value anything except 0 and 1.
					
					
				}

				
			}
			
			//If user chooses to select a directory
			if(e.getSource() == File_selectDirectory) {
				MainPanel.result = 5;
				//if scan is still in progress
				if(scan) {
					MainPanel.result = JOptionPane.showOptionDialog(null, "Do you want to cancel current scan?", "Confirmation",
					        JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);
					
				}
				//checking if cancel button is pressed
				if(MainPanel.result != 1) {
					directory_name = "";
					filepath = "";
					fileloader = new JFileChooser("C:\\Users\\user\\Desktop\\");
					fileloader.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int i = fileloader.showOpenDialog(null);
					if(i == JFileChooser.APPROVE_OPTION) {
						File f = fileloader.getSelectedFile();
						filepath = f.getPath();
						directory_name = f.getName();

					}
					//preventing directory scan if directory is not selected
					if(filepath != "") {
						//Creating thread for handling repetitive tasks.
						Thread t = new Thread() {
							@Override
							public void run() {
								scan = true;
								
								try {
									MainPanel.display_output.append("\n****************************************************************************************************************\n****************************************************************************************************************\n\t\t                Directory: " + directory_name + "(" + new File(filepath).listFiles().length + " files)\n****************************************************************************************************************\n****************************************************************************************************************\n");
									bytepattern.checkDirectory(filepath, bytepattern);
								}
								catch(Exception e) {
									e.printStackTrace();
									MainPanel.result = 0;
									JOptionPane.showMessageDialog(null, "Directory not suitable for scanning!" , "Error", -1);							
								}

								
								if(MainPanel.result == 0) 
								MainPanel.display_output.append("\nScan has been canceled!!\n");			

								scan = false;//for signifying end of directory scan

							}	
							
						};

						t.start();//starting the thread.
					}

				}

			}

			if(e.getSource()==File_loadPattern) {
				loadpattern = new LoadPattern();
				//checking for running processes
				if(scan) {
					MainPanel.result = JOptionPane.showOptionDialog(null, "Do you want to cancel current scan?", "Confirmation",
					        JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);
				}
				
				if(MainPanel.result != 1) 
				bytepattern.patterns = loadpattern.loadPattern();

			}
			
		}
		
	}
	
	

}


