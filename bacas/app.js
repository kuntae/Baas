
/**
 * Module dependencies.
 */

var express = require('express')
    , http = require('http')
    , path = require('path')
    , MemStore = express.session.MemoryStore
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
    , rank = require('./routes/mobile/rank')
    , user = require('./routes/mobile/user')
    , push = require('./routes/mobile/push')
    , location = require('./routes/mobile/location');

var app = express();

// all environments
app.set('port', process.env.PORT || 80);
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.cookieParser('shhhh, very secret'));
app.use(express.session({secret: 'alessios', store: MemStore({
    reapInterval: 60000 * 10
})}));
app.use(app.router);

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

//routes
//===================================root page======================================
app.get('/', routes.index);
//=================================db generation====================================
app.get('/db_generation',db_structure.database) ;
app.get('/web/connecttoid',db_structure.connecttoid);
//================================= web pages=======================================
app.get('/login',developer_management.into);                                // 로그인 페이지
app.post('/login/chk',developer_management.developerCheck);                // 로그인 확인
app.get('/logout', developer_management.logout);
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
app.post('/web/developer_management', developer_management.developer_page_save);
app.post('/web/register', push_management.regist);  //mobile에서 접근하는 것은 mobile로 통일 할 것을 권장함.
app.post('/web/send', push_management.send_push);
app.post('/web/addpoi', location_management.addpoi);
app.post('/web/removepoi', location_management.removepoi);
//================================mobile page=======================================
app.get('/mobile/rank',rank.into)
app.get('/mobile/user_regist_deviceid', user.user_regist_deviceid)
app.get('/mobile/user_regist_all', user.user_regist_all)
app.get('/mobile/get_user_info', user.get_user_info);
app.get('/mobile/get_location', location.get_location);
app.post('/mobile/push_register',push.regist); //mobile로 통일한 js파일

//=================================create Server===================================
http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
