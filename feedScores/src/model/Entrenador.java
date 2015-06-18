package model;

public class Entrenador {
	int entrenadorID = 0;
	int equipoID = 0;
	String nombre = null;
	String primerApellido = null;
	String segundoApellido = null;
	String fechaEntrada = null;
	String fechaSalida = null;
	
	public Entrenador(int entrenadorID, int equipoID, String nombre, String primerApellido,
			String segundoApellido, String fechaEntrada, String fechaSalida) {
		this.entrenadorID = entrenadorID;
		this.equipoID = equipoID;
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
	}

	public int getEntrenadorID() {
		return entrenadorID;
	}

	public int getEquipoID() {
		return equipoID;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public String getFechaEntrada() {
		return fechaEntrada;
	}

	public String getFechaSalida() {
		return fechaSalida;
	}
}
