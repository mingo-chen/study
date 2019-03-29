package cm.study.asm.common;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ReflectUtilTest {

    @Test
    public void testGetWrapMethod() {
    }

    @Test
    public void testGetSetterMethodName() {
        assertEquals(ReflectUtil.getSetterMethodName("id"), "setId");
        assertEquals(ReflectUtil.getSetterMethodName("Name"), "setName");
        assertEquals(ReflectUtil.getSetterMethodName("_age"), "set_age");
    }
}