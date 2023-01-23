package security;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class Hash {
	
    public String generarHash(String datos) throws Exception {
        // Crear un objeto MessageDigest para generar el hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Proporcionar los datos para generar el hash
        digest.update(datos.getBytes(StandardCharsets.UTF_8));

        // Generar el hash
        return new String(digest.digest());
    }
}
