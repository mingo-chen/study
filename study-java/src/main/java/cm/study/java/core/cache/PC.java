package cm.study.java.core.cache;

import cm.study.java.core.utils.T;

import java.util.concurrent.TimeUnit;

public class PC {

    private RW memory;

    private DataBus bus;

    public PC() {
        this.memory = new RAM();
        this.bus = new DataBus(this.memory);
    }

    public void addCore(CpuCore core) {
        this.bus.addCore(core);
    }

    public static void main(String[] args) {
        PC pc = new PC();

        // 4核心
        CpuCore cpuCore1 = new CpuCore("C001", pc.bus);
        CpuCore cpuCore2 = new CpuCore("C002", pc.bus);
        CpuCore cpuCore3 = new CpuCore("C003", pc.bus);
        CpuCore cpuCore4 = new CpuCore("C004", pc.bus);

        pc.addCore(cpuCore1);
        pc.addCore(cpuCore2);
        pc.addCore(cpuCore3);
        pc.addCore(cpuCore4);

        pc.memory.store("name", "cm");
//        syncTest(pc, cpuCore1, cpuCore2, cpuCore3, cpuCore4);
        asyncTest(pc, cpuCore1, cpuCore2, cpuCore3, cpuCore4);
    }

    public static void syncTest(PC pc, CpuCore cpuCore1, CpuCore cpuCore2, CpuCore cpuCore3, CpuCore cpuCore4) {
        cpuCore1.load("name");
        cpuCore2.load("name");
        cpuCore3.store("name", "ljx");
        cpuCore1.load("name");
        pc.bus.debug();
    }

    public static void asyncTest(PC pc, CpuCore cpuCore1, CpuCore cpuCore2, CpuCore cpuCore3, CpuCore cpuCore4) {
        cpuCore1.addTask(() -> {
            cpuCore1.load("name");
            T.sleep(TimeUnit.SECONDS, 2);
            cpuCore1.load("name");
        });

        cpuCore2.addTask(() -> cpuCore2.load("name"));

        cpuCore3.addTask(() -> {
            T.sleep(TimeUnit.SECONDS, 1);
            cpuCore3.store("name", "ljx");
        });

        T.sleep(TimeUnit.SECONDS, 3);
        pc.bus.debug();
    }
}
