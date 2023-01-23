package controller;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

public class EjemploAES {
	public static byte[] cifrarAES(String texto, SecretKey clave, byte[] iv) throws Exception {
		// Crear un objeto Cipher para cifrar
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, clave, ivSpec);

		// Cifrar el texto
		byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
		return textoCifrado;
	}

	public static String descifrarAES(byte[] textoCifrado, SecretKey clave, byte[] iv) throws Exception {
		// Crear un objeto Cipher para descifrar
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, clave, ivSpec);

		// Descifrar el texto
		byte[] textoDescifrado = cipher.doFinal(textoCifrado);
		return new String(textoDescifrado, StandardCharsets.UTF_8);
	}

	public static void main(String[] args) {
		try {
			// Generar una clave AES de 16 bytes
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, new SecureRandom());
			SecretKey clave = keyGenerator.generateKey();

			// Generar un vector de inicialización (IV) de 16 bytes
			SecureRandom secureRandom = new SecureRandom();
			byte[] iv = new byte[16];
			secureRandom.nextBytes(iv);

			// Cifrar una cadena de texto
			String texto = "Este es un texto secreto";
			byte[] textoCifrado = cifrarAES(texto, clave, iv);

			// Descifrar el texto
			String textoDescifrado = descifrarAES(textoCifrado, clave, iv);

			System.out.println("Texto original: " + texto);
			System.out.println("Texto cifrado: " + new String(textoCifrado));
			System.out.println("Texto descifrado: " + textoDescifrado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}