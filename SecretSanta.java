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
		System.out.println(newGame);
		
		newGame.draw("Dan");
	}
	
	protected Map<String, String> gameState = new HashMap<>();
//	protected Map<String, String> drawState = new HashMap<>();
	
	protected List<String> hasNotDrawnName = new ArrayList<>();
	protected List<String> hasDrawnName = new ArrayList<>();
	protected List<String> undrawnNames = new ArrayList<>();
	protected List<String> drawnNames = new ArrayList<>();
//	protected List<String> gameState = new LinkedList<>();

	
	public SecretSanta(List<String> players) {
		for (String player : players) {
			this.gameState.put(player, "");
			this.hasNotDrawnName.add(player);
			this.undrawnNames.add(player);
		}
		this.arrangeAll();
	}
	private void arrangeAll() {
		Collections.shuffle(undrawnNames);
		
		// assigning pairs based on random arrangement
		for (int i = 0; i < undrawnNames.size() - 1; i++) {
			gameState.put(undrawnNames.get(i), undrawnNames.get(i + 1)); 
		}
		
		// assign last pair
		gameState.put(undrawnNames.get(undrawnNames.size() - 1), undrawnNames.get(0));
	}
	
	public void draw(String player) {
		undrawnNames.remove(player);
		drawnNames.add(player);
		
		System.out.println(player + " is " + gameState.get(player) + "'s Secret Santa!");
	}
	
	public void add(List<String> newPlayers) {
		if (newPlayers.size() + hasNotDrawnName.size() < 3)
			System.out.println("Insufficient number of new players for a fair game.");
		
		else {
			hasNotDrawnName.addAll(newPlayers);
		}
	}
	
	public String toString() {
		String str = "";
		for (String player : this.gameState.keySet()) {
			str += player + ": " + gameState.get(player) + "\n";
		}
		return str;
	}
}