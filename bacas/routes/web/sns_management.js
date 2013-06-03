//=============================== database require =================================
var db = require('./../db_structure');

var file_name = "sns_management.js";                  // 이 파일의 이름은 sns_management.js
var login = new db.log.developerinfo();

// 로그인 체크 함수
function restrict(req, res, next) {
    if (req.session.user) {
        next();
    } else {
        req.session.error = 'Access denied!';
        res.redirect('/login');
    }
}

exports.sns_page = function(req, res) {
    console.log(file_name +  " get sns page");
    restrict(req, res, function() {
        res.render('sns_page',{title:"SNS에 자랑하기"});
    });
}