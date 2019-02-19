package PA2;

public class City extends Town {
	
	public City(int id, String name, int population, int armySize, int cropYield,int longitude, int latitude) {
		super(id, name, population, armySize, cropYield, longitude,latitude);
	}
	
	public City(Town town) {
		this(town.id, town.name, town.population, town.armySize, town.cropYield, town.longitude, town.latitude);
	}
	
	// Only City and Metropolis can attack another town
	// All units used for attack gets expended
	public boolean transferArmy(Town targetTown, General general, int expeditionSize) {
		if (expeditionSize > armySize) {
			System.out.println("ERROR: not enough units");
			String result = "ERROR: not enough units";
			GameApplication.printResult.add(result);
			return false;
		}

		this.armySize -= expeditionSize;
		targetTown.armySize += expeditionSize;
		

		System.out.println(this + " transferred " + expeditionSize + " troops to " + targetTown);
		String result = this + " transferred " + expeditionSize + " troops to " + targetTown;
		GameApplication.printResult.add(result);
		general.endTurn();//GY
		return true;
	}
	
	public boolean attackTown(Town targetTown, General general, int expeditionSize) {
		
		
		if (expeditionSize > armySize) {
			System.out.println("ERROR: not enough units!");
			String result = "ERROR: not enough units";
			GameApplication.printResult.add(result);
			return false;
		}
		
		int baseVal = expeditionSize;
		int targetVal = (int)(expeditionSize * (general.getCombatPoint()/100f));

		System.out.println(this + " attacked " + targetTown + " with " + baseVal + " troops");
		String result = this + " attacked " + targetTown + " with " + baseVal + " troops\n";
		
		this.armySize -= baseVal;
		String tmpResult = targetTown.receiveAttack(targetVal);
		result += tmpResult;
		GameApplication.printResult.add(result);
		
		
		return true;
	}
	
	public String toString() {
		return String.format("(City %-3s)", name);
	}
	
	public String tokenize(String ownerName) {
		return String.format("(%-3.3s:%-3.3s)", PrintUtils.allignMid(ownerName, 3), PrintUtils.allignMid(name, 3));
	}
	
	// display city info
	public void displayInfo(String ownerName) {
		int excessCrop = (cropYield - population - armySize*2>=0)?(cropYield - population - armySize*2):0;
		System.out.println(String.format("(%-5.5s: City %-5.5s)  Population:%-5d  Army:%-5d Crop_yield:%-5d Excess_crop:%-5d", ownerName, PrintUtils.allignMid(name, 5), population, armySize, cropYield, excessCrop));
	}
	
}
