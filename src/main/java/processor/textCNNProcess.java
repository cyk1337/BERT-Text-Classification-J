package processor;

import utils.PredictionUtils;

import java.util.HashMap;
import java.util.regex.Pattern;

public class textCNNProcess extends BasePreprocess {

    public textCNNProcess(String text, int max_seq) {
        super(text, max_seq);

        String cnn_pad_mode = "pre";
        String DIGIT_REPLACEMENT = "<number>";
        String UNK_SYMBOL = "<unk>";
        String PAD_SYMBOL = "padding";
        Pattern digitPat = Pattern.compile("\\d+");

        processedText = this.doLowercase(text); // do lowercase
        processedText = this.currentPunctToBeFixed(processedText); // tobefixed: 当前模型预处理部分，存在问题（需要重新规划）
//        processedText = this.doRemovePunctuation(processedText); // remove all punctuations
        processedText = this.doReplaceDigit(processedText, DIGIT_REPLACEMENT); // replace digit with `<number>`
        this.textCharList = this.str2CharList_with_replace_digits(processedText, DIGIT_REPLACEMENT);
        this.printProcessedText();
        HashMap<String, Integer> vocab = PredictionUtils.vocab_dict; // load textCNN vocab
        this.input_ids = this.charList2InputIds(this.textCharList, vocab, UNK_SYMBOL, PAD_SYMBOL, cnn_pad_mode); // do padding
        this.printInputIds();
        this.inputFeat = this.doConvertToFeatures(this.input_ids); // produce features
    }

    protected InputFeature doConvertToFeatures(int[] input_ids) {
        return new InputFeature(input_ids, this.text);
    }

}
