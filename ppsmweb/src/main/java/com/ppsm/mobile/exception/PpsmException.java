package com.ppsm.mobile.exception;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 20:09 2018/5/10
 */
public class PpsmException extends Exception {

    private static final long serialVersionUID = -8235565891256319509L;

    public PpsmException(String message, Throwable cause,
                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PpsmException(String message, Throwable cause) {
        super(message, cause);
    }

    public PpsmException(String message) {
        super(message);
    }

}
