
import javax.swing.ImageIcon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author potato
 */


public class GhostPowers {
    Block blueGhost;
    Block redGhost;
    Block pinkGhost;
    Block orangeGhost;
    
    public GhostPowers(PacmanGame pacman){
        for (Block Ghost: pacman.ghosts){
            if (Ghost.texture == new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/blueGhost.gif")).getImage()){
                this.blueGhost = Ghost;
            }
        }
    }
}

