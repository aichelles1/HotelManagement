var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){console.log("Inside postUpsertOrder method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var body = req.body;
    var MongoClient = mongodb.MongoClient;
    var orderJSON = body.order;
    var mode = JSON.parse(body.mode);

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            var orderJSON = body.order;
            console.log("connection Established:" + JSON.stringify(orderJSON));
            var order = JSON.parse(orderJSON);
            console.log(order);
			console.log("Mode="+mode);
            if (mode == "UPDATE") {
                db.collection('Order').update({
                        "_id": new ObjectId(order.id)
                    }, {
                        $set: {
                            "OrderStatus": order.orderStatus
                        }
                    },
                    function(err, result) {
                        if (result) {
                            console.log("Updated an order into the restaurants collection.");
                            console.log(result);
                            nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["orderId"] = order.id;
                            res.send(nodeJSServerResponse);
                        }
                    });

            } else if (mode == "CREATE") {
				console.log("Inside CReate");
                db.collection('Order').insert({
                    "OrderDateTime": order.orderTime,
                    "OrderStatus": order.orderStatus,
                    "TableID": order.tableId
                }, function(err, result) {
                    if (result) {
                        console.log("Inserted a document into the restaurants collection.");
                        nodeJSServerResponse.isSuccess = true;
                        nodeJSServerResponse.resultArray["orderId"] = result.ops[0]._id;
                        res.send(nodeJSServerResponse);
                    }
                });
            }else {
				console.log("But i am here...");
			}
        }
    });
   }