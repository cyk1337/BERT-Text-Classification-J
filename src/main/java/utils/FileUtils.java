package utils;


import com.google.common.base.Preconditions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileUtils {
    public static ArrayList<String> loadLabelList(String path) {
        Preconditions.checkNotNull(path);
        ArrayList<String> arrLst = new ArrayList<String>();

        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String s;
            while ((s = bf.readLine()) != null) {
                if (s.length() < 1) continue;
                arrLst.add(s);
            }
            bf.close();
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrLst;

    }

    public static HashMap<String, Integer> loadVocab(String path) {
        Preconditions.checkNotNull(path);

        HashMap<String, Integer> vocab_dict = new HashMap<String, Integer>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String s;
            int i = 0;
            while ((s = bf.readLine()) != null) {
                if (s.length() < 1) continue;
                String word = s.trim().toLowerCase();
                Integer word_id = new Integer(i++);
                vocab_dict.put(word, word_id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vocab_dict;
    }

}
