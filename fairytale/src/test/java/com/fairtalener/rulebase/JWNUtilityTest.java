package com.fairtalener.rulebase;

import com.fairytalener.rulebase.JWNLUtility;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JWNUtilityTest {

    @Test
    public void testDRF(){
        JWNLUtility jwnlUtility = new JWNLUtility();
        List<String> verbs = new ArrayList<>();
        verbs.add("said");
        verbs.add("replied");
        verbs.add("ripped");
       // verbs.add("dash");
        System.out.println(jwnlUtility.getDRF(verbs));

    }
}
