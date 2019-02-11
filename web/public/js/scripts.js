function slugify(str) {
    return (str || '')
        .trim()
        .toLowerCase()
        .replace(/[ _+'"]/g, '-')
        .replace(/--/g, '-')
        .normalize('NFD').replace(/[^a-z0-9-]/g, '');
}

// enable bootstrap features
(function () {
    $('[data-toggle="tooltip"]').tooltip();
})();

// build slug from an other field
(function () {
    $('input[slug-for]').each(function () {
        var slugInput = $(this);
        var srcInput = $('#' + slugInput.attr('slug-for'));
        srcInput.change(function () {
            var src = srcInput.val();
            var prevSrc = srcInput.data('prev');
            var oldSlug = slugInput.val();
            if (oldSlug === '' || oldSlug === slugify(prevSrc)) {
                slugInput.val(slugify(src));
            }
            srcInput.data('prev', src);
        });
    });
})();

// http://www.malot.fr/bootstrap-datetimepicker/
(function () {
    $('input.input-datetime').each(function () {
        $(this).datetimepicker({
            language: 'en',
            autoclose: true,
            initialDate: $(this).attr('startDate')
        });
    });
})();

// https://uxsolutions.github.io/bootstrap-datepicker/
(function () {
    var defaultConfig = {
        format: 'dd/mm/yyyy',
        weekStart: 1,
        maxViewMode: 2,
        language: 'en',
        autoclose: true,
        todayHighlight: true,
        toggleActive: true
    };
    $('input.input-date').each(function () {
        $(this).datepicker(Object.assign({}, defaultConfig, {
            defaultViewDate: $(this).attr('startDate')
        }));
    });
})();

// GMapPlace picker (https://developers.google.com/maps/documentation/javascript/examples/places-autocomplete?hl=fr)
var GMapPlacePicker = (function () {
    function initMap($map) {
        var map = new google.maps.Map($map.get(0), {
            center: {lat: -33.8688, lng: 151.2195},
            zoom: 13
        });
        var marker = new google.maps.Marker({
            map: map,
            anchorPoint: new google.maps.Point(0, -29)
        });
        var infowindow = new google.maps.InfoWindow();
        return {
            $map: $map,
            map: map,
            marker: marker,
            infowindow: infowindow
        };
    }

    function toggleMap(mapData, location) {
        if (location && location.lat && location.lng) {
            showMap(mapData, location);
        } else {
            hideMap(mapData);
        }
    }

    function showMap(mapData, location) {
        var point = {lat: parseFloat(location.lat), lng: parseFloat(location.lng)};
        mapData.$map.show();
        google.maps.event.trigger(mapData.map, 'resize');
        mapData.infowindow.close();
        mapData.marker.setVisible(false);
        mapData.map.setCenter(point);
        mapData.map.setZoom(15);
        mapData.marker.setPosition(point);
        mapData.marker.setVisible(true);
        mapData.infowindow.setContent(
            '<strong>' + location.name + '</strong><br>' +
            location.streetNo + ' ' + location.street + '<br>' +
            location.postalCode + ' ' + location.locality + ', ' + location.country
        );
        mapData.infowindow.open(mapData.map, mapData.marker);
    }

    function hideMap(mapData) {
        mapData.$map.hide();
    }

    var fields = ['id', 'name', 'streetNo', 'street', 'postalCode', 'locality', 'country', 'formatted', 'lat', 'lng', 'url', 'website', 'phone', 'utcOffset'];

    function writeForm($elt, location) {
        fields.forEach(function (field) {
            $elt.find('input.gmapplace-' + field).val(location ? location[field] : '');
        });
    }

    function readForm($elt) {
        var location = {};
        fields.forEach(function (field) {
            location[field] = $elt.find('input.gmapplace-' + field).val();
        });
        return location;
    }

    function toLocation(place) {
        function getSafe(elt, field) {
            return elt && elt[field] ? elt[field] : '';
        }

        function toAddress(components) {
            function getByType(components, type) {
                var c = components.find(function (e) {
                    return e.types.indexOf(type) >= 0;
                });
                return c && c.long_name;
            }

            return {
                streetNumber: getByType(components, 'street_number'),
                route: getByType(components, 'route'),
                postalCode: getByType(components, 'postal_code'),
                locality: getByType(components, 'locality'),
                country: getByType(components, 'country'),
                administrativeArea: {
                    level1: getByType(components, 'administrative_area_level_1'),
                    level2: getByType(components, 'administrative_area_level_2'),
                    level3: getByType(components, 'administrative_area_level_3'),
                    level4: getByType(components, 'administrative_area_level_4'),
                    level5: getByType(components, 'administrative_area_level_5')
                },
                sublocality: {
                    level1: getByType(components, 'sublocality_level_1'),
                    level2: getByType(components, 'sublocality_level_2'),
                    level3: getByType(components, 'sublocality_level_3'),
                    level4: getByType(components, 'sublocality_level_4'),
                    level5: getByType(components, 'sublocality_level_5')
                }
            };
        }

        var address = toAddress(place.address_components);
        var loc = place && place.geometry && place.geometry.location;
        return {
            id: getSafe(place, 'place_id'),
            name: getSafe(place, 'name'),
            streetNo: getSafe(address, 'streetNumber'),
            street: getSafe(address, 'route'),
            postalCode: getSafe(address, 'postalCode'),
            locality: getSafe(address, 'locality'),
            country: getSafe(address, 'country'),
            formatted: getSafe(place, 'formatted_address'),
            lat: loc ? loc.lat().toString() : '',
            lng: loc ? loc.lng().toString() : '',
            url: getSafe(place, 'url'),
            website: getSafe(place, 'website'),
            phone: getSafe(place, 'international_phone_number'),
            utcOffset: getSafe(place, 'utc_offset').toString()
        };
    }

    function initAutocomplete($elt, $input, mapData) {
        var autocomplete = new google.maps.places.Autocomplete($input.get(0));
        autocomplete.addListener('place_changed', function () {
            var place = autocomplete.getPlace(); // cf https://developers.google.com/maps/documentation/javascript/reference/places-service?hl=fr#PlaceResult
            var location = toLocation(place);
            writeForm($elt, location);
            toggleMap(mapData, location);
        });
    }

    return {
        init: function () {
            $('.input-gmapplace').each(function () {
                var $elt = $(this);
                var $input = $elt.find('input[type="text"]');
                var $map = $elt.find('.map');
                var mapData = initMap($map);
                var location = readForm($elt);
                toggleMap(mapData, location);
                initAutocomplete($elt, $input, mapData);
                // prevent form submit on enter
                $input.on('keydown', function (e) {
                    if (e && e.keyCode === 13) {
                        e.preventDefault();
                    }
                });
                // clear form when input is cleared
                $input.on('change', function () {
                    if ($input.val() === '') {
                        var location = null;
                        writeForm($elt, location);
                        toggleMap(mapData, location);
                    }
                });
            });
        }
    };
})();

function googleMapsInit() {
    GMapPlacePicker.init();
}
