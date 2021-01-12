
/**
 * Defines interface for checking bytes withing file or directory.
 * @author Bishanta
 *
 */
public interface BytePatternCheck {

	/**
	 * Checks each and every byte with a file against a pattern.
	 * @param value each bytes of a file passed
	 * @return false if the byte doesn't match any pattern.
	 */
  public boolean checkNext(byte value);
  /**
   * Goes through each file with a directory and calls method checkFile against those file.
   * @param startDir starting directory.
   * @param bytepattern Object of the class which implements this interface.
   */
  public void checkDirectory(String startDir, BytePattern bytepattern);
  /**
   * Goes through each bytes within a file and calls method checkNext for each byte.
   * @param filepath path of the file
   */
  public void checkFile(String filepath);
  /**
   * Convert byte array to string of hex values.
   * @param bytes array to be converted to hexString.
   * @return hexString value of the given byte.
   */
  public String bytesToHex(byte[] bytes);
}
