//Homework 3
//Questions.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import java.util.ArrayList;

/**
 * Created by chandra on 9/25/2015.
 */
public class Questions {
    int id;
    String question;
    ArrayList<String> possible_answers;
    String url;

    public Questions(int id,String question, ArrayList<String> possible_answers, String url) {
        this.id=id;
        this.question = question;
        this.possible_answers = possible_answers;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getPossible_answers() {
        return possible_answers;
    }

    public void setPossible_answers(ArrayList<String> possible_answers) {
        this.possible_answers = possible_answers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Questions(){

    }

    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", possible_answers=" + possible_answers +
                ", url='" + url + '\'' +
                '}';
    }
}
