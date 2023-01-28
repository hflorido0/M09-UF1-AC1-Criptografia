package controller;

import java.security.KeyPair;
import java.util.Random;

import javax.crypto.SecretKey;

import dao.KeyStoreManager;
import dao.Reader;
import dao.Writter;
import security.CriptografiaAsimetrica;
import security.CriptografiaSimetrica;
import security.Hash;
import utils.Constants;

public class Controller {
	private CriptografiaSimetrica AES;
	private CriptografiaAsimetrica clavePublica;
	private CriptografiaSimetrica DES;
	private CriptografiaAsimetrica ECDSA;
	private Hash Hash;
	private CriptografiaAsimetrica firmaDigital;
	private CriptografiaAsimetrica RSA;
	private KeyStoreManager keyStoreManager;
	
	private static final String SEPARATOR = "#@@#";
	
	private static final int FILES = 2;
	
	private String[] randoms = {"RSA", "DES", "AES", "ECDSA", "FD", "CP"};
	
	public Controller() {
		this.AES = new CriptografiaSimetrica();
		this.clavePublica = new CriptografiaAsimetrica();
		this.DES = new CriptografiaSimetrica();
		this.ECDSA = new CriptografiaAsimetrica();
		this.Hash = new Hash();
		this.firmaDigital = new CriptografiaAsimetrica();
		this.RSA = new CriptografiaAsimetrica();
		this.keyStoreManager = new KeyStoreManager();
	}

	public void init() {
		String toBeEncripted = "Esto es un mensaje encriptado";
		generateEncriptedDocuments(toBeEncripted);
		//decodeEncriptedDocument();
	}
	
	public void generateEncriptedDocuments(String toBeEncripted) {
		
		
		Random random = new Random();
		
		try {
			for (int count = 1; count <= FILES; count++) {
				Writter writer = new Writter("files/output" + count + ".txt");
				StringBuilder result = new StringBuilder();
				StringBuilder keys = new StringBuilder();
				
				for (int i = 0; i < toBeEncripted.split(" ").length; i++) {
	
					int rand = random.nextInt(randoms.length);
					String algoritmo = randoms[rand];
					
					KeyPair RSA = this.keyStoreManager.generateKeyPair(Constants.RSA);
					KeyPair CP = this.keyStoreManager.generateKeyPair(Constants.CLAVEPUBLICA);
					KeyPair ECDSA = this.keyStoreManager.generateKeyPair(Constants.ECDSA);
					KeyPair FD = this.keyStoreManager.generateKeyPair(Constants.FIRMADIGITAL);
					SecretKey DES = this.keyStoreManager.generateSecretKey(Constants.DES);
					SecretKey AES = this.keyStoreManager.generateSecretKey(Constants.AES);
					
					this.keyStoreManager.storeSecretKey(DES, "files/key1-" + count + ".jceks");
					this.keyStoreManager.storeSecretKey(AES, "files/key2-" + count + ".jceks");
					this.keyStoreManager.storeKeyPair(CP,  "files/key3-" + count + ".jceks");
					this.keyStoreManager.storeKeyPair(ECDSA,  "files/key4-" + count + ".jceks");
					this.keyStoreManager.storeKeyPair(FD,  "files/key5-" + count + ".jceks");
					this.keyStoreManager.storeKeyPair(RSA,  "files/key6-" + count + ".jceks");
					
					
					switch (algoritmo) {
						case "RSA": 
	
							result.append(this.RSA.cifrarRSA(toBeEncripted.split(" ")[i], RSA) + SEPARATOR);
							
							break;
						case "DES":

							result.append(this.DES.cifrar(toBeEncripted.split(" ")[i], DES, Constants.DES) + SEPARATOR);
							
							break;
						case "AES":

							result.append(this.AES.cifrar(toBeEncripted.split(" ")[i], AES, Constants.AES) + SEPARATOR);
							
							break;
						case "ECDSA":

							result.append(this.ECDSA.firmar(toBeEncripted.split(" ")[i], ECDSA, Constants.ECDSA) + SEPARATOR);
							
							break;
						case "FD":

							result.append(this.firmaDigital.firmar(toBeEncripted.split(" ")[i], FD, Constants.FIRMADIGITAL) + SEPARATOR);
							
							break;
						case "CP":

							result.append(this.clavePublica.cifrarCP(toBeEncripted.split(" ")[i], CP) + SEPARATOR);
							
							break;
					}
					
				}
				writer.write(keys.toString());
				writer.write(result.toString());
				writer.closeFile();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void decodeEncriptedDocument() {
		for (int count = 1; count <= FILES; count++) {
			Reader reader = new Reader("files/output" + count + ".txt");
			
			StringBuilder text = new StringBuilder();
			String line = "";
			
			while (( line = reader.read()) != null) {
				text.append(line);
			}
			
			String keys = text.toString().split(SEPARATOR)[0];
			
			String[] values = keys.split("--->");

			
			for (int i = 1; i < text.toString().split(SEPARATOR).length; i++) {
				System.out.println( text.toString().split(SEPARATOR));
			}
			
		}
	}
	
}
