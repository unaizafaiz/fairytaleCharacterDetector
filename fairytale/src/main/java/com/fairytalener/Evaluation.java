package com.fairytalener;

import com.fairytalener.utilities.StanfordParser;
import com.fairytalener.utilities.Utility;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.simple.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Evaluation {
   // public int nounCount = 0;
    public List<String> createCharacterList(File filename){

        List<String> characters = new ArrayList<>();
        String filecontents = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String st;
            StanfordParser stanfordParser = new StanfordParser();
            while ((st = br.readLine())!=null) {
            //    while(!st.equals("TOTAL NC:") && st!=null) {
                    if (st.contains("CHARACTER")) {
                        st = br.readLine();
                    }
                    if (!st.equals("")) {
                        String charac = st;
                        //String charac = stanfordParser.getHeadPhrase(st);
                        characters.add(charac);
                    }


                //  st = br.readLine();
               // }
              //  nounCount = Integer.valueOf(st);

            }
            //System.out.println(characters);
            return characters;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String args[]) {
        Evaluation evaluation = new Evaluation();
        //Evaluating baseline
        //baselineresults/train
        //
       // File newFile = new File("partial_train132.csv");
       // File newFile = new File("pH_train132.csv");

        File newFile = new File("partial_baseline_test.csv");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));


            HashMap<String, List<String>> baselineResults = evaluation.getCharacters("src/main/resources/Data/baseline/test");
          //  HashMap<String, List<String>> baselineResults = evaluation.getCharacters("src/main/resources/Data/Output/train");

            HashMap<String, List<String>> annotationResults = evaluation.getCharacters("src/main/resources/Data/corpus/annotate/data/all");

            List<Integer> truePost = new ArrayList<>();
            List<Integer> falsePost = new ArrayList<>();
            List<Integer> falseneg = new ArrayList<>();

            baselineResults.forEach((story, value) -> {
                List<String> annotationCharacter;
                // System.out.println(story);
                int truepositive = 0;
                int falsepositive = 0;
                int falsenegative = 0;
                if (!annotationResults.containsKey(story)) {
                    System.out.println(story+" not found");
                }else{
                    annotationCharacter = annotationResults.get(story).stream()
                            .map(String::toUpperCase)
                            .collect(Collectors.toList());
                    System.out.println("Annotated: " + annotationCharacter);
                    System.out.println("Baseline: " + value);
                    for (String charact : value) {
                       // if (annotationCharacter.contains(charact)) { //non overlappin
                        Utility utility = new Utility();
                        if (utility.isSubstring(annotationCharacter,charact)){
                            //  System.out.println(charact+" is present in annotation results");
                            truepositive++;
                        } else {
                            falsepositive++;
                        }
                    }
                    falsenegative = annotationCharacter.size() - truepositive;
                    truePost.add(truepositive);
                    falsePost.add(falsepositive);
                    falseneg.add(falsenegative);

                    // System.out.println(count+" \nTotal"+annotationCharacter.size());
                    // Float accuracy =  Float.valueOf(truepositive)/annotationCharacter.size()*100;
                    Float precision = Float.valueOf(truepositive) / (truepositive + falsepositive);
                    Float recall = Float.valueOf(truepositive) / (truepositive + falsenegative);
                    Float f1 = 2 * precision * recall / (precision + recall);
                    System.out.println(story + "\n --- \nF1-measure = \n" + f1);
                    System.out.println("precision = \n" + precision);
                    System.out.println("recall = \n" + recall + "\n\n");

                    try {
                        bw.write(story+";"+precision+";"+recall+";"+f1+";");
                        bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });

            int totaltruepost=0 ,totalfalsepos=0, totalfalseneg = 0;

           for(int i=0;i<truePost.size();i++){
               totaltruepost+=truePost.get(i);
               totalfalseneg+=falseneg.get(i);
               totalfalsepos+=falsePost.get(i);
           }

            Float totalprec = Float.valueOf(totaltruepost) / (totaltruepost + totalfalsepos);
            Float totalrecal = Float.valueOf(totaltruepost) / (totaltruepost + totalfalseneg);
            Float totalf1 = 2 * totalprec * totalrecal / (totalprec + totalrecal);

            bw.newLine();

            bw.write("Overall"+";"+totalprec+";"+totalrecal+";"+totalf1+";");

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String,List<String>> getCharacters(String s) {
        File folder = new File(s);
        File[] files = folder.listFiles();
        System.out.println("No. of files = "+files.length);
        HashMap<String, List<String>> charactersMap = new HashMap<>();
        for (File file : files) {
            List<String> characters = new Evaluation().createCharacterList(file);
            charactersMap.put(file.getName(), characters);
        }
        System.out.println("No. of keys in hashmap = "+charactersMap.size());
        return charactersMap;
    }
}
