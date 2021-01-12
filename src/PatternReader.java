import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Driver class for the program.
 * @author Bishanta
 *
 */
public class PatternReader {
	
	/**
	 * Main static method that runs the program.
	 * 
	 * Main method for the driver class of the program that runs all the other
	 * classes.
	 * 
	 * @param args Accepts commandline arguments.
	 */
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Byte Pattern Reader");
		frame.setTitle("Byte Pattern Reader\r\n");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("img/bytepatternicon.png"));

       javax.swing.SwingUtilities.invokeLater(new Runnable() {

		@Override
		public void run() {

	        MainPanel panel = new MainPanel(); 
	        panel.setPreferredSize(new Dimension(860,485));
	        panel.setBackground(new Color(224,224,77));
	        panel.setLayout(null);
	 
	       
	        frame.setJMenuBar(panel.setupMenu());
	        frame.setResizable(false);

	        frame.getContentPane().add(panel);
	        frame.pack();
	        frame.setVisible(true);
		}
    	   
       });


    }
}
