package com.ruolan.springtest.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruolan.springtest.cache.JedisUtil;
import com.ruolan.springtest.dao.AreaDao;
import com.ruolan.springtest.dto.AreaExecution;
import com.ruolan.springtest.entity.Area;
import com.ruolan.springtest.enums.AreaStateEnum;
import com.ruolan.springtest.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private AreaDao areaDao;

    private static String AREALISTKEY = "arealist";

    @Override
    public List<Area> getAreaList() throws IOException {
        String key = AREALISTKEY;
        List<Area> areaList = null;
        ObjectMapper mapper = new ObjectMapper();
        //判断是否有缓存
        if (!jedisKeys.exists(key)) {
            //没哟缓存  则查询数据库
            areaList = areaDao.queryArea();
            String jsonString = mapper.writeValueAsString(areaList);
            jedisStrings.set(key, jsonString);
        } else {
            //有缓存  这个时候从缓存中通过key获取到缓存的数据  然后转换为我们需要的数据
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory()
                    .constructParametricType(ArrayList.class, Area.class);
            areaList = mapper.readValue(jsonString, javaType);
        }
        return areaList;
    }

    @Override
    @Transactional
    public AreaExecution addArea(Area area) {
        if (area.getAreaName() != null && !"".equals(area.getAreaName())) {
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try {
                int effectedNum = areaDao.insertArea(area);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("添加区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public AreaExecution modifyArea(Area area) {
        if (area.getAreaId() != null && area.getAreaId() > 0) {
            area.setLastEditTime(new Date());
            try {
                int effectedNum = areaDao.updateArea(area);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("更新区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public AreaExecution removeArea(long areaId) {
        if (areaId > 0) {
            try {
                int effectedNum = areaDao.deleteArea(areaId);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("删除区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public AreaExecution removeAreaList(List<Long> areaIdList) {
        if (areaIdList != null && areaIdList.size() > 0) {
            try {
                int effectedNum = areaDao.batchDeleteArea(areaIdList);
                if (effectedNum > 0) {
                    String key = AREALISTKEY;
                    if (jedisKeys.exists(key)) {
                        jedisKeys.del(key);
                    }
                    return new AreaExecution(AreaStateEnum.SUCCESS);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("删除区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }
}
