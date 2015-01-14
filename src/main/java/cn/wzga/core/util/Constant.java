package cn.wzga.core.util;

/**
 * <p>
 * Description: 常量类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
public class Constant {
    public final static String EXT = "";

    public final static String MENU_SESSION = "MENU_SESSION";

    public final static String RAND_SESSION = "RAND_SESSION";

    public final static String SYS_USER_SESSION = "sysUser";

    public final static String BANK_SESSION = "BANK_SESSION";

    public final static String IMAGE_DATA = "IMAGE_DATA";

    public static String REAL_PATH;

    public final static String FILE_PATH = "/home/fileSystem/temp/";

    // Oracle sequences的缓存大小,与数据库对应
    public final static int ALLOCATION_SIZE = 0;

    // 超级管理员ID
    public final static long ADMIN_ID = 1l;

    // 每页默认条数
    public final static String PAGE_SIZE = "20";

    // 每页默认条数
    public final static String PAGE_SIZE15 = "15";

    // 假删除状态
    public final static int STATE_NOMAL = 1; // 正常
    public final static int STATE_DELETED = 2;// 已经删除

    // 资源权限类型
    public final static int RESOURCE_TYPE_MENU = 1;// 菜单资源
    public final static int RESOURCE_TYPE_OP = 2;// 操作资源

    // 请求查询状态
    public final static int QUERY_STATUS_1 = 1;// 未审核
    public final static int QUERY_STATUS_2 = 2;// 被退回
    public final static int QUERY_STATUS_3 = 3;// 已审核
    public final static int QUERY_STATUS_4 = 4;// 已签收
    public final static int QUERY_STATUS_5 = 5;// 已反馈

}
