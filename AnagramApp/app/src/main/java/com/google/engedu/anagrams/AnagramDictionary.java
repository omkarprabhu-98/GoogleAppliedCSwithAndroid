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
    // 1. For storing all the words from the dictionary
    private ArrayList<String> wordList;
    // 2. For storing all string permutations of all word from the dictionary
    private HashMap<String, ArrayList<String>> lettersToWord;
    // 3. storing unique words from dictionary
    private HashSet<String> wordSet;
    // 4. Storing words having same length together
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

            // fill the Hash Map for storing all permutations of the word
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

            // fill the Hash Map for storing set of words having same length
            ArrayList<String> tmpList1 = new ArrayList<>();
            int j = word.length();
            if(sizeToWords.containsKey(j)){
                tmpList1 = sizeToWords.get(j);
                tmpList1.add(word);
                sizeToWords.put(j, tmpList1);
            }
            else{
                tmpList1.add(word);
                sizeToWords.put(j, tmpList1);
            }

        }

    }

    // Check if string entered is valid
    // 1. It is present in the dictionary
    // 2. It is not word + one character at beginning or end
    public boolean isGoodWord(String word, String base) {
        return (wordSet.contains(word) && !(base.contains(word)));
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        return result;
    }

    // get a arrayList of anagrams for the word
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();

        ArrayList<String> anagramsWithOneMoreLetter;

        if(word.equals("")){
            return result;
        }

        // Log.d("Tag", "IN");
        // generate anagrams with one more letter list
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


    // pick a word which has no of anagrams with one more letter greater than minimum number of anagrams
    public String pickGoodStarterWord() {

        String starterWord = "";

        int noOfAnagrams;
        do {
            // pick a word of length wordLength
            if(count < sizeToWords.get(wordLength).size()){
                starterWord = sizeToWords.get(wordLength).get(count);
            }
            else{
                wordLength = (wordLength + 1)%MAX_WORD_LENGTH;
                count = 0;
            }
            count++;

            // check no of anagrams
            noOfAnagrams = getAnagramsWithOneMoreLetter(starterWord).size();

        }while (noOfAnagrams < MIN_NUM_ANAGRAMS);


        return starterWord;
    }

    // sort the word's characters
    private String sortLetters(String word) {
        char[] characters = word.toCharArray();
        Arrays.sort(characters);
        return new String(characters);
    }
}
