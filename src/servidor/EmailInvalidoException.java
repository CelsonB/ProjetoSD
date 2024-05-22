package servidor;

public class EmailInvalidoException extends Exception{
	/**
	 * 
	 */
	String err; 
	public EmailInvalidoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public EmailInvalidoException(String message, String err) {
		super(message);
		this.err = err; 
	}
	
}