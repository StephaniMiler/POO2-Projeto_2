package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import comms.Candidate;

public class ServerController {
    private static final ArrayList<Candidate> candidates = new ArrayList<Candidate>(); // Candidato -> Nome
    private static final Map<String, String> voters = new HashMap<>(); 	// CPF -> Senha
    private static final Map<String, String> userNameVoters = new HashMap<>(); // CPF -> Nome
    private static final Map<String, String> votes = new HashMap<>();   // CPF -> Candidate Number
    private static boolean votingOpen = false;

    public synchronized boolean login(String cpf, String senha) {
        return voters.containsKey(cpf) && voters.get(cpf).equals(senha);
    }

    public synchronized boolean registerVoter(String cpf, String senha, String name) {
        if (!voters.containsKey(cpf) && !userNameVoters.containsKey(cpf)) {
            voters.put(cpf, senha);
            userNameVoters.put(cpf, name);
            return true;
        }
        return false;
    }

    public synchronized boolean vote(String cpf, int candidateNumber) {
        if (!votingOpen || votes.containsKey(cpf)) {
            return false;
        }
        votes.put(cpf, String.valueOf(candidateNumber));
        return true;
    }

    public synchronized boolean startVoting() {
    	if(votingOpen) {
    		return false;
    	} else {
    		votingOpen = true;
    		return true;
    	}
    }

    public synchronized boolean stopVoting() {
    	if(!votingOpen) {
    		return false;
    	}
    	else {
    		votingOpen = false;
    		return true;
    	}
    }

    public synchronized String getResults() {
        Map<Integer, Integer> voteCount = new HashMap<>();
        for (String vote : votes.values()) {
            int candidateNumber = Integer.parseInt(vote);
            voteCount.put(candidateNumber, voteCount.getOrDefault(candidateNumber, 0) + 1);
        }
        
        ArrayList<String> nameVotes = new ArrayList<String>();
        for(Map.Entry<String, String> entry : userNameVoters.entrySet()) {
        	String cpf = entry.getKey();
        	String name = entry.getValue();
        	
        	if(votes.containsKey(cpf)) {
        		String vote = votes.get(cpf);
        		
        		nameVotes.add(name + ": " + vote);
        	}
        }
        
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : voteCount.entrySet()) {
            result.append("Candidate ").append(entry.getKey()).append(" - Votes: ").append(entry.getValue()).append("\n");
        }
        
        for(String vote : nameVotes) {
        	result.append(vote).append("\n");
        }
        
        result.append("\nIf the candidate does not appear, he may have had 0 vote or was not registered!");

        return result.toString();
    }

    public synchronized boolean addCandidate(Candidate candidate) {
    	if(!votingOpen) {
            candidates.add(candidate);
            return true;
    	}
    	else {
    		return false;
    	}
    }

    public synchronized Candidate getCandidate(int number) {
    	for(Candidate candidate : candidates) {
    		if(candidate.getNumber() == number) {
    			return candidate;
    		}
    	}
		return null;
    }
    
    public synchronized boolean getVotingOpen() {
    	return votingOpen;
    }
}