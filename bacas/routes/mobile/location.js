/**
 * Created with JetBrains WebStorm.
 * User: Jo
 * Date: 13. 5. 14
 * Time: 오후 6:29
 * 모바일에서 지역 정보를 조작하는 함수를 모아둔 파일
 */

var file_name = "location.js";                  // 이 파일의 이름은 rank.js
var db = require('./../db_structure');
var url_module = require('url');            // url 모듈을 불러온다.

// 정보를 받아오기
exports.get_location= function (req, res) {
    console.log(file_name +  "get location page");

    db.poiinfo.find({},function(err,doc){
        console.log(file_name + ' ' +doc);

        res.render('mobile_get_data', {
            mobile_data: '[' + doc + ']'
        });
    });
}