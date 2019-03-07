package org.egc.sao.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "AREAL_STRUCT_MANAGEMENT")
public class  ArealStructManagement {
    /*
    example:
        "_id" : ObjectId("5c48418a32aca72b891a482e"),
        "SUBSCENARIO" : 1,
        "NAME" : "FENGJIN",
        "REFERENCE" : "FENGJIN",
        "PARAMETERS" : "OM:ORGANIC MATTER:RC:1.22-DENSITY:BULK DENSITY:RC:0.98-POROSITY:TOTAL POROSITY:RC:1.02-USLE_K:USLE SOIL ERODIBILITY:RC:1.01-CONDUCTIVITY:SOIL HYDRAULIC CONDUCTIVITY:RC:0.81-FIELDCAP:SOIL FIELD CAPACITY:RC:1.02-WILTINGPOINT:WILTINGPOINT:RC:1.02-SOL_AWC:SOIL AVAILABLE WATER:RC:1.02-SOL_UL:SOIL SATURATED WATER:RC:1.02-SOL_CBN:SOIL CARBON CONTENT:RC:1.22-USLE_P:USLE PRACTICE FACTOR:RC:0.9",
        "EFFECTIVENESS" : 3,
        "OPEX" : 1.5,
        "INCOME" : 2,
        "LANDUSE" : "8-6",
        "SLPPOS" : "1-4",
        "CAPEX" : 15.5,
        "DESC" : "FENGJIN"
     */
    @Id
    private ObjectId id;
    private int subscenario;
    private String name;
    private String reference;
    private String parameters;
    private int effectiveness;
    private double opex;
    private int incomeSnakeTestDBIFYouCan;
    private String landuse;
    private String slppos;
    private double capex;
    private String desc;
}
