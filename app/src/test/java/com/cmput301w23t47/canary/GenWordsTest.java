package com.cmput301w23t47.canary;
import com.cmput301w23t47.canary.controller.RandomNameGenerator;


import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Gen words test.
 */
public class GenWordsTest {
    /**
     * Test gen words.
     */
    @Test
    public void testGenWords()
    {
        Set<String> words= new HashSet<String>();
       for (int i=0;i<20;i++)
       {
           RandomNameGenerator gen = new RandomNameGenerator();
           String word=gen.getWord();
           if(words.contains(word)){
               throw new RuntimeException("Duplicate word");
            }
           else{
               words.add(word);
           }
       }
    }
}