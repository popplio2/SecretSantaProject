import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecretSanta {
	public static void main(String[] args) {
		Set<String> players = new HashSet<>();
		players.add("Dan");
		players.add("Alyssa");
		players.add("Kerri");
		players.add("Mama Simone");
		players.add("Sir Steve");
		
		SecretSanta newGame = new SecretSanta(players);
		newGame.draw("Dan");
		newGame.draw("Alyssa");
		newGame.draw("Kerri");
		
		Set<String> newPlayers = new HashSet<>();
		newPlayers.add("Youssef");
		newPlayers.add("Silas");
		newPlayers.add("Nicole");
		
		newGame.add(newPlayers);

		newGame.draw("Mama Simone");
		newGame.draw("Sir Steve");
		newGame.draw("Youssef");
		
		
		newPlayers.add("Abby");
		newPlayers.add("Charlotte");
		
		newGame.add(newPlayers);
		
		newGame.draw("Silas");
		newGame.draw("Nicole");
		newGame.draw("Abby");
		newGame.draw("Charlotte");
		
		newPlayers.add("Keagan");
		newGame.add(newPlayers);
		
		newGame.draw("Keagan");
	}
	
	// stores preliminary pairings pre-draw
	private Map<String, String> gameState = new HashMap<>();
	
	// store information gained by drawing
	private Set<String> santees = new HashSet<>();
	private Set<String> santas = new HashSet<>();
	
	// universal set used for complementation
	private Set<String> allPlayers = new HashSet<>();

	public SecretSanta(Set<String> players) {
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
		if (!this.allPlayers.contains(player)) { // O(1) for hashSet
			System.out.println("\nCannot draw player who is not in the game.\n");
		}
		else {
			this.santas.add(player);
			this.santees.add(this.gameState.get(player));
			System.out.println(player + " is " + this.gameState.get(player) + "'s Secret Santa!");
		}
	}
	
	public void add(Set<String> newPlayers) {
		newPlayers.removeAll(this.allPlayers); // ensures that there are no duplicates between original and new players
		
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
		
		Map<String, String> stateChanges = new HashMap<>();
		
		// while !santas x !santees is not empty, !santas x santees -> !santas x !santees
		while (!notSantasAndNotSantees.isEmpty()) {
			
			stateChanges.putAll(this.assignPairings(new ArrayList<>(notSantasButSantees), new ArrayList<>(notSantasAndNotSantees)));
			
			// update sets with new pairings
			notSantasButSantees.clear();
			notSantasButSantees.addAll(stateChanges.values());
			
			notSantasAndNotSantees.removeAll(stateChanges.values());
			
			this.gameState.putAll(stateChanges);
			stateChanges.clear();
		}
		
		// then assign !santas x santees -> santas x !santees
		stateChanges.putAll(this.assignPairings(new ArrayList<>(notSantasButSantees), new ArrayList<>(santasButNotSantees)));
		
		this.gameState.putAll(stateChanges); // add all new pairings to gameState
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
	
	private Map<String, String> assignPairings(List<String> santaList, List<String> santeeList) {
		Map<String, String> stateChanges = new HashMap<>(); // tracks progress in creating next preliminary arrangement
		
		int randomSantaIndex;
		int randomSanteeIndex;
		String currentSanta;
		String currentSantee;
		
		String lastEl;
		
		// assign a random santa to a random santee until we run out of santas or santees
		while (santaList.size() > 0 && santeeList.size() > 0) {
			randomSantaIndex = (int) (Math.random() * santaList.size());
			randomSanteeIndex = (int) (Math.random() * santeeList.size());
			
			// get random santa and santee
			currentSanta = santaList.get(randomSantaIndex);
			currentSantee = santeeList.get(randomSanteeIndex);
			
			// store this pairing
			stateChanges.put(currentSanta, currentSantee); 
			
			
			// swapping currentSantee with last element so removal is O(1)
			lastEl = santeeList.get(santeeList.size() - 1);
			santeeList.set(santeeList.size() - 1, currentSantee);
			santeeList.set(randomSanteeIndex, lastEl);
			// swapping currentSanta with last element so removal is O(1)
			lastEl = santaList.get(santaList.size() - 1);
			santaList.set(santaList.size() - 1, currentSanta);
			santaList.set(randomSantaIndex, lastEl);
			
			// to ensure no two santas get the same santee and vice versa
			santaList.remove(santaList.size() - 1);
			santeeList.remove(santeeList.size() - 1);
		}
		return stateChanges;
	}
	
	@Override 
	public String toString() {
		String str = "";
		for (String player : this.gameState.keySet()) {
			str += player + ": " + this.gameState.get(player) + "\n";
		}
		str += "\n";
		return str;
	}
}