package app.dto;

import java.util.Date;

public class ConferenciaDTO {

	// Número único asignado a la conferencia (se asigna al aceptar el envío)
	private int idConferencia;
	// Nombre de la conferencia
	private String nombre;
	// Deadline de envío de artículos
	private String deadline;
	
	// Deadline como Date
	private Date deadlineDate;
	
	// Constructor vacío
	public ConferenciaDTO() {
	}
	
	// Constructor con todos los parametros
	public ConferenciaDTO(int idConferencia, String nombre, String deadline) {
		this.idConferencia = idConferencia;
		this.nombre = nombre;
		this.deadline = deadline;
	}

	//Getters  Setters
	
	public int getIdConferencia() {
		return idConferencia;
	}

	public void setIdConferencia(int idConferencia) {
		this.idConferencia = idConferencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date fechaDeadline) {
		this.deadlineDate = fechaDeadline;
	}
	
	// Metodo para pasar el Deadline de String a Date
	public void setFechaDeadline() {
		this.deadlineDate = stringToDate(this.deadline);
	}
	
	//Metodo para convertir el string de la fecha en un Date
	public Date stringToDate(String fecha) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(fecha);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
