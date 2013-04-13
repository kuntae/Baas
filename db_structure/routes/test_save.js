/**
 * Created with JetBrains WebStorm.
 * User: root
 * Date: 13. 4. 13
 * Time: 오후 11:53
 * To change this template use File | Settings | File Templates.
 */

var testdb = require('./db_structure');

 var t_save = testdb.ex();

exports.testsave = function(req,res){
    t_save.id = req.body.ids;
    t_save.PWD = req.body.password;

    t_save.save(function(err){
        if(err){
            console.log('SAVE FAIL');
        }

        res.redirect('/test_load');
    });
};