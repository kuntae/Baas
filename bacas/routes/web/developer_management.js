//=============================== database require =================================
var db = require('./../db_structure');
var login = new db.developerinfo();
var hash = require('./pass').hash
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

//==================================================================================
//first login page function
//==================================================================================
exports.into = function(req,res){
    res.render('login',{title:"login"});
};

exports.logout = function(req, res) {
    req.session.destroy(function() {
        res.redirect('/');
    });
}
//==================================================================================
//check insert id & password
//==================================================================================
exports.developerCheck = function(req,res){
    db.developerinfo.findOne({id:req.body.id},function(err,doc){ //findOne method
        try{
            if(doc.id == req.body.id){
                // 로그인 성공
                if(doc.pwd == req.body.password){
                    // 고정을 방지하기 위해 세션을 재생성한다.
                    req.session.regenerate(function() {
                        // 검색하기 위해 세션 저장소에 사용자의 기본키를 저장한다.
                        req.session.user = doc;
                        req.session.success = 'Authenticated as ' + doc.id
                            + ' click to <a href="/logout">logout</a>. ';
                        res.redirect('/web/user_management');
                    });
                }else{
                    console.log('else this position');
                    res.redirect('/login');
                }
            }
        }catch (err){ //catch err server down prevention
            res.redirect('/login');
        }

    });
};

exports.developersignup= function (req, res) {
    res.render('signup', {
        title: 'sign up page'
    });
}

exports.developersignupChk= function (req, res) {
    db.developerinfo.findOne({id:req.body.id},function(err,doc){ //findOne method
        try{
            if(doc.id == req.body.id){
                res.render('signup',{
                    title:'존재하는 아이디 입니다.'
                });
            }

        }catch (err){ //catch err server down prevention
            if(req.body.password.length <= 4){
                res.render('signup',{
                    title:'비밀번호가 올바르지 않습니다. 4글자 이상 입력하세요'
                });
            }else{
                var instance = new db.developerinfo();
                instance.id = req.body.id;
                instance.pwd = req.body.password;
                instance.save(function(err){
                    try{
                        res.redirect('/login');
                    }catch (err){
                        console.log('SAVE ERROR');
                    }
                });
            }
        }
    });
}

exports.developer_page= function (req, res) {
    console.log("developer page");

    // 로그인 체크
    restrict(req, res, function() {
        res.render('developer_page', {
            title: 'developer_page'
        });
    });
}

