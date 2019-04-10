package com.fairytalener.rulebase;

import com.fairytalener.utilities.Utility;
import edu.stanford.nlp.simple.Sentence;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.*;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class JWNLUtility {

    //Initializing JWNLUtility
    public JWNLUtility(){
        try {
            JWNL.initialize(new FileInputStream("src/main/resources/properties.xml"));
        } catch (JWNLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDRF(List<String> words){
        try {
            //Getting dictionary instance
            final Dictionary dictionary = Dictionary.getInstance();

            List<String> verbs = new ArrayList<>();
                for(String word: words) {
                   String lemma = new Sentence(word).lemma(0);

                    IndexWord indexWord = dictionary.getIndexWord(POS.VERB, lemma);
                    if (indexWord != null) {
                        //indexWord = dictionary.getIndexWord(POS.ADJECTIVE, lemma);
                        Synset[] synsets = indexWord.getSenses();
                        for (Synset synset : synsets) {
                            Pointer[] pointers = synset.getPointers(PointerType.NOMINALIZATION);
                         //   synset.
                            for (Pointer pointer : pointers) {
                                //System.out.println(pointer.getType());
                                String temp = pointer.getTarget().toString();
                                //String temp = pointer.toString();
                                if (temp.contains("a person who")||temp.contains("someone who")||temp.contains("to whom") ||temp.contains("one who")) {
                                    if(!verbs.contains(word))
                                        verbs.add(word);
                                }
                               // System.out.println(temp);
                            }
                        }
                    }

                }

            return verbs;

        } catch (JWNLException e) {
            e.printStackTrace();
            return null;
        }


    }

    //Getting sentenceFrame for the words and return the verbs that associate with human activity
    public List<String> getSentenceFrame(List<String> words, String type){
        Utility utility = new Utility();
        try {
            //Getting dictionary instance
            final Dictionary dictionary = Dictionary.getInstance();
            List<String> humanActivityVerbs = new ArrayList<>();
            for(String word: words) {
                String lemma = new Sentence(word).lemma(0);
                IndexWord indexWord = dictionary.getIndexWord(POS.VERB, lemma);
                    if (indexWord != null) {
                        Synset[] synsets = indexWord.getSenses();
                        List<String> verbFrametoList = new ArrayList<>();
                        for (Synset synset : synsets) {
                            String[] verbFrame = synset.getVerbFrames();

                            for (String s : verbFrame) {
                                verbFrametoList.add(s);
                            }
                        }
                       // if (verbFrametoList.contains("Somebody ----s somebody") && !verbFrametoList.contains("Somebody ----s something")) {
                       // String regex = "Something ----s.*";
                        String regex;
                        if(type.equals("VO"))
                           regex = ".* something";
                        else
                            regex = "Something ----s.*";
                       // if(!verbFrametoList.contains("")){
                        if(utility.getMatchingStrings(verbFrametoList,regex)){
                           // System.out.println("This verb is human activity " + word);
                            humanActivityVerbs.add(word);
                        }


                    }
            }

            return humanActivityVerbs;

        } catch (JWNLException e) {
            e.printStackTrace();
            return null;
        }


    }




    //Get nouns that are animate beings
    public List<String> getAnimateBeings(List<String> words){
        //WordNet initialization
        try {
            //Getting dictionary instance
            final Dictionary dictionary = Dictionary.getInstance();

            List<String> animatebeings = new ArrayList<>();
            //Getting set of all synonyms related to the topic nouns
            for(String word: words) {
                IndexWord indexWord = dictionary.getIndexWord(POS.NOUN, word);

                //Check if animate being
                    if(indexWord!=null) {
                        if(isWordAnimateBeing(indexWord)) {
                            if(!animatebeings.contains(word.toUpperCase()))
                                animatebeings.add(word.toUpperCase());
                        }
                    }else{
                        if(!animatebeings.contains(word.toUpperCase()))
                            animatebeings.add(word.toUpperCase());
                    }
            }

            return animatebeings;

        } catch (JWNLException e) {
            e.printStackTrace();
            return null;
        }


    }

    private Boolean isWordAnimateBeing(IndexWord word) throws JWNLException {
        List hypernyms = PointerUtils.getInstance().getHypernymTree(word.getSense(1)).toList();
       // System.out.println(" hypernyms of \"" + word.getLemma() + "\":");

        Iterator list1It = hypernyms.iterator();
        // browse lists
        while (list1It.hasNext()) {
            PointerTargetNodeList ptnl1 = (PointerTargetNodeList) list1It.next();
            Iterator listIt = ptnl1.iterator();
            // browse lists
            while (listIt.hasNext()) {
                if (ptnl1 != null) {
                    PointerTargetNode ptn = (PointerTargetNode) listIt.next();
                    String hypernym = ptn.toString();
                    int beginIndex = hypernym.indexOf("Words:");
                    int endIndex = hypernym.indexOf("--");
                    String substring = hypernym.substring(beginIndex, endIndex);

                  //  if (substring.contains("animal") || substring.contains("person")) {
                    if(substring.contains("animate")){
                       // System.out.println(ptn.toString());
                        return true;
                    }
                }

            }
        }
        return false;
    }




}
