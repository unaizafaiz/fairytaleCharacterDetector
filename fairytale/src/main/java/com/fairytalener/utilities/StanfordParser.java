package com.fairytalener.utilities;

import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.simple.Token;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.IntPair;

import java.io.File;
import java.util.*;

public class StanfordParser {
    public static List<String> tokenize(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            result.add(word);
        }

        return result;
    }

    public static List<String> sentenceSplit(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreMap sentence : sentences) {
            String sentenceString = sentence.get(TextAnnotation.class);
            result.add(sentenceString);

            // see tokenize(String) method
            List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
            for (CoreLabel token : tokens) {
                String word = token.get(TextAnnotation.class);
            }
        }

        return result;
    }

    public static List<String> posTagging(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            result.add(pos);
        }

        return result;
    }

    public static List<String> lemmatize(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
            result.add(lemma);
        }

        return result;
    }

    public static List<String> ner(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String nerTag = token.get(NamedEntityTagAnnotation.class);
            result.add(nerTag);
        }

        return result;
    }

    public static List<Tree> parse(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        List<Tree> result = new ArrayList<Tree>();
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeAnnotation.class);
            result.add(tree);
        }

        return result;
    }

    public static Map<Integer,CorefChain> coreferenceResolution(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        Map<Integer, CorefChain> graph =
                document.get(edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation.class);
       // System.out.println(graph);
        return graph;
    }

    public static HashMap<String,List<String>> coreferenceMentions(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, mention,coref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);


        HashMap<String,List<String>> resultBySentence = new HashMap<String,List<String>>();
        for (CoreMap sentence : document.get(SentencesAnnotation.class)) {
            List<String> result = resultBySentence.get(sentence);
            if(result == null) {
                result = new ArrayList<String>();
            }

           /* for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
                result.add(m.toString());
            }*/

            resultBySentence.put(sentence.toString(),result);
        }

        return resultBySentence;
    }

    public List<Collection<TypedDependency>> dependencyParser(String sent) {

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP stanford = new StanfordCoreNLP(props);

        TregexPattern npPattern = TregexPattern.compile("@NP");

        String text = "The fitness room was dirty.";


        // create an empty Annotation just with the given text
        Annotation document = new Annotation(sent);
        // run all Annotators on this text
        stanford.annotate(document);
        List<Collection<TypedDependency>> tdllist = new ArrayList<>();

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {

            Tree sentenceTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            /*TregexMatcher matcher = npPattern.matcher(sentenceTree);

            while (matcher.find()) {
                //this tree should contain "The fitness room"
                Tree nounPhraseTree = matcher.getMatch();
                //Question : how do I find that "dirty" has a relationship to the nounPhraseTree

            }*/

            // Output dependency tree
            TreebankLanguagePack tlp = new PennTreebankLanguagePack();
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(sentenceTree);
            Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();
            tdllist.add(tdl);
          //  System.out.println("typedDependencies: " + tdl);
        }
        return tdllist;
    }

    public List<Tree> mytregex(List<Tree> story, String POS){
        TregexPattern tPattern = TregexPattern.compile(POS);
        List<Tree> npList = new ArrayList<>();
        for(Tree tree:story) {
            TregexMatcher tMatcher = tPattern.matcher(tree);
            while (tMatcher.find()) {
                Tree np = tMatcher.getMatch();
                npList.add(np);
            }
        }
        npList.forEach(np->System.out.println(np));
        return npList;
    }

    public List<String> getNounPhrases(List<Tree> parse) {
        List<String> result = new ArrayList<>();
        for(Tree tree: parse) {
            TregexPattern pattern = TregexPattern.compile("@NP");
            TregexMatcher matcher = pattern.matcher(tree);
            while (matcher.find()) {
                Tree match = matcher.getMatch();
                List<Tree> leaves = match.getLeaves();
                // System.out.println(leaves);
                // Some Guava magic.
                String nounPhrase = Joiner.on(' ').join(Lists.transform(leaves, Functions.toStringFunction()));
                result.add(nounPhrase);
                //System.out.println(nounPhrase);
                //List<LabeledWord> labeledYield = match.labeledYield();
                // System.out.println("labeledYield: " + labeledYield);
            }
        }
        return result;
    }

    public List<String> getNouns(String thisstring){
        Properties properties = new Properties();
        properties.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
        Annotation annotation = pipeline.process(thisstring);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> output = new ArrayList<>();
        String regex = "([{pos:/NN|NNS|NNP/}])"; //extracting Nouns
        for (CoreMap sentence : sentences) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            TokenSequencePattern tspattern = TokenSequencePattern.compile(regex);
            TokenSequenceMatcher tsmatcher = tspattern.getMatcher(tokens);
            while (tsmatcher.find()) {
                output.add(tsmatcher.group().toUpperCase());
            }
        }
        /*String out = output.get(0);
        for(int i = 1;i<output.size();i++){
            out+=" "+output.get(i);
        }*/
        return output;
    }

    public String getHeadPhrase(String st){
        Sentence sent = new Sentence(st);
        List<Token> tokens = sent.tokens();
        List<String> posTags = sent.posTags();
        String charac="";
        for(int i=0;i<tokens.size();i++){
            String token = tokens.get(i).word();
            System.out.println(token+"/"+posTags.get(i));
            if(!posTags.get(i).equals("DT")){
                if(charac.equals(""))
                    charac = tokens.get(i).word();
                else{
                    charac +=" "+tokens.get(i).word();
                }
            }
        }
        return charac;
    }

    public static void main(String[] args){
        File file = new File("src/main/resources/Data/corpus/annotate/data/unaiza/149144903-HOW-THE-WICKED-SONS-WERE-DUPED-Indian-Writer.txt");
        FileUtilities utilities = new FileUtilities();
        String story = utilities.readFile(file);
        // String story = "John is a nice guy. I like him!";
         StanfordParser stanfordParser = new StanfordParser();

         List<Tree> myTree = stanfordParser.parse(story);
        //List<Tree> np = stanfordParser.mytregex(myTree, "NP");
        for(Tree tree: myTree) {
          //  List<String> np = stanfordParser.getNounPhrases(tree);
        }






         ///Coreference resolution
        /*Map<Integer, CorefChain> graph = stanfordParser.coreferenceResolution(story);
        Map<String,List<String>> reference = new HashMap<>();
        for (Map.Entry entry : graph.entrySet()) {
            CorefChain c = (CorefChain) entry.getValue();
            CorefChain.CorefMention cm = c.getRepresentativeMention();
            System.out.println(cm+"\n-------");
            Map<IntPair, Set<CorefChain.CorefMention>> mp = c.getMentionMap();
            mp.forEach((i,v)->{
                List<String> ref = new ArrayList<>();
                String key = "";
                Iterator iterator = v.iterator();
                if(iterator.hasNext()) {
                    CorefChain.CorefMention val = (CorefChain.CorefMention) iterator.next();
                    String thisVal = val.toString();
                    String[] temp = thisVal.split("\"");
                   // System.out.println("this--"+temp[0]);
                   // for(int a =0;a<temp.length;a++)
                    //    System.out.println(temp[a]+"---ind:"+a);
                    //System.out.println("this--"+temp[0]);

                   key = temp[1];
                }
                while (iterator.hasNext()) {
                    CorefChain.CorefMention val = (CorefChain.CorefMention) iterator.next();
                    String thisVal = val.toString();
                    String[] temp = thisVal.split("\"");
                    ref.add(temp[1]);
                }
                reference.put(key,ref);

            });
           // System.out.println(cm);
        }
        System.out.println(reference);
        System.out.println();
        stanfordParser.coreferenceResolution(story);*/
    }
}
