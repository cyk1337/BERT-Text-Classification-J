package utils;

import org.tensorflow.Tensor;
import com.google.common.base.Preconditions;
import java.util.List;

import processor.InputFeature;

public class TensorUtils {
    public static Tensor createTensor(int[] input_ids){
        Preconditions.checkNotNull(input_ids);
        int [][] tensor_1d = new int[1][];
        tensor_1d[0] = input_ids;
        return Tensor.create(tensor_1d);
    }

    public static Tensor createTensor(List<InputFeature> listInputFeat){
        Preconditions.checkNotNull(listInputFeat);

        int [][] tensor_2d = new int[listInputFeat.size()][];
        for (int i=0; i<listInputFeat.size(); i++){
            tensor_2d[i] = listInputFeat.get(i).input_ids;
        }
        return Tensor.create(tensor_2d);

    }

    public static int argmax(float[] prob){
        Preconditions.checkNotNull(prob);
        double tmp = prob[0];
        int indice = 0;
        for (int i=1; i< prob.length; i++){
             if(prob[i] > tmp){
                 tmp = prob[i];
                 indice = i;
             }
        }
        return indice;
    }

    public static float[] copyProb2list(Tensor tensor){
        Preconditions.checkNotNull(tensor);

        int n = (int) tensor.shape()[1];
        float[][] matrix = new float[1][n];
        try {
            tensor.copyTo(matrix);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            tensor.close();
        }

        float[] prob = matrix[0];
        return prob;
    }
}
