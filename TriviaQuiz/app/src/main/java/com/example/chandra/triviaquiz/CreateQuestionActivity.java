//Homework 3
//CreateQuestionActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.io.FileNotFoundException;

import java.io.InputStream;

import java.util.ArrayList;


public class CreateQuestionActivity extends AppCompatActivity implements CreateQuestions.Creation, UploadImage.Upload {

    ImageView image;
    int count = 1;
    private static final int SELECT_PHOTO = 100;
    String result;
    EditText options;
    EditText question;
    RadioGroup rg;
    ArrayList<String> options_list = new ArrayList<String>();
    int index;
    boolean image_change = false;
    ProgressDialog dialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        image = (ImageView) findViewById(R.id.image);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        options = (EditText) findViewById(R.id.options);
        question = (EditText) findViewById(R.id.questionText);
    }


    public void addOption(View v) {

        if (options.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), TriviaAppConstants.OPTION_LABEL, Toast.LENGTH_LONG).show();
            return;
        } else {

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setTextSize(20);

            radioButton.setText(options.getText());
            options_list.add(options.getText().toString());
            radioButton.setLayoutParams(layoutParams);
            rg.addView(radioButton);
            if (count == 1) {
                radioButton.setChecked(true);
                count++;
            }
            options.setText("");
        }

    }

    public void onSubmit(View v) {
        String url = "";
        if (validateQuestion()) {

            for (int i = 0; i < rg.getChildCount(); i++) {
                if (((RadioButton) rg.getChildAt(i)).isChecked()) {
                    index = i;
                    break;
                } else {
                    continue;
                }
            }

            if (image_change) {
                dialog1 = new ProgressDialog(this);
                dialog1.setMessage("Sending Image and Question..");
                dialog1.setCancelable(false);
                dialog1.show();

                new UploadImage(this).execute(result);

            } else {
                url = "";
                RequestParams params = new RequestParams(TriviaAppConstants.SAVE_LINK, TriviaAppConstants.METHOD_POST);
                params.addParams(TriviaAppConstants.GROUP_LABEL, TriviaAppConstants.GROUP_VALUE);
                params.addParams(TriviaAppConstants.QUESTION_LABEL, params.addQuestion(question.getText().toString(), options_list, url, index));
                new CreateQuestions(this).execute(params);
            }


        } else {
            Toast.makeText(getApplicationContext(), TriviaAppConstants.CREATE_ERROR, Toast.LENGTH_LONG).show();
            return;
        }

    }

    public void uploadImage(View v) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    getRealPathFromURI(selectedImage);
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(yourSelectedImage);
                    image_change = true;
                }
        }
    }

    private void getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(column_index);
        cursor.close();
    }


    public boolean validateQuestion() {

        if (question.getText().length() == 0 || rg.getChildCount() < 2) {
            return false;
        }
        return true;
    }


    @Override
    public void sendStatus(Integer i) {
        dialog1.dismiss();
        Log.d("resultofcreation", i + "");

        finish();
    }


    @Override
    public void sendURL(String url) {

        RequestParams params = new RequestParams(TriviaAppConstants.SAVE_LINK, TriviaAppConstants.METHOD_POST);
        params.addParams(TriviaAppConstants.GROUP_LABEL, TriviaAppConstants.GROUP_VALUE);
        params.addParams(TriviaAppConstants.QUESTION_LABEL, params.addQuestion(question.getText().toString(), options_list, url, index));
        new CreateQuestions(this).execute(params);
    }
}
