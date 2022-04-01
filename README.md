# Scenario Analysis Online 2 (SAO2)

Watershed Best Management Practices Scenario Analysis System

流域管理措施空间配置方案自动优化系统

[toc]

# Install

## 1. SEIMS

### 1.1 Install and Build SEIMS

SAO2 is essentially a Web Demo for the scenario analysis function of SEIMS (yet only for Youwuzhen watershed), so it's the first thing to install SEIMS. As the dev branch of SEIMS is under development and unstable, The SAO2 project is developed based on a feature branch of SEIMS - `feature/scenario-analysis-online-2`, which is hoped to be merged in the future. 

You can clone this branch: [lreis2415/SEIMS at feature/scenario-analysis-online-2 (github.com)](https://github.com/lreis2415/SEIMS/tree/feature/scenario-analysis-online-2) using

```
git clone -b feature/scenario-analysis-online-2 https://github.com/lreis2415/SEIMS.git
```

For installation and build, please refer to the SEIMS user manual.

### 1.2 Reinstall PyGeoC to be compatible with SEIMS

PyGeoC is a SEIMS python dependency. However, the latest master branches of SEIMS (2020-7-29) and PyGeoC (2022-1-11) are not compatible. To adress this problem:

1. revert PyGeoC repo to the `2021-4-8` version

2. modify `pygeoc/vector.py` line 24 from `import gdal` to `from osgeo import gdal` (if your gdal-python version >=3.1). 

3. reinstall PyGeoC via

   ```shell
   pip uninstall PyGeoC
   cd PyGeoC
   # For *nix OS
   sudo ./reinstall.sh
   # For Windows, open a cmd windows as Administrator
   ./reinstall.bat
   ```

### 1.3 Configure SEIMS to support SAO2 to run on the Youwuzhen watershed

First, put the **spatial configuration unit GeoTIFF files** in `SEIMS\data\youwuzhen\data_prepare\spatial`: 

- `hydrologically_connected_fields.tif` required.
- `SLOPEPOSITION.tif` required.
- `slppos_units.tif`
- `spatial_nonunique_hrus.tif` required.
- `spatial_unique_hrus.tif`

Second, modify the `data/youwuzhen/model_configs/preprocess.ini` template, add a line in the `[SPATIAL]` section:

```ini
additionalFile = {"SLPPOS_UNITS" : "slppos_units.tif", "SPATIAL_NONUNIQUE_HRUS" : "spatial_nonunique_hrus.tif"}
```

Third, run the SEIMS preprocessing scipt, `python demo_preprocessing.py -name youwuzhen`.

Last, Make sure the **spatial configuration unit JSON files** are in `SEIMS\data\youwuzhen\demo_youwuzhen30m_longterm_model`:

- `connected_field_units_updown_15.json` : should have been generated after running SEIMS preprocessing (`python demo_preprocessing.py -name youwuzhen`), not to change the default setting of `field_partition_thresh = 15` in the `demo/youwuzhen30m_longterm_model/model_config/preprocess.ini`
- `hru_units.json` and `slppos_3cls_units_updown.json`: copy them from somewhere and paste them in (?). The hru_units.json corresponds to the spatial nonunique HRUs, while the "spatially unique HRU" and "explicit HRU" are the same meaning.

## 2. PostgreSQL Database

First, create a role named `sao` in your postgres DB. Set the password to `sao` also.

Second, create a databse named `sao`.

Run the SQL file `sao.sql` to construct the database.

## 3. Backend - sao-server

Revise the configuration files, `sao-server/src/main/resources/application.properties`.

Run the `sao-server` SpringBoot project.

## 4. Frontend - saoweb

NodeJS is required to install the `saoweb`. It's recommended to use `nvm` (node version management) to manage nodejs environments. On Windows, it's  [coreybutler/nvm-windows: A node.js version management utility for Windows. Ironically written in Go. (github.com)](https://github.com/coreybutler/nvm-windows). 

The project is developed and built on NodeJS v12.

In the sao-web folder, run

```shell
npm i
```

If it needs python2 for some packages, or there are node-sass errors, you can run

```shell
npm config set python \path\to\python27
npm i node-sass -g
npm i --force
```

Then

```shell
npm run dev
```

The SAO website [localhost:7099](localhost:7099) will be opened automatically.

# Build

## Backend

In `sao-server`, run `mvn install` , or find it in the Intellij IDEA mave plugin. Copy `scenario-analysis-online2/sao-server/target/saos-1.0.0.jar` to the server.

Copy the `sao-server/data` folder to the same directory with `saos-1.0.0-jar`.

to start it, run `start.sh`

```shell
#!/bin/bash
nohup java -jar saos-1.0.0.jar --server.port=7299 > saos.log  2>&1 &
```

To stop it, run `stop.sh`

```shell
#!/bin/bash
PID=$(ps -ef | grep saos-1.0.0.jar | grep -v grep | awk '{ print $2 }')
 
if [ -z "$PID" ]

then
echo Application is already stopped

else
echo kill $PID
kill $PID

fi
```

## Frontend

In front end, run `npm run build:prod`

copy the `dist` into `/home/sao/sao/saoweb`

```shell
sudo touch /etc/nginx/sites-available/saoweb
ln -s /etc/nginx/sites-available/saoweb /etc/nginx/sites-enabled/saoweb

sudo vim /etc/nginx/sites-available/saoweb
#/etc/nginx/sites-available/saoweb
server {
    listen      7099;
    # server_name example.com;
    # Always serve index.html for any request
    location / {
        root /home/sao/sao/saoweb/dist;
        index   index.html index.htm;
        try_files $uri /index.html;
    }
}

```

And reload restart nginx.

