var db = require('./../db_structure');

var rank = new db.rankinfo();


var o = {};
o.map = function(){
    //this.used_function.forEach(function(element){
    emit(this.used_function,{count:1}) ;
    //});
}
o.reduce = function(k, vals){
    var total = 0;
    for(var i=0; i<vals.length; i++){
        total+=vals[i].count;
    }
    return {count: total}
}
o.out = {replace: 'sorts'}
o.verbose = true;

var getdate = function(i){
    // var settingDate = new Date();
    //settingDate.setDate(settingDate.getDate()-6+i); //i일 전

    var settingDate =new Date();
    settingDate.setDate(settingDate.getDate()-6+i);
    var y = settingDate.getFullYear();
    var m = settingDate.getMonth() + 1;
    var d = settingDate.getDate();
    if(m < 10)    { m = "0" + m; }
    if(d < 10)    { d = "0" + d; }

    var resultDate = y + "-" + m + "-" + d;
    return resultDate;
}

var p = {};
p.map = function(){
    var date = this.date;
    var format = date.getFullYear()+'-'+(date.getMonth()+1) + '-' + date.getDate();

    for(var i=0;i<7;i++){
        var tmp = getdate(i);
        if(tmp.equal(format)){
            emit(format,{count:1});
        }
    }
}
p.reduce = function(k, vals){
    var total = 0;
    for(var i=0;i<vals.length;i++){
        total+=vals[i].count;
    }
    return {count:total};
}
p.out = {replace:'dsorts'}
p.verbose = true;

exports.rank_page= function (req, res) {
    var function_name = new Array(3);
    var function_cnt = new Array(3);
    var d_cnt = new Array(7);

    for(i=0; i<3; i++){
        function_name[i] = "empty";
        function_cnt[i] = 0;
    }
    console.log("rank page");
    for(var i=6;i>=0;i--){
        console.log(getdate(i));
    }

    db.rankinfo.mapReduce(o, function(err, model, stats){
        console.log('map reduce took %d ms', stats.processtime);
        model.find({}).sort({'value':-1}).limit(3).exec( function(err, result){
            for(var i=0; i<result.length;i++){
                console.log('rank %d = %s : %d',i+1, result[i]._id, result[i].value.count);
                function_name[i] = result[i]._id;
                function_cnt[i] =  result[i].value.count;
            }
            console.log(function_name.length);
            db.rankinfo.mapReduce(p, function(err2, model2, stats2){
                try{
                    // console.log('map reduce took %d ms', stats.processtime);
                    model2.find({}).sort({'value':-1}).exec( function(err2, result2){
                        for(i=0;i<result2.length;i++){
                            console.log(result2[i]._id);
                            console.log(result2[i].value.count);
                        }
                    });
                }catch(err2){
                    console.log('not data');
                }
            });
            res.render('rank_page', {
                title: 'rank_page',
                r1:function_name[0],
                r2:function_name[1],
                r3:function_name[2],
                r1_p:function_cnt[0],
                r2_p:function_cnt[1],
                r3_p:function_cnt[2]
            });
        });
    });
}