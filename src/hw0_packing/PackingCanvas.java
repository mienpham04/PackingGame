package hw0_packing;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PackingCanvas extends Canvas
{
	// global constants of the radius of the smallest and the largest disks
	public static final int R_SMALL = 50;
	public static final int R_LARGE = 70;
	
	// the fixed size of canvas width and height
	public static final double CANVAS_WIDTH = 580;
	public static final double CANVAS_HEIGHT = 580;
	
	// contants of the size of the area where player drag disks in
	public static final double BIN_TOP_X = 30;
	public static final double BIN_TOP_Y = 60;
	
	public static final double BIN_WIDTH = 300;
	public static final double BIN_HEIGHT = 400;
	
	// measurements of score label
	public static final double SCORE_TOP_X = 400;
	public static final double SCORE_TOP_Y = 100;
	
	public static final double SCORE_WIDTH = 70;
	public static final double SCORE_HEIGHT = 50;
	
	// the variables of container where disks are created and be ready to be dragged
	public static final double CONTAINER_TOP_X = 350;
	public static final double CONTAINER_TOP_Y = 270;
	
	public static final double CONTAINER_WIDTH = 200;
	public static final double CONTAINER_HEIGHT = 190;
	
	// the boder's measure of rectangles painted on canvas
	public static final double RECT_BORDER = 3;
	
	// calculate the centerX and centerY to put disks in the center of container
	public static final double CENTER_X = CONTAINER_TOP_X + 0.5 * CONTAINER_WIDTH;
	public static final double CENTER_Y = CONTAINER_TOP_Y + 0.5 * CONTAINER_HEIGHT;
	
	// create member variables
	private GraphicsContext gc;
	private PackingApp myApp;
	
	// boolean to keep track of whether a disk is picked
	private boolean isPicked;
	
	// create disk and the arraylist of disks
	private Disk disk;
	private ArrayList<Disk> disks = new ArrayList<>();
	
	/**
	 * Create a new canvas and set up all variables in the main pane
	 */
	
	public PackingCanvas(PackingApp app, double w, double h) {
		super(w, h);
		this.myApp = app;

		gc = this.getGraphicsContext2D();
	}
	
	/**
	 * Initialize all the game variables and initial state of the game
	 */
	
	private void init() {
		// Initialize variables
		isPicked = false;
		disk = new Disk(CENTER_X, CENTER_Y, randomRadius());
		myApp.score = 0;
		myApp.time = PackingApp.DURATION;
		
		// set up all listeners
		setupListener();
		
		// draw graphical components of the game
		paint();
	}
	
	/**
	 * Draw all game graphical components
	 */
	
	public void paint() {
		// draw the main bin to drag disks in
		gc.setFill(Color.LIGHTGREY);
		gc.fillRect(BIN_TOP_X, BIN_TOP_Y, BIN_WIDTH, BIN_HEIGHT);
		
		// draw rectangle to contain score value
		gc.setStroke(Color.BLACK);
		gc.strokeRect(SCORE_TOP_X, SCORE_TOP_Y, SCORE_WIDTH, SCORE_HEIGHT);
		
		// draw score label
		gc.setStroke(Color.GREEN);
		gc.strokeText("SCORE", SCORE_TOP_X + 0.5*0.5*SCORE_WIDTH, SCORE_TOP_Y - SCORE_HEIGHT/2);
		
		// draw container with blue outlier
		gc.setStroke(Color.BLUE);
		gc.strokeRect(CONTAINER_TOP_X, CONTAINER_TOP_Y, CONTAINER_WIDTH, CONTAINER_HEIGHT);
		
		// draw disk
		disk.draw(gc);
		
		// draw the score text
		gc.setStroke(Color.BLACK);
		gc.strokeText("" + (int)(myApp.score), SCORE_TOP_X + 0.5*0.5*SCORE_WIDTH, SCORE_TOP_Y + SCORE_HEIGHT/2);
		
		// draw all the disks exist in the Disk Arraylist
		for (Disk d: disks) {
			d.draw(gc);
		}
	}

	/**
	 * Generate randomColor
	 */
	
	public static Color randomColor() {
		return Color.color(Math.random(), Math.random(), Math.random());
	}
	
	/**
	 * Generate random radius between the largest radius and the smallest radius
	 */
	
	public int randomRadius() {
		Random random = new Random();
		int n = R_LARGE - R_SMALL + 1;
		return random.nextInt(n) + R_SMALL;
	}
	
	/**
	 * Set up all listeners to press, drag, and release disks in the game
	 */
	
	public void setupListener() {
		
		/*
		 * Create a variable isPicked to keep track of the drag disk
		 * If the click is inside the disk and the disk is in the container, then the isPicked will return true
		 */
		setOnMousePressed(e -> {
			if (disk.inside(e.getX(), e.getY()) && disk.within(CONTAINER_TOP_X, CONTAINER_TOP_Y, CONTAINER_WIDTH, CONTAINER_HEIGHT)) {
				isPicked = true;
			}
		});
		
		// clear the canvas, move the disk then paint everything again
		setOnMouseDragged(e -> {
			if (isPicked) {
				gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
				disk.setCenter(e.getX(), e.getY());
				paint();
			}
		});
		
		// move the disk and check if it overlap with others or get out of the bin
		setOnMouseReleased(e -> {
			isPicked = false;
			
			if (!disk.within(BIN_TOP_X, BIN_TOP_Y, BIN_WIDTH, BIN_HEIGHT) && disk.distance(CENTER_X, CENTER_Y) != 0) {
				myApp.gameOver();
			}
			
			else if(overlapAny(disk)) {
				disk.reset();
				paint();
			}
			
			else if (disk.within(BIN_TOP_X, BIN_TOP_Y, BIN_WIDTH, BIN_HEIGHT)){
				disks.add(disk);
				myApp.updateScore(0.5 * disk.area());
				gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
				disk = new Disk(CENTER_X, CENTER_Y, randomRadius());
				paint();
			}
		});
	}
	
	/**
	 * Goes through the loop and check whether a disk overlap with others
	 * @param disk
	 * @return whether the disk overlap or not
	 */
	boolean overlapAny(Disk d) {
		for(Disk disk: disks) {
			if(disk.overlap(d) == true) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Clear the canvas and reset all the parameters in the game
	 */
	void newGame() {
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		Disk.id = 0;
		clearBin();
		init();
	}
	
	/**
	 * Set conditions to end the game by removing all the listeners and draw the "Game Over!" text
	 */
	void gameOver() {
		setOnMousePressed(null);
		setOnMouseDragged(null);
		setOnMouseReleased(null);
		
		gc.clearRect(CONTAINER_TOP_X + RECT_BORDER, CONTAINER_TOP_Y + RECT_BORDER, CONTAINER_WIDTH - RECT_BORDER, CONTAINER_HEIGHT - RECT_BORDER);
		gc.setStroke(Color.RED);
		gc.strokeText("GAME OVER!", CENTER_X - 0.5*0.5*CONTAINER_WIDTH, CENTER_Y);
	}
	
	/**
	 * Clear all the disks in the ArrayList
	 */
	void clearBin() {
		disks.clear();
	}
}
