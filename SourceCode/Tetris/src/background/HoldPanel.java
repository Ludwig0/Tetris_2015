package background;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import blocks.AbstractShape;
import blocks.SingleBlock;

public class HoldPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isBlockHeld = false;
	private AbstractShape heldShape;
	private Graphics holdGraphics;

	@Override
	public void addNotify(){
		super.addNotify();
		setPreferredSize(new Dimension(60,80));
		drawHeld();
	}

	public void newHold(AbstractShape newHeldShape){
		isBlockHeld = true;
		heldShape = newHeldShape;
		drawHeld();
	}

	public void drawHeld(){
		holdGraphics = this.getGraphics();
		holdGraphics.setColor(Color.BLACK);
		holdGraphics.fillRect(0, 0, 60, 80);
		if(isBlockHeld){
			for(SingleBlock currentBlock : heldShape.blocks){
				holdGraphics.setColor(heldShape.shapeColor);
				holdGraphics.fillRect(currentBlock.getHoldCollum() * 20, 60- currentBlock.getHoldRow() * 20, 20, 20);
				holdGraphics.setColor(Color.white);
				for(int lineNumber = 0; lineNumber < 3; lineNumber++){
					holdGraphics.drawRect(currentBlock.getHoldCollum()*20 + lineNumber, 60  - currentBlock.getHoldRow() * 20 + lineNumber, 20 - 2*lineNumber, 20 - 2*lineNumber);
				}		
			}
		}
	}
}
