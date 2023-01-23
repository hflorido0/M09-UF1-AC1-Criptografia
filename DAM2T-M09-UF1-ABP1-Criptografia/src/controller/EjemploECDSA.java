package controller;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class EjemploECDSA {
	public static byte[] firmar(byte[] mensaje, KeyPair clave) throws Exception {
		Signature firma = Signature.getInstance("ECDSA");
		firma.initSign(clave.getPrivate());
		firma.update(mensaje);
		byte[] firmaDigital = firma.sign();
		return firmaDigital;
	}

	public static boolean verificar(byte[] mensaje, byte[] firmaDigital, KeyPair clave) throws Exception {
		Signature firma = Signature.getInstance("ECDSA");
		firma.initVerify(clave.getPublic());
		firma.update(mensaje);
		return firma.verify(firmaDigital);
	}

	public static void main(String[] args) {
		try {
			// Registrar el proveedor de seguridad BC
			Security.addProvider(new BouncyCastleProvider());

			// Generar un par de claves ECDSA
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA");
			keyGen.initialize(256);
			KeyPair clave = keyGen.generateKeyPair();

			// Firmar un mensaje
			byte[] mensaje = "Este es un mensaje secreto".getBytes();
			byte[] firmaDigital = firmar(mensaje, clave);
			// Verificar la firma del mensaje
			boolean esValida = verificar(mensaje, firmaDigital, clave);
			if (esValida) {
				System.out.println("La firma es válida");
			} else {
				System.out.println("La firma no es válida");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}