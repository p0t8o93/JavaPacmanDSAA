
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Settings extends JPanel {

    private App app;
    private Image bgFrame;
    private JButton musicButton;
    private JButton soundButton;
    private boolean musicOn = true;
    private boolean soundOn = true;
    private Image backButtonImg;
    private ImageIcon musicOnIcon;
    private ImageIcon musicOffIcon;
    private ImageIcon soundOnIcon;
    private ImageIcon soundOffIcon;
    private Image settingpanel;
    public Clip clip;

    public Settings(App mainMenu) {
        this.app = mainMenu;
        setLayout(null); // Absolute positioning
        bgFrame = new ImageIcon(getClass().getResource("./assets/ui_graphics/settingsBG.gif")).getImage().getScaledInstance(680, 747, Image.SCALE_DEFAULT);

        // Load and scale the icons
        musicOnIcon = new ImageIcon(getClass().getResource("./assets/ui_graphics/BTN ON_A.png"));
        musicOffIcon = new ImageIcon(getClass().getResource("./assets/ui_graphics/BTN OFF_A.png"));
        soundOnIcon = new ImageIcon(getClass().getResource("./assets/ui_graphics/BTN ON_B.png"));
        soundOffIcon = new ImageIcon(getClass().getResource("./assets/ui_graphics/BTN OFF_B.png"));
        settingpanel = new ImageIcon(getClass().getResource("./assets/ui_graphics/Settings_FRAME RIL.png")).getImage().getScaledInstance(400, 290, Image.SCALE_DEFAULT);

        // Resize all icons
        musicOnIcon = new ImageIcon(musicOnIcon.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH));
        musicOffIcon = new ImageIcon(musicOffIcon.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH));
        soundOnIcon = new ImageIcon(soundOnIcon.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH));
        soundOffIcon = new ImageIcon(soundOffIcon.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH));

        JLabel setting_lbl = new JLabel(new ImageIcon(settingpanel));
        setting_lbl.setBounds(140, 250, 400, 300);

        musicButton = new JButton(musicOnIcon);
        musicButton.setBounds(250, 450, musicOnIcon.getIconWidth(), musicOnIcon.getIconHeight());
        musicButton.setBorderPainted(false);
        musicButton.setContentAreaFilled(false);
        musicButton.setFocusPainted(false);
        musicButton.addActionListener(e -> {
            buttonSFX();
            musicOn = !musicOn;
            musicButton.setIcon(musicOn ? musicOnIcon : musicOffIcon);
            if (musicOn) {

                playLobbyMusic("assets/game_sounds/Loby music.wav");
            } else {
                stopMusic();

            }
        });
        add(musicButton);

        soundButton = new JButton(soundOnIcon);
        soundButton.setBounds(360, 450, soundOnIcon.getIconWidth(), soundOnIcon.getIconHeight());
        soundButton.setBorderPainted(false);
        soundButton.setContentAreaFilled(false);
        soundButton.setFocusPainted(false);
        soundButton.addActionListener(e -> {
            buttonSFX();
            soundOn = !soundOn;
            soundButton.setIcon(soundOn ? soundOnIcon : soundOffIcon);
        });
        add(soundButton);

        add(setting_lbl);

        backButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/Back.png")).getImage();
        JButton topRightLabel = createImageButton("./assets/ui_graphics/Back.png", backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setBounds(600, 15, backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        topRightLabel.setOpaque(false);
        topRightLabel.addActionListener(new BackAction());
        add(topRightLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgFrame, 0, 0, getWidth(), getHeight(), this);
    }

    private class BackAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            app.MainFrame.setSize(680, 747);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "frontinterface");
        }
    }

    private JButton createImageButton(String filePath, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getResource(filePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    public void playLobbyMusic(String relativePath) {
        try {
            if (clip != null && clip.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playintermissionMusic(String relativePath) {
        try {
            if (clip != null && clip.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public Clip getClip() {
        return clip;
    }

    public boolean getMusic() {
        return musicOn;
    }

    public boolean getSound() {
        return soundOn;
    }

    private Clip pelletClip;

    public void loadpelletSFX(String relativePath) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            pelletClip = AudioSystem.getClip();
            pelletClip.open(audioInput);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playPelletSFX() {
        if (soundOn == false) {
            return;
        }
        if (pelletClip == null) {
            return;
        }

        if (pelletClip.isRunning()) {
            pelletClip.stop();

            loadpelletSFX("assets/game_sounds/eat_pellet.wav");

        }
        pelletClip.setFramePosition(1);
        pelletClip.start();
    }
    private Clip PpelletClip;

    public void loadPpelletSFX(String relativePath) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            PpelletClip = AudioSystem.getClip();
            PpelletClip.open(audioInput);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playPpelletSFX() {
        if (soundOn == false) {
            return;
        }
        if (PpelletClip == null) {
            return;
        }

        if (PpelletClip.isRunning()) {
            PpelletClip.stop();

            loadPpelletSFX("assets/game_sounds/eat_Ppellet.wav");

        }
        PpelletClip.setFramePosition(1);
        PpelletClip.start();
    }

    private Clip ghostVulnerable;

    public void loadVulnerableSFX(String relativePath) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            ghostVulnerable = AudioSystem.getClip();
            ghostVulnerable.open(audioInput);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playvulnerableSFX() {
        if (soundOn == false) {
            return;
        }
        if (ghostVulnerable == null) {
            return;
        }

        if (ghostVulnerable.isRunning()) {
            ghostVulnerable.stop();

            loadVulnerableSFX("assets/game_sounds/vulnerable.wav");

        }
        ghostVulnerable.setFramePosition(1);
        ghostVulnerable.start();
    }

    private Clip ghostClip;

    public void loadghostSFX(String relativePath) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            ghostClip = AudioSystem.getClip();
            ghostClip.open(audioInput);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playghostSFX() {
        if (soundOn == false) {
            return;
        }
        if (ghostClip == null) {
            return;
        }

        if (ghostClip.isRunning()) {
            ghostClip.stop();

            loadghostSFX("assets/game_sounds/eat_ghost.wav");

        }
        ghostClip.setFramePosition(1);
        ghostClip.start();
    }

    private Clip gobackClip;

    public void loadBackSFX(String relativePath) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            gobackClip = AudioSystem.getClip();
            gobackClip.open(audioInput);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playGoBackSFX() {
        if (soundOn == false) {
            return;
        }
        if (gobackClip == null) {
            return;
        }

        if (gobackClip.isRunning()) {
            gobackClip.stop();

            loadBackSFX("assets/game_sounds/goback.wav");

        }
        gobackClip.setFramePosition(1);
        gobackClip.start();
    }
    private Clip pacmanDeath;

    public void pacmandeath(String relativePath) {
        if (soundOn == false) {
            return;
        }
        try {
            if (pacmanDeath != null && pacmanDeath.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            pacmanDeath = AudioSystem.getClip();
            pacmanDeath.open(audioInput);
            pacmanDeath.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private Clip buttonsnd;

    public void loadbuttonsnd(String relativePath) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            buttonsnd = AudioSystem.getClip();
            buttonsnd.open(audioInput);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void buttonSFX() {
        if (soundOn == false) {
            return;
        }
        if (buttonsnd == null) {
            return;
        }

        if (buttonsnd.isRunning()) {
            buttonsnd.stop();

            loadghostSFX("assets/game_sounds/eat_ghost.wav");

        }
        buttonsnd.setFramePosition(1);
        buttonsnd.start();
    }
    private Clip GameOverSFX;

    public void playGameOver(String relativePath) {
        try {
            if (soundOn == false) {
                return;
            }
            if (GameOverSFX != null && GameOverSFX.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            GameOverSFX = AudioSystem.getClip();
            GameOverSFX.open(audioInput);
            GameOverSFX.loop(Clip.LOOP_CONTINUOUSLY);
            GameOverSFX.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopGameOver() {
        if (GameOverSFX != null && GameOverSFX.isRunning()) {
            GameOverSFX.stop();
            GameOverSFX.close();
        }
    }
    private Clip redP;

    public void redP(String relativePath) {
        try {
            if (soundOn == false) {
                return;
            }
            if (redP != null && redP.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            redP = AudioSystem.getClip();
            redP.open(audioInput);
            redP.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private Clip blueP;

    public void blueP(String relativePath) {
        try {
            if (soundOn == false) {
                return;
            }
            if (blueP != null && blueP.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            blueP = AudioSystem.getClip();
            blueP.open(audioInput);
            blueP.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private Clip pinkP;

    public void pinkP(String relativePath) {
        try {
            if (soundOn == false) {
                return;
            }
            if (pinkP != null && pinkP.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            pinkP = AudioSystem.getClip();
            pinkP.open(audioInput);
            pinkP.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
