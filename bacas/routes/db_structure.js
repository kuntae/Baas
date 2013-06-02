/**
 * Created with JetBrains WebStorm.
 * User: pc
 * Date: 13. 4. 12
 * Time: 오후 3:04
 * To change this template use File | Settings | File Templates.
 */

//mongodb를 사용하기 위한 mongoose의 모듈화를 한 JavaScript
var mongoose = require('mongoose');
var uri = "mongodb://localhost:27017/testdb";
var db;

db = mongoose.connect(uri,function(err,done){
    if(err){
        console.log('DB connection ERROR...............');
    }
    else{
        console.log('DB connection Successful!!!!!!');
    }

});

var Schema = mongoose.Schema;
//==================================================================================
//developerinfo schema
//==================================================================================
var developerinfo = new Schema({
    id : String,
    pwd : String,
    mail: String
});
//==================================================================================
//userinfo schema
//==================================================================================
var userinfo = new Schema({
    id : String,
    pwd : String,
    mail: String,
    deviceid: String
});
//==================================================================================
//rank schema
//==================================================================================
var rankinfo = new Schema({
    used_function: String,
    date: Date,
    user_id: String
});
//==================================================================================
//????????????????????????????????????????????????????
//==================================================================================

var poiinfo = new Schema({
    lat: String,
    lng: String,
    address: String,
    store: String,
    phonenumber: String,
    memo: String
});

//model export!!!
var developerinfo = exports.developerinfo = mongoose.model('developerinfo',developerinfo);
var userinfo = exports.userinfo = mongoose.model('userinfo',userinfo);
var rankinfo = exports.rankinfo = mongoose.model('rankinfo',rankinfo);
var poiinfo = exports.poiinfo = mongoose.model('poiinfo', poiinfo);

exports.database = function(req,res){
    res.redirect('/login');
};