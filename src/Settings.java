
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setting_lbl.setBounds(140,250,400,300);
        
        musicButton = new JButton(musicOnIcon);
        musicButton.setBounds(250, 450, musicOnIcon.getIconWidth(), musicOnIcon.getIconHeight());
        musicButton.setBorderPainted(false);
        musicButton.setContentAreaFilled(false);
        musicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicOn = !musicOn;
                musicButton.setIcon(musicOn ? musicOnIcon : musicOffIcon);
            }
        });
        add(musicButton);

        soundButton = new JButton(soundOnIcon);
        soundButton.setBounds(360, 450, soundOnIcon.getIconWidth(), soundOnIcon.getIconHeight());
        soundButton.setBorderPainted(false);
        soundButton.setContentAreaFilled(false);
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundOn = !soundOn;
                soundButton.setIcon(soundOn ? soundOnIcon : soundOffIcon);
            }
        });
        add(soundButton);
        add(setting_lbl);
        backButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/Back.png")).getImage();
        JButton topRightLabel = createImageButton("./assets/ui_graphics/Back.png", backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setBounds(600, 15, backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topRightLabel.setOpaque(false);

        topRightLabel.addActionListener(new Settings.back());
        add(topRightLabel);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgFrame, 0, 0, getWidth(), getHeight(), this);
    }

    private class back implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Switch the JPanel of FrontInterface to Pacmangame

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
}
