/**
 * Created with JetBrains WebStorm.
 * User: pc
 * Date: 13. 4. 12
 * Time: 오후 3:04
 * To change this template use File | Settings | File Templates.
 */

//mongodb를 사용하기 위한 mongoose의 모듈화를 한 JavaScript

var mongoose = require('mongoose');
var url = 'mongodb://localhost/testdb';
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
var userinfo = new Schema({
    id : String,
    PWD : String
});

var userinfo = exports.userinfo = mongoose.model('userinfo',userinfo);




exports.database = function(req,res){
    res.redirect('/login');
};