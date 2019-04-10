package com.fairytalener.utilities;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataAnalysis {
    public DataAnalysis(File file){
        analyzeData(file);
    }

    StanfordParser stanfordParser = new StanfordParser();

    public int wordCount(String text){
        List<String> tokens= stanfordParser.tokenize(text);
        return  tokens.size();
    }

    public int sentenceCount(String text){
        List<String> sentences= stanfordParser.sentenceSplit(text);
        return  sentences.size();
    }

    public int totalWordCount(List<Integer> wordCount){
        return wordCount.stream().mapToInt(count -> count).sum();
    }

    public int totalWordCountAverage(List<Integer> wordCount){
        return wordCount.stream().mapToInt(count -> count).sum()/wordCount.size();
    }

    public int totalSentenceCount(List<Integer> sentenceCount){
        return sentenceCount.stream().mapToInt(count -> count).sum();
    }

    public int totalSentCountAverage(List<Integer> sentCount){
        return sentCount.stream().mapToInt(count -> count).sum()/sentCount.size();
    }

    private void analyzeData(File newFile) {
        File[] files = newFile.listFiles();
        FileUtilities fileUtilities = new FileUtilities();
        List<Integer> wordCounts = new ArrayList<>();
        List<Integer> sentCount = new ArrayList<>();
        for (File file:
                files) {
            String story = fileUtilities.readFile(file);
            int wordCount = this.wordCount(story);
            int sentenceCount = this.sentenceCount(story);
            wordCounts.add(wordCount);
            sentCount.add(sentenceCount);
        }

        System.out.println("---- Corpora Statistics ---");
        System.out.println("No of stories: "+newFile.listFiles().length);
        System.out.println("Total words = "+this.totalWordCount(wordCounts));
        System.out.println("Average no. of words = "+this.totalWordCountAverage(wordCounts));
        System.out.println("Total no of Sentences = "+this.totalSentenceCount(sentCount));
        System.out.println("Average no. of sentences across corpora = "+this.totalSentCountAverage(sentCount));


    }

    public void intercoder_agreement(){

    }

    public static void main(String args[]){
        File folder = new File("src/main/resources/Data/corpus/all");
        DataAnalysis dataAnalysis = new DataAnalysis(folder);
    }


}
