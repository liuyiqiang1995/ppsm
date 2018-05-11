package com.ppsm.mobile.aspect;

import com.ppsm.mobile.sys.NaomsSystemContext;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 22:23 2018/5/10
 */
@Component
public class JobCoreAspect {
    private static Logger logger = LoggerFactory.getLogger(JobCoreAspect.class);
    private static Logger loggerTimeSpend = LoggerFactory.getLogger("serviceInvokeSpendTime");

    public void doAfterReturn(final JoinPoint jp) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(NaomsSystemContext.getServiceInvokeDate().get());
        long timeSpend = Calendar.getInstance().getTimeInMillis() - calendar2.getTimeInMillis();
        logger.info("{}服务调用完成,耗时{}毫秒", getLogMessage(jp), timeSpend);
        if (timeSpend > 5000) {
            loggerTimeSpend.info(getLogMessage(jp) + " spend" + timeSpend + "毫秒");
        }
        NaomsSystemContext.destroyResource();
    }

    public void doBeforeInvoke(JoinPoint jp) {

        NaomsSystemContext.getServiceInvokeDate().set(new Date());

        String bizClassName = jp.getTarget().getClass().getName();

        String methodName = jp.getSignature().getName();

        NaomsSystemContext.getClientInvokeInfoThreadLocal().get().setBizClass(bizClassName);
        NaomsSystemContext.getClientInvokeInfoThreadLocal().get().setBizMethodName(methodName);
        NaomsSystemContext.getClientInvokeInfoThreadLocal().get().setOperationTime(new Date());
        logger.info(getLogMessage(jp));
    }

    private String getLogMessage(JoinPoint jp) {

        Object[] args = jp.getArgs();
        String argDesc = "";
        if (args != null) {
            for (Object object : args) {
                argDesc += object == null ? "null" : object.toString() + ",";
            }
        }
        String message = String.format("调用类 %1$s 调用方法 %2$s 调用参数 %3$s",
                NaomsSystemContext.getClientInvokeInfoThreadLocal().get().getBizClass(),
                NaomsSystemContext.getClientInvokeInfoThreadLocal().get().getBizMethodName(), argDesc);
        return message;

    }
}
