/**
 * Created with JetBrains WebStorm.
 * User: pc
 * Date: 13. 4. 9
 * Time: 오후 6:36
 * To change this template use File | Settings | File Templates.
 */

var db = require('./../db_structure');

var login = new db.userinfo();
exports.into = function(req,res){
    res.render('login',{title:"login"});
};

exports.usercheck = function(req,res){

        db.userinfo.findOne({id:req.body.id},function(err,doc){
            try{
                if(doc.id == req.body.id){
                    res.redirect('/web/user_management');
                }
            }catch (err){
                res.redirect('/login');
            }

        });


};