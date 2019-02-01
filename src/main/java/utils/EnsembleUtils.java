package utils;

import com.google.common.base.Preconditions;

import java.util.List;

public class EnsembleUtils {
    /*
            do ensemble learning
     */
    public static float[] doWeightedVote(float[] weight, List<float[]> prob_list) {
        Preconditions.checkNotNull(weight);
        Preconditions.checkNotNull(prob_list);

        int n = prob_list.get(0).length;
        float[] weightedVote_prob = new float[n];
        for (int k = 0; k < weight.length; k++) {
            weightedVote_prob[k] = 0;
        }

        for (int i = 0; i < weight.length; i++) {
            float p = weight[i];
            float[] model_prob = prob_list.get(i);
            for (int j = 0; j < model_prob.length; j++) {
                weightedVote_prob[j] += p * model_prob[j];
            }
        }
        return weightedVote_prob;
    }
}
