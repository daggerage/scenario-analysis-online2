package org.egc.sao.service;

import org.egc.sao.domain.PlantBMP;

import java.util.List;

public interface PlantBMPService {
    long count();
    List<PlantBMP> findAll();
}
