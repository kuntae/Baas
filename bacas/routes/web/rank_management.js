var db = require('./../db_structure');

var rank = new db.rankinfo();

exports.rank_page= function (req, res) {
    console.log("rank page");
    res.render('rank_page', {
        title: 'rank_page'
    });
}
/*
var o = {};
o.map = function(){
    this.rankinfo.func.forEach(function(element){
        console.log(element);
          emit(element,{count:1});
    });
};
o.reduce = function(k,vals){
    var total = 0;
    for(var i=0;i<vals.length;i++){
        total+=vals[i].count;
    }
    console.log(total);
    return {cnt:total};
};
o.out = {replace: 'sortranks'};
o.verbose = true;

db.rankinfo.mapreduce(o,function(err,model,stats){
    console.log('map reduce took %d ms',stats.processtime);
    model.find().where('value').gt(10).exec(function (err, docs) {
        console.log(docs);
    });
});   */
var map = function(){
    this.func.forEach(function(element){
        emit(element, {count: 1});
    });
};
var reduce = function(key, values){
    var total = 0;
    for(var i=0;i<values.length;i++){
        total += values[i].count;
    }
    return {count:total};
};
var command = {
    mapreduce:"rankinfos",
    map: map.toString(),
    reduce: reduce.toString(),
    out:"sortranks"
};
var Schema = db.mongoose.Schema;
db.mongoose.connection.db.executeDbCommand(command, function(err,dbres){

});
