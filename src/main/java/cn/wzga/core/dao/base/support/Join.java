package cn.wzga.core.dao.base.support;

import java.io.Serializable;

/**
 * <p>连接方式</p>
 *
 * @author cl
 * @since 2014/11/7
 */
@SuppressWarnings("serial")
public class Join implements Serializable {
    public static enum JOIN_DIRECTION {
        LEFT,
        RIGHT,
        INNER
    }


    public static String pojo = "#POJO#";

    private StringBuffer joinStr;

    public Join() {
        joinStr = new StringBuffer();
    }

    public static Join getInstance() {
        return new Join();
    }

    public Join join(String field) {
        return join(JOIN_DIRECTION.LEFT, true, field);

    }

    public Join join(JOIN_DIRECTION direction, boolean isFetch, String field) {
        if (joinStr == null || null == direction) {
            return this;
        }

        String joinDirStr;
        switch (direction) {
            case RIGHT:
                joinDirStr = "right outer";
                break;

            case INNER:
                joinDirStr = "inner";
                break;

            default:
                joinDirStr = "left outer";
                break;
        }

        // direction
        joinStr.append(joinDirStr);
        joinStr.append(" join");

        // fetch

        if (isFetch) {
            joinStr.append(" fetch ");

        }

        if (field.indexOf(".") < 0) {
            joinStr.append(pojo);
            joinStr.append(".");
            joinStr.append(field);
            joinStr.append(" ");
        }

        joinStr.append(field);
        joinStr.append(" ");
        return this;
    }

    public Join joinNoFetch(String field) {
        return join(JOIN_DIRECTION.LEFT, false, field);
    }

    public String toString(String pojoClass) {
        if (joinStr != null) {
            return joinStr.toString().replace(pojo, pojoClass);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (joinStr != null) {
            return joinStr.toString();
        } else {
            return null;
        }
    }

}
