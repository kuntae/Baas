"use strict";

import * as express from "express";

class Server {
    private app : express.Application;

    constructor(){
        let self = this;
        // create expressjs app.
        self.app = new express();

    }

    public getApp(){
        return this.app;
    }
}

export default new Server().getApp();