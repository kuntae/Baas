
/**
 * Module dependencies.
 */

var express = require('express')
  , http = require('http')
  , path = require('path')
    //===========================================================================================
  , routes = require('./routes')
  , login = require('./routes/web/login')
  , db_structure = require('./routes/db_structure')
    testDB = require('./routes/testDB')
   //===============================web page routing javascript===================================
  , user_management = require('./routes/web/user_management')
  , push_management = require('./routes/web/push_management')
  , location_management = require('./routes/web/location_management')
  , file_management = require('./routes/web/file_management')
  , rank_management = require('./routes/web/rank_management')
  , datatree = require('./routes/web/datatree')
  , developer_management = require('./routes/web/developer_management')
  //===============================mobile page routing javascript=================================
  , rank = require('./routes/mobile/rank');

var app = express();

// all environments
app.set('port', process.env.PORT || 80);
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

//routes
//===================================root page======================================
app.get('/', routes.index);
//=================================db generation====================================
app.get('/db_generation',db_structure.database) ;
//=================================Test DataBase Case===============================
app.get('/test',testDB.developerSave);
app.get('/test2',testDB.userSave);
//================================= web pages=======================================
app.get('/login',developer_management.into);
app.post('/login/chk',developer_management.developerCheck);
app.get('/web/signup',developer_management.developersignup);
app.post('/web/signup/chk',developer_management.developersignupChk);
app.get('/web/user_management', user_management.user_page);
app.get('/web/push_management', push_management.push_page);
app.get('/web/location_management', location_management.location_page);
app.get('/web/file_management', file_management.file_page);
app.get('/web/rank_management', rank_management.rank_page);
app.get('/web/datatree', datatree.datatree_page);
app.get('/web/datatree/user', datatree.datatree_user);
app.get('/web/datatree/push', datatree.datatree_push);
app.get('/web/datatree/location', datatree.datatree_location);
app.get('/web/datatree/rank', datatree.datatree_rank);
app.get('/web/developer_management', developer_management.developer_page);
app.post('/web/register', push_management.regist);
app.post('/web/send', push_management.send_push);
app.post('/web/addpoi', location_management.addpoi);
app.post('/web/removepoi', location_management.removepoi);
//================================mobile page=======================================
app.get('/mobile/rank',rank.into);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
