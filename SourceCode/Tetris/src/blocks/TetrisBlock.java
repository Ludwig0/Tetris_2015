package blocks;

import java.awt.Graphics;
import java.util.Map;

import background.HoldPanel;

public class TetrisBlock extends AbstractShape{

	public TetrisBlock(Map<Integer, Integer> currentCollumsHighestRows, SingleBlock[][] currentBlocksOnBoard, Graphics currentTetrisBoardGraphics, HoldPanel holdPanel) {
		super(currentCollumsHighestRows, currentBlocksOnBoard, currentTetrisBoardGraphics, holdPanel);
		
		blocks = new SingleBlock[4];
		for(int counter = 0; counter < 4; counter++){	
			blocks[counter] = new SingleBlock(this.shapeColor);	
			switch(counter){
			case 0: 
				rotationBlock = blocks[counter];
				lowestBlocks[rotationBlock.getCollum()/10] = blocks[counter];
				rightMostBlock = blocks[counter];
				leftMostBlock = blocks[counter];
				blocks[counter].setHoldCollum(1);
				blocks[counter].setHoldRow(1);
				break;
			case 1:
				lowestBlocks[rotationBlock.getCollum()/10] = blocks[counter];
				rightMostBlock = blocks[counter];
				leftMostBlock = blocks[counter];
				blocks[counter].setXOffset(0);
				blocks[counter].setYOffset(-10);
				blocks[counter].setHoldCollum(1);
				blocks[counter].setHoldRow(0);
				break;
			case 2:
				blocks[counter].setXOffset(0);
				blocks[counter].setYOffset(10);
				blocks[counter].setHoldCollum(1);
				blocks[counter].setHoldRow(2);
				break;
			case 3:
				blocks[counter].setXOffset(0);
				blocks[counter].setYOffset(20);
				blocks[counter].setHoldCollum(1);
				blocks[counter].setHoldRow(3);
				break;
			}
		}
	}

}
