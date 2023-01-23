package security;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import utils.Constants;

public class RSA {
	
	private KeyPair claves;

	public void init() {
        KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance(Constants.RSA);
	        keyGen.initialize(2048);
	        claves = keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void init(String clavePublica, String clavePrivada) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(Constants.RSA);
			this.claves = new KeyPair(keyFactory.generatePublic(new X509EncodedKeySpec(clavePublica.getBytes(StandardCharsets.UTF_8))),
					keyFactory.generatePrivate(new X509EncodedKeySpec(clavePrivada.getBytes(StandardCharsets.UTF_8))));
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public String getClaves() {
		return new String(this.claves.getPublic().getEncoded()) + "#" + new String(this.claves.getPrivate().getEncoded());
	}
	
    public String cifrarRSA(String texto) throws Exception {
        // Crear un objeto Cipher para cifrar
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.ENCRYPT_MODE, claves.getPublic());

        // Cifrar el texto
        byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
        return new String(textoCifrado);
    }

    public String descifrarRSA(String textoCifrado) throws Exception {
        // Crear un objeto Cipher para descifrar
        Cipher cipher = Cipher.getInstance(Constants.RSA);
        cipher.init(Cipher.DECRYPT_MODE, claves.getPrivate());

        // Descifrar el texto
        byte[] textoDescifrado = cipher.doFinal(textoCifrado.getBytes(StandardCharsets.UTF_8));
        return new String(textoDescifrado);
    }
}
