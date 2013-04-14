/**
 * Created with JetBrains WebStorm.
 * User: root
 * Date: 13. 4. 14
 * Time: 오후 8:45
 * To change this template use File | Settings | File Templates.
 */

var db = require('./../db_structure');

var rank = new db.rankinfo();

exports.into = function(req,res){
    res.render('rank',{title:"rank"});
};