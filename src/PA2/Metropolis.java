package PA2;

public class Metropolis extends City{
	
	int walls;
	
	public Metropolis(int id, String name, int population, int armySize, int cropYield, int longitude, int latitude) {
		super(id, name, population, armySize, cropYield, longitude, latitude);
		walls = 0;
	}
	
	public Metropolis(Town town) {
		this(town.id, town.name, town.population, town.armySize, town.cropYield,town.longitude, town.latitude);
	}
	
	// decrement foodProduction by int(armySize/2)
	// increase population by amount of excess foodProduction
	// Metropolis doesn't get affected by disaster
	// 50 points of wall get regenerated each turn
	@Override
	public void processTurn(double rand) {
		int excessCrop = cropYield - population - armySize*2;
		String result;
		if (excessCrop > 0) {
			population += (int)(excessCrop / 4f);
			System.out.println(this + "'s population increased by " + (int)(excessCrop / 4f));
			result = this + "'s population increased by " + (int)(excessCrop / 4f)+"\n";
			
			
		} else {
			System.out.println(this + "'s growth is at stagnation");
			result = this + "'s growth is at stagnation\n";
			
		}
		
		System.out.println(this + "regenerated wall by 10 hit points");
		result += this + "regenerated wall by 10 hit points";
		GameApplication.RandomEventResult.add(result);
		
		walls += 10;
	}
	
	// wall point gets deducted first, then armySize
	@Override
	public String receiveAttack(int damage) {
		int prevArmySize = armySize;
		walls -= damage;
		
		if (walls <= 0) {
			armySize += walls;	// attack penetrates wall and harms army
			walls = 0;
		}
		
		if (armySize <= 0) {
			armySize = 0;
		}
		
		System.out.println(this + " lost " + (prevArmySize - armySize) + " troops");
		String result = this + " lost " + (prevArmySize - armySize) + " troops";
		return result;
	}
	
	public String toString() {
		return String.format("[Metr %-3s]", name);
	}
	
	public String tokenize(String ownerName) {
		return String.format("[%-3.3s:%-3.3s]", PrintUtils.allignMid(ownerName, 3), PrintUtils.allignMid(name, 3));
	}
	
	// display city info
	public void displayInfo(String ownerName) {
		int excessCrop = (cropYield - population - armySize*2>=0)?(cropYield - population - armySize*2):0;
		System.out.println(String.format("[%-5.5s: Metr %-5.5s]  Population:%-5d  Army:%-5d Crop_yield:%-5d Excess_crop:%-5d", ownerName, PrintUtils.allignMid(name, 5), population, armySize, cropYield, excessCrop));
	}
}
