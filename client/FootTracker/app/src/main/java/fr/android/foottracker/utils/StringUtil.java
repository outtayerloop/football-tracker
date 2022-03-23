package fr.android.foottracker.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class StringUtil {

    private StringUtil() {
    }

    public static boolean isNullOrEmptyOrWhiteSpaceInput(@Nullable String input) {
        return input == null
                || input.isEmpty()
                || input.replaceAll(" ", "").equals("");
    }

    public static boolean areEquivalent(@NonNull String input1, @NonNull String input2) {
        return input1.equals(input2)
                || input1.toLowerCase().replaceAll(" ", "")
                .equals(input2.toLowerCase().replaceAll(" ", ""));
    }

    public static String toCapitalized(@NonNull String input) {
        final StringBuilder capitalizedOutput = new StringBuilder();
        final String[] inputSpacedComponents = input.split(" ");
        for (int i = 0; i < inputSpacedComponents.length; ++i)
            buildCapitalizedOutput(inputSpacedComponents, i, capitalizedOutput);
        return capitalizedOutput.toString();
    }

    private static void buildCapitalizedOutput(@NonNull String[] inputSpacedComponents, int i, @NonNull StringBuilder capitalizedOutput) {
        final String[] hyphenSubComponents = inputSpacedComponents[i].split("-");
        handleHyphenSeparatedComponents(hyphenSubComponents, capitalizedOutput);
        appendSeparator(inputSpacedComponents, capitalizedOutput, " ", i);
    }

    private static void handleHyphenSeparatedComponents(@NonNull String[] hyphenSubComponents, @NonNull StringBuilder capitalizedOutput) {
        for (int j = 0; j < hyphenSubComponents.length; ++j) {
            String capitalizedFirstLetter = hyphenSubComponents[j].substring(0, 1).toUpperCase();
            String lowerCasedRemainingLetters = hyphenSubComponents[j].substring(1).toLowerCase();
            capitalizedOutput.append(capitalizedFirstLetter).append(lowerCasedRemainingLetters);
            appendSeparator(hyphenSubComponents, capitalizedOutput, "-", j);
        }
    }

    private static void appendSeparator(String[] components, StringBuilder capitalizedOutput, String separator, int index) {
        if (index < components.length - 1)
            capitalizedOutput.append(separator);
    }

}
