package com.sinontech.tools.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

public class DateUtil {

	private final static SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");

	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat sdfdetailTime = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	public static String sdfdetail() {
		return sdfdetailTime.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * 用途说明：相差秒数
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 *             2019年9月11日下午3:01:24
	 * @auther ljj
	 */
	public static long compareTimes(String beginTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long diff = 0l;
		try {
			Date bt = sdf.parse(beginTime);
			Date et = sdf.parse(endTime);
			diff = et.getTime() - bt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return diff / 1000;
	}

	/**
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws @author
	 *             luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		return fomatDate(s).getTime() >= fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa = 0;
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24))
					/ 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	/**
	 * 获取小时差
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return aison 2020年6月23日
	 */
	public static long getHourSub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.util.Date beginDate = null;
		java.util.Date endDate = null;
		try {
			beginDate = sdfTime.parse(beginDateStr);
			endDate = sdfTime.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (60 * 60 * 1000);

		return day;
	}

	/**
	 * 得到n天之前的数据
	 * 
	 * @param days
	 * @return aison 2020年6月24日
	 */
	public static String getBeforeDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, -daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	public static String getBeforeDayDate(String days, String format) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, -daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat(format);
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	public static String getBeforeMonthDate(String month, String format) {
		int daysInt = Integer.parseInt(month);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.MONTH, -daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat(format);
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	public static String getMonthAfter(String time, int month) {
		try {
			Date d = sdfMonth.parse(time);
			Calendar now = Calendar.getInstance();
			now.setTime(d);
			now.add(Calendar.MONTH, month);
			return sdfMonth.format(now.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 指定时间加天数
	 * 
	 * @param time
	 * @param day
	 * @return
	 */
	public static String getDateAfter(String time, int day) {
		try {
			Date d = sdfDay.parse(time);
			Calendar now = Calendar.getInstance();
			now.setTime(d);
			now.add(Calendar.DATE, day);
			return sdfDay.format(now.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
     * 分钟差
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static Long getDetailMinSub(String beginDateStr,String endDateStr){
	        long min=0;
	        java.util.Date beginDate = null;
	        java.util.Date endDate = null;
            try {
				beginDate = sdfTime.parse(beginDateStr);
				endDate= sdfTime.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
            System.out.println(endDate.getTime());
            System.out.println(beginDate.getTime());
            min=(endDate.getTime()-beginDate.getTime())/(60*1000);
        return min;
    }
	public static String getChar14() {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 获取日期之间每天日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getDayList(String startDate, String endDate) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(startDate);
		if (startDate.equals(endDate)) {
			return list;
		}
		String dayAfterDay = "";
		while (!dayAfterDay.equals(endDate)) {
			dayAfterDay = getDateAfter(startDate, 1);
			startDate = dayAfterDay;
			if (!dayAfterDay.equals("")) {
				list.add(dayAfterDay);
			}

		}
		return list;
	}

	public static List<String> getMonthList(String startDate, String endDate) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(startDate);
		if (startDate.equals(endDate)) {
			return list;
		}
		String dayAfterDay = "";
		while (!dayAfterDay.equals(endDate)) {
			dayAfterDay = getMonthAfter(startDate, 1);
			startDate = dayAfterDay;
			if (!dayAfterDay.equals("")) {
				list.add(dayAfterDay);
			}

		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.getMonthAfter("2022-05", 6));

		// System.out.println(getDay());
		//// System.out.println(getAfterDayWeek("3"));
		// Calendar now = Calendar.getInstance();
		// int hour = now.get(Calendar.HOUR_OF_DAY);
		//
		// System.out.println("年：" + now.get(Calendar.YEAR));
		// System.out.println("月：" + (now.get(Calendar.MONTH) + 1));
		// System.out.println("日：" + now.get(Calendar.DAY_OF_MONTH));
		// System.out.println("时：" + now.get(Calendar.HOUR_OF_DAY));
		// System.out.println("分：" + now.get(Calendar.MINUTE));
		// System.out.println("秒：" + now.get(Calendar.SECOND));
		// long times = DateUtil.compareTimes("2019-11-01 00:00:00","2019-10-31
		// 00:00:00");
		// System.out.println(times);
	}

}
