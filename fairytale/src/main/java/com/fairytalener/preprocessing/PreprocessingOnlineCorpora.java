package com.fairytalener.preprocessing;

import java.io.*;

public class PreprocessingOnlineCorpora {
    public static void main(String args[]){
        //cleanCorpus();
    }



    private static void cleanCorpus() {
        File inputFolder = new File("src/main/resources/Data/corpus-map");
        File[] folder = inputFolder.listFiles();
        File newFile = new File("src/main/resources/Data/cleanedCorpusMap");

        for (File fold : folder) {
            System.out.println("In Folder "+fold.getName());
            if(fold.isDirectory()) {
                File[] files = fold.listFiles();
                for (File file : files) {
                    System.out.println("\t\t\t" + file.getName());
                    if (!file.getName().startsWith("c")) {

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            BufferedWriter bw = new BufferedWriter(new FileWriter(newFile + "/" + file.getName()));
                            String st;
                            while ((st = br.readLine()) != null) {
                                if (st.matches("</author>.*")) {
                                    break;
                                }
                            }

                            while ((st = br.readLine()) != null) {
                                String[] temp = st.split("-->");
                        /*String pattern = "<.*>.*";
                        String[] temp1;
                        if (!temp[0].matches(pattern)) {*/
                                String[] temp1 = temp[0].split("/");
                                bw.write(temp1[0] + " ");

                                //}
                            }

                            bw.flush();
                            bw.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            System.out.println("In folder " + file.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("In folder " + file.getName());
                        }


                    }
                }
            }

        }
    }
}
