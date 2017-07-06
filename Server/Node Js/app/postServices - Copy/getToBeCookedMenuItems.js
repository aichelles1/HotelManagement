var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports = function(req, res, next) {
    console.log("Inside getToBeCookedMenuItems method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };



    var body = req.body;
    var hotelId = JSON.parse(body.hotelId);


    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + hotelId);
            db.collection("OrderTable").find({
                "HotelID": hotelId
            }).toArray(function(err, orderTables) {

                var orderTableIds = [];

                for (var i = 0; i < orderTables.length; i++) {
                    var orderTableId = orderTables[i]._id;
                    orderTableIds.push(orderTableId + "");
                }
                db.collection('Order').find({
                    "OrderStatus": "In Progress",
                    "TableID": {
                        $in: orderTableIds
                    }
                }).toArray(function(err, orders) {
                    var orderIds = [];
                    for (var i = 0; i < orders.length; i++) {
                        var orderId = orders[i]._id;
                        orderIds.push(orderId + "");
                    }
                    db.collection('OrderLineItem').find({
                        "OrderID": {
                            $in: orderIds
                        }
                    }).toArray(function(err, orderLineItems) {
						db.close();
						if (orderLineItems.length!=0) {
							nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["OrderLineItems"] = orderLineItems;
						}
						console.log(nodeJSServerResponse);
						console.log('Connection Ended.');
						//checking for errors
						res.send(nodeJSServerResponse);
                    });
                });
            });

        }
    });
}