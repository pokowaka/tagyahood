var BASE_URL='http://localhost:8080';


var map = L.map('map') .setView([37.7756, -122.4193], 15).addLayer(
  L.mapbox.tileLayer('examples.map-20v6611k', { detectRetina: true }));
var featureLayer = L.mapbox.featureLayer().addTo(map);

// Layers for the polygons.
var polygons = [];

var myLocation = L.circle([0, 0], 0).addTo(map);
var user = [];
var selectedMarkers = [];

// Hide the cut & connect button.
$('#cut').hide();
$('#link').hide();

// Load in the user info
// TODO, this probably should authenticate the user and log you in :-)
$.getJSON(BASE_URL + '/v1/user/rwin1', function(data) {
  console.log(data);
  user = data;
});

// 
// MAP EVENTS
//


// Clear the tooltip when map is clicked
map.on('move click',function(e){
  $('#info').hide();
  if (featureLayer.closePopup)
    featureLayer.closePopup();
});

map.on('dragend zoomend',function(e){
  getMarkers(map.getBounds()); 
});


//
// LOCATION RELATED FUNCTIONS
//
// TODO(ErwinJ): These should be in a class or something.
//

// Once we've got a position, zoom and center the map
// on it, and add a single marker.
map.on('locationfound', function(e) {
  map.panTo(e.latlng);
  var radius = e.accuracy / 2;
  myLocation.setRadius(radius);
  myLocation.setLatLng(e.latlng);
});

function onLocationError(e) {
  alert(e.message);
}

$('a[href="#locate"]').click(function(){
  map.locate();
}); 

map.on('locationerror', onLocationError);


$(".drag").draggable({
  helper: 'clone',
  containment: 'map',
  start: function(evt, ui) {
    $('#art').fadeTo('fast', 0.6, function() {});
  },
  stop: function(evt, ui) {
    $('#art').fadeTo('fast', 1.0, function() {});

    // INSERT Point
    var loc = map.containerPointToLatLng([ui.offset.left, ui.offset.top]);
    var type = ui.helper.attr('type');
    var typeid = user.tag.id;
    place(loc, typeid);
  }
});

var prev;
var cutPoints = [];
$(".cutdrag").draggable({
  helper: 'clone',
  containment: 'map',
  start: function(evt, ui) {
    $('#art').fadeTo('fast', 0.6, function() {});
  },
  drag: function(evt, ui) {
    var loc = map.containerPointToLatLng([ui.offset.left, ui.offset.top]);
    if (prev) {
      for(var i = 0; i < polygons.length; i++) {
        var pil = leafletPip.lineIntersects(loc, prev, polygons[i]);
        for(var j = 0; j < cutPoints.length; j++) {
          map.removeLayer(cutPoints[j]);
        }
        cutPoints = [];
        if (pil.length != 0) {
          pil = pil[0].between;
          var st = L.circleMarker(pil[0]); 
          var nd = L.circleMarker(pil[2]);
          cutPoints.push(st);
          cutPoints.push(nd);
          st.addTo(map);
          nd.addTo(map);
        }
      }
    }
    prev = loc;
  },
  stop: function(evt, ui) {
    $('#art').fadeTo('fast', 1.0, function() {});
    // Now cut the cutpoints..
  }
});


// Contains the highlighted circles / markers..
var circles = [];
var markers = [];

function getPointsOnPolygon(polyId) {
  $.getJSON(BASE_URL + '/v1/polygon/' + polyId, function(poly) {
    for(var i = 0; i < circles.length; i++) {
      map.removeLayer(circles[i]);
    } 

    for(var i = 0; i < markers.length; i++) {
      map.removeLayer(markers[i]);
    } 
    circles = [];
    markers = [];
    for(var i = 0; i < poly.polygon.length; i++) {
      var point = poly.polygon[i];
      var coord = L.latLng(point.lat, point.lon);
      var hexColor = (point.owner.crew.color & 0xFFFFFF).toString(16); 
      while(hexColor.length < 6) hexColor = '0' + hexColor;
      hexColor = '#' + hexColor;

      var circle = L.circle(coord, point.radius, { color : hexColor});
      //point.owner.color});
      circles.push(circle);
      circle.addTo(map);
      var  marker = L.marker(coord, { icon: L.mapbox.marker.icon({ 'marker-color': hexColor }), });
      marker['color'] = hexColor;

      marker.on('click', function(e) {
        clickedOnMarker(this);
      });
      markers.push(marker);
      marker.addTo(map);
    }
  });
}

function unselectMarker(marker) {
  marker.setIcon(L.mapbox.marker.icon({'marker-color' : marker.color}));
  $('#info').hide();
  var idx = selectedMarkers.indexOf(marker);
  selectedMarkers.splice(idx, 1);
  delete marker.selected;
  $('#cut').hide();
  $('#link').hide();
}

function clickedOnMarker(marker) {
  if (marker.selected) {
    unselectMarker(marker);
  } else {
    if (selectedMarkers.length == 2) {
      unselectMarker(selectedMarkers[0]);
    }
    marker['selected'] = true; // Not really the way to do it...
    marker.setIcon(L.mapbox.marker.icon({'marker-symbol' : 'marker-stroked', 'marker-color' : marker.color}));
    selectedMarkers.push(marker);
    if (selectedMarkers.length == 2) {
      $('#cut').show();
      $('#link').show();
    }
  }

}


function getMarkers(bounds) {
  $.getJSON(BASE_URL + '/v1/polygon/.geojson?lone=' + bounds.getEast() + '&lonw=' + bounds.getWest() + '&latn=' + bounds.getNorth() + '&lats=' + bounds.getSouth(),
            function(geojson) { 
              // featureLayer.setGeoJSON(geojson);
              // First clean out the polygons..
              for(var i = 0; i < polygons.length; i++) {
                map.removeLayer(polygons[i]);
              } 
              polygons = [];
              var features = geojson.features;
              for(var i = 0; i < features.length; i++) {
                var feature = features[i];
                var poly = L.geoJson(feature, { 
                  style: { color : feature.properties.fillColor },
                  pointToLayer: 
                    function (feature, latlng) {
                      var marker = L.marker(latlng,
                                          { icon: L.mapbox.marker.icon({
                                            'marker-color' : feature.properties['marker-color']
                                          }),
                                        });
                      marker['color'] = feature.properties['marker-color'];
                      return marker;
                  } 
                });
                polygons.push(poly);

                poly.on('click', function(e) {
                  if (e.layer instanceof L.Marker) {
                    clickedOnMarker(e.layer);
                  } else {
                    var feature = e.layer.feature;
                    var info = '<h2>' + feature.properties.crew+ '</h2>';
                    $('#info').html(info);
                    $('#info').show();
                    getPointsOnPolygon(feature.properties.id);
                  }
                });
                poly.addTo(map);
              }
            });
}

function place(location, typeid) {
  $.post(BASE_URL + '/v1/marker?lat=' + location.lat + '&lon=' + location.lng + '&owner=' + user.name + '&id=' + typeid,
         function(data) {
           $('#info').html(data);
         });
}
