var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){
	console.log("******inside postMenuItemDetails*******");
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

			var updateData = function(menuItemDetails,hotelId){
				console.log("******inside updateData*******");
							db.collection('MenuItemDetails').update({
								"_id": new ObjectId(menuItemDetails.id)
							}, menuItemDetails
							, function(err, res2) {
								if (res2) {
									console.log(res2.result);
									console.log("updated a menuItemDetails into the restaurants collection with id=" + menuItemDetails.id);
									nodeJSServerResponse.isSuccess = true;
									menuItemDetails._id = menuItemDetails.id;
									nodeJSServerResponse.resultArray["lstMenuItemDetails"] = [menuItemDetails];
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
			console.log("hotelId="+body.hotelId);
			var hotelId = JSON.parse(body.hotelId);
			console.log("menuItemId="+body.menuItemId);
			var menuItemId = JSON.parse(body.menuItemId);

			if(mode=="READ"){
				if(menuItemId!=undefined && menuItemId!=null && menuItemId!=''){
					readSingleRecord(menuItemId);
				}else{
					readData(hotelId);
				}
			}else{
				console.log("menuItemDetails="+body.menuItemDetails);
				var menuItemDetails = JSON.parse(body.menuItemDetails);
				if(menuItemDetails.id!=undefined && menuItemDetails.id!=null && menuItemDetails.id!=''){
					updateData(menuItemDetails,hotelId);
				}else{
					createData(menuItemDetails,hotelId);
				}
			}
		}
    });
}