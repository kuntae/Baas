var db = require('./../db_structure');

// 로그인 체크 함수
function restrict(req, res, next) {
    if (req.session.user) {
        next();
    } else {
        req.session.error = 'Access denied!';
        res.redirect('/login');
    }
}

exports.datatree_user=function(req,res){
    console.log("data tree of user");
    var idArray =[];
    var mailArray=[];
    var deviceArray=[];
    var countValue;
    db.expp.userinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            idArray.push('\''+doc[i].id+'\'');
            mailArray.push('\''+doc[i].mail+'\'');
            deviceArray.push('\''+doc[i].deviceid+'\'');
        }

        res.render('datatree_page_user', {
            title: 'datatree page',
            idArray: idArray,
            mailArray:mailArray,
            deviceArray:deviceArray,
            countValue: countValue
        });
    });
}
exports.datatree_rank=function(req,res){
    console.log("data tree of rank");
    var usedfunction =[];
    var date=[];
    var user_id=[];
    var countValue;
    db.expp.rankinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            usedfunction.push('\''+doc[i].used_function+'\'');
            date.push('\''+doc[i].date+'\'');
            user_id.push('\''+doc[i].user_id+'\'');
        }

        res.render('datatree_page_rank', {
            title: 'datatree page',
            usedfunction: usedfunction,
            date:date,
            user_id:user_id,
            countValue: countValue
        });
    });
}
exports.datatree_location=function(req,res){
    var latArray =[];
    var lngArray=[];
    var addressArray=[];
    var countValue;
    db.expp.poiinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            latArray.push('\''+doc[i].lat+'\'');
            lngArray.push('\''+doc[i].lng+'\'');
            addressArray.push('\''+doc[i].address+'\'');
        }

        res.render('datatree_page_location', {
            title: 'datatree page',
            latArray: latArray,
            lngArray:lngArray,
            addressArray:addressArray,
            countValue: countValue
        });
    });
}
exports.datatree_push=function(req,res){
    console.log("data tree of push");
    var idArray =[];
    var mailArray=[];
    var deviceArray=[];
    var countValue;
    db.expp.userinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            idArray.push('\''+doc[i].id+'\'');
            mailArray.push('\''+doc[i].mail+'\'');
            deviceArray.push('\''+doc[i].deviceid+'\'');
        }

        res.render('datatree_page_push', {
            title: 'datatree page',
            idArray: idArray,
            mailArray:mailArray,
            deviceArray:deviceArray,
            countValue: countValue
        });
    });
}
exports.datatree_page= function (req, res) {
    console.log("data tree");

    // 로그인 체크
    restrict(req, res, function() {
        res.render('datatree_page', {
            title: 'datatree page'
        });
    });
}