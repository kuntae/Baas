var regid; // regId 임시 저장 변수
var r;
var db = require('./../db_structure');     // db_structure를 불러온다
var count;

exports.push_page= function (req, res) {
    db.deviceinfo.find({}, function(err, doc) {
        var user_names = exports.user_names = [];

        try {
            var i = 0;
            while (doc[i] != null) {
                user_names.push(doc[i].name);
                i++;
            }

            for (var i = 0; i < user_names.length; i++) {
                user_names[i] = '\'' + user_names[i] + '\'';
            }

            res.render('push_page', {
                title: 'push page',
                user_names: user_names
            });
        }
        catch (err) {
            console.log(err);
        }
    });
}

exports.regist = function(req, res) { // Device ID 등록하기
    var device = new db.deviceinfo();
    var cnt = 0;

    console.log(req.param('regId'));
    regid = req.param('regId'); // regId 가져오기

    db.deviceinfo.find({}, function(err, doc) {
        try {
            var i = 0;
            while (doc[i] != null) {
                i++;
            }
            cnt = i + 1;
        }catch(err) {
            console.log(err);
        }
    });

    db.deviceinfo.findOne({device_id:regid}, function(err, doc) {
        if(doc == null) {
            device.name = 'new' + cnt;
            device.device_id = regid;

            device.save(function(err) {
                try{
                    res.render('push_management',{title:"push_management"});
                }catch (err){
                    console.log('SAVE ERROR');
                }
            });
        }
        else {
            console.log('Already Exist');
        }
    });
    res.end();
}

exports.send_push = function(req, res) {
    var gcm = require('node-gcm');
    var message = new gcm.Message();
    var sender = new gcm.Sender('AIzaSyDk9LE1o9omiCZjeePeUoVj6FowMI9OQmk'); // API Key

    // message.addData('message', res.message); // Key, Value (보내고 싶은 메시지)
    message.addData('message', req.body.message);
    message.collapseKey = 'demo';
    message.delayWhileIdle = true;
    message.timeToLive = 3;

    // Device ID Push
    if (req.body.to.length > 1) {
        for (var i = 0; i < req.body.to.length; i++) {
            db.deviceinfo.findOne({name:req.body.to[i]}, function(err, doc) {
                try {
                    var registrationIds = [];
                    console.log(req.body.to[i]);
                    r = doc.device_id;
                    registrationIds.push(r);

                    sender.send(message, registrationIds, 4, function (err, result) {
                        if(err) {
                            console.log(err);
                        }
                        console.log(result);

                    });
                    console.log(r);
                } catch (err) {
                    console.log(err);
                }
            });
        }
    }

    db.deviceinfo.findOne({name:req.body.to}, function(err, doc) {
        try {
            var registrationIds = [];
            console.log(req.body.to);
            r = doc.device_id;
            registrationIds.push(r);

            sender.send(message, registrationIds, 4, function (err, result) {
                if(err) {
                    console.log(err);
                }
                console.log(result);

            });
            console.log(r);                   n
        } catch (err) {
            console.log(err);
        }
    });
    res.redirect('/web/push_management');
}