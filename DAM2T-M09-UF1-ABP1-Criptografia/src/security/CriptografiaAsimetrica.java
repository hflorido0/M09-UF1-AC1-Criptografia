package security;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import utils.Constants;

public class CriptografiaAsimetrica {

	public String firmar(String mensaje, KeyPair claves, String algoritmo) throws Exception {
		Signature firma = Signature.getInstance(Constants.ECDSA);
		if (Constants.FIRMADIGITAL.equals(algoritmo))
			firma = Signature.getInstance(Constants.SHA_RSA);
		firma.initSign(claves.getPrivate());
		firma.update(mensaje.getBytes(StandardCharsets.UTF_8));
		return new String(firma.sign());
	}

	public boolean verificar(String mensaje, String firmaDigital, KeyPair claves, String algoritmo) throws Exception {
		Signature firma = Signature.getInstance("ECDSA");
		if (Constants.FIRMADIGITAL.equals(algoritmo))
			firma = Signature.getInstance(Constants.SHA_RSA);
		firma.initVerify(claves.getPublic());
		firma.update(mensaje.getBytes(StandardCharsets.UTF_8));
		return firma.verify(firmaDigital.getBytes(StandardCharsets.UTF_8));
	}

	public byte[] cifrarRSA(String texto, KeyPair claves) throws Exception {
		// Crear un objeto Cipher para cifrar
		Cipher cipher = Cipher.getInstance(Constants.RSA);
		cipher.init(Cipher.ENCRYPT_MODE, claves.getPublic());

		// Cifrar el texto
		byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
		return textoCifrado;
	}

	public String descifrarRSA(byte[] textoCifrado, KeyPair claves) {
		// Crear un objeto Cipher para descifrar

		try {
			Cipher cipher = Cipher.getInstance(Constants.RSA);
			cipher.init(Cipher.DECRYPT_MODE, claves.getPrivate());

			// Descifrar el texto
			byte[] textoDescifrado = cipher.doFinal(textoCifrado);
			return new String(textoDescifrado);
		} catch (Exception e) {
			//System.out.println("ERROR desencriptando");
		}
		return "";
	}

	public byte[] cifrarCP(String texto, PublicKey claves) throws Exception {
		// Generar una clave simétrica AES
		KeyGenerator keyGen = KeyGenerator.getInstance(Constants.AES);
		keyGen.init(256);
		SecretKey claveSimetrica = keyGen.generateKey();

		// Crear un objeto Cipher para cifrar con RSA
		Cipher cipher = Cipher.getInstance(Constants.RSA);
		cipher.init(Cipher.WRAP_MODE, claves);

		// Envolver la clave simétrica con la clave pública
		byte[] claveEnvoltorio = cipher.wrap(claveSimetrica);

		// Crear un objeto Cipher para cifrar con AES
		cipher = Cipher.getInstance(Constants.AES);
		cipher.init(Cipher.ENCRYPT_MODE, claveSimetrica);

		// Cifrar el texto
		byte[] textoCifrado = cipher.doFinal(texto.getBytes());

		// Concatenar el envoltorio de clave con el texto cifrado
		byte[] resultado = new byte[claveEnvoltorio.length + textoCifrado.length];
		System.arraycopy(claveEnvoltorio, 0, resultado, 0, claveEnvoltorio.length);
		System.arraycopy(textoCifrado, 0, resultado, claveEnvoltorio.length, textoCifrado.length);

		return resultado;
	}

	public String descifrarCP(byte[] datosCifrados, PrivateKey claves) {
		// Obtener el envoltorio de clave

		try {

			byte[] claveEnvoltorio = new byte[256];
			System.arraycopy(datosCifrados, 0, claveEnvoltorio, 0, 256);

			// Crear un objeto Cipher para descifrar el envoltorio con RSA
			Cipher cipher = Cipher.getInstance(Constants.RSA);
			cipher.init(Cipher.UNWRAP_MODE, claves);

			// Desenvolver la clave simétrica
			SecretKey claveSimetrica = (SecretKey) cipher.unwrap(claveEnvoltorio, "AES", Cipher.SECRET_KEY);

			// Obtener el texto cifrado
			int longitudTextoCifrado = datosCifrados.length - 256;
			byte[] textoCifrado = new byte[longitudTextoCifrado];
			System.arraycopy(datosCifrados, 256, textoCifrado, 0, longitudTextoCifrado);

			// Crear un objeto Cipher para descifrar el texto con AES
			cipher = Cipher.getInstance(Constants.AES);
			cipher.init(Cipher.DECRYPT_MODE, claveSimetrica);

			// Descifrar el texto
			byte[] textoDescifrado = cipher.doFinal(textoCifrado);
			return new String(textoDescifrado);
		} catch (Exception e) {
			//System.out.println("ERROR desencriptando");
		}
		return "";
	}

}