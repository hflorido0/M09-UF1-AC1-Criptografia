package security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import utils.Constants;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

public class CriptografiaSimetrica {
	
	public byte[] cifrar(byte[] texto, SecretKey clave, String algoritmo) throws Exception {
		// Crear un objeto Cipher para cifrar
		Cipher cipher = Cipher.getInstance(algoritmo);
		cipher.init(Cipher.ENCRYPT_MODE, clave);

		// Cifrar el texto
		byte[] textoCifrado = cipher.doFinal(texto);
		return textoCifrado;
	}

	public byte[] descifrar( byte[] textoCifrado, SecretKey clave, String algoritmo) throws Exception {
		// Crear un objeto Cipher para descifrar
		
		try {
			Cipher cipher = Cipher.getInstance(algoritmo);
			cipher.init(Cipher.DECRYPT_MODE, clave);
	
			// Descifrar el texto
			byte[] textoDescifrado = cipher.doFinal(textoCifrado);
			return textoDescifrado;
		} catch (Exception e) {
			//System.out.println("ERROR desencripatndo");
		}
		
		return null;
	}
	
}