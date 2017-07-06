var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports = function(req, res, next) {
    console.log("******inside postOrderLineDetails*******");
    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": [],
        "resultArray": {}
    };

    var MongoClient = mongodb.MongoClient;
    var url = 'mongodb://localhost:27017/HotelManagement';
    MongoClient.connect(url, function(err, db) {
        var resultArray = [];
        if (err) {
            nodeJSServerResponse.errorList.push("Could not connect to the database:" + err);
            res.send(nodeJSServerResponse);
        } else {

            var readData = function(hotelId) {
                console.log("******inside readData*******");

                db.collection('TableDetails').find({
                    "hotelId": hotelId
                }).toArray(function(err, lstTableDetails) {
                    if (lstTableDetails.length != 0) {
						console.log('Found Tables');
						
                        var tableIds = [];
                        for (var i = 0; i < lstTableDetails.length; i++) {
                            var tableId = lstTableDetails[i]._id;
                            tableIds.push(tableId + "");
                        }
						db.collection('OrderDetails').find({
							"tableId": {
								$in: tableIds
							},
							"orderStatus": "In Progress"
						}).toArray(function(err, lstOrderDetails) {
							if (lstOrderDetails.length != 0) {
								console.log('Found Orders');
								var orderIds = [];
								for (var i = 0; i < lstOrderDetails.length; i++) {
									var orderId = lstOrderDetails[i]._id;
									orderIds.push(orderId + "");
								}
								db.collection('OrderLineDetails').find({
									"orderId": {
										$in: orderIds
									}
								}).toArray(function(err, lstOrderLineDetails) {
									db.close();
									if (lstOrderLineDetails.length != 0) {
										console.log('Found OrderLines');
										console.log(lstOrderLineDetails);
										nodeJSServerResponse.isSuccess = true;
										nodeJSServerResponse.resultArray["lstOrderLineDetails"] = lstOrderLineDetails;
									}
									console.log(nodeJSServerResponse);
									console.log('Request Finished....Sending Headers...');
									res.send(nodeJSServerResponse);
								});
							} else {
								console.log(nodeJSServerResponse);
								console.log('Request Finished....Sending Headers...');
								res.send(nodeJSServerResponse);
							}
						});
                    }else{
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
					}
                });
            }

            var updateData = function(orderLineDetails) {
                console.log("******inside updateData*******");
                db.collection('OrderLineDetails').update({
                    "_id": new ObjectId(orderLineDetails.id)
                }, {
                    $set: {
                        "quantity": orderLineDetails.quantity
                    }
                }, function(err, res1) {
                    if (res1) {
                        console.log(res1.result);
                        console.log("updated an orderLineDetails into the restaurants collection with id=" + orderLineDetails.id);
                        nodeJSServerResponse.isSuccess = true;
                        orderLineDetails._id = orderLineDetails.id;
                        nodeJSServerResponse.resultArray["lstOrderLineDetails"] = [orderLineDetails];
                    }
                    console.log(nodeJSServerResponse);
                    console.log('Request Finished....Sending Headers...');
                    res.send(nodeJSServerResponse);
                });
            }

            var createData = function(orderLineDetails) {
                console.log("******inside createData*******");
                db.collection('OrderLineDetails').insert(orderLineDetails, function(err, result) {
                    if (result) {
                        console.log("inserted an orderLineDetails into the restaurants collection with id=" + result.ops[0]._id);
                        nodeJSServerResponse.isSuccess = true;
                        nodeJSServerResponse.resultArray["lstOrderLineDetails"] = result.ops;
                    }
                    console.log(nodeJSServerResponse);
                    console.log('Request Finished....Sending Headers...');
                    res.send(nodeJSServerResponse);
                });
            }


            console.log("Database connection Established");
            var body = req.body;
            console.log("Requested Parametes are:-");
            console.log("mode=" + body.mode);
            var mode = JSON.parse(body.mode);

            if (mode == "READ") {
				console.log("hotelId=" + body.hotelId);
				var hotelId = JSON.parse(body.hotelId);
				readData(hotelId);
			} else {
                console.log("orderLineDetails=" + body.orderLineDetails);
                var orderLineDetails = JSON.parse(body.orderLineDetails);
                if (orderLineDetails.id != undefined && orderLineDetails.id != null && orderLineDetails.id != '') {
                    updateData(orderLineDetails);
                } else {
                    createData(orderLineDetails);
                }
            }
        }
    });
}