package com.cst2335.finalassignmenttrivia;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar loader;
    private Global global;
    private LinearLayout buttonLayout;
    private TextView highscore;
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sharedPreferences = getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE);
        global = Global.getInstance(getApplication());
        global.reset();
        getView();

        listView = (ListView)findViewById(R.id.listView);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("General Knowledge");
        arrayList.add("Movies");
        arrayList.add("Celebrities");
        arrayList.add("Books");
        arrayList.add("Music");
        arrayList.add("TV");
        arrayList.add("Art");
        arrayList.add("Video Games");
        arrayList.add("Board Games");
        arrayList.add("Computers");
        arrayList.add("Gadgets");
        arrayList.add("Math");
        arrayList.add("Nature");
        arrayList.add("Animals");
        arrayList.add("Greek");
        arrayList.add("History");
        arrayList.add("Politics");
        arrayList.add("Geography");
        arrayList.add("Vehicles");
        arrayList.add("Sports");
        arrayList.add("Comics");
        arrayList.add("Anime");
        arrayList.add("Cartoons");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

    }

    public void displayAlert(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Instructions");
        String[] directions = {"1. Welcome", "2. Choose a category to proceed", "3. Enjoy your game!"};
        alert.setItems(directions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                    case 1:
                    case 2:

                }
            }
        });
        alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Snackbar snackbar = Snackbar.make(buttonLayout, "Enjoy your game! Best of luck!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        alert.create().show();
    }

    public void displayToast(View v) {
        Toast.makeText(this, "Made By: Marko", Toast.LENGTH_SHORT).show();
    }

    private void getView(){
        loader = findViewById(R.id.loader);
        buttonLayout = findViewById(R.id.button_layout);

        highscore = findViewById(R.id.score);
        highscore.setTag("highscore");
        highscore.setOnClickListener(this);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_window);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if(sharedPreferences.contains(Config.SCORE)){
            highscore.setVisibility(View.VISIBLE);
        }else{
            highscore.setVisibility(View.GONE);
        }

    }

    private void getCurrentList(final View v){
        final TextView textView = (TextView) v;
        loader.setVisibility(View.VISIBLE);

        String[] random = {"easy", "medium", "hard"};
        int pos = new Random().nextInt(3);

        global.getApiService().getQuestion("5", global.getChosenCategory(), random[pos], "multiple").enqueue(new Callback<TriviaList>() {
            @Override
            public void onResponse(Call<TriviaList> call, Response<TriviaList> response) {
                if(response.isSuccessful()){
                    TriviaList list = response.body();
                    List<Trivia> TList = list.getResults();

                    if(list.getResults().isEmpty()){
                        getCurrentList(v);
                    }else{
                        global.currentList.clear();
                        global.currentList.addAll(TList);
                        global.loadNextList();
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(StartActivity.this, "Network Timeout", Toast.LENGTH_LONG).show();
                }
                loader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<TriviaList> call, Throwable t) {
                Toast.makeText(StartActivity.this, "Network Timeout", Toast.LENGTH_LONG).show();
                loader.setVisibility(View.INVISIBLE);
                enableAll(true);
            }
        });
    }

    @Override
    public void onClick(View view) {
        TextView textView = (TextView) view;
        String text = view.getTag().toString();
        boolean b_high_score_clicked = false;

        switch(text){
            case"highscore":
                showDialogWindow(view);
                b_high_score_clicked = true;
                break;
            case "gk":
                global.chosenCategory = Config.GENERAL;
                break;
            case "films":
                global.chosenCategory = Config.FILMS;
                break;
            case "celebrities":
                global.chosenCategory = Config.CELEBERITIES;
                break;
            case "books":
                global.chosenCategory = Config.BOOKS;
                break;
            case "music":
                global.chosenCategory = Config.MUSIC;
                break;
            case "tv":
                global.chosenCategory = Config.TV;
                break;
            case "art":
                global.chosenCategory = Config.ART;
                break;
            case "video_games":
                global.chosenCategory = Config.VIDEOGAMES;
                break;
            case "board_games":
                global.chosenCategory = Config.BOARD_GAMES;
                break;
            case "computers":
                global.chosenCategory = Config.COMPUTERS;
                break;
            case "gadgets":
                global.chosenCategory = Config.GADGETS;
                break;
            case "mathematics":
                global.chosenCategory = Config.MATH;
                break;
            case "nature":
                global.chosenCategory = Config.NATURE;
                break;
            case "animals":
                global.chosenCategory = Config.ANIMALS;
                break;
            case "mythology":
                global.chosenCategory = Config.GREEK;
                break;
            case "history":
                global.chosenCategory = Config.HISTORY;
                break;
            case "politics":
                global.chosenCategory = Config.POLITICS;
                break;
            case "geography":
                global.chosenCategory = Config.GEOGRAPHY;
                break;
            case "vehicles":
                global.chosenCategory = Config.VEHICLES;
                break;
            case "sports":
                global.chosenCategory = Config.SPORTS;
                break;
            case "comics":
                global.chosenCategory = Config.COMICS;
                break;
            case "anime":
                global.chosenCategory = Config.ANIME;
                break;
            case "cartoons":
                global.chosenCategory = Config.CARTOON;
                break;
        }

        if(global.networkAvailable()){
            enableAll(false);
            getCurrentList(view);
        }else{
            Toast.makeText(this, "Network Timeout", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableAll(boolean v){
        for(int i=0;i<buttonLayout.getChildCount();i++){
            ViewGroup child = (ViewGroup) buttonLayout.getChildAt(i);
            for (int q=0;q<child.getChildCount();q++){
                child.getChildAt(q).setEnabled(v);
            }
        }
    }

    private void showDialogWindow(View v){
        final TextView textView = (TextView) v;
        TextView title, score, trivia, correct, totalQuestions;
        Button exitButton;

        int marks = sharedPreferences.getInt(Config.SCORE, -1);
        int questionsAnsweredCorrectly = sharedPreferences.getInt(Config.ANSWERED, 0);
        int questionsGiven = sharedPreferences.getInt(Config.QGIVEN, 1);

        String wisdom = sharedPreferences.getString(Config.WISDOM, Config.ERROR);
        String category = sharedPreferences.getString(Config.CATEGORY, Config.HIGH_SCORE);

        score = dialog.findViewById(R.id.exit_score);
        trivia = dialog.findViewById(R.id.exit_trivia);
        exitButton = dialog.findViewById(R.id.exit_button);
        correct = dialog.findViewById(R.id.correct_number);
        totalQuestions = dialog.findViewById(R.id.total_questions);

        String correct_string = Integer.toString(questionsAnsweredCorrectly);
        String total_String = Integer.toString(questionsGiven);
        String score_string = Integer.toString(marks);

        correct.setText(correct_string);
        totalQuestions.setText(total_String);
        score.setText(total_String);
        trivia.setText(wisdom);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
