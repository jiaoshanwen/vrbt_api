package com.sinontech.time;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sinontech.service.CooperatorInfoService;
import com.sinontech.tools.common.Const;
import com.sinontech.tools.common.RateLimiterUtils;



@Component
public class RefreshCooperatorInfoTimer {
	protected static final Logger log = Logger.getLogger(RefreshCooperatorInfoTimer.class);
	
	@Autowired
	private CooperatorInfoService cooperatorInfoService;
	/**
	 *    更新接口信息
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void openUserTimer() {
		log.info("更新接口账号信息开始");
		try {
			Const.SYS_COOPERATOR_INFO = cooperatorInfoService.listAll(null);
			RateLimiterUtils.loadRateLimiter(Const.SYS_COOPERATOR_INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("更新接口账号信息结束");
	}
	
}
