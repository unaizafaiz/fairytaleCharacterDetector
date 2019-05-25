package com.fairytalener.baseline;

import com.fairytalener.rulebase.FTNamedEntityRecognizer;
import com.fairytalener.utilities.DataAnalysis;
import com.fairytalener.utilities.FileUtilities;
import com.fairytalener.utilities.StanfordParser;
import com.fairytalener.utilities.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Baseline {
    public static void main(String args[]) {
        String inputFolder = "src/main/resources/Data/corpus/hcAnderson";
        String outputFolder = "src/main/resources/Data/baseline/hcanderson";

        StanfordNER(inputFolder,outputFolder);
    }

    private static void StanfordNER(String inputFolder, String outputFolder) {
        FileUtilities utilities = new FileUtilities();
        File[] files = utilities.getFiles(inputFolder);

        List<String> charactersFull = new ArrayList<String>();
        List<String> timeFull = new ArrayList<String>();
        List<String> locationFull = new ArrayList<String>();

        File characterResults = new File("src/main/resources/Data/CharacterResultsMC.txt");
        File locationResults = new File("src/main/resources/Data/LocationResultsMC.txt");
        File timeResults = new File("src/main/resources/Data/timeResultsMC.txt");

        try {
            BufferedWriter charBuffer = new BufferedWriter(new FileWriter(characterResults));
            BufferedWriter timeBuffer = new BufferedWriter(new FileWriter(timeResults));
            BufferedWriter locationBuffer = new BufferedWriter(new FileWriter(locationResults));

            for (File file : files) {

                String filename = file.getName();
                StanfordParser stanfordParser = new StanfordParser();
                String story = utilities.readFile(file);
                charBuffer.write("\n\nSTORY: "+filename+"\n");
                timeBuffer.write("\n\nSTORY: "+filename+"\n\n");
                locationBuffer.write("\n\nSTORY: "+filename+"\n\n");

                        List<String> characters = new ArrayList<String>();
                        List<String> time = new ArrayList<String>();
                        List<String> location = new ArrayList<String>();
                        List<String> tokens = stanfordParser.tokenize(story);
                        List<String> posTags = stanfordParser.posTagging(story);
                        List<String> nerTags = stanfordParser.ner(story);
                        int nouncount = 0;
                for (int index = 0; index < tokens.size(); index++) {
                    if(posTags.get(index).startsWith("NN"))
                        nouncount++;
                }


               FTNamedEntityRecognizer ftNamedEntityRecognizer = new FTNamedEntityRecognizer(tokens,posTags,nerTags);
               characters = ftNamedEntityRecognizer.nerCharRules();
               List<String> chars = new ArrayList<>();
               chars = new Utility().addToList(chars,characters);
                        BufferedWriter bw  = utilities.writeToFile(filename, chars, time, location, outputFolder);
                charactersFull.addAll(characters);
                timeFull.addAll(time);
                locationFull.addAll(location);

                bw.flush();
                bw.close();
            }

            locationBuffer.flush();
            locationBuffer.close();
            charBuffer.flush();
            charBuffer.close();
            timeBuffer.flush();
            timeBuffer.close();

        } catch(FileNotFoundException e){
                e.printStackTrace();
        } catch(IOException e){
                e.printStackTrace();
        }

    }

    private static void nerTags(List<String> tokens, List<String> posTags, List<String> nerTags, BufferedWriter bw) throws IOException {
        bw.write("\n\nALL NER TAGS\n\n");
        for (int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            String posTag = posTags.get(index);
            String nerTag = nerTags.get(index);
            if(!nerTag.equals("O")){
                bw.write(token+"/"+nerTag+"/"+posTag);
                bw.newLine();
            }
        }
    }

    private static void totalNNPCount(List<String> tokens, List<String> posTags, BufferedWriter bw) throws IOException {
        bw.write("\n\nALL NNP TAGS\n\n");
        int countNNP = 0;
        for (int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            String posTag = posTags.get(index);
            if(posTag.equals("NNP")){
                bw.write(token+"/"+posTag);
                countNNP++;
                bw.newLine();
            }
        }
        bw.write("Totaly NNP = "+countNNP);
        bw.newLine();
    }

    private static void totalPhraseCount(List<String> tokens, List<String> posTags, BufferedWriter bw) throws IOException {
        bw.write("\n\nALL <DT NN> TAGS\n\n");
        HashMap<String, Integer> phraseFreq= new HashMap<String, Integer>();
        int countDTNN = 0;
        for (int index = 1; index < tokens.size(); index++) {
            String token = tokens.get(index);
            String prevtoken = tokens.get(index-1);
            String posTag = posTags.get(index);
            String prevPosTag = posTags.get(index-1);
            if(posTag.equals("NN") && prevPosTag.equals("DT")){
                String phrase = prevtoken+" "+ token;
                bw.write(phrase);
                if(phraseFreq.containsKey(phrase)){
                    int val = phraseFreq.get(phrase);
                    phraseFreq.put(phrase,++val);
                }else{
                    phraseFreq.put(phrase,1);
                }
                countDTNN++;
                bw.newLine();
            }
        }
        bw.write("Total DT NN = "+countDTNN);
        bw.newLine();

        phraseFreq.forEach((key,value)->{
            if(value>1) {
                try {
                    bw.write(key+"  --- "+value);
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
