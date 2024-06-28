package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PacmanGirisEkrani extends JFrame {

    private PacmanHighScore highScoreManager;
    Board board = new Board();

    public PacmanGirisEkrani() {
        setTitle("PACMAN");
        setSize(380, 490);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        board.playMusic(0);
        highScoreManager = new PacmanHighScore();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(LoadImages.backGround, 0, 0, 380, 500, this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JButton baslaButton = new JButton("Oyuna Başla");
        JButton skorButton = new JButton("Skorları Görüntüle");

        ImageIcon gifIcon = new ImageIcon(LoadImages.gifPath);
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gifLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        skorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHighScores();
            }
        });

        baslaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pacman ex = new Pacman();
                Board.inGame = true;
                ex.setVisible(true);
                setVisible(false);
                board.stopMusic();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.add(baslaButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 90)));
        buttonPanel.add(skorButton);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 300)));
        panel.add(buttonPanel);
        panel.add(gifLabel);

        add(panel);
    }

    private void showHighScores() {
        List<Integer> highScores = highScoreManager.readHighScores();
        List<Integer> top3Scores = highScoreManager.getTop3Scores(highScores);

        StringBuilder message = new StringBuilder("En Yüksek Skorlar\n\n");
        for (int i = 0; i < top3Scores.size(); i++) {
            message.append(i + 1).append(".  ").append(top3Scores.get(i)).append("\n");
        }

        JOptionPane.showMessageDialog(this, message.toString(), "Skorlar",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
