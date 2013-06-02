var regid; // regId 임시 저장 변수
var r;
var db = require('./../db_structure');     // db_structure를 불러온다
var count;

// 로그인 체크 함수
function restrict(req, res, next) {
    if (req.session.user) {
        next();
    } else {
        req.session.error = 'Access denied!';
        res.redirect('/login');
    }
}

exports.push_page= function (req, res) {
    console.log("push page");

    // 로그인 체크
    restrict(req, res, function() {
        db.userinfo.find({}, function(err, doc) {
            var user_names = exports.user_names = [];

            try {
                var i = 0;
                while (doc[i] != null) {
                    user_names.push(doc[i].id);
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
    });
}

exports.regist = function(req, res) { // Device ID 등록하기
    var device = new db.userinfo();
    var cnt = 0;

    console.log(req.param('regId'));
    regid = req.param('regId'); // regId 가져오기

    db.userinfo.find({}, function(err, doc) {
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

    db.userinfo.findOne({deviceid:regid}, function(err, doc) {
        if(doc == null) {
            device.id = 'user' + cnt;
            device.pwd = 'user' + cnt;
            device.mail = 'user' + cnt;
            device.deviceid = regid;

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
            db.userinfo.findOne({id:req.body.to[i]}, function(err, doc) {
                try {
                    var registrationIds = [];
                    console.log(req.body.to[i]);
                    r = doc.deviceid;
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

    db.userinfo.findOne({id:req.body.to}, function(err, doc) {
        try {
            var registrationIds = [];
            console.log(req.body.to);
            r = doc.deviceid;
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
    res.redirect('/web/push_management');
}