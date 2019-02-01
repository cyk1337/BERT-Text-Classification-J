package model;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;


import processor.InputFeature;
import utils.*;

public abstract class BaseModel {

    protected Graph graph;
    protected Session sess;

    public BaseModel(String modelpath) {
        Preconditions.checkNotNull(modelpath);

        try {
            byte[] graphDef = ByteStreams.toByteArray(this.getClass().getClassLoader().getResourceAsStream(modelpath));

            this.graph = new Graph();
            this.graph.importGraphDef(graphDef);
            this.sess = new Session(graph);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return;
        }
    }


    public float[] predict_single_case(InputFeature inputfeat) {
        Preconditions.checkNotNull(inputfeat);
        Preconditions.checkNotNull(inputfeat.input_ids);

        Tensor tensor_input_ids = TensorUtils.createTensor(inputfeat.input_ids);

        Tensor t = this.sess.runner()
                .feed("input_x:0", tensor_input_ids)
                .fetch("score/my_output:0")
                .run().get(0);

        return TensorUtils.copyProb2list(t);
    }

}
