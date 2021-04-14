package com.cst2335.finalassignmenttrivia;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.security.auth.callback.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class Global {

    private static Global instance;
    private static ApiHelper APIService;
    private static NumberService NumberService;
    private Context context;
    private String funNumberTrivia;
    private int score = 0;
    private boolean isGameOver = false;
    private ProgressBar progressBar;

    public List<Trivia> currentList;
    public List<Trivia> nextList;
    public int questionsCorrectly = 0;
    public int questionsEncountered = 1;
    public String chosenCategory;

    private Global(Context c){
        context = c;
        APIService = ApiUrl.getAPIService();
        currentList = new ArrayList<>();
        nextList = new ArrayList<>();
    }

    public static synchronized Global getInstance(Context c){
        if(instance==null){
            instance = new Global(c);
        }
        return instance;
    }

    public ApiHelper getApiService(){
        return APIService;
    }

    public NumberService getNumberService(){
        return NumberService;
    }

    public String getFunNumberTrivia(){
        return funNumberTrivia;
    }

    public void correctAnswer(int answer){
        score+=answer;
    }

    public int getScore(){
        return score;
    }

    public boolean getGameOver(){
        return isGameOver;
    }

    public void reset(){
        score = 0;
        questionsEncountered = 1;
        questionsCorrectly = 0;
        isGameOver = false;
    }

    public boolean networkAvailable(){
        NetworkInfo netInfo = (NetworkInfo) ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(netInfo==null){
            return false;
        }else{
            return netInfo.isConnected();
        }
    }

    public void loadNextList(){
        if(networkAvailable()){
            String[] list = {"easy", "medium", "hard"};
            int pos = new Random().nextInt(3);

            getApiService().getQuestion("5", chosenCategory, list[pos], "multiple").enqueue(new retrofit2.Callback<TriviaList>() {
                @Override
                public void onResponse(Call<TriviaList> call, Response<TriviaList> response) {
                    TriviaList list = response.body();
                    if(!list.getResults().isEmpty()) {
                        nextList.clear();
                        nextList.addAll(list.getResults());
                    }
                    else{
                        loadNextList();
                    }
                }

                @Override
                public void onFailure(Call<TriviaList> call, Throwable t) {
                    nextList.clear();
                }
            });

        }else{
            nextList.clear();
            Toast.makeText(context, "Network Timeout", Toast.LENGTH_LONG).show();
        }
    }

    public void loadTrivia(final Context c){
        if(networkAvailable()){
            String scoreString = Integer.toString(score);

            String [] random = {"math", "trivia", "date", "year"};
            int pos = new Random().nextInt(4);

            getNumberService().getStringResponse("/"+scoreString+"/"+random[pos]).enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        funNumberTrivia = response.body();
                        showGameOver(context, true, null);
                    }else{
                        showGameOver(context, false, Config.ERROR);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    showGameOver(context, false, Config.ERROR);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }

    public void gameOver(final Context c){
        isGameOver = true;

        progressBar = ((MainActivity) c).progressBar;
        progressBar.setIndeterminate(true);

        if(networkAvailable()){
            loadTrivia(c);
        }else{
            showGameOver(c, false, Config.ERROR);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void showGameOver(final Context context, final boolean triviaLoad, final String anyError){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.exit_window);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("You Lost!");

        TextView score, trivia, correctNumber, totalQuestions;
        Button exitButton;

        score = dialog.findViewById(R.id.exit_score);
        trivia = dialog.findViewById(R.id.exit_trivia);
        exitButton = dialog.findViewById(R.id.exit_button);
        correctNumber = dialog.findViewById(R.id.correct_number);
        totalQuestions = dialog.findViewById(R.id.total_questions);

        String correctString = Integer.toString(questionsCorrectly);
        String totalString = Integer.toString(questionsEncountered);
        String scoreString = Integer.toString(getScore());

        if(triviaLoad){
            trivia.setText((getFunNumberTrivia()));
        }else{
            if(anyError == null){
                trivia.setText(Config.ERROR);
            }else {
                trivia.setText(anyError);
            }
        }

        exitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SharedPreferences shared = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = shared.edit();
                int sharedScore = shared.getInt(Config.SCORE, -1);

                if(getScore()>sharedScore){
                    edit.putInt(Config.SCORE, getScore());
                    edit.putInt(Config.ANSWERED, questionsCorrectly);
                    edit.putInt(Config.QGIVEN, questionsEncountered);
                    if(triviaLoad){
                        edit.putString(Config.WISDOM, getFunNumberTrivia());
                    }else{
                        if(anyError == null){
                            edit.putString(Config.WISDOM, Config.ERROR);
                        }else{
                            edit.putString(Config.WISDOM, anyError);
                        }
                        String category = getChosenCategory();
                        edit.putString(Config.CATEGORY, category);
                        edit.apply();
                    }
                }
                ((MainActivity)context).onBackPressed();

            }
        });
        dialog.show();
    }

    public String getChosenCategory(){
        String cat = "";
        switch(chosenCategory){
            case Config.GENERAL:
                cat = "General";
                break;
                case Config.BOOKS:
                cat = "Books";
                break;
                case Config.FILMS:
                cat = "Movies";
                break;
                case Config.MUSIC:
                cat = "Music";
                break;
                case Config.TV:
                cat = "TV";
                break;
                case Config.VIDEOGAMES:
                cat = "Video Games";
                break;
                case Config.BOARD_GAMES:
                cat = "Board Games";
                break;
                case Config.NATURE:
                cat = "Nature";
                break;
                case Config.COMPUTERS:
                cat = "Computers";
                break;
                case Config.MATH:
                cat = "Math";
                break;
                case Config.GREEK:
                cat = "Mythology";
                break;
                case Config.SPORTS:
                cat = "Sports";
                break;
                case Config.GEOGRAPHY:
                cat = "Geography";
                break;
                case Config.HISTORY:
                cat = "History";
                break;
                case Config.POLITICS:
                cat = "Politics";
                break;
                case Config.ART:
                cat = "Art";
                break;
                case Config.CELEBERITIES:
                cat = "Celeberities";
                break;
                case Config.ANIMALS:
                cat = "Animals";
                break;
                case Config.VEHICLES:
                cat = "Vehicles";
                break;
                case Config.COMICS:
                cat = "Comics";
                break;
                case Config.GADGETS:
                cat = "Gadgets";
                break;
                case Config.ANIME:
                cat = "Anime";
                break;
                case Config.CARTOON:
                cat = "Cartoons";
                break;

        }
        return cat;
    }

}
