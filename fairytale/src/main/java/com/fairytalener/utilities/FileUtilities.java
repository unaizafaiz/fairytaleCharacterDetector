package com.fairytalener.utilities;

import java.io.*;
import java.util.List;

public class FileUtilities {
    public String readFile(File file) {
        String filecontents = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;

            while ((st = br.readLine()) != null) {
                filecontents = filecontents + st;

            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filecontents;
    }

    public BufferedWriter writeToFile(String filename, List<String> characters, List<String> time, List<String> location, String outputfolder) {
        try {
            File newFile = new File(outputfolder+"/" + filename);
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));

            bw.write("CHARACTERS:\n\n");
            for (String character : characters) {
                bw.write(character);
                bw.newLine();
            }

           /* bw.write("\n\nTIME:\n\n");
            for (String t : time) {
                bw.write(t);
                bw.newLine();
            }

            bw.write("\n\nLOCATION:\n\n");
            for (String loc : location) {
                bw.write(loc);
                bw.newLine();
            }*/

            return bw;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File[] getFiles(String folderpath) {
        File folder = new File(folderpath);
        return folder.listFiles();
    }


}
