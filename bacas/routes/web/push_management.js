var regid; // regId 임시 저장 변수
var r;
var db = require('./../db_structure');     // db_structure를 불러온다.

exports.push_page= function (req, res) {
    console.log("push page");
    res.render('push_page', {
        title: 'push page'
    });
}

exports.regist = function(req, res) { // Device ID 등록하기
    var device = new db.deviceinfo();

    console.log(req.param('regId'));
    regid = req.param('regId'); // regId 가져오기
    device.device_id = regid;

    device.save(function(err){
        try{
            res.render('push_management',{title:"push_management"});
        }catch (err){
            console.log('SAVE ERROR');
        }
    });
    res.end();
}

exports.send_push = function(req, res) {
    var gcm = require('node-gcm');
    var message = new gcm.Message();
    var sender = new gcm.Sender('AIzaSyDk9LE1o9omiCZjeePeUoVj6FowMI9OQmk'); // API Key
    var registrationIds = [];
   // var msg = req.body.message.toString();

   // message.addData('message', res.message); // Key, Value (보내고 싶은 메시지)
    message.addData('message', req.body.message);
    console.log(req.body.message);
    message.collapseKey = 'demo';
    message.delayWhileIdle = true;
    message.timeToLive = 3;

    // Device ID Push
    //registrationIds.push('APA91bE7V-vwVJ5gpNn-7hdiNIPhfTC9dNWL43DiLIPG9qheHbtTIS0M35UTSh3hkwb2D0szT4Ke2ORkuVZzfbjr_ieTic8MZaVC8WMeD4hbTuFTOmVdPOGZ2-5mAX3-MelRmS-MYHP-Ccp1Vn494I7o4_zU_ZqQ8Q');
    //registrationIds.push(regid);
    db.deviceinfo.findOne({name:req.body.name}, function(doc) {
        try {
            r = doc.device_id;
            console.log(r);
        } catch (err) {
            console.log('Oh, Shit.....');
        }
    });

    registrationIds.push(r);

    // Push Notification Send
    sender.send(message, registrationIds, 4, function (err, result) {
        if(err) {
               console.log(err);
        }
        console.log(result);
        res.redirect('/web/push_management');
    });

    /* sender.sendNoRetry(message, registrationIds, function (err, result) {
     console.log(result);
     }); // Retry 없이 보내기 */
}