package org.egc.sao;

import org.egc.sao.base.ResInfo;
import org.egc.sao.dao.mongodb.ArealStructManagementDao;
import org.egc.sao.domain.ArealStructManagement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArealStructManagementTest {

    @Autowired
    ArealStructManagementDao dao;

    @Test
    public void findAllStructBMP(){
        List<ArealStructManagement> list= dao.findAll();
        System.out.println(Arrays.toString(list.toArray()));
    }

    @Test
    public void ResCodeTest(){
        ResInfo.SUCCESS.setMsg("aa");
        System.out.println(ResInfo.SUCCESS.getMsg());
    }
}
