package hva.exceptions;

import java.io.Serial;

public class IncorrectEntryException extends Exception {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	/** Incorrect entry. */
	private final String[] _entrySpecificationParams;

	public IncorrectEntryException(String[] entrySpecificationParams) {
		_entrySpecificationParams = entrySpecificationParams;
	}

	public IncorrectEntryException(String[] entrySpecificationParams, Exception cause) {
		super(cause);
		_entrySpecificationParams = entrySpecificationParams;
	}

	public String[] getEntrySpecification() {
		return _entrySpecificationParams;
	}

}
