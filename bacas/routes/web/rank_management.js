var db = require('./../db_structure');



exports.rank_page= function (req, res) {
    console.log("rank page");
    res.render('rank_page', {
        title: 'rank_page'
    });
}