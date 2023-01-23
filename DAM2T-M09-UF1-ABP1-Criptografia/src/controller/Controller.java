package controller;

import java.util.ArrayList;
import java.util.Random;

import dao.Reader;
import dao.Writter;
import security.CriptografiaSimetrica;
import security.ClavePublica;
import security.Criptografia;
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
	private static final String HASTAG = "#";
	
	private String[] randoms = {"#RSA#", "#DES#", "#AES#", "#ECDSA#", "#FD#", "#CP#"};
	private ArrayList<Criptografia> criptografia;
	
	public Controller() {
		this.AES = new CriptografiaSimetrica();
		this.clavePublica = new ClavePublica();
		this.DES = new CriptografiaSimetrica();
		this.ECDSA = new ECDSA();
		this.Hash = new Hash();
		this.firmaDigital = new FirmaDigital();
		this.RSA = new RSA();
		this.criptografia = new ArrayList<>();
		//this.criptografia.add(this.RSA);
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
		
		Random random = new Random();
		
		try {
			for (int count = 1; count < FILES; count++) {
				Writter writer = new Writter("files/output" + count + ".txt");
				StringBuilder result = new StringBuilder();
				
				for (int i = 0; i < LOOPS; i++) {
	
					int rand = random.nextInt(randoms.length);
					String algoritmo = randoms[rand];
					
					result.append(algoritmo);
					result.append(HASTAG);
					
					switch (algoritmo) {
						case "#RSA#": 
	
							result.append(this.RSA.cifrarRSA(toBeEncripted));
							result.append(HASTAG);
							result.append(this.RSA.getClaves());
							result.append(algoritmo);
							
							break;
						case "#DES#":
							
							break;
						case "#AES#":
							
							break;
						case "#ECDSA#":
							
							break;
						case "#FD#":
							
							break;
						case "#CP#":
							
							break;
					}
					
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void decodeEncriptedDocument() {
		for (int count = 1; count < FILES; count++) {
			Reader reader = new Reader("files/output" + count + ".txt");
			
			for (int i = 0; i < LOOPS; i++) {
				
			}
		}
	}
	
}
