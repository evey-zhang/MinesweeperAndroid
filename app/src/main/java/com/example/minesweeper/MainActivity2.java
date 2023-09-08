package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView startAgainButton= (TextView)findViewById(R.id.playAgain);
        startAgainButton.setOnClickListener(this::onClickStartAgain);
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.activity_main2, null);
    }
    public void onClickStartAgain(View view){
        TextView startAgainButton= (TextView)findViewById(R.id.playAgain);
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }
}