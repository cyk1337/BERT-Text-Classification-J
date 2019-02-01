package utils;

import java.util.ArrayList;

public class WordTokenizer {
    private boolean doLowerCase = true;

    public WordTokenizer() {
    }

    public String[] tokenize(String text) {
        String[] output_tokens;
        String cleanedStr = this.cleanText(text.toLowerCase());
        output_tokens = cleanedStr.split(" ");
        return output_tokens;
    }

    public String cleanText(String text) {
        String cleanedStr = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String s = String.valueOf(c);
            int cp = Integer.valueOf(c);
            if (cp == 0xfffd || cp == 0) continue;
            else if (s.equals("\t") || s.equals("\n") || s.equals("\r")) cleanedStr += " "; // replace with whitespace
            else cleanedStr += s;
        }
        cleanedStr = tokenizeChinese_n_Punctuation(cleanedStr); // tokenize CJK chars
//        TODO: remove accent!

        // tokenize
        return cleanedStr;
    }

    public String tokenizeChinese_n_Punctuation(String text) {
//        add whitespace around CJK chars and punctuation
        String outStr = "";
        for (int i = 0; i < text.length(); i++) {
            if (isChineseChar(text.charAt(i)) || isPunctuation(text.charAt(i))) {
                outStr = outStr + " " + text.charAt(i) + " ";
            }else outStr += text.charAt(i);
        }
        return outStr;
    }

    public boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }


    public boolean isPunctuation(char c) {
        return String.valueOf(c).matches("[\\pP‘’“”]");
    }
}
