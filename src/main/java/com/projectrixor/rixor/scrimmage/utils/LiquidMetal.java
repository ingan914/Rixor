package com.projectrixor.rixor.scrimmage.utils;

public class LiquidMetal
        /**
         * Created by @Anxuiz
         */

{
    public static final double SCORE_NO_MATCH = 0.0D;
    public static final double SCORE_MATCH = 1.0D;
    public static final double SCORE_TRAILING = 0.8D;
    public static final double SCORE_TRAILING_BUT_STARTED = 0.9D;
    public static final double SCORE_BUFFER = 0.85D;

    public static final double score(String string, String abbreviation)
    {
        if (abbreviation.length() == 0) return 0.8D;
        if (abbreviation.length() > string.length()) return 0.0D;

        double[] scores = buildScoreArray(string, abbreviation);

        if (scores == null) {
            return 0.0D;
        }

        double sum = 0.0D;
        for (double score : scores) {
            sum += score;
        }

        return sum / scores.length;
    }

    private static final double[] buildScoreArray(String string, String abbreviation) {
        double[] scores = new double[string.length()];
        String lower = string.toLowerCase();
        String chars = abbreviation.toLowerCase();

        int lastIndex = -1;
        boolean started = false;
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            int index = lower.indexOf(c, lastIndex + 1);

            if (index == -1) return null;
            if (index == 0) started = true;

            if (isNewWord(string, index)) {
                scores[(index - 1)] = 1.0D;
                fillArray(scores, 0.85D, lastIndex + 1, index - 1);
            } else if (isUpperCase(string, index)) {
                fillArray(scores, 0.85D, lastIndex + 1, index);
            } else {
                fillArray(scores, 0.0D, lastIndex + 1, index);
            }

            scores[index] = 1.0D;
            lastIndex = index;
        }

        double trailingScore = started ? 0.9D : 0.8D;
        fillArray(scores, trailingScore, lastIndex + 1, scores.length);
        return scores;
    }

    private static final boolean isNewWord(String string, int index) {
        if (index == 0) return false;
        char c = string.charAt(index - 1);
        return (c == ' ') || (c == '\t');
    }

    private static final void fillArray(double[] array, double value, int from, int to) {
        for (int i = from; i < to; i++)
            array[i] = value;
    }

    private static final boolean isUpperCase(String string, int index)
    {
        char c = string.charAt(index);
        return ('A' <= c) && (c <= 'Z');
    }

    public static final void test() {
        System.out.print(score("FooBar", "foo"));
        System.out.print(score("FooBar", "fb"));
        System.out.print(score("Foo Bar", "fb"));
        System.out.print(score("Foo Bar", "baz"));
        System.out.print(score("Foo Bar", ""));
    }
}
