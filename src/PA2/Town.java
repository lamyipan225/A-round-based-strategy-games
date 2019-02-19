package PA2;

//import java.util.ArrayList;

public class Town {
	
	protected final int id;
	protected final String name;
	protected int population;
	protected int armySize;
	protected int cropYield;
	protected int longitude;//y
	protected int latitude;
	
	public Town(int id, String name, int population, int armySize, int cropYield, int longitude, int latitude) {
		this.id = id;
		this.name = name;
		this.population = population;
		this.armySize = armySize;
		this.cropYield = cropYield;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Town(Town town) {
		this(town.id, town.name, town.population, town.armySize, town.cropYield, town.longitude, town.latitude);
	}
	
	public int getLongitude()
	{
		return this.longitude;
	}
	public int getLatitude()
	{
		return this.latitude;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getArmySize() {
		return armySize;
	}
	
	public boolean improveCropYield(General general) {
		float baseVal = 100;
		int targetVal = (int)(baseVal * (general.getWisdomPoint() / 100f));
		
		cropYield+=targetVal;
		
		System.out.println("crop yield improved by "+targetVal);
		String result = "crop yield improved by "+targetVal;
		GameApplication.printResult.add(result);
		general.endTurn();//GY
		return true;
	}
	
	public boolean collectTax(Player player, General general) {
		float baseVal = population/5f;
		int targetVal = (int)((baseVal) * (general.getLeadershipPoint() / 100f));
		
		player.earnGold(targetVal);
		
		System.out.println("collected " + targetVal + " tax");
		String result = "collected " + targetVal + " tax";
		GameApplication.printResult.add(result);
		general.endTurn();//GY
		return true;
	}

	// increment armySize by 10th of population
	// and decrement population by number of recruited soldiers
	// also, recruit size should not exceed the current population
	// if recruit size > population, then only recruit up to the number of population. In that case, you should automatically adjust the spending.
	public boolean recruitArmy(Player player, General general, int budget) {
		if (budget < 0) {
			System.out.println("ERROR: invalid budget!");
			String result = "ERROR: invalid budget!";
			GameApplication.printResult.add(result);
			return false;
		}
		
		float baseVal = budget;
		int targetVal = (int)(baseVal * (general.getCombatPoint() / 100f));
		
		if (targetVal >= population) {
			targetVal = population;
			budget = (int)(budget * (population / (float)targetVal));
		}
		
		if (player.spendGold(budget)) {
			armySize += targetVal;
			population -= targetVal;
			
			System.out.println("recruited " + targetVal + " soldiers");
			String result = "recruited " + targetVal + " soldiers";
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
	
	public String receiveAttack(int damage) {
		int prevArmySize = armySize;
		armySize -= damage;
		if (armySize <= 0) {
			armySize = 0;
		}
		System.out.println(this + " lost " + (prevArmySize - armySize) + " troops");
		String result = this + " lost " + (prevArmySize - armySize) + " troops";
		return result;
	}
	
	// decrement foodProduction by int(armySize/2)
	// increase population by amount of excess foodProduction
	public void processTurn(double rand) {
		if (rand < 0.3) {
			int casualty = (int)(population * 0.2f);
			System.out.println(this + " was hit by natural disaster! Lost " + casualty + " population!");
			String result = this + " was hit by natural disaster! Lost " + casualty + " population!";
			GameApplication.RandomEventResult.add(result);
			population -= casualty;
			return;
		}
		
		int excessCrop = cropYield - population - armySize*2;
		
		if (excessCrop > 0) {
			population += (int)(excessCrop / 4f);
			System.out.println(this + "'s population increased by " + (int)(excessCrop / 4f));
			String result = this + "'s population increased by " + (int)(excessCrop / 4f);
			GameApplication.RandomEventResult.add(result);
			
		} else {
			System.out.println(this + "'s growth is at stagnation");
			String result = this + "'s growth is at stagnation";
			GameApplication.RandomEventResult.add(result);
			
			
		}
	}
	
	
	public String toString() {
		return String.format("<Town %-3s>", name);
	}
	
	public String tokenize(String ownerName) {
		return String.format("<%-3.3s:%-3.3s>", PrintUtils.allignMid(ownerName, 3), PrintUtils.allignMid(name, 3));
	}
	
	// display city info
	public void displayInfo(String ownerName) {
		int excessCrop = (cropYield - population - armySize*2>=0)?(cropYield - population - armySize*2):0;
		System.out.println(String.format("<%-5.5s: Town %-5.5s>  Population:%-5d  Army:%-5d Crop_yield:%-5d Excess_crop:%-5d", ownerName, PrintUtils.allignMid(name, 5), population, armySize, cropYield, excessCrop));
	}
}
