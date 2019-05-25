package com.fairytalener;

import com.fairytalener.utilities.FileUtilities;
import com.fairytalener.utilities.StanfordParser;

import java.io.*;
import java.util.HashSet;
import java.util.List;

public class EvaluationNN {
    StanfordParser stanfordParser = new StanfordParser();

    public void calculate() throws IOException {
        File folders = new File("src/main/resources/Data/corpus/grimms");
        File[] listOfFiles = folders.listFiles();
        int totaltruepost=0 ,totalfalsepos=0, totalfalseneg = 0, totaltrueneg=0;
        int totalAnnotatedChar =0, totalAnnotatedNotChar =0, totalPredictedChar = 0, totalPredictedNotChar =0;
        File newFile = new File("nn_grimms.csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
        int yes_yes=0, yes_no=0, no_yes=0, no_no=0;

        for(File file: listOfFiles) {
            FileUtilities fileUtilities = new FileUtilities();
            String story = fileUtilities.readFile(file);
            List<String> nouns = stanfordParser.getNouns(story);
            File baselineFile = new File("src/main/resources/Data/baseline/grimms/" + file.getName());
            HashSet<String> characterbaseline = getCharacters(baselineFile);
            File annotatedFile = new File("src/main/resources/Data/corpus/annotate/data/all/" + file.getName());
            HashSet<String> characterExpected = getCharacters(annotatedFile);
            File predictedCharFile = new File("src/main/resources/Data/Output/grimms/" + file.getName());
            HashSet<String> characterPredicted = getCharacters(predictedCharFile);

            int truePositive = 0, falsePositive = 0, falseNegative = 0, trueNegative = 0;

            System.out.println("Total nouns: " + nouns.size());
            System.out.println("Total annotated first: " + characterExpected.size());
            System.out.println("Total annotated second:" + characterPredicted.size());
            for (String noun : nouns) {

                if (characterExpected.contains(noun)) {
                    if (characterPredicted.contains(noun)) {
                        System.out.println(noun + ":\t" + characterExpected.contains(noun) + "\t" + characterPredicted.contains(noun));
                        truePositive++;
                        totaltruepost++;
                        // } else {
                    }else {
                        System.out.println(noun + ":\t" + characterExpected.contains(noun) + "\t" + characterPredicted.contains(noun));
                        falseNegative++;
                        totalfalseneg++;
                    }
                    if (characterPredicted.contains(noun)&&characterbaseline.contains(noun)) {
                        yes_yes++;
                        // } else {
                    }else if(characterPredicted.contains(noun) && !characterbaseline.contains(noun)){
                        no_yes++;
                    }
                    else if(!characterPredicted.contains(noun) && characterbaseline.contains(noun)){
                       yes_no++;
                    }
                    else if(!characterPredicted.contains(noun) && !characterbaseline.contains(noun)){
                       no_no++;
                    }
                } else {
                    if (characterPredicted.contains(noun)) {
                        System.out.println(noun + ":\t" + characterExpected.contains(noun) + "\t" + characterPredicted.contains(noun));
                        falsePositive++;
                        totalfalsepos++;
                    } else {
                        System.out.println(noun + ":\t" + characterExpected.contains(noun) + "\t" + characterPredicted.contains(noun));
                        trueNegative++;
                        totaltrueneg++;
                    }
                }
            }
            System.out.println("\n\n" + "True Positive " + truePositive);
            System.out.println("False Positive " + falsePositive);
            System.out.println("False Negative " + falseNegative);
            System.out.println("True Negative " + trueNegative);
            Float precision = Float.valueOf(truePositive) / (truePositive + falsePositive);
            Float recall = Float.valueOf(truePositive) / (truePositive + falseNegative);
            Float f1 = 2 * precision * recall / (precision + recall);

            int a1_characters = truePositive + falseNegative;
            int a1_notCharac = falsePositive + trueNegative;
            int a2_character = truePositive + falsePositive;
            int a2_notChracters = falseNegative + trueNegative;
            int total = a1_characters + a1_notCharac;
            totalAnnotatedChar+=a1_characters;
            totalAnnotatedNotChar+=a1_notCharac;
            totalPredictedChar+=a2_character;
            totalPredictedNotChar+=a2_notChracters;
            //=((D8/D10)*(B10/D10))+((D9/D10)*(C10/D10))
            float pE = ((Float.valueOf(a1_characters) / total) * (Float.valueOf(a2_character) / total)) + ((Float.valueOf(a1_notCharac) / total) * (Float.valueOf(a2_notChracters) / total));
            float pA = (float) (truePositive + trueNegative) / total;
            //=(B16-B15)/(1-B15)
            float kappa = (pA - pE) / (1 - pE);
            System.out.println(file.getName() + "\n --- \nF1-measure = \n" + f1);
            System.out.println("precision = \n" + precision);
            System.out.println("recall = \n" + recall + "\n\n");
            System.out.println("P(E) = " + pE);
            System.out.println("P(A) = " + pA);
            System.out.println("Kappa = " + kappa);

            try {
                bw.write(file.getName()+";"+precision+";"+recall+";"+f1+";"+kappa);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println("True Positive: "+totaltruepost);
        System.out.println("True Negative: "+totaltrueneg);
        System.out.println("False Positive: "+totalfalsepos);
        System.out.println("False Negative: "+totalfalseneg);


        float totalprec = Float.valueOf(totaltruepost) / (totaltruepost + totalfalsepos);
        float totalrecal = Float.valueOf(totaltruepost) / (totaltruepost + totalfalseneg);
        float totalf1 = 2 * totalprec * totalrecal / (totalprec + totalrecal);
        int total = totalAnnotatedChar+totalAnnotatedNotChar;
        float pE = ((Float.valueOf(totalAnnotatedChar) / total) * (Float.valueOf(totalPredictedChar) / total)) + ((Float.valueOf(totalAnnotatedNotChar) / total) * (Float.valueOf(totalPredictedNotChar) / total));
        float pA = (float) (totaltruepost + totaltrueneg) / total;
        //=(B16-B15)/(1-B15)
        float kappa = (pA - pE) / (1 - pE);

        System.out.println("Yes_Yes: "+yes_yes);
        System.out.println("Yes_No: "+yes_no);
        System.out.println("No_Yes: "+no_yes);
        System.out.println("No_No: "+no_no);

        bw.newLine();

            bw.write("Overall"+";"+totalprec+";"+totalrecal+";"+totalf1+";"+kappa);

            bw.flush();
            bw.close();
    }

    private HashSet<String> getCharacters(File filename) {
        HashSet<String> allcharacter = new HashSet<>();
        String filecontents = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String st;
            while ((st = br.readLine()) != null) {
                //    while(!st.equals("TOTAL NC:") && st!=null) {
                if (st.contains("CHARACTER")) {
                    st = br.readLine();
                }
                if (!st.equals("")) {
                        List<String> nouns = stanfordParser.getNouns(st);
                        //String[] temp = nouns.split(" ");
                        for(String t: nouns) {
                            allcharacter.add(t.toUpperCase());
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(allcharacter);

        return allcharacter;
    }

    public static void main(String args[]){
        EvaluationNN interco = new EvaluationNN();
        try {
            interco.calculate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
