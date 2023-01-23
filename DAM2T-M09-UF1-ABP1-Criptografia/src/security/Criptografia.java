package security;

public interface Criptografia {
	String descifrar(String mensaje);
	String getClaves();
	void init(String clave);
}
