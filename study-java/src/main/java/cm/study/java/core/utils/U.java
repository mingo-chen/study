package cm.study.java.core.utils;

import java.util.Collection;
import java.util.Iterator;

public class U {

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            fail(message);
        }
    }

    public static void assertNull(Object target, String message) {
        assertTrue(target == null, message);
    }

    public static void assertNotNull(Object target, String message) {
        assertFalse(target == null, message);

    }

    public static void assertEquals(Object o1, Object o2, String message) {
        assertTrue(isEquals(o1, o2), message);
    }

    public static void assertNotEquals(Object o1, Object o2, String message) {
        assertFalse(isEquals(o1, o2), message);
    }

    static void fail(String message) {
        throw new RuntimeException(message);
    }

    static boolean isEquals(Object o1, Object o2) {
        boolean equals = false;

        if(o1 == o2) {
            equals = true;
        } else {
            if(o1.getClass() == o2.getClass()) {
                equals = o1.equals(o2);
            } else {
                if(o1 instanceof Number && o2 instanceof Number) {  // 数字, 只比较字面值
                    return isEquals(o1.toString(), o2.toString());

                } else if(o1 instanceof Collection && o2 instanceof Collection) { // 集合就比较里面的元素
                    Collection c1 = (Collection) o1;
                    Collection c2 = (Collection) o2;

                    if (c1.size() != c2.size()) {
                        equals = false;
                    } else {
                        boolean _eq = true;

                        for (Iterator it1 = c1.iterator(), it2 = c2.iterator(); it1.hasNext() && it2.hasNext(); ) {
                            Object e1 = it1.next();
                            Object e2 = it2.next();
                            if (!isEquals(e1, e2)) {
                                _eq = false;
                                break;
                            }
                        }

                        equals = _eq;
                    }


                } else {
                    equals = false;
                }
            }
        }

        return equals;
    }
}
