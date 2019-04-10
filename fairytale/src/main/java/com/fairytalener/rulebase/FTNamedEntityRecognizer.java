package com.fairytalener.rulebase;

import edu.stanford.nlp.simple.Sentence;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FTNamedEntityRecognizer {
    List<String> tokens;
    List<String> posTags;
    List<String> nerTags;

    public FTNamedEntityRecognizer(List<String> tokens, List<String> posTags, List<String> nerTags) {
        this.tokens = tokens;
        this.posTags = posTags;
        this.nerTags = nerTags;
    }

    public List<String> nerCharRules() {
        List<String> characters = new ArrayList<String>();
        for (int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            String posTag = posTags.get(index);
            String nerTag = nerTags.get(index);
            String name = token;

             /* if(!nerTag.equals("O"))
                    System.out.println("[" + token + "/" + nerTag + "] ");*/

            if(token.equals(token.toUpperCase()) && token.matches("[a-zA-z^``]+")){
                // System.out.println(token);
                token = token.toLowerCase();
                token = StringUtils.capitalize(token);
                Sentence sentence = new Sentence(token);
                nerTag = sentence.nerTag(0);
            }

            if (nerTag.equals("PERSON")) {

                // Checking for Characters with Two word names eg. Tom Thumb
                while (nerTags.get(index + 1).equals("PERSON")) {
                    name = name + " " + tokens.get(++index);
                }


                if (!characters.contains(name.toUpperCase())) {
                    characters.add(name.toUpperCase());
                }
            }



            /*else if (firstLetter.equals(firstLetter.toUpperCase()) && index != 0) { //characters like The Beast
                System.out.println("first letter - "+token);
                // if(token.equals("Beast")) {
                //System.out.println(posTags.get(index - 1)+"....token="+token+"/"+posTag);
                if (posTags.get(index - 1).equals("DT") && posTag.equals("NN") && posTags.get(index + 1).equals("VBD")) {
                    // System.out.println("[" +tokens.get(index-1)+" "+ token + " " + tokens.get(index+1) + "]");
                    if (!characters.contains(token.toLowerCase())) {
                        characters.add(token.toLowerCase());
                    }
                }
            }*/
        }
        return characters;
    }

}
