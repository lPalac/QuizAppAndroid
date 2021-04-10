package com.example.seminarlp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarlp.data.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Quiz extends AppCompatActivity implements View.OnClickListener {

    private  Button answer1,answer2,answer3,answer4;
    private TextView question, pointText, timerText;
    ArrayList<Question> questionsList = new ArrayList<>();
    Integer points;
    Integer timer;

    Question currentQuestion;


    Integer answeredQuestion = 0;

    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initElements();

        //Getting information from the menu/previous question
        String json = getIntent().getStringExtra("questions");
        points = Integer.parseInt(getIntent().getStringExtra("points"));
        timer = Integer.parseInt(getIntent().getStringExtra("time"));

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Question>>() {}.getType();
        questionsList = gson.fromJson(json, type);


        setData(answeredQuestion);
        startingTimer(timer);

        initTelephonyManager();


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

        pointText = findViewById(R.id.pointText);
        timerText = findViewById(R.id.timerText);

        question = findViewById(R.id.quizQuestion);
    }

    void setData(Integer index){

        currentQuestion = questionsList.get(index);

        answer1.setText(currentQuestion.getAnswer().get(0));
        answer2.setText(currentQuestion.getAnswer().get(1));
        answer3.setText(currentQuestion.getAnswer().get(2));
        answer4.setText(currentQuestion.getAnswer().get(3));

        question.setText(currentQuestion.getQuestion());

        pointText.setText("Points: " + points.toString());
    }

    void startingTimer(Integer timerTime){
        countDownTimer = new CountDownTimer(timerTime, 1000) {

            public void onTick(long millisUntilFinished) {
                timer = (int)(millisUntilFinished / 1000);
                timerText.setText("seconds remaining: " + timer);
            }
            public void onFinish() {
                if(timer<1){
                    goToFinish();
                }
            }
        }.start();
    }

    void initTelephonyManager(){
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // register PhoneStateListener
        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber)
            {
                // If incoming call received
                if(state==TelephonyManager.CALL_STATE_OFFHOOK)
                {
                    Log.d("INCALL", "INCALL");
                    Log.d("TIMER", "Time left: " + timer);
                    countDownTimer.cancel();
                }
            }
        };
        telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.quizAns1:
                if(currentQuestion.getBoolAnswer().get(0)){
                    points+=1;
                }
                else{
                    points -= 1;
                }
                renewQuestions();

                break;
            case R.id.quizAns2:
                if(currentQuestion.getBoolAnswer().get(1)){
                    points+=1;
                }
                else{
                    points -= 1;
                }
                renewQuestions();

                break;
            case R.id.quizAns3:
                if(currentQuestion.getBoolAnswer().get(2)){
                    points+=1;
                }
                else{
                    points -= 1;
                }
                renewQuestions();

                break;
            case R.id.quizAns4:
                if(currentQuestion.getBoolAnswer().get(3)){
                    points+=1;
                }
                else{
                    points -= 1;
                }
                renewQuestions();
                break;

        }

    }


    void renewQuestions(){
        answeredQuestion++;
        if(questionsList.size()-1==answeredQuestion){
            goToFinish();
        }
        setData(answeredQuestion);
    }

    void goToFinish(){
            Intent intent = new Intent(this, FinishedScreen.class);
            intent.putExtra("points", points.toString());
            intent.putExtra("time", timer.toString());
            startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startingTimer(timer);

    }
}