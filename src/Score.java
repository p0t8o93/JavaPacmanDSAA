
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class Score extends JPanel {

    private String playerName;
    private int score;
    private JTextField nameInputField;
    private JLabel scoreLabel;
    private Connection connection;

    private void setupDatabase() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveScoreToDatabase(String playerName, int score) {
        try {
            String query = "INSERT INTO leaderboard (name, score) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setScore(int score) {
        this.score = score;
        scoreLabel.setText("" + score);  // Update the score display

    }

    private Image backgroundGif;
    private App app; // Reference to the main menu
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
    Image icecream, burger, fries, peach, continue_text, inputname, bar, scorec;
    private Font PixelFont;

    public Score(App mainMenu) {
        this.app = mainMenu;
        setLayout(null); // Absolute positioning

        try {
            InputStream fontStream = getClass().getResourceAsStream("./assets/game_font/PixelGame.otf");
            if (fontStream != null) {
                PixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(48f);
                fontStream.close();
            } else {
                System.err.println("Font file not found!");
                PixelFont = new Font("SansSerif", Font.PLAIN, 12);
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            PixelFont = new Font("SansSerif", Font.PLAIN, 12);
        }

        setupDatabase();

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
        peach = new ImageIcon(getClass().getResource("./assets/ui_graphics/peach.gif")).getImage().getScaledInstance(foodWidth, foodHeight, Image.SCALE_DEFAULT);
        continue_text = new ImageIcon(getClass().getResource("./assets/ui_graphics/continue_textt.png")).getImage().getScaledInstance(570, 370, Image.SCALE_SMOOTH);
        inputname = new ImageIcon(getClass().getResource("./assets/ui_graphics/inputname.png")).getImage().getScaledInstance(455, 165, Image.SCALE_SMOOTH);
        bar = new ImageIcon(getClass().getResource("./assets/ui_graphics/bar.png")).getImage().getScaledInstance(365, 105, Image.SCALE_SMOOTH);
        scorec = new ImageIcon(getClass().getResource("./assets/ui_graphics/score.png")).getImage().getScaledInstance(140, 50, Image.SCALE_SMOOTH);

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
        JLabel Ctext_lbl = new JLabel(new ImageIcon(continue_text));
        JLabel input_lbl = new JLabel(new ImageIcon(inputname));
        JLabel bar_lbl = new JLabel(new ImageIcon(bar));
        JLabel scorec_lbl = new JLabel(new ImageIcon(scorec));

        // Positioning for ghosts and Pac-Moe
        pacMoe_lbl.setBounds(gifStartX, gifY - 20, gifWidth * 2, gifHeight * 2);
        redGhost_lbl.setBounds(gifStartX + gifSpacing + 10, gifY + 20, gifWidth, gifHeight);
        orangeGhost_lbl.setBounds(gifStartX + gifSpacing + 110, gifY + 20, gifWidth, gifHeight);
        blueGhost_lbl.setBounds(gifStartX + gifSpacing + 210, gifY + 20, gifWidth, gifHeight);
        pinkGhost_lbl.setBounds(gifStartX + gifSpacing + 310, gifY + 20, gifWidth, gifHeight);

        // Positioning for food items
        icecream_lbl.setBounds(foodStartX, foodY, foodWidth, foodHeight);
        burger_lbl.setBounds(foodStartX + foodSpacing, foodY, foodWidth, foodHeight);
        fries_lbl.setBounds(foodStartX + foodSpacing * 2, foodY, foodWidth, foodHeight);
        peach_lbl.setBounds(foodStartX + foodSpacing * 3, foodY, foodWidth, foodHeight);
        Ctext_lbl.setBounds(10, 380, 600, 50);
        input_lbl.setBounds(10, 210, 600, 50);
        bar_lbl.setBounds(160, 310, 300, 50);
        scorec_lbl.setBounds(150, 530, 200, 50);
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
        add(Ctext_lbl);
        add(input_lbl);
        add(bar_lbl);
        add(scorec_lbl);

        scoreLabel = new JLabel();
        scoreLabel.setFont(PixelFont);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(300, 530, 300, 60); // Position it on the panel

        nameInputField = new JTextField();
        nameInputField.setFont(PixelFont);
        nameInputField.setOpaque(false);                // Make background non-opaque
        nameInputField.setBackground(new Color(0, 0, 0, 0)); // Fully transparent
        nameInputField.setBorder(null);
        nameInputField.setForeground(Color.WHITE);  // White font
        nameInputField.setCaretColor(Color.WHITE);
        nameInputField.setBounds(150, 295, 300, 50);  // Adjust position as needed
        nameInputField.setHorizontalAlignment(JTextField.CENTER);

        nameInputField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (str != null && getLength() < 5) {
                    super.insertString(offset, str, attr);  // Limit input to 5 characters
                }
            }
        });

        // Add key listener to handle Enter key press and save the score
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "enterPressed");
        getActionMap().put("enterPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                playerName = nameInputField.getText().trim();  // Get the player's name
                if (playerName.length() > 0) {  // Ensure name is not empty
                    saveScoreToDatabase(playerName, score);  // Save score to database
                    nameInputField.setEditable(false);  // Disable further editing after Enter is pressed
                } else {
                    JOptionPane.showMessageDialog(app.MainFrame, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                app.MainFrame.setSize(680, 747);
                app.MainFrame.setLocationRelativeTo(null);
                app.cardLayout.show(app.MainPanel, "frontinterface");
                app.settings.stopGameOver();
                app.settings.playLobbyMusic("assets/game_sounds/Loby music.wav");
            }
        });

        add(scoreLabel);
        add(nameInputField);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    nameInputField.requestFocusInWindow();
                });
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundGif, 0, 0, this);

    }
}