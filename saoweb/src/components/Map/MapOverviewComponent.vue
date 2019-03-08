<template>
  <div>
    <div id="map"/>
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

export default {
  name: 'MapOverview0',
  props: {
    dataUrl: {
      type: String,
      default: '/data/field.geojson'
    },
    interactive: {
      type: Boolean,
      default: false
    },
    emitFeature: {
      type: Boolean,
      default: false
    }
  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {
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

      var style = new Style({
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
      })

      var vectorLayer = new VectorLayer({
        source: new VectorSource({
          url: this.dataUrl,
          // url: this.props.dataUrl,
          format: new GeoJSON()
        }),
        style: function(feature) {
          style.getText().setText(feature.get('name'))
          return style
        }
      })
      map.addLayer(vectorLayer)

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

        var that = this
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
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  #map {
    width: 100%;
    height: 100%;
  }
</style>
