package com.ruolan.springtest.service;

import com.ruolan.springtest.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

//测试类加入如下的两个注解
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {

    @Autowired
    private AreaService areaService;

    // Redis的安装和配置      启动之后再来运行这个是可以的  也就是走缓存了
    //    https://www.jianshu.com/p/6b5eca8d908b

    @Test
    public void testGetAreaList() throws IOException {
        List<Area> areaList = areaService.getAreaList();
        System.out.println("查询出来的地区集合的个数是:" + areaList.size());
    }

}
