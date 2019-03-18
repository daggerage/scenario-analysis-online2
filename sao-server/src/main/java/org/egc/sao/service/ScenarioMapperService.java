package org.egc.sao.service;

import org.egc.sao.domain.OptimizeAlgorithm;
import org.egc.sao.domain.ScenarioConfigStrategy;
import org.egc.sao.domain.ScenarioUnitDelineation;

public interface ScenarioMapperService {
    OptimizeAlgorithm findOptimizeAlgorithm(String name);
    OptimizeAlgorithm findOptimizeAlgorithm(int id);
    ScenarioConfigStrategy findScenarioConfigStrategy(String name);
    ScenarioConfigStrategy findScenarioConfigStrategy(int id);
    ScenarioUnitDelineation findScenarioUnitDelineation(String name);
    ScenarioUnitDelineation findScenarioUnitDelineation(int id);
}
