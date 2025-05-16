
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

class QuickSort {

    public static void quickSort(TopScore[] scores, int low, int high) {
        if (low < high) {
            int pi = partition(scores, low, high);

            // Recursively sort elements before and after partition
            quickSort(scores, low, pi - 1);
            quickSort(scores, pi + 1, high);
        }
    }

    private static int partition(TopScore[] scores, int low, int high) {
        // Choose the last element as pivot
        TopScore pivot = scores[high];
        int i = low - 1; // Index of smaller element

        for (int j = low; j < high; j++) {
            // If the current element is greater than the pivot (for descending order)
            if (scores[j].getScore() > pivot.getScore()) {
                i++;

                // Swap the elements
                TopScore temp = scores[i];
                scores[i] = scores[j];
                scores[j] = temp;
            }
        }

        // Swap the pivot element with the element at i+1
        TopScore temp = scores[i + 1];
        scores[i + 1] = scores[high];
        scores[high] = temp;

        return i + 1;
    }
}

public class Leaderboard extends JPanel {

    private Image backgroundGif;
    private Image backButtonImg;
    private App app; // Reference to the main menu
    private JTextArea leaderboardTextArea;
    private Connection connection;

    public void setLeaderboard() {
        ArrayList<TopScore> topScores = getTopScores(6);
        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (TopScore score : topScores) {
            sb.append(rank).append(".      ").append(score.getName()).append("               ").append(score.getScore()).append("\n");
            rank++;
        }
        leaderboardTextArea.setText(sb.toString());
    }

    private void setupDatabase() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ;
    private Font PixelFont;

    public Leaderboard(App mainMenu) {
        this.app = mainMenu;
        setPreferredSize(new Dimension(1154, 667));
        setLayout(null); // Use absolute layout for layering

        try {
            InputStream fontStream = getClass().getResourceAsStream("./assets/game_font/PixelGame.otf");
            if (fontStream != null) {
                PixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(50f);
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

        int pacmoeHeight = 60;
        int pacmoeWidth = 60;
        int gifWidth = 65;
        int gifHeight = 65;
        int gifStartX = 300;
        int gifY = 30;
        int gifSpacing = 70;
        Image blueGhost, orangeGhost, pinkGhost, redGhost, pacMoe, sparkle, gold, silver, bronze;

        // Load and add background GIF
        backgroundGif = new ImageIcon(getClass().getResource("./assets/ui_graphics/LeaderboardPanel.png")).getImage().getScaledInstance(1080, 780, Image.SCALE_SMOOTH);
        pacMoe = new ImageIcon(getClass().getResource("./assets/ui_graphics/pacmoe.gif")).getImage().getScaledInstance(pacmoeWidth, pacmoeHeight, Image.SCALE_DEFAULT);
        blueGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/blueGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        orangeGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/orangeGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        pinkGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/pinkGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        redGhost = new ImageIcon(getClass().getResource("./assets/ui_graphics/redGhost.gif")).getImage().getScaledInstance(gifWidth, gifHeight, Image.SCALE_DEFAULT);
        sparkle = new ImageIcon(getClass().getResource("./assets/ui_graphics/sparkle.gif")).getImage().getScaledInstance(600, 500, Image.SCALE_DEFAULT);
        gold = new ImageIcon(getClass().getResource("./assets/ui_graphics/gold.gif")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        silver = new ImageIcon(getClass().getResource("./assets/ui_graphics/silver.gif")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        bronze = new ImageIcon(getClass().getResource("./assets/ui_graphics/bronze.gif")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);

        JLabel pacMoe_lbl = new JLabel(new ImageIcon(pacMoe));
        JLabel redGhost_lbl = new JLabel(new ImageIcon(redGhost));
        JLabel orangeGhost_lbl = new JLabel(new ImageIcon(orangeGhost));
        JLabel blueGhost_lbl = new JLabel(new ImageIcon(blueGhost));
        JLabel pinkGhost_lbl = new JLabel(new ImageIcon(pinkGhost));
        JLabel sparkle_lbl = new JLabel(new ImageIcon(sparkle));
        JLabel gold_lbl = new JLabel(new ImageIcon(gold));
        JLabel silver_lbl = new JLabel(new ImageIcon(silver));
        JLabel bronze_lbl = new JLabel(new ImageIcon(bronze));

        pacMoe_lbl.setBounds(gifStartX + 35, gifY - 20, gifWidth * 2, gifHeight * 2);
        redGhost_lbl.setBounds(gifStartX + gifSpacing * 2, gifY + 20, gifWidth, gifHeight);
        orangeGhost_lbl.setBounds(gifStartX + gifSpacing * 3, gifY + 20, gifWidth, gifHeight);
        blueGhost_lbl.setBounds(gifStartX + gifSpacing * 4, gifY + 20, gifWidth, gifHeight);
        pinkGhost_lbl.setBounds(gifStartX + gifSpacing * 5, gifY + 20, gifWidth, gifHeight);
        sparkle_lbl.setBounds(gifStartX - 30, gifY + 100, gifWidth, gifHeight);
        gold_lbl.setBounds(gifStartX + 200, gifY + 280, gifWidth, gifHeight);
        silver_lbl.setBounds(gifStartX + 200, gifY + 340, gifWidth, gifHeight);
        bronze_lbl.setBounds(gifStartX + 200, gifY + 400, gifWidth, gifHeight);

        add(pacMoe_lbl);
        add(redGhost_lbl);
        add(orangeGhost_lbl);
        add(blueGhost_lbl);
        add(pinkGhost_lbl);
        add(sparkle_lbl);
        add(gold_lbl);
        add(silver_lbl);
        add(bronze_lbl);

        // Load and add top-right image (arrow)
        backButtonImg = new ImageIcon(getClass().getResource("./assets/ui_graphics/Back.png")).getImage();
        JButton topRightLabel = createImageButton("./assets/ui_graphics/Back.png", backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setBounds(980, 15, backButtonImg.getWidth(this), backButtonImg.getHeight(this));
        topRightLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topRightLabel.setOpaque(false);

        topRightLabel.addActionListener(new back());

        add(topRightLabel);

        leaderboardTextArea = new JTextArea();
        leaderboardTextArea.setFont(PixelFont);
        leaderboardTextArea.setForeground(Color.WHITE);
        leaderboardTextArea.setOpaque(false);
        leaderboardTextArea.setEditable(false);
        leaderboardTextArea.setFocusable(false);
        leaderboardTextArea.setBounds(300, 320, 800, 500); // Adjust as needed
        setLeaderboard();
        add(leaderboardTextArea);
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

            app.MainFrame.setSize(680, 747);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "frontinterface");
        }
    }

    private static final String GET_TOP_SCORES_SQL = "SELECT name, score FROM leaderboard ORDER BY score DESC LIMIT ?";

//     Get the top N scores from the leaderboard and sort them using Quick Sort
    public ArrayList<TopScore> getTopScores(int n) {
        ArrayList<TopScore> topScores = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(GET_TOP_SCORES_SQL)) {

            pstmt.setInt(1, n);
            ResultSet rs = pstmt.executeQuery();

            // Populate the list with the results from the database
            while (rs.next()) {
                String playerName = rs.getString("name");
                int score = rs.getInt("score");
                topScores.add(new TopScore(playerName, score));
            }

            // Convert ArrayList to array for Quick Sort
            TopScore[] scoresArray = new TopScore[topScores.size()];
            topScores.toArray(scoresArray);

            // Sort using Quick Sort (descending order)
            QuickSort.quickSort(scoresArray, 0, scoresArray.length - 1);

            // Convert back the sorted array to an ArrayList
            topScores.clear();
            for (TopScore s : scoresArray) {
                topScores.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topScores;
    }
}