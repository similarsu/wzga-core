package cn.wzga.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Description: 日期工具类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-09-09
 */
public class DateUtil {
	/**
	 * <p>
	 * description:比较两个日期的大小，如果date2>date1则返回true或者返回false
	 * </p>
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean
	 */
	public static boolean compare(Date date1, Date date2) {
		if (date1.before(date2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 给传入的日期加上传入的天数
	 * </p>
	 * 
	 * @param date传入的日期
	 * @param days要增加的天数
	 * @return Date
	 */
	public static Date addDateToDays(Date date, int days) {
		if (date == null)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 给传入的日期加上传入的分钟
	 * 
	 * @param date
	 *            传入的日期
	 * @param mins
	 *            要增加的分钟
	 * @return
	 */
	public static Date addDateToMins(Date date, int mins) {
		if (date == null)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, mins);
		date = calendar.getTime();
		return date;
	}

	/**
	 * <p>
	 * 取得参数日期n月之后的那一天
	 * </p>
	 * 
	 * @param date
	 * @param n
	 * @return Date
	 */
	public static Date getNMonthAfterOneDay(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, n);
		return calendar.getTime();

	}

	/**
	 * <p>
	 * description:取得某个时间前n天
	 * </p>
	 * 
	 * @param date
	 * @param n
	 * @return Date
	 */
	public static Date getNDayBeforeOneDate(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -n);
		return calendar.getTime();
	}

	/**
	 * <p>
	 * description:计算两个日期相差的天数 一般是date2要大于date1
	 * </p>
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @return int
	 * @date 2011-12-9
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000l * 3600l * 24l);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static String formatDateTime(Date date, String format) {
		if (date == null)
			return null;
		if (format == null)
			return date.toString();

		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static String formatDate(Date date, String format) {
		return date == null ? "" : formatDateTime(date, format);
	}

	public static String formatYMD(Date date) {
		return date == null ? "" : formatDateTime(date, "yyyy-MM-dd");
	}

	/**
	 * 字符串转日期
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date formatStrToDate(String strDate, String dataFormat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dataFormat);

			return format.parse(strDate);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转日期
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat format = null;
		try {
			if (strDate.split(" ").length > 1) {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else {
				strDate.replace("/", "-");
				format = new SimpleDateFormat("yyyy-MM-dd");
			}

			return format.parse(strDate);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取指定指定日期的前N年的所有年份，并返回列表
	 * 
	 * @param date
	 * @param berfore
	 * @return
	 */
	public static List<String> getBerforeNYeas(Date date, int berfore) {
		return DateUtil.getBerforeNAndAfterNYeas(date, berfore, 0);
	}

	/**
	 * 获取指定指定日期的后N年的所有年份，并返回列表
	 * 
	 * @param date
	 * @param after
	 * @return
	 */
	public static List<String> getAfterNYeas(Date date, int after) {
		return DateUtil.getBerforeNAndAfterNYeas(date, 0, after);
	}

	/**
	 * 获取指定指定日期的前N年和后N年的所有年份，并返回列表
	 * 
	 * @param strDate
	 * @return
	 */
	public static List<String> getBerforeNAndAfterNYeas(Date date, int berfore,
			int after) {
		List<String> years = new ArrayList<String>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - berfore);
		years.add(String.valueOf(calendar.get(Calendar.YEAR)));
		for (int i = 1; i < berfore; i++) {
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
			years.add(String.valueOf(calendar.get(Calendar.YEAR)));
		}
		calendar.setTime(date);

		years.add(String.valueOf(calendar.get(Calendar.YEAR)));

		for (int i = 1; i <= after; i++) {
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
			years.add(String.valueOf(calendar.get(Calendar.YEAR)));
		}
		return years;
	}

	/**
	 * 获取指定年份的第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Calendar getFirstOfYear(String year) {

		Calendar time = Calendar.getInstance();
		time.set(Calendar.YEAR, Integer.valueOf(year));
		time.set(Calendar.DAY_OF_YEAR, 1);

		return time;
	}

	/**
	 * 获取当天时间的开始时间和结束时间
	 * 
	 * @return
	 */
	public static Date[] getCurrentDate() {
		Date now = new Date();
		String nowStr = formatDate(now, "yyyyMMdd");
		Date date1 = formatStrToDate(nowStr + "000000", "yyyyMMddHHmmss");
		Date date2 = formatStrToDate(nowStr + "235959", "yyyyMMddHHmmss");
		return new Date[] { date1, date2 };
	}

	public static String getCurrentYear() {
		Calendar time = Calendar.getInstance();
		return String.valueOf(time.get(Calendar.YEAR));
	}

	public static void main(String args[]) {
		/*
		 * Date nowDate=new Date(); System.out.println(formatDate(nowDate,
		 * "yyyy-MM-dd HH:mm:ss"));
		 * System.out.println(formatDate(addDateToMins(nowDate, 10),
		 * "yyyy-MM-dd HH:mm:ss")); String dateFormat="yyyy-MM-dd'T'HH:mm:ss";
		 * System
		 * .out.println(formatDate(formatStrToDate("2013-07-16T00:00:00+08:00",
		 * dateFormat), "yyyy-MM-dd HH:mm:ss"));
		 */
		Date[] dates = getCurrentDate();
		System.out.println(formatDate(dates[0], "yyyy-MM-dd HH:mm:ss"));
		System.out.println(formatDate(dates[1], "yyyy-MM-dd HH:mm:ss"));
	}

}
