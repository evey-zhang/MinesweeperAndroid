package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import java.util.LinkedList;
import java.util.Queue;
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
    private ArrayList<Cell> flaggedMines;
     private boolean flagMode;
    private int flagsLeft = 4;
    private int clock = 0;
    private boolean running = false;
    //CREATE TEXTVIEW OBJECTS FOR IMAGES

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cellArr = new ArrayList<Cell>();
        mines = new ArrayList<Cell>();
        flaggedMines = new ArrayList<Cell>();

        TextView pick = findViewById(R.id.pick);
        TextView flag_bottom = (TextView)findViewById(R.id.flag_bottom);
        flag_bottom.setVisibility(View.INVISIBLE);
        pick.setVisibility((View.VISIBLE));
        flagMode=false;
        pick.setOnClickListener(this::onClickPick);
        flag_bottom.setOnClickListener(this::onClickFlag);


        //make the grid
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                TextView tv = new TextView(this);
                tv.setHeight(dpToPixel(30));
                tv.setWidth(dpToPixel(30));
                tv.setTextSize(15);//dpToPixel(32) )
                tv.setText(String.valueOf(i)+String.valueOf(j));
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.LTGRAY);
                tv.setBackgroundColor(Color.parseColor("lime"));
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);


                grid.addView(tv, lp);
                Cell currCell = new Cell(tv);
                currCell.setIndex(COLUMN_COUNT * i+j);
                //System.out.println(COLUMN_COUNT * i + j);
                cellArr.add(currCell);

            }
        }
        placeMines();
        searchAdjacent();
        running = true;
        runTimer();
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
        //NOTE NEED TO ACCOUNT TO PREVENT TWO SAME MINES
        Random randomGenerator = new Random();
        for (int i = 0; i < 4 ; i++){
            int randInt = randomGenerator.nextInt(120);
            System.out.println(randInt);
            //update cells to mines
            cellArr.get(randInt).setMine(true);
            cellArr.get(randInt).setAdjacentMines(-1);
            mines.add(cellArr.get(randInt)); // add mine to mine array
        }
    }

    //FUNCTION TO recalculate adjacent mines for each cell
    public void searchAdjacent(){
        for (int i = 0; i < mines.size(); i++){
            int index = mines.get(i).getIndex();
            int row = index / COLUMN_COUNT;
            int col = index % COLUMN_COUNT;

            for (int r = -1; r <=1; r = r+1){
                for (int c = -1; c <= 1; c = c+1){
                    int currCol = col + c;
                    int currRow = row + r;

                    if ((currRow >=0 && currRow < ROW_COUNT)&& (currCol >=0 && currCol < COLUMN_COUNT) ){
                        int newIndex = currRow * COLUMN_COUNT+ currCol;
                        if (cellArr.get(index).isMine() && !cellArr.get(newIndex).isMine()) {

                            int prevMines = cellArr.get(newIndex).getAdjacentMines();
                            cellArr.get(newIndex).setAdjacentMines(prevMines + 1);
                        }
                    }
                }
            }

        }
    }

    public String printCells(ArrayList<Cell> grid){
        String toPrint = "";
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                Cell curr = grid.get(i*COLUMN_COUNT+j);
                toPrint = toPrint + Integer.toString(curr.getAdjacentMines()) + ", ";
            }
            toPrint += "\n";
        }
        return toPrint;

    }
    //when non-mine, non
    public void BFScells(Cell clickedCell){
        //input: user-pressed cell
        //search 8 directions from input cell. if cell has no adjacent mines, add to queue to be searched again
        Queue<Cell> cellQueue = new LinkedList<>();
        boolean[] visited = new boolean[cellArr.size()];

        //Queue the first index
        cellQueue.add(clickedCell);
        visited[clickedCell.getIndex()] = true;
        clickedCell.setAlreadyClicked(true);
        clickedCell.getCellTV().setText(String.valueOf(clickedCell.getAdjacentMines()));
        clickedCell.getCellTV().setTextColor(Color.DKGRAY);


        while (cellQueue.isEmpty() == false){
            Cell currCell = cellQueue.remove();
            int index = currCell.getIndex();
            int row = index / COLUMN_COUNT;
            int col = index % COLUMN_COUNT;

            for (int r = -1; r <=1; r = r+1) {
                for (int c = -1; c <= 1; c = c + 1) {
                    int currCol = col + c;
                    int currRow = row + r;
                    int newIndex = currRow * COLUMN_COUNT+ currCol;

                    if (currRow >=0 && currRow < ROW_COUNT && currCol >=0 && currCol < COLUMN_COUNT ) {
                        currCell = cellArr.get(newIndex);
                        //check that element is not a mine and has not been visited
                        if (!currCell.isMine() && visited[newIndex]== false) {
                            //if acell has no adjacent mines
                            if (currCell.getAdjacentMines() == 0){
                                cellQueue.add(currCell);
                            }
                            //mark cell as visited
                            visited[newIndex] = true;
                            currCell.setAlreadyClicked(true);

                            TextView tv = currCell.getCellTV();
                            tv.setText(String.valueOf(currCell.getAdjacentMines()));
                            tv.setTextColor(Color.DKGRAY);
                        }
                        if (!currCell.isMine()){
                            //UNCOVER THIS CELL -> TO REVEAL
                            TextView tv = currCell.getCellTV();
                            tv.setBackgroundColor(Color.LTGRAY);
                            if (currCell.getAdjacentMines() > 0){
                                tv.setText(String.valueOf(currCell.getAdjacentMines()));
                                tv.setTextColor(Color.DKGRAY);
                            }

                        }


//

                        //IF cell has adjacent mines: show adjacent mines -> turn green
                        //if cell does not have adjacent mines, show no numbers -> turn green
                    }
                }
            }
        }
    }
    public void onClickPick(View view){
        TextView pick = (TextView) view;
        TextView flag_bottom = (TextView)findViewById(R.id.flag_bottom);

        pick.setVisibility(View.INVISIBLE);
        flag_bottom.setVisibility(View.VISIBLE);
        flagMode = true;
    }
    public void onClickFlag(View view){
        TextView flag = (TextView) view;
        TextView pick = (TextView)findViewById(R.id.pick);


        pick.setVisibility(View.VISIBLE);
        flag.setVisibility(View.INVISIBLE);
        flagMode = false;
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.time);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = clock%60;
                String time = Integer.toString(seconds);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        TextView flag_number = (TextView)findViewById(R.id.flag_number);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_main2, null);
        TextView gameEnd = layout.findViewById(R.id.GameEnded);

        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;
        Cell currCell = cellArr.get(n);


        //IN PICK MODE:
        if (!flagMode){
            if (currCell.isMine() ){
                //game ends - LOSE
                String message = "Game Ended! You have hit a mine. You lost the game in "
                        + Integer.toString(clock%60)
                        + " seconds.";
                gameEnd.setText(message);
                running = false;

                //CHANGE SCREENS
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);

            }
            else {
                BFScells(cellArr.get(n));
            }
        }
        //In Flag Mode:
        else if (flagMode ){

            if (flagsLeft > 0 && !currCell.isFlagged()){
                tv.setText(R.string.flag);
                if(currCell.isMine()){
                    flaggedMines.add(currCell);
                }
                flagsLeft -= 1;
                flag_number.setText(Integer.toString(flagsLeft));
                currCell.setFlagged(true);

            }
            else if (currCell.isFlagged()){
                tv.setText(" ");
                if(currCell.isMine()){
                    flaggedMines.remove(currCell);
                }
                flagsLeft += 1;
                flag_number.setText(Integer.toString(flagsLeft));
                currCell.setFlagged(false);
            }
            if (flaggedMines.equals(mines)){
                //game ends - WIN
                String message = "Game Ended! You have found the mines. You won the game in "
                        + Integer.toString(clock%60)
                        + " seconds.";
                gameEnd.setText(message);
                running = false;
                //CHANGE SCREENS
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);

            }
        }


//        tv.setText(String.valueOf(i)+String.valueOf(j));
//        if (tv.getCurrentTextColor() == Color.GRAY) {
//            tv.setTextColor(Color.GREEN);
//            tv.setBackgroundColor(Color.parseColor("lime"));
//        }else {
//            tv.setTextColor(Color.GRAY);
//            tv.setBackgroundColor(Color.LTGRAY);
//        }
    }
}