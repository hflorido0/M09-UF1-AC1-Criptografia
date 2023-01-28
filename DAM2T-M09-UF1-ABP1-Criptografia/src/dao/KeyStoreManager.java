package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import utils.Constants;

public class KeyStoreManager {

	private static final String PASSWORD = "myPassword";
	private static final String KEY = "myKey";
	private static final String ALGORITHM = "JCEKS";

	public KeyPair generateKeyPair(String algoritmo) throws NoSuchAlgorithmException {

		if (Constants.ECDSA.equals(algoritmo))
			Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator keyGen;
		
		if (Constants.RSA.equals(algoritmo) || Constants.FIRMADIGITAL.equals(algoritmo)) {
			keyGen = KeyPairGenerator.getInstance(Constants.RSA);
			keyGen.initialize(2048);
		} else if (Constants.ECDSA.equals(algoritmo)) {
			keyGen = KeyPairGenerator.getInstance(algoritmo);
			keyGen.initialize(256);
		} else if (Constants.CLAVEPUBLICA.equals(algoritmo)) {
			keyGen = KeyPairGenerator.getInstance(Constants.RSA);
			keyGen.initialize(2048);
		} else {
			keyGen = null;
			System.out.println("ERROR " + algoritmo);
		}
		return keyGen.generateKeyPair();
	}

	public SecretKey generateSecretKey(String algoritmo) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator;
		keyGenerator = KeyGenerator.getInstance(algoritmo);
		if (algoritmo.equals(Constants.DES))
			keyGenerator.init(new SecureRandom());
		else
			keyGenerator.init(128, new SecureRandom());
		return keyGenerator.generateKey();
	}

	public void storeKeyPair(KeyPair kp, String file)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		// Guardar las claves en un archivo
		KeyStore ks = KeyStore.getInstance(ALGORITHM);
		ks.load(null, null);
		
		FileInputStream fis = new FileInputStream("files/certificate.pem");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate certificate = cf.generateCertificate(fis);
		Certificate[] certificateChain = new Certificate[] { certificate };
		
		ks.setKeyEntry(KEY, kp.getPrivate(), PASSWORD.toCharArray(), certificateChain);
		FileOutputStream fos = new FileOutputStream(file);
		ks.store(fos, PASSWORD.toCharArray());
		fos.close();
	}

	public void storeSecretKey(SecretKey secretKey, String file)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance(ALGORITHM);
		ks.load(null, null);
		ks.setKeyEntry(KEY, secretKey, PASSWORD.toCharArray(), null);
		FileOutputStream fos = new FileOutputStream(file);
		ks.store(fos, PASSWORD.toCharArray());
		fos.close();
	}

	public KeyPair getKeyPair(String file) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, UnrecoverableKeyException {
		// Cargar las claves desde el archivo
		KeyStore ks2 = KeyStore.getInstance(ALGORITHM);
		FileInputStream fis = new FileInputStream(file);
		ks2.load(fis, PASSWORD.toCharArray());
		KeyPair kp2 = new KeyPair(ks2.getCertificate(KEY).getPublicKey(),
				(PrivateKey) ks2.getKey(KEY, PASSWORD.toCharArray()));
		return kp2;
	}

	public SecretKey getSecretKey(String file) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, UnrecoverableKeyException {
		KeyStore ks2 = KeyStore.getInstance(ALGORITHM);
		FileInputStream fis = new FileInputStream(file);
		ks2.load(fis, PASSWORD.toCharArray());
		SecretKey secretKey2 = (SecretKey) ks2.getKey(KEY, PASSWORD.toCharArray());
		return secretKey2;
	}
}
