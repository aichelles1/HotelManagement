var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){
	console.log("******inside postOrderDetails*******");
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
			
			var readData = function(hotelId){
				console.log("******inside readData*******");
				db.collection('MenuItemDetails').find({
						"hotelId": hotelId
					}).toArray(function(err, lstMenuItemDetails) {
						db.close();
						if (lstMenuItemDetails.length!=0) {
							nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["lstMenuItemDetails"] = lstMenuItemDetails;
						}
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
					});
			}

			var readSingleRecord = function(menuItemId){
				console.log("******inside readSingleRecord*******");
				db.collection('MenuItemDetails').find({
						"_id": new ObjectId(menuItemId)
					}).toArray(function(err, lstMenuItemDetails) {
						db.close();
						if (lstMenuItemDetails.length!=0) {
							nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["lstMenuItemDetails"] = lstMenuItemDetails;
						}
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
					});
			}

            var updateData = function(orderDetails) {
                console.log("******inside updateData*******");
                db.collection('OrderDetails').update({
                    "_id": new ObjectId(orderDetails.id)
                }, {
                    $set: {
                        "orderStatus": orderDetails.orderStatus
                    }
                }, function(err, res1) {
                    if (res1) {
                        console.log(res1.result);
						console.log("updated an orderDetails into the restaurants collection with id=" + orderDetails.id);
						if(orderDetails.orderStatus=="Completed"){
							db.collection('TableDetails').update({
								"_id": new ObjectId(orderDetails.tableId)
							}, {
								$set: {
									"status": "Vacant"
								}
							}, function(err, res2) {
								if (res2) {
									console.log(res2.result);
									console.log("updated a tableDetails into the restaurants collection with id=" + orderDetails.tableId);
									nodeJSServerResponse.isSuccess = true;
									orderDetails._id = orderDetails.id;
									nodeJSServerResponse.resultArray["lstOrderDetails"] = [orderDetails];
								}
								console.log(nodeJSServerResponse);
								console.log('Request Finished....Sending Headers...');
								res.send(nodeJSServerResponse);
							});
						}else{
							console.log("updated an orderDetails into the restaurants collection with id=" + orderDetails.id);
							nodeJSServerResponse.isSuccess = true;
							orderDetails._id = orderDetails.id;
							nodeJSServerResponse.resultArray["lstOrderDetails"] = [orderDetails];
						}
                    }else{
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
					}
                });
            }

			
			var createData = function(menuItemDetails,hotelId){
				console.log("******inside createData*******");
				db.collection('MenuItemDetails').insert(menuItemDetails, function(err, result) {
					if (result) {
						console.log("Inserted documents into the restaurants collection.");
						nodeJSServerResponse.isSuccess = true;
						nodeJSServerResponse.resultArray["lstMenuItemDetails"] = result.ops;
					}
					console.log(nodeJSServerResponse);
					console.log('Request Finished....Sending Headers...');
					res.send(nodeJSServerResponse);
				});
			}
			console.log("Database connection Established");
			var body = req.body;
			console.log("Requested Parametes are:-");
			console.log("mode="+body.mode);
			var mode = JSON.parse(body.mode);

			if(mode=="UPDATE"){
				console.log("orderDetails="+body.orderDetails);
				var orderDetails = JSON.parse(body.orderDetails);
				if(orderDetails.id!=undefined && orderDetails.id!=null && orderDetails.id!=''){
					updateData(orderDetails);
				}
			}
		}
    });
}