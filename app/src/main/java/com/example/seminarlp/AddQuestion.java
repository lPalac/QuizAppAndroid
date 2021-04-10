package com.example.seminarlp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.seminarlp.data.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddQuestion extends AppCompatActivity implements View.OnClickListener {

    private EditText answer1,answer2,answer3,answer4,questionText;
    private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    private Button add;
    ArrayList<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        initElements();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Question>>() {}.getType();

        String questionsString = getIntent().getStringExtra("questions");
        questions = gson.fromJson(questionsString, type);

    }

    void initElements(){

        answer1 = findViewById(R.id.quizAns1);
        answer1.setOnClickListener(this);
        answer2 = findViewById(R.id.quizAns2);
        answer2.setOnClickListener(this);
        answer3 = findViewById(R.id.quizAns3);
        answer3.setOnClickListener(this);
        answer4 = findViewById(R.id.quizAns4);
        answer4.setOnClickListener(this);
        questionText = findViewById(R.id.quizQuestion);

        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        add = findViewById(R.id.btnAddQuestion);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddQuestion:
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("questions");
                questions.add(getInfo());
                Gson gson = new Gson();
                String json = gson.toJson(questions);

                db.setValue(json);
                startActivity(new Intent(this, Menu.class));
                break;
        }
    }

    Question getInfo() {
        Question question = new Question();
        question.setQuestion(questionText.getText().toString());
        ArrayList<String> answers = new ArrayList<>();

        answers.add(answer1.getText().toString());
        answers.add(answer2.getText().toString());
        answers.add(answer3.getText().toString());
        answers.add(answer4.getText().toString());
        question.setAnswer(answers);

        ArrayList<Boolean> correctAns = new ArrayList<>();
        correctAns.add(radioButton1.isChecked());
        correctAns.add(radioButton2.isChecked());
        correctAns.add(radioButton3.isChecked());
        correctAns.add(radioButton4.isChecked());

        question.setBoolAnswer(correctAns);

        return question;
    }
}