package processor;

import org.apache.commons.lang.ArrayUtils;
import utils.PredictionUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class BertProcess extends BasePreprocess {
    public BertProcess(String text, int max_seq) {
        super(text, max_seq);

        String bert_pad_mode = "post";
        String UNK_SYMBOL = "[unk]";
        String PAD_SYMBOL = "[pad]";

        HashMap<String, Integer> vocab = PredictionUtils.bert_vocab_dict; // load bert vocab
        processedText = this.doLowercase(text); // do lowercase

        Tokenizer tokenizer = new Tokenizer(vocab, UNK_SYMBOL);
        ArrayList<String> tokArrays = tokenizer.tokenize(processedText);

        this.textCharList = this.getTokenList_bert(tokArrays);
        this.printProcessedText();
        this.input_ids = this.charList2InputIds(this.textCharList, vocab, UNK_SYMBOL, PAD_SYMBOL, bert_pad_mode); // do padding
        this.printInputIds();
        int validLen = this.getValidLength();
        int[] input_mask = ArrayUtils.addAll(fillArray(1, validLen), fillArray(0, max_seq - validLen));
        int[] seg_ids = fillArray(0, max_seq);
        this.inputFeat = this.doConvertToFeatures(input_ids, input_mask, seg_ids); // produce features
    }

    public ArrayList<String> getTokenList_bert(ArrayList<String> subTokens) {
        ArrayList<String> tokenList = new ArrayList<String>();
        tokenList.add("[cls]");
        for (int i = 0; i < subTokens.size(); i++) {
            if (i >= this.max_seq - 2) break;    //  cut if exceeds max sequence char length
            String subTok = subTokens.get(i);
            tokenList.add(subTok);
        }
        tokenList.add("[sep]");
        return tokenList;
    }

    protected InputFeature doConvertToFeatures(int[] input_ids, int[] input_mask, int[] seg_ids) {
        return new InputFeature(input_ids, input_mask, seg_ids, this.text);
    }
}
