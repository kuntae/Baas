var regid; // regId 임시 저장 변수
var r;
var db = require('./../db_structure');     // db_structure를 불러온다
var count;

var getdate = function(i){
    // var settingDate = new Date();
    //settingDate.setDate(settingDate.getDate()-6+i); //i일 전

    var settingDate =new Date();
    settingDate.setDate(settingDate.getDate()-6+i);
    var y = settingDate.getFullYear();
    var m = settingDate.getMonth() + 1;
    var d = settingDate.getDate();
    var h = settingDate.getHours();
    var mi = settingDate.getMinutes();
    var s = settingDate.getSeconds();
    var resultDate = y + '-' + m + '-'+ d+ '-'+ h+ '-'+ mi+ '-'+ s;
    return resultDate;
}

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
        db.expp.userinfo.find({}, function(err, doc) {
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
    var device = new db.expp.userinfo();
    var cnt = 0;

    console.log(req.param('regId'));
    regid = req.param('regId'); // regId 가져오기

    db.expp.userinfo.find({}, function(err, doc) {
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

    db.expp.userinfo.findOne({deviceid:regid}, function(err, doc) {
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
    var sender = new gcm.Sender('AIzaSyBptZgxytckKtDPAX8nVnKEvORISa7SR9s'); // API Key

    // message.addData('message', res.message); // Key, Value (보내고 싶은 메시지)
    message.addData('message', req.body.message);
    message.collapseKey = 'demo';
    message.delayWhileIdle = true;
    message.timeToLive = 3;

    // Device ID Push
    if (req.body.to.length > 1) {
        for (var i = 0; i < req.body.to.length; i++) {
            db.expp.userinfo.findOne({id:req.body.to[i]}, function(err, doc) {
                try {
                    var registrationIds = [];
                    console.log(req.body.to[i]);
                    r = doc.deviceid;
                    registrationIds.push(r);

                    sender.send(message, registrationIds, 4, function (err, result) {
                        if(err) {
                            console.log(err);
                        }else{
                            console.log(result);
                            var infos = new db.expp.pushinfo();
                            var cnt = 0;
                            db.expp.userinfo.findOne({deviceid:registrationIds[cnt]},function(err,doc){
                                var tmp = getdate(6);
                                infos.date = tmp;
                                infos.message = req.body.message;
                                infos.userid = doc.id;

                                infos.save(function(err){
                                    try{
                                        console.log('save');
                                        cnt++;
                                    }catch(err){
                                        console.log(err);
                                    }
                                });
                            });
                        }


                    });
                    console.log(r);
                } catch (err) {
                    console.log(err);
                }
            });
        }
    }

    db.expp.userinfo.findOne({id:req.body.to}, function(err, doc) {
        try {
            var registrationIds = [];
            console.log(req.body.to);
            r = doc.deviceid;
            registrationIds.push(r);

            sender.send(message, registrationIds, 4, function (err, result) {
                if(err) {
                    console.log(err);
                }else{
                    console.log(result);
                    db.expp.userinfo.findOne({deviceid:registrationIds[0]},function(err,docs){
                        var infos = new db.expp.pushinfo();
                        var tmp = getdate(6);
                        infos.date = tmp;                            3
                        infos.message = req.body.message;
                        infos.userid = docs.id;

                        infos.save(function(err){
                            try{
                                console.log('save');
                            }catch(err){
                                console.log(err);
                            }
                        });
                    });
                }
            });
            console.log(r);
        } catch (err) {
            console.log(err);
        }
    });
    res.redirect('/web/push_management');
}