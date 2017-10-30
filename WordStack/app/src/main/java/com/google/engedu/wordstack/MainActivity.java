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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private Stack<View> placedTiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length() <= WORD_LENGTH){
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
//        word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
//        word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    // add tile to placed tiles stack for undo action
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");

        // clear word1 layout
        View word1LinearLayout = findViewById(R.id.word1);
        if(((LinearLayout) word1LinearLayout).getChildCount() > 0)
            ((LinearLayout) word1LinearLayout).removeAllViews();

        // clear word2 layout
        View word2LinearLayout = findViewById(R.id.word2);
        if(((LinearLayout) word2LinearLayout).getChildCount() > 0)
            ((LinearLayout) word2LinearLayout).removeAllViews();

        // clear stackLayout
        stackedLayout.clear();

        //placed tiles stack
        placedTiles = new Stack<>();

        // get 2 words at random
        int wordsLength = words.size();
        word1 = words.get(random.nextInt(wordsLength));
        char[] cword1 = word1.toCharArray();
        word2 = words.get(random.nextInt(wordsLength));
        char[] cword2 = word2.toCharArray();
        int len1 = word1.length();
        int len2 =  word2.length();

        char[] cscString = new char[len1 + len2];


        Log.d("Words", word1);
        Log.d("Words", word2);

        // scramble words

        int i = 0, j = 0, k = 0;
        while(i < len1 && j < len2){
            int randomPick = random.nextInt(2);
            if(randomPick == 0){
                cscString[k++] = cword1[i];
                i++;
            }
            else{
                cscString[k++] = cword2[j];
                j++;
            }
        }
        while(i < len1){
            cscString[k++] = cword1[i];
            i++;
        }
        while(j < len2){
            cscString[k++] = cword2[j];
            j++;
        }

        // scrambled word
        String scString = String.valueOf(cscString);
        messageBox.setText(scString);

        // add tile object to stackedLayout
        k--;
        while(k >= 0){
            stackedLayout.push(new LetterTile(MainActivity.this, cscString[k]));
            k--;
        }

        return true;
    }


    public boolean onUndo(View view) {
        // check if placed tile is not empty
        if(!placedTiles.empty()){
            // pop the last tile from it
            View tile = placedTiles.pop();
            // typecast to letter tile object
            LetterTile tileUndo = (LetterTile) tile;
            // move to stacked layout view group
            tileUndo.moveToViewGroup(stackedLayout);
        }

        return true;
    }
}
