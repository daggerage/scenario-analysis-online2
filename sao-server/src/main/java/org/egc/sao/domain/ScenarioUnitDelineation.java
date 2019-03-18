package org.egc.sao.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ScenarioUnitDelineation {
    int id;
    String name;
    String description;

}
