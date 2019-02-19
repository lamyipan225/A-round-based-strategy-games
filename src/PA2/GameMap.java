package PA2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import PA2.Player;
import PA2.Town;


public class GameMap {
	
	private final ArrayList<Player> players;

	private int townsCount;
	private int[][] adjacencyMatrix;
	private ArrayList<String> displayMap;
	
	public GameMap(ArrayList<Player> players) {
		this.players = players;
		displayMap = new ArrayList<String>();
	}
	
	/**
	 * TODO:
	 */
	//   0 1
	//   1 0
	// we consider the same city is not connected to itself 
	public void loadGameMap(String filename) throws IOException {
		try (Scanner scanner = new Scanner(new File(filename)))
		{
			this.townsCount = scanner.nextInt();
			adjacencyMatrix = new int[this.townsCount][this.townsCount];
			
			
			for (int i=0; i<this.townsCount; ++i) {
				for (int j=0; j<this.townsCount; ++j) {
					adjacencyMatrix[i][j] = scanner.nextInt();
					
				}
			}
			displayMap.clear();
			while(scanner.hasNextLine()) {
				displayMap.add(scanner.nextLine());
				
			}
		}
	}
	
	
	public Town getTownById(int id) {
		for (Player p: players) {
			for (Town t: p.getTownList()) {
				if (t.getId() == id) {
					return t;
					
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @param town
	 * @return returns the player who currently owns the town.
	 */
	public Player getTownOwner(Town town) {
		for (Player p: players) {
			if (p.getTownList().contains(town)) {
				return p;
				
			}
		}
		
		return null;
	}
	
	//TODO
	public ArrayList<Town> getAdjacentTownList(Town town) {
		int townId = town.getId();
		ArrayList<Town> adjTowns = new ArrayList<Town>();
		for (int i=0; i<townsCount; ++i) {
			if (adjacencyMatrix[townId][i]==1) {
				adjTowns.add(getTownById(i));
				
			}
		}
		
		return adjTowns;
	}
	
	public ArrayList<Town> getAllTownList() {
		ArrayList<Town> towns = new ArrayList<Town>();
		for (Player p: players) {
			for (Town t: p.getTownList()) {
				towns.add(t);
				
			}
		}
		
		return towns;
	}
	
	//display the map read into the ArrrayList[] object "displayMap"
	//used by the other code
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		for (String line: displayMap) {
			StringBuffer processedLine = new StringBuffer(line);
			char[] chrSeq = line.toCharArray();
			
			for (int i = 0; i<chrSeq.length; ++i) {
				if (Character.isDigit(chrSeq[i])) {
					int id=Integer.parseInt(extractId("", i, chrSeq));//to get ID of the town/city/metropolis from MapData.txt
					processedLine.replace(i-4, i+5, getTownById(id).tokenize(getTownOwner(getTownById(id)).getName()));
					
					if (id!=0) {
						i += (int)(Math.log10(id));
					}
				}
			}
			
			s.append(processedLine);
			s.append("\n");
		}
		return s.toString();
	}
	
	// Number should not start with 0!
	private String extractId(String id, int idx, char[] chrSeq) {
		if (idx >= chrSeq.length || !Character.isDigit(chrSeq[idx])) {
			return id;
		}
		
		return chrSeq[idx] + extractId(id, idx+1, chrSeq);
	}
}
