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
		System.out.println(newGame);
	}
	
	protected Map<String, String> gameState = new HashMap<>();
	
	protected Set<String> undrawnNames = new HashSet<>();
	protected Set<String> hasNotDrawn = new HashSet<>();
	
	public SecretSanta(List<String> players) {
		for (String player : players) {
			this.gameState.put(player, "");
			this.undrawnNames.add(player);
			this.hasNotDrawn.add(player);
		}
		initialArrangement();
	}
	private void initialArrangement() {
		List<String> undrawnNamesList = new ArrayList<>(this.undrawnNames);
		Collections.shuffle(undrawnNamesList);
		assignRemainingPairings(undrawnNamesList); // for the initial arrangement, undrawnNames = hasNotDrawn so it does not matter which list we arrange
	}
	
	public void draw(String player) {
		this.undrawnNames.remove(this.gameState.get(player)); // if player was already drawn, then the list does not change
		this.hasNotDrawn.remove(player);
		System.out.println(player + " is " + this.gameState.get(player) + "'s Secret Santa!");
	}
	
	public void add(List<String> newPlayers) {
		if (newPlayers.size() + this.hasNotDrawn.size() < 3)
			System.out.println("\nInsufficient number of new players for a fair game.\n");
		
		else {
			System.out.println("\nNew players added.\n");
			
			this.undrawnNames.addAll(newPlayers);
			this.hasNotDrawn.addAll(newPlayers);
			updateArrangement();
		}
	}
	
	private void updateArrangement() {
		// erase previous pairings from undrawn names
//		for (String name : this.hasNotDrawn)
//			this.gameState.put(name, "");
//		
//		// first, (hasNotDrawn && !undrawnNames) gets to choose from undrawnNames
//		List<String> hasNotDrawnAndNotUndrawn = new ArrayList<>(this.hasNotDrawn);
//		for (String player : this.hasNotDrawn) { // O(n^2) - consider changing lists to hashsets for more efficient set operations
//			for (String player2 : this.undrawnNames) {
//				if (player.equals(player2))
//					hasNotDrawnAndNotUndrawn.remove(player);
//			}
//		}
//		for (String player : hasNotDrawnAndNotUndrawn) {
//			String randomUndrawn = this.undrawnNames.get((int) (Math.random() * this.undrawnNames.size()));
//			this.gameState.put(player, randomUndrawn);
//			this.hasNotDrawn.remove(player);
//			this.undrawnNames.remove(randomUndrawn);
//		}
//		
//		// then, (hasNotDrawn && undrawnNames) choose from among each other
//		List<String> hasNotDrawnAndUndrawn = new ArrayList<>();
//		for (String player : this.hasNotDrawn) { // O(n^2) - consider changing lists to hashsets for more efficient set operations
//			for (String player2 : this.undrawnNames) {
//				if (player.equals(player2))
//					hasNotDrawnAndUndrawn.add(player);
//			}
//		}
//		Collections.shuffle(hasNotDrawnAndUndrawn);
//		assignRemainingPairings(hasNotDrawnAndUndrawn);
		
	}
	
	private void assignRemainingPairings(List<String> players) { // assigning pairs based on random arrangement
		if (players.size() > 2) {
			for (int i = 0; i < players.size() - 1; i++) {
				gameState.put(players.get(i), players.get(i + 1)); 
			}
			
			// assign last pair (last element in list + first element in list)
			gameState.put(players.get(players.size() - 1), players.get(0));
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