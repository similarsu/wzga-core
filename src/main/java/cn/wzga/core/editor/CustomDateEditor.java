package cn.wzga.core.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 
 * @author sutong
 * @version 1.0 2014-07-14
 */
public class CustomDateEditor extends PropertyEditorSupport {
	private static final Map<String, String> dateMap;

	static {
		dateMap = new HashMap<String, String>();
		dateMap.put("yyyy-MM-dd", "\\d{4}-\\d{2}-\\d{2}");
		dateMap.put("yyyy-MM-dd hh:mm:ss",
				"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
		dateMap.put("yyyy年MM月dd日", "\\d{4}年\\d{2}月\\d{2}日");
	}

	private final boolean allowEmpty;

	public CustomDateEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else {
			try {
				boolean flag = false;
				for (String dateFormatStr : dateMap.keySet()) {
					if (text.matches(dateMap.get(dateFormatStr))) {
						flag = true;
						DateFormat dateFormat = new SimpleDateFormat(
								dateFormatStr);
						setValue(dateFormat.parse(text));
						break;
					}
				}
				if (!flag) {
					// throw new
					// IllegalArgumentException("Could not parse date: " +
					// text);
				}
			} catch (ParseException ex) {
				// throw new IllegalArgumentException("Could not parse date: " +
				// ex.getMessage(), ex);
			}
		}
	}

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return value != null ? dateFormat.format(value) : "";
	}
}
