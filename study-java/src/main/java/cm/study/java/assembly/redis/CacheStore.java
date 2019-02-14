package cm.study.java.assembly.redis;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据过期实现
 * 懒汉+异步批量
 */
public class CacheStore {
    public static final long PERMANENT = -1L;

    Map<String, CacheValue> dataStore = new HashMap<>();

    public static void assertValueType(CacheValue cacheValue, ValueType except) {
        if (cacheValue.type != except) {
            throw new RuntimeException("unsupport operator for target data");
        }
    }

    public String get(String key) {
        CacheValue value = dataStore.get(key);
        if (null == value) {
            return "";
        } else {
            assertValueType(value, ValueType.String);

            if(value.expireAt != PERMANENT && System.currentTimeMillis() > value.expireAt) {
                return "";
            } else {
                return (String) value.data;
            }

        }
    }

    public int set(String key, String value) {
        CacheValue cacheValue = new CacheValue(value, ValueType.String, PERMANENT);
        dataStore.put(key, cacheValue);
        return 1;
    }

    public int setNx(String key, String value) {
        CacheValue cacheValue = new CacheValue(value, ValueType.String, PERMANENT);
        CacheValue retValue = dataStore.putIfAbsent(key, cacheValue);
        return retValue == null ? 1 : 0;
    }

    /**
     * set命令完整版
     * @param key
     * @param value
     * @param nxxx: true -> nx, not exist, false -> xx, exist
     * @param expx: true -> ex, second, false -> px, millis
     * @param expire
     * @return
     */
    public int setNxxxExpx(String key, String value, boolean nxxx, boolean expx, long expire) {
        long expireAt = expx ? System.currentTimeMillis() + expire * 1000 : System.currentTimeMillis() + expire;
        CacheValue newValue = new CacheValue(value, ValueType.String, expireAt);

        if(nxxx) {
            CacheValue retValue = dataStore.putIfAbsent(key, newValue);
            return retValue == null ? 1 : 0;
        } else { // 只有存在, 才能写入成功
            CacheValue oldValue = dataStore.get(key);
            if (oldValue != null) {
                assertValueType(oldValue, ValueType.String);
                dataStore.put(key, newValue);
                return 1;

            } else {
                return 0;
            }
        }

    }

    public Map<String, String> hGetAll(String key) {
        CacheValue value = dataStore.get(key);
        if (null == value) {
            return new HashMap<>();
        } else {
            assertValueType(value, ValueType.Map);

            if(value.expireAt != PERMANENT && System.currentTimeMillis() > value.expireAt) {
                return new HashMap<>();

            } else {
                return (Map<String, String>) value.data;
            }
        }

    }

    public String hGet(String key, String field) {
        Map<String, String> map = hGetAll(key);
        if (map == null) {
            return "";
        } else {
            return map.get(field);
        }
    }

    public List<String> hMget(String key, String... fields) {
        List<String> values = new ArrayList<>();
        Map<String, String> map = hGetAll(key);
        if (map == null) {
            return new ArrayList<>();
        } else {
            for (String field : fields) {
                values.add(map.get(field));
            }
        }

        return values;
    }

    public int hSet(String key, String field, String value) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            cacheValue = new CacheValue(new HashMap<>(), ValueType.Map, PERMANENT);
            dataStore.put(key, cacheValue);
        }

        Object ret = ((Map) (cacheValue.data)).put(field, value);
        return ret == null ? 1 : 0;
    }

    public int hMset(String key, Map<String, String> mpairs) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            cacheValue = new CacheValue(new HashMap<>(), ValueType.Map, PERMANENT);
            dataStore.put(key, cacheValue);
        }

        ((Map)(cacheValue.data)).putAll(mpairs);
        return 1;
    }

    public void sAdd(String key, String... members) {
        CacheValue cacheValue = dataStore.get(key);
        if (null == cacheValue) {
            Set<String> data = new HashSet<>(Arrays.asList(members));
            cacheValue = new CacheValue(data, ValueType.Set, PERMANENT);
            dataStore.put(key, cacheValue);
        } else {
            assertValueType(cacheValue, ValueType.Set);

            Set<String> data = (Set<String>) cacheValue.data;
            data.addAll(Arrays.asList(members));
        }
    }

    public boolean sIsMember(String key, String member) {
        CacheValue cacheValue = dataStore.get(key);
        if (null == cacheValue) {
            return false;

        } else {
            assertValueType(cacheValue, ValueType.Set);

            Set<String> data = (Set<String>) cacheValue.data;
            return data.contains(member);
        }
    }

    public String sPop(String key) {
        CacheValue cacheValue = dataStore.get(key);
        if (null == cacheValue) {
            return "";

        } else {
            assertValueType(cacheValue, ValueType.Set);

            Set<String> data = (Set<String>) cacheValue.data;
            if(data.size() > 0) {
                Iterator<String> it = data.iterator();
                String popElement = it.next();
                it.remove();
                return popElement;
            } else {

                return "";
            }
        }
    }

    public int sRem(String key, String... members) {
        CacheValue cacheValue = dataStore.get(key);
        if (null == cacheValue) {
            return 0;

        } else {
            assertValueType(cacheValue, ValueType.Set);

            Set<String> data = (Set<String>) cacheValue.data;
            int rem = 0;
            for (String member : members) {
                if(data.remove(member)) {
                    rem++;
                }
            }

            return rem;
        }
    }

    public int sCard(String key) {
        CacheValue cacheValue = dataStore.get(key);
        if (null == cacheValue) {
            return 0;

        } else {
            assertValueType(cacheValue, ValueType.Set);

            Set<String> data = (Set<String>) cacheValue.data;
            return data.size();
        }
    }

    public int zAdd(String key, long score, String member) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            Map<String, Long> data = new HashMap<>();
            data.put(member, score);
            cacheValue = new CacheValue(data, ValueType.Zset, PERMANENT);
            dataStore.put(key, cacheValue);
            return 1;

        } else {
            assertValueType(cacheValue, ValueType.Zset);

            Map<String, Long> data = (Map<String, Long>) cacheValue.data;
            Long oldValue = data.get(member);
            if(oldValue != null && oldValue == score) {
                return 0;
            } else {
                data.put(member, score);
                return 1;
            }
        }
    }

    public int zAdd(String key, Map<String, Long> members) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            cacheValue = new CacheValue(members, ValueType.Zset, PERMANENT);
            dataStore.put(key, cacheValue);
            return 1;

        } else {
            assertValueType(cacheValue, ValueType.Zset);

            Map<String, Long> data = (Map<String, Long>) cacheValue.data;
            int success = 0;

            for (Map.Entry<String, Long> entry : members.entrySet()) {
                Long oldValue = data.get(entry.getKey());
                if(oldValue == null || oldValue != entry.getValue()) {
                    success++;
                }
            }

            return success;
        }
    }

    public long zScore(String key, String member) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            return 0;
        } else {
            assertValueType(cacheValue, ValueType.Zset);

            Map<String, Long> data = (Map<String, Long>) cacheValue.data;
            return data.get(member);
        }
    }

    public int zCard(String key) {
        CacheValue cacheValue = dataStore.get(key);
        if (null == cacheValue) {
            return 0;

        } else {
            assertValueType(cacheValue, ValueType.Zset);

            Map<String, Long> data = (Map<String, Long>) cacheValue.data;
            return data.size();
        }
    }
    /**
     * 按score从小到大排序, 返回从start到stop之间的members
     */
    public List<String> zRange(String key, int start, int stop) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            return new ArrayList<>();
        } else {
            Map<String, Long> data = (Map<String, Long>) cacheValue.data;

            if (start < 0) { // 从后往前数
                start = data.size() + start;
            }

            if(stop > data.size()) {
                stop = data.size();
            } else if (stop < 0) {
                stop = data.size() + stop;
            }

            if(start > data.size() || start > stop) {
                return new ArrayList<>();
            }

            List<Map.Entry<String, Long>> entries = new ArrayList<>(data.entrySet());

            // O(log(N))
            Collections.sort(entries, Comparator.comparingLong(entry -> entry.getValue()));

            // O(M)
            return entries.subList(start, stop+1)
                    .stream().map(entry -> entry.getKey()).collect(Collectors.toList());
        }

    }

    public int expire(String key, long seconds) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            return 0;
        } else {
            cacheValue.expireAt = System.currentTimeMillis() + seconds * 1000;
            return 1;
        }
    }

    public int ttl(String key) {
        CacheValue cacheValue = dataStore.get(key);
        if (cacheValue == null) {
            return -1;
        } else {
            long ttl = cacheValue.expireAt - System.currentTimeMillis();
            if (cacheValue.expireAt < 0 || ttl < 0) {
                return -1;
            } else { // 返回单位为秒
                return (int)(ttl / 1000);
            }
        }
    }

    public String debug() {
        return dataStore.toString();
    }

    public static class CacheValue {
        private Object data;

        private ValueType type;

        /**
         * 过期时间
         */
        private long expireAt;

        public CacheValue(Object data, ValueType type, long expireAt) {
            this.data = data;
            this.type = type;
            this.expireAt = expireAt;
        }

        @Override
        public String toString() {
            return "CacheValue{" +
                   "data=" + data +
                   ", type=" + type +
                   ", expireAt=" + expireAt +
                   '}';
        }
    }

    public enum ValueType {
        String, Set, Zset, List, Map;
    }

    public static class Tuple implements Comparable<Tuple> {
        String value;
        long score;

        public Tuple() {
        }

        public Tuple(String value, long score) {
            this.value = value;
            this.score = score;
        }

        @Override
        public int compareTo(Tuple o) {
            if (o == null) {
                return 1;
            } else {
                return Long.compare(this.score, o.score);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tuple)) return false;
            Tuple tuple = (Tuple) o;
            return score == tuple.score &&
                   Objects.equals(value, tuple.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, score);
        }

        @Override
        public String toString() {
            return "Tuple{" +
                   "value='" + value + '\'' +
                   ", score=" + score +
                   '}';
        }
    }
}
