package com.fairytalener.rulebase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JWNLUtilityTest {

    @Test
    public void getDRF() {
        JWNLUtility jwnlUtility = new JWNLUtility();
        List<String> verbs = new ArrayList();
        verbs.add("took");
        jwnlUtility.getDRF(verbs);
    }

    @Test
    public void getSentenceFrame() {
        JWNLUtility jwnlUtility = new JWNLUtility();
        List<String> verbs = new ArrayList();
        verbs.add("sighed");
        jwnlUtility.getSentenceFrame(verbs,"VO");
    }

    @Test
    public void getAnimateBeings() {
    }
}