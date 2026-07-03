import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypoSolver {

    private String target;
    private String typo;
    private CostCalculator costCalculator;
    private Map<State, Result> memo;


    public TypoSolver(String target, String typo) {
        this.target = target;
        this.typo = typo;
        this.costCalculator = new CostCalculator();
        this.memo = new HashMap<>();
    }

    public Solution solve() {
        Result result = solveRecursive(0, 0);
        return new Solution(result.cost, result.operations);
    }

    private Result solveRecursive(int i, int j) {
        if (i >= target.length() && j >= typo.length()) {
            return new Result(0, new ArrayList<>());
        }

        State state = new State(i, j);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        Result best = new Result(Integer.MAX_VALUE, new ArrayList<>());


        if (i >= target.length()) {
            Result subResult = solveRecursive(i, j + 1);
            if (subResult.cost != Integer.MAX_VALUE) {
                char before = (j > 0) ? typo.charAt(j - 1) : '\0';
                char after = '\0';
                int cost = costCalculator.insertCostContext(before, after, typo.charAt(j));
                List<String> ops = new ArrayList<>();
                ops.add(String.format("Insert %c before %d", typo.charAt(j), i));
                ops.addAll(adjustOperations(subResult.operations, 1, i));
                best = new Result(cost + subResult.cost, ops);
            }
            memo.put(state, best);
            return best;
        }

        if (j >= typo.length()) {
            Result subResult = solveRecursive(i + 1, j);
            if (subResult.cost != Integer.MAX_VALUE) {
                char before = (j > 0) ? typo.charAt(j - 1) : '\0';
                int cost = costCalculator.deleteCostContext(before, target.charAt(i));
                List<String> ops = new ArrayList<>();
                ops.add(String.format("Delete %d", i));
                ops.addAll(adjustOperations(subResult.operations, -1, i));
                best = new Result(cost + subResult.cost, ops);
            }
            memo.put(state, best);
            return best;
        }


        if (target.charAt(i) == typo.charAt(j)) {
            Result subResult = solveRecursive(i + 1, j + 1);
            if (subResult.cost < best.cost) {
                best = new Result(subResult.cost, new ArrayList<>(subResult.operations));
            }
        }

        if (i + 1 < target.length() && j + 1 < typo.length()) {
            if (target.charAt(i) == typo.charAt(j + 1) &&
                    target.charAt(i + 1) == typo.charAt(j)) {
                Result subResult = solveRecursive(i +2, j + 2);
                if (subResult.cost != Integer.MAX_VALUE) {
                    int cost = costCalculator.transposeCost(target, i);
                    if (cost + subResult.cost < best.cost) {
                        List<String> ops = new ArrayList<>();
                        ops.add(String.format("Transpose %d-%d", i, i + 1));
                        ops.addAll(subResult.operations);
                        best = new Result(cost + subResult.cost, ops);
                    }
                }
            }
        }
        if (i + 2 < target.length() && j + 2 < typo.length()) {
            if (target.charAt(i + 1) == typo.charAt(j) && target.charAt(i + 2) == typo.charAt(j + 1) && target.charAt(i) == typo.charAt(j + 2)) {

                Result subResult = solveRecursive(i + 3, j + 3);

                if (subResult.cost != Integer.MAX_VALUE) {
                    int cost = costCalculator.transposeCostChars(target.charAt(i), target.charAt(i + 1)) + costCalculator.transposeCostChars(target.charAt(i), target.charAt(i + 2));

                    if (cost + subResult.cost < best.cost) {
                        List<String> ops = new ArrayList<>();
                        ops.add(String.format("Transpose %d-%d", i, i + 1));
                        ops.add(String.format("Transpose %d-%d", i + 1, i + 2));
                        ops.addAll(subResult.operations);
                        best = new Result(cost + subResult.cost, ops);
                    }
                }
            }
        }

        Result subResult = solveRecursive(i + 1, j + 1);
        if (subResult.cost != Integer.MAX_VALUE) {
            int cost = costCalculator.substituteCost(target.charAt(i), typo.charAt(j));
            if (cost + subResult.cost < best.cost) {
                List<String> ops = new ArrayList<>();
                ops.add(String.format("Substitute %c at %d", typo.charAt(j), i));
                ops.addAll(subResult.operations);
                best = new Result(cost + subResult.cost, ops);
            }
        }

        Result delResult = solveRecursive(i + 1, j);
        if (delResult.cost != Integer.MAX_VALUE) {
            char before = (j > 0) ? typo.charAt(j - 1) : '\0';
            int cost = costCalculator.deleteCostContext(before, target.charAt(i));
            if (cost + delResult.cost < best.cost) {
                List<String> ops = new ArrayList<>();
                ops.add(String.format("Delete %d", i));
                ops.addAll(adjustOperations(delResult.operations, -1, i + 1));
                best = new Result(cost + delResult.cost, ops);
            }
        }

        Result insResult = solveRecursive(i, j + 1);
        if (insResult.cost != Integer.MAX_VALUE) {
            char before = (j > 0) ? typo.charAt(j - 1) : '\0';
            char after = (i < target.length()) ? target.charAt(i) : '\0';
            int cost = costCalculator.insertCostContext(before, after, typo.charAt(j));
            if (cost + insResult.cost < best.cost) {
                List<String> ops = new ArrayList<>();
                ops.add(String.format("Insert %c before %d", typo.charAt(j), i));
                ops.addAll(adjustOperations(insResult.operations, 1, i));
                best = new Result(cost + insResult.cost, ops);

            }
        }
        memo.put(state, best);
        return best;

    }

    private List<String> adjustOperations(List<String> ops, int delta, int afterPos) {
        List<String> adjusted = new ArrayList<>();
        for (String op : ops) {
            adjusted.add(adjustOperation(op, delta, afterPos));
        }
        return adjusted;
    }

    private String adjustOperation(String op, int delta, int afterPos) {
        if (op.startsWith("Insert")) {
            int beforeTdx = op.lastIndexOf("before ") + 7;
            int pos = Integer.parseInt(op.substring(beforeTdx));
            if (pos >= afterPos) {
                pos += delta;
            }
            return op.substring(0, beforeTdx) + pos;
        } else if (op.startsWith("Delete")) {

            int pos = Integer.parseInt(op.substring(7).trim());
            if (pos >= afterPos) {
                pos += delta;
            }
            return "Delete " + pos;
        } else if (op.startsWith("Substitute")) {
            int atIDx = op.lastIndexOf("at") + 3;
            int pos = Integer.parseInt(op.substring(atIDx));
            if (pos >= afterPos) {
                pos += delta;
            }
            return op.substring(0, atIDx) + pos;

        } else if (op.startsWith("Transpose")) {
            int dashIdx = op.indexOf('-');
            int pos1 = Integer.parseInt(op.substring(10, dashIdx));
            int pos2 = Integer.parseInt(op.substring(dashIdx + 1));
            if (pos1 >= afterPos) {
                pos1 += delta;
                pos2 += delta;
            }
            return "Transpose " + pos1 + "-" + pos2;
        }
        return op;
    }


}
