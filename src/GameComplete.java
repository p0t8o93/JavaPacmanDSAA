import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GameComplete  extends JPanel{
    
   private Image backgroundGif;
    private App app; // Reference to the main menu
    private int score;
    int BGgifWidth = 624;
    int BGgifHeight = 692;
    int pacmoeHeight = 90;
    int pacmoeWidth = 90;
    int gifWidth = 100;
    int gifHeight = 100;
    int gifStartX = 0;
    int gifY = 90;
    int gifSpacing = 150;
    int foodY = 450;
    int foodStartX = 90;
    int foodSpacing = 120;
    int foodHeight = 80;
    int foodWidth = 80;

    Image blueGhost, orangeGhost, pinkGhost, redGhost, pacMoe;
    Image icecream, burger, fries, peach, gameover_text,continue_text,lvl_completed_text;

    public GameComplete(App mainMenu) {
        this.app = mainMenu;
        setLayout(null); // Absolute positioning

        // Load background and characters
        backgroundGif = new ImageIcon(getClass().getResource("/assets/ui_graphics/GameOver.png")).getImage().getScaledInstance(BGgifWidth, BGgifHeight, Image.SCALE_SMOOTH);
        pacMoe = new ImageIcon(getClass().getResource("/assets/ui_graphics/pacmoeflip.gif")).getImage().getScaledInstance(pacmoeWidth, pacmoeHeight, Image.SCALE_DEFAULT);
        blueGhost = new ImageIcon(getClass().getResource("/assets/ui_graphics/blueghost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        orangeGhost = new ImageIcon(getClass().getResource("/assets/ui_graphics/orangeGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        pinkGhost = new ImageIcon(getClass().getResource("/assets/ui_graphics/pinkGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        redGhost = new ImageIcon(getClass().getResource("/assets/ui_graphics/redGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        icecream = new ImageIcon(getClass().getResource("/assets/ui_graphics/icecream.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
        burger = new ImageIcon(getClass().getResource("/assets/ui_graphics/burger.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
        fries = new ImageIcon(getClass().getResource("/assets/ui_graphics/fries.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
        peach = new ImageIcon(getClass().getResource("/assets/ui_graphics/peach.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
        gameover_text = new ImageIcon(getClass().getResource("/assets/ui_graphics/victory_textt.png")).getImage().getScaledInstance(570, 370, Image.SCALE_SMOOTH);
        continue_text = new ImageIcon(getClass().getResource("/assets/ui_graphics/continue_textt.png")).getImage().getScaledInstance(570, 370, Image.SCALE_SMOOTH);
         lvl_completed_text = new ImageIcon(getClass().getResource("/assets/ui_graphics/lvl_complete_txt.png")).getImage().getScaledInstance(570, 370, Image.SCALE_SMOOTH);


        // Create labels
        JLabel pacMoe_lbl = new JLabel(new ImageIcon(pacMoe));
        JLabel redGhost_lbl = new JLabel(new ImageIcon(redGhost));
        JLabel orangeGhost_lbl = new JLabel(new ImageIcon(orangeGhost));
        JLabel blueGhost_lbl = new JLabel(new ImageIcon(blueGhost));
        JLabel pinkGhost_lbl = new JLabel(new ImageIcon(pinkGhost));
        JLabel icecream_lbl = new JLabel(new ImageIcon(icecream));
        JLabel burger_lbl = new JLabel(new ImageIcon(burger));
        JLabel fries_lbl = new JLabel(new ImageIcon(fries));
        JLabel peach_lbl = new JLabel(new ImageIcon(peach));
        JLabel Gtext_lbl = new JLabel(new ImageIcon(gameover_text));
        JLabel Ctext_lbl = new JLabel(new ImageIcon(continue_text));
        JLabel lvltext_lbl = new JLabel(new ImageIcon(lvl_completed_text));
        

        // Positioning for ghosts and Pac-Moe
        pacMoe_lbl.setBounds(gifStartX, gifY - 20, gifWidth * 2, gifHeight * 2);
        redGhost_lbl.setBounds(gifStartX + gifSpacing+10 , gifY + 20, gifWidth, gifHeight);
        orangeGhost_lbl.setBounds(gifStartX + gifSpacing+110, gifY + 20, gifWidth, gifHeight);
        blueGhost_lbl.setBounds(gifStartX + gifSpacing +210, gifY + 20, gifWidth, gifHeight);
        pinkGhost_lbl.setBounds(gifStartX + gifSpacing +310, gifY + 20, gifWidth, gifHeight);

        // Positioning for food items
        icecream_lbl.setBounds(foodStartX, foodY, foodWidth, foodHeight);
        burger_lbl.setBounds(foodStartX + foodSpacing, foodY, foodWidth, foodHeight);
        fries_lbl.setBounds(foodStartX + foodSpacing * 2, foodY, foodWidth, foodHeight);
        peach_lbl.setBounds(foodStartX + foodSpacing * 3, foodY, foodWidth, foodHeight);
        
        Gtext_lbl.setBounds(10,215,600,100);
        Ctext_lbl.setBounds(10,380,600,50);
        lvltext_lbl.setBounds(10,280,600, 50);

        // Add all labels
        add(pacMoe_lbl);
        add(redGhost_lbl);
        add(orangeGhost_lbl);
        add(blueGhost_lbl);
        add(pinkGhost_lbl);
        add(icecream_lbl);
        add(burger_lbl);
        add(fries_lbl);
        add(peach_lbl);
        add(Gtext_lbl);
        add(Ctext_lbl);
        add(lvltext_lbl);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "enterPressed");
        getActionMap().put("enterPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.MainFrame.setSize(624, 692);
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
