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

var Schema = mongoose.Schema;
var test = new Schema({
    name : String,
    age : String
});

var ex = exports.ex = mongoose.model('ex',test);

db = mongoose.connect(url,function(err,done){
    if(err){
        console.log('DB connection ERROR...............');
    }
    else{
        console.log('DB connection Successful!!!!!!');
    }

});


exports.save = function(req,res){

    res.render('db_test',{title:"Mongoose"});
};