/**
 * Created with JetBrains WebStorm.
 * User: pc
 * Date: 13. 4. 12
 * Time: 오후 3:04
 * To change this template use File | Settings | File Templates.
 */

//mongodb를 사용하기 위한 mongoose의 모듈화를 한 JavaScript
var mongoose = exports.mongoose = require('mongoose');
var url = 'mongodb://localhost:27017/testdb';
var db;

db = mongoose.connect(url,function(err,done){
    if(err){
        console.log('DB connection ERROR...............');
    }
    else{
        console.log('DB connection Successful!!!!!!');
    }

});


var Schema = mongoose.Schema;
//==================================================================================
//userinfo schema
//==================================================================================
var userinfo = new Schema({
    id : String,
    PWD : String
});
//==================================================================================
//rnak schema
//==================================================================================
var rankinfo = new Schema({
    used_function: String,
    date: Date,
    location: String,
    user_id: String
});

var deviceinfo = new Schema({
    device_id: String,
    name: String
});

//model export!!!
var userinfo = exports.userinfo = mongoose.model('userinfo',userinfo);
var rankinfo = exports.rankinfo = mongoose.model('rankinfo',rankinfo);
var deviceinfo = exports.deviceinfo = mongoose.model('deviceinfo',deviceinfo);

exports.database = function(req,res){
    res.redirect('/login');
};