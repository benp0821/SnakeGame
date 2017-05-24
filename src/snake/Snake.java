/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Benjamin
 */
public class Snake {
	private ArrayList<Point> theSnake;
	private int invincibleCounter;

	private int directionFacing;
	public static final int FACING_LEFT = 0;
	public static final int FACING_DOWN = 1;
	public static final int FACING_RIGHT = 2;
	public static final int FACING_UP = 3;
	public static final int SIZE = 10;
	public static int SPEED = 75;
	public static final int INVINCIBLE_TIME = 1;
	private boolean invincible;

	public Snake() {
		resetSnake();
	}

	public void resetSnake(){
		theSnake = new ArrayList<>();
		directionFacing = FACING_RIGHT;
		theSnake.add(new Point(-SIZE*2, 0));
		theSnake.add(new Point(-SIZE*3, 0));
		theSnake.add(new Point(-SIZE*4, 0));
		invincible = true;
		invincibleCounter = INVINCIBLE_TIME;
	}
	
	public void moveForward() {
		Point prev = new Point(theSnake.get(0));

		if (directionFacing == FACING_LEFT) {
			moveLeft();
		}
		if (directionFacing == FACING_RIGHT) {
			moveRight();
		}
		if (directionFacing == FACING_UP) {
			moveUp();
		}
		if (directionFacing == FACING_DOWN) {
			moveDown();
		}

		for (int i = 1; i < theSnake.size(); i++) {
			Point prev2 = new Point(theSnake.get(i));
			theSnake.set(i, prev);
			prev = new Point(prev2);
		}
		
		if (invincibleCounter > 0){
			invincibleCounter--;
		}else{
			invincible = false;
		}
	}

	public void moveUp() {
		theSnake.get(0).y -= SIZE;
	}

	public void moveDown() {
		theSnake.get(0).y += SIZE;
	}

	public void moveLeft() {
		theSnake.get(0).x -= SIZE;
	}

	public void moveRight() {
		theSnake.get(0).x += SIZE;
	}

	public boolean checkColCollision() {
		if (Collectible.X == theSnake.get(0).x && Collectible.Y == theSnake.get(0).y) {
			return true;
		}
		return false;
	}

	public boolean checkSnakeCollision() {
		for (int i = 1; i < theSnake.size(); i++) {
			if (theSnake.get(i).equals(theSnake.get(0))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkEdgeCollision(){
		if (theSnake.get(0).y > Window.WINDOW_DIMENSION - SIZE){
			return true;
		}else if (theSnake.get(0).x < 0){
			return true;
		}else if (theSnake.get(0).x > Window.WINDOW_DIMENSION - SIZE){
			return true;
		}else if (theSnake.get(0).y < 0){
			return true;
		}
		return false; 
	}

	public int getDirection() {
		return directionFacing;
	}

	public int getTheSize() {
		return SIZE;
	}

	public void setDirection(int d) {
		if (d >= FACING_LEFT && d <= FACING_UP) {
			directionFacing = d;
		} else
			directionFacing = FACING_RIGHT;
	}

	public ArrayList<Point> getSnake() {
		return theSnake;
	}
	
	public boolean isInvincible(){
		return invincible;
	}
	
	public void setInvincible(boolean invinc){
		invincible = invinc;
	}

}
