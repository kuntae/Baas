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


exports.rank_page= function (req, res) {
    var function_name = new Array(3);
    var function_cnt = new Array(3);

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
            console.log(function_name.length);


            console.log(function_name[0] + ' ' + function_name[1] + ' ' + function_name[2] + ' ' + function_cnt[0] + function_cnt[1] + function_cnt[2]);

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
    })

}