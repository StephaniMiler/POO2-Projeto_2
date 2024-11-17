package comms;

import java.io.Serializable;

public class VoteRequest implements Serializable {
    private static final long serialVersionUID = 1L;
	private int vote;
	private String cpf;

    public VoteRequest(int vote, String cpf) {
        this.vote = vote;
        this.cpf = cpf;
    }

    public int getVote() {
        return vote;
    }
    
    public String getCpf() {
    	return cpf;
    }
}
