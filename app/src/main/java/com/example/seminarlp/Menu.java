package com.example.seminarlp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.seminarlp.data.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    private Button btnPlay, btnHighScore, btnAddQuestion;

    ArrayList<Question> questionsList = new ArrayList<>();



    String jsonArrayList;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnPlay = findViewById(R.id.playButton);
        btnPlay.setOnClickListener(this);

        btnAddQuestion=findViewById(R.id.addQuestion);
        btnAddQuestion.setOnClickListener(this);

        btnHighScore=findViewById(R.id.highScoreButton);
        btnHighScore.setOnClickListener(this);


        getQuestions();

        getScores();
    }


    @Override
    public void onClick(View v) {
        if(!questionsList.isEmpty()){
            switch (v.getId()){
                case R.id.playButton:
                    Intent intent = new Intent(this, Quiz.class);
                    intent.putExtra("questions", gson.toJson(questionsList));
                    intent.putExtra("points", "0");
                    //60000 milisekundi je 60 sekundi
                    intent.putExtra("time", "60000");

                    intent.putExtra("answered", "0");
                    startActivity(intent);
                    break;
                case R.id.addQuestion:
                    Intent intent2 = new Intent(this, AddQuestion.class);
                    intent2.putExtra("questions", gson.toJson(questionsList));
                    startActivity(intent2);
                    break;
                case R.id.highScoreButton:
                    if(jsonArrayList != ""){
                        Intent intentScores = new Intent(this,HighScore.class);
                        intentScores.putExtra("highScores", jsonArrayList);
                        startActivity(intentScores);
                    } else{
                        Toast.makeText(this, "User scores are loading", Toast.LENGTH_LONG).show();
                    }
                    break;
            }

        } else{
            Toast.makeText(this, "Questions loading", Toast.LENGTH_LONG).show();
        }

    }

    void getQuestions(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("questions");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Type type = new TypeToken<ArrayList<Question>>() {}.getType();

                String questions = snapshot.getValue().toString();
                questionsList = gson.fromJson(questions, type);
                Collections.shuffle(questionsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getScores(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("finishedGames");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jsonArrayList = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}