package com.fairytalener.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

public class StanfordParserTest {

    @Test
    public void getNouns() {
        StanfordParser stanfordParser = new StanfordParser();
        String output = stanfordParser.getNouns("an old woman");
        assert(output.equals("woman"));

        output = stanfordParser.getNouns("the sister jane");
        assert(output.equals("sister jane"));
    }

    @Test
    public void getHeadPhrase() {

        StanfordParser stanfordParser = new StanfordParser();
        String output = stanfordParser.getHeadPhrase("an old woman");
        System.out.println(output);
        assert(output.equals("old woman"));

        output = stanfordParser.getHeadPhrase("Mr. Gernmy Phrase");
        System.out.println(output);
        assert(output.equals("Mr. Gernmy Phrase"));
    }
}