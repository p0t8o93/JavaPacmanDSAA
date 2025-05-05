/*
This class is the main runner class of the whole game. This is where every infterace
will be shown and managed.

 */

import javax.swing.*;
import java.awt.*;

public class App {

    JFrame MainFrame;
    JPanel MainPanel;
    CardLayout cardLayout;
    GameOverPanel gameOverPanel;
    Score scorePanel;
    

    public App() {
        MainFrame = new JFrame("Moeka the TIPIAN Ghost Hunter");
        cardLayout = new CardLayout();
        MainPanel = new JPanel();
        MainPanel.setLayout(cardLayout);

        // Initialize the JPanel Classes here of the different Interfaces
        JPanel Instructions = new Instructions(this);
        JPanel PacmanGame = new PacmanGame(this);
        JPanel FrontInterface = new FrontInterface(this, (PacmanGame) PacmanGame);
        JPanel AboutUs = new AboutUsPanel(this);
        JPanel GameOver = new GameOverPanel(this);
        gameOverPanel = new GameOverPanel(this); // Game Over Panel
        scorePanel = new Score(this); // Score Panel
        JPanel Leaderboard = new Leaderboard(this);
        
        // Adding the JPanels to the main panel for switching different
        // interfaces. (Name)           (keyword)
        MainPanel.add(FrontInterface, "frontinterface");
        MainPanel.add(Instructions, "instructions");
        MainPanel.add(PacmanGame, "pacmangame");
        MainPanel.add(AboutUs, "aboutus");
        MainPanel.add(gameOverPanel, "gameover");  
        MainPanel.add(scorePanel, "score");
        MainPanel.add(Leaderboard,"leaderboard");

        MainFrame.getContentPane().add(MainPanel);
        //MainFrame.setSize(960,540);
        MainFrame.setSize(680, 747);
        MainFrame.setLocationRelativeTo(null);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        App app = new App();
    }
}
