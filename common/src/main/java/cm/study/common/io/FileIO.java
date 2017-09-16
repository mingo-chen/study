package cm.study.common.io;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by chenming on 2017/9/16.
 */
public class FileIO {

    public static String read(String fileName) {
        InputStream inputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputStream = new FileInputStream(fileName);

            byte[] buf = new byte[1024];
            for(int read = 0; (read=inputStream.read(buf)) != -1; ) {
                String text = new String(buf, 0, read);
//                System.out.println("--> " + text);
                sb.append(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {inputStream.close();}catch (Exception e){}
            }
        }

        return sb.toString();
    }
}
