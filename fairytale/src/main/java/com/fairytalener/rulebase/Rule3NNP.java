package com.fairytalener.rulebase;

import com.fairytalener.utilities.FileUtilities;
import com.fairytalener.utilities.StanfordParser;
import com.sun.xml.internal.bind.v2.TODO;
import edu.stanford.nlp.simple.Sentence;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Rule3NNP {
    private final List<String> tokens;
    private final List<String> posTags;

    public Rule3NNP(List<String> tokens, List<String> posTags) {
        this.tokens = tokens;
        this.posTags = posTags;
    }

    public List<String> findCharacters(){
        List<String> characters = new ArrayList<>();

        for(int i=0;i<tokens.size();i++){
            String token = tokens.get(i);
            String posTag = posTags.get(i);

            //Edge case - SOMETIMES Peter Rabbit
            if(token.equals(token.toUpperCase()) && token.matches("[a-zA-z^``]+")){
               // System.out.println(token);
                token = token.toLowerCase();
                token = StringUtils.capitalize(token);
                Sentence sentence = new Sentence(token);
                posTag = sentence.posTag(0);
            }

            if(posTag.equals("NNP")) {
                String name = token;
                //  System.out.println(token+"/"+posTag);
                while (i < tokens.size() - 1 && posTags.get(i + 1).equals("NNP")) {
                    name = name + " " + tokens.get(i + 1);
                    //System.out.println(token + "/" + posTag);
                    i++;
                    //token = tokens.get(i);
                    //posTag = posTags.get(i);
                }
                //TODO: check for substrings and then add character
                if (!characters.contains(name.toUpperCase()))
                    characters.add(name.toUpperCase());


            }
            //System.out.println(token+"/"+posTag);
        }
        //System.out.println(characters);
        JWNLUtility jwnlUtility = new JWNLUtility();
        characters = jwnlUtility.getAnimateBeings(characters);
        System.out.println(characters);
        return characters;
    }


    public static void main(String args[]){
        /*File folder = new File("src/main/resources/Data/corpus/train");
        File[] files = folder.listFiles();
        for (File file : files) {*/
            FileUtilities utilities = new FileUtilities();
            File file = new File("src/main/resources/Data/corpus/annotate/data/unaiza/187655076-THE-TALE-OF-THE-FLOPSY-BUNNIES-Beatrix-Potter.txt");
            String story = utilities.readFile(file);
            StanfordParser stanfordParser = new StanfordParser();
            List<String> tokens = stanfordParser.tokenize(story);
            List<String> posTags = stanfordParser.posTagging(story);
            System.out.println(file);
            Rule3NNP rule3NNP = new Rule3NNP(tokens, posTags);
            rule3NNP.findCharacters();
       // }
    }
}
