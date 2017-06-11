var mongodb = require("mongodb");

module.exports= function(req, res, next){
	console.log("Inside getOrderDetails method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var body = req.body;
    var tableId = JSON.parse(body.tableId);
    var mode = JSON.parse(body.mode);
    var orderId;

    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/testHotel';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + tableId);
            var cursor = db.collection('Order').find({
                "TableID": tableId,
                "OrderStatus": "In Progress"
            });
            cursor.forEach(function(doc, err) {
                if (err) {
                    console.log("Code fata in collection");
                    errorList.push(err);
                } else {
                    resultArray.push(doc);
                    orderId = doc._id;
                    console.log("id is-->" + orderId);
                }
            }, function() {

                if (errorList) {
                    nodeJSServerResponse.errorList["Errors"] = errorList;
                }
                if (resultArray) {
                    nodeJSServerResponse.isSuccess = true;
                    nodeJSServerResponse.resultArray["Order"] = resultArray;
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