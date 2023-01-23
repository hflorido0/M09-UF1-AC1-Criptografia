package security;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import utils.Constants;

import java.security.Security;

public class ECDSA {
	
	private KeyPair claves;
	
	public void init() {
		try {
			Security.addProvider(new BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Constants.ECDSA);
			keyGen.initialize(256);
			claves = keyGen.generateKeyPair();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(String clavePublica, String clavePrivada) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(Constants.ECDSA);
			this.claves = new KeyPair(keyFactory.generatePublic(new X509EncodedKeySpec(clavePublica.getBytes(StandardCharsets.UTF_8))),
					keyFactory.generatePrivate(new X509EncodedKeySpec(clavePrivada.getBytes(StandardCharsets.UTF_8))));
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public String firmar(String mensaje) throws Exception {
		Signature firma = Signature.getInstance(Constants.ECDSA);
		firma.initSign(claves.getPrivate());
		firma.update(mensaje.getBytes(StandardCharsets.UTF_8));
		byte[] firmaDigital = firma.sign();
		return new String(firmaDigital);
	}

	public boolean verificar(String mensaje, byte[] firmaDigital) throws Exception {
		Signature firma = Signature.getInstance(Constants.ECDSA);
		firma.initVerify(claves.getPublic());
		firma.update(mensaje.getBytes(StandardCharsets.UTF_8));
		return firma.verify(firmaDigital);
	}
	
}