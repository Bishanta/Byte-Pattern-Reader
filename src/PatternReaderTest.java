import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PatternReaderTest {
	BytePattern bytepattern= new BytePattern();
	LoadPattern loadpattern= new LoadPattern();
	MainPanel mainpanel= new MainPanel();
	String testText = "Hello this is test file containing some patterns such as ABC and XYZ at various places.";
	
	
	@Test
	public void checkNexttest() {

		boolean test = true;
		for(int i = 0; i < testText.length(); i++) {

			test = bytepattern.checkNext((byte) testText.charAt(i));
			
			if(testText.charAt(i) == 'C' || testText.charAt(i) == 'Z') {
				assertEquals(true, test);//tests if pattern is found at the end of ABC and XYZ.
			}
			else {
				assertEquals(false, test); //test if pattern return false for every other char.
			}

		}
		assertEquals(false, test); //checks if value of test is false.
	}
	
	@Test
	public void checkDirectoryTest() {
		bytepattern.checkDirectory("", bytepattern);//checkDirectory method test.
	}
	
	@Test
	public void checkFileTest() {
		bytepattern.checkFile("");
	}
	
	@Test
	public void bytesToHexTest() {
		byte[] testByte = {65,66,67};
		String result = "";
		result = bytepattern.bytesToHex(testByte);
		
		assertEquals("414243", result);
	}
	
	@Test
	public void clearTest() {
		mainpanel.clear();
		assertEquals(0,bytepattern.file_num);
		assertEquals(0, bytepattern.pattern_num);
	}

}
