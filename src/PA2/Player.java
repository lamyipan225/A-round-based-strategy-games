package PA2;

import java.util.ArrayList;
import java.util.Random;

public class Player {
	private final String name;
	private ArrayList<General> generals;
	private ArrayList<Town> towns;
	//private ArrayList<String> messages;
	
	private int gold;
	
	public Player(String name, int gold)
	{
		this.name = name;
		generals = new ArrayList<General>();
		towns = new ArrayList<Town>();
		this.gold = gold;
	}
	
	public String getName() 
	{
		return name; 
	}
	
	public ArrayList<General> getGeneralList()
	{
		return generals;
	}
	
	public void addGeneral(General c)
	{
		generals.add(c);
	}
	
	public ArrayList<Town> getTownList()
	{
		return towns;
	}
	
	public void addTown(Town t)
	{
		towns.add(t);
	}
	
	public boolean hasReadyGenerals() {
		for (General c:generals) {
			if (c.isReady()) {
				return true;
			}
		}
		return false;
	}
	
	public void readyAllGenerals() {
		for (General c:generals) {
			c.beginTurn();
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Player)
		{
			return (name.equals(((Player)(obj)).getName()));
		}
		else
		{
			return false;
		}
	}
	
	public int getGold() {
		return gold;
	}
	
	public void earnGold(int amount) {
		gold += amount;
	}
	
	public boolean spendGold(int amount) {
		if (gold>=amount) {
			gold-=amount;
			return true;
			
		}
		
		return false;
	}
	
	//TODO
	public boolean upgradeTown(Town town, General general) {
		//modified by Alex
		Town newTown = null;

		if(town instanceof Metropolis) {
			System.out.println("ERROR: already at max tier");
			String result = "ERROR: already at max tier";
			GameApplication.printResult.add(result);
			return false;
		}
		if (town instanceof City) { //City more specific 
			newTown = new Metropolis(town);
			
		} else if (town instanceof Town) {//Town less specific
			newTown = new City(town);
			
		}
		//modified by Alex

		if (spendGold(50)) {
			towns.remove(town);
			addTown(newTown);
			
			
			System.out.println(town+" is upgraded to "+newTown+"!");
			String result = town+" is upgraded to "+newTown+"!";
			GameApplication.printResult.add(result);
			general.endTurn();//GY
			return true;
			
		} else {
			System.out.println("ERROR: not enough gold");
			String result = "ERROR: not enough gold";
			GameApplication.printResult.add(result);
			return false;
			
		}
	}
	
	//TODO
	public void surrenderTown(Town town, Player opponent) {
		towns.remove(town);
		opponent.addTown(town);
		System.out.println(this+" surrendered "+town+" to "+opponent+"!");
		String result = this+" surrendered "+town+" to "+opponent+"!";
		GameApplication.printResult.add(result);
	}
	
	public String toString() {
		return "Player " + getName();
	}
	
	public void displayInfo() {
		int readyGenerals = 0;
		for (General g: generals) {
			if (g.isReady()) {
				++readyGenerals;
			}
		}
		
		System.out.println("__________________________");
		System.out.println(String.format("%-25.25s |",this));
		System.out.println("_________________________________________________________________");
		System.out.println(String.format(" Gold: %-8d | %-1d/%-1d ready Generals | %-1d towns", gold, readyGenerals, generals.size(), towns.size()));
		System.out.println("=================================================================");
		System.out.print("  General List  | ");
		for (General g: generals) {
			System.out.print(g + "  ");
		}
		System.out.println("\n-----------------------------------------------------------------");
		System.out.print("     Town List  | ");
		for (Town t: towns) {
			System.out.print(t + "  ");
		}
		System.out.println("\n-----------------------------------------------------------------");
	}

	

	
}
