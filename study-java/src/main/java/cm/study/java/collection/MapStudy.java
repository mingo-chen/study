package cm.study.java.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapStudy {

    public static class SKey {
        private String key;

        public SKey(String key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SKey)) return false;
            SKey sKey = (SKey) o;
            return Objects.equals(key, sKey.key);
        }

        @Override
        public int hashCode() {
            // 写死, 这样为了观察所有value都在同一个Node链表上
            return 1024;
        }

        @Override
        public String toString() {
            return "SKey{" +
                   "key='" + key + '\'' +
                   '}';
        }
    }

    public static void main(String[] args) {
        Map<SKey, Integer> map = new HashMap<>(1);
        map.put(new SKey("aaa"), 1);
        System.out.println("---> " + map);

        map.put(new SKey("bbb"), 2);
        System.out.println("---> " + map);

        map.put(new SKey("ccc"), 3);
        System.out.println("---> " + map);

        map.put(new SKey("ddd"), 4);
        System.out.println("---> " + map);
    }
}
