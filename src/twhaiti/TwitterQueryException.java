package twhaiti;

public class TwitterQueryException extends Exception {

	private static final long serialVersionUID = 1L;

	public TwitterQueryException(String responseCode) {
		super(responseCode);
	}

}
