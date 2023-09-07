package io.github.lancelothuxi.mock.system.service.impl;

import io.github.lancelothuxi.mock.common.core.domain.entity.SysDictData;
import io.github.lancelothuxi.mock.common.core.text.Convert;
import io.github.lancelothuxi.mock.common.utils.DictUtils;
import io.github.lancelothuxi.mock.system.mapper.SysDictDataMapper;
import io.github.lancelothuxi.mock.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author lancelot huxisuz@gmail.com
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {
  @Autowired private SysDictDataMapper dictDataMapper;

  /**
   * 根据条件分页查询字典数据
   *
   * @param dictData 字典数据信息
   * @return 字典数据集合信息
   */
  @Override
  public List<SysDictData> selectDictDataList(SysDictData dictData) {
    return dictDataMapper.selectDictDataList(dictData);
  }

  /**
   * 根据字典类型和字典键值查询字典数据信息
   *
   * @param dictType 字典类型
   * @param dictValue 字典键值
   * @return 字典标签
   */
  @Override
  public String selectDictLabel(String dictType, String dictValue) {
    return dictDataMapper.selectDictLabel(dictType, dictValue);
  }

  /**
   * 根据字典数据ID查询信息
   *
   * @param dictCode 字典数据ID
   * @return 字典数据
   */
  @Override
  public SysDictData selectDictDataById(Long dictCode) {
    return dictDataMapper.selectDictDataById(dictCode);
  }

  /**
   * 批量删除字典数据
   *
   * @param ids 需要删除的数据
   */
  @Override
  public void deleteDictDataByIds(String ids) {
    Long[] dictCodes = Convert.toLongArray(ids);
    for (Long dictCode : dictCodes) {
      SysDictData data = selectDictDataById(dictCode);
      dictDataMapper.deleteDictDataById(dictCode);
      List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
      DictUtils.setDictCache(data.getDictType(), dictDatas);
    }
  }

  /**
   * 新增保存字典数据信息
   *
   * @param data 字典数据信息
   * @return 结果
   */
  @Override
  public int insertDictData(SysDictData data) {
    int row = dictDataMapper.insertDictData(data);
    if (row > 0) {
      List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
      DictUtils.setDictCache(data.getDictType(), dictDatas);
    }
    return row;
  }

  /**
   * 修改保存字典数据信息
   *
   * @param data 字典数据信息
   * @return 结果
   */
  @Override
  public int updateDictData(SysDictData data) {
    int row = dictDataMapper.updateDictData(data);
    if (row > 0) {
      List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
      DictUtils.setDictCache(data.getDictType(), dictDatas);
    }
    return row;
  }
}
