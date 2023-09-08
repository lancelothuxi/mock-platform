package io.github.lancelothuxi.mock.service.impl;

import java.util.List;

import io.github.lancelothuxi.mock.common.core.text.Convert;
import io.github.lancelothuxi.mock.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.lancelothuxi.mock.mapper.KubeConfigMapper;
import io.github.lancelothuxi.mock.domain.KubeConfig;
import io.github.lancelothuxi.mock.service.IKubeConfigService;

/**
 * k8s管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
@Service
public class KubeConfigServiceImpl implements IKubeConfigService 
{
    @Autowired
    private KubeConfigMapper kubeConfigMapper;

    /**
     * 查询k8s管理
     * 
     * @param id k8s管理主键
     * @return k8s管理
     */
    @Override
    public KubeConfig selectKubeConfigById(Long id)
    {
        return kubeConfigMapper.selectKubeConfigById(id);
    }

    /**
     * 查询k8s管理列表
     * 
     * @param kubeConfig k8s管理
     * @return k8s管理
     */
    @Override
    public List<KubeConfig> selectKubeConfigList(KubeConfig kubeConfig)
    {
        return kubeConfigMapper.selectKubeConfigList(kubeConfig);
    }

    /**
     * 新增k8s管理
     * 
     * @param kubeConfig k8s管理
     * @return 结果
     */
    @Override
    public int insertKubeConfig(KubeConfig kubeConfig)
    {
        kubeConfig.setCreateTime(DateUtils.getNowDate());
        return kubeConfigMapper.insertKubeConfig(kubeConfig);
    }

    /**
     * 修改k8s管理
     * 
     * @param kubeConfig k8s管理
     * @return 结果
     */
    @Override
    public int updateKubeConfig(KubeConfig kubeConfig)
    {
        kubeConfig.setUpdateTime(DateUtils.getNowDate());
        return kubeConfigMapper.updateKubeConfig(kubeConfig);
    }

    /**
     * 批量删除k8s管理
     * 
     * @param ids 需要删除的k8s管理主键
     * @return 结果
     */
    @Override
    public int deleteKubeConfigByIds(String ids)
    {
        return kubeConfigMapper.deleteKubeConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除k8s管理信息
     * 
     * @param id k8s管理主键
     * @return 结果
     */
    @Override
    public int deleteKubeConfigById(Long id)
    {
        return kubeConfigMapper.deleteKubeConfigById(id);
    }
}
