package org.binwang.bard.core;

import junit.framework.TestCase;
import org.binwang.bard.core.defines.FalseAdapter1;
import org.binwang.bard.core.defines.FalseAdapter2;
import org.binwang.bard.core.defines.TrueAdapter1;
import org.binwang.bard.core.defines.TrueAdapter2;
import org.eclipse.jetty.server.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

public class AdapterTest extends TestCase {
    public Servlet servlet = null;

    public static class AllTrueAdapterHandler extends Handler {
        @TrueAdapter1
        @TrueAdapter2
        public void testAllTrue() {
            context.response.addHeader("test", "all_true");
        }
    }

    public static class AllFalseAdapterHandler extends Handler {
        @FalseAdapter1
        @FalseAdapter2
        public void testAllFalse() {
            context.response.addHeader("test", "all_true");
        }
    }

    public static class AdapterHandler extends Handler {
        @FalseAdapter1
        public void notHere() {
            context.response.addHeader("test_false", "added");
        }

        @TrueAdapter1
        public void here() {
            context.response.addHeader("test_true", "added");
        }
    }

    @Before
    public void setUp()
        throws NoSuchMethodException, InstantiationException, IllegalAccessException,
        NoSuchFieldException {
        servlet = new Servlet("org.binwang.bard.core.defines");
    }

    @After
    public void tearDown() {
        servlet = null;
    }

    @Test
    public void allTrueAdapterTest() {
        servlet.addHandler(AllTrueAdapterHandler.class);
    }

}
