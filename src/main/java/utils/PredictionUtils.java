package utils;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PredictionUtils {
    static String base_dir = "./src/main/resources/";
    static String label_path = base_dir + "label/label.txt";
    public static ArrayList<String> labelList = FileUtils.loadLabelList(label_path);
    ;


    static String vocab_path = base_dir + "vocab/vocab.txt";
    public static HashMap<String, Integer> vocab_dict = FileUtils.loadVocab(vocab_path);


    static String bert_vocab_path = base_dir + "vocab/bert_vocab.txt";
    public static HashMap<String, Integer> bert_vocab_dict = FileUtils.loadVocab(bert_vocab_path);

    public static String getLabelName(float[] prob) {
        int label_id = TensorUtils.argmax(prob);
        String pred_label = labelList.get(label_id);
        return pred_label;
    }

    public static void printPerLabelProb(float[] prob) {
        Preconditions.checkNotNull(prob);
        String resultStr = "";
        String sepStr = "|";
        for (int i = 0; i < prob.length; i++) {
            String str = String.format("%s:%.6f%s ", labelList.get(i), prob[i], sepStr);
            resultStr += str;
        }
        System.out.println(resultStr);
    }

    public static float[] getProbInLabelSequence(String[] raw_label_list, ArrayList<String> std_label_list, float[] pred_prob) {
        float[] out_prob = new float[pred_prob.length];
        HashMap<String, Float> prob_map = new HashMap<String, Float>();
        for (int i = 0; i < pred_prob.length; i++) {
            prob_map.put(raw_label_list[i], pred_prob[i]);
        }

        for (int i = 0; i < pred_prob.length; i++) {
            out_prob[i] = prob_map.get(std_label_list.get(i));
        }

        return out_prob;
    }

}
