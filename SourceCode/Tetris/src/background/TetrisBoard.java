
package background;

import java.awt.*;
import java.util.*;

import javax.swing.JPanel;

import blocks.*;

/**
 *
 * @author Brett
 */
public class TetrisBoard extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//maps each collum with its highest row and is used in collision
	private Map<Integer, Integer> collumsHighestRow = new HashMap<Integer, Integer>(10);
	//First array is collum second array is row, stored like this save time in moving left and right 
	private SingleBlock[][] blocksOnBoard = new SingleBlock[10][20];
	// Image of the game that changes constantly
	private Image tetrisBoardImage;
	//Image of the board containing all the relatively static aspects and is used to clear
	private Image staticTetrisBoardImage;
	//Graphics corresponding to their images
	private Graphics staticTetrisBoardGraphics;
	private Graphics tetrisBoardGraphics;
	private boolean isRunning;
	//Thread in which the game occurs
	private Thread tetrisFrame;
	//Tells if there's already a piece moving down on the board
	private boolean doesMovingPieceExist = false;
	//the current shape going down
	private AbstractShape currentShape = null;
	private AbstractShape heldShape = null;
	private HoldPanel holdPanel;
	private boolean hasSwapped = false;


	public TetrisBoard(HoldPanel gameHoldPanel){
		holdPanel = gameHoldPanel;
	}
	public void run() {
		runGame();
	}

	public void runGame(){
		isRunning = true;
		this.requestFocus();

		long lastUpdate = getTime();
		long timeDiff;
		long sleepTime;
		Random blockDecider = new Random();

		for(int key = 0; key < 10; key ++){
			collumsHighestRow.put(key, 0);
		}

		updateImage();				

		while(isRunning){

			paintTetris();			

			if(!doesMovingPieceExist || currentShape == null || currentShape.isHeld){
				if(currentShape != null){
					this.removeKeyListener(currentShape);
				}
				if(currentShape != null && currentShape.isHeld && !hasSwapped){
					AbstractShape currentShapeSwap = currentShape;
					currentShape = heldShape;
					heldShape = currentShapeSwap;
					hasSwapped = true;
					if(currentShape != null){
						currentShape.hasSwapped = true;
						currentShape.isMoving = true;
					}
				}

				if(currentShape == null || !doesMovingPieceExist){
					switch(blockDecider.nextInt(7)){
					case 0:
						currentShape = new TBlock(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
						break;
					case 1:
						currentShape = new LeftLBlock(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
						break;
					case 2:
						currentShape = new RightLBlock(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
						break;
					case 3:
						currentShape = new TetrisBlock(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
						break;
					case 4:
						currentShape = new SquareBlock(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
						break;
					case 5:
						currentShape = new RightZBlock(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
						break;
					case 6:
						currentShape = new LeftZBlock(collumsHighestRow, blocksOnBoard, tetrisBoardGraphics, holdPanel);
					}
					hasSwapped = false;
				}
				this.addKeyListener(currentShape);

				currentShape.isHeld = false;
				doesMovingPieceExist = true;
			}
			if(currentShape.isMoving){
				currentShape.moveBlocks();
				currentShape.checkIfMoving();

				updateImage();
				currentShape.setAndDrawGhost();
				currentShape.drawBlocks();
				currentShape.rotateLimiter++;
			}else{
				boolean gameOver = false;
				//Adds the blocks of the shape to the blocksOnBoard Array
				for(SingleBlock currentBlock : currentShape.blocks){
					try{
						blocksOnBoard[currentBlock.getCollum()/10][currentBlock.getRow()/10] = currentBlock;
					}catch(Exception e){
						System.out.println("hi");
						isRunning = false;
						gameOver = true;
					}
				}
				if(!gameOver){
					for(int row = 19; row >= 0; row--){
						int collumsFilled = 0;
						for(int collum = 0; collum < 10; collum++){
							if(blocksOnBoard[collum][row] != null){
								collumsFilled++;
							}
						}
						if(collumsFilled == 10){
							for(int mover = row; mover < 20; mover++){
								for(int collum = 0; collum < 10; collum++){
									//lowers block in the blockOnBoard array and their actual row value
									if(mover == 19){
										blocksOnBoard[collum][mover] = null;
									}else{
										//puts it on a lower row in the array
										blocksOnBoard[collum][mover] = blocksOnBoard[collum][mover+1];
										//resets the row for when it resets
										if(blocksOnBoard[collum][mover] != null){
											blocksOnBoard[collum][mover].setRow(mover*10);
										}
									}
								}
							}						
						}
					}
					tetrisBoardGraphics.setColor(Color.BLACK);
					tetrisBoardGraphics.fillRect(0, 0, 400, 800);

					//resets collumsHighestRow
					for(int key = 0; key < 10; key ++){
						collumsHighestRow.put(key, 0);
					}

					//redraws and reassigns the collumsHighestRow
					for(int row = 0; row < 20; row++){
						for(int collum = 0; collum < 10; collum++){
							if(blocksOnBoard[collum][row] != null){
								blocksOnBoard[collum][row].paintOntoFrame(tetrisBoardGraphics);
								if(blocksOnBoard[collum][row].getRow() >= collumsHighestRow.get(collum)){
									collumsHighestRow.put(collum, blocksOnBoard[collum][row].getRow()+10);
								}
							}
						}
					}
					staticTetrisBoardGraphics.drawImage(tetrisBoardImage,0,0,this);
					doesMovingPieceExist = false;
				}
			}

			timeDiff = lastUpdate - getTime();
			sleepTime = 1000/30 - timeDiff;

			if(sleepTime <= 0){
				sleepTime = 5;
			}
			try{
				Thread.sleep(sleepTime);
			}catch(InterruptedException e){}
			lastUpdate = getTime();

		}
		Graphics endGame = this.getGraphics();
		endGame.setColor(Color.RED);
		endGame.fillRect(0, 0, 400, 800);
		endGame.setColor(Color.BLACK);
		endGame.setFont(new Font("Times New Roman", Font.PLAIN , 30));
		endGame.drawString("Game Over Hope you had fun", 20, 400);
	}
	public void addNotify(){
		super.addNotify();
		this.setPreferredSize(new Dimension(400,800));
		tetrisFrame = new Thread(this);
		tetrisFrame.start();
	}

	public long getTime(){
		return System.nanoTime() / 1000000;
	}

	public void updateImage(){

		if(tetrisBoardImage == null){
			tetrisBoardImage = createImage(400, 800);
			staticTetrisBoardImage = createImage(400, 800);
			if(tetrisBoardImage == null){
				System.out.println("image is null");
			}else{
				tetrisBoardGraphics = tetrisBoardImage.getGraphics();
				staticTetrisBoardGraphics = staticTetrisBoardImage.getGraphics();

				staticTetrisBoardGraphics.setColor(Color.BLACK);
				staticTetrisBoardGraphics.fillRect(0, 0, 400, 800);
			}
		}
		tetrisBoardGraphics.drawImage(staticTetrisBoardImage, 0, 0, this);
	}

	public void paintTetris(){
		Graphics g = this.getGraphics();
		g.drawImage(tetrisBoardImage,0,0,this);
	}
}