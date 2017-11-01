/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static com.google.engedu.ghost.R.id.gameStatus;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String LOSE = "You lose :( , Please press restart to play a new game :)";
    private static final String WIN = "You win!!, Please press restart to play a new game :)";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private SimpleDictionary sDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            sDictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);

        Button restart = (Button) findViewById(R.id.restart);
        // listen for click on restart button
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {

        // status text view object
        TextView label = (TextView) findViewById(gameStatus);

        int win = 0;

        // computers turn

        TextView text = (TextView) findViewById(R.id.ghostText);
        String wordFragment = text.getText().toString();


        // try to find if the word fragment exists and has length greater than 4
        // this ensures that computer is challenging the user and winning the game if user enter a meaningful word
        if (sDictionary.isWord(wordFragment) && wordFragment.length() >= 4){
                // user loses if he enters a meaningful word
                win = 1;
        }
        else{
            // try to find if such a word Fragment exists in a word in the dictionary
            // if word Fragment is empty the fill it with a random word (computers turn first)
            String anyWordStartingWith = sDictionary.getAnyWordStartingWith(wordFragment);

            // if word Fragment doesn't exist then user is trying to fool the computer
            if(anyWordStartingWith == null){
                // computer wins by declaring than this word doesn't exist
//                Log.d("Turn", "Oout");
                win = 1;

            }
            // if such a word Fragment exists in a word in the dictionary
            else{
                // add next character to the word
                int index = wordFragment.length();
                wordFragment += Character.toString(anyWordStartingWith.charAt(index));
                text.setText(wordFragment);
//                Log.d("Turn", "In");
//                Log.d("Word", anyWordStartingWith);
            }
        }
        Log.d("Turn", "Computer done");
        Log.d("Word", wordFragment);

        // check status of the game
        if(win == 1){
            label.setText(LOSE);
        }
        else{
            userTurn = true;
            label.setText(USER_TURN);
        }

    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        // get which character is pressed
        char pressedKey = (char) event.getUnicodeChar();

        // if key pressed is a alphabet then add to word Fragment
        if((pressedKey >= 'a' && pressedKey <= 'z')|| (pressedKey >= 'A' && pressedKey <= 'Z')){
            TextView text = (TextView) findViewById(R.id.ghostText);
            String wordFragment = text.getText().toString();
            wordFragment += Character.toString(pressedKey);
            text.setText(wordFragment);

//          Log.d("Turn", "Computer called");
            computerTurn();


        }
        // anything other than alphabet then do nothing
        else{
            return super.onKeyUp(keyCode, event);
        }

        return true;
    }
}
