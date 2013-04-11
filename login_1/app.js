
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  ,login = require('./routes/login')
  , http = require('http')
  , path = require('path')
  , user_management = require('./routes/user_management')
  , push_management = require('./routes/push_management')
  , location_management = require('./routes/location_management')
  , file_management = require('./routes/file_management')
  , rank_management = require('./routes/rank_management')
  , datatree = require('./routes/datatree')
  , developer_management = require('./routes/developer_management')

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

app.get('/', routes.index);
app.get('/users', user.list);
app.post('/login',login.login);
app.get('/user_management', user_management.user_page);
app.get('/push_management', push_management.push_page);
app.get('/location_management', location_management.location_page);
app.get('/file_management', file_management.file_page);
app.get('/rank_management', rank_management.rank_page);
app.get('/datatree', datatree.datatree_page);
app.get('/developer_management', developer_management.developer_page);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
