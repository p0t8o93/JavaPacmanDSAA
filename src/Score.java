
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
    int BGgifWidth = 980;
    int BGgifHeight = 780;
    int pacmoeHeight = 150;
    int pacmoeWidth = 150;
    int gifWidth = 120;
    int gifHeight = 120;
    int gifStartX = 111;
    int gifY = 150;
    int gifSpacing = 120;
    int foodY = gifY + 240;
    int foodStartX = gifStartX;
    int foodSpacing = 100;
    int foodHeight = 450;
    int foodWidth = 450;

    Image blueGhost, orangeGhost, pinkGhost, redGhost, pacMoe;
    Image icecream, burger, fries, peach;
    private Font PixelFont;

    public Score(App mainMenu) {
        this.app = mainMenu;
        setLayout(null); // Absolute positioning

        try {
            InputStream fontStream = getClass().getResourceAsStream("./assets/game_font/PixelGame.otf");
            if (fontStream != null) {
                PixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(64f);
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
        backgroundGif = new ImageIcon(getClass().getResource("./assets/ui_graphics/score.gif")).getImage().getScaledInstance(BGgifWidth, BGgifHeight, Image.SCALE_SMOOTH);
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

        scoreLabel = new JLabel();
        scoreLabel.setFont(PixelFont);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(430, 690, 300, 60); // Position it on the panel

        nameInputField = new JTextField();
        nameInputField.setFont(PixelFont);
        nameInputField.setOpaque(false);                // Make background non-opaque
        nameInputField.setBackground(new Color(0, 0, 0, 0)); // Fully transparent
        nameInputField.setBorder(null);
        nameInputField.setForeground(Color.WHITE);  // White font
        nameInputField.setCaretColor(Color.WHITE);
        nameInputField.setBounds(340, 375, 300, 50);  // Adjust position as needed
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
