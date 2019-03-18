package org.egc.sao.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ScenarioAnalysisResult {
    UUID id;
    double result;
    String url;
}
