package cn.wzga.core.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import cn.wzga.core.util.FTPUtil;

/**
 * <p>
 * description:ftp测试类
 * </p>
 * 
 * @author sutong
 * @author cl
 * @version v1.0
 * @since 2014/11/7
 */
public class FTPUtilTest {
    @Test
    public void testUploadFile() throws Exception {
        FTPUtil.setUp("10.119.228.213", "yykfk", "wzga_yykfk", 9898);
        File tempEmptyFile = File.createTempFile("test", "jpg");
        InputStream fin = new FileInputStream(tempEmptyFile);
        FTPUtil ftp = new FTPUtil();
        ftp.connectServer();
        ftp.uploadFile(fin, "/piqc/22/12/44", "222.jpg");
        ftp.closeConnect();
        fin.close();
    }
}
