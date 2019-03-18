package org.egc.sao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ScenarioRecord {
    UUID id;
    LocalDateTime createdOn;
    UUID accountId;
    String title;

    UUID scenarioAnalysisResultId;
    @JsonIgnore
    Integer scenarioConfigStrategyId;
    @JsonIgnore
    Integer scenarioUnitDelineationId;
    @JsonIgnore
    Integer optimizeAlgorithmId;

    @Transient
    int result;
    @Transient
    String optimizeAlgorithm;
    @Transient
    String scenarioUnitDelineation;
    @Transient
    String scenarioConfigStrategy;


}
