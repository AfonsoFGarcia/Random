import org.jfugue.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;

public class Music {
	static String keyEnc = "a tua prima de 4";

	public static void main(String[] args) throws Exception {
		System.out.print("Please enter the text to encrypt: ");
		String t = System.console().readLine();
		System.out.println();

		byte[] ciphertext = encrypt(t);

		String p = getInstrument(ciphertext);

		for(byte b : ciphertext) {
			p += byteToNote(b);
		}

		System.out.println("Your song was saved in cryptosong.mid");
		System.out.print("It is: ");
		System.out.println(p);

		Player player = new Player();
		Pattern pattern = new Pattern(p);
		player.saveMidi(pattern, new File("cryptosong.mid"));
	}

	public static byte[] encrypt(String plainText) throws Exception {
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
	    SecretKeySpec key = new SecretKeySpec(keyEnc.getBytes("UTF-8"), "AES");
	    SecureRandom random = new SecureRandom();
        byte iv[] = new byte[16];//generate random 16 byte IV AES is always 16bytes
        random.nextBytes(iv);
	    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(iv));
	    return cipher.doFinal(plainText.getBytes("UTF-8"));
  	}

  	public static String getInstrument(byte[] b) {
  		String i = "I";
  		Integer value = (0xff&b[0])%128;
  		return i+value+" ";
  	}

  	public static String byteToNote(byte b) {
  		Integer modifier = (0xc0&b)/64;
  		Integer value = (0x3f&b)+32;
  		String ret = "[" + value + "]";

  		switch(modifier) {
  			case 0:
  			default: return ret + "h ";
  			case 1: return ret + "q ";
  			case 2: return ret + "i ";
  			case 3: return ret + "s ";
  		}
  	}
}
