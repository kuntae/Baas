/**
 * Created with JetBrains WebStorm.
 * User: root
 * Date: 13. 5. 9
 * Time: 오후 5:34
 * To change this template use File | Settings | File Templates.
 */

var db = require('./db_structure');
var developer = new db.developerinfo();
var user = new db.userinfo();

exports.developerSave = function(req,res){
        developer.id = "test1";
        developer.pwd = "12345";
        developer.mail = "test1@test.com";
        developer.save(function(err){
            try{
                console.log('developer saved');
                res.redirect('/login');
            }catch (err){
                console.log('SAVE ERROR');
            }
        });
}

exports.userSave = function(req,res){
        user.id = "test2";
        user.pwd = "12345";
        user.mail = "test2@test.com";
        user.deviceid = "000000000";
        user.save(function(err){
            try{
                console.log('user saved');
                res.redirect('/login');
            }catch (err){
                console.log('SAVE ERROR');
            }
        });
}