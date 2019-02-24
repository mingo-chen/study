package cm.study.java.lang;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class XXXTest {

    @Test
    public void testMathAlgo() {
        int lo = 0;
        int fence = 6;
        int mid = ((lo + fence) >>> 1) & ~1;
        System.out.println("--> " + mid + ", " + ((lo + fence) >>> 1) + ", " + (3 & ~2));
    }
}