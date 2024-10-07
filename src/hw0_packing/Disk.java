package hw0_packing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Disk
{
	// create variables centerX and centerY which are the center of the container rectangle
	public double centerX;
	public double centerY;
	
	// create variables initial X and initial Y to keep the origin coordinator of the disk
	private double initX;
	private double initY;
	
	// variables radius and color which are characteristics of disks
	public double radius;
	private Color color;
	
	// another idColor variable to generate different random color for the id number
	private Color idColor;
	
	// keep track of the origin id of the previous disk
	public int personalId;
	
	// static id for the whole ArrayList of disks
	public static int id;
	
	/**
	 * Return the square of parameter
	 * 
	 * @param x parameter
	 * @return square of x
	 */
	public double square(double x) {
		return x * x;
	}
	
	/**
	 * Create and initialize disk object in the game
	 * 
	 * @param cx center x
	 * @param cy center y
	 * @param radius radius of the disk in the game
	 */
	public Disk(double cx, double cy, double radius) {
		
		// set the initX and initY to the center of the rectangle container
		this.initX = cx;
		this.initY = cy;
		
		// initialize variables
		this.centerX = cx;
		this.centerY = cy;
		this.radius = radius;
		
		// set random color for disks and id display
		color = Color.color(Math.random(), Math.random(), Math.random());
		idColor = Color.color(Math.random(), Math.random(), Math.random());
		
		personalId = id;
		id += 1;
	}
	
	/**
	 * Draw disks with their color and id characteristics
	 * 
	 * @param g canvas graphic context
	 */
	public void draw(GraphicsContext g) {
		
		// set color and draw the oval
		g.setFill(color);
		g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
		
		// set stroke and draw the text of the id on disks
		g.setStroke(idColor);
		g.strokeText("" + personalId, centerX, centerY);
	}
	
	/**
	 * Calculate the area of disk by square its radius
	 * 
	 * @return area value
	 */
	public double area() {
		return square(radius);
	}
	
	/**
	 * Set the x and y coordinate of the disk
	 * 
	 * @param x x coordinate of disk
	 * @param y y coordinate of disk
	 */
	public void setCenter(double x, double y) {
		centerX = x;
		centerY = y;
	}
	
	/**
	 * Return the disk to the original position by setting the centerX and centerY which has been changed through dragging to initialX and inital Y
	 */
	public void reset() {
		this.centerX = initX;
		this.centerY = initY;
	}
	
	/**
	 * Check whether the point x, y is inside the disk
	 * 
	 * @param x x coordinate of the point need to be checked
	 * @param y y coordinate of that point
	 * @return a boolean whether that point is inside or not
	 */
	public boolean inside(double x, double y) {
		if(x <= centerX + radius && x >= centerX - radius && y <= centerY + radius && y >= centerY - radius) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check whether the point or the center of the disk is in the disk container
	 * 
	 * @param x the x coordinate of the point that need to be checked
	 * @param y the y coordinate of that point
	 * @param w the width of the container
	 * @param h the height of the container
	 * 
	 * @return a boolean whether the disk is inside the container
	 */
	public boolean within(double x, double y, double w, double h) {
		if(centerX - radius >= x && centerY - radius >= y && 
				centerX + radius <= x + w && centerY + radius <= y + h) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Calculate the distance to 2 points
	 * 
	 * @param xp x position of the point
	 * @param yp y position of that point
	 * 
	 * @return the distance of the 2 points to the center coordinate
	 */
	public double distance(double xp, double yp) {
		return Math.sqrt((square(centerX - xp) + square(centerY - yp)));
	}
	
	/**
	 * Calculate the distance of a disk to the other
	 * 
	 * @param d a disk that need to caculated
	 * @return the distance
	 */
	public double distance(Disk d) {
		double distance = distance(d.centerX, d.centerY);
		return distance;
	}
	
	/**
	 * @param d the disk that is taken to be checked
	 * 
	 * @return whether that disk overlap with other disks
	 */
	public boolean overlap(Disk d) {
		double distance = distance(d);
		
		// Calculate if the distance is less than sum of the radius of disks. Then they overlap each other
		if(distance < d.radius + this.radius) {
			return true;
		}
		else {
			return false;
		}
	}
}
