package com.sinontech.tools.common.result;

public enum ComplaintEnum {
	// 成功or失败
	SUCCESS(0, "成功"), FAIL(101, "失败"),

	// 工单类型
	TYPE_APPEAL(1, "投诉"), TYPE_RETURN(2, "退订"),
	// 运营商
	MERCHANT_DIANXIN(1, "电信"), MERCHANT_YIDONG(2, "移动"), MERCHANT_LIANTONG(3, "联通"),
	// 来源
	SOURCE_WOWMUSIC(1, "沃音乐"), SOURCE_SUPPORT(2, "客服"),
	// 类别
	CATEGORY_DAY_APPEAL(1, "日常投诉"), CATEGORY_PRM_APPEAL(2, "PRM投诉"), CATEGORY_SUPPORT(3,
			"咨询退订"), CATEGORY_SUPPORT_APPEAL(4, "欣网投诉");

	private Integer code;
	private String desc;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private ComplaintEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
