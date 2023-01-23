package controller;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Criptografia {

	public static byte[] cifrarAES(String texto, SecretKey clave) throws Exception {
		// Crear un objeto Cipher para cifrar
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, clave);

		// Cifrar el texto
		byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
		return textoCifrado;
	}

	public static String descifrarAES(byte[] textoCifrado, SecretKey clave) throws Exception {
		// Crear un objeto Cipher para descifrar
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, clave);

		// Descifrar el texto
		byte[] textoDescifrado = cipher.doFinal(textoCifrado);
		return new String(textoDescifrado, StandardCharsets.UTF_8);
	}

    
    public static byte[] cifrarRSA(byte[] texto, KeyPair claves) throws Exception {
        // Crear un objeto Cipher para cifrar
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, claves.getPublic());

        // Cifrar el texto
        byte[] textoCifrado = cipher.doFinal(texto);
        return textoCifrado;
    }

    public static byte[] descifrarRSA(byte[] textoCifrado, KeyPair claves) throws Exception {
        // Crear un objeto Cipher para descifrar
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, claves.getPrivate());

        // Descifrar el texto
        byte[] textoDescifrado = cipher.doFinal(textoCifrado);
        return textoDescifrado;
    }
    
    public static void main(String[] args) {
        try {
            // Generar un par de claves RSA
        	// Generar una clave AES de 16 bytes
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, new SecureRandom());
			SecretKey clave = keyGenerator.generateKey();
			
            // Generar un par de claves RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair claves = keyGen.generateKeyPair();

			String texto = "Este es un texto secreto";
	
			// Cifrar una cadena de texto AES
			byte[] textoCifradoAES = cifrarAES(texto, clave);
			
			// Cifrar una cadena de texto RSA
            byte[] textoCifradoRSA = cifrarRSA(texto.getBytes(), claves);

			// Descifrar el texto AES
			String textoDescifradoAES = descifrarAES(textoCifradoAES, clave);

            // Descifrar el texto
            byte[] textoDescifradoRSA = descifrarRSA(textoCifradoRSA, claves);


            System.out.println("Texto original: " + texto);
            System.out.println("Texto cifrado AES: " + new String(textoCifradoAES));
            System.out.println("Texto cifrado RSA: " + new String(textoCifradoRSA));
            System.out.println("Texto descifrado AES: " + textoDescifradoAES);
            System.out.println("Texto descifrado RSA: " + new String(textoDescifradoRSA));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}