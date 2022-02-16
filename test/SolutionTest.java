import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SolutionTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c", "d", "e"})
    public void testWithJavaSolution(String s) {
        String inputFileName = String.format("input/%s.txt", s);
        String outputFileName = String.format("output/%s.txt", s);

        Input input = InputFileParser.parseInputFile(inputFileName);

        // Get Output from Java solution
        Solution solution = new Solution1();
        Output output = null;
        OutputFileWriter.writeToOutputFile(output, outputFileName);

        long score = ScoreCalculator.calculateScore(input, output);

        System.out.println("Test " + s + ": " + score);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c", "d", "e"})
    public void testWithOutputFile(String s) {
        String inputFileName = String.format("input/%s.txt", s);
        String outputFileName = String.format("output/%s.txt", s);

        Input input = InputFileParser.parseInputFile(inputFileName);

        // Get Output from output file
        Output output = OutputFileParser.parseOutputFile(outputFileName);

        long score = ScoreCalculator.calculateScore(input, output);

        System.out.println("Test " + s + ": " + score);
    }
}
