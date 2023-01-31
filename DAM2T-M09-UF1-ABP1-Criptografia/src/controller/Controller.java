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

public class Controller {
	private CriptografiaSimetrica AES;
	private CriptografiaAsimetrica clavePublica;
	private CriptografiaSimetrica DES;
	private CriptografiaAsimetrica RSA;
	private KeyStoreManager keyStoreManager;
	 
	private static final int FILES = 25;
	private static final int LOOPS = 10;
	
	private SecretKey LDES;
	private SecretKey LAES;
	private KeyPair LCP;
	private KeyPair LRSA;
	
	//private String[] randoms = {"RSA", "DES", "AES", "CP"};
	private String[] randoms = { "DES", "AES", "CP"};
	
	private String[] toBeEncripted = {
			"Hay una fuerza motriz mas poderosa que el vapor la electricidad y la energia atomica la voluntad", 
			"El mundo no esta en peligro por las malas personas sino por aquellas que permiten la maldad",
			"Comienza a manifestarse la madurez cuando sentimos que nuestra preocupacion es mayor por los demas que por nosotros mismos",
			"Si buscas resultados distintos no hagas siempre lo mismo",
			"La vida es muy peligrosa No por las personas que hacen el mal sino por las que se sientan a ver lo que pasa",
			"Es un milagro que la curiosidad sobreviva a la educacion reglada",
			"Al principio todos los pensamientos pertenecen al amor Despues todo el amor pertenece a los pensamientos",
			"Los grandes espiritus siempre han encontrado una violenta oposicion de parte de mentes mediocres",
			"El hombre encuentra a Dios detras de cada puerta que la ciencia logra abrir",
			"Cuando me preguntaron sobre algun arma capaz de contrarrestar el poder de la bomba atomica yo sugeri la mejor de todas La paz",
			"Una velada en que todos los presentes esten absolutamente de acuerdo es una velada perdida",
			"En los momentos de crisis solo la imaginacion es mas importante que el conocimiento",
			"Si tu intencion es describir la verdad hazlo con sencillez y la elegancia dejasela al sastre",
			"Hay dos maneras de vivir su vida una como si nada es un milagro la otra es como si todo es un milagro",
			"Los ideales que iluminan mi camino y una y otra vez me han dado coraje para enfrentar la vida con alegria han sido la amabilidad la belleza y la verdad",
			"Triste epoca la nuestra Es mas facil desintegrar un atomo que un prejuicio",
			"Hay dos cosas infinitas el Universo y la estupidez humana Y del Universo no estoy seguro",
			"Todos somos muy ignorantes Lo que ocurre es que no todos ignoramos las mismas cosas",
			"Si tu intencion es describir la verdad hazlo con sencillez y la elegancia dejasela al sastre",
			"Intenta no volverte un hombre de exito sino volverte un hombre de valor",
			"Dar ejemplo no es la principal manera de influir sobre los demas es la unica manera",
			"Nunca consideres el estudio como una obligacion sino como una oportunidad para penetrar en el bello y maravilloso mundo del saber",
			"Cada dia sabemos mas y entendemos menos",
			"Vivimos en el mundo cuando amamos Solo una vida vivida para los demas merece la pena ser vivida",
			"El problema del hombre no esta en la bomba atomica sino en su corazon",
			};
	
	public Controller() {
		this.AES = new CriptografiaSimetrica();
		this.clavePublica = new CriptografiaAsimetrica();
		this.DES = new CriptografiaSimetrica();
		this.RSA = new CriptografiaAsimetrica();
		
		this.keyStoreManager = new KeyStoreManager();
	}

	public void init() {
		try {
			generateMultiencriptedDocument();
			decodeEncriptedDocument();
			System.out.println("FINISHED");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generateMultiencriptedDocument() {
		
		
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
				byte[] encripted = this.RSA.cifrarRSA(toBeEncripted[count-1].getBytes(), RSA);
				
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

			FileInputStream fis = new FileInputStream("files/output" + count + ".txt");
			byte[] data = new byte[fis.available()];
			
			fis.read(data);
			fis.close();
				
			System.out.println(new String(recursiva(data, 0)));
		}
	}
	
	public byte[] recursiva (byte[] data, int loops) throws Exception {
		byte[] dataAux;
		
		if (loops > LOOPS + 1) {
			
			return null;
			
		} else if (data != null && new String(data).replace(" ","").matches("[a-zA-Z]+")) {
			
			return data;
			
		} else {
			
			dataAux = recursiva(this.AES.descifrar(data, LAES, Constants.AES), loops+1);
			if (dataAux == null) {
				dataAux = recursiva(this.clavePublica.descifrarCP(data, LCP.getPrivate()), loops+1);
				if (dataAux == null) {
					dataAux = recursiva(this.RSA.descifrarRSA(data, LRSA), loops+1);
					if (dataAux == null) {
						dataAux = recursiva(this.DES.descifrar(data, LDES, Constants.DES), loops+1);
					}
				}
			}
		}
		
		return dataAux;
		
	}
	
}
