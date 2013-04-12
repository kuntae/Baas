
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.redirect('/db_test');    //index는 비우고 가장 첫 페이지로 바로 연결
};

