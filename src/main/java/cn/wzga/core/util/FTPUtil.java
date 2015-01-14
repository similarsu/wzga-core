package cn.wzga.core.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * <p>
 * description:FTP帮助类
 * </p>
 * 
 * @author sutong
 * @since 2014年10月31日
 * @version v1.0
 */
public class FTPUtil {
    private static String username;
    private static String password;
    private static String url;
    private static int port;
    private FTPClient ftpClient = null;
    private String encoding = "utf-8";

    public static void setUp(String url, String username, String password, int port) {
        FTPUtil.url = url;
        FTPUtil.username = username;
        FTPUtil.password = password;
        FTPUtil.port = port;
    }

    /**
     * 
     * <p>
     * description:初始化ftp参数
     * </p>
     * 
     * @throws Exception
     * 
     * @author sutong
     * @since 2014年11月3日
     */
    private void setArg() throws Exception {
        try {
            Properties prop = new Properties();
            InputStream fis = FTPUtil.class.getResourceAsStream("/ftp.properties");

            prop.load(fis); // 载入文件

            fis.close();
            url = prop.getProperty("url");
            port = Integer.parseInt(prop.getProperty("port"));
            username = prop.getProperty("userName");
            password = prop.getProperty("password");
        } catch (Exception e) {
            throw new Exception("未在classpath下面找到ftp的配置文件ftp.properties");
        }
    }

    public FTPUtil() throws Exception {
        // setArg();
    }

    /**
     * 
     * <p>
     * description:连接ftp
     * </p>
     * 
     * @throws Exception
     * 
     * @author sutong
     * @since 2014年11月3日
     */
    public void connectServer() throws Exception {
        if (ftpClient == null) {
            // setArg();
            try {
                ftpClient = new FTPClient();
                ftpClient.setControlEncoding(encoding);
                ftpClient.setDefaultPort(port);
                ftpClient.connect(url);
                ftpClient.login(username, password);
                int reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    System.out.println("连接失败");
                    ftpClient.disconnect();
                }
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            } catch (Exception e) {
                throw new Exception("登录FTP服务器：" + url + "失败，连接超时");
            }
        }
    }

    /**
     * 
     * <p>
     * description:关闭连接
     * </p>
     * 
     * @throws Exception
     * 
     * @author sutong
     * @since 2014年11月3日
     */
    public void closeConnect() throws Exception {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
                ftpClient = null;
            }
        } catch (IOException e) {

            throw new Exception("断开ftp连接异常！");
        }
    }

    /**
     * 
     * @param localFile 文件完整路径
     * @param serverFilePath 文件上传路径（不包含文件名）
     * @return
     */
    @Deprecated
    public boolean uploadFile(File localFile, String serverFilePath) {
        boolean flag = true;
        try {
            if (!localFile.exists()) {
                System.out.println("文件不存在");
                throw new RuntimeException("file.notfound");
            }
            InputStream input = new FileInputStream(localFile);
            ftpClient.makeDirectory(serverFilePath);
            if (ftpClient.changeWorkingDirectory(serverFilePath)) {
                flag = ftpClient.storeFile(localFile.getName(), input);
                if (flag) {
                    System.out.println("文件" + localFile.getAbsolutePath() + "上传成功");
                } else {
                    System.out.println("文件上传失败");
                }

            } else {
                System.out.println("创建ftp目录失败");
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件
     * 
     * @param localFile 本地文件路径
     * @param distFolder 目标文件路径
     * @return 返回上传失败的文件名称
     */
    @Deprecated
    public String uploadManyFiles(File localFile, String distFolder) {
        boolean flag = true;
        StringBuffer str = new StringBuffer();
        try {
            ftpClient.changeWorkingDirectory(distFolder);
            if (localFile.isDirectory()) {
                ftpClient.makeDirectory(localFile.getName());
                File[] fileList = localFile.listFiles();
                for (File f : fileList) {
                    if (f.isDirectory()) {
                        uploadManyFiles(f, distFolder + "\\" + localFile.getName());
                    } else {
                        flag = uploadFile(f, distFolder + "\\" + localFile.getName());
                    }
                    if (!flag) {
                        str.append(f.getAbsolutePath() + "\r\n");
                    }
                }
            } else {
                uploadFile(localFile, distFolder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    /**
     * 
     * @param remoteFileName 文件完整路径（带文件名）
     * @param localFileName 文件下载地址完整路径（带文件名）
     * @return true下载成功，false下载失败
     */

    public boolean downloadFile(String remoteFileName, String localFileName) {
        boolean flag = true;
        BufferedOutputStream buffOS = null;
        try {
            buffOS = new BufferedOutputStream(new FileOutputStream(localFileName));
            flag = ftpClient.retrieveFile(remoteFileName, buffOS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("下载失败");
            flag = false;
        } finally {
            if (buffOS != null) {
                try {
                    buffOS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 删除单个文件
     * 
     * @param fileName 文件完整路径
     * @return true成功
     */
    public boolean deleteFile(String fileName) {
        boolean flag = true;
        try {
            flag = ftpClient.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除空目录
     * 
     * @param filePath
     * @return true成功，false失败
     */
    public boolean deleteEmptyDirectory(String filePath) {
        boolean flag = true;
        try {
            flag = ftpClient.removeDirectory(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 列出指定路径下所有文件
     * 
     * @param path 指定完整路径
     * @return
     */
    public String[] listAllFiles(String path) {
        if (path.equals(null)) {
            path = "\\";
        }
        String[] list = null;
        try {
            list = ftpClient.listNames(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 
     * <p>
     * description:上传文件到ftp
     * </p>
     * 
     * @param inputStream
     * @param filePath
     * @param fileName
     * @throws Exception
     * 
     * @author sutong
     * @since 2014年11月3日
     */
    public void uploadFile(InputStream inputStream, String filePath, String fileName)
            throws Exception {
        if (StringUtil.isBlank(filePath)) {
            return;
        }
        String newPath = "";
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        String[] paths = filePath.split("/");
        for (String tmp : paths) {
            newPath += "/" + tmp;
            ftpClient.makeDirectory(newPath);
            ftpClient.changeWorkingDirectory(newPath);
        }
        if (ftpClient.changeWorkingDirectory(newPath)) {
            ftpClient.storeFile(fileName, inputStream);

        } else {
            System.out.println("创建ftp目录失败");
        }
    }

    /**
     * 
     * <p>
     * description:上传字节流到ftp
     * </p>
     * 
     * @param bytes
     * @param path
     * @param name
     * @throws Exception
     * 
     * @author sutong
     * @since 2014年11月3日
     */
    public void uploadFile(byte[] bytes, String filePath, String fileName) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        uploadFile(byteArrayInputStream, filePath, fileName);
        byteArrayInputStream.close();
    }

    public static void main(String[] args) throws Exception {
        FTPUtil.setUp("10.119.228.213", "yykfk", "yykfk", 9898);
        InputStream fis = new FileInputStream("E:/1.jpg");
        FTPUtil ftp = new FTPUtil();
        ftp.connectServer();
        ftp.uploadFile(fis, "/piqc/555/44", "222.jpg");
        ftp.closeConnect();
        fis.close();

    }
}
