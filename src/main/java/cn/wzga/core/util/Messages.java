package cn.wzga.core.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SuppressWarnings("serial")
public final class Messages implements Serializable {
	public static final String MESSAGE_SUCCESS = "message_success";
	public static final String MESSAGE_WARN = "message_warn";
	public static final String MESSAGE_ERROR = "message_error";
	public static final String MESSAGE_PARAM = "message_param";
	public static final String ERROR_FLAG = "error_flag";

	private static HttpServletRequest getRequest(
			RequestAttributes requestAttributes) {
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	public static void setSuccessMessage(String success) {
		HttpSession session = getRequest(
				RequestContextHolder.currentRequestAttributes()).getSession(
				false);
		session.setAttribute(MESSAGE_SUCCESS, success);
	}

	public static void setWarnMessage(String warn) {
		HttpSession session = getRequest(
				RequestContextHolder.currentRequestAttributes()).getSession(
				false);
		session.setAttribute(MESSAGE_WARN, warn);
	}

	public static void setParams(Object[] arg) {
		HttpSession session = getRequest(
				RequestContextHolder.currentRequestAttributes()).getSession(
				false);
		String param = "";
		for (Object obj : arg) {
			if (obj != null) {
				if (param.length() > 0) {
					param += ",";
				}
				param += obj.toString();
			}
		}
		session.setAttribute(MESSAGE_PARAM, param);
	}

	public static void setErrorMessage(String error) {
		HttpSession session = getRequest(
				RequestContextHolder.currentRequestAttributes()).getSession(
				false);
		if (error != null && !error.startsWith("error.")
				&& !error.startsWith("validation.")
				&& !error.startsWith("warn.")) {
			session.setAttribute(MESSAGE_ERROR, "error.sys.interior");
		} else if (error != null && error.startsWith("warn.")) {
			session.setAttribute(MESSAGE_WARN, error);
		} else {
			session.setAttribute(MESSAGE_ERROR, error);
		}
		session.setAttribute(ERROR_FLAG, ERROR_FLAG);
	}

	public static boolean hasErrors() {
		HttpSession session = getRequest(
				RequestContextHolder.currentRequestAttributes()).getSession(
				false);
		if (null != session && session.getAttribute(ERROR_FLAG) != null) {
			session.removeAttribute(ERROR_FLAG);
			return true;
		} else {
			return false;
		}
	}
}
