package src;

import java.awt.Event;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MyKeyListener extends KeyAdapter {
    private Board board;

    public MyKeyListener(Board board) {
        this.board = board;
    }

    public static int req_dx, req_dy, view_dx, view_dy;

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_R) {
            board.resetGame();
        }
        if (Board.inGame) {
            if (key == KeyEvent.VK_LEFT) {
                req_dx = -1;
                req_dy = 0;
            } else if (key == KeyEvent.VK_RIGHT) {
                req_dx = 1;
                req_dy = 0;
            } else if (key == KeyEvent.VK_UP) {
                req_dx = 0;
                req_dy = -1;
            } else if (key == KeyEvent.VK_DOWN) {
                req_dx = 0;
                req_dy = 1;
            } else if (key == KeyEvent.VK_ESCAPE && Board.timer.isRunning()) {

                Board.inGame = false;
                PacmanHighScore highScoreManager = new PacmanHighScore();
                highScoreManager.writeHighScore(Board.score);
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(board);
                currentFrame.dispose();
                PacmanGirisEkrani pacman = new PacmanGirisEkrani();
                pacman.setVisible(true);
                Board.timer.stop();
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == Event.LEFT || key == Event.RIGHT
                || key == Event.UP || key == Event.DOWN) {
            req_dx = 0;
            req_dy = 0;
        }
    }

}
