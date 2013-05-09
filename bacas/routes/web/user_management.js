//=============================== database require =================================
var db = require('./../db_structure');
var login = new db.developerinfo();
var date = new Date();

exports.into = function(req,res){
    res.render('login',{title:"login"});
};
exports.user_page= function (req, res) {
    console.log("user page");
    var idArray =[];
    var mailArray=[];
    var countValue;
    db.userinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            idArray.push(doc[i].id);
            mailArray.push(doc[i].pwd);
        }

        res.render('user_page', {
            title: 'user page',
            idArray: idArray,
            mailArray:mailArray,
            countValue: countValue
        });
    });

}