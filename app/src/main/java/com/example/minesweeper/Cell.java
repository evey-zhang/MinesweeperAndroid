package com.example.minesweeper;

import android.widget.TextView;

import java.util.ArrayList;

public class Cell {
    private TextView cellTV;
    private int index;
    private int adjacentMines;
    private boolean isMine;
    private boolean alreadyClicked;

    //default instantiation
    public Cell(){
        //cellTv ;
        index = -1;
        adjacentMines = 0;
        isMine = false;
        alreadyClicked = false;

    }

    //user changed instantiation
    public Cell(TextView tv, int i, int minesNextTO, boolean m, boolean aC){
        cellTV = tv;
        index = i;
        adjacentMines = minesNextTO;
        isMine = isMine ;
        alreadyClicked = aC;

    }

    public Cell(TextView tv){
        cellTV = tv;
        index = -1;
        adjacentMines = 0;
        isMine = false;
        alreadyClicked = false;

    }
    public TextView getCellTV() {
        return cellTV;
    }

    public int getIndex() {
        return index;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isAlreadyClicked() {
        return alreadyClicked;
    }

    public void setCellTV(TextView cellTV) {
        this.cellTV = cellTV;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setAlreadyClicked(boolean alreadyClicked) {
        this.alreadyClicked = alreadyClicked;
    }
}
