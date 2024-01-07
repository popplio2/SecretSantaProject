import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SecretSanta {
	public static void main(String[] args) {
		List<String> players = new ArrayList<>();
		players.add("Dan");
		players.add("Alyssa");
		players.add("Kerri");
		players.add("Mama Simone");
		players.add("Sir Steve");
		
		SecretSanta newGame = new SecretSanta(players);
		
		newGame.draw("Dan");
		newGame.draw("Alyssa");
		newGame.draw("Kerri");
//		newGame.draw("Mama Simone");
//		newGame.draw("Sir Steve");
		
		List<String> newPlayers = new ArrayList<>();
		newPlayers.add("Nicole");
		newPlayers.add("Silas");
		
//		// insufficient players test
//		newGame.add(newPlayers);

		// sufficient players
		newPlayers.add("Youssef");
		newGame.add(newPlayers);
		
		
		newGame.draw("Nicole");
		newGame.draw("Silas");
		newGame.draw("Youssef");
		
		// draw the rest
		newGame.draw("Mama Simone");
		newGame.draw("Sir Steve");
	}
	
	protected Map<String, String> gameState = new HashMap<>();
	
	protected List<String> undrawnNames = new ArrayList<>();

	
	public SecretSanta(List<String> players) {
		for (String player : players) {
			this.gameState.put(player, "");
			this.undrawnNames.add(player);
		}
		initialArrangement();
	}
	private void initialArrangement() {
		Collections.shuffle(undrawnNames);
		assignRemainingPairings();
	}
	
	public void draw(String player) {
		undrawnNames.remove(player);
		
		System.out.println(player + " is " + gameState.get(player) + "'s Secret Santa!");
	}
	
	public void add(List<String> newPlayers) {
		if (newPlayers.size() + this.undrawnNames.size() < 3)
			System.out.println("\nInsufficient number of new players for a fair game.\n");
		
		else {
			System.out.println("\nNew players added.\n");
			
			this.undrawnNames.addAll(newPlayers);
			updateArrangement();
		}
	}
	
	private void updateArrangement() {
		// erase previous pairings from undrawn names
		for (String name : this.undrawnNames)
			this.gameState.put(name, "");
		
		// shuffle undrawn names
		Collections.shuffle(undrawnNames);
		
		// update the state 
		assignRemainingPairings();
	}
	
	private void assignRemainingPairings() { // assigning pairs based on random arrangement
		for (int i = 0; i < undrawnNames.size() - 1; i++) {
			gameState.put(undrawnNames.get(i), undrawnNames.get(i + 1)); 
		}
		
		// assign last pair (last element in list + first element in list)
		gameState.put(undrawnNames.get(undrawnNames.size() - 1), undrawnNames.get(0));
	}
	
	public String toString() {
		String str = "";
		for (String player : this.gameState.keySet()) {
			str += player + ": " + gameState.get(player) + "\n";
		}
		str += "\n";
		return str;
	}
}