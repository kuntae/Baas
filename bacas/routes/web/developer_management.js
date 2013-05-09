//=============================== database require =================================
var db = require('./../db_structure');
var login = new db.developerinfo();
var date = new Date();
//==================================================================================
//first login page function
//==================================================================================
exports.into = function(req,res){
    res.render('login',{title:"login"});
};
//==================================================================================
//check insert id & password
//==================================================================================
exports.developerCheck = function(req,res){
    db.developerinfo.findOne({id:req.body.id},function(err,doc){ //findOne method
        try{
            if(doc.id == req.body.id){
                if(doc.PWD == req.body.password){
                    res.writeHead(302,{'Location':'/web/user_management','Set-Cookie':['test = test',' Expries ='+ date.toUTCString()]});
                    res.end();
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
                    title:'비밀번호가 올바르지 않습니다.'
                });
            }else{
                var instance = new db.developerinfo();
                instance.id = req.body.id;
                instance.PWD = req.body.password;
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
exports.developer_page= function (req, res) {
    console.log("developer page");
    res.render('developer_page', {
        title: 'developer_page'
    });
}
exports.developer_page= function (req, res) {
    console.log("developer page");
    res.render('developer_page', {
        title: 'developer_page'
    });
}

