import javax.swing.*;
import java.awt.*;

public class App {

    JFrame MainFrame;
    JPanel MainPanel;
    CardLayout cardLayout;
    GameOverPanel gameOverPanel;
    Score scorePanel;
    public Settings settings;

    public App() {
        MainFrame = new JFrame("Moeka the TIPIAN Ghost Hunter");
        cardLayout = new CardLayout();
        MainPanel = new JPanel();
        MainPanel.setLayout(cardLayout);

        // Initialize panels
        JPanel Instructions = new Instructions(this);
        JPanel PacmanGame = new PacmanGame(this);
        JPanel FrontInterface = new FrontInterface(this, (PacmanGame) PacmanGame);
        JPanel AboutUs = new AboutUsPanel(this);
        gameOverPanel = new GameOverPanel(this);
        scorePanel = new Score(this);
        JPanel Leaderboard = new Leaderboard(this);
        
        // Create Settings panel
        settings  = new Settings(this);

        // Add panels to card layout
        MainPanel.add(FrontInterface, "frontinterface");
        MainPanel.add(Instructions, "instructions");
        MainPanel.add(PacmanGame, "pacmangame");
        MainPanel.add(AboutUs, "aboutus");
        MainPanel.add(gameOverPanel, "gameover");
        MainPanel.add(scorePanel, "score");
        MainPanel.add(Leaderboard, "leaderboard");
        MainPanel.add(settings, "settings");

        // ✅Automatically play lobby music at game start
        settings.playLobbyMusic("assets/game_sounds/Loby music.wav"); // Ensure the path is correct

        // Frame setup
        MainFrame.getContentPane().add(MainPanel);
        MainFrame.setSize(680, 747);
        MainFrame.setLocationRelativeTo(null);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}
