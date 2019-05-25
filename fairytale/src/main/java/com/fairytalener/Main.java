package com.fairytalener;

import com.fairytalener.rulebase.FTNamedEntityRecognizer;
import com.fairytalener.rulebase.Rule2VAHA;
import com.fairytalener.rulebase.Rule3NNP;
import com.fairytalener.utilities.FileUtilities;
import com.fairytalener.utilities.StanfordParser;
import com.fairytalener.utilities.Utility;
import edu.stanford.nlp.trees.Tree;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import edu.stanford.nlp.io.EncodingPrintWriter;
import java.util.HashMap;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {


        File folder = new File("src/main/resources/Data/corpus/Test");
        File[] files = folder.listFiles();
        for (File file : files) {
            FileUtilities utilities = new FileUtilities();
            //File file = new File("src/main/resources/Data/corpus/annotate/data/unaiza/187655076-THE-TALE-OF-THE-FLOPSY-BUNNIES-Beatrix-Potter.txt");
            String story = utilities.readFile(file);
            StanfordParser stanfordParser = new StanfordParser();
            List<String> tokens = stanfordParser.tokenize(story);
            List<String> posTags = stanfordParser.posTagging(story);
            List<String> nerTags = stanfordParser.ner(story);
            List<String> sentences = stanfordParser.sentenceSplit(story);
            FTNamedEntityRecognizer ftNamedEntityRecognizer = new FTNamedEntityRecognizer(tokens, posTags, nerTags);
            Rule2VAHA rule2VAHA = new Rule2VAHA();
            Rule3NNP rule3NNP = new Rule3NNP(tokens,posTags);
            List<String> characters = new ArrayList<>();

            List<String> rule1_characters = ftNamedEntityRecognizer.nerCharRules();
            List<String> rule2_characters = rule2VAHA.rule2_humanActivityVerb(sentences, stanfordParser);
            List<String> rule3_characters = rule3NNP.findCharacters();
            System.out.println("RULE 1: "+rule1_characters);
           System.out.println("RULE 2: "+rule2_characters);
            System.out.println("RULE 3: "+rule3_characters);
            Utility utility = new Utility();
             characters = utility.addToList(characters, rule1_characters);
             characters = utility.addToList(characters, rule3_characters);
            characters = utility.addToList(characters, rule2_characters);


            BufferedWriter bw = utilities.writeToFile(file.getName(), characters, null, null, "src/main/resources/Data/output.test");
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void ExtractAnimateBeings() {
        FileUtilities utilities = new FileUtilities();
        File file = new File( "src/main/resources/Data/corpus/EvaluationNN/data/unaiza/187655076-THE-TALE-OF-THE-FLOPSY-BUNNIES-Beatrix-Potter.txt");
        String story = utilities.readFile(file);
        StanfordParser stanfordParser = new StanfordParser();
        //List<String> sentences = stanfordParser.sentenceSplit(story);
        List<Tree> trees = stanfordParser.parse(story);

        //Getting animate objects (animals, person)
        List<Tree> nounphrase = stanfordParser.mytregex(trees,"NP");
        List<Tree> nouns = stanfordParser.mytregex(nounphrase,"NN");


        List<String> words = new ArrayList<>();
        for(Tree tree: nouns){
            String word = tree.toString().substring(4,(tree.toString().length()-1));
            words.add(word);
            //System.out.println(word);
        }


       /* JWNLUtility jwnlUtility = new JWNLUtility();
        List<String> animateobjects = jwnlUtility.intial(words);
        System.out.println(animateobjects);

        //List<Tree> verbphrases = stanfordParser.mytregex(trees,"VP");
        //verbphrases.forEach(vp->System.out.println(vp));

        for (Tree tree: trees){
            for (String obj: animateobjects){
                if(tree.toString().contains(obj))
                    System.out.println(tree.toString());
            }
        }*/

/*

                String folderpath = "src/main/resources/Data/GrimmsFairytale";
        File[] files = utilities.getFiles(folderpath);
        int characterCount = 0;
            for(File file: files) {

                StanfordParser stanfordParser = new StanfordParser();
                String story = utilities.readFile(file);

                stanfordParser.dependencyParser(story);

               *//* List<String> tokens = stanfordParser.tokenize(story);
                List<String> posTags = stanfordParser.posTagging(story);
                List<String> nerTags = stanfordParser.ner(story);


                FTNamedEntityRecognizer ftNamedEntityRecognizer = new  FTNamedEntityRecognizer(tokens,posTags,nerTags);
                List<String> characters = ftNamedEntityRecognizer.nerCharRules();
                List<String> time = ftNamedEntityRecognizer.nerTimeRules();
                List<String> location = ftNamedEntityRecognizer.nerLocRules();

                String outputfolder = "src/main/resources/Data/Output";
                BufferedWriter bw  = utilities.writeToFile(file.getName(), characters, time, location, outputfolder);

                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                characterCount += characters.size();
                System.out.println();*//*
            }
        System.out.println("Total no. of characters detected = "+characterCount);*/

    }




}
