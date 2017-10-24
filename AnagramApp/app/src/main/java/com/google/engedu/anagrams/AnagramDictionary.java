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

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    // data structures initialized
    private ArrayList<String> wordList;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashSet<String> wordSet;
    private HashMap<Integer, ArrayList<String>> sizeToWords;

    private int wordLength = DEFAULT_WORD_LENGTH;
    private int count = 0;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        // objects for data structures created
        wordList = new ArrayList<String>();
        lettersToWord = new HashMap<String, ArrayList<String>>();
        wordSet = new HashSet<String>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            // fill wordList
            wordList.add(word);
            // fill wordSet
            wordSet.add(word);

            // fill the Hash Map for storing all permutations of all words
            ArrayList<String> tmpList = new ArrayList<>();
            String sortedWord = sortLetters(word);
            if(lettersToWord.containsKey(sortedWord)){
                tmpList = lettersToWord.get(sortedWord);
                tmpList.add(word);
                lettersToWord.put(sortedWord, tmpList);
            }
            else{
                tmpList.add(word);
                lettersToWord.put(sortedWord, tmpList);
            }

            // fill the Hash Map for storing array of words of a their length as key
            ArrayList<String> tmpList1 = new ArrayList<>();
            int i = word.length();

            if(sizeToWords.containsKey(i)){
                tmpList1 = lettersToWord.get(sortedWord);
                tmpList1.add(word);
                sizeToWords.put(i, tmpList1);
            }
            else{
                tmpList1.add(word);
                sizeToWords.put(i, tmpList1);
            }

        }
    }

    // Check if string entered is valid
    // 1. It is present in the dictionary
    // 2. It is not word + one character at beginning or end
    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word) && !(base.contains(word))) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        return result;
    }

    // get a arrayList of anagrams for the word
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        ArrayList<String> anagramsWithOneMoreLetter = new ArrayList<String>();

//        Log.d("Tag", "IN");
        // generate anagrams list
        for (char alph = 'a'; alph <= 'z'; ++alph){
            // add a character to the word
            String oneMoreLetterWord = word + alph;
            // get the sorted string for the oneMoreLetterWord
            String sortedWordWithOneMoreLetter = sortLetters(oneMoreLetterWord);

            // fill all permutations of oneMoreLetterWord from Hash Map lettersToWords in result List
            if (lettersToWord.containsKey(sortedWordWithOneMoreLetter)){

                anagramsWithOneMoreLetter = lettersToWord.get(sortedWordWithOneMoreLetter);

                for (int i = 0; i < anagramsWithOneMoreLetter.size(); ++i) {
                    if ( !(anagramsWithOneMoreLetter.get(i).contains(word)) ) {
                        result.add(anagramsWithOneMoreLetter.get(i));
                    }
                }
            }
        }

        return result;

    }


    // pick a good starting word
    public String pickGoodStarterWord() {

        String starterWord = "badge";
        do {

            if(sizeToWords.get(wordLength).size() > count){

                starterWord = sizeToWords.get(wordLength).get(count);
                Log.d("Word", starterWord);
                count++;
            }
            else{
                wordLength++;
                count = 0;
            }

            if(getAnagramsWithOneMoreLetter(starterWord).size() !=  0) {
                break;
            }

        }while(true);

        return starterWord;
    }

    // sort the word's characters
    public String sortLetters(String word) {
        char[] characters = word.toCharArray();
        Arrays.sort(characters);
        String sortedWord = new String(characters);
        return sortedWord;
    }
}
