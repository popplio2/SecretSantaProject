
public class Player {
	private String playerName;
	private String preliminaryDrawnName;
	private String drawnName;
	
	public Player(String playerName) {
		this.setPlayerName(playerName);
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	public String getPreliminaryDrawnName() {
		return this.preliminaryDrawnName;
	}
	public String getDrawnName() {
		return this.drawnName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public void setPreliminaryDrawnName(String preliminaryDrawnName) {
		this.preliminaryDrawnName = preliminaryDrawnName;
	}
	public void setDrawnName(String drawnName) {
		this.drawnName = drawnName;
	}
}
