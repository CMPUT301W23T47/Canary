package com.cmput301w23t47.canary.controller;

import com.github.javafaker.Faker;

import java.util.Random;

/**
 * A random Name generator
 */
public class RandomNameGenerator {
    Faker faker = new Faker();
    private String combinedWords=randomGenName();

    public String getWord(){
        return combinedWords;
    }

    /**
     * Constructor for the randomGenName
     * @param: name
     * @return two combine gen name
     **/
    public String randomGenName() {
        Random random = new Random();
        String genName="";
        for (int i=0;i<2;i++){
            genName+=produceName(random.nextInt(10));
        }
        return genName;
    }
    /**
     * Constructor for the produceName
     * @param: choice
     * @return returnName
     **
     **/
    private String produceName(int choice){
        String returnName;
        switch (choice){
            case 0:
                returnName = faker.cat().name();
                break;
            case 1:
                returnName = faker.dog().name();
                break;
            case 2:
                returnName = faker.company().name();
                break;
            case 3:
                returnName=faker.color().name();
                break;
            case 4:
                returnName=faker.country().name();
                break;
            case 5:
                returnName=faker.currency().name();
                break;
            case 6:
                returnName=faker.gameOfThrones().city();
                break;
            case 7:
                returnName=faker.university().name();
                break;
            case 8:
                returnName=faker.country().capital();
                break;
            case 9:
                returnName=faker.artist().name();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
        return returnName;
    }


}
