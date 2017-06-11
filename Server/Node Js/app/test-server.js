var express = require('express');
var app = express();
var fs = require("fs");
var bodyParser = require('body-parser');
var mongodb = require("mongodb");
var urlencodedParser = bodyParser.urlencoded({extended: false})
var ObjectId = require('mongodb').ObjectID;
var getUsers = require('./getServices/getUsers');
var postDoLogin = require('./postServices/postDoLogin');
var getTableDetails = require('./postServices/getTableDetails');
var getMenuItemDetails = require('./postServices/getMenuItemDetails');
var getMenuItemDescription = require('./postServices/getMenuItemDescription');
var getOrderDetails = require('./postServices/getOrderDetails');
var getOrderLineDetails = require('./postServices/getOrderLineDetails');
var postUpsertOrder = require('./postServices/postUpsertOrder');
var postUpsertOrderLineItem = require('./postServices/postUpsertOrderLineItem');
var postUpdateHotelTable = require('./postServices/postUpdateHotelTable');
//Variables
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));


// Methods

// Get home Page
app.get('/hi', function(req, res, next) {
    res.writeHead(200, {
        'Content-Type': 'text/html'
    });
    fs.readFile('./index.html', null, function(err, data) {
        if (err) {
            console.log('Error aa gaya re');
        } else {
            res.write(data);
        }
        res.end();
    });
    //res.send('mar jaa');
});

app.post('/postUsers', urlencodedParser, function(req, res, next) {
    var name = req.body;
    console.log('inside post method');
    console.log(name);
    res.send(name);
});

app.get('/getUsers', function(req, res, next) {
     getUsers(req, res, next);
});

app.post('/doLogin', function(req, res, next) {
  postDoLogin(req, res, next);
});



//START function to get table details

app.post('/getTableDetails', function(req, res, next) {
   getTableDetails(req, res ,next);
});

//END function to get table details


//START function to getMenuItemDetails

app.post('/getMenuItemDetails', function(req, res, next) {
    getMenuItemDetails(req, res, next);
});

//END function to getMenuItemDetails

//START function to getMenuItemDetails

app.post('/getMenuItemDescription', function(req, res, next) {
    getMenuItemDescription(req, res, next);
});

//END function to getMenuItemDetails




//START function to getOrderDetails
app.post('/getOrderDetails', function(req, res, next) {
    getOrderDetails(req, res, next);
});

//END function to getOrderDetails

//START function to postUpsertOrder
app.post('/postUpsertOrder', function(req, res, next) {
    postUpsertOrder(req, res, next);
});

//END function to postUpsertOrder



//START function to getOrderLineDetails

app.post('/getOrderLineDetails', function(req, res, next) {
    getOrderLineDetails(req, res, next);
});

//END function to getOrderLineDetails

// Starting function postUpsertOrderLineItem
app.post('/postUpsertOrderLineItem', function(req, res, next) {
    postUpsertOrderLineItem(req, res, next);
});

//END function to postUpsertOrderLineItem

//START function to postUpdateHotelTable

app.post('/postUpdateHotelTable', function(req, res, next) {
    postUpdateHotelTable(req, res, next);
});

//END function to postUpdateHotelTable



var server = app.listen(3000, function() {

    var host = 'localhost'
    var port = server.address().port

    console.log("Example app listening at http://%s:%s", host, port)
});