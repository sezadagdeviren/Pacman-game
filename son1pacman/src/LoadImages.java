package src;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;

public class LoadImages {

    static final String IMAGE_PATH = "src/images/";
    static Image ghost1, ghost2, ghost3, ghost4, fruit, pacman1, pacman2up, pacman2left, pacman2right, pacman2down,
            pacman3up, pacman3down, pacman3left, pacman3right, pacman4up, pacman4down, pacman4left, pacman4right, ii,
            gameOverImage, backGround;
    static int pacman_x, pacman_y, pacmand_x, pacmand_y, pacmanAnimPos = 0;
    static String gifPath;

    public static void loadImages() {
        gifPath = IMAGE_PATH + "/giphy.gif";
        backGround = new ImageIcon(IMAGE_PATH + "/preview.png").getImage();
        gameOverImage = new ImageIcon(IMAGE_PATH + "/gameOver.png").getImage();
        ghost1 = new ImageIcon(IMAGE_PATH + "/ghost.png").getImage();
        ghost2 = new ImageIcon(IMAGE_PATH + "/ghost1.png").getImage();
        ghost3 = new ImageIcon(IMAGE_PATH + "/ghost2.png").getImage();
        ghost4 = new ImageIcon(IMAGE_PATH + "/ghost3.png").getImage();
        fruit = new ImageIcon(IMAGE_PATH + "/fruit.png").getImage();
        pacman1 = new ImageIcon(IMAGE_PATH + "/pacman.png")
                .getImage();
        pacman2up = new ImageIcon(IMAGE_PATH + "/up1.png")
                .getImage();
        pacman3up = new ImageIcon(IMAGE_PATH + "/up2.png")
                .getImage();
        pacman4up = new ImageIcon(IMAGE_PATH + "/up3.png")
                .getImage();
        pacman2down = new ImageIcon(IMAGE_PATH + "/down1.png")
                .getImage();
        pacman3down = new ImageIcon(IMAGE_PATH + "/down2.png")
                .getImage();
        pacman4down = new ImageIcon(IMAGE_PATH + "/down3.png")
                .getImage();
        pacman2left = new ImageIcon(IMAGE_PATH + "/left1.png")
                .getImage();
        pacman3left = new ImageIcon(IMAGE_PATH + "/left2.png")
                .getImage();
        pacman4left = new ImageIcon(IMAGE_PATH + "/left3.png")
                .getImage();
        pacman2right = new ImageIcon(IMAGE_PATH + "/right1.png")
                .getImage();
        pacman3right = new ImageIcon(IMAGE_PATH + "/right2.png")
                .getImage();
        pacman4right = new ImageIcon(IMAGE_PATH + "/right3.png")
                .getImage();

    }

    public static void drawPacmanUp(Graphics2D g2d, Board board) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(LoadImages.pacman2up, pacman_x + 1, pacman_y + 1, board);
                break;
            case 2:
                g2d.drawImage(LoadImages.pacman3up, pacman_x + 1, pacman_y + 1, board);
                break;
            case 3:
                g2d.drawImage(LoadImages.pacman4up, pacman_x + 1, pacman_y + 1, board);
                break;
            default:
                g2d.drawImage(LoadImages.pacman1, pacman_x + 1, pacman_y + 1, board);
                break;
        }
    }

    public static void drawPacmanDown(Graphics2D g2d, Board board) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(LoadImages.pacman2down, pacman_x + 1, pacman_y + 1, board);
                break;
            case 2:
                g2d.drawImage(LoadImages.pacman3down, pacman_x + 1, pacman_y + 1, board);
                break;
            case 3:
                g2d.drawImage(LoadImages.pacman4down, pacman_x + 1, pacman_y + 1, board);
                break;
            default:
                g2d.drawImage(LoadImages.pacman1, pacman_x + 1, pacman_y + 1, board);
                break;
        }
    }

    public static void drawPacnanLeft(Graphics2D g2d, Board board) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(LoadImages.pacman2left, pacman_x + 1, pacman_y + 1, board);
                break;
            case 2:
                g2d.drawImage(LoadImages.pacman3left, pacman_x + 1, pacman_y + 1, board);
                break;
            case 3:
                g2d.drawImage(LoadImages.pacman4left, pacman_x + 1, pacman_y + 1, board);
                break;
            default:
                g2d.drawImage(LoadImages.pacman1, pacman_x + 1, pacman_y + 1, board);
                break;
        }
    }

    public static void drawPacmanRight(Graphics2D g2d, Board board) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(LoadImages.pacman2right, pacman_x + 1, pacman_y + 1, board);
                break;
            case 2:
                g2d.drawImage(LoadImages.pacman3right, pacman_x + 1, pacman_y + 1, board);
                break;
            case 3:
                g2d.drawImage(LoadImages.pacman4right, pacman_x + 1, pacman_y + 1, board);
                break;
            default:
                g2d.drawImage(LoadImages.pacman1, pacman_x + 1, pacman_y + 1, board);
                break;
        }
    }

}
