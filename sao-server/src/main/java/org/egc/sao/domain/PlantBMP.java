package org.egc.sao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "PLANT_MANAGEMENT")
public class PlantBMP {
    /*
    Example:
        "_id" : ObjectId("5c48418a32aca72b891a485a"),
        "BASE_HU" : 1,
        "MGT8" : 0,
        "HUSC" : 0.19,
        "MGT_OP" : 13,
        "NAME" : "RICE_PADDY_CROP_ROTATION",
        "MGT9" : 0,
        "SEQUENCE" : 1,
        "MGT1" : 0,
        "MGT2" : 50,
        "MGT3" : 10,
        "MGT4" : 30,
        "MGT5" : 0,
        "MGT6" : 0,
        "MONTH" : 5,
        "MGT10" : 0,
        "MGT7" : 0,
        "LANDUSE_ID" : 33,
        "YEAR" : 1,
        "SUBSCENARIO" : 0,
        "DAY" : 1
     */

    @Id
    private String id;

    private int baseHu;
    @JsonIgnore
    private int mgt8;
    private int husc;
    private int mgtOp;
    private String name;
    @JsonIgnore
    private int mgt9;
    private int sequence;
    @JsonIgnore
    private int mgt1;
    @JsonIgnore
    private int mgt2;
    @JsonIgnore
    private int mgt3;
    @JsonIgnore
    private int mgt4;
    @JsonIgnore
    private int mgt5;
    @JsonIgnore
    private int mgt6;
    private int month;
    @JsonIgnore
    private int mgt10;
    @JsonIgnore
    private int mgt7;
    private int landuseId;
    private int year;
    private int subscenario;
    private int day;

    //[key,value]
    @BsonIgnore
    private ArrayList<String[]> paramDetail;

    public void constructParamDetail(){
        //hard-coded as these plant managements may be one-offs
        paramDetail=new ArrayList<>();
        this.paramDetail.add(new String[]{"mgt1", String.valueOf(mgt1)});
        this.paramDetail.add(new String[]{"mgt2", String.valueOf(mgt2)});
        this.paramDetail.add(new String[]{"mgt3", String.valueOf(mgt3)});
        this.paramDetail.add(new String[]{"mgt4", String.valueOf(mgt4)});
        this.paramDetail.add(new String[]{"mgt5", String.valueOf(mgt5)});
        this.paramDetail.add(new String[]{"mgt6", String.valueOf(mgt6)});
        this.paramDetail.add(new String[]{"mgt7", String.valueOf(mgt7)});
        this.paramDetail.add(new String[]{"mgt8", String.valueOf(mgt8)});
        this.paramDetail.add(new String[]{"mgt9", String.valueOf(mgt9)});
        this.paramDetail.add(new String[]{"mgt10", String.valueOf(mgt10)});
    }
}
