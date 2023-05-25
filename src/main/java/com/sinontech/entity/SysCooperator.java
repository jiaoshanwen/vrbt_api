package com.sinontech.entity;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SysCooperator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1293739817749846646L;
	private Long cooperatorId;
	private String cooperatorName;
	private String loginName;
	private String loginPwd;
	private String loginIp;
	private String state;
	private String remark;
	private Integer credibleLevel;
	private String requestIp;
	private Double qps;

	public SysCooperator(Long cooperatorId, String cooperatorName,
			String loginName, String loginPwd, String loginIp, String state,
			String remark, Integer credibleLevel) {
		this.cooperatorId = cooperatorId;
		this.cooperatorName = cooperatorName;
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.loginIp = loginIp;
		this.state = state;
		this.remark = remark;
		this.credibleLevel = credibleLevel;
	}

	public SysCooperator() {
	}

 

	public Long getCooperatorId() {
		return this.cooperatorId;
	}

	public void setCooperatorId(Long cooperatorId) {
		this.cooperatorId = cooperatorId;
	}

	public String getCooperatorName() {
		return this.cooperatorName;
	}

	public void setCooperatorName(String cooperatorName) {
		this.cooperatorName = cooperatorName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return this.loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCredibleLevel() {
		return this.credibleLevel;
	}

	public void setCredibleLevel(Integer credibleLevel) {
		this.credibleLevel = credibleLevel;
	}

	public String getRequestIp() {
		return this.requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String toString() {
		return new ToStringBuilder(this).append("cooperatorId",
				getCooperatorId()).toString();
	}

	public Double getQps() {
		return qps;
	}

	public void setQps(Double qps) {
		this.qps = qps;
	}
	
}