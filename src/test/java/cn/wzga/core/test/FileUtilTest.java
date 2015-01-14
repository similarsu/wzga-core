package cn.wzga.core.test;

import cn.wzga.core.util.FileUtil;
import junit.framework.Assert;
import org.junit.Test;

/**
 * <p>
 * FileUtil测试类
 * </p>
 *
 * @author cl
 * @version v1.0
 * @since 2014/10/31
 */
public class FileUtilTest {

    @Test
    public void base64Str2File() throws Exception {
        // test null array
        Assert.assertNull(FileUtil.transformBase2Image(null));
        Assert.assertNull(FileUtil.transformBase2Image(""));
        Assert.assertNotNull(FileUtil.transformBase2Image("  "));

    }
}
