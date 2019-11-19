package com.fanzhe.payhelp.config;

public class UrlAddress {
    private static final String Host = "http://xinpay.yc699.com";
    /**
     * 用户登录
     */
    public static final String USER_LOGIN = Host + "/v1/admin/connect/login";
    /**
     * 通道列表
     */
    public static final String CHANNEL_LIST = Host + "/v1/pay/channel/channel-list";
    /**
     * 平台查看码商通道列表
     */
    public static final String ORG_CODE_CHANNEL_LIST = Host + "/v1/pay/channel-instance/coder-channel";
    /**
     * 切换通道状态
     */
    public static final String CHANNEL_SWITCH_STATE = Host + "/v1/pay/channel/switch-status";
    /**
     * 用户/码商列表
     */
    public static final String USER_LIST = Host + "/v1/admin/user/user-list";
    /**
     * 添加/编辑 商户/码商
     */
    public static final String USER_EDIT_ADD = Host + "/v1/admin/user/user-edit";
    /**
     * 码商通道实例
     */
    public static final String CODE_CHANNEL_LIST = Host + "/v1/pay/channel-instance/channel-list";
    /**
     * 码商开启/关闭实例
     */
    public static final String CODE_CHANNEL_SWITCH_STATE = Host + "/v1/pay/channel-instance/switch-instance-status";
    /**
     * 平台码商入款
     */
    public static final String ORG_PRE_DEPOSIT = Host + "/v1/pay/balance/pre-deposit";
    /**
     * 平台删除用户
     */
    public static final String ORG_DEL_USER = Host + "/v1/admin/user/user-del";
    /**
     * 平台查看码商入款记录
     */
    public static final String ORH_IMPORT_DEPOSIT = Host + "/v1/pay/balance/balance-log";
    /**
     * 订单列表
     */
    public static final String ORDER_LIST = Host + "/v1/pay/order/order-list";
    /**
     * 码商查看子通道列表
     */
    public static final String CODE_CHILD_CHANNEL = Host + "/v1/pay/channel-instance/sub-instance-list";
    /**
     * 上传图片文件
     */
    public static final String UPDATE_FILE = Host + "/v1/common/upload";
    /**
     * 码商保存子通道
     */
    public static final String CODE_SAVE_CHILD_CHANNEL = Host + "/v1/pay/channel-instance/edit-sub-instance";
    /**
     * 商户查看密钥
     */
    public static final String LOOK_PS_KET = Host + "/v1/admin/user/view-secret-key";
    /**
     * 费率列表
     */
    public static final String RATE_LIST = Host + "/v1/pay/pay-conf/conf-list";
    /**
     * 修改费率
     */
    public static final String EDIT_RATE = Host + "/v1/pay/pay-conf/edit-conf";
    /**
     * 修改密码
     */
    public static final String EDIT_PASSWORD = Host + "/v1/admin/user/edit-pass";
    /**
     * 结算列表
     */
    public static final String SETTLEMENT_LIST = Host + "/v1/pay/settlement/statistical";
    /**
     * 结算明细
     */
    public static final String SETTLEMENT_INFO_LIST = Host + "/v1/pay/settlement/settlement-log";
    /**
     * 结算
     */
    public static final String SETTLEMENT = Host + "/v1/pay/settlement/settlement-op";
    /**
     * 首页数据
     */
    public static final String INDEX_DATA = Host + "/v1/pay/settlement/index-data";
}
