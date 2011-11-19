//init map with 2 markers
function initialize(){
    var latlng = new google.maps.LatLng(47.523693,19.04068);
    var options = {
        zoom: 11,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("map_canvas"), options);

    //GEOCODER
    geocoder = new google.maps.Geocoder();

    var icon_red = new google.maps.MarkerImage("/static/img/marker_red.png",
            null, null, new google.maps.Point(16, 32));
    var icon_green = new google.maps.MarkerImage("/static/img/marker_green.png",
            null, null, new google.maps.Point(16, 32));

    marker_src = new google.maps.Marker({
        map: map,
        draggable: true,
        icon : icon_red
    });

    marker_dst = new google.maps.Marker({
        map: map,
        draggable: true,
        icon: icon_green
    });
}

function initial_data(){
    var src_lat = $("#id_src_lat").val();
    var src_lng = $("#id_src_lng").val();
    if (src_lat && src_lng){
        var location = new google.maps.LatLng(src_lat, src_lng);
        marker_src.setPosition(location);
    }
    
    var dst_lat = $("#id_dst_lat").val();
    var dst_lng = $("#id_dst_lng").val();
    if (dst_lat && dst_lng){
        var location = new google.maps.LatLng(dst_lat, dst_lng);
        marker_dst.setPosition(location);
    }

    set_viewport();
}

function set_viewport(){
    var current_bounds = map.getBounds();
    var marker_src_pos = marker_src.getPosition();
    var marker_dst_pos = marker_dst.getPosition();

    var bounds = new google.maps.LatLngBounds();

//    if( !current_bounds.contains( marker_src_pos ) ){
    if (marker_src_pos){
        bounds.extend(marker_src_pos);
//        var new_bounds = current_bounds.extend( marker_src_pos );
    }
    if (marker_dst_pos){
        bounds.extend(marker_dst_pos);
//        var new_bounds = current_bounds.extend( marker_dst_pos );
        map.fitBounds( bounds );
    }
//    }
}

$(function(){
    initialize()

    initial_data()

    $("#id_source").autocomplete({
        //This bit uses the geocoder to fetch address values
        source: function(request, response) {
            geocoder.geocode( {'address': request.term }, function(results, status) {
                response($.map(results, function(item) {
                    return {
                        label:  item.formatted_address,
                        value: item.formatted_address,
                        latitude: item.geometry.location.lat(),
                        longitude: item.geometry.location.lng()
                    }
                }));
            })
        },
        //This bit is executed upon selection of an address
        select: function(event, ui) {
            $("#id_src_lat").val(ui.item.latitude);
            $("#id_src_lng").val(ui.item.longitude);

            console.log(ui.item.latitude);
            console.log(ui.item.longitude);
            var location = new google.maps.LatLng(ui.item.latitude, ui.item.longitude);
            marker_src.setPosition(location);
            set_viewport();
        }
    });

    $("#id_destination").autocomplete({
        //This bit uses the geocoder to fetch address values
        source: function(request, response) {
            geocoder.geocode( {'address': request.term }, function(results, status) {
                response($.map(results, function(item) {
                    return {
                        label:  item.formatted_address,
                        value: item.formatted_address,
                        latitude: item.geometry.location.lat(),
                        longitude: item.geometry.location.lng()
                    }
                }));
            })
        },
        //This bit is executed upon selection of an address
        select: function(event, ui) {
            $("#id_dst_lat").val(ui.item.latitude);
            $("#id_dst_lng").val(ui.item.longitude);
            var location = new google.maps.LatLng(ui.item.latitude, ui.item.longitude);
            marker_dst.setPosition(location);
            set_viewport();
        }
    });

    //Add listener to marker for reverse geocoding
    google.maps.event.addListener(marker_src, 'drag', function() {
        geocoder.geocode({'latLng': marker_src.getPosition()}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    $('#id_source').val(results[0].formatted_address);
                    $('#id_src_lat').val(marker_src.getPosition().lat());
                    $('#id_src_lng').val(marker_src.getPosition().lng());
                }
            }
        });
    });

    google.maps.event.addListener(marker_dst, 'drag', function() {
        geocoder.geocode({'latLng': marker_dst.getPosition()}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    $('#id_destination').val(results[0].formatted_address);
                    $('#id_dst_lat').val(marker_dst.getPosition().lat());
                    $('#id_dst_lng').val(marker_dst.getPosition().lng());
                }
            }
        });
    });

})