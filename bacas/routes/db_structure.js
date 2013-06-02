/**
 * Created with JetBrains WebStorm.
 * User: pc
 * Date: 13. 4. 12
 * Time: 오후 3:04
 * To change this template use File | Settings | File Templates.
 */

//mongodb를 사용하기 위한 mongoose의 모듈화를 한 JavaScript
var mongoose = exports.mongoose = require('mongoose');
var uri = "mongodb://localhost:27017/logdb";
var db;
var log = {};
db = mongoose.createConnection(uri,function(err){
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


log.developerinfo = db.model('developerinfo', developerinfo);

exports.log = log;

exports.database = function(req,res){
    res.redirect('/login');
};
var id;
var con =exports.con =  mongoose.createConnection();
var expp = {};
expp.userinfo = con.model('userinfo', userinfo);
expp.rankinfo = con.model('rankinfo', rankinfo);
expp.poiinfo = con.model('poiinfo', poiinfo);
exports.expp = expp;
exports.connecttoid = function(req,res){
    id = req.signedCookies.id;
    con.open('localhost', id,27017);
    res.redirect('/web/user_management');
};




