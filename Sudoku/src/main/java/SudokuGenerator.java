import java.util.*;

public class SudokuGenerator {
    public static final int SIZE = 9;

    public static int[][] generateFullBoard() {
        int[][] board = new int[SIZE][SIZE];
        fillBoard(board);
        return board;
    }

    private static boolean fillBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i <= SIZE; i++) numbers.add(i);
                    Collections.shuffle(numbers); // deixa aleatório

                    for (int num : numbers) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard(board)) return true;
                            board[row][col] = 0; // backtracking
                        }
                    }
                    return false; // nenhuma opção válida
                }
            }
        }
        return true;
    }

    public static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++)
            for (int c = boxColStart; c < boxColStart + 3; c++)
                if (board[r][c] == num) return false;
        return true;
    }

    // Remove números para criar desafio
    public static void removeNumbers(int[][] board, int difficulty) {
        Random rand = new Random();
        int removed = 0;
        int totalToRemove = difficulty; // 20-50 é um bom range
        while (removed < totalToRemove) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                removed++;
            }
        }
    }

    public static void printBoard(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(board[r][c] == 0 ? ". " : board[r][c] + " ");
            }
            System.out.println();
        }
    }
}
