/**
 * Created with JetBrains WebStorm.
 * User: root
 * Date: 13. 6. 1
 * Time: 오후 12:02
 * To change this template use File | Settings | File Templates.
 */

var file_name = "push.js";                  // 이 파일의 이름은 rank.js
var db = require('./../db_structure');     // db_structure를 불러온다.
var url_module = require('url');            // url 모듈을 불러온다.

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