package com.cst2335.finalassignmenttrivia;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int posCurrentList = 0;
    private TextView score;

    public ProgressBar progressBar;
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    private void init(){
        global = Global.getInstance(this);
        score = (TextView) findViewById(R.id.score);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        score.setText("0");
    }

    public void onAnswerClicked(boolean isCorrect, String difficulty){
        int marks = 0;

        switch(difficulty){
            case "easy":
                marks = 5;
                break;
            case "medium":
                marks = 8;
                break;
            case "hard":
                marks = 12;
                break;
        }

        if(isCorrect){
            global.questionsCorrectly++;
            global.correctAnswer(marks);
            String scoreString = Integer.toString(global.getScore());
            score.setText(scoreString);
        }
        nextQuestion();
    }

    public void nextQuestion(){
        posCurrentList++;
        global.questionsEncountered++;

        if(posCurrentList>=global.currentList.size()){
            if(global.nextList.size()>0){
                global.currentList.clear();
                global.currentList.addAll(global.nextList);
                global.loadNextList();

                posCurrentList = 0;
            }else{
                global.gameOver(this);
            }
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



}