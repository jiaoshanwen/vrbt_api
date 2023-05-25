package com.sinontech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sinontech.service.CooperatorInfoService;
import com.sinontech.tools.common.Const;
import com.sinontech.tools.common.RateLimiterUtils;

@Component
public class StartRunner implements CommandLineRunner{

	@Autowired
	private CooperatorInfoService cooperatorInfoService;
	
	@Override
	public void run(String... args) throws Exception {
		try {
			Const.SYS_COOPERATOR_INFO = cooperatorInfoService.listAll(null);
			RateLimiterUtils.loadRateLimiter(Const.SYS_COOPERATOR_INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
