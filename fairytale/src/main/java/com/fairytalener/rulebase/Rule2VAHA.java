package com.fairytalener.rulebase;

import com.fairytalener.utilities.SentenceProperty;
import com.fairytalener.utilities.StanfordParser;
import com.fairytalener.utilities.Utility;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Rule2VAHA {

    List<String> characters = new ArrayList<>();


    public List<String> rule2_humanActivityVerb(List<String> sentences, StanfordParser stanfordParser) {
        List<Pair<String, String>> sv_phrases = new ArrayList<>();
        List<Pair<String, String>> vo_phrases = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> vo_verbs = new ArrayList<>();
        List<SentenceProperty> sentenceProperties = new ArrayList<>();
        List<String> vo_nouns = new ArrayList<>();
       // System.out.println("Story: " + file.getName());
        for (String sentence : sentences) {

            /**Getting dependencies in a sentence**/
            SentenceProperty sentenceProperty = new SentenceProperty();
            sentenceProperty.setSentence(sentence);
            List<TypedDependency> typedDependencyList = new ArrayList<>();
            //System.out.println("\nDependencies");
            sentenceProperty.setDependencies(stanfordParser.dependencyParser(sentence.toLowerCase()));
            sentenceProperties.add(sentenceProperty);
            // System.out.println(sentenceProperty.getDependencies().size());
            List<String> relation = new ArrayList<>();
            relation.add("nsubj");
            relation.add("nn");
            relation.add("nsubjpass");
            relation.add("dobj");
            /**nsubj, nn, nsubjpaas and dobj dependencies only**/
            sentenceProperty.getDependencies().forEach(tdl -> tdl.stream().forEach(typedDependency -> {
                if(relation.contains(typedDependency.reln().toString())) {
                    typedDependencyList.add(typedDependency);
                }

            }));

            /**for each dependency build the subject verb and verb object phrases**/
            for (int i = 0; i < typedDependencyList.size(); i++) {
                TypedDependency typedDependency = typedDependencyList.get(i);
                String rel = typedDependency.reln().toString();
               // if (relation.contains(rel)) {
                    TypedDependency prevTypedDependency = null;
                    TypedDependency nextTypedDependency = null;
                    if (i > 0)
                        prevTypedDependency = typedDependencyList.get(i - 1);
                    if (i < typedDependencyList.size()-1)
                        nextTypedDependency = typedDependencyList.get(i + 1);
                    String tag1 = typedDependency.dep().tag();
                    String tag2 = typedDependency.gov().tag();
                  //  if ((tag1.equals("NN") || tag1.equals("NNS")) && tag2.contains("VB")) {
                    if(tag1.contains("NN") && tag2.contains("VB")){
                        String noun = typedDependency.dep().word();
                        String noun_actual = noun;

                        if (i > 0)
                            if (prevTypedDependency.reln().toString().equals("compound") || prevTypedDependency.reln().toString().equals("amod") || prevTypedDependency.reln().toString().equals("det"))
                                noun = prevTypedDependency.dep().word() + " " + noun;
                        if (i < typedDependencyList.size()-1)
                            if(nextTypedDependency.reln().toString().equals("amod")) {
                                noun = noun + " " + nextTypedDependency.dep().word();
                                noun_actual = nextTypedDependency.dep().word();
                            }
                        if(!vo_nouns.contains(noun_actual))
                            vo_nouns.add(noun_actual);
                            //  System.out.println(typedDependency.reln() + "---" + typedDependency.dep() + "---" + typedDependency.gov());
                        Pair temp = new Pair(noun, typedDependency.gov().word().toString());
                        String verb = typedDependency.gov().word().toString();

                       //Verb object
                        if (rel.equals("dobj")) { //Verb object phrases
                            if (!vo_phrases.contains(temp))
                                vo_phrases.add(temp);
                            if (!vo_verbs.contains(verb))
                                vo_verbs.add(verb);

                        } else { //Subject Verb phrases
                            if (!sv_phrases.contains(temp))
                                sv_phrases.add(temp);
                            if (!verbs.contains(verb))
                                verbs.add(verb);
                        }
                  //  }
                }
            }


        }
        JWNLUtility jwnlUtility = new JWNLUtility();
        Utility utility = new Utility();

        List<String> dsrVerbs = jwnlUtility.getDRF(verbs);
       // System.out.println("DSR Verbs: \n\n"+dsrVerbs+"\n");

        //Subject Verbs phrases
        List<String> humanActivityVerbs = jwnlUtility.getSentenceFrame(verbs, "SV");
        List<String> sv_animateBeings = jwnlUtility.getAnimateBeings(vo_nouns);
        List<String> tempChar = new ArrayList<>();
        System.out.println("Subject Verb characters: ");
        sv_phrases.forEach(tuple -> {
            if(dsrVerbs.contains(tuple.second)){
                String[] temp = tuple.first.split(" ");
                String noun;
                if (temp.length>1)
                    noun = temp[1];
                else
                    noun = tuple.first;
                System.out.println(noun);
                int threshold = countPhrase(noun,sentences);

                if(sv_animateBeings.contains(noun.toUpperCase())) {
                if (!tempChar.contains(tuple.first.toUpperCase()))
                    tempChar.add(tuple.first.toUpperCase());
                System.out.println(tuple.first + " " + tuple.second);
               }
           }else {
                Boolean isHumanActivityVerb = getCharactersIfHumanVerb( humanActivityVerbs, tuple);
                if (isHumanActivityVerb) {
                    String[] temp = tuple.first.split(" ");
                    String noun;
                    if (temp.length > 1)
                        noun = temp[1];
                    else
                        noun = tuple.first;
                    System.out.println(noun);
                    if(sv_animateBeings.contains(noun.toUpperCase())) {
                    if (!tempChar.contains(tuple.first.toUpperCase()))
                        tempChar.add(tuple.first.toUpperCase());
                    System.out.println(tuple.first + " " + tuple.second);
                    }
                }
            }
        });
        characters = utility.addToList(characters, tempChar);
        System.out.println();

        //Verb object
        List<String> vo_humanActivityVerbs = jwnlUtility.getSentenceFrame(vo_verbs, "VO");
        List<String> vo_animateBeings = jwnlUtility.getAnimateBeings(vo_nouns);
        System.out.println(vo_nouns);
        System.out.println(vo_animateBeings);
        List<String> tempChar2 = new ArrayList<>();
        System.out.println("Verb object character phrases: ");
        vo_phrases.forEach(tuple -> {
            if(vo_humanActivityVerbs.contains(tuple.second)){
                String[] temp = tuple.first.split(" ");
                String noun;
                if (temp.length>1)
                    noun = temp[1];
                else
                    noun = tuple.first;
                System.out.println(noun);
               if(vo_animateBeings.contains(noun.toUpperCase())) {
                   if (!tempChar.contains(tuple.first.toUpperCase()))
                       tempChar2.add(tuple.first.toUpperCase());
                   System.out.println(tuple.second + " " + tuple.first);
              }
            }
        });
        characters = utility.addToList(characters,tempChar2);
        System.out.println();

        /*  Map<Integer, CorefChain> graph = stanfordParser.coreferenceResolution(story);
        for (Map.Entry entry : graph.entrySet()) {
            CorefChain c = (CorefChain) entry.getValue();
            CorefChain.CorefMention cm = c.getRepresentativeMention();
            System.out.println(cm);
        }*/

        return characters;
    }

    private int countPhrase(String noun, List<String> sentences) {
        int count = 0;
        //if
        return count;
    }


    private Boolean getCharactersIfHumanVerb( List<String> humanActivityVerbs, Pair<String, String> tuple) {
        Boolean isHumanActivityVerb = false;
        //if (dsrVerbs.contains(tuple.second))
           // isHumanActivityVerb = true;
        if (humanActivityVerbs.contains(tuple.second))
            isHumanActivityVerb = true;
        return isHumanActivityVerb;
    }
}
