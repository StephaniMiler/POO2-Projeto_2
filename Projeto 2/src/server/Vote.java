package server;

public class Vote {
    private String cpf;
    private int candidateNumber;

    public Vote(String cpf, int candidateNumber) {
        this.cpf = cpf;
        this.candidateNumber = candidateNumber;
    }

    public String getCpf() {
        return cpf;
    }

    public int getCandidateNumber() {
        return candidateNumber;
    }
}