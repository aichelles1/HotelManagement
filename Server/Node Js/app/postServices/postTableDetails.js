var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){
	console.log("******inside postTableDetails*******");
	    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": [],
        "resultArray": {}
    };
	var updateOK=true;
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
				db.collection('TableDetails').find({
						"hotelId": hotelId
					}).toArray(function(err, lstTableDetails) {
						db.close();
						if (lstTableDetails.length!=0) {
							lstTableDetails.sort(function(a, b) { 
								return a.tableName - b.tableName;
							})

							nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["lstTableDetails"] = lstTableDetails;
						}
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
					});
			}
			
			var createTableWithCapacity = function(lstTableDetails){
				console.log("******inside createTableWithCapacity*******");
				db.collection('TableDetails').insert(lstTableDetails, function(err, result) {
					if (result) {
						console.log("Inserted documents into the restaurants collection.");
						nodeJSServerResponse.isSuccess = true;
						nodeJSServerResponse.resultArray["lstTableDetails"] = result.ops;
					}
					console.log(nodeJSServerResponse);
					console.log('Request Finished....Sending Headers...');
					res.send(nodeJSServerResponse);
				});
			}

			var updateTableWithCapacity = function(tableDetails){
				console.log("******updateTableWithCapacity*******");
				db.collection('TableDetails').update({
					"_id": new ObjectId(tableDetails.id)
				}, tableDetails
				, function(err, res2) {
					if (err) {
						updateOK=false;
					}
				});
			}
			
			var createData = function(numberOfTables,hotelId){
				console.log("******inside createData*******");
				var numberOfTables = parseInt(numberOfTables);
				var lstTableDetails = [];
				for(var i=0;i<numberOfTables;i++){
						var tableDetails = {};
						tableDetails.tableName = String(i+1);
						tableDetails.hotelId = hotelId;
						tableDetails.status = "Vacant";
						lstTableDetails.push(tableDetails);
				}
				
				db.collection('TableDetails').insert(lstTableDetails, function(err, result) {
					if (result) {
						console.log("Inserted documents into the restaurants collection.");
						nodeJSServerResponse.isSuccess = true;
						nodeJSServerResponse.resultArray["lstTableDetails"] = result.ops;
					}
				});
			}
			console.log("Database connection Established");
			var body = req.body;
			console.log("Requested Parametes are:-");
			console.log("mode="+body.mode);
			var mode = JSON.parse(body.mode);
			console.log("hotelId="+body.hotelId);
			var hotelId = JSON.parse(body.hotelId);

			if(mode=="READ"){
				readData(hotelId);
			}else if(mode=="CREATEWITHCAPACITY"){
				console.log("lstTableDetails="+body.lstTableDetails);
				var lstTableDetails = JSON.parse(body.lstTableDetails);
				var toInsert = [];
				var toUpdate = [];
				for(var i=0;i<lstTableDetails.length;i++){
					if(lstTableDetails[i].id==undefined){
						toInsert.push(lstTableDetails[i]);
					}else{
						updateTableWithCapacity(lstTableDetails[i]);
					}
				}
				
				if(updateOK){
					if(toInsert.length>0){
						createTableWithCapacity(toInsert);
					}else{
						nodeJSServerResponse.isSuccess=true;
						nodeJSServerResponse.resultArray["lstTableDetails"] = lstTableDetails;
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);					
					}
				}else{
					console.log(nodeJSServerResponse);
					console.log('Request Finished....Sending Headers...');
					res.send(nodeJSServerResponse);					
				}
				
				
			}else{
				console.log("numberOfTables="+body.numberOfTables);
				var numberOfTables = JSON.parse(body.numberOfTables);
				createData(numberOfTables,hotelId);
			}
		}
    });
}