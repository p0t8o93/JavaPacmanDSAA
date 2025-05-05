
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverPanel extends JPanel {

    private Image backgroundGif;
    private App app; // Reference to the main menu
    private int score;
    int BGgifWidth = 980;
    int BGgifHeight = 780;
    int pacmoeHeight = 150;
    int pacmoeWidth = 150;
    int gifWidth = 120;
    int gifHeight = 120;
    int gifStartX = 111;
    int gifY = 150;
    int gifSpacing = 120;
    int foodY = gifY + 120;
    int foodStartX = gifStartX;
    int foodSpacing = 100;
    int foodHeight = 450;
    int foodWidth = 450;

    Image blueGhost, orangeGhost, pinkGhost, redGhost, pacMoe;
    Image icecream, burger, fries, peach;

    public GameOverPanel(App mainMenu) {
        this.app = mainMenu;
        setLayout(null); // Absolute positioning

        // Load background and characters
        backgroundGif = new ImageIcon(getClass().getResource("./assets/ui_graphics/GameOver.png")).getImage().getScaledInstance(BGgifWidth, BGgifHeight, Image.SCALE_SMOOTH);
        pacMoe = new ImageIcon(getClass().getResource("./assets/ui_graphics/pacmoeflip.gif")).getImage().getScaledInstance(pacmoeWidth, pacmoeHeight, Image.SCALE_DEFAULT);
        blueGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/blueGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        orangeGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/orangeGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        pinkGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/pinkGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        redGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/redGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        icecream = new ImageIcon(getClass().getResource("./assets/ui_graphics/icecream.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
        burger = new ImageIcon(getClass().getResource("./assets/ui_graphics/burger.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
        fries = new ImageIcon(getClass().getResource("./assets/ui_graphics/fries.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
//        peach = new ImageIcon(getClass().getResource("./assets/ui_graphics/peach.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);

        // Create labels
        JLabel pacMoe_lbl = new JLabel(new ImageIcon(pacMoe));
        JLabel redGhost_lbl = new JLabel(new ImageIcon(redGhost));
        JLabel orangeGhost_lbl = new JLabel(new ImageIcon(orangeGhost));
        JLabel blueGhost_lbl = new JLabel(new ImageIcon(blueGhost));
        JLabel pinkGhost_lbl = new JLabel(new ImageIcon(pinkGhost));
        JLabel icecream_lbl = new JLabel(new ImageIcon(icecream));
        JLabel burger_lbl = new JLabel(new ImageIcon(burger));
        JLabel fries_lbl = new JLabel(new ImageIcon(fries));
//        JLabel peach_lbl = new JLabel(new ImageIcon(peach));

        // Positioning for ghosts and Pac-Moe
        pacMoe_lbl.setBounds(gifStartX, gifY - 20, gifWidth * 2, gifHeight * 2);
        redGhost_lbl.setBounds(gifStartX + gifSpacing * 2, gifY + 20, gifWidth, gifHeight);
        orangeGhost_lbl.setBounds(gifStartX + gifSpacing * 3, gifY + 20, gifWidth, gifHeight);
        blueGhost_lbl.setBounds(gifStartX + gifSpacing * 4, gifY + 20, gifWidth, gifHeight);
        pinkGhost_lbl.setBounds(gifStartX + gifSpacing * 5, gifY + 20, gifWidth, gifHeight);

        // Positioning for food items
        icecream_lbl.setBounds(foodStartX, foodY, foodWidth, foodHeight);
        burger_lbl.setBounds(foodStartX + foodSpacing, foodY, foodWidth, foodHeight);
        fries_lbl.setBounds(foodStartX + foodSpacing * 2, foodY, foodWidth, foodHeight);
//        peach_lbl.setBounds(foodStartX + foodSpacing * 3, foodY, foodWidth, foodHeight);

        // Add all labels
        add(pacMoe_lbl);
        add(redGhost_lbl);
        add(orangeGhost_lbl);
        add(blueGhost_lbl);
        add(pinkGhost_lbl);
        add(icecream_lbl);
        add(burger_lbl);
        add(fries_lbl);
//        add(peach_lbl);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "enterPressed");
        getActionMap().put("enterPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.MainFrame.setSize(980, 780);
                app.MainFrame.setLocationRelativeTo(null);
                app.scorePanel.setScore(score); 
                app.cardLayout.show(app.MainPanel, "score");
            

            }
        });
    }
    public void setScore(int score) {
        this.score = score; // Store the score for later use
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundGif, 0, 0, this);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    }
}
