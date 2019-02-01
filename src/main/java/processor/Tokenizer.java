package processor;

import utils.WordTokenizer;
import utils.WordpieceTokenizer;

import java.util.ArrayList;
import java.util.HashMap;

public class Tokenizer {
    public HashMap<Integer, String> inv_vocab_dict;
    public WordTokenizer basicTok = new WordTokenizer();
    public WordpieceTokenizer wpTok;


    public Tokenizer(HashMap<String, Integer> vocab_dict, String UNK_SYMBOL) {
        this.inv_vocab_dict = inverseDict(vocab_dict);
        this.wpTok = new WordpieceTokenizer(vocab_dict, UNK_SYMBOL);

    }

    public ArrayList<String> tokenize(String text) {
        ArrayList<String> split_tokens = new ArrayList<String>();
        for (String token:this.basicTok.tokenize(text)
             ) {
            ArrayList<String> subTokens = this.wpTok.tokenize(token);
            for (int i = 0; i < subTokens.size(); i++) {
                String sub_token = subTokens.get(i);
                split_tokens.add(sub_token);
            }
        }
        return split_tokens;
    }

    public HashMap<Integer, String> inverseDict(HashMap<String, Integer> vocab) {
        HashMap<Integer, String> inv_vocab = new HashMap<Integer, String>();
        for (String k : vocab.keySet()
        ) {
            inv_vocab.put(vocab.get(k), k);
        }
        return inv_vocab;
    }

}
