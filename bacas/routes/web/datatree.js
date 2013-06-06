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
    var imeiArray=[];
    var countValue;
    db.expp.userinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            idArray.push('\''+doc[i].id+'\'');
            mailArray.push('\''+doc[i].mail+'\'');
            deviceArray.push('\''+doc[i].deviceid+'\'');
            imeiArray.push('\''+doc[i].imeiid+'\'');

        }

        res.render('datatree_page_user', {
            title: 'Data Tree (User Collection)',
            idArray: idArray,
            mailArray:mailArray,
            deviceArray:deviceArray,
            imeiArray:imeiArray,
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
            title: 'Data Tree (Rank Collection)',
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
    var storeArray=[];
    var pnumArray=[];
    var memoArray=[];
    var countValue;
    db.expp.poiinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            latArray.push('\''+doc[i].lat+'\'');
            lngArray.push('\''+doc[i].lng+'\'');
            addressArray.push('\''+doc[i].address+'\'');
            storeArray.push('\''+doc[i].store+'\'');
            pnumArray.push('\''+doc[i].phonenumber+'\'');
            memoArray.push('\''+doc[i].memo+'\'');
        }

        res.render('datatree_page_location', {
            title: 'Data Tree (Location Collection)',
            latArray: latArray,
            lngArray:lngArray,
            addressArray:addressArray,
            storeArray:storeArray,
            pnumArray:pnumArray,
            memoArray:memoArray,
            countValue: countValue
        });
    });
}
exports.datatree_push=function(req,res){
    console.log("data tree of push");
    var dateArray =[];
    var msgArray=[];
    var useridArray=[];
    var countValue;
    db.expp.pushinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            dateArray.push('\''+doc[i].date+'\'');
            msgArray.push('\''+doc[i].message+'\'');
            useridArray.push('\''+doc[i].userid+'\'');
        }

        console.log(dateArray);

        res.render('datatree_page_push', {
            title: 'Data Tree (Push Collection)',
            dateArray: dateArray,
            msgArray:msgArray,
            useridArray:useridArray,
            countValue: countValue
        });
    });
}
exports.datatree_page= function (req, res) {
    console.log("data tree");

    // 로그인 체크
    restrict(req, res, function() {
        res.render('datatree_page', {
            title: 'Data Tree'
        });
    });
}