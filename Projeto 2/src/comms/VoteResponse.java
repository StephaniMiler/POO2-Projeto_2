package comms;

import java.io.Serializable;

public class VoteResponse implements Serializable {
    private static final long serialVersionUID = 1L;
	private boolean successful;
    private String message;

    public VoteResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }
}

