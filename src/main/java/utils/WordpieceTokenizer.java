package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class WordpieceTokenizer {
    private final int max_input_char_per_word = 200;
    protected HashMap<String, Integer> vocab;
    protected String UNK_SYMBOL;

    public WordpieceTokenizer(HashMap<String, Integer> vocab, String UNK_SYMBOL) {
        this.vocab = vocab;
        this.UNK_SYMBOL = UNK_SYMBOL;
    }

    public ArrayList<String> tokenize(String text) {
        ArrayList<String> tokenizedList = new ArrayList<String>();
        for (String token : this.whitespaceTokenize(text)) {
            if (token.length() > this.max_input_char_per_word) {
                tokenizedList.add(this.UNK_SYMBOL);
            }

            boolean NotMatch = false;
            int start = 0;
            ArrayList<String> sub_tokens = new ArrayList<String>();

            while (start < token.length()) {
                int end = token.length();
                String currentSubStr = null;
                while (start < end) {
                    String tmpStr = token.substring(start, end);
                    if (start > 0) {
                        tmpStr = "##" + tmpStr;
                    }
                    if (this.vocab.get(tmpStr) != null) {
                        currentSubStr = tmpStr;
                        break;
                    }
                    end -= 1;
                }
                if (currentSubStr == null) {
                    NotMatch = true;
                    break;
                }
                sub_tokens.add(currentSubStr);
                start = end;
            }
            if (NotMatch) {
                tokenizedList.add(this.UNK_SYMBOL);
            } else {
                for (String tok : sub_tokens) {
                    tokenizedList.add(tok);
                }
            }

        }

        return tokenizedList;
    }


    public String[] whitespaceTokenize(String text) {
        String[] out_tokens = text.split(" ");
        return out_tokens;
    }

}
