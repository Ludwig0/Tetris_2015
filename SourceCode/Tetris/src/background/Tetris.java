
package background;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

/**
 *
 * @author Brett
 */
public class Tetris {

    /**
     * @param args the command line arguments
     */
    
    private static final SpringLayout layout = new SpringLayout();
    public static JFrame tetris;
    
    public static void main(String[] args) {
        tetris = new JFrame("Tetris");
        tetris.setSize(600,852);
        tetris.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tetris.setResizable(false);
        tetris.setLayout(layout);
        
                   
		HoldPanel holdPanel = new HoldPanel();
		tetris.add(holdPanel);
		layout.putConstraint(SpringLayout.NORTH, holdPanel, 20 , SpringLayout.NORTH, tetris);
        layout.putConstraint(SpringLayout.WEST, holdPanel, 20, SpringLayout.WEST, tetris);
        
        TetrisBoard tetrisBoard = new TetrisBoard(holdPanel);
        tetris.add(tetrisBoard, layout);
        layout.putConstraint(SpringLayout.NORTH, tetrisBoard, 23 , SpringLayout.NORTH, tetris);
        layout.putConstraint(SpringLayout.WEST, tetrisBoard, 100, SpringLayout.WEST, tetris);
        
        tetris.setVisible(true);
        
        
        try {
            Clip clip = AudioSystem.getClip();
            String musicLocation = System.getProperty("user.dir") + "\\res\\music.wav";
        	File audio = new File(musicLocation);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(audio);
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
          } catch (Exception e) {
            e.printStackTrace();
          }
    }  
}