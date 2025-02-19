package app.enums;

public enum Rol {
	REVISOR("Revisor"), AUTOR("Autor"), COORDINADOR("Coordinador");

	private final String nombre;

	private Rol(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
