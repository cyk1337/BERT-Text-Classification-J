import model.*;
import processor.BertProcess;
import processor.InputFeature;
import processor.textCNNProcess;
import utils.*;

import java.util.ArrayList;
import java.util.Scanner;


public class EnsembleDemo {

    protected TextCNN textcnn;
    protected Bert bert;
    protected float[] textcnn_prob;
    protected float[] bert_prob;


    public EnsembleDemo() {
        this.loadModels(); // load models and keep it in the memory

    }

    public void loadModels() {
        System.out.println("====================== Start loading models ... ====================== ");
        this.textcnn = this._load_TextCNNs();
        this.bert = this._load_Bert();
        System.out.println("====================== Models loaded! ======================");
    }

    public TextCNN _load_TextCNNs() {
        String textcnn_model_path = "pb_model/text_cnn.pb";
        TextCNN textcnn = new TextCNN(textcnn_model_path);
        return textcnn;
    }

    public Bert _load_Bert() {
        String bert_model_path = "pb_model/bert_L8_FC3_Seq128.pb";
        Bert bert = new Bert(bert_model_path);
        return bert;
    }

    public void doPredictWithModels(String text, int cnn_max_seq, int bert_max_seq) {
        this.textcnn_prob = this.__pred_with_textCNNs(this.textcnn, text, cnn_max_seq);
        // output textCNNs result
        System.out.println("======================textCNN predictions ======================");
        PredictionUtils.printPerLabelProb(this.textcnn_prob);
        String pred_label_cnn = PredictionUtils.getLabelName(this.textcnn_prob);
        System.out.println(pred_label_cnn);

        this.bert_prob = this.__pred_with_Bert(this.bert, text, bert_max_seq);
        // output bert result
        System.out.println("====================== Bert predictions ======================");
        PredictionUtils.printPerLabelProb(this.bert_prob);
        String pred_label_bert = PredictionUtils.getLabelName(this.bert_prob);
        System.out.println(pred_label_bert);

    }


    public float[] __pred_with_textCNNs(TextCNN textcnn, String text, int cnn_max_seq) {
        //   Run TextCNN
        System.out.println("====================== Start textCNN preprocessor! ======================");
        long cnn_pro_time = System.currentTimeMillis();
        textCNNProcess processor = new textCNNProcess(text, cnn_max_seq);
        InputFeature inputfeat = processor.getInputFeatures();
        System.out.println("TextCNN 预处理耗时 : " + (System.currentTimeMillis() - cnn_pro_time) / 1000f + " 秒 ");

        long cnn_run_time = System.currentTimeMillis();
        float[] prob = textcnn.predict_single_case(inputfeat);
        System.out.println("TextCNN 预测执行耗时 : " + (System.currentTimeMillis() - cnn_run_time) / 1000f + " 秒 ");
        return prob;

    }

    public float[] __pred_with_Bert(Bert bert, String text, int bert_max_seq) {
        System.out.println("====================== Start Bert preprocessor! ======================");
        long bert_pro_time = System.currentTimeMillis();
        BertProcess bert_processor = new BertProcess(text, bert_max_seq);
        InputFeature bert_feat = bert_processor.getInputFeatures();
        System.out.println("Bert 预处理执行耗时 : " + (System.currentTimeMillis() - bert_pro_time) / 1000f + " 秒 ");

        long bert_pred_time = System.currentTimeMillis();
        float[] prob = bert.predict_single_case(bert_feat);
        String[] bert_labels = bert.getBertLabel();
        float[] std_prob_bert = PredictionUtils.getProbInLabelSequence(bert_labels, PredictionUtils.labelList, prob);
        System.out.println("Bert 预测执行耗时 : " + (System.currentTimeMillis() - bert_pred_time) / 1000f + " 秒 ");
        return std_prob_bert;
    }

    public String _doVotingWithWeight() {
        //    Do ensemble vote with the order of textCNN/Bert/...
        float[] weightedVote = {.25f, .5f};
        ArrayList<float[]> prob_list = new ArrayList<float[]>();
        prob_list.add(this.textcnn_prob);
        prob_list.add(this.bert_prob);

        System.out.println("====================== Weighted voting results  ======================");
        float[] weight_prob = EnsembleUtils.doWeightedVote(weightedVote, prob_list);
        PredictionUtils.printPerLabelProb(weight_prob);
        String pred_label_weightedVote = PredictionUtils.getLabelName(weight_prob);
        System.out.println(pred_label_weightedVote);
        return pred_label_weightedVote;
    }

    public static void main(String[] args) {
        System.out.println("Starting to predict!");
        int cnn_max_seq = 30, bert_max_seq = 128; //  hyper-parameter
        EnsembleDemo demo = new EnsembleDemo();
        Scanner scn = new Scanner(System.in);
//        我要听小猪佩奇的故事。
        while (true) {
            System.out.println("请输入测试话术 (输入 exit 结束):");
            String X = scn.nextLine().trim();
            if (X.equals("exit")) {
                System.out.println("Bye!");
                break;
            }
            demo.doPredictWithModels(X, cnn_max_seq, bert_max_seq);
            demo._doVotingWithWeight();

        }
    }
}

