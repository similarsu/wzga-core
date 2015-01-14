package cn.wzga.core.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * <p>
 * Description: 文件压缩成Zip格式
 * </p>
 * 
 * @author yechaojun
 * @version 1.0 2014-07-25
 */
public class ZipUtil {

	/**
	 * 
	 * @param orginFile
	 * @param newFilePath
	 *            zip文件路径，不加文件名
	 * @param password
	 * @throws ZipException
	 */

	public static void toZipFileWithPassword(File orginFile,
			String newFilePath, String password) throws ZipException {

		ZipFile zipFile = new ZipFile(newFilePath);
		ArrayList<File> filesToAdd = new ArrayList<File>();

		// 多个文件添加
		// for(File a:orginFile.listFiles()){
		// filesToAdd.add(a);
		// }

		// 单个文件添加
		filesToAdd.add(orginFile);

		// 设置ZIP参数
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

		// 设置密码
		parameters.setEncryptFiles(true);
		parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
		parameters.setPassword(password);

		zipFile.createZipFile(filesToAdd, parameters);
	}

	/**
	 * 
	 * @param zipFilePath
	 *            完整路径，以。zip结尾
	 * @param targetPath
	 *            目标路径，文件夹路径
	 * @param password
	 * @throws ZipException
	 */

	public static String unZipFileWithPassword(String zipFilePath,
			String targetPath, String password) throws ZipException {
		ZipFile zipFile2 = new ZipFile(zipFilePath);

		// 判断是否加密
		if (zipFile2.isEncrypted()) {
			zipFile2.setPassword(password);
		}

		List<FileHeader> fileHeaderList = zipFile2.getFileHeaders();
		FileHeader fileHeader = (FileHeader) fileHeaderList.get(0);
		zipFile2.extractFile(fileHeader, targetPath);
		return fileHeader.getFileName();
	}

	public static void main(String[] args) throws IOException, ZipException {

		toZipFileWithPassword(new File("D:\\test\\"), "E:\\testZip\\1.zip",
				"123456789");

		// unZipFileWithPassword("E:\\testZip\\test1.zip", "D:\\", "123456789");

	}
}
