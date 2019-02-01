package processor;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePreprocess {
    protected String text;
    protected ArrayList<String> textCharList;
    protected InputFeature inputFeat;
    protected int[] input_ids;
    protected String processedText;
    protected int max_seq;


    public BasePreprocess(String text, int max_seq) {
        this.text = text;
        this.max_seq = max_seq;
        this.input_ids = new int[max_seq];
        this.processedText = text;
    }


    public String doTextNormalization(String text) {
        return text;
    }

    public String doSegmentation(String text) {
        return text;
    }

    public String doLowercase(String text) {
        return text.toLowerCase().trim();
    }

    public String doRemovePunctuation(String text) {
//        去除所有标点
        return text.replaceAll("[\\pP\\p{Punct}]", "").trim();
    }


    public String currentPunctToBeFixed(String text) {
        //        当前过滤的标点, 未考虑到所有情况。需重新规划！
        return text.replaceAll("[., 。'\"，;]", "").trim();
    }

    public String doReplaceDigit(String text, String DIGIT_REPLACEMENT) {
// replace the continuous digits with a `<number>` symbol
        Pattern digitPat = Pattern.compile("\\d+");
        Matcher m = digitPat.matcher(text);
        return m.replaceAll(DIGIT_REPLACEMENT).trim();
    }

    public ArrayList<String> str2CharList_with_replace_digits(String text, String DIGIT_REPLACEMENT) {
        ArrayList<String> textCharList = new ArrayList<String>();
        String[] arr = text.split(DIGIT_REPLACEMENT);
        int token_cnt = 0;
        for (int i = 0; i < arr.length; i++) {
            String subStr = arr[i];
            for (int j = 0; j < subStr.length(); j++) {
                if (token_cnt >= max_seq) return textCharList;
                char c = subStr.charAt(j);
                textCharList.add(String.valueOf(c));
                token_cnt += 1;

            }
            if (i < arr.length - 1 && token_cnt < max_seq) {
                textCharList.add(DIGIT_REPLACEMENT);
                token_cnt += 1;
            }
        }


        return textCharList;
    }


    public int[] charList2InputIds(ArrayList<String> textCharList, HashMap<String, Integer> vocab, String UNK_SYMBOL, String PAD_SYMBOL, String pad_mode) {
        int unk_id = vocab.get(UNK_SYMBOL);
        int pad_id = vocab.get(PAD_SYMBOL);
        int validLen = textCharList.size();
        int[] valid_arr = new int[validLen];

        for (int i = 0; i < validLen; i++) {
            String c = textCharList.get(i);
            Integer c_val = vocab.get(c);
            if (c_val == null) {
                valid_arr[i] = unk_id;
            } else {
                valid_arr[i] = vocab.get(c);
            }
        }
        if (pad_mode == "valid") {
            return valid_arr;
        }

        if (max_seq > validLen) {
            int[] pad_arr = new int[max_seq - validLen];
            for (int i = 0; i < max_seq - validLen; i++) {
                pad_arr[i] = pad_id;
            }

            if (pad_mode == "pre") {
                return ArrayUtils.addAll(pad_arr, valid_arr);
            } else if (pad_mode == "post") {
                return ArrayUtils.addAll(valid_arr, pad_arr);
            }
        }
        return valid_arr;
    }


    public int[] fillArray(int value, int len) {
        int[] arr = new int[len];
        Arrays.fill(arr, value);
        return arr;
    }

    public InputFeature getInputFeatures() {
        return this.inputFeat;
    }

    public ArrayList<String> getTextCharList() {
        return this.textCharList;
    }

    public String getProcessedText() {
        return this.processedText;
    }

    public int getValidLength() {
        return this.textCharList.size();
    }

    public void printProcessedText() {
        String outStr = "";
        for (int i = 0; i < this.textCharList.size(); i++) {
            outStr += this.textCharList.get(i) + " ";
        }
        System.out.println("Processed tokens:" + outStr);
    }

    public void printInputIds() {
        String outStr = "";
        for (int i : this.input_ids) {
            outStr += i + " ";
        }
        System.out.println("Input ids:" + outStr);
    }

}
