public class CostCalculator {
    private Keyboard keyboard;

    public CostCalculator() {
        this.keyboard = new Keyboard();

    }

    private int insertCostAfter(char prev, char ch) {
        if (ch == ' ' && prev != ' ' && keyboard.isBottomRow(prev)) {
            return 2;
        }
        if (ch == ' ' && prev != ' ') {
            return 6;
        }
        if (prev == ' ' && ch != ' ') {
            return 6;
        }
        if (prev != ' ' && ch != ' ') {
            if (keyboard.sameHand(prev, ch)) {
                return keyboard.distance(prev, ch);
            } else {
                return 5;
            }
        }
        return 5;
    }

    private int insertCostBefore(char ch, char next) {
        if (ch != ' ' && next == ' ') {
            return 6;
        }

        if (ch == ' ' && next != ' ') {
            return 6;
        }

        if (ch != ' ' && next != ' ') {
            if (keyboard.sameHand(ch, next)) {
                return keyboard.distance(ch, next);
            } else {
                return 5;
            }
        }
        return 5;
    }

    public int transposeCostChars(char ch1, char ch2) {
        if (ch1 == ' ' || ch2 == ' ') {
            return 3;
        }
        if (!keyboard.sameHand(ch1, ch2)) {
            return 1;
        }
        return 2;
    }


    public int substituteCost(char oldChar, char newChar) {
        if (oldChar == ' ' || newChar == ' ') {
            return 6;
        }
        if (keyboard.sameHand(oldChar, newChar)) {
            return keyboard.distance(oldChar, newChar);
        }

        if (keyboard.sameFinger(oldChar, newChar)) {
            return 1;
        }
        return 5;
    }

    public int transposeCost(String s, int pos) {
        if (pos < 0 || pos >= s.length() - 1) {
            return Integer.MAX_VALUE;
        }
        char ch1 = s.charAt(pos);
        char ch2 = s.charAt(pos + 1);

        if (ch1 == ' ' || ch2 == ' ') {
            return 3;
        }

        if (!keyboard.sameHand(ch1, ch2)) {
            return 1;
        }
        return 2;
    }

    public int insertCostContext(char before, char after, char ch) {
        if (before != '\0' && before == ch) return 1;
        if (after != '\0' && after == ch) return 1;

        int costAfter = Integer.MAX_VALUE;
        int costBefore = Integer.MAX_VALUE;

        if (before != '\0') {
            costAfter = insertCostAfter(before, ch);
        }
        if (after != '\0') {
            costBefore = insertCostBefore(ch, after);
        }

        if (before == '\0' && after == '\0') return 5;
        if (before == '\0') return costBefore;
        if (after == '\0') return costAfter;

        return Math.min(costAfter, costBefore);
    }

    public int deleteCostContext(char before, char current) {
        if (before == '\0') return 6;
        if (current == before) return 1;
        if (current == ' ') return 3;
        if (before != ' ' && current != ' ' && keyboard.sameHand(before, current)) {
            return 2;
        }
        return 6;
    }

}
