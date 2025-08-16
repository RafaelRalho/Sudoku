import java.util.*;

public class SudokuGame {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int[][] solution = new int[9][9];
        generateBoard(solution); // Tabuleiro resolvido

        System.out.println("Escolha a dificuldade: 1-Fácil, 2-Médio, 3-Difícil");
        int difficulty = sc.nextInt();

        int remove;
        switch (difficulty) {
            case 1: remove = 30; break;
            case 2: remove = 40; break;
            case 3: remove = 50; break;
            default: remove = 30; break;
        }

        int[][] puzzle = copyBoard(solution);
        removeWithUniqueSolution(puzzle, remove);

        while (!isFull(puzzle)) {
            clearScreen();
            printBoard(puzzle);
            System.out.println("Digite linha, coluna e número (1-9): ");
            int row = sc.nextInt() - 1;
            int col = sc.nextInt() - 1;
            int num = sc.nextInt();

            if (row < 0 || row > 8 || col < 0 || col > 8 || num < 1 || num > 9) {
                System.out.println("Valores inválidos! Pressione Enter.");
                sc.nextLine(); sc.nextLine();
                continue;
            }

            if (puzzle[row][col] != 0) {
                System.out.println("Essa posição já está preenchida! Pressione Enter.");
                sc.nextLine(); sc.nextLine();
                continue;
            }

            if (solution[row][col] == num) {
                puzzle[row][col] = num;
            } else {
                System.out.println("Número incorreto! Pressione Enter.");
                sc.nextLine(); sc.nextLine();
            }
        }

        clearScreen();
        printBoard(puzzle);
        System.out.println("Parabéns! Você completou o Sudoku!");
    }

    // ====== GERAÇÃO DE TABULEIRO ======
    static void generateBoard(int[][] board) {
        fillBoard(board);
    }

    static boolean fillBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) numbers.add(i);
                    Collections.shuffle(numbers);
                    for (int num : numbers) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[startRow + i][startCol + j] == num) return false;
        return true;
    }

    // ====== REMOVER COM SOLUÇÃO ÚNICA ======
    static void removeWithUniqueSolution(int[][] board, int count) {
        Random rand = new Random();
        while (count > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (board[row][col] != 0) {
                int backup = board[row][col];
                board[row][col] = 0;
                if (!hasUniqueSolution(copyBoard(board))) {
                    board[row][col] = backup; // volta se perder unicidade
                } else {
                    count--;
                }
            }
        }
    }

    static boolean hasUniqueSolution(int[][] board) {
        return countSolutions(board, 0) == 1;
    }

    static int countSolutions(int[][] board, int count) {
        if (count > 1) return count;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            count = countSolutions(board, count);
                            board[row][col] = 0;
                        }
                    }
                    return count;
                }
            }
        }
        return count + 1;
    }

    // ====== FUNÇÕES AUXILIARES ======
    static int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            copy[i] = Arrays.copyOf(original[i], 9);
        return copy;
    }

    static void printBoard(int[][] board) {
        System.out.println("Sudoku:");
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) System.out.println("+-------+-------+-------+");
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print((board[i][j] == 0 ? ". " : board[i][j] + " "));
            }
            System.out.println("|");
        }
        System.out.println("+-------+-------+-------+");
    }

    static boolean isFull(int[][] board) {
        for (int[] row : board)
            for (int num : row)
                if (num == 0) return false;
        return true;
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
