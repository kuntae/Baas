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
    console.log("rank page");
    db.rankinfo.mapReduce(o, function(err, model, stats){
        console.log('map reduce took %d ms', stats.processtime);
        model.find({}).sort({'value':-1}).limit(3).exec( function(err, result){
            for(var i=0; i<result.length;i++){
                console.log('rank %d = %s : %d',i+1, result[i]._id, result[i].value.count);
            }
            res.render('rank_page', {
                title: 'rank_page',
                r1:result[0]._id,
                r2:result[1]._id,
                r3:result[2]._id,
                r1_p:result[0].value.count,
                r2_p:result[1].value.count,
                r3_p:result[2].value.count
            });
        });
    })

}