package dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writter {
	private BufferedWriter bf;
	
	
	public Writter(String file) {
		try {
			FileWriter fw = new FileWriter(file);
			this.bf = new BufferedWriter(fw);
		} catch (IOException e) {
			System.out.println("ERROR buscando fichero de salida");
		}
	}
	
	public void write (String linea) {
		try {
			this.bf.append(linea+"\n");
		} catch (IOException e) {
			System.out.println("ERROR escribiendo en fichero");
		}
	}

	public void closeFile() {
		try {
			this.bf.close();
		} catch (IOException e) {
			System.out.println("ERROR cerrando el fichero");
		}
	}
}
