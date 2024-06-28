package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PacmanHighScore {

    public List<Integer> readHighScores() {
        ArrayList<Integer> highScores = new ArrayList<>();
        try {
            File file = new File("score.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextInt()) {
                    int score = scanner.nextInt();
                    highScores.add(score);
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScores;
    }

    public void addScore(List<Integer> highScores, int newScore) {
        highScores.add(newScore);
    }

    public List<Integer> getTop3Scores(List<Integer> highScores) {
        Collections.sort(highScores, Collections.reverseOrder());
        return highScores.subList(0, Math.min(5, highScores.size()));
    }

    public void writeHighScores(List<Integer> highScores) {
        try {
            FileWriter writer = new FileWriter("score.txt");
            for (int score : highScores) {
                writer.write(score + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHighScore(int newScore) {
        List<Integer> highScores = readHighScores();
        addScore(highScores, newScore);
        writeHighScores(highScores);
    }
}
