var db = require('./../db_structure');     // db_structure를 불러온다

exports.location_page = function(req, res) {
    db.poiinfo.find({}, function(err, doc) {
        var lat = []; // 위도
        var lng = []; // 경도
        var address = [];

        try {
            var i = 0;
            while (doc[i] != null) {
                lat.push(doc[i].lat);
                lng.push(doc[i].lng);
                address.push(doc[i].address);
                i++;
            }

            for (var i = 0; i < lat.length; i++) {
                lat[i] = '\'' + lat[i] + '\'';
                lng[i] = '\'' + lng[i] + '\'';
                address[i] = '\'' + address[i] + '\'';
            }

            res.render('location_page', {
                title: 'location page',
                lat: lat,
                lng: lng,
                address: address
            });
        }
        catch (err) {
            console.log(err);
        }
    });
}

exports.addpoi= function(req, res) {
    var instance = new db.poiinfo();

    instance.lat = req.body.lat;
    instance.lng = req.body.lng;
    instance.address = req.body.address;

    instance.save(function(err) {
        try {
            res.redirect('/web/location_management');
        }
        catch (err) {
            console.log(err);
        }
    });
}

exports.removepoi= function(req, res) {
    console.log(req.body.remove);

    if (req.body.remove.length > 1) {
        for (var i = 0; i < req.body.remove.length; i++) {
            db.poiinfo.remove({lat : req.body.remove[i]}, function(err) {
                try {
                    res.redirect('/web/location_management');
                }
                catch (err) {
                    console.log(err);
                }
            });
        }
    }

    db.poiinfo.remove({lat : req.body.remove}, function(err) {
        try {
            res.redirect('/web/location_management');
        }
        catch (err) {
            console.log(err);
        }
    });
}