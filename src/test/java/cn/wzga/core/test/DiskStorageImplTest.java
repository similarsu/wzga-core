package cn.wzga.core.test;

import cn.wzga.core.storage.Storage;
import cn.wzga.core.storage.impl.DiskBasedStorageImpl;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author cl
 * @2014/11/11
 */
public class DiskStorageImplTest {
    public static String OS = System.getProperty("os.name").toLowerCase();
    private String testPath;

    public static boolean isLinux() {
        return -1 < OS.indexOf("nix");
    }

    @Before
    public void setup() {
        if (isLinux()) {
            testPath = "/tmp/junit";
        } else {
            testPath = "c:/junit";
        }
    }

    @After
    public void cleanupDir() {
        deleteFile(new File(testPath));
    }

    public void deleteFile(File file) {
        if (null == file || !file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (null != subFiles && 0 < subFiles.length) {
                for (File tmpFile : subFiles) {
                    if (tmpFile.isDirectory()) {
                        deleteFile(tmpFile);
                    } else {
                        tmpFile.delete();
                    }
                }
            }
            file.delete();

        } else {
            file.delete();
        }

    }

    @Test
    public void normal() throws IOException {
        Storage storage = new DiskBasedStorageImpl(testPath);

        // fake file
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(111);
        byteArrayOutputStream.write(111);
        byteArrayOutputStream.write(111);
        byte[] fileContent = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        // create file
        Assert.assertTrue(storage.put("piqc", "test.txt", new ByteArrayInputStream(fileContent)));

        // test file exits
        File checkFile = new File(testPath);
        Assert.assertTrue(checkFile.exists());

        File createdFile = new File(new File(checkFile, "piqc"), "test.txt");
        Assert.assertTrue(createdFile.exists());

    }

}
