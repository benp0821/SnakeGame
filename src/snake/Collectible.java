/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

/**
 *
 * @author Benjamin
 */
public class Collectible{
    public static int X = 0;
    public static int Y = 0;
    public static int COLLECTED_COUNTER = 0;
    
    public static void randomizeLocation(int screenWidth, int screenHeight, Snake s){
    	int xVal = (int) (Math.round((Math.random()*(screenWidth-Snake.SIZE))/Snake.SIZE)*Snake.SIZE);
        int yVal = (int) (Math.round((Math.random()*(screenHeight-Snake.SIZE))/Snake.SIZE)*Snake.SIZE);
        for (int i = 0; i < s.getSnake().size(); i++){
        	if (xVal == s.getSnake().get(i).x && yVal == s.getSnake().get(i).y){
        		xVal = (int) (Math.round((Math.random()*(screenWidth-Snake.SIZE))/Snake.SIZE)*Snake.SIZE);
                yVal = (int) (Math.round((Math.random()*(screenHeight-Snake.SIZE))/Snake.SIZE)*Snake.SIZE);
                i = 0;
        	}
        }
        X = xVal;
        Y = yVal;
    }
}
