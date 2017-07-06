var mongodb = require("mongodb");
var ObjectId = require('mongodb').ObjectID;

module.exports= function(req, res, next){console.log("Inside postHandleMenuItems method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };

    var body = req.body;
	var menuItems = JSON.parse(body.menuItems);
	var mode = JSON.parse(body.mode);
    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
			if(mode=="CREATE"){
                db.collection('MenuItems').insert(menuItems, function(err, result) {
                    if (result) {
                        console.log("Inserted a document into the restaurants collection.");
                        nodeJSServerResponse.isSuccess = true;
                        nodeJSServerResponse.resultArray["id"] = result.ops[0]._id;
                        res.send(nodeJSServerResponse);
                    }
                });
			}else if(mode=="READ"){
                    db.collection('MenuItems').find({})
					.toArray(function(err, menuItems) {
						db.close();
						if (menuItems.length!=0) {
							nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["menuItems"] = menuItems;
						}
						console.log(nodeJSServerResponse);
						console.log('Connection Ended.');
						//checking for errors
						res.send(nodeJSServerResponse);
                    });
				
			}
        }
    });
   }