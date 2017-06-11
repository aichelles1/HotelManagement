var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){
	console.log("Inside getMenuItemDescription method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };



    var body = req.body;
    var menuItemId = JSON.parse(body.menuItemId);


    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/testHotel';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + menuItemId);
            var cursor = db.collection('MenuItem').find({
                "_id": new ObjectId(menuItemId)
            });
            cursor.forEach(function(doc, err) {
                if (err) {
                    console.log("Code fata in collection");
                    errorList.push(err);
                } else {
                    resultArray.push(doc);
                    console.log(doc);
                    console.log('Pushed');
                }
            }, function() {
                db.close();
                console.log('Connection Ended.');
                //checking for errors
                if (errorList) {
                    nodeJSServerResponse.errorList["Errors"] = errorList;
                }
                if (resultArray) {
                    nodeJSServerResponse.isSuccess = true;
                    nodeJSServerResponse.resultArray["MenuItem"] = resultArray;
                }

                console.log(nodeJSServerResponse);
                res.send(nodeJSServerResponse);
            });
        }
    });
   }