
/*Here Alice and Bob are communicating using AEs-128 bit key. Thus both of them should know the key before communication to be able to successfully encrypt and decrypt.
As given key ranges from 0 to 2^^16 which is 16 bits with the preceding 112 bits hardcoded to zero.
Brute forcing all 2^^16 values on the given set of data would give a unique key.

To find the unique key:-
1) Loop from 0 - 2^^24 -1. 
2) For first cipher text pass all 2^^16 keys and try to decrypt the data using AES.
3) On decryption try to match the plaintext with string starting with "Puzzle is ".
4) If a match is found store those keys into a list of keys
5) Run the decryption on the next cipher text using the new key list created.
6) Continue untill a key is found that could decrypt all cipher texts. Return if at any stage no key in the list is able to decrypt the given cipher text.

7) On decryption of all cipher texts use the key to fetch the plain text and convert the string from bytes 10 to 26 into ascii. Name that x
8) At any stage if the x matches the given x then return the hex value of 27 to 43 characters.
9) Display the resulting key of the chosen merkel puzzle in hex.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Q7
{
	  static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	  private static void callAES(String cipher, String keyString, ArrayList<String> list) throws Exception {
			byte[] cipherToByte, IV;
			String originalText;
			SecretKeySpec key;
			key = new SecretKeySpec(convertHexStringToByteArray(keyString), "AES");
			cipherToByte = convertHexStringToByteArray(cipher.substring(32));
			  IV = convertHexStringToByteArray(cipher.substring(0, 32));
			  originalText = new String(decrypt(cipherToByte, key, IV));
			  if(originalText.startsWith("Puzzle is ")) {
				  list.add(keyString);
			  }
		}
	  public static String bytesToHex(byte[] bytes) {
		    char[] hexChars = new char[bytes.length * 2];
		    for (int j = 0; j < bytes.length; j++) {
		        int v = bytes[j] & 0xFF;
		        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
		        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		    }
		    return new String(hexChars);
		}
	  
	  
	  private static byte[] convertHexStringToByteArray(String str) {
		  byte[] val = new byte[str.length() / 2];

		  for (int i = 0; i < val.length; i++) {
		     int index = i * 2;
		     int j = Integer.parseInt(str.substring(index, index + 2), 16);
		     val[i] = (byte) j;
		  }
		  return val;
		  }

	  public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] IV)
			throws Exception
	  {
		  Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		  SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
		  cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(IV));
		  byte[] cipherText = cipher.doFinal(plaintext);
		  return cipherText;
		  }
	 
	  public static byte[] decrypt(byte[] cipherText, SecretKey key, byte[] IV)
			  throws Exception
	  {
		  Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		  SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
		  cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(IV));
		  return cipher.doFinal(cipherText);
		  }
	  
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	  {
		  File file = new File("path/to/encrypted_merkel_puzzle_format.txt"); 
		  BufferedReader br = new BufferedReader(new FileReader(file));
		  String cipher,keyString, keyHex = "0000000000000000000000000000";
		  
		  ArrayList<String> list = new ArrayList<>(),previousList;
		  if((cipher = br.readLine()) != null) {
			  for (int i=65536; i<131072; i++) {
				  keyString = keyHex+Integer.toHexString(i).substring(1);
				  callAES(cipher, keyString, list);
				  }
		  }
		  while ((cipher = br.readLine()) != null) {
			  previousList = (ArrayList<String>) list.clone();
			  list.clear();
			  if(previousList.size()==0) {
				  System.out.println("There is no key in the given key space that could decrypt all the cipher text. Exiting...");
				  return;
			  }
			  for (int i=0; i<previousList.size(); i++) {
				  keyString = previousList.get(i);
				  callAES(cipher, keyString, list);
				  }
			  }
		  if(list.size()==1) {
			  SecretKeySpec key = new SecretKeySpec(convertHexStringToByteArray(list.get(0)), "AES");
			  byte[] cipherToByte, IV;
			  String originalText,xInHex;
			  br.close();
			  br = new BufferedReader(new FileReader(file));
			  while ((cipher = br.readLine()) != null) {
				  cipherToByte = convertHexStringToByteArray(cipher.substring(32));
				  IV = convertHexStringToByteArray(cipher.substring(0, 32));
				  originalText = bytesToHex(decrypt(cipherToByte, key, IV));
				  xInHex = originalText.substring(20, 52);
				  if(xInHex.equalsIgnoreCase("740AC607E4F3A32193DA750FACF38D87")) {
					  System.out.println("Required key: "+originalText.substring(54));
					  break;
				  }
				  }
			  }
		  br.close();
	  }
  }
 