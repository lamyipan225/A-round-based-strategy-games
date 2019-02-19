package PA2;




import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class GameApplication extends Application {
	// Note: Please play the game in full-screen.
	// Resolution, Tiles and Offset for Rendering Unit ID on bottom-right.
	
	
	private static final int RESOLUTION_GAMEPLAY_WIDTH = 960;
	private static final int RESOLUTION_GAMEPLAY_HEIGHT = 720;
	
	
	
	// Scene and Stage
	private static final int SCENE_NUM = 2;
	private static final int SCENE_WELCOME = 0;
	private static final int SCENE_STARTGAME = 1;
	private static final String[] SCENE_TITLES = {"Welcome", "Wars of the cities"};
	private Scene[] scenes = new Scene[SCENE_NUM];
	private Stage stage;
	
	// Part 1: paneWelcome
	private Label lbMenuTitle;
	private Button btNewGame, btQuit;

	// Part 2: paneGameStart
	
	//Buttons at the botton 
	private Button btReStartGame;	
	private Button btExit;
	
	
	//Log window
	private ListView<String> listViewMessage;
	private ObservableList<String> listViewMessageItems = FXCollections.observableArrayList();
	private Label lbLog;
	
	private ListView<String> listViewRandomEvent;
	private ObservableList<String> listViewRandomEventItems = FXCollections.observableArrayList();
	private Label lbRandomEvent;
	
	//my generals
	private ListView<String> listViewGeneral;
	private ObservableList<String> listViewGeneralItems = FXCollections.observableArrayList();
	private String listViewSelectedGeneral = null;
	private Label lbMyGeneral;
	private Label lbMyGeneralTitle;
	
	//my cities
	private ListView<String> listViewCity;
	private ObservableList<String> listViewCityItems = FXCollections.observableArrayList();
	private String listViewSelectedCity= null;
	private Label lbMyCity;
	private Label lbMyCityTitle;
	
	//my neighbor
	private ListView<String> listViewNei;
	private ObservableList<String> listViewNeiItems = FXCollections.observableArrayList();
	private String listViewSelectedNei= null;
	private Label lbMyNei;
	private Label lbMyNeiTitle;
	
	//my gold
	private Label lbMyGold;
	//Command Button
	private Button btImproveCrop;
	private Button btCollectTax;
	private Button btRecruit;
	private Button btUpgrade;
	private Button btSendTroop;
	private Button exit;
	private TextField tfRecruit;
	private TextField tfSendTroop;
	
	//Map
	private Canvas canvasGameStart = new Canvas(RESOLUTION_GAMEPLAY_WIDTH, RESOLUTION_GAMEPLAY_HEIGHT);

	// Part 3: paneGameOver
	private Label lbGameOver;
	private Button btExitToMenu, btGameOverQuitGame;
	
	//Thead for player
	private Thread playerThread;
	
	private GameEngine game = new GameEngine();
	private Player humanPlayer;
	private ArrayList<Player> computerPlayers = new ArrayList<>();
	
	protected static ArrayList<String> printResult = new ArrayList<>();//To store the message that will be printed in the log window
	protected static ArrayList<String> RandomEventResult = new ArrayList<>(); //To store the message that will be printed in the random event window
	
	private boolean gameOver = false;
	
	
	private Pane paneWelcome() 
	{
		/**
		 *  To do: 
		 *  1. This function is to create the welcome scene
		 *  2. There are two buttons and a label in this scene
		 *  Hint:
		 *  1. To set the background of the pane, you can use welcomePane.getStyleClass().add("pane-welcome")
		 *  2. To set the style of the button/label, you can use 
		 *  	lbMenuTitle.getStyleClass().add("menu-title");
		 *  	btNewGame.getStyleClass().add("large-button"); 
		 *  	where lbMenuTitle is a Label and btNewGame is a button.
		 */
		BorderPane welcomePane = new BorderPane();
		VBox vBox = new VBox(20);
		lbMenuTitle = new Label("Wars of the cities");
		btNewGame = new Button("New Game");
		btQuit = new Button("Quit");
		welcomePane.getStyleClass().add("pane-welcome");
		lbMenuTitle.getStyleClass().add("welcome-title");;
		btNewGame.getStyleClass().add("large-button"); 
		btQuit.getStyleClass().add("large-button"); 
		vBox.getChildren().add(lbMenuTitle);
		vBox.getChildren().add(btNewGame);
		vBox.getChildren().add(btQuit);
		vBox.setAlignment(Pos.CENTER);
		welcomePane.setCenter(vBox);
		return welcomePane;
	}

	private Pane paneStartGame() 
	{
		/**
		 * To do: 
		 * 1. This function is to create the game start scene.
		 * 2. There are two parts in this scene:
		 * 2.1 the canvas where you can draw the map 
		 * 2.2 the control part that consists of four listview, some buttons and two TextField
		 * 3. Please think that how to put the button/listview/label in the right place
		 * Hint: To set the background of the control pane, you can use pane.getStyleClass().add("pane-game-start");
		 */
		listViewNei = new ListView<String>();
		listViewNei.setPrefSize(150, 200);
		listViewNei.setItems(listViewNeiItems);
		listViewNei.getStyleClass().add("game-control");
		lbMyNei = new Label("Neighbour Cities");
		lbMyNeiTitle = new Label("  Name  Population  Army   Crop    Wall");
		VBox paneMyNeiCity = new VBox();
		paneMyNeiCity.getChildren().addAll(lbMyNei,lbMyNeiTitle,listViewNei);
		paneMyNeiCity.setAlignment(Pos.BOTTOM_CENTER);
		
		listViewCity = new ListView<String>();
		listViewCity.setPrefSize(150, 200);
		listViewCity.setItems(listViewCityItems);
		listViewCity.getStyleClass().add("game-control");
		lbMyCity = new Label("My cities");
		lbMyCityTitle = new Label("  Name  Population  Army   Crop    Wall");
		VBox paneMyCity = new VBox();
		paneMyCity.getChildren().addAll(lbMyCity,lbMyCityTitle,listViewCity);
		paneMyCity.setAlignment(Pos.BOTTOM_CENTER);
		
		listViewGeneral = new ListView<String>();
		listViewGeneral.setPrefSize(150, 200);
		listViewGeneral.setItems(listViewGeneralItems);
		listViewGeneral.getStyleClass().add("game-control");
		lbMyGeneral = new Label("My Generals");
		lbMyGeneralTitle = new Label("Name  Combat  Leadship   Wisdom    Status");
		VBox paneMyGeneral = new VBox();
		paneMyGeneral.getChildren().addAll(lbMyGeneral,lbMyGeneralTitle,listViewGeneral);
		paneMyGeneral.setAlignment(Pos.BOTTOM_CENTER);
		
		lbMyGold = new Label("My Gold: XXX");
		VBox paneGold = new VBox();
		paneGold.getChildren().add(lbMyGold);
		paneGold.setAlignment(Pos.CENTER_RIGHT);
		
		listViewRandomEvent = new ListView<String>();
		listViewRandomEvent.setPrefSize(320, 220);
		listViewRandomEvent.setItems(listViewRandomEventItems);
		listViewRandomEvent.getStyleClass().add("game-control");
		lbRandomEvent = new Label("Random Events");
		VBox paneListViewRandomEvent = new VBox();
		paneListViewRandomEvent.getChildren().addAll(lbRandomEvent,listViewRandomEvent);
		paneListViewRandomEvent.setAlignment(Pos.BOTTOM_CENTER);
		
		listViewMessage = new ListView<String>();
		listViewMessage.setPrefSize(320, 220);
		listViewMessage.setItems(listViewMessageItems);
		listViewMessage.getStyleClass().add("game-control");
		lbLog = new Label("Commands Window");
		VBox paneListViewMessage = new VBox();
		paneListViewMessage.getChildren().addAll(lbLog,listViewMessage);
		paneListViewMessage.setAlignment(Pos.BOTTOM_CENTER);
		
		btCollectTax = new Button("Collect Tax");
		btUpgrade = new Button("Upgrade Town");
		btImproveCrop = new Button("Improve Crop yield");
		tfRecruit = new TextField();
		btRecruit = new Button("Recruit Army");
		tfSendTroop = new TextField();
		btSendTroop = new Button("Send Troops");
		btReStartGame= new Button("Restart Game");
		btExit= new Button("Exit");
		
		//Center Pane
		VBox containerCanvas = new VBox(20);
		containerCanvas.getChildren().addAll(canvasGameStart);
		containerCanvas.setAlignment(Pos.CENTER);
			
		//Left Pane
		
		VBox containerLbt = new VBox(20);
		containerLbt.getChildren().addAll(paneMyNeiCity);
		containerLbt.getChildren().addAll(paneMyCity);
		containerLbt.getChildren().addAll(paneMyGeneral);
		containerLbt.setAlignment(Pos.BOTTOM_CENTER);
		containerLbt.setPadding(new Insets(10, 0, 40, 10));

		//Recruit
		HBox paneRecruit= new HBox(10);
		paneRecruit.getChildren().add(tfRecruit);
		paneRecruit.getChildren().add(btRecruit);
		paneRecruit.setAlignment(Pos.BASELINE_RIGHT);
		
		HBox paneSend= new HBox(10);
		paneSend.getChildren().add(tfSendTroop);
		paneSend.getChildren().add(btSendTroop);
		paneSend.setAlignment(Pos.BASELINE_RIGHT);
		
		
		HBox paneRestartAndExit = new HBox(10);
		paneRestartAndExit.getChildren().add(btReStartGame);
		paneRestartAndExit.getChildren().add(btExit);
		paneRestartAndExit.setAlignment(Pos.BASELINE_RIGHT);
		
		VBox paneControl = new VBox();
		paneControl.getChildren().addAll(btCollectTax,btUpgrade,btImproveCrop,paneRecruit,paneSend,paneRestartAndExit);
		paneControl.getStyleClass().add("game-control");
		paneControl.setPadding(new Insets(10, 10, 10, 10));
		paneControl.setAlignment(Pos.BOTTOM_RIGHT);
		
		//Right Pane
		VBox containerRbt = new VBox(20);	
		containerRbt.getChildren().addAll(paneGold);
		containerRbt.getChildren().addAll(paneListViewRandomEvent);
		containerRbt.getChildren().addAll(paneListViewMessage);
		containerRbt.getChildren().addAll(paneControl);
		containerRbt.setAlignment(Pos.BOTTOM_CENTER);
		containerRbt.setPadding(new Insets(40, 40, 40, 40));
		
		HBox contral_pane = new HBox();
		contral_pane.getChildren().addAll(containerLbt,containerRbt);
		contral_pane.setAlignment(Pos.BOTTOM_RIGHT);
		
		
		lbMyNei.getStyleClass().add("menu-title");
		lbMyNeiTitle.getStyleClass().add("game-title");
		lbMyCity.getStyleClass().add("menu-title");		
		lbMyCityTitle.getStyleClass().add("game-title");	
		lbMyGeneral.getStyleClass().add("menu-title");
		lbMyGeneralTitle.getStyleClass().add("game-title");
		lbMyGold.getStyleClass().add("menu-title");
		lbRandomEvent.getStyleClass().add("menu-title");
		lbLog.getStyleClass().add("menu-title");
		btCollectTax.getStyleClass().add("menu-button");
		btUpgrade.getStyleClass().add("menu-button");
		btRecruit.getStyleClass().add("menu-button");
		btImproveCrop.getStyleClass().add("menu-button");
		btSendTroop.getStyleClass().add("menu-button");
		btReStartGame.getStyleClass().add("menu-button");
		btExit.getStyleClass().add("menu-button");
		
		
		BorderPane pane = new BorderPane();
		pane.setRight(contral_pane);
		pane.setCenter(containerCanvas);
		pane.getStyleClass().add("pane-game-start");
		return pane;
	}
	
	
	
	
	
	
	private void handleExitGame()
	{
		/**
		 * Todo: 
		 * 1. The handler for the Button exit and quit
		 * 2. When the button exit or quit is clicked, this function will be called.
		 * 3. When the function is called, there will be a dialog box showing 
		 * 		"Do you want to exit this game?"
		 * 4. If the user choose yes, then you can call Platform.exit();
		 * 
		 * Hint: user Alert: 
		 * 		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to exit this game?", ButtonType.YES, ButtonType.NO);
		 */
		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to exit this game?", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {
			Platform.exit();
		}
	}
	
	//Draw the line in the map
	private void drawPath(int x1,int y1,int x2,int y2)
	{
				
		canvasGameStart.getGraphicsContext2D().strokeLine(x1,y1,x2,y2);
		canvasGameStart.getGraphicsContext2D().setStroke(Color.GREEN);
		canvasGameStart.getGraphicsContext2D().setLineWidth(5);
		canvasGameStart.getGraphicsContext2D().stroke();
		
		
	}
	private void clearLayer(Canvas layer)
	{
		//clear the content of the canvas
		layer.getGraphicsContext2D().clearRect(0, 0, layer.getWidth(), layer.getHeight());
	}
	//load the map and draw the towns in the map
	private void loadTownMap()
	{
		/**
		* TODO: 
		* 1. Draw the town/city/Metropolis in the canvas according to their coordinate
		* 2. Draw the flags next to the town/city/Metropolis's owners. 
		* 	For the 1st player, use the "flag_0.png", for the 2nd player, use the "flag_1.png"
		* 3. Draw the line between the connected town/city/Metropolis according to the data in the "MapData"
		* 	You can use the drawPath(int x1,int y1,int x2,int y2) to draw the line between two points (x1,y1) and (x2,y2).
		*
		*/
		clearLayer(canvasGameStart);
		
		for(int i = 0;i < game.players.size();i++)
		{
			Player player = game.players.get(i);
			for(Town town: player.getTownList())
			{
				int longitute = town.getLongitude();
				int latitude = town.getLatitude();
				String townName = town.getName(); 
				Image image_flag = new Image("file:flag_"+i+".png");
				if(town instanceof Metropolis)
				{
					Image image = new Image("file:metropolis.png");
					
					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);					
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					canvasGameStart.getGraphicsContext2D().drawImage(image_flag,latitude-40,longitute+40);
					
				}
				else if(town instanceof City)
				{
					Image image = new Image("file:city.png");
					
					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					canvasGameStart.getGraphicsContext2D().drawImage(image_flag,latitude-40,longitute+40);
				}
				else if(town instanceof Town)
				{
					Image image = new Image("file:town.png");
					
					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					canvasGameStart.getGraphicsContext2D().drawImage(image_flag,latitude-40,longitute+40);
					
				}
				ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(town);
				for(Town neiTown:neiTowns)
				{
					int x1 = town.getLatitude() + 48;
					int y1 = town.getLongitude() + 48;
					int x2 = neiTown.getLatitude() + 48;
					int y2 = neiTown.getLongitude() +48;
					drawPath(x1,y1,x2,y2);
				}
			}
		}
	
}
	
	private void handleNewGame() 
	{
		/**Todo: the handler for the button "New Game"
		 * 		 when the button "New Game" is clicked, this function will be called
		 * 
		 * Hints:
		 * 
		 * 1. load the map information from the file "MapData.txt":
		 * 		game.gameMap.loadGameMap("MapData.txt");
		 * 2. load the player information from the file "PlayersData.txt":
		 * 		game.loadPlayersData("PlayersData.txt");
		 * 3. call the loadTownMap() to plot the Map;
		 * 4. initialize the humanPlayer and the computerPlayers: 
		 * 		the first user in the "PlayersData.txt" is the humanPlayer;
		 * 5. change the scene: putSceneOnStage(SCENE_STARTGAME);
		 * 6. call the startTurn to start the Game: startTurn()
		 * 
		 *
		 */
		try {
			game.gameMap.loadGameMap("MapData.txt");
			game.loadPlayersData("PlayersData.txt");
			loadTownMap();
			humanPlayer = game.players.get(0);
			for (Player player:game.players) {
				computerPlayers.add(player);
			}
			computerPlayers.remove(0);
			putSceneOnStage(SCENE_STARTGAME);
			updateListView();
			startTurn();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateListViewRandomEvent()
	{
		//Update the listview of random event
		//Since the listview is updated outside the UI thread, Platform.runLater() is used here.
		
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	listViewRandomEventItems.clear();
            	for(int i = 0; i < RandomEventResult.size(); i++)
            	{
            		listViewRandomEventItems.add(RandomEventResult.get(i));
            	}
					
            	
            	
              
            }
          }); 
	}
	
	
	private void updateListViewMessageItems()
	{
		//Update the listview of command messages
		listViewMessageItems.clear();
		for(int i = 0;i < printResult.size();i++)
		{
			listViewMessageItems.add(printResult.get(i));
		}
			
		
		
	}
	private void updateListViewNeiItems()
	{
		//Update the listview of Neighbor City
		listViewNeiItems.clear();
		
		Town town = null;
		if(listViewSelectedCity!=null)
		{
			String selectedCityName = listViewSelectedCity.split(" ")[0].trim();
			town = getTown(selectedCityName);
			
		
			ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(town);
			for(Town neiTown:neiTowns)
			{
				int wall = 0;
				if(neiTown instanceof Metropolis)
				{
					wall = ((Metropolis) neiTown).walls;
				}
				listViewNeiItems.add(neiTown.name + "           " + neiTown.population + "             " + neiTown.armySize + "        " + neiTown.cropYield + "          " + wall);
			}
		}
			
		
		
	}
	private void updateListViewCityItems()
	{
		/**
		 *  TODO: 
		 *  To update the listview of the information of the human player's cities.
		 *  1. It is similar to the function updateListViewNeiItems()
		 *  2. Use listViewCityItems.add() to add the information of cities
		 */
		listViewCityItems.clear();
		for(Town town:humanPlayer.getTownList()){
			int wall = 0;
			if(town instanceof Metropolis)
			{
				wall = ((Metropolis) town).walls;
			}
			listViewCityItems.add(town.name + "           " + town.population + "             " + town.armySize + "        " + town.cropYield + "          " + wall);
		}	
	}
	
	private void updateListViewGeneralItems() {
		/**
		 *  TODO: 
		 *  To update the listview of the information of the human player's generals.
		 *  1. It is similar to the function updateListViewNeiItems()
		 *  2. Use listViewGeneralItems.add() to add the information of generals.
		 */
		listViewGeneralItems.clear();
		for(General general:humanPlayer.getGeneralList()){
			String ready = "D";
			if (general.isReady())
				ready = "R";
			listViewGeneralItems.add(general.getName() + "        " + general.getCombatPoint() + "          " + general.getLeadershipPoint() + "            " + general.getWisdomPoint() + "              " + ready);	
		}
	}

	//update the listview in another thread
	private void updateListView()
	{
		//Since the listview should be updated outside the UI thread, the Platform.runLater() is used here.
		//It will update the listview of the human player's generals, cities and neighbor cities.
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
            		updateListViewCityItems();
					updateListViewGeneralItems();
					updateListViewNeiItems();
					updateMyGold();
					
            	
            	
              
            }
          }); 
	}
	
	//update the listview in another thread
	private void updateMessage()
	{
		//Since the listview should be updated outside the UI thread, the Platform.runLater() is used here.
		//It will update the listview of the command information
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
            		updateListViewMessageItems();
					
            	
            	
              
            }
          }); 
	}
	
	//update the gold of the human player
	private void updateMyGold()
	{
		int gold = humanPlayer.getGold();
		lbMyGold.setText("My Gold: " + gold);
	}
	
	private void initWelcomeSceneHandler() 
	{
		/**
		 * TODO:
		 * To initialize the handler for the two buttons in the welcome scene
		 * 1. handleNewGame() for button btNewGame
		 * 2. handleExitGame() for button btQuit
		 */
		btNewGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleNewGame();
			}});
		
		btQuit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleExitGame();
			}});
	}
	
	
	private void initListView()
	{
		/**
		 * Initialize the listeners for all listview
		 */
		
		listViewGeneral.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					//when a row in the listview is clicked, the listViewSelectedGeneral will be set as the content of that row
					listViewSelectedGeneral = new_val;
					
				}
			}
		});
		
		
		listViewCity.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					listViewSelectedCity = new_val;
					updateListViewNeiItems();
					
				}
			}
		});
		
		
		listViewNei.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					listViewSelectedNei = new_val;
					
				}
			}
		});
	}
	
	private void initStartGameSceneHandler()
	{
		/**
		 *  Todo: 
		 *  To initialize the handle for all listView and button in the start game pane.
		 *  1. call the initListView(); to initialize the listview
		 *  2. call setOnAction for each button
		 */
		initListView();
		btCollectTax.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleCollectTax();
		}});
		btUpgrade.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleUpgrade();
		}});
		btImproveCrop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleImproveCrop();
		}});
		btRecruit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleRecruit();
		}});
		btSendTroop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleSendTroop();
		}});
		btReStartGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleReStartGame();
		}});
		btExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleExitGame();
		}});
	}
	
	private void showGameOver()
	{
		/**
		 * Todo: 
		 * TO Show Game Over using Dialog box. 
		 * When the game is over, this function will be called.
		 * Hint: 
		 * 1. Use another thread to handle it. You may use Platform.runLater in this function.
		 * 2. Show "YOU LOSE!" if the human player loses the game, otherwise "YOU WIN!"
		 * 3. You can use 
		 * 		Alert alert = new Alert(AlertType.CONFIRMATION, result, ButtonType.YES, ButtonType.NO);
		 * 		where result is the String to be presented
		 */
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	String result = "YOU WIN!\nDo you want to exit this game?";
            	if (humanPlayer.getTownList().size() == 0)
            		result = "YOU LOSE!\nDo you want to exit this game?";
            	Alert alert = new Alert(AlertType.CONFIRMATION, result, ButtonType.YES, ButtonType.NO);
        		Optional<ButtonType> input = alert.showAndWait();
        		if (input.isPresent() && input.get() == ButtonType.YES) {
        			Platform.exit();
        		}
        		if (input.isPresent() && input.get() == ButtonType.NO) {
        			handleReStartGame();
        		}
            }
		});
	}
	
	private void handleReStartGame() {
		/**
		 * Todo: 
		 * The handle for the button of restart game.
		 * When the button "Restart Game" is clicked, this function will be called.
		 * Hints:
		 * 1. Clear all the players
		 * 2. call game.gameMap.loadGameMap("MapData.txt");
		 	       game.loadPlayersData("PlayersData.txt");
				   loadTownMap();
		   3. Update all the listview
		   4. call the startTurn()
		 */
		game.players.clear();
		gameOver = true;
		try {
			game.gameMap.loadGameMap("MapData.txt");
			game.loadPlayersData("PlayersData.txt");
			System.out.println("Restart Game!");
			printResult.add("Restart Game!");
			updateListViewMessageItems();
			humanPlayer = game.players.get(0);
			updateListView();
			loadTownMap();
			gameOver = false;
			startTurn();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	private void startTurn() {
		/**
		 * TODO: 
		 * 1. This function is to start the thread playerThread.
		 * 2. the function processPlayerTurns() will be run in the playerThread.
		 */
		printResult.add("--------------Game begins!------------");
		printResult.add("-----------------"+game.players.get(0).getName()+"-----------------");
		updateListViewMessageItems();
		playerThread = new Thread(new Runnable(){
			@Override
			public void run() {
				processPlayerTurns();
			}	
		});
		playerThread.start();
	}
	
	public void processPlayerTurns()
	{
		/**
		 * TODO: 
		 * 1. This function is for processing the commands of computer players.
		 * 2. All the computer players have to wait until all the generals of human player are done.
		 * 3. When the game over or when the human player chooses to restart the game, the function will be terminated.
		 * 4. After all the computer players finish their commands, all the generals of human players will be ready.
		 * 5. Each computer player in each round will randomly select a general, a town/city/metropolis and a command.
		 * 6. The same as the PA1, there are five command"
		 * (1)collect tax;(2)upgrade town;(3)improve crop yield;(4)recruit;(5)send troops 
		 * 7. All the output message will be added in the arraylist printResult and print in the log window.
		 * 8. When the game is over, there will be a dialog box to print the result and ask whether to exit the game.
		 * 	  To achieve this goal, you can call showGameOver();
		 * 9. When the button "Restart" is clicked, this function will be over. The variable gameOver can be used here.
		 * 10. If you want to change the UI outside the UI-thread, you should use Platform.runLater(), an example is provides in the updateMessage().
		 */
		while(!(gameOver&&game.isGameOver())){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					if (game.isGameOver()){
						gameOver = true;
					}
					else if (!humanPlayer.hasReadyGenerals()){
						printResult.add("End Turn");
						updateListViewMessageItems();
						processComputerPlayerTurn();
						updateListViewMessageItems();
						processRandowmEvents();
						resetAllGeneral();
						updateListViewGeneralItems();
						printResult.add("-----------------"+game.players.get(0).getName()+"-----------------");
						updateListViewMessageItems();
					}
				}
			});
		}
		showGameOver();
	}
	
	private void processRandowmEvents() {
		for (Player player:game.players)
			for (Town town:player.getTownList()) {
				Random rand = new Random();
				double n = rand.nextDouble();
				town.processTurn(n);
			}
		updateListViewRandomEvent();
	}
	
	private synchronized void processComputerPlayerTurn() {
		for (Player computerPlayer:computerPlayers) {
			printResult.add("-----------------"+computerPlayer.getName()+"-----------------");
			updateListViewMessageItems();
			for (General general:computerPlayer.getGeneralList()) {
				boolean weak = false;
				boolean strong = false;
				Town targetTown = null;
				Town selectTown = null;
				int requireDamage = 0;
				int neiTownAmrySize = 0;
				for (Town town:computerPlayer.getTownList()) {
					ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(town);
					for (Town neiTown:neiTowns)
						if ((!computerPlayer.getTownList().contains(neiTown))&&(neiTown.getArmySize()>town.getArmySize()))
						{
							weak = true;
							System.out.println(town.toString());
							System.out.println(neiTown.toString());
							System.out.println(neiTown.getArmySize());
							System.out.println(town.getArmySize());
							neiTownAmrySize = neiTown.getArmySize();
							selectTown = town;
							System.out.println(selectTown.toString());
							System.out.println("----------------");
							break;
						}
					if (weak)
						break;
					for (Town neiTown:neiTowns) {
						int targetVal = (int)(town.getArmySize() * (general.getCombatPoint()/100f));
						requireDamage = neiTown.getArmySize();
						if (neiTown instanceof Metropolis) {
							requireDamage += ((Metropolis)neiTown).walls;
						}
						if ((!computerPlayer.getTownList().contains(neiTown))&&(targetVal>=requireDamage))
						{
							if (requireDamage==0)  //The town has no army
								requireDamage++;
							strong = true;
							targetTown = neiTown;
							selectTown = town;
							break;
						}
					}
					if (strong)
						break;
				}
				if (weak) {
					int targetVal = (int)(computerPlayer.getGold() * (general.getCombatPoint() / 100f));
					//System.out.println(targetVal);
					//System.out.println(selectTown.getArmySize());
					//System.out.println(neiTownAmrySize);
					System.out.println(selectTown.toString());
					if ((selectTown.getArmySize()+targetVal)>neiTownAmrySize)
						selectTown.recruitArmy(computerPlayer, general, computerPlayer.getGold());
					else selectTown.collectTax(computerPlayer, general);
					weak = false;
				}
				else if (strong) {
					if (selectTown instanceof Metropolis || selectTown instanceof City) {
						((City)selectTown).attackTown(targetTown, general, requireDamage);
						if (targetTown.getArmySize()<=0) {
							game.gameMap.getTownOwner(targetTown).surrenderTown(targetTown, computerPlayer);
							updateListViewMessageItems();
						}
					}
					else {
						if (computerPlayer.getGold()>=50)
							computerPlayer.upgradeTown(selectTown, general);
						else selectTown.collectTax(computerPlayer, general);
						updateListViewMessageItems();
					}
					strong = false;
				}/*
				else {
					Random rand = new Random();
					int randomSelectTown = rand.nextInt(computerPlayer.getTownList().size());
					if (computerPlayer.getGold()>=50) {
						int n = rand.nextInt(2);
						if (n==0){
							computerPlayer.getTownList().get(randomSelectTown).improveCropYield(general);
						}else if (n==1)
							computerPlayer.getTownList().get(randomSelectTown).recruitArmy(computerPlayer, general , computerPlayer.getGold());
					}
					else {
						computerPlayer.getTownList().get(randomSelectTown).collectTax(computerPlayer, general);
					}
				}*/
				updateListView();
				loadTownMap();
				updateListViewMessageItems();
			}
		}
	}
	
	private void resetAllGeneral() {
		for (Player player:game.players)
			player.readyAllGenerals();	
	}
	
	//return the general object given the general's name
	private General getGeneral(String generalName)
	{
		for(General general:humanPlayer.getGeneralList())
		{
			if(general.getName().equals(generalName))
			{
				return general;
			}
		}
		return null;
		
	}
	
	//return the town object given the town's name
	private Town getTown(String townName)
	{
		for(Town town:humanPlayer.getTownList())
		{
			if(townName.equals(town.name))
			{
				return town;
			}
		}
		return null;
	}
	
	//return the general selected by the user
	private General selectGeneral()
	{
		General selectedGeneral = null;
		if(listViewSelectedGeneral!=null)
		{
			String generalName = listViewSelectedGeneral.split(" ")[0];
			selectedGeneral = getGeneral(generalName);
			//check whether generalName is ready
			if(!selectedGeneral.isReady())
			{
				listViewSelectedGeneral = null;	
				String result = "You have to select a ready general";
				printResult.add(result);
				updateListViewMessageItems();
				return null;
			}
			
		}
		return selectedGeneral;
	}
	
	// return the town selected by the user 
	private Town selectTown()
	{
		Town selectedTown = null;
		if(listViewSelectedCity!=null)
		{
			String cityName = listViewSelectedCity.split(" ")[0];
			selectedTown = getTown(cityName);
			
			
		}
		return selectedTown;
		
		
	}
	
	//return the neighbor town selected by the user
	private Town selectTargetTown(Town selectedTown)
	{
		Town targetTown = null;
		if(listViewSelectedNei != null && selectedTown != null)
		{
			String cityName = listViewSelectedNei.split(" ")[0];
			System.out.println(cityName);
			System.out.println(selectedTown.name);
			ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(selectedTown);
			for(Town town:neiTowns)
			{
				System.out.println(town.name);
				if(cityName.equals(town.name))
				{
					targetTown = town;
					break;
				}
			}
			listViewSelectedNei = null;
		}
		
		return targetTown;
	}

	
	private void handleSendTroop() {
		/**
		 * 
		 * When the button send troop is clicked, this function will be executed.
		 *  
		 * 
		 */
		
		General selectedGeneral = null;
		Town selectedTown = null;
		Town targetTown = null;
		//get troopSize
		String tfText = tfSendTroop.getText();
		int troopSize;
		try
		{
			troopSize = Integer.parseInt(tfText);
		}catch(NumberFormatException e)
		{
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
			
	    	return;
		}
		if (troopSize < 0) {
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
	    	return ;	    	
		}
		
		
		//get general
		selectedGeneral = selectGeneral();
		if(selectedGeneral == null)
		{
			System.out.println("general");selectedGeneral = selectGeneral();
		}
		//get Town
		selectedTown = selectTown();
		if(selectedTown == null)
		{
			System.out.println("town");
		}
		//get TargetTown
		targetTown = selectTargetTown(selectedTown);
		
		if(selectedGeneral== null || selectedTown == null || targetTown == null)
		{
			
			String result = "ERROR: You have to select a general, a town and a target town";
			printResult.add(result);
			updateListViewMessageItems();
			//print error info
			return;
		}
		
		if (!(selectedTown instanceof City || selectedTown instanceof Metropolis)) 
		{
			System.out.println("ERROR: Invalid town type");
			String result = "ERROR: Invalid town type";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		if (humanPlayer.getTownList().contains(targetTown)) 
		{
			((City)selectedTown).transferArmy(targetTown, selectedGeneral, troopSize);
			updateListViewMessageItems();
			
		} 
		else 
		{
			
			if (((City)selectedTown).attackTown(targetTown, selectedGeneral, troopSize)) {
				updateListViewMessageItems();
				if (targetTown.getArmySize()<=0) {
					game.gameMap.getTownOwner(targetTown).surrenderTown(targetTown, humanPlayer);
					updateListViewMessageItems();
					
				}
				
				selectedGeneral.endTurn();//modified by GY	
			}
		}
		updateListView();
		updateMyGold();
		loadTownMap();
		
	}

	private void handleUpgrade() {
		
		/**
		 * Todo: 
		 * When the button upgrade is clicked, this function will be called.
		 * 1. call selectGeneral() to obtain the selected general
		 * 2. call selectTown() to obtain the selected town
		 * 3. call upgradeTown(selectedTown, selectedGeneral) to upgrade
		 * 4. if upgrade successfully, call loadTownMap() to redraw the map
		 * 5. update all the listview and myGold
		 */
		General selectedGeneral = null;
		Town selectedTown = null;
		
		selectedGeneral = selectGeneral();
		if(selectedGeneral == null)
		{
			System.out.println("general");selectedGeneral = selectGeneral();
		}

		selectedTown = selectTown();
		if(selectedTown == null)
		{
			System.out.println("town");
		}
		
		if(selectedGeneral== null || selectedTown == null)
		{
			
			String result = "ERROR: You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			//print error info
			return;
		}
				
		Player currentPlayer = game.gameMap.getTownOwner(selectedTown);
		currentPlayer.upgradeTown(selectedTown,selectedGeneral);
		updateListViewMessageItems();
		
		updateListView();
		updateMyGold();
		loadTownMap();
	}

	private void handleRecruit() {
		/**
		 * Todo: 
		 * When the button recruit is clicked, this function will be executed.
		 * 1. Obtain the input budget from the TextField using tfRecruit.getText();
		 * 2. call selectGeneral() and selectTown() to obtain the selected general and town
		 * 3. call selectedTown.recruitArmy(humanPlayer, selectedGeneral, budget) to execute the command
		 * 4. Update all the listview and myGold
		 */
		General selectedGeneral = null;
		Town selectedTown = null;
		
		String tfText = tfRecruit.getText();
		int recruitSize;
		
		try
		{
			recruitSize = Integer.parseInt(tfText);
		}catch(NumberFormatException e)
		{
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
			
	    	return;
		}
		if (recruitSize < 0) {
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
	    	return;	    	
		}
		
		if(selectedGeneral == null)
		{
			System.out.println("general");selectedGeneral = selectGeneral();
		}

		selectedTown = selectTown();
		if(selectedTown == null)
		{
			System.out.println("town");
		}
		
		if(selectedGeneral== null || selectedTown == null)
		{
			
			String result = "ERROR: You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			//print error info
			return;
		}
		
		Player currentPlayer = game.gameMap.getTownOwner(selectedTown);
		selectedTown.recruitArmy(currentPlayer,selectedGeneral,recruitSize);
		updateListViewMessageItems();
		
		updateListView();
		updateMyGold();
		loadTownMap();
	}

	private void handleCollectTax() {
		/**
		 * Todo: 
		 * When the button collect tax is clicked, this function is called.
		 * 1. selectGeneral() for obtaining the selected general
		 * 2. selectTown() for obtaining the selected town
		 * 3. call selectedTown.collectTax(humanPlayer, selectedGeneral) to execute the command
		 * 4. update all the listview and myGold:
		 * 		updateListViewCityItems();
		 * 		updateListViewGeneralItems();
		 * 		updateListViewMessageItems();
		 * 		updateMyGold();
		 * 		
		 */
		General selectedGeneral = null;
		Town selectedTown = null;
		
		selectedGeneral = selectGeneral();
		if(selectedGeneral == null)
		{
			System.out.println("general");selectedGeneral = selectGeneral();
		}

		selectedTown = selectTown();
		if(selectedTown == null)
		{
			System.out.println("town");
		}
		
		if(selectedGeneral== null || selectedTown == null)
		{
			
			String result = "ERROR: You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			//print error info
			return;
		}
				
		Player currentPlayer = game.gameMap.getTownOwner(selectedTown);
		selectedTown.collectTax(currentPlayer, selectedGeneral);
		
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewMessageItems();
		updateMyGold();
	}
	
	
	private void handleImproveCrop() {
		/**
		 * Todo: 
		 * When the button improve crop is clicked, this function will be called.
		 * Hints:
		 * 1. selectGeneral() for obtaining the selected general
		 * 2. selectTown() for obtaining the selected town
		 * 3. call selectedTown.improveCropYield(selectedGeneral) to execute the command
		 * 4. update all the listview and myGold:
		 * 		updateListViewCityItems();
		 * 		updateListViewGeneralItems();
		 * 		updateListViewMessageItems();
		 * 		updateMyGold();
		 * 		
		 *
		 */
		General selectedGeneral = null;
		Town selectedTown = null;
		
		selectedGeneral = selectGeneral();
		if(selectedGeneral == null)
		{
			System.out.println("general");selectedGeneral = selectGeneral();
		}

		selectedTown = selectTown();
		if(selectedTown == null)
		{
			System.out.println("town");
		}
		
		if(selectedGeneral== null || selectedTown == null)
		{
			
			String result = "ERROR: You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			//print error info
			return;
		}
		
		selectedTown.improveCropYield(selectedGeneral);
		
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewMessageItems();
		updateMyGold();
	}

	private void initEventHandlers() 
	{
		
		initWelcomeSceneHandler();
		initStartGameSceneHandler();
		
	}

	
	private void initScenes() 
	{
		scenes[SCENE_WELCOME] = new Scene(paneWelcome(), 1800,1000);
		scenes[SCENE_STARTGAME] = new Scene(paneStartGame(), 1800,1000);
		
		for (int i = 0; i < SCENE_NUM; i++)
		{
			scenes[i].getStylesheets().add("menu_and_css/styles.css"); // share stylesheet for all scenes
		}
	}

	private void putSceneOnStage(int sceneID) 
	{
		// ensure the sceneID is valid
		if (sceneID < 0 || sceneID >= SCENE_NUM)
		{
			return;
		}

		stage.hide();
		stage.setTitle(SCENE_TITLES[sceneID]);
		stage.setScene(scenes[sceneID]);
		stage.show();
	}
	
	

	@Override
	public void start(Stage primaryStage) throws Exception {
			
		initScenes();
		initEventHandlers();
		stage = primaryStage;
		putSceneOnStage(SCENE_WELCOME);
		
	}
	public static void main(String[] args)
	{
		launch(args);
	}
	
	
}
