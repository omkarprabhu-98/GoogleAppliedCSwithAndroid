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

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

public class StackedLayout extends LinearLayout {

    private Stack<View> tiles = new Stack();

    public StackedLayout(Context context) {
        super(context);
    }

    public void push(View tile) {
        // hide top tile view
        if(!tiles.empty()){
            removeView(tiles.peek());
        }
        // push new tile into tiles stack
        tiles.push(tile);
        // add this tile into view
        addView(tiles.peek());
    }

    public View pop() {
        // popped view
        View popped = null;

        // pop tiles stack
        if(!tiles.empty()){
            // remove that tile from view
            removeView(tiles.peek());
            tiles.pop();
        }

        // add tile at the top of tiles stack to the view
        if(!tiles.empty()){
            addView(tiles.peek());
        }

        return popped;
    }

    public View peek() {
        return tiles.peek();
    }

    public boolean empty() {
        return tiles.empty();
    }

    public void clear() {
        // cleared tiles stack and removed all views of each tile
        while(!tiles.empty()){
            removeView(tiles.peek());
            tiles.pop();
        }
    }
}
