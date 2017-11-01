package com.example.omkar.scarnesdice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    // Data variables for the game
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

    // handller for adding delay
    private Handler handler = new Handler();


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

        // check is dice is 1
        if(diceValue == 1){
            userTurnValue = 0;
            // wait for s1 sec and then call computers Turn
            Toast.makeText(getApplicationContext(), "You rolled 1! No points added", Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computersTurn();
                }
            }, 1000);
        }
        else{
            // add diceValue to userTurn
            userTurnValue += diceValue;
        }


    }


    /**
     * Hold the turn value and give the turn to the other player
     */
    public void hold(){

        // hold value (add userTurn value to userScore) and switch to computer turn
        userScore += userTurnValue;
        userScoreText.setText(String.valueOf(userScore));
        userTurnValue = 0;

        // check if user has points greater than 100
        if(checkGameStatus() == 0){
            // wait for 1 sec and call computers turn
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computersTurn();
                }
            }, 1000);
        }

    }

    /**
     * Checks if players score have crossed 100 points
     */
    public int checkGameStatus(){
        if(userScore >= 100){
            // Winning message
            Toast.makeText(getApplicationContext(), "GAME OVER YOU WON!", Toast.LENGTH_LONG).show();
            reset();
            return 1;
        }
        else if(compScore >= 100){
            // Winning message
            Toast.makeText(getApplicationContext(), "GAME OVER YOU LOOSE!", Toast.LENGTH_LONG).show();
            reset();
            return 1;
        }
        return 0;
    }


    /**
     * Reset the game
     */
    public void reset(){
        // reset game variables
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

        // disable buttons as computers turn now
        roll.setEnabled(false);
        hold.setEnabled(false);

        // computer rolls and hold
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

        // get dice no
        String diceNo = "dice";
        diceNo = diceNo + diceValue;
        // add to image view
        diceImage.setImageResource(this.getResources().getIdentifier(diceNo, "drawable", this.getPackageName()));

        compScore += compTurnValue;
        compScoreText.setText(String.valueOf(compScore));
        compTurnValue = 0;

        checkGameStatus();

        // enable button as turn is over
        roll.setEnabled(true);
        hold.setEnabled(true);

    }


}
