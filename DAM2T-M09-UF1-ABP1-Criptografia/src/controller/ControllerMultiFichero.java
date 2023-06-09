package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.util.Random;

import javax.crypto.SecretKey;

import dao.KeyStoreManager;
import security.CriptografiaAsimetrica;
import security.CriptografiaSimetrica;
import security.Hash;
import utils.Constants;

public class ControllerMultiFichero {
	private CriptografiaSimetrica AES;
	private CriptografiaAsimetrica clavePublica;
	private CriptografiaSimetrica DES;
	private CriptografiaAsimetrica RSA;
	private KeyStoreManager keyStoreManager;
	
	private CriptografiaAsimetrica ECDSA;
	private Hash Hash;
	private CriptografiaAsimetrica firmaDigital;
	 
	private static final int FILES = 2;
	private static final int LOOPS = 12;
	private static final String SEPARADOR = "#@@#";
	
	private SecretKey LDES;
	private SecretKey LAES;
	private KeyPair LCP;
	private KeyPair LRSA;
	
	//private String[] randoms = {"RSA", "DES", "AES", "CP"};
	private String[] randoms = { "AES"};
	
	public ControllerMultiFichero() {
		this.AES = new CriptografiaSimetrica();
		this.clavePublica = new CriptografiaAsimetrica();
		this.DES = new CriptografiaSimetrica();
		this.RSA = new CriptografiaAsimetrica();
		
		this.keyStoreManager = new KeyStoreManager();

		this.ECDSA = new CriptografiaAsimetrica();
		this.Hash = new Hash();
		this.firmaDigital = new CriptografiaAsimetrica();
	}

	public void init() {
		String toBeEncripted = "Esto es un mensaje encriptado";
		try {
			//generateEncriptedDocuments(toBeEncripted);
			//decodeEncriptedDocument();
			generateMultiencriptedDocument(toBeEncripted);
			decodeEncriptedDocument();
			System.out.println("FINISHED");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public void generateEncriptedDocuments(String toBeEncripted) {
		
		
		Random random = new Random();
		
		try {
			for (int count = 1; count <= FILES; count++) {
				
				StringBuilder result = new StringBuilder();
				
				KeyPair RSA = this.keyStoreManager.generateKeyPair(Constants.RSA);
				KeyPair CP = this.keyStoreManager.generateKeyPair(Constants.CLAVEPUBLICA);
				SecretKey DES = this.keyStoreManager.generateSecretKey(Constants.DES);
				SecretKey AES = this.keyStoreManager.generateSecretKey(Constants.AES);
				

				this.keyStoreManager.storeSecretKey(DES, "files/key1-" + count + ".jceks");
				this.keyStoreManager.storeSecretKey(AES, "files/key2-" + count + ".jceks");
				this.keyStoreManager.storeKeyPair(CP,  "files/key3-" + count + ".jceks");
				this.keyStoreManager.storeKeyPair(RSA,  "files/key4-" + count + ".jceks");
				
				
				//Firmas digitales TODO: not used
				KeyPair ECDSA = this.keyStoreManager.generateKeyPair(Constants.ECDSA);
				KeyPair FD = this.keyStoreManager.generateKeyPair(Constants.FIRMADIGITAL);
				this.keyStoreManager.storeKeyPair(ECDSA,  "files/key5-" + count + ".jceks");
				this.keyStoreManager.storeKeyPair(FD,  "files/key6-" + count + ".jceks");
				
				
				for (int i = 0; i < toBeEncripted.split(" ").length; i++) {

					FileOutputStream fos = new FileOutputStream("files/output" + count + "-" + i + ".txt");
	
					int rand = random.nextInt(randoms.length);
					String algoritmo = randoms[rand];
					
					byte[] encripted = new byte[] {};
					System.out.println(algoritmo);
					
					switch (algoritmo) {
						case "RSA": 
							
							encripted = this.RSA.cifrarRSA(toBeEncripted.split(" ")[i], RSA);
							System.out.println(this.RSA.descifrarRSA(encripted, RSA));
							break;
						case "DES":

							encripted = this.DES.cifrar(toBeEncripted.split(" ")[i], DES, Constants.DES);
							System.out.println(encripted);
							System.out.println(this.DES.descifrar(encripted, DES, Constants.DES));
							
							break;
						case "AES":

							encripted = this.AES.cifrar(toBeEncripted.split(" ")[i], AES, Constants.AES);
							System.out.println(encripted);
							System.out.println(this.AES.descifrar(encripted,  AES, Constants.AES));
							
							break;
						case "CP":

							encripted = this.clavePublica.cifrarCP(toBeEncripted.split(" ")[i], CP.getPublic());
							System.out.println(this.RSA.descifrarCP(encripted, CP.getPrivate()));
							
							break;
							
						//FIRMAS DIGITALES
						case "ECDSA":

							//encripted = this.ECDSA.firmar(toBeEncripted.split(" ")[i], ECDSA, Constants.ECDSA);
							//System.out.println(this.RSA.descifrarRSA(encripted,  this.keyStoreManager.generateKeyPair(Constants.RSA)));
							
							break;
						case "FD":

							//encripted = this.firmaDigital.firmar(toBeEncripted.split(" ")[i], FD, Constants.FIRMADIGITAL);
							//System.out.println(this.RSA.descifrarRSA(encripted,  this.keyStoreManager.generateKeyPair(Constants.RSA)));
							
							break;
					}
					
					fos.write(encripted);
					fos.close();
				}
				System.out.println("-------------");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void decodeEncriptedDocument() throws Exception {
		for (int count = 1; count <= FILES; count++) {

			SecretKey DES = this.keyStoreManager.getSecretKey("files/key1-" + count + ".jceks");
			SecretKey AES = this.keyStoreManager.getSecretKey("files/key2-" + count + ".jceks");
			KeyPair CP = this.keyStoreManager.getKeyPair("files/key3-" + count + ".jceks");
			KeyPair RSA = this.keyStoreManager.getKeyPair("files/key4-" + count + ".jceks");
			
			//FIRMAS DIGITALES
			KeyPair ECDSA = this.keyStoreManager.getKeyPair("files/key5-" + count + ".jceks");
			KeyPair FD = this.keyStoreManager.getKeyPair("files/key6-" + count + ".jceks");
			
			for (int i = 0; i < 5; i++) {

				FileInputStream fis = new FileInputStream("files/output" + count + "-" + i + ".txt");
				byte[] data = new byte[fis.available()];
				fis.read(data);
				fis.close();

				System.out.println(this.AES.descifrar(data, AES, Constants.AES));
				System.out.println(this.clavePublica.descifrarCP(data, RSA.getPrivate()));
				System.out.println(this.RSA.descifrarRSA(data, RSA));
				System.out.println(this.DES.descifrar(data, DES, Constants.DES));
			}
		}
	}
	*/

	public void generateMultiencriptedDocument(String toBeEncripted) {
		
		
		Random random = new Random();
		
		try {
			for (int count = 1; count <= FILES; count++) {
				
				StringBuilder result = new StringBuilder();
				
				KeyPair RSA = this.keyStoreManager.generateKeyPair(Constants.RSA);
				KeyPair CP = this.keyStoreManager.generateKeyPair(Constants.CLAVEPUBLICA);
				SecretKey DES = this.keyStoreManager.generateSecretKey(Constants.DES);
				SecretKey AES = this.keyStoreManager.generateSecretKey(Constants.AES);
				

				this.keyStoreManager.storeSecretKey(DES, "files/key1-" + count + ".jceks");
				this.keyStoreManager.storeSecretKey(AES, "files/key2-" + count + ".jceks");
				this.keyStoreManager.storeKeyPair(CP,  "files/key3-" + count + ".jceks");
				this.keyStoreManager.storeKeyPair(RSA,  "files/key4-" + count + ".jceks");
				

				FileOutputStream fos = new FileOutputStream("files/output" + count + ".txt");
				byte[] encripted = this.RSA.cifrarRSA(toBeEncripted.getBytes(), RSA);
				
				for (int i = 0; i < LOOPS; i++) {
	
					int rand = random.nextInt(randoms.length);
					String algoritmo = randoms[rand];
					
					switch (algoritmo) {
						case "RSA": 
							
							encripted = this.RSA.cifrarRSA(encripted, RSA);
							break;
						case "DES":

							encripted = this.DES.cifrar(encripted, DES, Constants.DES);
							break;
						case "AES":

							encripted = this.AES.cifrar(encripted, AES, Constants.AES);
							break;
						case "CP":

							encripted = this.clavePublica.cifrarCP(encripted, CP.getPublic());
							break;
					}
				}
				System.out.println("-------------");
				
				fos.write(encripted);
				fos.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void decodeEncriptedDocument() throws Exception {
		for (int count = 1; count <= FILES; count++) {

			LDES = this.keyStoreManager.getSecretKey("files/key1-" + count + ".jceks");
			LAES = this.keyStoreManager.getSecretKey("files/key2-" + count + ".jceks");
			LCP = this.keyStoreManager.getKeyPair("files/key3-" + count + ".jceks");
			LRSA = this.keyStoreManager.getKeyPair("files/key4-" + count + ".jceks");
			
			//FIRMAS DIGITALES
			KeyPair ECDSA = this.keyStoreManager.getKeyPair("files/key5-" + count + ".jceks");
			KeyPair FD = this.keyStoreManager.getKeyPair("files/key6-" + count + ".jceks");
			

			FileInputStream fis = new FileInputStream("files/output" + count + ".txt");
			byte[] data = new byte[fis.available()];
			
			fis.read(data);
			fis.close();
				
			System.out.println(new String(recursiva(data, LOOPS)));
		}
	}
	
	public byte[] recursiva (byte[] data, int loops) throws Exception {
		byte[] dataAux;
		
		if (loops == LOOPS) {
			return null;
		} else {
			
			dataAux = this.AES.descifrar(data, LAES, Constants.AES);
			if (new String(dataAux).matches("[a-zA-Z]+")) {
				return dataAux;
			} else {
				if (recursiva(dataAux, loops+1) != null)
					return dataAux;
			}
			
			dataAux = this.clavePublica.descifrarCP(data, LRSA.getPrivate());
			if (new String(dataAux).matches("[a-zA-Z]+")) {
				return dataAux;
			} else {
				if (recursiva(dataAux, loops+1) != null)
					return dataAux;
			}
			
			dataAux = this.RSA.descifrarRSA(data, LRSA);
			if (new String(dataAux).matches("[a-zA-Z]+")) {
				return dataAux;
			} else {
				if (recursiva(dataAux, loops+1) != null)
					return dataAux;
			}
			
			dataAux = this.DES.descifrar(data, LDES, Constants.DES);
			if (new String(dataAux).matches("[a-zA-Z]+")) {
				return dataAux;
			} else {
				if (recursiva(dataAux, loops+1) != null)
					return dataAux;
			}
		}
		
		return null;
		
	}
	
}
