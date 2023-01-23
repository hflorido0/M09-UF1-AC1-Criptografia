package controller;

import dao.Reader;
import dao.Writter;
import security.CriptografiaSimetrica;
import security.ClavePublica;
import security.ECDSA;
import security.FirmaDigital;
import security.Hash;
import security.RSA;
import utils.Constants;

public class Controller {
	private CriptografiaSimetrica AES;
	private ClavePublica clavePublica;
	private CriptografiaSimetrica DES;
	private ECDSA ECDSA;
	private Hash Hash;
	private FirmaDigital firmaDigital;
	private RSA RSA;
	
	private static final int LOOPS = 16;
	private static final int FILES = 2;
	private static final String hashtag = "#";
	
	public Controller() {
		this.AES = new CriptografiaSimetrica();
		this.clavePublica = new ClavePublica();
		this.DES = new CriptografiaSimetrica();
		this.ECDSA = new ECDSA();
		this.Hash = new Hash();
		this.firmaDigital = new FirmaDigital();
		this.RSA = new RSA();
	}

	public void init() {
		String toBeEncripted = "Esto es un mensaje encriptado";
		generateEncriptedDocuments(toBeEncripted);
	}
	
	public void generateEncriptedDocuments(String toBeEncripted) {
		
		this.RSA.init();
		this.firmaDigital.init();
		this.ECDSA.init();
		this.DES.init(Constants.DES);
		this.clavePublica.init();
		this.AES.init(Constants.AES);
		
		for (int count = 1; count < FILES; count++) {
			Writter writer = new Writter("output" + count + ".txt");
			
			for (int i = 0; i < LOOPS; i++) {
				
			}
		}
	}
	
	private void decodeEncriptedDocument() {
		for (int count = 1; count < FILES; count++) {
			Reader reader = new Reader("output" + count + ".txt");
			
			for (int i = 0; i < LOOPS; i++) {
				
			}
		}
	}
	
}
