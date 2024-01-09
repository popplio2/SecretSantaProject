import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecretSanta {
	public static void main(String[] args) {
		List<String> players = new ArrayList<>();
		players.add("Dan");
		players.add("Alyssa");
		players.add("Kerri");
		
		SecretSanta newGame = new SecretSanta(players);
		
		newGame.draw("Dan");
		newGame.draw("Alyssa");
		
		List<String> newPlayers = new ArrayList<>();

		newPlayers.add("Mama Simone");
		newPlayers.add("Sir Steve");
		
		newGame.add(newPlayers);

		newGame.draw("Kerri");
		newGame.draw("Mama Simone");
		newGame.draw("Sir Steve");
	}
	
	protected Map<String, String> gameState = new HashMap<>();
	
	protected Set<String> santees = new HashSet<>();
	protected Set<String> santas = new HashSet<>();
	protected Set<String> allPlayers = new HashSet<>();
	
	public SecretSanta(List<String> players) {
		for (String player : players) {
			this.gameState.put(player, "");
			this.allPlayers.add(player);
		}
		initialArrangement();
	}
	private void initialArrangement() {
		List<String> players = new ArrayList<>(this.allPlayers);
		Collections.shuffle(players);
		assignOrder(players);
	}
	
	public void draw(String player) {
		this.santas.add(player);
		this.santees.add(this.gameState.get(player));
		System.out.println(player + " is " + this.gameState.get(player) + "'s Secret Santa!");
	}
	
	public void add(List<String> newPlayers) {
		if (newPlayers.size() + (this.allPlayers.size() - this.santas.size()) < 3)
			System.out.println("\nInsufficient number of new players for a fair game.\n");
		
		else {
			System.out.println("\nNew players added.\n");
			
			this.allPlayers.addAll(newPlayers);
			updateArrangement();
		}
	}
	
	private void updateArrangement() {
		// erase previous pairings from undrawn names
		
		for (String player : this.allPlayers) { // erasing preliminary pairings that were not already drawn
			if (!this.santas.contains(player))
				this.gameState.put(player, "");
		}
		
		
		Set<String> notSantasButSantees = new HashSet<>(this.allPlayers); // !santas x santees (only incoming arrow)
		notSantasButSantees.removeAll(this.santas);
		notSantasButSantees.retainAll(this.santees);
		
		Set<String> notSantasAndNotSantees = new HashSet<>(this.allPlayers); // !santas x !santees (no arrows)
		notSantasAndNotSantees.removeAll(this.santas);
		notSantasAndNotSantees.removeAll(this.santees);
		
		Set<String> santasButNotSantees = new HashSet<>(this.allPlayers); // santas x !santees (only outgoing arrow)
		santasButNotSantees.retainAll(this.santas);
		santasButNotSantees.removeAll(this.santees);
		
		// while !santas x !santees is not empty, !santas x santees -> !santas x !santees
		while (!notSantasAndNotSantees.isEmpty()) {
			
			this.assignPairings(new ArrayList<>(notSantasButSantees), new ArrayList<>(notSantasAndNotSantees));
			
			// need to update sets
			notSantasButSantees.addAll(this.allPlayers);
			notSantasButSantees.removeAll(this.santas);
			notSantasButSantees.retainAll(this.santees);
			
			notSantasAndNotSantees.addAll(this.allPlayers);
			notSantasAndNotSantees.removeAll(this.santas);
			notSantasAndNotSantees.removeAll(this.santees);
		}
		
		// then assign !santas x santees -> santas x !santees
		this.assignPairings(new ArrayList<>(notSantasButSantees), new ArrayList<>(santasButNotSantees));
	}
	
	private void assignOrder(List<String> players) { // assigning pairs based on random arrangement
		if (players.size() > 2) {
			for (int i = 0; i < players.size() - 1; i++) {
				gameState.put(players.get(i), players.get(i + 1)); 
			}
			
			// assign last pair (last element in list + first element in list)
			gameState.put(players.get(players.size() - 1), players.get(0));
		}
	}
	
	private void assignPairings(List<String> santaList, List<String> santeeList) {
		int randomIndex;
		String currentSanta;
		String currentSantee;
		for (int i = 0; i < santaList.size(); i++) {
			randomIndex = (int) (Math.random() * santeeList.size());
			currentSanta = santaList.get(i);
			currentSantee = santeeList.get(randomIndex);
			
			gameState.put(currentSanta, currentSantee); 
			this.santas.add(currentSanta);
			this.santees.add(currentSantee);
		}
	}
	
	public String toString() {
		String str = "";
		for (String player : this.gameState.keySet()) {
			str += player + ": " + this.gameState.get(player) + "\n";
		}
		str += "\n";
		return str;
	}
}