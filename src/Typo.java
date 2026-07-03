import java.io.*;

public class Typo {

    public static void main(String[] args) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            int numCase = Integer.parseInt(reader.readLine().trim());

            StringBuilder output = new StringBuilder();
            for (int i = 0; i < numCase; i++) {
                reader.readLine();
                String target = reader.readLine();
                String typo = reader.readLine();

                System.out.println("Target " + target);
                System.out.println("Typo " + typo);

                TypoSolver solver = new TypoSolver(target, typo);
                Solution solution = solver.solve();

                if (i > 0) {
                    output.append("\n");
                }

                output.append(solution.minCost).append("\n");
                for (String ops : solution.operations) {
                    output.append(ops).append("\n");
                }

            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(output.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
