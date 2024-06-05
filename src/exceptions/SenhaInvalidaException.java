package exceptions;

public class SenhaInvalidaException extends Exception{
	public String err;
	public SenhaInvalidaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public SenhaInvalidaException(String message, String err) {
		super(message);
		this.err = err;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}
	
	
}
