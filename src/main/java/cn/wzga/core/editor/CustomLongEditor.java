package cn.wzga.core.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-07-14
 */
public class CustomLongEditor extends PropertyEditorSupport {
	private final boolean allowEmpty;

	public CustomLongEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else {
			try {
				setValue(Long.parseLong(text));
			} catch (Exception ex) {
				// throw new IllegalArgumentException("Could not parse date: " +
				// ex.getMessage(), ex);
			}
		}
	}

	@Override
	public String getAsText() {
		Long value = (Long) getValue();

		return value != null ? value.toString() : null;
	}
}
