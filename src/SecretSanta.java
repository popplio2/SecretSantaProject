import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecretSanta<E> {
	public static void main(String[] args) {
		List<String> players = new ArrayList<>();
		players.add("Dan");
		players.add("Alyssa");
		players.add("Kerri");
		players.add("Mama Simone");
		players.add("Sir Steve");
		
		SecretSanta<String> game1 = new SecretSanta<>(players);
		game1.draw("Dan");
		game1.draw("Alyssa");
		game1.draw("Kerri");
		
		Set<String> newPlayers = new HashSet<>();
		newPlayers.add("Youssef");
		newPlayers.add("Silas");
		newPlayers.add("Nicole");
		
		game1.add(newPlayers);

		game1.draw("Mama Simone");
		game1.draw("Sir Steve");
		game1.draw("Youssef");
		
		
		newPlayers.add("Abby");
		newPlayers.add("Charlotte");
		
		game1.add(newPlayers);
		
		game1.draw("Silas");
		game1.draw("Nicole");
		game1.draw("Abby");
		game1.draw("Charlotte");
		
		newPlayers.add("Keagan");
		game1.add(newPlayers);
		
		game1.draw("Keagan");
		
		List<Integer> nums = new ArrayList<>();
		nums.add(1);
		nums.add(2);
		nums.add(3);
		nums.add(4);
		nums.add(5);
		
		SecretSanta<Integer> game2 = new SecretSanta<>(nums);
		game2.draw(1);
		game2.draw(2);
		game2.draw(3);
		game2.draw(4);
		game2.draw(5);
		
	}
	
	// stores preliminary pairings pre-draw
	private Map<E, E> gameState = new HashMap<>();
	
	// store information gained by drawing
	private Set<E> santees = new HashSet<>();
	private Set<E> santas = new HashSet<>();
	
	// universal set used for complementation
	private Set<E> allPlayers = new HashSet<>();
	
	public SecretSanta(List<E> players) {
		for (E player : players) {
			this.gameState.put(player, null);
			this.allPlayers.add(player);
		}
		initialArrangement();
	}
	public SecretSanta(Set<E> players) {
		for (E player : players) {
			this.gameState.put(player, null);
			this.allPlayers.add(player);
		}
		initialArrangement();
	}
	
	private void initialArrangement() {
		List<E> players = new ArrayList<>(this.allPlayers);
		Collections.shuffle(players);
		assignOrder(players);
	}
	
	public void draw(E player) {
		if (!this.allPlayers.contains(player)) { // O(1) for hashSet
			System.out.println("\nCannot draw player who is not in the game.\n");
		}
		else {
			this.santas.add(player);
			this.santees.add(this.gameState.get(player));
			System.out.println(player + " is " + this.gameState.get(player) + "'s Secret Santa!");
		}
	}
	
	public void add(Set<E> newPlayers) {
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
		Set<E> notSantasButSantees = new HashSet<>(this.allPlayers); // !santas x santees (only incoming arrow)
		notSantasButSantees.removeAll(this.santas);
		notSantasButSantees.retainAll(this.santees);
		
		Set<E> notSantasAndNotSantees = new HashSet<>(this.allPlayers); // !santas x !santees (no arrows)
		notSantasAndNotSantees.removeAll(this.santas);
		notSantasAndNotSantees.removeAll(this.santees);
		
		Set<E> santasButNotSantees = new HashSet<>(this.allPlayers); // santas x !santees (only outgoing arrow)
		santasButNotSantees.retainAll(this.santas);
		santasButNotSantees.removeAll(this.santees);
		
		Map<E, E> stateChanges = new HashMap<>();
		
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
	
	private void assignOrder(List<E> players) { // assigning pairs based on random arrangement
		if (players.size() > 2) {
			for (int i = 0; i < players.size() - 1; i++) {
				gameState.put(players.get(i), players.get(i + 1)); 
			}
			
			// assign last pair (last element in list + first element in list)
			gameState.put(players.get(players.size() - 1), players.get(0));
		}
	}
	
	private Map<E, E> assignPairings(List<E> santaList, List<E> santeeList) {
		Map<E, E> stateChanges = new HashMap<>(); // tracks progress in creating next preliminary arrangement
		
		int randomSantaIndex;
		int randomSanteeIndex;
		E currentSanta;
		E currentSantee;
		
		E lastEl;
		
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
		for (E player : this.gameState.keySet()) {
			str += player + ": " + this.gameState.get(player) + "\n";
		}
		str += "\n";
		return str;
	}
}