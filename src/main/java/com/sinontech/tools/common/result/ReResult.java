package com.sinontech.tools.common.result;

import com.sinontech.tools.common.PageData;

public class ReResult {
	
	public static PageData success(PageData pd) {
		pd.put("code", ComplaintEnum.SUCCESS.getCode());
		pd.put("msg", ComplaintEnum.SUCCESS.getDesc());
		return pd;
	}
	
	public static PageData fail(PageData pd,String msg) {
		pd.put("code", ComplaintEnum.FAIL.getCode());
		pd.put("msg", msg);
		return pd;
	}

}
