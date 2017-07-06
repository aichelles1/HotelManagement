var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){
	console.log("******inside postOrderAlongWithOrderLines*******");
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
            nodeJSServerResponse.errorList.push("Could not connect to the database:"+err);
			res.send(nodeJSServerResponse);
        } else {
			
			var readData = function(tableId){
				console.log("******inside readData*******");
				db.collection('OrderDetails').find({
						"tableId": tableId,
						"orderStatus":"In Progress"
					}).toArray(function(err, lstOrderDetails) {
						if (lstOrderDetails.length!=0) {
							nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["lstOrderDetails"] = lstOrderDetails;
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
								if (lstOrderLineDetails.length!=0) {
									console.log(lstOrderLineDetails);
									nodeJSServerResponse.resultArray["lstOrderLineDetails"] = lstOrderLineDetails;
								}
								console.log(nodeJSServerResponse);
								console.log('Request Finished....Sending Headers...');
								res.send(nodeJSServerResponse);
							});
						}else{
							console.log(nodeJSServerResponse);
							console.log('Request Finished....Sending Headers...');
							res.send(nodeJSServerResponse);
						}
					});
			}
			
			var createData = function(orderDetails,orderLineDetails){
				console.log("******inside createData*******");
				db.collection('OrderDetails').insert(orderDetails, function(err, result) {
					if (result) {
						console.log("Inserted an order into the restaurants collection.");
						var orderId = result.ops[0]._id+"";
						nodeJSServerResponse.resultArray["lstOrderDetails"] = result.ops;
						console.log("OrderId="+orderId);
						db.collection('TableDetails').update({
								"_id": new ObjectId(orderDetails.tableId)
							}, {
								$set: {
									"status": "Occupied"
								}
							},
							function(err1, result1) {
								if (result1) {
									console.log("Updated HotelTable into the restaurants collection.");
									orderLineDetails.orderId = orderId;
									db.collection('OrderLineDetails').insert(orderLineDetails, function(err2, result2) {
										if(result2){
											console.log("Inserted an orderLine into the restaurants collection.");
											nodeJSServerResponse.resultArray["lstOrderLineDetails"] = result2.ops;
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
			
			console.log("Database connection Established");
			var body = req.body;
			console.log("Requested Parametes are:-");
			console.log("tableId="+body.tableId);
			var tableId = JSON.parse(body.tableId);
			console.log("mode="+body.mode);
			var mode = JSON.parse(body.mode);
			if(mode=="READ"){
				readData(tableId);
			}else{
				console.log("orderDetails="+body.orderDetails);
				var orderDetails = JSON.parse(body.orderDetails);
				console.log("orderLineDetails="+body.orderLineDetails);
				var orderLineDetails = JSON.parse(body.orderLineDetails);
				createData(orderDetails,orderLineDetails);
			}
		}
    });
}