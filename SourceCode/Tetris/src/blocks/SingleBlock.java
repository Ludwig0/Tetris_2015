
package blocks;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Brett
 */
public class SingleBlock {

	//0 is the far left
	private int currentCollum;
	//0 is the bottom
	private int currentRow;
	//offset from rotation block important to make rotation easy
	private int xOffset;
	private int yOffset;
	private int holdCollum;
	private int holdRow;
	public int smallRow = 20;
	//the color that is chosen when the shape is made
	private Color blockColor;
	//Constants for horizontal movement, yes their just random numbers so you actually use them
	public final int LEFT = 27;
	public final int RIGHT = 34;
	private int ghostRow  = 0;
	
	public SingleBlock(Color newBlockColor){

		blockColor = newBlockColor;
		currentCollum = 50;
		currentRow = 194;
	}

	public void changeCollum(int directionOfMovement){
			if(directionOfMovement == RIGHT){
				currentCollum += 10;
			}
			if(directionOfMovement == LEFT){
				currentCollum -=10;
			}
	}

	public void changeBlockDirection(SingleBlock rotationBlock){
		int oldXOffset = xOffset;
		xOffset = yOffset;
		yOffset = -oldXOffset;
		currentCollum = rotationBlock.getCollum() + xOffset;
		currentRow = rotationBlock.getRow() + yOffset;
		if(yOffset > 0){
			smallRow = smallRow - yOffset/10;
		}else{
			smallRow = smallRow + yOffset/10;
		}
	}
	
	public boolean isDirectionValid(SingleBlock rotationBlock){
		int testXOffset = yOffset;		
		if(rotationBlock.getCollum() + testXOffset >= 0 && rotationBlock.getCollum() + testXOffset <= 90 && rotationBlock.getRow() + -xOffset - 10 > 0){
			return true;
		}else{
			return false;
		}
		
	}

	public void moveDownRotationBlock(){
		if(currentRow > 0){
			currentRow--;
			if((currentRow + yOffset) %10 == 0){
				smallRow = (currentRow + yOffset)/10;
			}
		}else{
			currentRow = 0;
		}
	}
	
	public void moveDownBlocks(SingleBlock rotationBlock){
		currentCollum = rotationBlock.getCollum() + xOffset;
		currentRow = rotationBlock.getRow() + yOffset;
	}
	
	public void setXOffset(int newXOffset){
		xOffset = newXOffset;
	}
	
	public int getXOffset(){
		return xOffset;
	}
	
	public void setYOffset(int newYOffset){
		yOffset = newYOffset;
	}
	
	public int getYOffset(){
		return yOffset;
	}
	
	public void setRow(int newRowNumber){
		currentRow = newRowNumber;
	}

	public void setCollum(int newCollumNumber){
		currentCollum = newCollumNumber;
	}

	public int getRow(){
		return currentRow;
	}

	public int getCollum(){
		return currentCollum;
	}
	
	public void setHoldCollum(int newHoldCollum){
		holdCollum = newHoldCollum;
	}
	
	public int getHoldCollum(){
		return holdCollum;
	}
	
	public void setHoldRow(int newHoldRow){
		holdRow = newHoldRow;
	}
	
	public int getHoldRow(){
		return holdRow;
	}
	
	public void paintOntoFrame(Graphics g){
		g.setColor(blockColor);
		g.fillRect(currentCollum*4, 760 - currentRow * 4, 40, 40);
		
		g.setColor(Color.white);
		for(int lineNumber = 0; lineNumber < 3; lineNumber++){
			g.drawRect(currentCollum*4 + lineNumber, 760-  currentRow * 4 + lineNumber, 40 - 2*lineNumber, 40 - 2*lineNumber);
			
		}
	}
	
	public void setGhostRow(int newRow){
		ghostRow = newRow;
	}
	
	public int getGhostRow(){
		return ghostRow;
	}
	
	public void drawGhost(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(currentCollum*4, 760 - ghostRow * 4, 40, 40);

	}
}