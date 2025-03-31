import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FRONTInterfacePanel extends JFrame {
    private Image backgroundImage;

    public FRONTInterfacePanel() {
        setTitle("Moeka The Tipian Ghost Hunter");
        setSize(680, 747);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Load Background Image
        backgroundImage = new ImageIcon("./assets/ui_graphics/BG_Bldg9_2.png").getImage();
        
        // Create Panel with Background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);
        setContentPane(panel);
        
        // GIF Settings
        int gifWidth = 80;
        int gifHeight = 80;
        int gifStartX = 111;
        int gifY = 250;
        int gifSpacing = 120;

        JLabel gif1 = new JLabel(new ImageIcon(new ImageIcon("./assets/ui_graphics/GAB_GHOST_FINALLASSSTN_A.gif").getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT)));
        gif1.setBounds(gifStartX, gifY, gifWidth, gifHeight);
        
        JLabel gif2 = new JLabel(new ImageIcon(new ImageIcon("./assets/ui_graphics/Reuven_FINAL_(1).gif").getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT)));
        gif2.setBounds(gifStartX + gifSpacing, gifY, gifWidth, gifHeight);
        
        JLabel gif3 = new JLabel(new ImageIcon(new ImageIcon("./assets/ui_graphics/GARRY_final_LAST_NA_(1).gif").getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT)));
        gif3.setBounds(gifStartX + gifSpacing * 2, gifY, gifWidth, gifHeight);
        
        JLabel gif4 = new JLabel(new ImageIcon(new ImageIcon("./assets/ui_graphics/EITHAN_final_LAST_na.gif").getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT)));
        gif4.setBounds(gifStartX + gifSpacing * 3, gifY, gifWidth, gifHeight);
        
        panel.add(gif1);
        panel.add(gif2);
        panel.add(gif3);
        panel.add(gif4);
        
        // Buttons Setup
        int btnWidth = 270;
        int btnHeight = 120;
        int btnX = 190;
        int btnX2 = 205;
        int btnYStart = 360;
        int btnYStart2 = 320;
        int btnSpacing = 82;

        int newWidth = 200;
        int newX = btnX + 40 - (newWidth - 220) / 2;
        
        JButton playButton = createImageButton("./assets/ui_graphics/New_Play.png", btnWidth, btnHeight);
        playButton.setBounds(btnX2, btnYStart2, btnWidth, btnHeight);
        playButton.addActionListener(e -> System.out.println("Play_Button_Clicked"));
        
        JButton helpButton = createImageButton("./assets/ui_graphics/Help.png", newWidth, 80);
        helpButton.setBounds(newX, btnYStart + btnSpacing, newWidth, 80);
        helpButton.addActionListener(e -> System.out.println("Help_Button_Clicked"));
        
        JButton aboutButton = createImageButton("./assets/ui_graphics/About.png", newWidth, 80);
        aboutButton.setBounds(newX, btnYStart + btnSpacing * 2, newWidth, 80);
        aboutButton.addActionListener(e -> {
    setVisible(false); // Hide Main Menu
    //AboutUsFrame aboutFrame = new AboutUsFrame(this);
    //aboutFrame.setVisible(true);
});



        
        JButton quitButton = createHoverButton(
            "./assets/ui_graphics/Quit Yellow.png", 
            "./assets/ui_graphics/Quit Red.png", 
            newWidth, 80);
        quitButton.setBounds(newX, btnYStart + btnSpacing * 3, newWidth, 80);
        quitButton.addActionListener(e -> System.exit(0));
        
        panel.add(playButton);
        panel.add(helpButton);
        panel.add(aboutButton);
        panel.add(quitButton);
        
        // Bottom Right GIF Icons
        JLabel settingsGif = new JLabel(new ImageIcon(new ImageIcon("./assets/ui_graphics/Settings Gear.gif").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        settingsGif.setBounds(getWidth() - 130, getHeight() - 90, 50, 50);
        settingsGif.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Settings Icon Clicked");
            }
        });
        
        JLabel trophyGif = new JLabel(new ImageIcon(new ImageIcon("./assets/ui_graphics/Trophy.gif").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        trophyGif.setBounds(getWidth() - 70, getHeight() - 90, 50, 50);
        trophyGif.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Trophy Icon Clicked");
            }
        });
        
        panel.add(settingsGif);
        panel.add(trophyGif);
    }
    
    private JButton createImageButton(String filePath, int width, int height) {
        ImageIcon icon = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        JButton button = new JButton(icon);
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
                button.setIcon(new ImageIcon(new ImageIcon(hoverPath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(new ImageIcon(new ImageIcon(defaultPath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            }
        });
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FRONTInterfacePanel().setVisible(true));
    }
}