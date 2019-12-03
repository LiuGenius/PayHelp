package com.fanzhe.payhelp.config;

public class UrlAddress {
    private static final String Host = "https://pay.33th.me/v2";
    /**
     * 用户登录
     */
    public static final String USER_LOGIN = Host + "/admin/connect/login";
    /**
     * websocket链接地址
     */
    public static final String WEB_SOCKET_URL = "wss://paywebsocket.33th.me";
    /**
     * 检查版本
     */
    public static final String CHECK_NEW_VERSION = Host + "/update/?name=米来&versionName=%s";
    /**
     * 通道列表
     */
    public static final String CHANNEL_LIST = Host + "/pay/channel/channel-list";
    /**
     * 平台查看码商通道列表
     */
    public static final String ORG_CODE_CHANNEL_LIST = Host + "/pay/channel-instance/coder-channel";
    /**
     * 切换通道状态
     */
    public static final String CHANNEL_SWITCH_STATE = Host + "/pay/channel/switch-status";
    /**
     * 用户/码商列表
     */
    public static final String USER_LIST = Host + "/admin/user/user-list";
    /**
     * 添加/编辑 商户/码商
     */
    public static final String USER_EDIT_ADD = Host + "/admin/user/user-edit";
    /**
     * 码商通道实例
     */
    public static final String CODE_CHANNEL_LIST = Host + "/pay/channel-instance/channel-list";
    /**
     * 码商开启/关闭实例
     */
    public static final String CODE_CHANNEL_SWITCH_STATE = Host + "/pay/channel-instance/switch-instance-status";
    /**
     * 平台码商入款
     */
    public static final String ORG_PRE_DEPOSIT = Host + "/pay/balance/pre-deposit";
    /**
     * 平台删除用户
     */
    public static final String ORG_DEL_USER = Host + "/admin/user/user-del";
    /**
     * 平台查看码商入款记录
     */
    public static final String ORH_IMPORT_DEPOSIT = Host + "/pay/balance/balance-log";
    /**
     * 订单列表
     */
    public static final String ORDER_LIST = Host + "/pay/order/order-list";
    /**
     * 码商查看子通道列表
     */
    public static final String CODE_CHILD_CHANNEL = Host + "/pay/channel-instance/sub-instance-list";
    /**
     * 上传图片文件
     */
    public static final String UPDATE_FILE = Host + "/common/upload";
    /**
     * 码商保存子通道
     */
    public static final String CODE_SAVE_CHILD_CHANNEL = Host + "/pay/channel-instance/edit-sub-instance";
    /**
     * 商户查看密钥
     */
    public static final String LOOK_PS_KET = Host + "/admin/user/view-secret-key";
    /**
     * 费率列表
     */
    public static final String RATE_LIST = Host + "/pay/pay-conf/conf-list";
    /**
     * 修改费率
     */
    public static final String EDIT_RATE = Host + "/pay/pay-conf/edit-conf";
    /**
     * 修改密码
     */
    public static final String EDIT_PASSWORD = Host + "/admin/user/edit-pass";
    /**
     * 结算列表
     */
    public static final String SETTLEMENT_LIST = Host + "/pay/settlement/statistical";
    /**
     * 结算明细
     */
    public static final String SETTLEMENT_INFO_LIST = Host + "/pay/settlement/settlement-log";
    /**
     * 结算
     */
    public static final String SETTLEMENT = Host + "/pay/settlement/settlement-op";
    /**
     * 首页数据
     */
    public static final String INDEX_DATA = Host + "/pay/settlement/index-data";
    /**
     * 掉单处理
     */
    public static final String OFF_SINGLE = Host + "/pay/order/edit-order";
    /**
     * 切换用户状态
     */
    public static final String USER_SWITCH_STATUS = Host + "/admin/user/switch-status";
    /**
     * 码农充值
     */
    public static final String MA_NONG_RECHARGE = Host + "/pay/balance/recharge";
    /**
     * 平台查看商户码农
     */
    public static final String ORG_LOOK_MCH_MA_NONG = Host + "/admin/user/mch-acmen-list";
    /**
     * 平台对商户添加码农
     */
    public static final String ORG_BIND_ACMEN = Host + "/admin/user/bind-acmen";
    /**
     * 充值记录
     */
    public static final String RECHARGE_LIST = Host + "/pay/balance/recharge-list";

    /**
     * 获取充值列表
     */
    public static final String CHARGE_CONF = Host + "/pay/pay-conf/charge-conf";
    /**
     * 保存金额信息
     */
    public static final String EDIT_MONEY_SETTING = Host + "/pay/pay-conf/edit-params";
    /**
     * 掉单列表
     */
    public static final String OUT_ORDER_LIST = Host + "/pay/order/order-off-list";
}
