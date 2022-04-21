package com.example.photogallery;

import java.util.ArrayList;

public class solver {

    int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;
    int selectedRow;
    int selectedCol;
    solver(){
        selectedRow = -1;
        selectedCol = -1;
        board = new int[9][9];
        for(int r = 0; r < 9; r++){
            for(int c = 0; c < 9; c++){
                board[r][c] = 0;
            }
        }

        emptyBoxIndex = new ArrayList<>();

    }

    public void getEmptyBoxIndexes(){
        for(int r = 0; r < 9; r++)
            for(int c = 0; c < 9; c++)
                if(this.board[r][c] == 0){
                    this.emptyBoxIndex.add(new ArrayList<>());
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(r);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(c);
                }
    }

    public void resetBoard(){
        for(int r = 0; r < 9; r++){
            for(int c = 0; c < 9; c++){
                board[r][c] = 0;
            }
        }
        this.emptyBoxIndex = new ArrayList<>();
    }


    public void setNumberPos(int num){
        if(this.selectedRow != -1 && this.selectedCol != -1){}
            if(this.board[this.selectedRow-1][this.selectedCol-1] == num)
                this.board[this.selectedRow-1][this.selectedCol-1] = 0;
            else
                this.board[this.selectedRow-1][this.selectedCol-1] = num;
    }

    public  int[][] getBoard(){
        return this.board;
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndex(){
        return this.emptyBoxIndex;
    }

    private boolean check(int row, int col){
        if(this.board[row][col] > 0){
            for(int i = 0; i < 9; i++){
                if(this.board[i][col] == this.board[row][col] && row != i){
                    return false;
                }
                if(this.board[row][i] == this.board[row][col] && col != i){
                    return false;
                }
            }
            int rowBox = row/3;
            int colBox = col/3;
            for(int r = rowBox*3; r < rowBox*3+3; r++){
                for(int c = colBox*3; c < colBox*3+3; c++){
                    if(this.board[r][c] == this.board[row][col] && row != r && col != c){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean solve(soduko display){
        int row = -1;
        int col = -1;
        for(int r = 0; r < 9; r++)
            for(int c = 0; c < 9; c++)
                if(this.board[r][c] == 0){
                    row = r;
                    col = c;
                    break;
                }
        if(row == -1 && col == -1)
            return true;
        for(int i = 1; i < 10; i++){
            this.board[row][col] = i;
            display.invalidate();
            if(check(row, col)){
                if(solve(display)){
                    return true;
                }
            }
            this.board[row][col] = 0;
        }
        return false;
    }

    public int getSelectedRow(){
        return selectedRow;
    }
    public int getSelectedCol(){
        return selectedCol;
    }
    public void setSelectedRow(int row){
        selectedRow = row;
    }
    public void setSelectedCol(int col){
        selectedCol = col;
    }
}
