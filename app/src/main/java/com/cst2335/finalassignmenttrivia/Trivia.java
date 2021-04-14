package com.cst2335.finalassignmenttrivia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trivia {

    @SerializedName("Category")
    @Expose
    private String category;

    @SerializedName("questionType")
    @Expose
    private String questionType;

    @SerializedName("difficulty")
    @Expose
    private String difficulty;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("correctAnswer")
    @Expose
    private String correctAnswer;

    @SerializedName("incorrectAnswer")
    @Expose
    private List<String> incorrectAnswer = null;

    public String getCategory(){
        return category;
    }

    public String getQuestionType(){
        return questionType;
    }

    public String getDifficulty(){
        return difficulty;
    }

    public String getQuestion(){
        return question;
    }

    public String getCorrectAnswer(){
        return correctAnswer;
    }

    public List<String> getIncorrectAnswer(){
        return incorrectAnswer;
    }



}
