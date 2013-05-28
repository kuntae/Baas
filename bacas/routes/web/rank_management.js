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

    var resultDate = y + '-' + m + '-'+ d;
    return resultDate;
}

var p = {};
p.map = function(){
    var date = this.date;
    var format = date.getFullYear()+'-'+(date.getMonth()+1) + '-' + date.getDate();

    emit(format,{count:1});
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
    var d_cnt = [];
    var date;

    for(i=0; i<3; i++){
        function_name[i] = "empty";
        function_cnt[i] = 0;
    }
    console.log("rank page");

    db.rankinfo.mapReduce(o, function(err, model, stats){
        console.log('map reduce took %d ms', stats.processtime);
        model.find({}).sort({'value':-1}).limit(3).exec( function(err, result){
            for(var i=0; i<result.length;i++){
                console.log('rank %d = %s : %d',i+1, result[i]._id, result[i].value.count);
                function_name[i] = result[i]._id;
                function_cnt[i] =  result[i].value.count;
            }

            db.rankinfo.mapReduce(p, function(err2, model2, stats2){
                try{
                    // console.log('map reduce took %d ms', stats.processtime);
                    model2.find({}).sort({'value':-1}).exec( function(err2, result2){
                        for(i=0;i<result2.length;i++){
                            for(var j=0;j<7;j++){
                                date = getdate(j);
                                if(date==result2[i]._id){
                                    d_cnt.push(result2[i].value.count);
                                    console.log(d_cnt[i]);
                                    console.log(result2[i]._id);
                                    console.log(result2[i].value.count);
                                }else
                                    d_cnt.push(0);
                            }
                        }

                        res.render('rank_page', {
                            title: 'rank_page',
                            r1:function_name[0],
                            r2:function_name[1],
                            r3:function_name[2],
                            r1_p:function_cnt[0],
                            r2_p:function_cnt[1],
                            r3_p:function_cnt[2],
                            d_cnt:d_cnt
                        });
                    });
                }catch(err2){
                    console.log('not data');
                    for(i=0;i<7;i++)
                        d_cnt.push(0);
                    res.render('rank_page', {
                        title: 'rank_page',
                        r1:function_name[0],
                        r2:function_name[1],
                        r3:function_name[2],
                        r1_p:function_cnt[0],
                        r2_p:function_cnt[1],
                        r3_p:function_cnt[2],
                        d_cnt:d_cnt
                    });
                }

            });

        });
    });
}