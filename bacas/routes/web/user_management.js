//=============================== database require =================================
var db = require('./../db_structure');
var login = new db.expp.userinfo();
var date = new Date();

// 로그인 체크 함수
function restrict(req, res, next) {
    if (req.session.user) {
        next();
    } else {
        req.session.error = 'Access denied!';
        res.redirect('/login');
    }
}

exports.into = function(req,res){
    res.render('login',{title:"login"});
};
exports.user_page= function (req, res) {
    console.log("user page");
    var idArray =[];
    var mailArray=[];
    var deviceArray=[];
    var countValue;

    // 로그인 체크
    restrict(req, res, function() {
        db.expp.userinfo.find({},function(err,doc){
            countValue = doc.length;
            for(var i=0;i<doc.length;i++){
                idArray.push('\''+doc[i].id+'\'');
                mailArray.push('\''+doc[i].mail+'\'');
                deviceArray.push('\''+doc[i].deviceid+'\'');
            }

            res.render('user_page', {
                title: 'user page',
                idArray: idArray,
                mailArray:mailArray,
                deviceArray:deviceArray,
                countValue: countValue
            });
        });
    });
}