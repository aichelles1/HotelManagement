var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports = function(req, res, next) {
    console.log("Inside getOrderAlongWithOrderLines method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };



    var body = req.body;
    var tableID = JSON.parse(body.tableId);


    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + tableID);
                db.collection('Order').find({
                    "OrderStatus": "In Progress",
                    "TableID": tableID
                }).toArray(function(err, orders) {
                    var orderIds = [];
					if(orders.length!=0){
						console.log(orders);
						nodeJSServerResponse.isSuccess = true;
						nodeJSServerResponse.resultArray["Orders"] = orders;
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
								console.log(orderLineItems);
								nodeJSServerResponse.resultArray["OrderLineItems"] = orderLineItems;
							}
							console.log(nodeJSServerResponse);
							console.log('Connection Ended.');
							res.send(nodeJSServerResponse);
						});
						}else{
							nodeJSServerResponse.isSuccess = false;
							console.log('Connection Ended.');
							res.send(nodeJSServerResponse);

							}
						
                });			
        }
    });
}