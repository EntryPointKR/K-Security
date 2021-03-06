package kr.rvs.ksecurity;

import kr.rvs.ksecurity.util.Version;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Junhyeong Lim on 2017-10-20.
 */
public class VersionTest extends Assert {
    @Test
    public void onTest() {
        assertTrue(new Version(0, 0, 0).equals(new Version(0, 0, 0)));
        assertTrue(new Version(0, 0, 1).after(new Version(0, 0, 0)));
        assertFalse(new Version(0, 0, 1).before(new Version(0, 0, 0)));

        assertTrue(new Version(0, 0, 1).afterEquals(new Version(0, 0, 1)));
        assertTrue(new Version(0, 0, 1).afterEquals(new Version(0, 0, 0)));
        assertFalse(new Version(0, 0, 1).beforeEquals(new Version(0, 0, 0)));
        assertFalse(new Version(0, 0, 1).beforeEquals(new Version(0, 0, 0)));
    }
}
