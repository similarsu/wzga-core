package cn.wzga.core.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Description: 字符串通用处理类
 * </p>
 *
 * @author sutong
 * @version 1.0 2014-07-18
 */
@SuppressWarnings("rawtypes")
public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static String filter(String content) {
        return content == null ? "" : content;
    }

    /**
     * 首字母转小写
     *
     * @param content
     * @return String
     */
    public static String firstToLowerCase(String content) {
        if (isBlank(content)) {
            return content;
        }
        String firstStr = content.substring(0, 1).toLowerCase();

        return firstStr + content.substring(1);
    }

    /**
     * 首字母转大写
     *
     * @param content
     * @return String
     */
    public static String firstToUpperCase(String content) {
        if (isBlank(content)) {
            return content;
        }
        String firstStr = content.substring(0, 1).toUpperCase();

        return firstStr + content.substring(1);
    }

    /**
     * 大写转下划线的算法 驼峰转 下划线
     *
     * @param column
     * @return String
     */
    public static String attribute2Column(String column) {
        if (isBlank(column)) {
            return column;
        }
        Pattern p = Pattern.compile("[A-Z]");
        StringBuilder builder = new StringBuilder(column);
        Matcher mc = p.matcher(column);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 小写带下划线转驼峰字符
     *
     * @param column
     * @return
     */
    public static String column2Attribute(String column) {
        if (isBlank(column)) {
            return column;
        }
        StringBuffer attribute = new StringBuffer(column.toLowerCase());
        // 将下划线后面的字符替换成大写
        int i = attribute.indexOf("_");
        int length = attribute.length();
        while (i > -1) {
            if (length > i + 1) {
                attribute.replace(i + 1, i + 2, attribute.substring(i + 1, i + 2).toUpperCase());
            }
            attribute.deleteCharAt(i);

            length--;
            i = attribute.indexOf("_", i);
        }

        return attribute.toString();

    }

    /**
     * 密码明文加密
     *
     * @param password
     * @return String
     */
    public static String encryptPassword(String password) {
        if (!StringUtil.isBlank(password))
            return DigestUtils.sha256Hex(password);
        else
            return password;
    }

    /**
     * 将Id列表转换成Set
     *
     * @param clazz
     * @param ids
     * @return
     */
    public static <T> Set<T> ids2Set(Class<T> clazz, String ids) {
        try {
            if (StringUtil.isBlank(ids)) {
                return null;
            }
            Set<T> objList = new HashSet<T>();

            String[] idArray = ids.split(",");
            for (String id : idArray) {
                T obj = clazz.newInstance();

                PropertyUtil.setProperty(obj, "id", Long.parseLong(id));

                objList.add(obj);
            }

            return objList;
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * id转换成对象
     *
     * @param clazz
     * @param id
     * @return
     */
    public static <T> T id2Object(Class<T> clazz, String id) {
        try {
            if (StringUtil.isBlank(id)) {
                return null;
            }
            T obj = clazz.newInstance();

            PropertyUtil.setProperty(obj, "id", Long.parseLong(id));

            return obj;
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * str转换成对象
     *
     * @param clazz
     * @param field
     * @param value
     * @return
     */
    public static <T> T str2Object(Class<T> clazz, String field, String value) {
        try {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            T obj = clazz.newInstance();

            PropertyUtil.setProperty(obj, field, value);

            return obj;
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * Set转换成Id字符串组
     *
     * @param objList
     * @return String
     */
    public static <T> String set2Ids(Set<T> objList) {
        try {
            if (objList == null)
                return null;
            StringBuffer result = new StringBuffer();
            int i = 0;
            for (T obj : objList) {
                if (i++ != 0) {
                    result.append(",");
                }
                result.append(PropertyUtil.getProperty(obj, "id"));
            }

            return result.toString();
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * object转换成id
     *
     * @param obj
     * @return
     */
    public static <T> String obj2Id(T obj) {
        try {
            if (obj == null)
                return null;
            return PropertyUtil.getProperty(obj, "id") + "";
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * object转换成str
     *
     * @param obj
     * @param field
     * @return
     */
    public static <T> String obj2Str(T obj, String field) {
        try {
            if (obj == null)
                return null;
            return PropertyUtil.getProperty(obj, field) + "";
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * 判断字符串是否包含在数字中
     *
     * @param destField
     * @param fields
     * @return boolean
     */
    public static boolean contains(String destField, String[] fields) {
        if (fields == null || StringUtil.isBlank(destField))
            return false;
        boolean result = false;
        for (String field : fields) {
            if (destField.equals(field)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 下拉框json
     *
     * @param c
     * @param key
     * @param value
     * @return String
     */
    public static String jsonSelect(Collection c, String key, String value) {
        // {"list":[{"value":"1","key":"大专"},{"value":"2","key":"本科"},{"value":"3","key":"硕士"},{"value":"4","key":"博士"}]}
        try {
            StringBuffer json = new StringBuffer("{\"list\":[");
            int i = 0;
            for (Iterator it = c.iterator(); it.hasNext(); ) {
                Object obj = it.next();

                if (i++ != 0) {
                    json.append(",");
                }
                json.append("{\"value\":\"" + PropertyUtil.getProperty(obj, key) + "\",\"key\":\""
                        + PropertyUtil.getProperty(obj, value) + "\"}");
            }
            json.append("]}");

            return json.toString();
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * 树形下拉框json
     *
     * @param c
     * @param id
     * @param name
     * @param parentId
     * @return String
     */
    public static String jsonSelectTree(Collection c, String id, String name, String parentId) {
        // {"treeNodes":[{"id":"1","parentId":"0","name":"南京分公司"},{"id":"2","parentId":"0","name":"市场部"}]}
        try {
            StringBuffer json = new StringBuffer("{\"treeNodes\":[");
            int i = 0;
            for (Iterator it = c.iterator(); it.hasNext(); ) {
                Object obj = it.next();

                Long pId = null;
                if (StringUtil.isBlank(parentId)) {
                    pId = 0l;
                } else {
                    try {
                        pId = (Long) PropertyUtil.getProperty(obj, parentId);
                    } catch (Exception e) {
                        pId = 0l;
                    }
                }

                if (i++ != 0) {
                    json.append(",");
                }
                json.append("{\"id\":\"" + PropertyUtil.getProperty(obj, id)
                        + "\", \"parentId\":\"" + pId + "\",\"name\":\""
                        + PropertyUtil.getProperty(obj, name) + "\", \"open\": \"true\"}");
            }
            json.append("]}");

            return json.toString();
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     *
     * <p>双向选择器json</p>
     * @param from
     * @param fromKey
     * @param fromValue
     * @param to
     * @param toKey
     * @param toValue
     * @return
     */
    public static String jsonLister(Collection from, String fromKey, String fromValue,
                                    Collection to, String toKey, String toValue) {
        try {
            StringBuffer json = new StringBuffer("{\"fromList\":[");
            int i = 0;
            for (Iterator it = from.iterator(); it.hasNext(); ) {
                Object obj = it.next();

                if (i++ != 0) {
                    json.append(",");
                }
                json.append("{\"value\":\"" + PropertyUtil.getProperty(obj, fromKey)
                        + "\",\"key\":\"" + PropertyUtil.getProperty(obj, fromValue) + "\"}");
            }
            json.append("],\"toList\":[");

            if (to != null && !StringUtil.isBlank(toKey) && !StringUtil.isBlank(toValue)) {
                int j = 0;
                for (Iterator it = to.iterator(); it.hasNext(); ) {
                    Object obj = it.next();
                    if (j++ != 0) {
                        json.append(",");
                    }
                    json.append("{\"value\":\"" + PropertyUtil.getProperty(obj, toKey)
                            + "\",\"key\":\"" + PropertyUtil.getProperty(obj, toValue) + "\"}");
                }
            }
            json.append("]}");

            return json.toString();
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * <p>自动提示框</p>
     *
     * @param c
     * @param key
     * @param value
     * @param suggests
     * @return
     */
    public static String jsonSuggestion(Collection c, String key, String value, String... suggests) {
        // {"list":[{value:"1",key:"北京",suggest:"北京|beijing|bj"},{value:"2",key:"广州",suggest:"广州|guangzhou|gz"}]};
        try {
            StringBuffer json = new StringBuffer("{\"list\":[");
            int i = 0;
            for (Iterator it = c.iterator(); it.hasNext(); ) {
                Object obj = it.next();

                if (i++ != 0) {
                    json.append(",");
                }
                json.append("{\"value\":\"" + PropertyUtil.getProperty(obj, key) + "\",\"key\":\""
                        + PropertyUtil.getProperty(obj, value) + "\",\"suggest\":\"");
                int j = 0;
                for (String suggest : suggests) {
                    if (j++ != 0) {
                        json.append("|");
                    }
                    json.append(PropertyUtil.getProperty(obj, suggest));
                }
                json.append("\"}");
            }
            json.append("]}");

            return json.toString();
        } catch (Exception e) {
            logger.error(null, e);
            return null;
        }
    }

    /**
     * 生成n位随机的数字
     *
     * @param size
     * @return
     */
    public static String toRandom(int size) {
        if (size <= 0) {
            size = 6;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(new Random().nextInt(10));
        }
        return sb.toString();
    }

    /**
     * <p>
     * description:向左补齐
     * </p>
     *
     * @param size
     * @param origin
     * @param padding
     * @return
     * @author sutong
     * @since 2014年11月3日
     */
    public static String leftPadding(int size, String origin, String padding) {
        if (isBlank(origin) || origin.length() > size) {
            return origin;
        }
        StringBuffer sb = new StringBuffer();
        int diff = size - origin.length();
        for (int i = 0; i < diff; i++) {
            sb.append(padding);
        }
        sb.append(origin);
        return sb.toString();
    }
}
