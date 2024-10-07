package hw0_packing;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PackingApp extends Application
{
	// global constants in PackingApp class
	public static final double APP_WIDTH = 600;
	public static final double APP_HEIGHT = 600;
	public static final double CONTROL_HEIGHT = 50;
	public static final double H_SPACE = 20;
	public static final double PADDING = 7;
	public static final int DURATION = 20;
	public static final int INTERVAL = 1;
	
	// global GUI variables
	private BorderPane pane;
	private HBox controlPanel;
	private PackingCanvas packing_space;
	private Button new_game;
	private Button quit_game;
	private Label time_remaining;
	private Timeline count_down = new Timeline();
	private Label score_label;
	
	// non_GUI variables
	public int time = DURATION;
	public double score;

	public void start(Stage primaryStage) {
		
		// setup Panes and Controls of the main view
		setupPanes();
		setupControls();
		
		// also setup Timeline for the game
		setupTimeline();

		Scene scene = new Scene(pane, APP_WIDTH, APP_HEIGHT);
		primaryStage.setTitle("CS216 Packing Disk");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Set up all the control elements and designs of the main pane
	 */
	
	private void setupPanes() {
		// the main pane of the game is borderpane
		pane = new BorderPane();
		
		// set space, alignment, and padding of the top of the main pane
		controlPanel = new HBox(H_SPACE);
		controlPanel.setAlignment(Pos.CENTER);
		controlPanel.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		
		// place the packing_space in the center of the main pane
		packing_space = new PackingCanvas(this, PackingCanvas.CANVAS_WIDTH, PackingCanvas.CANVAS_HEIGHT);
		
		// place the controlPanel at the top 
		pane.setTop(controlPanel);
		
		// place the canvas in the center of main pane
		pane.setCenter(packing_space);
	}
	
	/**
	 * Set up all the control buttons and labels for the game
	 */
	
	private void setupControls() {
		// create button new_game
		new_game = new Button("New Game");
		new_game.setOnAction(e -> { newGame(); });
		
		// create button quit_game
		quit_game = new Button("Quit Game");
		quit_game.setOnAction(e -> { quitGame(); });
		
		// create label for time and score
		time_remaining = new Label("Time Remaining: " + time);
		score_label = new Label("Score: " + score);
		
		// add all of these elements into the controlPanel
		controlPanel.getChildren().addAll(new_game, quit_game, time_remaining, score_label);
	}
	
	/**
	 * Set up the countdown timeline for the game
	 */
	private void setupTimeline() {
		
		// create a keyframe which countdown 1 second and update the time
		KeyFrame kf = new KeyFrame(Duration.seconds(1), e -> updateTime());
		
		// set cycle count of the timeline by a duration time
		count_down = new Timeline(kf);
		count_down.setCycleCount(DURATION);
		
		// method gameOver() will be triggered when the time finishes
		count_down.setOnFinished(e -> {
			gameOver();
		});
	}
	
	/**
	 * Update the score variable and score label
	 * 
	 * @param score
	 */
	void updateScore(double score) {
		
		// increase current score by score parameter
		this.score += score;
		
		// update the score label
		score_label.setText("Score: " + this.score);
	}
	
	/**
	 * Update time and change the time label displayed in the main pane
	 */
	
	void updateTime() {
		time = time - INTERVAL;
		time_remaining.setText("Time Remaining: " + time);
	}
	
	/**
	 * Calls newGame() in canvas and reinitialize game variables
	 */
	
	private void newGame() {
		score = 0;
		score_label.setText("Score: " + score);
		
		count_down.play();
		packing_space.newGame();
	}
	
	/**
	 * Call gameOver() in canvas remove all listeners and stop the countdown
	 */
	
	public void gameOver() {
		packing_space.gameOver();
		count_down.stop();
	}
	
	/**
	 * Close the program when player press button quit_game
	 */
	
	private void quitGame() {
		Platform.exit();
		System.exit(0);
	}

	public static void main(String[] args)
	{
		Application.launch(args);
	}

}
