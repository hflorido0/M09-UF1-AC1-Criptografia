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
	
	private SecretKey clave;
	private String algoritmo;
	
	public void init(String algoritmo) {
		this.algoritmo = algoritmo;
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance(algoritmo);
			if (this.algoritmo.equals(Constants.DES))
				keyGenerator.init(new SecureRandom());
			else 
				keyGenerator.init(128, new SecureRandom());
			clave = keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public void init(String algoritmo, String clave) {
		this.clave = new SecretKeySpec(clave.getBytes(), algoritmo);
	}
	
	public String cifrarAES(String texto) throws Exception {
		// Crear un objeto Cipher para cifrar
		Cipher cipher = Cipher.getInstance(algoritmo);
		cipher.init(Cipher.ENCRYPT_MODE, clave);

		// Cifrar el texto
		byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
		return new String(textoCifrado);
	}

	public String descifrarAES(String textoCifrado) throws Exception {
		// Crear un objeto Cipher para descifrar
		Cipher cipher = Cipher.getInstance(algoritmo);
		cipher.init(Cipher.DECRYPT_MODE, clave);

		// Descifrar el texto
		byte[] textoDescifrado = cipher.doFinal(textoCifrado.getBytes(StandardCharsets.UTF_8));
		return new String(textoDescifrado, StandardCharsets.UTF_8);
	}
	
}