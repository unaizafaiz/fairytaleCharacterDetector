package com.fairytalener.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Utility {
    List<String> globalchar = new ArrayList<>();

    public List<String> addToList(List<String> characters, List<String> temp) {
        this.globalchar = characters;
        temp.forEach(charac -> {
            if(!isSubstring(globalchar, charac)) {
               String superstring = isSuperString(globalchar,charac);
               if(superstring!=null){
                   globalchar.remove(superstring);
                   globalchar.add(charac);
               }
               else {

                   if (!globalchar.contains(charac))
                       globalchar.add(charac);
               }
            }
        } );
        return globalchar;
    }

    public boolean isSubstring(List<String> characters, String charac) {
        Boolean contain = true;
        for (String character : characters) {
            contain = true;
            if(character.contains(charac)) {
                System.out.println(character+" contains "+charac);
                return true;
            }else{
                String[] temp = charac.split(" ");
                for(String each: temp){
                    if(!character.contains(each)){
                        contain = false;
                        break;
                    }
                }
                if(contain){
                    return contain;
                }
                //return contain;
            }
        }

        return false;
    }

    public String isSuperString(List<String> characters, String charac) {
        for (String character : characters) {
            if(charac.indexOf(character)!=-1) {
                System.out.println(charac+" index of "+character+" = "+charac.indexOf(character));
                return character;
            }
        }
        return null;
    }


    /**
     * Finds the index of all entries in the list that matches the regex
     * @param list The list of strings to check
     * @param regex The regular expression to use
     * @return false if matching entries present and true otherwise
     */
    public Boolean getMatchingStrings(List<String> list, String regex) {

        //  ArrayList<String> matches = new ArrayList<String>();

        Pattern p = Pattern.compile(regex);

        for (String s:list) {
            if (p.matcher(s).matches()) {
                //System.out.println(s);
                // matches.add(s);
                return false;
            }
        }

        return true;
    }

}
