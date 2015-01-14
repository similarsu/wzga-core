package cn.wzga.core.storage;

import java.io.InputStream;

/**
 * <p>
 * 存储抽象
 * </p>
 * 
 * @author cl
 * @since 2014/11/11
 */
public interface Storage {
    /**
     * <p>
     * 增加文件
     * </p>
     * 
     * @param path 文件路径
     * @param fileName 文件名
     * @param input 文件流
     * @return
     */
    boolean put(String path, String fileName, InputStream input);

    /**
     * 
     * <p>
     * 增加文件
     * </p>
     * 
     * @param path 文件路径
     * @param fileName 文件名
     * @param bytes 字节流
     * @return
     * 
     * @author sutong
     * @since 2014年11月11日
     */
    boolean put(String path, String fileName, byte[] bytes);
}
