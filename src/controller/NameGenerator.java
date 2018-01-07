package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NameGenerator {

    private static List<String> SYLLABLES = Arrays.asList("far", "gon", "lin", "mor", "pyl", "hala", "ra");
    private final static Random random = new Random();
    
    public static String generateName() {

        int numSyllables = 2 + random.nextInt(2);
        StringBuilder builder = new StringBuilder(numSyllables*4);

        for (int i = 0; i < numSyllables; ++i) {
            builder.append(SYLLABLES.get(random.nextInt(SYLLABLES.size())));
        }
        builder.replace(0, 1, builder.substring(0, 1).toUpperCase());
        return builder.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 40; ++i) {
            System.out.println(generateName());
        }
    }
}
