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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    /**
     * Add a word to trie data structure for creating a dictionary
     * @param s word to be added
     */
    public void add(String s) {

        // if last character is finished then add isWord tag to indicate word is formed until this character
        if(s.length() == 0){
            this.isWord = true;
            return;
        }

        // if the child does not contain a key then add the key and make a child node for it
        if (!this.children.containsKey(Character.toString(s.charAt(0)))) {
            this.children.put(Character.toString(s.charAt(0)), new TrieNode());
        }

        // add the next character to the child of the one added now
        TrieNode tmp = this.children.get(Character.toString(s.charAt(0)));
        tmp.add(s.substring(1));

    }

    /**
     *
     * @param s string passed as s character
     * @return True if s is a word
     */
    public boolean isWord(String s) {

//        // false if string is null
//        if (s.length() == 0){
//            return false;
//        }

        // check if last character is reached then return value of the isWord label
        //      if the word is formed after reaching this node than is word label will be true
        //      else it will be false
        if(s.length() == 0){
            return this.isWord;
        }

        // if key doesn't exist then word doesn't exist return false
        if(!this.children.containsKey(Character.toString(s.charAt(0)))){
            return false;
        }

        // if key exists move on to the next node in the trie
        TrieNode tmp = this.children.get(Character.toString(s.charAt(0)));

        return tmp.isWord(s.substring(1));
    }


    public String getAnyWordStartingWith(String s) {

        TrieNode currentNode = this;

        // String to store result
        String newString = "";

        // tracing the String s along the trie data structure
        for(int i = 0; i < s.length(); i++){
            // if it is not present in the this child then return null indicating not found
            if(!currentNode.children.containsKey(String.valueOf(s.charAt(i)))){
                return null;
            }
            newString += s.charAt(i);
            currentNode = currentNode.children.get(String.valueOf(s.charAt(i)));
        }

        if(currentNode.children.size() == 0){
            return null;
        }

        // if its the computers turn to provide a starting word
        if(newString.length() == 0) {
            // pick a random letter from the keyset of children to get a letter to start with
            Random random = new Random();
            ArrayList<String> keys = new ArrayList<>(currentNode.children.keySet());
            return keys.get(random.nextInt(keys.size()));
        }

        // Traverse the trie dictionary data structure
        do {
            // search through possible keys for children HashMap
            for (char i = 'a'; i <= 'z'; i++) {
                // if child exists add that character to newString and proceed for finding next element
                if (currentNode.children.containsKey(String.valueOf(i))) {
                    newString += i;
                    currentNode = currentNode.children.get(String.valueOf(i));
                    break;
                }
            }
        } while (!currentNode.isWord);

        // return the newly formed string
        return newString;

    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
