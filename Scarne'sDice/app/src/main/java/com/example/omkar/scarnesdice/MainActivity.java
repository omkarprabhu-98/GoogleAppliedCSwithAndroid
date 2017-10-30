package com.example.omkar.scarnesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int userScore = 0;
    private int compScore = 0;
    private int usesrTurnValue = 0;
    private int compTurnValue = 0;
    private int turn = 0;
    private int diceValue;
    private Random random = new Random();

    ImageView diceImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diceImage = (ImageView) findViewById(R.id.DiceImage);


    }

    public void roll(View v){

        // choose a random dice number
        String diceNo = "dice";
        diceValue = random.nextInt(6) + 1;
        diceNo = diceNo + diceValue;

        // add to image view
        diceImage.setImageResource(this.getResources().getIdentifier(diceNo, "drawable", this.getPackageName()));

        // Toast
        Toast.makeText(getApplicationContext(), "Dice Rolled!", Toast.LENGTH_SHORT).show();

        if(diceValue == 1){
            turn  = ~ turn;
            if(turn == 0){
                usesrTurnValue = 0;
                comptersTurn();
            }
            else{
                compTurnValue = 0;
            }
        }
        else{
            if(turn == 0){
                usesrTurnValue += diceValue;
            }
            else{
                compTurnValue += diceValue;
            }
        }

    }


    public void hold (){

        if(turn  == 0){
           userScore += usesrTurnValue;
        }
        else{
            compTurnValue += compTurnValue;
        }

    }

    public void reset(View v){

        userScore = 0;
        compScore = 0;
        diceImage.setImageResource(this.getResources().getIdentifier("dice1", "drawable", this.getPackageName()));
        usesrTurnValue = 0;
        compTurnValue = 0;
    }

    public  void comptersTurn(){


    }


}
