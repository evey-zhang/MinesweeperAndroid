package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Random;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //INSTANTIATION OF VARIABLES
    private static final int COLUMN_COUNT = 10;

    private ArrayList<TextView> cell_tvs;
    private ArrayList<Integer> adjacentMines; // number of mines next to square
    private ArrayList<Cell> cellArr;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //array to store each cell
        cell_tvs = new ArrayList<TextView>();
        cellArr = new ArrayList<Cell>();
        adjacentMines = new ArrayList<Integer>();

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
                cellArr.add(currCell);
                adjacentMines.add(0);
            }
        }
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
        }
    }

    //FUNCTION TO recalculate adjacent mines for each cell
    public void searchAdjacent(){
        for (int i = 0; i < cellArr.size(); i++){
            int row = i / COLUMN_COUNT;
            int col = i % COLUMN_COUNT;

            for (int )

        }


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