package src;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
    Sound sound = new Sound();
    // Renk ve Boyut ayarları
    private final Color dotColor = new Color(255, 255, 0);
    private Color mazeColor = new Color(0, 191, 255);
    private Dimension d;
    // Font ve Resim
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    private final Font font = new Font("Press Start 2P", Font.PLAIN, 12);
    // Oyun Ayarları
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 1;
    private final int PACMAN_ANIM_COUNT = 4;
    private final int MAX_GHOSTS = 4;
    private final int PACMAN_SPEED = 6;
    private boolean dying = false;
    private int currentLevel = 2;
    // Oyun durumu ve hareketler
    private int pacAnimCount = PAC_ANIM_DELAY;
    private int pacAnimDir = 1;
    private int N_GHOSTS = 4;
    private int immortalTime = 0;
    private int pacsLeft;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;
    private final int maxSpeed = 6;
    private int currentSpeed = 3;
    private short[] screenData;
    public static Timer timer;
    public static boolean inGame = false;
    public static int score;
    public boolean gameOver = false;
    public boolean isSpecialFoodEaten = false;

    public Board() {
        LoadImages.loadImages();
        initVariables();
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new MyKeyListener(this));
        setFocusable(true);
        setBackground(Color.black);
    }

    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        timer = new Timer(40, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        initGame();
    }

    private void doAnim() {

        pacAnimCount--;
        if (pacAnimCount <= 0) {
            pacAnimCount = PAC_ANIM_DELAY;
            LoadImages.pacmanAnimPos = LoadImages.pacmanAnimPos + pacAnimDir;
            if (LoadImages.pacmanAnimPos == (PACMAN_ANIM_COUNT - 1) || LoadImages.pacmanAnimPos == 0) {
                pacAnimDir = -pacAnimDir;
            }
        }
    }

    private void playGame(Graphics2D g2d) {
        if (dying) {
            death();
            if (pacsLeft > 0)
                playSE(2);
        } else {
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    public void resetGame() {
        inGame = true;
        gameOver = false;
        pacsLeft = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 4;
        currentSpeed = 3;
        timer.restart();
        repaint();
        currentLevel = 1;
    }

    private void drawScore(Graphics2D g) {
        int i;
        String s;
        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + (score);
        g.drawString(s, SCREEN_SIZE / 2 + 90, SCREEN_SIZE + 17);
        for (i = 0; i < pacsLeft; i++) {
            g.drawImage(LoadImages.pacman3left, i * 28 + 8, SCREEN_SIZE + 4, this);
        }
    }

    private void drawImmortality(Graphics2D g) {

        int i;
        String s;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Ölümsüzlük: " + (immortalTime / 1000) + 's';
        g.drawString(s, SCREEN_SIZE / 2 - 60, SCREEN_SIZE + 17);
        for (i = 0; i < pacsLeft; i++) {
            g.drawImage(LoadImages.pacman3left, i * 28 + 8, SCREEN_SIZE + 4, this);
        }
    }

    private void checkMaze() {

        short i = 0;
        boolean finished = true;
        int maxLevel = 2;
        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 48) != 0) {
                finished = false;
            }
            i++;
        }
        if (finished) {
            playSE(3);
            score += 50;
            currentLevel++;
            if (currentLevel <= maxLevel) {
                initLevel();

            } else {
                currentLevel = 1;
                initLevel();
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }
        }
    }

    private void death() {

        pacsLeft--;
        if (pacsLeft == 0) {
            inGame = false;
            playSE(4);
            PacmanHighScore highScoreManager = new PacmanHighScore();
            highScoreManager.writeHighScore(score);
            gameOver = true;
        }
        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {
        short i;
        int pos;
        int count;

        for (i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);
                count = 0;
                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {
                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }
                } else {
                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }
            }

            ghost_x[i] += (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] += (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1, i + 1);

            if (LoadImages.pacman_x > (ghost_x[i] - 12) && LoadImages.pacman_x < (ghost_x[i] + 12)
                    && LoadImages.pacman_y > (ghost_y[i] - 12) && LoadImages.pacman_y < (ghost_y[i] + 12)
                    && inGame) {
                dying = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y, int ghostNumber) {
        switch (ghostNumber) {
            case 1:
                g2d.drawImage(LoadImages.ghost1, x, y, this);
                break;
            case 2:
                g2d.drawImage(LoadImages.ghost2, x, y, this);
                break;
            case 3:
                g2d.drawImage(LoadImages.ghost3, x, y, this);
                break;
            case 4:
                g2d.drawImage(LoadImages.ghost4, x, y, this);
                break;
        }
    }

    private void movePacman() {

        int pos;
        short ch;

        if (MyKeyListener.req_dx == -LoadImages.pacmand_x && MyKeyListener.req_dy == -LoadImages.pacmand_y) {
            LoadImages.pacmand_x = MyKeyListener.req_dx;
            LoadImages.pacmand_y = MyKeyListener.req_dy;
            MyKeyListener.view_dx = LoadImages.pacmand_x;
            MyKeyListener.view_dy = LoadImages.pacmand_y;
        }

        if (LoadImages.pacman_x % BLOCK_SIZE == 0 && LoadImages.pacman_y % BLOCK_SIZE == 0) {
            pos = LoadImages.pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (LoadImages.pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
                playSE(1);

            }
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            if ((ch & 32) != 0) {
                screenData[pos] = (short) (ch & 15);
                score += 60;
                playSE(1);

                isSpecialFoodEaten = true;
                immortalTime = 5 * 1000;

                scheduler.scheduleAtFixedRate(() -> {
                    immortalTime -= 40;
                    dying = false;
                    if (immortalTime <= 0) {
                        scheduler.shutdown();
                        isSpecialFoodEaten = false;
                    }
                }, 0, 40, TimeUnit.MILLISECONDS);

            }
            if (MyKeyListener.req_dx != 0 || MyKeyListener.req_dy != 0) {
                if (!((MyKeyListener.req_dx == -1 && MyKeyListener.req_dy == 0 && (ch & 1) != 0)
                        || (MyKeyListener.req_dx == 1 && MyKeyListener.req_dy == 0 && (ch & 4) != 0)
                        || (MyKeyListener.req_dx == 0 && MyKeyListener.req_dy == -1 && (ch & 2) != 0)
                        || (MyKeyListener.req_dx == 0 && MyKeyListener.req_dy == 1 && (ch & 8) != 0))) {
                    LoadImages.pacmand_x = MyKeyListener.req_dx;
                    LoadImages.pacmand_y = MyKeyListener.req_dy;
                    MyKeyListener.view_dx = LoadImages.pacmand_x;
                    MyKeyListener.view_dy = LoadImages.pacmand_y;
                }
            }

            // Check for standstill
            if ((LoadImages.pacmand_x == -1 && LoadImages.pacmand_y == 0 && (ch & 1) != 0)
                    || (LoadImages.pacmand_x == 1 && LoadImages.pacmand_y == 0 && (ch & 4) != 0)
                    || (LoadImages.pacmand_x == 0 && LoadImages.pacmand_y == -1 && (ch & 2) != 0)
                    || (LoadImages.pacmand_x == 0 && LoadImages.pacmand_y == 1 && (ch & 8) != 0)) {
                LoadImages.pacmand_x = 0;
                LoadImages.pacmand_y = 0;

            }
        }
        LoadImages.pacman_x = LoadImages.pacman_x + PACMAN_SPEED * LoadImages.pacmand_x;
        LoadImages.pacman_y = LoadImages.pacman_y + PACMAN_SPEED * LoadImages.pacmand_y;

    }

    private void drawPacman(Graphics2D g2d) {

        if (MyKeyListener.view_dx == -1) {
            LoadImages.drawPacnanLeft(g2d, this);
        } else if (MyKeyListener.view_dx == 1) {
            LoadImages.drawPacmanRight(g2d, this);
        } else if (MyKeyListener.view_dy == -1) {
            LoadImages.drawPacmanUp(g2d, this);
        } else {
            LoadImages.drawPacmanDown(g2d, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 1; y < (SCREEN_SIZE); y += (BLOCK_SIZE)) {
            for (x = 1; x < (SCREEN_SIZE); x += (BLOCK_SIZE)) {
                // 0001 1111
                // 1 2 4 8 16 32
                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2)); // çizginin kalınlığı
                // sağdan sola geçmememiz için çizilen çizgi

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                // Aşağıdan yukarı çıkmama için çizgi
                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                // soldan sağa geçmemek için çizginin görüntüsü
                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                // Yukarıdan aşağı gidilmeyen çizginin görüntüsü
                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                // Sarı yemler
                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(dotColor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }
                // Özel yem
                if ((screenData[i] & 32) != 0) {
                    g2d.drawImage(LoadImages.fruit, x + 3, y + 6, 15, 15, null);
                }

                i++;
            }
        }
    }

    public void initGame() {

        pacsLeft = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 4;
        currentSpeed = 3;
    }

    private void initLevel() {
        int i;
        short[] currentLevelData = null;
        switch (currentLevel) {
            case 1:
                currentLevelData = LevelData.levelData;
                break;
            case 2:
                currentLevelData = LevelData.levelData1;
                break;
            default:
                continueLevel();
                break;
        }
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = currentLevelData[i];
        }
        continueLevel();
        if (pacsLeft == 3)
            return;
        pacsLeft++;
    }

    private void continueLevel() {
        short i;
        int dx = 1;

        for (i = 0; i < N_GHOSTS; i++) {
            if (currentLevel == 1) {
                ghost_y[i] = BLOCK_SIZE * 7;
                ghost_x[i] = BLOCK_SIZE * 7;
            } else {
                ghost_y[i] = BLOCK_SIZE * 1;
                ghost_x[i] = BLOCK_SIZE * 7;
            }
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            ghostSpeed[i] = 3;
        }
        // 7.sütün 11.satır
        LoadImages.pacman_x = 7 * BLOCK_SIZE;
        LoadImages.pacman_y = 11 * BLOCK_SIZE;
        LoadImages.pacmand_x = 0;
        LoadImages.pacmand_y = 0;
        MyKeyListener.req_dx = 0;
        MyKeyListener.req_dy = 0;
        MyKeyListener.view_dx = -1;
        MyKeyListener.view_dy = 0;
        dying = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

    public void playMusic(int i) {
        if (sound.clip != null) {
            sound.stop();
        }
        sound.play();
        sound.setFile(i);
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        if (inGame) {
            drawMaze(g2d);
            drawScore(g2d);
            drawImmortality(g2d);
            doAnim();
            playGame(g2d);
        } else {
            if (gameOver) {

                g2d.drawImage(LoadImages.gameOverImage, 0, -30, this);
                g2d.setColor(Color.white);

                String scoreText = "Puan: " + score;
                g2d.drawString(scoreText, 150, 340);
                g2d.setFont(font);
                String promptText = "Yeninden Başlamak İçin R Tuşuna Basınız";
                g2d.drawString(promptText, 70, 360);
            }
        }
        g2d.drawImage(LoadImages.ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}