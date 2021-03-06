package blocks;

import java.awt.Graphics;
import java.util.Map;

import background.HoldPanel;

public class LeftZBlock extends AbstractShape{

	public LeftZBlock(Map<Integer, Integer> currentCollumsHighestRows, SingleBlock[][] currentBlocksOnBoard, Graphics currentTetrisBoardGraphics, HoldPanel holdPanel) {
		super(currentCollumsHighestRows, currentBlocksOnBoard, currentTetrisBoardGraphics, holdPanel);

		blocks = new SingleBlock[4];
		for(int counter = 0; counter < 4; counter++){	
			blocks[counter] = new SingleBlock(this.shapeColor);	
			switch(counter){
			case 0: 
				rotationBlock = blocks[counter];
				lowestBlocks[rotationBlock.getCollum()/10] = blocks[counter];
				blocks[counter].setHoldCollum(1);
				blocks[counter].setHoldRow(0);
				break;
			case 1:
				blocks[counter].setXOffset(10);
				blocks[counter].setYOffset(0);
				rightMostBlock = blocks[counter];
				lowestBlocks[(rotationBlock.getCollum() + blocks[counter].getXOffset())/10] = blocks[counter];
				blocks[counter].setHoldCollum(0);
				blocks[counter].setHoldRow(0);
				break;
			case 2:
				blocks[counter].setXOffset(0);
				blocks[counter].setYOffset(10);
				blocks[counter].setHoldCollum(1);
				blocks[counter].setHoldRow(1);
				break;
			case 3:
				leftMostBlock = blocks[counter];
				blocks[counter].setXOffset(-10);
				blocks[counter].setYOffset(10);
				blocks[counter].setHoldCollum(0);
				blocks[counter].setHoldRow(1);
				break;
			}
		}
	}

}
