package io.github.lancelothuxi.mock.common.exception.user;

import io.github.lancelothuxi.mock.common.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author lancelot huxisuz@gmail.com
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
