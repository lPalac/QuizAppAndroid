package com.example.seminarlp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seminarlp.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FinishedScreen extends AppCompatActivity {

    TextView pointsText, timerText, congrats;
    Button btnSave, btnHome;
    ArrayList<User> finishedGameList = new ArrayList<>();


    Integer points;
    Integer timer;

    DatabaseReference db;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_screen);


        points = Integer.parseInt(getIntent().getStringExtra("points"));
        timer = Integer.parseInt(getIntent().getStringExtra("time"));


        initElements();

        gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>() {}.getType();

        db = FirebaseDatabase.getInstance().getReference().child("finishedGames");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String finishedGames = snapshot.getValue().toString();
                finishedGameList = gson.fromJson(finishedGames, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    void goHome(){
        startActivity(new Intent(this, Menu.class));
    }

    void initElements(){
        pointsText = findViewById(R.id.finalScore);
        pointsText.setText("You have: " + points.toString());

        timerText = findViewById(R.id.finalTime);
        timerText.setText("You have: " + timer.toString());

        congrats = findViewById(R.id.congrat);
        runAnimation();

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
                goHome();
            }
        });
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
    }

    void saveScore(){

        User user = new User();

        EditText name = findViewById(R.id.editTextName);

        user.setName(name.getText().toString());
        user.setPoints(Integer.parseInt(points.toString()));
        for(int i = 0;i<finishedGameList.size();i++){
            if(finishedGameList.get(i).getPoints() < user.getPoints()){
                finishedGameList.add(i, user);
            }
        }
        if(!finishedGameList.contains(user)){
            finishedGameList.add(user);
        }
        if(finishedGameList.size()>=10){
            ArrayList<User> tempList = new ArrayList<>();
            for(int i=0; i<finishedGameList.size();i++){
                tempList.add(finishedGameList.get(i));
            }

            finishedGameList = tempList;
        }

        String json = gson.toJson(finishedGameList);
        db.setValue(json);
    }
    private void runAnimation()
    {
        congrats.startAnimation(AnimationUtils.loadAnimation(FinishedScreen.this, R.anim.scale));
    }
}