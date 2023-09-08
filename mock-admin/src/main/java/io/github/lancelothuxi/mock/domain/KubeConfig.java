package io.github.lancelothuxi.mock.domain;

import io.github.lancelothuxi.mock.common.annotation.Excel;
import io.github.lancelothuxi.mock.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * k8s管理对象 kube_config
 *
 * @author ruoyi
 * @date 2023-09-08
 */
public class KubeConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 是否生效 */
    @Excel(name = "是否生效")
    private Long isValid;

    /** kuebeconfig文件内容 */
    @Excel(name = "kuebeconfig文件内容")
    private String content;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setIsValid(Long isValid)
    {
        this.isValid = isValid;
    }

    public Long getIsValid()
    {
        return isValid;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("isValid", getIsValid())
                .append("content", getContent())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
