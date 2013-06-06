var db = require('./../db_structure');     // db_structure를 불러온다

exports.location_page = function(req, res) {
    db.expp.poiinfo.find({}, function(err, doc) {
        var lat = []; // 위도
        var lng = []; // 경도
        var address = []; // 주소
        var store = [];
        var phonenumber = [];
        var memo = [];

        try {
            var i = 0;
            while (doc[i] != null) {
                lat.push(doc[i].lat);
                lng.push(doc[i].lng);
                address.push(doc[i].address);
                store.push(doc[i].store);
                phonenumber.push(doc[i].phonenumber);
                memo.push(doc[i].memo);
                i++;
            }

            for (var i = 0; i < lat.length; i++) {
                lat[i] = '\'' + lat[i] + '\'';
                lng[i] = '\'' + lng[i] + '\'';
                address[i] = '\'' + address[i] + '\'';
                store[i] = '\'' + store[i] + '\'';
                phonenumber[i] = '\'' + phonenumber[i] + '\'';
                memo[i] = '\'' + memo[i] + '\'';
            }

            res.render('location_page', {
                title: 'Geographical Information (POI)',
                lat: lat,
                lng: lng,
                address: address,
                store: store,
                phonenumber: phonenumber,
                memo: memo
            });
        }
        catch (err) {
            console.log(err);
        }
    });
}

exports.addpoi= function(req, res) {
    var instance = new db.expp.poiinfo();

    if (req.body.lat == '')
        res.redirect('/web/location_management');

    instance.lat = req.body.lat;
    instance.lng = req.body.lng;
    instance.address = req.body.address;
    instance.store = req.body.store;
    instance.phonenumber = req.body.phonenumber;
    instance.memo = req.body.memo;

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

    if (req.body.remove == null)
        res.redirect('/web/location_management');

    else if (req.body.remove.length > 1) {
                for (var i = 0; i < req.body.remove.length; i++) {
                    db.expp.poiinfo.remove({lat : req.body.remove[i]}, function(err) {
                        try { }
                        catch (err) {
                            console.log('first : '+err);
                        }
                    });
        }
    }

    db.expp.poiinfo.remove({lat : req.body.remove}, function(err) {
        try { }
        catch (err) {
            console.log('second   :  '+err);
        }
    });
    res.redirect('/web/location_management');
}