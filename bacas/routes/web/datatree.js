var db = require('./../db_structure');

exports.datatree_user=function(req,res){
    console.log("data tree of user");
    res.render('datatree_page', {
        title: 'datatree page'
    });
}
exports.datatree_rank=function(req,res){
    console.log("data tree of rank");
    res.render('datatree_page', {
        title: 'datatree page'
    });
}
exports.datatree_location=function(req,res){
    console.log("data tree of location");
    res.render('datatree_page', {
        title: 'datatree page'
    });
}
exports.datatree_push=function(req,res){
    console.log("data tree of push");
    res.render('datatree_page', {
        title: 'datatree page'
    });
}
exports.datatree_page= function (req, res) {
    console.log("data tree");
    res.render('datatree_page', {
        title: 'datatree page'
    });
}