//init map with 2 markers
function initialize(){
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer();


    var latlng = new google.maps.LatLng(47.523693,19.04068);
    var options = {
        zoom: 11,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("map_canvas"), options);
    directionsDisplay.setMap(map);

}

function calcRoute() {
  var start = document.getElementById("source").value;
  var end = document.getElementById("destination").value;
  var request = {
    origin:start,
    destination:end,
    travelMode: google.maps.TravelMode.DRIVING
  };
  directionsService.route(request, function(result, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      directionsDisplay.setDirections(result);
    }
  });
}

$(function(){
    initialize()
    calcRoute()
})