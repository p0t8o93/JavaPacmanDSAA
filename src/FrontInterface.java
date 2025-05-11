
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer; 


public class FrontInterface extends JPanel {
   
    private App app;
    private PacmanGame Game;

    Image backgroundImage;
    Image blueGhost;
    Image orangeGhost;
    Image pinkGhost;
    Image redGhost;

    Image playButtonImg;
    Image playButtonClicked;
    Image helpButtonImg;
    Image helpButtonClicked;
    Image aboutButtonImg;
    Image quitButton;
    Image settingsButtonImg;
    Image trophyButtonImg;

    int gifWidth = 80;
    int gifHeight = 80;
    int gifStartX = 111;
    int gifY = 250;
    int gifSpacing = 120;

    int btnWidth = 270;
    int btnHeight = 120;
    int btnX = 190;
    int btnX2 = 205;
    int btnYStart = 360;
    int btnYStart2 = 320;
    int btnSpacing = 82;

    int newWidth = 200;
    int newX = btnX + 40 - (newWidth - 220) / 2;

    public FrontInterface(App app, PacmanGame Game) {
        this.app = app;
        this.Game = Game;
        setLayout(null);

        backgroundImage = new ImageIcon(getClass().getResource("./assets/ui_graphics/BG_Bldg9_2.png")).getImage();
        blueGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/blueGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        orangeGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/orangeGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        pinkGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/pinkGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        redGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/redGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);

        playButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/New_Play.png")).getImage().getScaledInstance(btnWidth, btnHeight, Image.SCALE_SMOOTH);
        helpButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/Help.png")).getImage().getScaledInstance(newWidth, 80, Image.SCALE_SMOOTH);
        aboutButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/About.png")).getImage().getScaledInstance(newWidth, 80, Image.SCALE_SMOOTH);
        settingsButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/Settings_Gear.gif")).getImage().getScaledInstance(gifWidth - 30, gifHeight - 30, Image.SCALE_DEFAULT);
        trophyButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/Trophy.gif")).getImage().getScaledInstance(gifWidth * 2 + 200, gifHeight * 2 + 40, Image.SCALE_DEFAULT);

        JLabel redGhost_lbl = new JLabel(new ImageIcon(redGhost));
        JLabel orangeGhost_lbl = new JLabel(new ImageIcon(orangeGhost));
        JLabel blueGhost_lbl = new JLabel(new ImageIcon(blueGhost));
        JLabel pinkGhost_lbl = new JLabel(new ImageIcon(pinkGhost));
        JLabel trophy_lbl = new JLabel(new ImageIcon(trophyButtonImg));
        JLabel settings_lbl = new JLabel(new ImageIcon(settingsButtonImg));

        redGhost_lbl.setBounds(gifStartX, gifY, gifWidth, gifHeight);
        orangeGhost_lbl.setBounds(gifStartX + gifSpacing, gifY, gifWidth, gifHeight);
        blueGhost_lbl.setBounds(gifStartX + gifSpacing * 2, gifY, gifWidth, gifHeight);
        pinkGhost_lbl.setBounds(gifStartX + gifSpacing * 3, gifY, gifWidth, gifHeight);
        trophy_lbl.setBounds(newX + 300, btnYStart + btnSpacing * 3, 50, 50);
        settings_lbl.setBounds(newX + 350, btnYStart + btnSpacing * 3, 50, 50);

        add(redGhost_lbl);
        add(orangeGhost_lbl);
        add(blueGhost_lbl);
        add(pinkGhost_lbl);
        add(trophy_lbl);
        add(settings_lbl);

        // BUTTONS
        JButton playButton = createHoverButton("./assets/ui_graphics/New_Play.png", "./assets/ui_graphics/Play_red.PNG", btnWidth, btnHeight);
        playButton.setBounds(btnX2, btnYStart2, btnWidth, btnHeight);
        playButton.addActionListener(new PlayGame());
        add(playButton);

        JButton helpButton = createHoverButton("./assets/ui_graphics/Help.png", "./assets/ui_graphics/Help_red.PNG", newWidth, 80);
        helpButton.setBounds(newX, btnYStart + btnSpacing, newWidth, 80);
        helpButton.addActionListener(new DisplayInstructions());
        add(helpButton);

        JButton aboutButton = createHoverButton("./assets/ui_graphics/About.png", "./assets/ui_graphics/About_red.PNG", newWidth, 80);
        aboutButton.setBounds(newX, btnYStart + btnSpacing * 2, newWidth, 80);
        aboutButton.addActionListener(new aboutUs());
        add(aboutButton);

        JButton quitButton = createHoverButton("./assets/ui_graphics/Quit_Yellow.png", "/assets/ui_graphics/Quit_Red.png", newWidth, 80);
        quitButton.setBounds(newX, btnYStart + btnSpacing * 3, newWidth, 80);
        quitButton.addActionListener(new QuitGame());
        add(quitButton);

        JButton settingsButton = new JButton();
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setBounds(newX + 350, btnYStart + btnSpacing * 3, 50, 50);
        settingsButton.addActionListener(new settings());
        add(settingsButton);

        JButton trophysButton = new JButton();
        trophysButton.setBorderPainted(false);
        trophysButton.setContentAreaFilled(false);
        trophysButton.setFocusPainted(false);
        trophysButton.setBounds(newX + 300, btnYStart + btnSpacing * 3, 50, 50);
        trophysButton.addActionListener(new leaderboard());
        add(trophysButton);
       

    }

    private class QuitGame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);  // This will terminate the application
            }
        }
    }

    private class settings implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            app.MainFrame.setSize(680, 747);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "settings");
        }
    }

    private class leaderboard implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            app.MainFrame.setSize(1080, 780);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "leaderboard");
        }
    }

    private class DisplayInstructions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Switch the JPanel of FrontInterface to Instructions
            app.MainFrame.setSize(960, 580);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "instructions");
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

private class PlayGame implements ActionListener {

    private Timer timer; // Timer for intermission music delay

    @Override
    public void actionPerformed(ActionEvent e) {
        
       
        Clip clip = app.settings.getClip(); 
        if (clip != null && clip.isRunning()) {
            app.settings.stopMusic(); // Stop the lobby music
        }

        // Switch to the gameplay frame immediately
        app.MainFrame.setSize(boardWidth + 16, boardHeight + 110); // Set the size of the gameplay window
        app.MainFrame.setLocationRelativeTo(null); // Center the window
        app.cardLayout.show(app.MainPanel, "pacmangame"); // Show the PacmanGame panel

        // Play the intermission music for 10 seconds
        if(app.settings.getMusic()!=false){
        app.settings.playintermissionMusic("./assets/game_sounds/intermission.wav");
        }
        // Use a Timer to delay the game start by 10 seconds
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // After 10 seconds, stop the intermission music and start the game
                app.settings.stopMusic(); // Stop the intermission music

                // Switch to the actual game state
                Game.setFocusable(true);
                Game.requestPanelFocus(); // Focus on the game
                Game.startGame(); // Start the game logic
            }
        });
        timer.setRepeats(false); // Make sure the timer runs only once
        timer.start();

        // Add a KeyListener to stop the timer, stop the music, and start the game immediately
        app.MainPanel.getComponent(2).requestFocusInWindow(); 
        app.MainPanel.getComponent(2).addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Stop the timer and the music
                if (timer != null && timer.isRunning()) {
                    timer.stop(); // Stop the intermission music timer
                }
                app.settings.stopMusic(); // Stop the intermission music

                // Start the game immediately
                Game.setFocusable(true);
                Game.requestPanelFocus(); // Focus on the game
                Game.startGame(); // Start the game logic
            }
        });
    }
}



    private class aboutUs implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Switch the JPanel of FrontInterface to Pacmangame
            app.MainFrame.setSize(1154, 667);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "aboutus");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private JButton createImageButton(String filePath, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getResource(filePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    private JButton createHoverButton(String defaultPath, String hoverPath, int width, int height) {
        JButton button = createImageButton(defaultPath, width, height);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(hoverPath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(defaultPath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            }
        });
        return button;
    }
 
}
