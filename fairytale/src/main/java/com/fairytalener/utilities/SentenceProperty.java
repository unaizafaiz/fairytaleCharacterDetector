package com.fairytalener.utilities;

import edu.stanford.nlp.trees.TypedDependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SentenceProperty {
    private String sentence;
    private List<String> subjectVerbPrase;
    private List<String> verbObjectPhrase;
    private List<Collection<TypedDependency>> dependencies;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<String> getSubjectVerbPrase() {
        return subjectVerbPrase;
    }

    public void setSubjectVerbPrase(List<String> subjectVerbPrase) {
        this.subjectVerbPrase = subjectVerbPrase;
    }

    public List<String> getVerbObjectPhrase() {
        return verbObjectPhrase;
    }

    public void setVerbObjectPhrase(List<String> verbObjectPhrase) {
        this.verbObjectPhrase = verbObjectPhrase;
    }

    public List<Collection<TypedDependency>> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Collection<TypedDependency>> dependencies) {
        this.dependencies = dependencies;
    }

    public void findBiGrams(List<String> tokens, List<String> POS){
        List<String> nv_bigrams = new ArrayList<>();
        List<String> vo_bigrams = new ArrayList<>();
       /* List<String> mainVerbs = new ArrayList<>();
        mainVerbs.add("VV");
        mainVerbs.add( "VVD");
        mainVerbs.add( "VVG");
        mainVerbs.add( "VVN");
        mainVerbs.add( "VVP");
        mainVerbs.add( "VVZ");*/
        for(int i=0; i<tokens.size()-1;i++){
            if (POS.get(i).startsWith("N") && POS.get(i+1).startsWith("V"))
                nv_bigrams.add(tokens.get(i)+" "+tokens.get(i+1));
            else if (POS.get(i).startsWith("V") && POS.get(i+1).startsWith("N")){
                vo_bigrams.add(tokens.get(i)+" "+tokens.get(i+1));
            }
        }

        setSubjectVerbPrase(nv_bigrams);
        setVerbObjectPhrase(vo_bigrams);
    }



}