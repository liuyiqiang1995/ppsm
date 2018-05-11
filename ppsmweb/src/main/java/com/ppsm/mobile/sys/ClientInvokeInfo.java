/**
 * Copyright 2002-2013 Qingdao Civil Aviation Cares Co., LTD
 * All Rights Reserved.
 * 2013年11月16日 下午2:42:16 darkblue
 */
package com.ppsm.mobile.sys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 22:11 2018/5/10
 */
public class ClientInvokeInfo implements Serializable {

	private static final long serialVersionUID = 5416256715246196674L;

	private String bizClass;// 业务类

	private String bizMethodName;// 方法名

	private Date operationTime;// 操作时间

	public String getBizClass() {
		return bizClass;
	}

	public void setBizClass(String bizClass) {
		this.bizClass = bizClass;
	}

	public String getBizMethodName() {
		return bizMethodName;
	}

	public void setBizMethodName(String bizMethodName) {
		this.bizMethodName = bizMethodName;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
}
