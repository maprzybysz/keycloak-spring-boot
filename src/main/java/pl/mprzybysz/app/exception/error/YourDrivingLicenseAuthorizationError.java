package pl.mprzybysz.app.exception.error;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class YourDrivingLicenseAuthorizationError extends AbstractThrowableProblem implements Serializable {

	private static final long serialVersionUID = 1L;

	public YourDrivingLicenseAuthorizationError(String URI, String message) throws URISyntaxException {
		super(new URI(URI), "error", Status.UNAUTHORIZED, message);
	}

}
