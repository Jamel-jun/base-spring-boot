package com.springboot.autoconfig.error.exception;

import com.springboot.autoconfig.error.BaseErrors;
import org.springframework.http.HttpStatus;

/**
 * @author ray4work@126.com
 * @date 2018/6/5 16:17
 */
public class ClientSideException extends BizException {

    public <E extends Enum<E> & BaseErrors> ClientSideException(E exceptionCode, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, exceptionCode, cause);
    }

    public <E extends Enum<E> & BaseErrors> ClientSideException(E exceptionCode) {
        super(HttpStatus.BAD_REQUEST, exceptionCode, null);
    }
}
