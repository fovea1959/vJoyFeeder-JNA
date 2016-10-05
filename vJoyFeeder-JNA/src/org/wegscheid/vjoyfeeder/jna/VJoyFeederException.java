package org.wegscheid.vjoyfeeder.jna;

@SuppressWarnings("serial")
public class VJoyFeederException extends RuntimeException {

	public VJoyFeederException() {
	}

	public VJoyFeederException(String message) {
		super(message);
	}

	public VJoyFeederException(Throwable cause) {
		super(cause);
	}

	public VJoyFeederException(String message, Throwable cause) {
		super(message, cause);
	}

	public VJoyFeederException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
