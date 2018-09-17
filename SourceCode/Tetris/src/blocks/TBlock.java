package blocks;

import java.awt.Graphics;
import java.util.Map;

import background.HoldPanel;


public class TBlock extends AbstractShape{

	public TBlock(Map<Integer, Integer> collumsHighestRow, SingleBlock[][] blocksOnBoard, Graphics tetrisBoardGraphics, HoldPanel holdPanel){
		super(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
		
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
				blocks[counter].setHoldCollum(2);
				blocks[counter].setHoldRow(0);
				break;
			case 2:
				blocks[counter].setXOffset(-10);
				blocks[counter].setYOffset(0);
				leftMostBlock = blocks[counter];
				lowestBlocks[(rotationBlock.getCollum() + blocks[counter].getXOffset())/10] = blocks[counter];
				blocks[counter].setHoldCollum(0);
				blocks[counter].setHoldRow(0);
				break;
			case 3:
				blocks[counter].setXOffset(0);
				blocks[counter].setYOffset(10);
				blocks[counter].setHoldCollum(1);
				blocks[counter].setHoldRow(1);
				break;
			}
		}
	}
}