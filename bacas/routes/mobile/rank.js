/**
 * Created with JetBrains WebStorm.
 * User: root
 * Date: 13. 4. 14
 * Time: 오후 8:45
 * 모바일의 유저 정보를 받아 DB의 rankinfos 컬래견에 저장하기 위한 js파일
 */

var file_name = "rank.js";                  // 이 파일의 이름은 rank.js
var db = require('./../db_structure');     // db_structure를 불러온다.
var url_module = require('url');            // url 모듈을 불러온다.

exports.into = function(req,res){
    console.log(req.url);

    var rank = new db.rankinfo();            // rank 가상 객체 생성

    // url로 부터 parameter를 변수로 뽑는다.
    var query = url_module.parse(req.url, true).query;
    var used_function = query["used_function"];
    var location = query["location"];
    var user_id = query["user_id"];
    var app_id = query["app_id"];

    console.log("used_function : " + used_function + "location : " + location
        + "user_id : " + user_id + "app_id : " + app_id);

//    var used_function = "used_function";
//    var location = "location";
//    var user_id = "user_id";
//    var app_id = "app_id";

    // rank에 변수를 집어 넣은 후 DB에 저장한다.
    rank.used_function = used_function;
    rank.location = location;
    rank.user_id = user_id;
    rank.app_id = app_id;
    rank.date = Date.now();
    ((Date.getYear()+1900)+(Date.getMonth()+1)+(Date.getDate())).toString(function(result){
        console.log(result);
    });
    console.log(rank.date.getDate());
    console.log(rank.date.getMonth()+1);
    console.log(rank.date.getYear()+1900);

    console.log();
    rank.save(function(err){
        try{
            res.render('rank',{title:"rank"});
        }catch (err){
            console.log('SAVE ERROR');
        }
    });
};