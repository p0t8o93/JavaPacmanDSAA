
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AboutUsPanel extends JPanel {

    private Image backgroundGif;
    private Image backButtonImg;
    private App app; // Reference to the main menu

    public AboutUsPanel(App mainMenu) {
        this.app = mainMenu;
        setPreferredSize(new Dimension(1154, 667));
        setLayout(null); // Use absolute layout for layering

        // Load and add background GIF
        backgroundGif = new ImageIcon(getClass().getResource("./assets/ui_graphics/aboutus.gif")).getImage();

        // Load and add top-right image (arrow)
        backButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/Back.png")).getImage();
        JButton topRightLabel = createImageButton("./assets/ui_graphics/Back.png", backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setBounds(1092, 15, backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topRightLabel.setOpaque(false);
        
        topRightLabel.addActionListener(new back());

        add(topRightLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
            //Image scaledImage = InstructionsImage.getScaledInstance(960, 720, Image.SCALE_FAST);
            g.drawImage(backgroundGif, 0, 0, this);
    }
    
    private JButton createImageButton(String filePath, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getResource(filePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }
    
    private class back implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Switch the JPanel of FrontInterface to Pacmangame

            app.MainFrame.setSize(680,747);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "frontinterface");
        }
    }
}
