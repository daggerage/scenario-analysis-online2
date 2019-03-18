package org.egc.sao.service.impl;

import org.egc.sao.dao.postgresql.OptimizeAlgorithmMapper;
import org.egc.sao.dao.postgresql.ScenarioConfigStrategyMapper;
import org.egc.sao.dao.postgresql.ScenarioUnitDelineationMapper;
import org.egc.sao.domain.OptimizeAlgorithm;
import org.egc.sao.domain.ScenarioConfigStrategy;
import org.egc.sao.domain.ScenarioUnitDelineation;
import org.egc.sao.service.ScenarioMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioMapperServiceImpl implements ScenarioMapperService {

    private final OptimizeAlgorithmMapper oam;
    private final ScenarioConfigStrategyMapper scsm;
    private final ScenarioUnitDelineationMapper sudm;

    @Autowired
    ScenarioMapperServiceImpl(
            OptimizeAlgorithmMapper oam,
            ScenarioConfigStrategyMapper scsm,
            ScenarioUnitDelineationMapper sudm
    ){
        this.oam=oam;
        this.scsm=scsm;
        this.sudm=sudm;
    }

    @Override
    public OptimizeAlgorithm findOptimizeAlgorithm(String name) {
        return oam.findByName(name);
    }

    @Override
    public OptimizeAlgorithm findOptimizeAlgorithm(int id) {
        return oam.findById(id);
    }

    @Override
    public ScenarioConfigStrategy findScenarioConfigStrategy(String name) {
        return scsm.findByName(name);
    }

    @Override
    public ScenarioConfigStrategy findScenarioConfigStrategy(int id) {
        return scsm.findById(id);
    }

    @Override
    public ScenarioUnitDelineation findScenarioUnitDelineation(String name) {
        return sudm.findByName(name);
    }

    @Override
    public ScenarioUnitDelineation findScenarioUnitDelineation(int id) {
        return sudm.findById(id);
    }
}
