package com.fairytalener.preprocessing;

import java.io.*;

public class PreprocessingGrimmsTales {
    public static void clean_data(){
        File file = new File("src/main/resources/Data/GrimmsTales.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            File newfile;
            BufferedWriter bw = null;
           // PrintWriter bw = null;
            String prev = "";
            String title = "";
            Boolean firstline = true;
            while((st = br.readLine())!=null ){

                if(st.matches("^[^a-z]*$") && st.length() != 0){
                    if(!firstline){
                        bw.flush();
                        bw.close();
                    }
                   // System.out.printf(st+" matched regex\n");
                     title = st.replace(" ","_").toLowerCase();
                    // bw = new PrintWriter("src/main/resources/Data/GrimmsFairytale/"+title+".txt");
                     newfile = new File("src/main/resources/Data/GrimmsFairytale/"+title+".txt");
                     bw = new BufferedWriter(new FileWriter(newfile));
                     firstline = false;
                }
                else {
                    if(!prev.equals(title.toUpperCase().replace("_"," "))) {
                        if (st.length() == 0) {
                            //System.out.println(prev+ " "+title.toUpperCase().replace("_"," "));
                            bw.write("\n\n");
                        } else {
                            if (prev.length() != 0)
                                bw.write(" ");
                        }
                    }
                    bw.write(st);
                }
                prev = st;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]){
        clean_data();
    }
}
