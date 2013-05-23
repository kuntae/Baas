var db = require('./../db_structure');

exports.datatree_user=function(req,res){
    console.log("data tree of user");
    var idArray =[];
    var mailArray=[];
    var deviceArray=[];
    var countValue;
    db.userinfo.find({},function(err,doc){
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
    var idArray =[];
    var mailArray=[];
    var deviceArray=[];
    var countValue;
    db.userinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            idArray.push('\''+doc[i].id+'\'');
            mailArray.push('\''+doc[i].mail+'\'');
            deviceArray.push('\''+doc[i].deviceid+'\'');
        }

        res.render('datatree_page_rank', {
            title: 'datatree page',
            idArray: idArray,
            mailArray:mailArray,
            deviceArray:deviceArray,
            countValue: countValue
        });
    });
}
exports.datatree_location=function(req,res){
    var idArray =[];
    var mailArray=[];
    var deviceArray=[];
    var countValue;
    db.userinfo.find({},function(err,doc){
        countValue = doc.length;
        for(var i=0;i<doc.length;i++){
            idArray.push('\''+doc[i].id+'\'');
            mailArray.push('\''+doc[i].mail+'\'');
            deviceArray.push('\''+doc[i].deviceid+'\'');
        }

        res.render('datatree_page_location', {
            title: 'datatree page',
            idArray: idArray,
            mailArray:mailArray,
            deviceArray:deviceArray,
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
    db.userinfo.find({},function(err,doc){
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
    res.render('datatree_page', {
        title: 'datatree page'
    });
}