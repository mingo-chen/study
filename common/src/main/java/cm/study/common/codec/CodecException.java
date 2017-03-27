package cm.study.common.codec;

/**
 * Created by chenming on 2017/3/25.
 */
public class CodecException extends RuntimeException {

    private static final long serialVersionUID = -4902891761158958502L;

    public CodecException() {
        super();
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(Throwable th) {
        super(th);
    }

    public CodecException(String message, Throwable th) {
        super(message, th);
    }
}
