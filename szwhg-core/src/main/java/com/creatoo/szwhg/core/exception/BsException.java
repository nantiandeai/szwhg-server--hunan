package com.creatoo.szwhg.core.exception;

/**
 * Created by yunyan on 2017/3/17.
 */
public class BsException extends RuntimeException{

    private static final long serialVersionUID = -4491620812235998645L;
    private int errorCode=400;

    public BsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BsException(String message) {
        super(message);
    }
    public BsException(int code,String message){
        super(message);
        this.errorCode=code;
    }
    public int getErrorCode(){
        return this.errorCode;
    }

}