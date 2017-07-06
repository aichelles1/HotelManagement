var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){console.log("Inside postUpsertOrderLineItem method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);
            var body = req.body;
            var orderLineItemJSON = body.orderLineItem;
            var orderLineItem = JSON.parse(orderLineItemJSON);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + JSON.stringify(orderLineItem));
			if(orderLineItem.id==undefined || orderLineItem==''){
				db.collection('OrderLineItem').insert({
					"MenuItemID": orderLineItem.menuItemId,
					"OrderID": orderLineItem.orderId,
					"Quantity": orderLineItem.quantity,
					"MenuItemName": orderLineItem.menuItemName
				}, function(err, result) {
					console.log("Inserted an orderLine into the restaurants collection.");
					nodeJSServerResponse.isSuccess = true;
					res.send(nodeJSServerResponse);
				});
			}else{
                db.collection('OrderLineItem').update({
                        "_id": new ObjectId(orderLineItem.id)
                    }, {
                        $set: {
                            "Quantity": orderLineItem.quantity
                        }
                    },
                    function(err, result) {
                        if (result) {
							console.log("updated an orderLine into the restaurants collection.");
							nodeJSServerResponse.isSuccess = true;
							res.send(nodeJSServerResponse);
                        }
                    });
			}
        }
    });
   }