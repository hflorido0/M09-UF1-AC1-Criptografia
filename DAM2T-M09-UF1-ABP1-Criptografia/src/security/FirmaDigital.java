package security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class FirmaDigital {
	
	private KeyPair claves;
	
	public void init() {
		try {
	        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	        keyGen.initialize(2048);
	        claves = keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
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

    public String generarFirma(String datos) throws Exception {
        // Crear un objeto Signature para generar la firma
        Signature firma = Signature.getInstance("SHA256withRSA");

        // Inicializar el objeto Signature con la clave privada
        firma.initSign(claves.getPrivate());

        // Proporcionar los datos a firmar
        firma.update(datos.getBytes(StandardCharsets.UTF_8));

        // Generar la firma
        return new String(firma.sign());
    }

    public boolean verificarFirma(String datos, String firma) throws Exception {
        // Crear un objeto Signature para verificar la firma
        Signature verificador = Signature.getInstance("SHA256withRSA");

        // Inicializar el objeto Signature con la clave pública
        verificador.initVerify(claves.getPublic());

        // Proporcionar los datos y la firma a verificar
        verificador.update(datos.getBytes(StandardCharsets.UTF_8));

        // Verificar la firma
        return verificador.verify(firma.getBytes(StandardCharsets.UTF_8));
    }
}
