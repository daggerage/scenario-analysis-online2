package org.egc.sao.service;

import org.egc.sao.domain.StructBMP;

import java.util.List;

public interface StructBMPService {
    List<StructBMP> findAll();

    List<StructBMP> findAllBySubscenario(int subscenario);

    StructBMP findById(String id);

    List<StructBMP> findAllByIds(String[] ids);


    List<StructBMP> insert(List<StructBMP> asm);

    StructBMP insert(StructBMP asm);
}
