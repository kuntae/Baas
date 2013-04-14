/**
 * Created with JetBrains WebStorm.
 * User: pc
 * Date: 13. 4. 9
 * Time: 오후 6:36
 * To change this template use File | Settings | File Templates.
 */
//=============================== database require =================================
var db = require('./../db_structure');
var login = new db.userinfo();

//==================================================================================
//first login page function
//==================================================================================
exports.into = function(req,res){
    res.render('login',{title:"login"});
};
//==================================================================================
//check insert id & password
//==================================================================================
exports.usercheck = function(req,res){

        db.userinfo.findOne({id:req.body.id},function(err,doc){ //findOne method
            try{
                if(doc.id == req.body.id){
                    if(doc.PWD == req.body.password){
                        res.redirect('/web/user_management');
                    }else{
                        res.redirect('/login');
                    }
                }
            }catch (err){ //catch err server down prevention
                res.redirect('/login');
            }

        });
};