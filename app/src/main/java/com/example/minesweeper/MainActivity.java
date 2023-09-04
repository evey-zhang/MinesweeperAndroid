package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Random;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //INSTANTIATION OF VARIABLES
    private static final int COLUMN_COUNT = 10;
    private static final int ROW_COUNT = 12;

    //private ArrayList<Integer> cell_tvs;
    //private ArrayList<Integer> adjacentMines; // number of mines next to square
    private ArrayList<Cell> cellArr;
    private ArrayList<Cell> mines;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //array to store each cell
        //cell_tvs = new ArrayList<TextView>();
        cellArr = new ArrayList<Cell>();
        mines = new ArrayList<Cell>();
        //adjacentMines = new ArrayList<Integer>();

        //make the grid
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                TextView tv = new TextView(this);
                tv.setHeight(dpToPixel(30));
                tv.setWidth(dpToPixel(30));
                tv.setTextSize(10);//dpToPixel(32) )
                tv.setText(String.valueOf(i)+String.valueOf(j));
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(Color.GRAY);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);


                grid.addView(tv, lp);
                Cell currCell = new Cell(tv);
                currCell.setIndex(COLUMN_COUNT * j+i);
                cellArr.add(currCell);
                //adjacentMines.add(0);

//                String print = printCells(cellArr);
//                Log.i("Cell", print);
            }
        }
        System.out.println(printCells(cellArr));
    }
    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cellArr.size(); n++) {
            if (cellArr.get(n).getCellTV() == tv)
                return n;
        }
        return -1;
    }
    //FUNCTION TO INITIALIZE MINES RANDOMLY
    public void placeMines(){
        Random randomGenerator = new Random();
        for (int i = 0; i < 4 ; i++){
            int randInt = randomGenerator.nextInt(120);
            System.out.println(randInt);
            //update cells to mines
            cellArr.get(randInt).setMine(true);
            mines.add(cellArr.get(randInt)); // add mine to mine array
        }
    }

    //FUNCTION TO recalculate adjacent mines for each cell
    public void searchAdjacent(){
        for (int i = 0; i < mines.size(); i++){
            int index = mines.get(i).getIndex();
            int row = index / COLUMN_COUNT;
            int col = index % COLUMN_COUNT;

            for (int r = -1; r <=1; r = r+2){
                for (int c = -1; c <= 1; c = c+2){
                    int currCol = col + c;
                    int currRow = row + r;

                    if (currRow >=0 && currRow < ROW_COUNT&& currCol >=0 && currCol < COLUMN_COUNT ){
                        if (cellArr.get(index).isMine()) {
                            int newIndex = currRow * COLUMN_COUNT+ currCol;
                            int prevMines = cellArr.get(newIndex).getAdjacentMines();
                            cellArr.get(newIndex).setAdjacentMines(prevMines + 1);
                        }
                    }
                }
            }
        }
    }

    public String printCells(ArrayList<Cell> grid){
        String toPrint = "hello";
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                Cell curr = grid.get(i*COLUMN_COUNT+j);
                toPrint = toPrint + Integer.toString(curr.getAdjacentMines()) + ", ";
            }
            toPrint += "\n";
        }
        return toPrint;

    }


    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;
        tv.setText(String.valueOf(i)+String.valueOf(j));
        if (tv.getCurrentTextColor() == Color.GRAY) {
            tv.setTextColor(Color.GREEN);
            tv.setBackgroundColor(Color.parseColor("lime"));
        }else {
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.LTGRAY);
        }
    }
}