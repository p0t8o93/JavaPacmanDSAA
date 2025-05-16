import javax.swing.*;
import java.awt.*;

public class App {

    JFrame MainFrame;
    JPanel MainPanel;
    CardLayout cardLayout;
    GameOverPanel gameOverPanel;
    Score scorePanel;
    PacmanGame currgame;
    public Settings settings;
    GameComplete gamecomplete;
    

    public App() {
        MainFrame = new JFrame("Moeka the TIPIAN Ghost Hunter");
        cardLayout = new CardLayout();
        MainPanel = new JPanel();
        MainPanel.setLayout(cardLayout);

        // Initialize the JPanel Classes here of the different Interfaces
        currgame = new PacmanGame(this);
        JPanel FrontInterface = new FrontInterface(this, (PacmanGame) currgame);
        JPanel AboutUs = new AboutUsPanel(this);
        gameOverPanel = new GameOverPanel(this); // Game Over Panel
        JPanel Leaderboard = new Leaderboard(this);
        settings  = new Settings(this);
         gamecomplete= new GameComplete(this);
        JPanel Instructions = new Instructions(this);
        
        scorePanel = new Score(this,currgame); // Score Panel
        
        
        // Adding the JPanels to the main panel for switching different
        // interfaces. (Name)           (keyword)
        MainPanel.add(FrontInterface, "frontinterface");
        MainPanel.add(Instructions, "instructions");
        MainPanel.add(currgame, "pacmangame");
        MainPanel.add(AboutUs, "aboutus");
        MainPanel.add(gameOverPanel, "gameover");  
        MainPanel.add(scorePanel, "score");
        MainPanel.add(Leaderboard,"leaderboard");
        MainPanel.add(settings, "settings");
        MainPanel.add(gamecomplete, "gameComplete");
        
        settings.playLobbyMusic("assets/game_sounds/Loby music.wav");
          settings.loadpelletSFX("assets/game_sounds/eat_pellet.wav");
          settings.loadPpelletSFX("assets/game_sounds/eat_Ppellet.wav");
          settings.loadghostSFX("assets/game_sounds/eat_ghost.wav");
          settings.loadbuttonsnd("assets/game_sounds/button.wav");
          settings.loadVulnerableSFX("assets/game_sounds/vulnerable.wav");
          settings.loadBackSFX("assets/game_sounds/goback.wav");
          

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
