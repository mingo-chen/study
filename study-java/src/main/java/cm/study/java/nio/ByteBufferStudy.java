package cm.study.java.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteBufferStudy {

    public void usage() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(21);
//        byte[] data = "中华人民共和国".getBytes("UTF-8");
//        buffer.put(data, 0, data.length);

        buffer.put(char2byte2('中'));
        buffer.put(char2byte2('华'));
        buffer.put(char2byte2('人'));
        buffer.put(char2byte2('民'));
        buffer.put(char2byte2('共'));
        buffer.put(char2byte2('和'));
        buffer.put(char2byte2('国'));

        System.out.println("--> " + buffer.position());
        System.out.println("--> " + new String(buffer.array()));
    }

    public byte[] char2byte(char ch) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(1);
        cb.put(ch);
        cb.flip();

        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    /**
     * 一个java char按utf8编码为byte流
     * @param ch
     * @return
     */
    public byte[] char2byte2(char ch) {
        byte[] bts;
        if (ch <= 0x7F) {

            byte bt0 = (byte) (ch & 0x7F);
            bts = new byte[]{bt0};

        } else if (ch <= 0x7FF) {
            byte bt0 = (byte) (ch & 0x3F | 0x80);
            byte bt1 = (byte) ((ch >> 6) | 0xC0);
            bts = new byte[]{bt1, bt0};

        } else if (ch <= 0xFFFF) {
            byte bt0 = (byte) (ch & 0x3F | 0x80);
            byte bt1 = (byte) ((ch >> 6) & 0x3F | 0x80);
            byte bt2 = (byte) ((ch >> 12) | 0xE0);
            bts = new byte[]{bt2, bt1, bt0};

        } else if (ch <= 0x10FFFF) {
            byte bt0 = (byte) (ch & 0x3F | 0x80);
            byte bt1 = (byte) ((ch >> 6) & 0x3F | 0x80);
            byte bt2 = (byte) ((ch >> 12) & 0x3F | 0x80);
            byte bt3 = (byte) ((ch >> 18) | 0xF0);
            bts = new byte[]{bt3, bt2, bt1, bt0};
        } else {
            bts = new byte[0];
        }

        return bts;
    }

    public static void main(String[] args) throws Exception {
        ByteBufferStudy study = new ByteBufferStudy();
        study.usage();

        for (char ch : new char[]{'中','华','人','民','共','和','国'}) {
            System.out.println(ch + "[1] --> " + Arrays.toString(study.char2byte(ch)));
            System.out.println(ch + "[2] --> " + Arrays.toString(study.char2byte2(ch)));
            System.out.println("$$$$$$");
        }

    }
}
