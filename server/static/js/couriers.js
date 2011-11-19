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

    //GEOCODER
    geocoder = new google.maps.Geocoder();

    icon_red = new google.maps.MarkerImage("http://maps.google.com/mapfiles/marker.png",
            null, null, new google.maps.Point(16, 32));
    icon_purple = new google.maps.MarkerImage("http://maps.google.com/mapfiles/marker_purple.png",
            null, null, new google.maps.Point(16, 32));

    markers = {};
    active_marker=null;
}

function initial_data(){
    $('.courier_row:not(.inactive)').each(function(index, el){
        var lat = $(el).find('.position').html().split(',')[0]
        var lng = $(el).find('.position').html().split(',')[1]
        var username = $(el).find('.username').html()
        if (lat && lng){
            markers[username] = new google.maps.Marker({
                map: map,
                title: username,
                icon: icon_red
            });
            var location = new google.maps.LatLng(lat, lng);
            markers[username].setPosition(location);

        }

    });
//    set_viewport();
}

function set_viewport(){
    var current_bounds = map.getBounds();
    var marker_src_pos = marker_src.getPosition();
    var marker_dst_pos = marker_dst.getPosition();

    var bounds = new google.maps.LatLngBounds();

    if (marker_src_pos){
        bounds.extend(marker_src_pos);
    }
    if (marker_dst_pos){
        bounds.extend(marker_dst_pos);
        map.fitBounds( bounds );
    }
}

$(function(){
    initialize()

    initial_data()

    $(".courier_row:not(.inactive)").bind("mouseover", function(){
        if (active_marker){
            active_marker.setIcon(icon_red)
        }
        active_marker = markers[$(this).find('.username').html()]
        if (active_marker){
            active_marker.setIcon(icon_purple)
        }
    })
    $(".datagrid").bind("mouseleave", function(){
        if (active_marker){
            active_marker.setIcon(icon_red)
        }
        active_marker = null
    })
})