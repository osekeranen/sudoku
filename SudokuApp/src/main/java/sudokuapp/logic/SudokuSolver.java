package sudokuapp.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 */
public class SudokuSolver {

    public SudokuSolver() {
    }
    
    public int[][] solve(int y, int x, int[][] sudoku) {
        ArrayList<Integer> availableNumbers = this.getAvailableNumbersAsAList(y, x, sudoku);
        int[][] newSudoku = this.copySudoku(sudoku);
        
        while (true) {
            if (availableNumbers.isEmpty()) {
                break;
            }
            
            newSudoku[y][x] = availableNumbers.get(0);
            
            if (x == 8) {
                if (y == 8) {
                    return newSudoku;
                } else {
                    newSudoku = this.solve(y + 1, 0, newSudoku);
                }
            } else {
                newSudoku = this.solve(y, x + 1, newSudoku);
            }
            
            if (newSudoku[8][8] != 0) {
                break;
            } else {
                newSudoku = this.copySudoku(sudoku);
                availableNumbers.remove(0);
            }
        }
        
        return newSudoku;
    }
    
    public int[][] desolve(int[][] sudoku, int clues) {
        int[][] newSudoku = this.copySudoku(sudoku);
        
        Random rgen = new Random();
        int emptySpots = 0;
        
        while (true) {
            if (emptySpots >= (newSudoku.length * newSudoku.length) - clues) {
                break;
            }
            
            int x = rgen.nextInt(newSudoku.length);
            int y = rgen.nextInt(newSudoku.length);
            
            if (newSudoku[y][x] == 0) {
                continue;
            } else {
                newSudoku[y][x] = 0;
                emptySpots++;
            }
        }
        
        return newSudoku;
    }
    
    private int[][] copySudoku(int[][] sudoku) {
        int[][] newSudoku = new int[sudoku.length][sudoku[0].length];
        
        for (int y = 0; y < sudoku.length; y++) {
            for (int x = 0; x < sudoku[y].length; x++) {
                newSudoku[y][x] = sudoku[y][x];
            }
        }
        
        return newSudoku;
    }
    
    private ArrayList<Integer> getAvailableNumbersAsAList(int y, int x, int[][] sudoku) {
        boolean[] availableNumbers = this.getAvailableNumbers(y, x, sudoku);
        
        ArrayList<Integer> availableNumbersAsAList = new ArrayList<>();
        for (int i = 1; i < availableNumbers.length; i++) {
            if (availableNumbers[i]) {
                availableNumbersAsAList.add(i);
            }
        }
        
        Collections.shuffle(availableNumbersAsAList);
        
        return availableNumbersAsAList;
    }
    
    private boolean[] getAvailableNumbers(int y, int x, int[][] sudoku) {
        boolean[] availableNumbers = new boolean[10];
        
        boolean[] availableNumbersInRow = this.getAvailableNumbersInRow(y, sudoku);
        boolean[] availableNumbersInColumn = this.getAvailableNumbersInColumn(x, sudoku);
        int squareIndex = ((y * 9 + x) / 3) % 3 + ((y * 9 + x) / 3) / 3 - ((y * 9 + x) / 9) % 3;
        boolean[] availableNumbersInSquare = this.getAvailableNumbersInSquare(squareIndex, sudoku);
        
        for (int i = 0; i < availableNumbers.length; i++) {
            availableNumbers[i] = availableNumbersInRow[i] && availableNumbersInColumn[i] && availableNumbersInSquare[i];
        }
        
        return availableNumbers;
    }
    
    private boolean[] getAvailableInArray(int[] array) {
        boolean[] availableNumbers = new boolean[10];
        
        for (int i = 0; i < availableNumbers.length; i++) {
            availableNumbers[i] = true;
        }
        
        for (int i = 0; i < array.length; i++) {
            availableNumbers[array[i]] = false;
        }
        
        return availableNumbers;
    }
    
    private boolean[] getAvailableNumbersInRow(int index, int[][] sudoku) {
        return this.getAvailableInArray(this.getRow(index, sudoku));
    }
    
    private boolean[] getAvailableNumbersInColumn(int index, int[][] sudoku) {
        return this.getAvailableInArray(this.getColumn(index, sudoku));
    }
    
    private boolean[] getAvailableNumbersInSquare(int index, int[][] sudoku) {
        return this.getAvailableInArray(this.getSquare(index, sudoku));
    }
    
    private int[] getRow(int index, int[][] sudoku) {
        int[] row = sudoku[index];
        return row;
    }
    
    private int[] getColumn(int index, int[][] sudoku) {
        int[] column = new int[9];
        
        for (int i = 0; i < column.length; i++) {
            column[i] = sudoku[i][index];
        }
        
        return column;
    }
    
    private int[] getSquare(int index, int[][] sudoku) {
        int[] square = new int[9];
        
        int arrayIndex = 0;
        for (int i = 0; i < (square.length * square.length); i++) {
            int y = i / 9;
            int x = i % 9;
            
            int squareIndex = ((y * 9 + x) / 3) % 3 + ((y * 9 + x) / 3) / 3 - ((y * 9 + x) / 9) % 3;
            
            if (squareIndex == index) {
                square[arrayIndex] = sudoku[y][x];
                arrayIndex++;
            }
        }
        
        return square;
    }
}
