package cn.wzga.core.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cn.wzga.core.storage.Storage;
import cn.wzga.core.util.FileUtil;
import cn.wzga.core.util.StringUtil;

/**
 * <p>
 * 基于磁盘的存储
 * </p>
 * 
 * @author cl
 * @since 2014/11/11
 */
public class DiskBasedStorageImpl implements Storage {
    private String diskPath;

    public DiskBasedStorageImpl(String absPath) {
        this.diskPath = absPath;
    }

    private String getRealPath(String path) {
        return new File(new File(this.diskPath), path).getPath();
    }

    @Override
    public boolean put(String path, String fileName, InputStream input) {
        if (StringUtil.isEmpty(path) || StringUtil.isEmpty(fileName)) {
            return false;
        }

        try {
            FileUtil.createFile(input, getRealPath(path), fileName);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean put(String path, String fileName, byte[] bytes) {
        boolean result = false;
        if (bytes != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            result = put(path, fileName, byteArrayInputStream);
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                return false;
            }
        }
        return result;
    }

}
