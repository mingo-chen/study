package cm.study.java.assembly.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 命令执行
 */
public class CmdInvoker {

    /**
     * 把客户端发送过来的命令调用相应的本地方法
     */
    public static Object call(String[] cmdInfo, CacheStore store) {
        String cmdName = cmdInfo[0];

        if(StringUtils.equalsIgnoreCase(cmdName, "search")) {
            if(cmdInfo.length == 2) {
                return store.get(cmdInfo[1]);
            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "set")) {
            if (cmdInfo.length == 3) {
                return store.set(cmdInfo[1], cmdInfo[2]);
            } else if(cmdInfo.length == 6) {
                boolean nxxx = StringUtils.equalsIgnoreCase(cmdInfo[3], "NX");
                boolean expx = StringUtils.equalsIgnoreCase(cmdInfo[4], "EX");
                long expire = NumberUtils.toLong(cmdInfo[5]);
                return store.setNxxxExpx(cmdInfo[1], cmdInfo[2], nxxx, expx, expire);
            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }
        } else if (StringUtils.equalsIgnoreCase(cmdName, "expire")) {
            if (cmdInfo.length == 3) {
                long expire = NumberUtils.toLong(cmdInfo[2]);
                return store.expire(cmdInfo[1], expire);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "hset")) {
            if (cmdInfo.length == 4) {
                return store.hSet(cmdInfo[1], cmdInfo[2], cmdInfo[3]);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "hget")) {
            if (cmdInfo.length == 3) {
                return store.hGet(cmdInfo[1], cmdInfo[2]);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "hgetall")) {
            if (cmdInfo.length == 2) {
                return store.hGetAll(cmdInfo[1]);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "zadd")) {
            if (cmdInfo.length == 4) {
                return store.zAdd(cmdInfo[1], NumberUtils.toLong(cmdInfo[2]), cmdInfo[3]);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "zcard")) {
            if (cmdInfo.length == 2) {
                return store.zCard(cmdInfo[1]);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "zscore")) {
            if (cmdInfo.length == 3) {
                return store.zScore(cmdInfo[1], cmdInfo[2]);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "zrange")) {
            if (cmdInfo.length == 4) {
                int start = NumberUtils.toInt(cmdInfo[2]);
                int stop = NumberUtils.toInt(cmdInfo[3]);
                return store.zRange(cmdInfo[1], start, stop);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "ttl")) {
            if (cmdInfo.length == 2) {
                return store.ttl(cmdInfo[1]);

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else if (StringUtils.equalsIgnoreCase(cmdName, "debug")) {
            if (cmdInfo.length == 1) {
                return store.debug();

            } else {
                return "cmd syntax error: #" + StringUtils.join(cmdInfo, " ") + "#";
            }

        } else {
            return "unknown cmd: " + cmdName;
        }

    }

}
