package io.github.lancelothuxi.mock.common.exception.file;

import io.github.lancelothuxi.mock.common.exception.base.BaseException;

/**
 * 文件信息异常类
 * 
 * @author lancelot huxisuz@gmail.com
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
