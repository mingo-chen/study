package cm.study.java.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapStudy {

    public static void main(String[] args) {
        Map<String, Integer> scores = new LinkedHashMap<>(16);
//        Map<String, Integer> scores = new HashMap<>();

        scores.put("cm", 100);
        scores.put("cy", 150);
        scores.put("cxs", 145);

        System.out.println("--> " + scores.toString());
    }
}
