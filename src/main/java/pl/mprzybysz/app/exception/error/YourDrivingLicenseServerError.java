package pl.mprzybysz.app.exception.error;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class YourDrivingLicenseServerError extends AbstractThrowableProblem implements Serializable {

	private static final long serialVersionUID = 1L;

	public YourDrivingLicenseServerError(String URI, String message) throws URISyntaxException {
		super(new URI(URI), "error", Status.INTERNAL_SERVER_ERROR, message);
	}

}
