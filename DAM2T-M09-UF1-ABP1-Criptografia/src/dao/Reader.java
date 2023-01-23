package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
	private BufferedReader bf;
	private FileReader rd;
	
	public Reader(String file) {
		try {
			this.rd = new FileReader(file);
			this.bf = new BufferedReader(this.rd);
		} catch (IOException e) {
			System.out.println("ERROR buscando fichero de salida");
		}
	}
	
	public String read() {
		try {
			return this.bf.readLine();
		} catch (IOException e) {
			System.out.println("ERROR escribiendo en fichero");
			return null;
		}
	}

	public void closeFile() {
		try {
			this.bf.close();
		} catch (IOException e) {
			System.out.println("ERROR cerrando el fichero");
		}
	}

	public int readChar() {
		try {
			return this.rd.read();
		} catch (IOException e) {
			System.out.println("ERROR escribiendo en fichero");
			return -1;
		}
	}
}
