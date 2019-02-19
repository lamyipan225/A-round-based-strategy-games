package PA2;

import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

import PA2.GameMap;

import java.io.IOException;
//import java.util.InputMismatchException;


public class GameEngine 
{
//	private final ArrayList<String> displayMap = new ArrayList<>();
	
	protected final ArrayList<Player> players = new ArrayList<>();
	protected final GameMap gameMap = new GameMap(players);
	private static final Scanner userInputScanner = new Scanner(System.in);
	
	/**
	 * TODO:
	 * 			
	 */
	protected void loadPlayersData(String filename) throws IOException
	{
		try (Scanner scanner = new Scanner(new File(filename)))
		{
			// Temporary Global Unit List for Duplicate Unit ID Checking.
			//ArrayList<General> globalUnitList = new ArrayList<General>();
			
			int numPlayers = scanner.nextInt();
			
			for (int n = 0; n < numPlayers; n++)
			{
				String name = scanner.next();
				int gold = scanner.nextInt();
				int numTowns = scanner.nextInt();
				int numGenerals = scanner.nextInt();
				
				
				Player player = new Player(name, gold);
				
				for (int h = 0; h < numTowns; ++h)
				{
					int townId = scanner.nextInt();
					String townName = scanner.next();
					String townType = scanner.next();
					int population = scanner.nextInt();
					int armySize = scanner.nextInt();
					int foodProduction = scanner.nextInt();
					int latitude = scanner.nextInt();
					int longitude = scanner.nextInt();
					
					
					Town town;
					switch (townType)
					{
					case "Town":
						town = new Town(townId, townName, population, armySize, foodProduction,longitude, latitude);
						break;
					case "City":
						town = new City(townId, townName, population, armySize, foodProduction,longitude, latitude);
						break;
					case "Metropolis":
						town = new Metropolis(townId, townName, population, armySize, foodProduction,longitude, latitude);
						break;
					default:
						throw new IOException();
					}
					
					player.addTown(town);
				}
				
				for (int c = 0; c < numGenerals; ++c)
				{
					String generalName = scanner.next();
					int population = scanner.nextInt();
					int armySize = scanner.nextInt();
					int foodProduction = scanner.nextInt();
					
					player.addGeneral(new General(generalName, population, armySize, foodProduction));
				}
				
				players.add(player);
			}
		}
		
		
	}
	
	
	

	
	
	protected boolean processSendTroopsCommand(Player player, Town selectedTown, Town targetTown, General selectedGeneral,int troopSize) {
		System.out.print("Enter the size of troops:");
		//int troopSize;
		
		/*try {
			troopSize = Integer.parseInt(userInputScanner.next());
			
	    } catch (NumberFormatException e) {
	    	System.out.println("ERROR: invalid input");
	    	return false;
	    	
	    }*/
		if (troopSize < 0) {
			System.out.println("ERROR: invalid input");
	    	return false;
	    	
		}
		
		if (player.getTownList().contains(targetTown)) {
			return ((City)selectedTown).transferArmy(targetTown, selectedGeneral, troopSize);
			
		} else {
			if (((City)selectedTown).attackTown(targetTown, selectedGeneral, troopSize)) {
				if (targetTown.getArmySize()<=0) {
					gameMap.getTownOwner(targetTown).surrenderTown(targetTown, player);
					
				}
				selectedGeneral.endTurn();//modify by GY
				return true;
				
			}
			return false;
			
		}
	}
	
	/**
	 * @return true if only one player has any town remaining.
	 */
	protected boolean isGameOver() 
	{
		int playersRemaining = 0;
		
		for (Player player : players) 
		{
			if (0 < player.getTownList().size())
			{
				playersRemaining ++;
			}
		}
		return (playersRemaining == 1);
	}

	
	
	
	
	
}
