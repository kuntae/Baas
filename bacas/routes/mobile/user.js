/**
 * Created with JetBrains WebStorm.
 * User: Jo
 * Date: 13. 5. 10
 * Time: 오후 6:00
 * 모바일에서 유저 정보를 조작하는 함수를 모아둔 파일
 */
var file_name = "user.js";                  // 이 파일의 이름은 rank.js
var db = require('./../db_structure');
var url_module = require('url');            // url 모듈을 불러온다.

// 유저정보를 받아오기
exports.get_user_info= function (req, res) {
    console.log(file_name +  "get user info page");

    db.expp.userinfo.find({},function(err,doc){
        console.log(file_name + ' ' +doc);

        res.render('mobile_get_data', {
            mobile_data: '[' + doc + ']'
        });
    });
}

// 유저 정보 devciceid를 저장
exports.user_regist_deviceid = function (req, res) {
    console.log(file_name + ' ' + req.url);

    var rank = new db.expp.userinfo();            // user 가상 객체 생성

    // url로 부터 parameter를 변수로 뽑는다.
    var query = url_module.parse(req.url, true).query;
    var id ;
    var pwd ;
    var mail = '-';
    var deviceid = query["deviceid"];
    var imeiid = query["imeiid"];
    var cnt = 0;
    console.log(file_name + " id : " + id + " pwd : " + pwd
        + " mail : " + mail + " deviceid : " + deviceid + "imeiid : " + imeiid);

    // 데이터가 없을 경우 에러처리
    db.expp.userinfo.find({},function(err,doc){
        try{
            while(doc[cnt]!=null){
                cnt++;
            }
            cnt+=1;
        }catch(err){
            console.log('deviceid regist error');
        }finally{
            console.log('find function close');
        }
    });

    // 검색
    db.expp.userinfo.findOne({imeiid:imeiid},function(err,doc){
        // 기존의 id가 없다면 저장
        if(doc == null) {
            console.log(file_name + " 새로운 id 저장");
            var newUser = new db.expp.userinfo();
            newUser.id = 'user'+cnt;
            newUser.pwd = 'user'+cnt;
            newUser.mail = mail;
            newUser.deviceid = deviceid;
            newUser.imeiid = imeiid;
            newUser.save(function(err, doc) {
                if(err != null ) {
                    console.log(file_name + ' save error ' + err)
                }
            });
        }
        // 기존의 id가 있다면 deviceid를 업데이트
        else {
            db.expp.userinfo.findOneAndUpdate({imeiid:imeiid},{deviceid:deviceid},function(err,doc2){
                try{
                    console.log(file_name + " deviceid 정보를 업데이트 하였습니다.")
                }catch(err){
                    console.log(file_name + " deviceid 업데이터 에러 " + err);
                }
            });
            console.log(file_name + ' ' + doc.id + ' ' + doc.pwd + ' ' + doc.mail + ' ' + doc.deviceid);
        }
    });
}

// 유저 정보 전체를 저장
exports.user_regist_all = function (req, res) {
    console.log(file_name + ' ' + req.url);

    var rank = new db.userinfo();            // user 가상 객체 생성

    // url로 부터 parameter를 변수로 뽑는다.
    var query = url_module.parse(req.url, true).query;
    var id = query["id"];
    var pwd = query["pwd"];
    var mail = query["mail"];
    var deviceid = query["deviceid"];

    console.log(file_name + " id : " + id + " pwd : " + pwd + " mail : " + mail + " deviceid : " + deviceid);

    db.userinfo.findOne({deviceid:deviceid},function(err,doc){
        // 기존의 deviceid가 없다면 저장
        if(doc == null) {
            console.log(file_name + " 새로운 id 저장");
            var newUser = new db.userinfo();
            newUser.id = id;
            newUser.pwd = pwd;
            newUser.mail = mail;
            newUser.deviceid = deviceid;

            newUser.save(function(err, doc) {
                if(err != null ) {
                    console.log(file_name + ' save error ' + err)
                }
            });
        }
        // 기존의 deviceid가 있다면 무시
        else {
            console.log(file_name + " 이미 정보가 저장되어 있습니다.")
            console.log(file_name + ' ' + doc.id + ' ' + doc.pwd + ' ' + doc.mail + ' ' + doc.deviceid);
        }
    });
}