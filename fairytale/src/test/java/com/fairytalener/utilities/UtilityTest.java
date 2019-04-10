package com.fairytalener.utilities;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UtilityTest {

    @Test
    public void addToList() {
    }

    @Test
    public void isSubstring() {
        List<String> characters = new ArrayList<>();
        Utility utility = new Utility();
        characters.add("Thomson");
        characters.add("Mr. Peter Thomson");

        assertTrue(utility.isSubstring(characters,"Mr. Peter"));
        assertTrue(utility.isSubstring(characters,"Mr. Thomson"));
        assertFalse(utility.isSubstring(characters,"Someone"));
        assertTrue(utility.isSubstring(characters,"Mr. Peter Thomson"));
        //assertFalse(utility.isSubstring(characters,""));
    }

    @Test
    public void getMatchingStrings() {
    }
}