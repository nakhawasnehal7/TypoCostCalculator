import java.util.HashMap;
import java.util.Map;

public class Keyboard {

    private static final String[] ROWS = {
            "1234567890",
            "qwertyuiop",
            "asdfghjkl;",
            "zxcvbnm,."
    };

    private Map<Character, Position> keyPositions;
    private Map<Character, Integer> keyHands;
    private Map<Character, Integer> keyFingers;


    public Keyboard() {
        keyPositions = new HashMap<>();
        keyHands = new HashMap<>();
        keyFingers = new HashMap<>();
        initializeKeyboard();
    }

    private void initializeKeyboard() {

        for (int row = 0; row < ROWS.length; row++) {
            for (int col = 0; col < ROWS[row].length(); col++) {
                char key = ROWS[row].charAt(col);
                keyPositions.put(key, new Position(row, col));

                if (col < 5) {
                    keyHands.put(key, 0);
                } else {
                    keyHands.put(key, 1);
                }

                if (col == 0 || col == 9) {
                    keyFingers.put(key, 0);
                } else if (col == 1 || col == 8) {
                    keyFingers.put(key, 1);

                } else if (col == 2 || col == 7) {
                    keyFingers.put(key, 2);
                } else {
                    keyFingers.put(key, 3);
                }

            }
        }
    }

    public int distance(char k1, char k2) {
        Position p1 = keyPositions.get(k1);
        Position p2 = keyPositions.get(k2);
        return Math.max(Math.abs(p1.row - p2.row),
                Math.abs(p1.col - p2.col));
    }

    public boolean sameHand(char k1, char k2) {
        if (k1 == ' ' || k2 == ' ') return false;
        return keyHands.get(k1).equals(keyHands.get(k2));
    }

    public boolean sameFinger(char k1, char k2) {
        if (k1 == ' ' || k2 == ' ') return false;
        return keyFingers.get(k1).equals(keyFingers.get(k2));
    }

    public boolean isBottomRow(char c) {
        return ROWS[3].indexOf(c) != -1;
    }

    private static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}

