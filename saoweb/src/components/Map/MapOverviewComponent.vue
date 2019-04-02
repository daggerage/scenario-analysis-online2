<template>
  <div>
    <div id="map"/>

    <div v-if="gene">
      <div v-for="item of bmps" :key="item.id" class="legend-box">
        <div class="legend-color"/>
        <div class="legend-label">{{replaceBmpName(item.name)}}</div>
      </div>
    </div>
    </div>

</template>

<script>
import 'ol/ol.css'
import { Map, View } from 'ol'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import GeoJSON from 'ol/format/GeoJSON'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import { Fill, Stroke, Style, Text } from 'ol/style'
import {fetchStructBMPs,fetchPlantBMPs} from "@/api/bmp";
import {replaceBmpName} from '@/utils/bmpDisplay'


export default {
  name: 'MapOverview0',
  props: {
    dataUrl: {
      type: String,
      default: ''
    },
    interactive: {
      type: Boolean,
      default: false
    },
    emitFeature: {
      type: Boolean,
      default: false
    },
    gene: {
      type: Array,
      default: null
    }
  },
  data(){
    return {
      map:null,
      style:new Style({
        fill: new Fill({
          color: 'rgba(255, 255, 255, 0.6)'
        }),
        stroke: new Stroke({
          color: '#319FD3',
          width: 1
        }),
        text: new Text({
          font: '12px Calibri,sans-serif',
          fill: new Fill({
            color: '#000'
          }),
          stroke: new Stroke({
            color: '#fff',
            width: 3
          })
        })
      }),
      baseVectorLayer:null,
      bmpStylesMap:{},
      bmps:[],
      colors:[
        'rgba(255, 195, 18,1.0)',
        'rgba(196, 229, 56,1.0)',
        'rgba(18, 203, 196,1.0)',
        'rgba(253, 167, 223,1.0)',
        'rgba(237, 76, 103,1.0)',
        'rgba(234, 32, 39,1.0)',
        'rgba(0, 98, 102,1.0)',
        'rgba(27, 20, 100,1.0)',
        'rgba(87, 88, 187,1.0)',
        'rgba(111, 30, 81,1.0)'
      ]
    }
  },
  mounted() {
    this.initColoredFeatures()
    this.initMap()
  },
  methods: {
    addBaseLayer(){
      var that=this
      let vectorLayer = new VectorLayer({
        source: new VectorSource({
          url: this.dataUrl,
          format: new GeoJSON()
        }),
        style: function(feature) {
          if(that.gene[feature.values_['gridcode'] || feature.values_['SLPPOS_UNI']]){
            // console.log(that.gene[feature.values_['gridcode'] || feature.values_['SLPPOS_UNI']])
            console.log('style:'+that.bmpStylesMap[that.gene[feature.values_['gridcode'] || feature.values_['SLPPOS_UNI']]]);
            // return that.bmpStylesMap[parseInt(that.gene[feature.values_['gridcode']||feature.values_['SLPPOS_UNI']])]
            return that.bmpStylesMap[parseInt(that.gene[feature.values_['gridcode']||feature.values_['SLPPOS_UNI']])]

          }else {
            return that.style
          }
        }
      })
      let oldLayer = this.baseVectorLayer
      this.baseVectorLayer = vectorLayer
      if(oldLayer){
        this.map.removeLayer(oldLayer)
      }
      this.map.addLayer(vectorLayer)
    },
    initMap() {
      var that=this
      // 生成map
      const map = new Map({
        target: 'map',
        layers: [
          new TileLayer({
            source: new OSM()
          })
        ],
        view: new View({
          center: [12964346.628866788, 2959662.83938],
          zoom: 14
        })
      })
      this.map=map
      var vectorLayer = new VectorLayer({
        source: new VectorSource({
          url: this.dataUrl,
          format: new GeoJSON()
        }),
        style: function(feature) {
          return that.style
        }
      })
      this.baseVectorLayer = vectorLayer
      map.addLayer(vectorLayer)
      // map.addLayer(
      //   new TileLayer({
      //     visible: false,
      //     preload: Infinity,
      //     source: new BingMaps({
      //       key: 'AogMY_DAdDwIYuVA5d6EAHPVGzWEuskRZoPTWTL9Hyn01qbox5jUwoHwZ8RSeJlc',
      //       imagerySet: 'Road'
      //       // use maxZoom 19 to see stretched tiles instead of the BingMaps
      //       // "no photos at this zoom level" tiles
      //       // maxZoom: 19
      //     })
      //   })
      // )
      var componentProps = this
      if (componentProps.interactive) {
        var highlightHoverStyle = new Style({
          stroke: new Stroke({
            color: '#f00',
            width: 1
          }),
          fill: new Fill({
            color: 'rgba(255,0,0,0.1)'
          }),
          text: new Text({
            font: '12px Calibri,sans-serif',
            fill: new Fill({
              color: '#000'
            }),
            stroke: new Stroke({
              color: '#f00',
              width: 3
            })
          })
        })
        var highlightClickStyle = new Style({
          stroke: new Stroke({
            color: '#0af',
            width: 2
          }),
          fill: new Fill({
            color: 'rgba(0,0,0,0.1)'
          }),
          text: new Text({
            font: '12px Calibri,sans-serif',
            fill: new Fill({
              color: '#000'
            }),
            stroke: new Stroke({
              color: '#0af',
              width: 3
            })
          })
        })
        var featureHoverOverlay = new VectorLayer({
          source: new VectorSource(),
          map: map,
          style: function(feature) {
            // highlightStyle.getText().setText(feature.get('ObjectID'));
            return highlightHoverStyle
          }
        })
        var featureClickOverlay = new VectorLayer({
          source: new VectorSource(),
          map: map,
          style: function(feature) {
            return highlightClickStyle
          }
        })
        var getFeatureByPixel = function(pixel) {
          return map.forEachFeatureAtPixel(pixel, function(feature) {
            return feature
          })
        }

        class FeatureDisplay {
          constructor(highlight, featureOverlay) {
            this.highlight = highlight
            this.featureOverlay = featureOverlay
          }

          clearAndDisplay(feature) {
            if (feature !== this.highlight) {
              if (this.highlight) {
                this.featureOverlay.getSource().removeFeature(this.highlight)
              }
              if (feature) {
                this.featureOverlay.getSource().addFeature(feature)
              }
              this.highlight = feature
            }
          }
        }

        var emitFeature = function(feature) {
          that.$emit('displayFeature', feature)
        }
        var highlightHover
        var highlightClick
        var featureDisplayByHover = new FeatureDisplay(highlightHover, featureHoverOverlay)
        var featureDisplayByClick = new FeatureDisplay(highlightClick, featureClickOverlay)

        map.on('pointermove', function(evt) {
          if (evt.dragging) {
            return
          }
          var pixel = map.getEventPixel(evt.originalEvent)
          var feature = getFeatureByPixel(pixel)
          featureDisplayByHover.clearAndDisplay(feature)
        })
        map.on('click', function(evt) {
          var feature = getFeatureByPixel(evt.pixel)
          featureDisplayByClick.clearAndDisplay(feature)
          if (componentProps.emitFeature) {
            emitFeature(feature)
          }
        })
      }
    },
    initColoredFeatures(){
      var that=this
      var colors = 0
      var structBmpNum=0
      fetchStructBMPs().then(res=>{
        if(res.data.status===200){
          colors+=res.data.data.length
          structBmpNum+=colors
          for(let bmp of res.data.data){
            that.bmps.push({id:bmp['subscenario'],name:bmp['name']})
          }
          that.bmps.push({id:colors+1,name:'RICEPADDYCROPROTATION'})
          colors+=1
          that.bmpStylesMap[0]=that.style
          for (let i = 0; i < colors; i++) {
            that.bmpStylesMap[i + 1] = new Style({
              fill: new Fill({
                color: that.colors[i]
              })
            })
          }
        }
      })
    },
    replaceBmpName
  },
  watch: {
    geneAndUrl(newUrl,oldUrl){
      var that=this
      this.addBaseLayer()
      for (let i = 0; i <this.bmps.length; i++) {
        var x=document.getElementsByClassName('legend-color')[i]
        console.log(that.colors[i]);
        x.style.backgroundColor=that.colors[i]
      }

    },
    deep:true
  },
  computed:{
    geneAndUrl:function () {
      return [this.gene,this.dataUrl]
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  #map {
    width: 100%;
    height: 100%;
  }
  .legend-box{
    margin: 20px 20px 0 0;
    /*width:150px;*/
    height: 30px;
    position:relative;
    float:left;
  }
  .legend-color{
    width: 30px;
    height: 20px;
    border-radius:5px;
    float:left;
  }
  .legend-label{
    float:left;
    margin-left: 5px;
  }
</style>
