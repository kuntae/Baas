/**
 * Created with JetBrains WebStorm.
 * User: root
 * Date: 13. 4. 13
 * Time: 오후 11:53
 * To change this template use File | Settings | File Templates.
 */

var testdb = require('./db_structure');

var t_load = testdb.ex();
var id;
var pwd;

exports.testload = function(req,res){
      testdb.ex.find({},function(err,doc){
          id = doc[0].id;
          pwd = doc[0].PWD;
          console.log(id);
          console.log(pwd);

          res.render('test_load',{
              title:"LOAD" ,
              name:id,
              pass:pwd
      });


      });
};