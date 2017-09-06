"use strict";

import App from "./app";
import * as http from 'http';

App.set('port', 8080);

var server = http.createServer(App);

server.listen(8080);