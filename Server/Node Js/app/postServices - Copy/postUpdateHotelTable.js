var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){
	console.log("Inside postUpdateHotelTable method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var body = req.body;
    var hotelTable = JSON.parse(body.hotelTable);
    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + JSON.stringify(hotelTable));
            db.collection('OrderTable').update({
                    "_id": new ObjectId(hotelTable.id)
                }, {
                    $set: {
                        "Status": hotelTable.status
                    }
                },
                function(err, result) {
                    if (result) {
                        console.log("Updated a document into the restaurants collection.");
                        console.log(result);
                        nodeJSServerResponse.isSuccess = true;
                        res.send(nodeJSServerResponse);
                    }
                });
        }
    });
   }