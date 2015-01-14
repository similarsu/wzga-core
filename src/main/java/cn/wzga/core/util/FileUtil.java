package cn.wzga.core.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.*;

/**
 * <p>
 * Description: 文件上传、下载
 * </p>
 *
 * @author sutong
 * @version 1.0 2014-08-22
 */
public class FileUtil {

    /**
     * 读取文件流
     *
     * @return inputStream
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        // Reader reader = new InputStreamReader(inputStream, DEFAULT_ENCODE);
        int ch;
        while ((ch = inputStream.read()) != -1) {
            bytestream.write(ch);
        }
        byte[] data = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

    /**
     * 根据 文件流 创建 文件
     *
     * @param is
     * @param dir
     * @param fileName
     * @throws Exception
     */
    public static void createFile(InputStream is, String dir, String fileName)
            throws Exception {
        createPath(dir);
        FileOutputStream fos = new FileOutputStream(dir + "/" + fileName);
        int ch;
        while ((ch = is.read()) != -1) {
            fos.write(ch);
        }
        fos.flush();
        fos.close();
    }

    /**
     * <p>将Base64字符串 转化为文件</p>
     *
     * @param content
     * @param dir
     * @param fileName
     * @throws Exception
     */
    public static void transformBase2Image(String content, String dir,
                                           String fileName) throws Exception {
        byte[] imageByteArray = transformBase2Image(content);
        if (null == imageByteArray) {
            return;
        }

        createPath(dir);
        FileOutputStream fos = new FileOutputStream(dir + fileName);
        fos.write(imageByteArray);
        fos.flush();
        fos.close();
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @throws Exception
     */
    public static void deleteFile(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.isFile()) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        file.delete();
    }

    /**
     * 获取文件后缀名
     *
     * @param originName
     * @return
     */
    public static String getSuffixName(String originName) {
        int index = originName.lastIndexOf(".");
        if (index > -1) {
            return originName.substring(index);
        }
        return "";
    }

    /**
     * 根据原始的文件名获取新的UUID文件名，后缀名相同
     *
     * @param originName
     * @return
     */
    public static String getUUIDFileName(String originName) {
        return UUID.randomUUID().toString() + getSuffixName(originName);
    }

    /**
     * 根据原始的文件名获取新的日期文件名，后缀名相同
     *
     * @param originName
     * @return
     */
    public static String getTimeFileName(String originName) {
        return new Date().getTime() + getSuffixName(originName);
    }

    /**
     * 根据路径创建文件夹
     *
     * @param dir
     */
    public static void createPath(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取文件夹下的所有文件
     *
     * @param dir
     * @param level
     * @param fileMap
     */
    public static void readFiles(String dir, int level,
                                 Map<Integer, ArrayList<File>> fileMap) {
        File root = new File(dir);
        if (!root.isDirectory()) {
            return;
        }
        if (fileMap == null) {
            return;
        }
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // for(int i=0;i<level;i++){
                // System.out.print("--");
                // }
                // System.out.println(file.getName());
                readFiles(file.getPath(), ++level, fileMap);
                level = 0;
            } else {
                // for(int i=0;i<level;i++){
                // System.out.print("--");
                // }
                // System.out.println(file.getName());
                if (fileMap.get(level) == null) {
                    fileMap.put(level, new ArrayList<File>());
                }
                fileMap.get(level).add(file);
            }
        }
    }

    /**
     * 获取第level层以suffix为后缀名的所有文件
     *
     * @param fileMap
     * @param level
     * @param suffix
     * @return
     */
    public static List<File> levelFile(Map<Integer, ArrayList<File>> fileMap,
                                       int level, String suffix) {
        if (fileMap == null) {
            return null;
        }
        List<File> fileList = new ArrayList<File>();
        for (File file : fileMap.get(level)) {
            if (file.getAbsolutePath().endsWith(suffix)) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * <p>将base64编码的图片转换成byte数组</p>
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] transformBase2Image(String content) throws Exception {
        if (StringUtil.isEmpty(content)) {
            return null;
        }

        Base64 base64 = new Base64();
        byte[] b = base64.decode(content);
        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        return b;
    }

    public static void main(String[] args) throws Exception {
        // Map<Integer,ArrayList<File>> fileMap=new HashMap<Integer,
        // ArrayList<File>>();
        // readFiles("d:/upload/bank_query", 0,fileMap);
        // List<File> files=levelFile(fileMap, 1, ".xls");
        // System.out.println(files.size());
        String str =
                "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";
        System.out.println(str.length());
        Base64 base64 = new Base64();
        byte[] b = base64.decode(str);
        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        FileOutputStream fos = new FileOutputStream("d:/1/5.jpg");
        fos.write(b);
        fos.flush();
        fos.close();
    }
}
