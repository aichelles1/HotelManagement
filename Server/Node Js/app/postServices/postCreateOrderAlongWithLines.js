var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports = function(req, res, next) {
    console.log("Inside postCreateOrderAlongWithLines method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var body = req.body;
    var order = JSON.parse(body.orderDetail);
	var orderLineItem = JSON.parse(body.orderLineItem);
    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:");
            console.log(JSON.stringify(order));
            db.collection('Order').insert({
                "OrderDateTime": order.orderTime,
                "OrderStatus": order.orderStatus,
                "TableID": order.tableId
            }, function(err, result) {
                if (result) {
                    console.log("Inserted an order into the restaurants collection.");
                    var orderId = result.ops[0]._id+"";
					console.log(orderId);
                    db.collection('OrderTable').update({
                            "_id": new ObjectId(order.tableId)
                        }, {
                            $set: {
                                "Status": "Occupied"
                            }
                        },
                        function(err1, result1) {
                            if (result1) {
								console.log("Updated HotelTable into the restaurants collection.");
								db.collection('OrderLineItem').insert({
                                    "MenuItemID": orderLineItem.menuItemId,
                                    "OrderID": orderId,
                                    "Quantity": orderLineItem.quantity,
                                    "MenuItemName": orderLineItem.menuItemName
                                }, function(err2, result2) {
									if(result2){
										console.log("Inserted an orderLine into the restaurants collection.");
										nodeJSServerResponse.isSuccess = true;
										res.send(nodeJSServerResponse);
									}else{
										res.send(nodeJSServerResponse);
									}
                                });
                            }else{
								res.send(nodeJSServerResponse);
							}
                        });
                }else{
					res.send(nodeJSServerResponse);
				}
			});
        }
    });
}