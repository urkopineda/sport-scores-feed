package model;

public class Equipo {
	int equipoID = 0;
	int ligaID = 0;
	String nombre = null;
	String wikiURL = null;
	
	public Equipo(int equipoID, int ligaID,	String nombre, String wikiURL) {
		this.equipoID = equipoID;
		this.ligaID = ligaID;
		this.nombre = nombre;
		this.wikiURL = wikiURL;
	}

	public int getEquipoID() {
		return equipoID;
	}

	public int getLigaID() {
		return ligaID;
	}

	public String getNombre() {
		return nombre;
	}

	public String getWikiURL() {
		return wikiURL;
	}
}
