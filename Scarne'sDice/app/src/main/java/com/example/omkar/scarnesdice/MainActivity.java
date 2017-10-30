package com.example.omkar.scarnesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Data varibles
    private int userScore = 0;
    private int compScore = 0;
    private int userTurnValue = 0;
    private int compTurnValue = 0;
    private int diceValue;
    private Random random = new Random();

    // View objects
    ImageView diceImage;
    Button hold;
    Button roll;
    Button reset;
    TextView userScoreText;
    TextView compScoreText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // refer objects to view objects
        diceImage = (ImageView) findViewById(R.id.DiceImage);
        roll = (Button) findViewById(R.id.roll);
        hold = (Button) findViewById(R.id.hold);
        reset = (Button) findViewById(R.id.reset);
        compScoreText = (TextView) findViewById(R.id.compScoreText);
        userScoreText = (TextView) findViewById(R.id.userScoreText);

        // Button click listeners
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll();
            }
        });
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

    }

    /**
     * Roll the dice
     */
    public void roll(){

        // choose a random dice number
        String diceNo = "dice";
        diceValue = random.nextInt(6) + 1;
        diceNo = diceNo + diceValue;

        // add to image view
        diceImage.setImageResource(this.getResources().getIdentifier(diceNo, "drawable", this.getPackageName()));

        // check whose turn it is
        if(diceValue == 1){
            userTurnValue = 0;
            computersTurn();
        }
        else{
            userTurnValue += diceValue;
        }


    }


    /**
     * Hold the turn value and give the turn to the other player
     */
    public void hold(){

        userScore += userTurnValue;
        userScoreText.setText(String.valueOf(userScore));
        userTurnValue = 0;
        computersTurn();


        if(userScore >= 100){
            // Winning message
            Toast.makeText(getApplicationContext(), "GAME OVER YOU WON!", Toast.LENGTH_LONG).show();
            reset();
        }
        else if(compScore >= 100){
            // Winning message
            Toast.makeText(getApplicationContext(), "GAME OVER YOU LOOSE!", Toast.LENGTH_LONG).show();
            reset();
        }

    }

    /**
     * Reset the game
     */
    public void reset(){

        userScore = 0;
        compScore = 0;
        diceImage.setImageResource(this.getResources().getIdentifier("dice1", "drawable", this.getPackageName()));
        userTurnValue = 0;
        compTurnValue = 0;
        compScoreText.setText("0");
        userScoreText.setText("0");
    }


    /**
     * Implements computer's turn in the game
     */
    public void computersTurn() {

        // diable buttons
        roll.setEnabled(false);
        hold.setEnabled(false);

        int randomTimesRoll = random.nextInt(5);
        int i = 0;
        while (i <= randomTimesRoll){

            diceValue = random.nextInt(6) + 1;

            if(diceValue == 1){

                compTurnValue = 0;
                break;
            }
            else{
                compTurnValue += diceValue;
            }

            i++;
        }

        String diceNo = "dice";
        diceNo = diceNo + diceValue;
        // add to image view
        diceImage.setImageResource(this.getResources().getIdentifier(diceNo, "drawable", this.getPackageName()));

        compScore += compTurnValue;
        compScoreText.setText(String.valueOf(compScore));
        compTurnValue = 0;

        // enable button as turn is over
        roll.setEnabled(true);
        hold.setEnabled(true);

    }


}
