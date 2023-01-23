package controller;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class EjemploECDSA {
    public static KeyPair generarClaves() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        // Crear un objeto KeyPairGenerator para generar claves
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");

        // Especificar los parámetros de la curva elíptica
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
        keyGen.initialize(ecSpec, new SecureRandom());

        // Generar un par de claves
        return keyGen.generateKeyPair();
    }

    public static byte[] generarFirma(byte[] datos, PrivateKey clavePrivada) throws Exception {
        // Crear un objeto Signature para generar la firma
        Signature firma = Signature.getInstance("SHA256withECDSA", "BC");

        // Inicializar el objeto Signature con la clave privada
        firma.initSign(clavePrivada);

        // Proporcionar los datos a firmar
        firma.update(datos);

        // Generar la firma
        return firma.sign();
    }

    public static boolean verificarFirma(byte[] datos, byte[] firma, PublicKey clavePublica) throws Exception {
        // Crear un objeto Signature para verificar la firma
        Signature verificador = Signature.getInstance("SHA256withECDSA", "BC");

        // Inicializar el objeto Signature con la clave pública
        verificador.initVerify(clavePublica);

        // Proporcionar los datos y la firma a verificar
        verificador.update(datos);

        // Verificar la firma
        return verificador.verify(firma);
    }

    public static void main(String[] args) {
        try {
            // Generar un par de claves
            KeyPair claves = generarClaves();

            // Generar una firma
            byte[] datos = "Este es un texto secreto".getBytes();
            byte[] firma = generarFirma(datos, claves.getPrivate());

            // Verificar la firma
            boolean firmaValida = verificarFirma(datos, firma, claves.getPublic());

            System.out.println("Firma válida: " + firmaValida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
