/**
 * Created with JetBrains WebStorm.
 * User: pc
 * Date: 13. 4. 9
 * Time: 오후 6:36
 * To change this template use File | Settings | File Templates.
 */


var mongoose = require('mongoose');
var db = mongoose.connect('mongodb://localhost/mydb');

var Schema = mongoose.Schema;

var userSchema = new Schema({
    userid: String,
    PWD: String
});

var userinfo = mongoose.model('userinfo', userSchema);

var instance = new userinfo();

instance.userid = "test";
instance.PWD = "123";

var ID;
var pw;
exports.login = function(req,res){

    console.log("Non Save function state: "+instance.userid);
    instance.save(function(err){
        if(err){
            console.log("==========FAIL DB SAVE===========");
        }

    });

    userinfo.find({},function(err, info){
        if(err){
            console.log("==========FAIL DB FIND===========");
        }
        ID = info.userid;
        pw = info.PWD;
    });
    console.log("Save function state: "+ID);

    if(req.body.id == ID && req.body.password == pw){
    res.render('login'
        ,{ title: 'LOGIN'
            ,username: req.body.id
        });
    }
    else{
        res.render('login_F',{title:'LOGIN_F'});
    }
};

