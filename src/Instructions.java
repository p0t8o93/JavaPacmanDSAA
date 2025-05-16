
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Instructions extends JPanel {

    private App app;
    private Image InstructionsImage;
    
    private Image backButtonImg;
    
    public Instructions(App app) {
        this.app = app;
        setLayout(null);
        
         InstructionsImage= new ImageIcon(getClass().getResource("/assets/ui_graphics/instructions.png")).getImage();
        backButtonImg = new ImageIcon(getClass().getResource("/assets/ui_graphics/Back.png")).getImage();
        JButton topRightLabel = createImageButton("/assets/ui_graphics/Back.png", backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setBounds(900, 15, backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topRightLabel.setOpaque(false);
        
        topRightLabel.addActionListener(new back());

        add(topRightLabel);
        app.MainFrame.setSize(100,100);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (InstructionsImage != null) {
            //Image scaledImage = InstructionsImage.getScaledInstance(960, 720, Image.SCALE_FAST);
            g.drawImage(InstructionsImage, 0, 0, this);
        }
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
    
    private JButton createImageButton(String filePath, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getResource(filePath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }
}
