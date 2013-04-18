exports.push_page= function (req, res) {
    console.log("push page");
    res.render('push_page', {
        title: 'push page'
    });
}

var regid; // regId 임시 저장 변수

exports.regist = function(req, res) { // Device ID 등록하기
    console.log(req.param('regId'));
    regid = req.param('regId'); // regId 가져오기
    res.end();
}

exports.send_push = function(req, res) {
    var gcm = require('node-gcm');
    var message = new gcm.Message();
    var sender = new gcm.Sender('AIzaSyDk9LE1o9omiCZjeePeUoVj6FowMI9OQmk'); // API Key
    var registrationIds = [];

    message.addData('message', '조승모문'); // Key, Value (보내고 싶은 메시지)
    message.collapseKey = 'demo';
    message.delayWhileIdle = true;
    message.timeToLive = 3;

    // Device ID Push
    registrationIds.push('APA91bE7V-vwVJ5gpNn-7hdiNIPhfTC9dNWL43DiLIPG9qheHbtTIS0M35UTSh3hkwb2D0szT4Ke2ORkuVZzfbjr_ieTic8MZaVC8WMeD4hbTuFTOmVdPOGZ2-5mAX3-MelRmS-MYHP-Ccp1Vn494I7o4_zU_ZqQ8Q');
    registrationIds.push(regid);

    // Push Notification Send
    sender.send(message, registrationIds, 4, function (err, result) {
        if(err) {
            //   console.log(err);
        }
        console.log(result);
    });

    /* sender.sendNoRetry(message, registrationIds, function (err, result) {
     console.log(result);
     }); // Retry 없이 보내기 */

}