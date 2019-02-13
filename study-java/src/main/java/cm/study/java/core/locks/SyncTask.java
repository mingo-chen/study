package cm.study.java.core.locks;

import java.util.List;

public interface SyncTask {

    void sayA();

    void sayB();

    void sayC();

    List<String> getOutput();
}
