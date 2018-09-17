package blocks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Random;

import background.HoldPanel;

public class AbstractShape implements KeyListener{

	public SingleBlock[] blocks;
	public Color shapeColor;
	public boolean isHeld = false;
	protected SingleBlock rotationBlock;
	protected SingleBlock leftMostBlock;
	protected SingleBlock rightMostBlock;
	protected Graphics tetrisBoardGraphics;
	protected SingleBlock[] lowestBlocks = new SingleBlock[10];
	protected Map<Integer, Integer> collumsHighestRows;
	protected SingleBlock[][] blocksOnBoard;
	protected SingleBlock lowestBlock;
	public boolean hasSwapped = false;
	protected HoldPanel holdPanel;
	protected boolean warpRequested = false;
	public int rotateLimiter = 3;
	public boolean isMoving = true;

	public AbstractShape(Map<Integer, Integer> currentCollumsHighestRows, SingleBlock[][] currentBlocksOnBoard,Graphics currentTetrisBoardGraphics, HoldPanel holdPanel){
		collumsHighestRows = currentCollumsHighestRows;
		blocksOnBoard = currentBlocksOnBoard;
		tetrisBoardGraphics = currentTetrisBoardGraphics;
		this.holdPanel = holdPanel;
		Random randomColor = new Random();

		switch(randomColor.nextInt(5)){
		case 0:
			shapeColor = Color.YELLOW;
			break;
		case 1:
			shapeColor = Color.RED;
			break;
		case 2:
			shapeColor = Color.CYAN;
			break;
		case 3:
			shapeColor = Color.GREEN;
			break;
		case 4:
			shapeColor = Color.MAGENTA;
			break;
		}

		for(int counter = 0; counter < 4; counter++){
			lowestBlocks[counter] = null;
		}
	}

	public void moveBlocks(){
		for(SingleBlock currentBlock : blocks){
			currentBlock.moveDownRotationBlock();
			currentBlock.moveDownBlocks(rotationBlock);
		}
	}

	public void drawBlocks(){
		for(int counter = 0; counter < blocks.length; counter++){
			blocks[counter].paintOntoFrame(tetrisBoardGraphics);
		}
	}

	public void checkIfMoving(){
		for(SingleBlock currentBlock : lowestBlocks){
			if(currentBlock != null){
				if(collumsHighestRows.get(currentBlock.getCollum()/10) >= currentBlock.getRow()){
					isMoving = false;
				}
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		boolean allowedToMove = true;

		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(rightMostBlock.getCollum() != 90){
				for(SingleBlock currentBlock : blocks){
					if(currentBlock.getRow() == 0){
						if(blocksOnBoard[currentBlock.getCollum()/10+1][currentBlock.getRow()/10] != null){
							allowedToMove = false;
						}
					}else{
						if(blocksOnBoard[currentBlock.getCollum()/10+1][currentBlock.getRow()/10-1] != null){
							allowedToMove = false;
						}
					}
				}
			}else{
				allowedToMove = false;
			}
			for(SingleBlock currentBlock : blocks){
				if(allowedToMove){
					currentBlock.changeCollum(currentBlock.RIGHT);
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT){
			if(!hasSwapped){
				holdPanel.newHold(this);
				isHeld = true;
				for(SingleBlock currentBlock : blocks){
					currentBlock.setRow(200);
				}
			}
		}			
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			warpRequested = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(leftMostBlock.getCollum()!= 0){
				for(SingleBlock currentBlock : blocks){
					if(currentBlock.getRow() == 0){
						if(blocksOnBoard[currentBlock.getCollum()/10-1][currentBlock.getRow()/10] != null){
							allowedToMove = false;
						}
					}else{
						if(blocksOnBoard[currentBlock.getCollum()/10-1][currentBlock.getRow()/10-1] != null){
							allowedToMove = false;
						}
					}
				}
			}else{
				allowedToMove = false;
			}
			for(SingleBlock currentBlock : blocks){
				if(allowedToMove){
					currentBlock.changeCollum(currentBlock.LEFT);
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){

			if(isMoving && rotateLimiter > 3){
				boolean isTurning = true;
				while(isTurning){
					for(int counter = 0; counter < 10; counter++){
						lowestBlocks[counter] = null;
					}
					for(SingleBlock currentBlock : blocks){
						if(!currentBlock.isDirectionValid(rotationBlock)){
							isTurning = false;

						}
					}
					if(isTurning){
						for(SingleBlock currentBlock : blocks){
							currentBlock.changeBlockDirection(rotationBlock);
						}
					}
					for(int counter = 0; counter < blocks.length; counter++){
						if(counter == 0){
							rightMostBlock = blocks[counter]; 
							leftMostBlock = blocks[counter];
							lowestBlocks[blocks[counter].getCollum()/10] = blocks[counter];
						}else{
							if(rightMostBlock.getCollum() < blocks[counter].getCollum()){
								rightMostBlock = blocks[counter];
							}
							if(leftMostBlock.getCollum() > blocks[counter].getCollum()){
								leftMostBlock = blocks[counter];
							}
							if(lowestBlocks[blocks[counter].getCollum()/10] == null || lowestBlocks[blocks[counter].getCollum()/10].getRow() > blocks[counter].getRow()){
								lowestBlocks[blocks[counter].getCollum()/10] = blocks[counter];
							}
						}
					}

					for(SingleBlock currentBlock : lowestBlocks){
						if(currentBlock != null){
							if(collumsHighestRows.get(currentBlock.getCollum()) == null || collumsHighestRows.get(currentBlock.getCollum()) < currentBlock.getRow()){
								isTurning = false;
							}
						}
					}
				}
				rotateLimiter = 0;
			}
		}
	}

	public void setAndDrawGhost(){
		boolean isGhostMoving = true;
		while(isGhostMoving){
			for(SingleBlock currentBlock : blocks){
				currentBlock.setGhostRow(currentBlock.getGhostRow() - 1);
			}
			for(SingleBlock currentBlock : blocks){
				if(collumsHighestRows.get(currentBlock.getCollum()/10) >= currentBlock.getGhostRow()){
					isGhostMoving = false;
				}
			}
		}
		if(warpRequested){
			for(SingleBlock currentBlock : blocks){
				currentBlock.setRow(1 + currentBlock.getGhostRow());
			}
			warpRequested = false;
		}
		for(SingleBlock currentBlock : blocks){
			currentBlock.drawGhost(tetrisBoardGraphics);
			currentBlock.setGhostRow(currentBlock.getRow());
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}