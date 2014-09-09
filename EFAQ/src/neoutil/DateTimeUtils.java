/**
 * Description: DateTimeUtil.java Copyright (c) 2008-2009 Neo. All Rights
 * Reserved.
 * 
 * @version 1.0 Nov 14, 2009 9:44:31 AM Neo (neolimeng@gmail.com)created
 */
package neoutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * Description: Copyright (c) 2008-2009 Neo. All Rights Reserved.
 * 
 * @version 1.0 Nov 14, 2009 9:44:31 AM Neo (neolimeng@gmail.com)created
 */
public class DateTimeUtils extends DateUtils {

	public static Logger	log			= Logger.getLogger(DateTimeUtils.class);
	public static String	DATE_FORMAT	= "yy-MM-dd";

	/**
	 * Description: Return a Date instance from a formated string. If the string
	 * is null or "", return the current date of the system.
	 * 
	 * @Version1.0 Nov 14, 2009 9:55:37 AM Neo (neolimeng@gmail.com) created
	 * @param sDateTime
	 * @return
	 */
	public static Date getFormatedDateFromString(String sDateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		System.out.println("The date time needed to be converted is: " + sDateTime);
		Date date;
		if (null == sDateTime || "".equals(sDateTime)) {
			date = new Date();
		}
		else {
			try {
				date = sdf.parse(sDateTime);
			}
			catch (ParseException e) {
				date = new Date();
				log.info("DateTimeUtil-->");
				e.printStackTrace();
			}
		}
		return date;
	}

	public static String getFormatedDateStringFromDate(Date dateToFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String formatedDateString = sdf.format(dateToFormat);
		return formatedDateString;
	}
}
