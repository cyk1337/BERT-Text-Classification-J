package model;

import com.google.common.base.Preconditions;
import org.tensorflow.Tensor;
import processor.InputFeature;
import utils.TensorUtils;

public class Bert extends BaseModel {
    public Bert(String model_path) {
        super(model_path);
    }

    public float[] predict_single_case(InputFeature inputfeat) {
        Preconditions.checkNotNull(inputfeat);
        Preconditions.checkNotNull(inputfeat.input_ids);

        Tensor tensor_input_ids = TensorUtils.createTensor(inputfeat.input_ids);
        Tensor tensor_input_mask = TensorUtils.createTensor(inputfeat.input_mask);
        Tensor tensor_seg_ids = TensorUtils.createTensor(inputfeat.seg_ids);

        Tensor t = this.sess.runner()
                .feed("input_ids:0", tensor_input_ids)
                .feed("input_mask:0", tensor_input_mask)
                .feed("segment_ids:0", tensor_seg_ids)
                .fetch("loss/Softmax:0")
                .run().get(0);
        return TensorUtils.copyProb2list(t);
    }

    public String[] getBertLabel(){
        String[] bert_labels = {"alerts", "baike", "calculator", "call", "car_limit", "chat", "cook_book", "fm", "general_command",
                "home_command", "music", "news", "shopping", "stock", "time", "translator", "video", "weather"};
        return bert_labels;
    }


}
