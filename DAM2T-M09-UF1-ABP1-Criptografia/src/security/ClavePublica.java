package security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.*;

public class ClavePublica {
	
	private KeyPair claves;
	
	public void init() {
        try {
            // Generar un par de claves
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);

            claves = keyGen.generateKeyPair();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void init(String clavePublica, String clavePrivada) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
			this.claves = new KeyPair(keyFactory.generatePublic(new X509EncodedKeySpec(clavePublica.getBytes(StandardCharsets.UTF_8))),
					keyFactory.generatePrivate(new X509EncodedKeySpec(clavePrivada.getBytes(StandardCharsets.UTF_8))));
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
    public String cifrar(String texto) throws Exception {
        // Generar una clave simétrica AES
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey claveSimetrica = keyGen.generateKey();

        // Crear un objeto Cipher para cifrar con RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, claves.getPublic());

        // Envolver la clave simétrica con la clave pública
        byte[] claveEnvoltorio = cipher.wrap(claveSimetrica);

        // Crear un objeto Cipher para cifrar con AES
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, claveSimetrica);

        // Cifrar el texto
        byte[] textoCifrado = cipher.doFinal(texto.getBytes());

        // Concatenar el envoltorio de clave con el texto cifrado
        byte[] resultado = new byte[claveEnvoltorio.length + textoCifrado.length];
        System.arraycopy(claveEnvoltorio, 0, resultado, 0, claveEnvoltorio.length);
        System.arraycopy(textoCifrado, 0, resultado, claveEnvoltorio.length, textoCifrado.length);

        return new String(resultado);
    }

    public String descifrar(String datosCifrados) throws Exception {
        // Obtener el envoltorio de clave
        byte[] claveEnvoltorio = new byte[256];
        System.arraycopy(datosCifrados, 0, claveEnvoltorio, 0, 256);

        // Crear un objeto Cipher para descifrar el envoltorio con RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, claves.getPrivate());

        // Desenvolver la clave simétrica
        SecretKey claveSimetrica = (SecretKey) cipher.unwrap(claveEnvoltorio, "AES", Cipher.SECRET_KEY);

        // Obtener el texto cifrado
        int longitudTextoCifrado = datosCifrados.getBytes(StandardCharsets.UTF_8).length - 256;
        byte[] textoCifrado = new byte[longitudTextoCifrado];
        System.arraycopy(datosCifrados, 256, textoCifrado, 0, longitudTextoCifrado);

        // Crear un objeto Cipher para descifrar el texto con AES
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, claveSimetrica);

        // Descifrar el texto
        byte[] textoDescifrado = cipher.doFinal(textoCifrado);
        return new String(textoDescifrado);
    }
}
