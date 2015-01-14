package cn.wzga.core.util;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>
 * Description: 身份证号码转换及判断身份证是否合法的一系列帮助方法。关于身份证的规则，请参考国家标准。
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-09-17
 */
public class IdcardUtil {

	/**
	 * 根据规则计算出来的R值对应的校验码，这里R值为数组的索引号，对应的校验码为索引号对应的元素值。如R=9，则对应的索引号为JYM_ARRAY[9]=
	 * 3
	 */
	private static String[] JYM = { "1", "0", "X", "9", "8", "7", "6", "5",
			"4", "3", "2" };

	/**
	 * 身份证号码倒排位置序号对应的权值
	 */
	private static int[] WI = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
			4, 2 };

	/**
	 * 新身份证样本代码（除校验码外的部分）长度
	 */
	private static int SFZYBDMCD = 17;

	/**
	 * 新身份证号码长度
	 */
	private static int NEW_SFZHMCD = 18;

	/**
	 * 旧身份证号码长度
	 */
	private static int OLD_SFZHMCD = 15;

	/**
	 * 身份证转换方法，如果输入的是15位的旧身份证号码，转换为18位的新身份证号码，反之依然。
	 * 
	 * @param sfzhm
	 *            身份证号码
	 * @return 转换后的身份证号码，如果给定的身份证位数不正确，返回输入串
	 */
	public static String convert(String sfzhm) {
		if (null == sfzhm) {
			return null;
		}
		String hmToUse = sfzhm.trim();
		int len = hmToUse.length();
		if (OLD_SFZHMCD == len) {
			return oldConverNew(hmToUse);
		}
		if (NEW_SFZHMCD == len) {
			return newConverOld(hmToUse);
		}
		return sfzhm;
	}

	/**
	 * 将15位的旧身份证号码转换为18位的新身份证号码。 本方法对旧身份证号码除了作位数的判断，不作其他的真伪校验。
	 * 
	 * @param oldCode
	 *            15位的旧身份证号
	 * @return 18位的新身份证号码，如果输入的旧身份证号码位数不对，返回输入串
	 */
	public static String oldConverNew(String oldCode) {
		if (null == oldCode) {
			return null;
		}
		String codeToUse = oldCode.trim();
		int len = codeToUse.length();
		if (OLD_SFZHMCD != len) {
			return oldCode;
		}
		if (!isDigit(codeToUse)) {
			return oldCode;
		}
		int[] ai = get17StringArray(codeToUse);
		String r = getRCode(ai);
		StringBuffer codeSb = new StringBuffer("");
		for (int i = 0; i < SFZYBDMCD; i++) {
			codeSb.append(ai[i]);
		}
		return codeSb.toString() + JYM[Integer.valueOf(r).intValue()];
	}

	/**
	 * 将18位的新身份证号码转换为15位的旧身份证号码。 本方法对新身份证号码除了作位数的判断，不作其他的真伪校验。
	 * 
	 * @param newCode
	 *            18位的新身份证号
	 * @return 15位的旧身份证号码，如果输入的新身份证号码位数不对，返回输入串
	 */
	public static String newConverOld(String newCode) {
		if (null == newCode) {
			return null;
		}
		String codeToUse = newCode.trim();
		int len = codeToUse.length();
		if (NEW_SFZHMCD != len) {
			return newCode;
		}
		StringBuffer codeSb = new StringBuffer("");
		codeSb.append(codeToUse.substring(0, 6)).append(
				codeToUse.substring(8, SFZYBDMCD));
		return codeSb.toString();
	}

	/**
	 * 判定给定的身份证号码是否合法。 对于15位的身份证号码，只校验以下三项：
	 * <ul>
	 * <il>除校验码（如果有的话）外，其他是否都是数字</il> <il>出生日期是否合乎历法</il>
	 * <il>对于18位的身份证号码，还校验最后一位校验码是否正确</il>
	 * </ul>
	 * 目前不对行政区划是否正确作校验。
	 * 
	 * @param sfzhm
	 *            15位或18位身份证号码
	 * @return 给定的身份证号码是否合法
	 */
	public static boolean isLegalCode(String sfzhm) {
		if (null == sfzhm) {
			return false;
		}
		String strToUse = sfzhm.trim();
		int len = strToUse.length();
		// 判断长度是否是15或18位
		if (!(len == OLD_SFZHMCD || len == NEW_SFZHMCD)) {
			return false;
		}
		// 判断出生日期是否合乎历法
		String csrqStr = "";
		if (len == OLD_SFZHMCD) {
			csrqStr = "19" + strToUse.substring(6, 12);
		}
		if (len == NEW_SFZHMCD) {
			csrqStr = strToUse.substring(6, 14);
		}
		if ("".equals(csrqStr)) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyymmdd");
		try {
			sdf.parse(csrqStr);
		} catch (ParseException e) {
			return false;
		}
		// 判断除校验码外的子串是否全部由数字组成
		String numString = strToUse;
		if (len == NEW_SFZHMCD) {
			numString = strToUse.substring(0, NEW_SFZHMCD - 1);// 去掉最后一位校验码
		}

		if (!isDigit(numString)) {
			return false;
		}
		// 如果是18位的新身份证号码，判断最后一位校验码是否正确
		if (len == NEW_SFZHMCD) {
			String jymStr = strToUse.substring(SFZYBDMCD);
			int[] testArray = get17StringArray(strToUse);
			String testR = getRCode(testArray);
			String testJYM = JYM[Integer.valueOf(testR).intValue()];
			if (!jymStr.equals(testJYM)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将15或18位的字符串转换为17个元素的字符串数组返回。 对于15位的字符串，在第6位之后填加"19"；对于18位的字符串，去掉最后一位。
	 * 
	 * @param str
	 *            15或18位的字符串
	 * @return 17个元素的字符串数组，如果输入的参数位数不对，返回null
	 */
	@SuppressWarnings("unchecked")
	private static int[] get17StringArray(String str) {
		if (null == str) {
			return null;
		}
		String strToUse = str.trim();
		int len = strToUse.length();
		if (len != OLD_SFZHMCD && len != NEW_SFZHMCD) {
			return null;
		}
		List l = new ArrayList(SFZYBDMCD);
		for (int i = 0; i < len; i++) {
			l.add(String.valueOf(strToUse.charAt(i)));
		}
		if (len == OLD_SFZHMCD) {
			l.add(6, "1");
			l.add(7, "9");

		}
		if (len == NEW_SFZHMCD) {
			l.remove(SFZYBDMCD);
		}
		int[] rs = new int[SFZYBDMCD];
		for (int i = 0; i < SFZYBDMCD; i++) {
			rs[i] = new Integer((String) l.get(i)).intValue();
		}
		return rs;
	}

	/**
	 * 按照新身份证生成规则，根据样本代码生成R值。
	 * 
	 * @param ai
	 *            样本代码对应的字符传数组
	 * @return R值
	 */
	private static String getRCode(int[] ai) {
		int alen = ai == null ? 0 : ai.length;
		int wlen = WI.length;
		if (alen != wlen) {
			return null;
		}
		int sumCode = 0;
		for (int i = 0; i < alen; i++) {
			sumCode += ai[i] * WI[i];
		}
		int r = sumCode % 11;
		return String.valueOf(r);
	}

	public static boolean isDigit(String source) {
		if (null == source) {
			return false;
		}
		String numString = source;
		int numStrLen = numString.length();
		for (int t = 0; t < numStrLen; t++) {
			if (!Character.isDigit(numString.charAt(t))) {
				return false;
			}
		}
		return true;
	}
}
