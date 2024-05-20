import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UniqueInt {
    public static void main(String[] args) {
        String inputDirectory = "/Data-structures_algorithm/hw_01/sample_inputs/";
        String outputDirectory = "/Data-structures_algorithm/hw_01/sample_results/";

        // Create output directory if it doesn't exist
        new File(outputDirectory).mkdirs();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputDirectory))) {
            for (Path entry : stream) {
                String inputFilePath = entry.toString();
                String outputFilePath = outputDirectory + entry.getFileName().toString() + "_results.txt";
                processFile(inputFilePath, outputFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processFile(String inputFilePath, String outputFilePath) {
        Set<Integer> uniqueIntegers = new TreeSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (isValidInteger(line)) {
                    uniqueIntegers.add(Integer.parseInt(line));
                }
            }

            for (Integer num : uniqueIntegers) {
                bw.write(num.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidInteger(String str) {
        if (str.isEmpty()) return false;
        try {
            int val = Integer.parseInt(str);
            return val >= -1023 && val <= 1023;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
