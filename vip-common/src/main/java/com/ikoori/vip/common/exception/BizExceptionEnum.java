package com.ikoori.vip.common.exception;

/**
 * @Description 所有业务异常的枚举
 * @author fengshuonan
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum {

	/**
	 * 字典
	 */
	DICT_EXISTED(400,"字典已经存在"),
	ERROR_CREATE_DICT(500,"创建字典失败"),
	ERROR_WRAPPER_FIELD(500,"包装字典属性失败"),


	/**
	 * 文件上传
	 */
	FILE_READING_ERROR(400,"FILE_READING_ERROR!"),
	FILE_NOT_FOUND(400,"FILE_NOT_FOUND!"),
	UPLOAD_ERROR(500,"上传图片出错"),

	/**
	 * 权限和数据问题
	 */
	DB_RESOURCE_NULL(400,"数据库中没有该资源"),
	NO_PERMITION(405, "权限异常"),
	REQUEST_INVALIDATE(400,"请求数据格式不正确"),
	INVALID_KAPTCHA(400,"验证码不正确"),
	CANT_DELETE_ADMIN(600,"不能删除超级管理员"),
	CANT_FREEZE_ADMIN(600,"不能冻结超级管理员"),
	CANT_CHANGE_ADMIN(600,"不能修改超级管理员角色"),

	/**
	 * 账户问题
	 */
	USER_ALREADY_REG(401,"该用户已经注册"),
	NO_THIS_USER(400,"没有此用户"),
	USER_NOT_EXISTED(400, "没有此用户"),
	ACCOUNT_FREEZED(401, "账号被冻结"),
	OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
	TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),

	/**
	 * 错误的请求
	 */
	MENU_PCODE_COINCIDENCE(400,"菜单编号和副编号不能一致"),
	EXISTED_THE_MENU(400,"菜单编号重复，不能添加"),
	DICT_MUST_BE_NUMBER(400,"字典的值必须为数字"),
	REQUEST_NULL(400, "请求有错误"),
	SESSION_TIMEOUT(400, "会话超时"),
	SERVER_ERROR(500, "服务器异常"),
	EMPTY_MOBILE(410, "手机号为空"),
	ERROR_MOBILE_CODE(411,"短信验证码不正确"),
	EXISTED_MOBILE(412,"菜单编号重复，不能添加"),
	

	/**
	 * 红包问题
	 * @param code
	 * @param message
	 */
	EXISTED_PACKTYPE(400,"红包类型已有，请选择其他红包类型！"),
	
	/**
	 * 会员问题
	 * @param code
	 * @param message
	 */
	INVALID_MEMBER(500,"没有找到该会员信息"),
	INVALID_cardName(500,"会员卡名称已存在"),
	INVALID_cardLevel(500,"会员卡级别已存在"),
	INVALID_grantType(500,"发卡方式为“关注微信”的会员卡只能有一种"),
	
	/**
	 * 签到
	 */
    error_sign(500, "签到失败"),
    
    
	/**
	 * 优惠券问题
	 */
	coupon_expired(500, "该优惠券已经过期啦"),
	invalid_member_card(500, "您还没有会员卡哦"),
	limit_card_level(500, "该优惠券只能“{cardName}”才能领哦"),
	limit_quota(500, "该优惠券每人只能领 {quota}张哦"),
	coupon_no_stock(500, "该优惠券已经领完啦"),
	invalid_member(500, "您还不是会员哦");
	
	BizExceptionEnum(int code, String message) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
	}
	
	BizExceptionEnum(int code, String message,String urlPath) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
		this.urlPath = urlPath;
	}

	private int friendlyCode;

	private String friendlyMsg;
	
	private String urlPath;

	public int getCode() {
		return friendlyCode;
	}

	public void setCode(int code) {
		this.friendlyCode = code;
	}

	public String getMessage() {
		return friendlyMsg;
	}

	public void setMessage(String message) {
		this.friendlyMsg = message;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

}
