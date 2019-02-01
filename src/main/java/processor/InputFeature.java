package processor;

public class InputFeature {

    public int[] input_ids;
    public int[] input_mask;
    public int[] seg_ids;
    public String text;

    public InputFeature(int[] input_ids, String text) {
        this.input_ids = input_ids;
        this.text = text;
    }

    public InputFeature(int[] input_ids, int[] input_mask, int[] seg_ids, String text) {
        this.input_ids = input_ids;
        this.input_mask = input_mask;
        this.seg_ids = seg_ids;
        this.text = text;
    }


}
