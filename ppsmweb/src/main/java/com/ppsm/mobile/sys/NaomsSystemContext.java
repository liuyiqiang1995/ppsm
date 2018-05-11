package com.ppsm.mobile.sys;

import java.util.Date;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 22:08 2018/5/10
 */
public class NaomsSystemContext {

    private static ThreadLocal<Date> serviceInvokeDate = new ThreadLocal<Date>();

    /**
     * @return the serviceInvokeDate
     */
    public static ThreadLocal<Date> getServiceInvokeDate() {
        return serviceInvokeDate;
    }


    /**
     * 存储客户端相关调用相关信息
     */
    private static ThreadLocal<ClientInvokeInfo> clientInvokeInfoThreadLocal = new ThreadLocal<ClientInvokeInfo>() {

        /*
         * (non-Javadoc)
         *
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected ClientInvokeInfo initialValue() {
            return new ClientInvokeInfo();
        }

    };

    /**
     * 存储客户端相关调用相关信息
     */
    public static ThreadLocal<ClientInvokeInfo> getClientInvokeInfoThreadLocal() {
        return clientInvokeInfoThreadLocal;
    }

    public static ThreadLocal<Boolean> aspectFlag = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };
    public static ThreadLocal<Boolean> getAspectFlag(){
        return aspectFlag;
    }

    public static ThreadLocal<Boolean> auditFlag = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };
    public static ThreadLocal<Boolean> getAuditFlag(){
        return auditFlag;
    }

    /**
     * 删除调用过程中产生的资源
     */
    public static void destroyResource() {
        clientInvokeInfoThreadLocal.set(new ClientInvokeInfo());
        aspectFlag.set(true);
    }

}
