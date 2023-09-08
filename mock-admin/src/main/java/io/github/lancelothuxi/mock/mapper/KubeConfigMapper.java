package io.github.lancelothuxi.mock.mapper;

import java.util.List;
import io.github.lancelothuxi.mock.domain.KubeConfig;

/**
 * k8s管理Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
public interface KubeConfigMapper 
{
    /**
     * 查询k8s管理
     * 
     * @param id k8s管理主键
     * @return k8s管理
     */
    public KubeConfig selectKubeConfigById(Long id);

    /**
     * 查询k8s管理列表
     * 
     * @param kubeConfig k8s管理
     * @return k8s管理集合
     */
    public List<KubeConfig> selectKubeConfigList(KubeConfig kubeConfig);

    /**
     * 新增k8s管理
     * 
     * @param kubeConfig k8s管理
     * @return 结果
     */
    public int insertKubeConfig(KubeConfig kubeConfig);

    /**
     * 修改k8s管理
     * 
     * @param kubeConfig k8s管理
     * @return 结果
     */
    public int updateKubeConfig(KubeConfig kubeConfig);

    /**
     * 删除k8s管理
     * 
     * @param id k8s管理主键
     * @return 结果
     */
    public int deleteKubeConfigById(Long id);

    /**
     * 批量删除k8s管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKubeConfigByIds(String[] ids);
}
