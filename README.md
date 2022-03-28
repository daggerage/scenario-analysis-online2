# Scenario Analysis Online 2 (SAO2)

Watershed Best Management Practices Scenario Analysis System

流域管理措施空间配置方案自动优化系统



# Install

## 1. SEIMS

### 1.1 Install and Build SEIMS

Please refer to the SEIMS user manual.

[lreis2415/SEIMS: Spatially Explicit Integrated Modeling System --- open-source, cross-platform, and high performance computation (github.com)](https://github.com/lreis2415/SEIMS)

### 1.2 Configure SEIMS to support SAO2 to run on the Youwuzhen watershed

First, put the **spatial configuration unit GeoTIFF files** in `SEIMS\data\youwuzhen\data_prepare\spatial`: 

- `hydrologically_connected_fields.tif` required.
- `SLOPEPOSITION.tif` required.
- `slppos_units.tif`
- `spatial_nonunique_hrus.tif` required.
- `spatial_unique_hrus.tif`

Second, modify the `data/youwuzhen/model_configs/preprocess.ini` template, add a line in the `[SPATIAL]` section:

```
additionalFile = {"SLPPOS_UNITS" : "slppos_units.tif", "SPATIAL_NONUNIQUE_HRUS" : "spatial_nonunique_hrus.tif"}
```

Third, run the SEIMS preprocessing scipt, `python demo_preprocessing.py -name youwuzhen`.

Last, Make sure the **spatial configuration unit JSON files** are in `SEIMS\data\youwuzhen\demo_youwuzhen30m_longterm_model`:

- `connected_field_units_updown_15.json` : should have been generated after running SEIMS preprocessing (`python demo_preprocessing.py -name youwuzhen`), not to change the default setting of `field_partition_thresh = 15` in the `demo/youwuzhen30m_longterm_model/model_config/preprocess.ini`
- `hru_units.json` and `slppos_3cls_units_updown.json`: copy them from somewhere and paste them in (?). The hru_units.json corresponds to the spatial nonunique HRUs, while the "spatially unique HRU" and "explicit HRU" are the same meaning.

## 2. PostgreSQL Database

First, create a role named `sao` in your postgres DB. Set the password to `sao` also.

Second, create a databse named `sao`.

Run the SQL file to restore the database

## 3. Backend

Revise the configuration files:

```
sao-server/data/template/scenario_analysis_template.ini
sao-server/src/main/resources/application.properties
```

Run the `sao-server` SpringBoot project

## 4. Frontend

In the sao-web folder, run

```
npm i
```

If it needs python2 for some packages, or there are node-sass errors, you can run

```
npm config set python \path\to\python27
npm i node-sass -g
npm i --force
```

Then

```

npm run dev
```

# Build

