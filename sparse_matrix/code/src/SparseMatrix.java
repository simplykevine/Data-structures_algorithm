import java.io.*;
import java.util.*;

class SparseMatrix {
    private int numRows;
    private int numCols;
    private Map<Pair, Integer> elements;

    public SparseMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.elements = new HashMap<>();
    }

    public SparseMatrix(String matrixFilePath) throws IOException {
        this.elements = new HashMap<>();
        readFromFile(matrixFilePath);
    }

    private void readFromFile(String matrixFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(matrixFilePath))) {
            this.numRows = Integer.parseInt(reader.readLine().split("=")[1].trim());
            this.numCols = Integer.parseInt(reader.readLine().split("=")[1].trim());

            // Debugging statements
            System.out.println("Reading matrix from " + matrixFilePath);
            System.out.println("Rows: " + this.numRows + ", Columns: " + this.numCols);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (!line.startsWith("(") || !line.endsWith(")")) {
                    throw new IllegalArgumentException("Input file has wrong format");
                }

                line = line.substring(1, line.length() - 1);
                String[] parts = line.split(",");
                int row = Integer.parseInt(parts[0].trim());
                int col = Integer.parseInt(parts[1].trim());
                int value = Integer.parseInt(parts[2].trim());

                elements.put(new Pair(row, col), value);
            }
        }
    }

    public int getElement(int currRow, int currCol) {
        Pair key = new Pair(currRow, currCol);
        return elements.containsKey(key) ? elements.get(key) : 0;
    }

    public void setElement(int currRow, int currCol, int value) {
        if (value != 0) {
            elements.put(new Pair(currRow, currCol), value);
        } else {
            elements.remove(new Pair(currRow, currCol));
        }
    }

    public SparseMatrix add(SparseMatrix other) {
        if (this.numRows != other.numRows || this.numCols != other.numCols) {
            throw new IllegalArgumentException("Matrices dimensions do not match for addition");
        }

        SparseMatrix result = new SparseMatrix(this.numRows, this.numCols);
        Set<Pair> keys = new HashSet<>(this.elements.keySet());
        keys.addAll(other.elements.keySet());

        for (Pair key : keys) {
            result.setElement(key.row, key.col, this.getElement(key.row, key.col) + other.getElement(key.row, key.col));
        }

        return result;
    }

    public SparseMatrix subtract(SparseMatrix other) {
        if (this.numRows != other.numRows || this.numCols != other.numCols) {
            throw new IllegalArgumentException("Matrices dimensions do not match for subtraction");
        }

        SparseMatrix result = new SparseMatrix(this.numRows, this.numCols);
        Set<Pair> keys = new HashSet<>(this.elements.keySet());
        keys.addAll(other.elements.keySet());

        for (Pair key : keys) {
            result.setElement(key.row, key.col, this.getElement(key.row, key.col) - other.getElement(key.row, key.col));
        }

        return result;
    }

    public SparseMatrix multiply(SparseMatrix other) {
        if (this.numCols != other.numRows) {
            throw new IllegalArgumentException("Matrices dimensions do not match for multiplication");
        }

        SparseMatrix result = new SparseMatrix(this.numRows, other.numCols);
        for (Map.Entry<Pair, Integer> entry : this.elements.entrySet()) {
            int i = entry.getKey().row;
            int k = entry.getKey().col;
            for (int j = 0; j < other.numCols; j++) {
                int value = result.getElement(i, j) + entry.getValue() * other.getElement(k, j);
                result.setElement(i, j, value);
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Rows=").append(numRows).append(", Cols=").append(numCols).append("\n");
        for (Map.Entry<Pair, Integer> entry : elements.entrySet()) {
            result.append("(").append(entry.getKey().row).append(", ").append(entry.getKey().col).append(", ").append(entry.getValue()).append(")\n");
        }
        return result.toString();
    }

    private static class Pair {
        int row;
        int col;

        Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return row == pair.row && col == pair.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select operation:");
            System.out.println("1. Add matrices");
            System.out.println("2. Subtract matrices");
            System.out.println("3. Multiply matrices");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("4")) {
                break;
            }

            System.out.print("Enter path to first matrix file: ");
            String file1 = scanner.nextLine();
            System.out.print("Enter path to second matrix file: ");
            String file2 = scanner.nextLine();

            try {
                SparseMatrix matrix1 = new SparseMatrix(file1);
                SparseMatrix matrix2 = new SparseMatrix(file2);
                SparseMatrix result;

                switch (choice) {
                    case "1":
                        result = matrix1.add(matrix2);
                        break;
                    case "2":
                        result = matrix1.subtract(matrix2);
                        break;
                    case "3":
                        result = matrix1.multiply(matrix2);
                        break;
                    default:
                        System.out.println("Invalid choice");
                        continue;
                }

                System.out.println("Result:");
                System.out.println(result);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
