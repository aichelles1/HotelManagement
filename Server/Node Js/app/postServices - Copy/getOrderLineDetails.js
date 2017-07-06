var mongodb = require("mongodb");

module.exports= function(req, res, next){console.log("Inside getOrderLineDetails method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var body = req.body;
    var orderId = JSON.parse(body.orderId);
    var mode = JSON.parse(body.mode);

    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + orderId);
            var cursor = db.collection('OrderLineItem').find({
                "OrderID": orderId
            });
            cursor.forEach(function(doc, err) {
                if (err) {
                    console.log("Code fata in collection");
                    errorList.push(err);
                } else {
                    resultArray.push(doc);
                }
            }, function() {

                if (errorList) {
                    nodeJSServerResponse.errorList["Errors"] = errorList;
                }
                if (resultArray) {
                    nodeJSServerResponse.isSuccess = true;
                    nodeJSServerResponse.resultArray["OrderLineItems"] = resultArray;
                }
                db.close();
                console.log('Connection Ended.');
                //checking for errors
                console.log(nodeJSServerResponse);
                res.send(nodeJSServerResponse);
            });
        }
    });
   }