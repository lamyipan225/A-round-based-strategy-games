package PA2;

public class General {
	private String name;
	private int combatPoint;
	private int leadershipPoint;
	private int wisdomPoint;
	
	private boolean isReady;
	
	public General(String name, int combatPoint, int leadershipPoint, int wisdomPoint) {
		this.name = name;
		this.combatPoint = combatPoint;
		this.leadershipPoint = leadershipPoint;
		this.wisdomPoint = wisdomPoint;
		isReady = true;
	}
	
	public int getLeadershipPoint() {
		return leadershipPoint;
	}
	
	public int getCombatPoint() {
		return combatPoint;
	}
	
	public int getWisdomPoint() {
		return wisdomPoint;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void beginTurn()
	{
		isReady = true;
	}
	
	public void endTurn() {
		isReady = false;
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	public String toString() {
		return String.format("{General %-2s}", name);
	}
	
	public void displayInfo() {
		System.out.println(String.format("{General %-5.5s}  COMBAT:%-3d  LEADERSHIP:%-3d WISDOM:%-3d %-1s", PrintUtils.allignMid(name, 5), combatPoint, leadershipPoint, wisdomPoint, (isReady?"READY":"DONE")));
	}
}
