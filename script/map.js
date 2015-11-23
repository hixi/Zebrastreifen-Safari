
var southWest = L.latLng(45.7300, 5.8000),
    northEast = L.latLng(47.9000, 10.600),
    bounds = L.latLngBounds(southWest, northEast);
var map = L.map('map',{
    maxBounds:bounds,
    invalidateSize: true
}).setView([46.8259, 8.2000], 9);

function unproject(latlng){
    var point = new L.Point(latlng.lng,latlng.lat);
    return L.Projection.SphericalMercator.unproject(point.divideBy(6378137));
}

map.on('moveend', function() {
    var zoom = L.map.getZoom();
    var bound = L.map.getBounds();
});
$(document).ready(function() {
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/crosswalks',
        dataType:'json',
        method: 'get',
        success:function(response){
            var icon = new L.icon({
                iconUrl:"libs/script/leaflet/images/crossing.png",
                iconSize:[40,40]
            });
            var crossing = L.geoJson(response, {

                // add the points to the layer
                pointToLayer: function (feature, latlng) {
                    var newlatlng = unproject(latlng);
                    console.log(latlng);
                    return L.marker(newlatlng, {
                        icon:icon
                    });
                }

            });
            crossing.addTo(map);
        }
    });
});
L.tileLayer('http://tile.osm.ch/osm-swiss-style/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy;<a href="http://openstreetmap.org">OpenStreetMap</a> contributors'
}).addTo(map);