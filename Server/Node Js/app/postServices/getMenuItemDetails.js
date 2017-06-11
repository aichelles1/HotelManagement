var mongodb = require("mongodb");

module.exports= function(req, res, next){
	console.log("Inside getMenuItemDetails method");

    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };



    var body = req.body;
    var hotelId = JSON.parse(body.hotelId);


    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/testHotel';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established:" + hotelId);
            var cursor = db.collection('MenuItem').find({
                "HotelID": hotelId
            });
            cursor.forEach(function(doc, err) {
                if (err) {
                    console.log("Code fata in collection");
                    errorList.push(err);
                } else {
                    resultArray.push(doc);
                    console.log(doc);
                    console.log('Pushed');
                }
            }, function() {
                db.close();
                console.log('Connection Ended.');
                //checking for errors
                if (errorList) {
                    nodeJSServerResponse.errorList["Errors"] = errorList;
                }
                if (resultArray) {
                    nodeJSServerResponse.isSuccess = true;
                    nodeJSServerResponse.resultArray["MenuItem"] = resultArray;
                }

                console.log(nodeJSServerResponse);
                res.send(nodeJSServerResponse);
            });
        }
    });
   }