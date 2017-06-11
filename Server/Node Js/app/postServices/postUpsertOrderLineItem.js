var mongodb = require("mongodb");

module.exports= function(req, res, next){console.log("Inside postUpsertOrderLineItem method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var body = req.body;
    var orderLineItem = JSON.parse(body.orderLineItem);
    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/testHotel';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + JSON.stringify(orderLineItem));
            var body = req.body;
            var orderLineItemJSON = body.orderLineItem;
            var orderLineItem = JSON.parse(orderLineItemJSON);
            console.log(orderLineItem);
            var cursor = db.collection('OrderLineItem').insert({
                "MenuItemID": orderLineItem.menuItemId,
                "OrderID": orderLineItem.orderId,
                "Quantity": orderLineItem.quantity,
                "MenuItemName": orderLineItem.menuItemName
            }, function(err, result) {
                console.log("Inserted a document into the restaurants collection.");
                nodeJSServerResponse.isSuccess = true;
                res.send(nodeJSServerResponse);
            });
        }
    });
   }