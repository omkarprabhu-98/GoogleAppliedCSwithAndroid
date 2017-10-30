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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static com.google.engedu.ghost.R.id.gameStatus;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
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
        TextView label = (TextView) findViewById(gameStatus);

        // Do computer turn stuff then make it the user's turn again
        TextView text = (TextView) findViewById(R.id.ghostText);
        String wordFragment = text.getText().toString();
        if (sDictionary.isWord(wordFragment)){
            if(wordFragment.length() >= 4){
                label.setText("YOU WIN!");
            }
        }
        else{
            String anyWordStartingWith = sDictionary.getAnyWordStartingWith(wordFragment);
            if(anyWordStartingWith == null){
                label.setText("YOU LOSE!");
            }
            else{
                int index = wordFragment.length();
                wordFragment += Character.toString(anyWordStartingWith.charAt(index));
                text.setText(wordFragment);
            }
        }

        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        char pressedKey = (char) event.getUnicodeChar();

        if((pressedKey >= 'a' && pressedKey <= 'z')|| (pressedKey >= 'A' && pressedKey <= 'Z')){
            TextView text = (TextView) findViewById(R.id.ghostText);
            String wordFragment = text.getText().toString();
            wordFragment += Character.toString(pressedKey);
            text.setText(wordFragment);
            if (sDictionary.isWord(wordFragment)){
                TextView gameStatus = (TextView) findViewById(R.id.gameStatus);
                gameStatus.setText("GAME OVER! YOU LOSE");
            }
            else{
                computerTurn();
            }



        }
        else{
            return super.onKeyUp(keyCode, event);
        }



        return true;
    }
}
