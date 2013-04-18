
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
//=================================login page=======================================
app.get('/login',login.into);
app.post('/login/chk',login.usercheck);
//=================================db generation====================================
app.get('/db_generation',db_structure.database) ;
//================================= web pages=======================================
app.get('/web/user_management', user_management.user_page);
app.get('/web/push_management', push_management.push_page);
app.get('/web/location_management', location_management.location_page);
app.get('/web/file_management', file_management.file_page);
app.get('/web/rank_management', rank_management.rank_page);
app.get('/web/datatree', datatree.datatree_page);
app.get('/web/developer_management', developer_management.developer_page);
app.post('/web/register', push_management.regist);
app.get('/web/send', push_management.send_push);
//================================mobile page=======================================
app.get('/mobile/rank',rank.into);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
