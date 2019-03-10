package org.egc.sao.service;

import org.egc.sao.domain.StructBMP;

import java.util.List;

public interface StructBMPService {
    List<StructBMP> findAll();

    List<StructBMP> findAllBySubscenario(int subscenario);

    List<StructBMP> insert(List<StructBMP> asm);

    StructBMP insert(StructBMP asm);
}
