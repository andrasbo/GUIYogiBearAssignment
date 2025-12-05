package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Andras Boromissza [hyczbo]
 */
public class StatsPanel extends JPanel {
    private final JLabel heartsLabel;
    private final JLabel pointsLabel;
    private final JLabel timeLabel;
    public StatsPanel() {  
        setLayout(new BorderLayout());
        setBackground(new Color(245, 222, 179));
        setBorder(new EmptyBorder(10, 20, 10, 20));
        
        heartsLabel = new JLabel("❤️❤️❤");
        pointsLabel = new JLabel("0⭐");
        timeLabel = new JLabel("00:00");
        
        Font font = new Font("Monospaced", Font.BOLD, 24);
        heartsLabel.setFont(font);        
        pointsLabel.setFont(font);       
        timeLabel.setFont(font);
        
        pointsLabel.setHorizontalAlignment(JLabel.CENTER);
        
        heartsLabel.setForeground(Color.RED);
        pointsLabel.setForeground(new Color(255, 140, 0));
        timeLabel.setForeground(Color.DARK_GRAY);
        
        add(heartsLabel, BorderLayout.LINE_START);
        add(pointsLabel, BorderLayout.CENTER);
        add(timeLabel, BorderLayout.LINE_END);
    }
    public void updateStats(int hearts, int points, long elapsedTimeInMillis) {
        drawHearts(hearts);
        drawPoints(points);
        drawTime(elapsedTimeInMillis);
    }
    public void drawHearts(int hearts) {
        String str = "";
        for (int i = 0; i < hearts; i++) {
            str += "❤";
        }
        heartsLabel.setText(str);
    }
    public void drawPoints(int points) {
        pointsLabel.setText(points + "⭐");
    }
    public void drawTime(long millis) {
        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        
        timeLabel.setText(formattedTime);
    }
    public void refresh() {
        repaint();
    }
}
