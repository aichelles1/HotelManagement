var mongodb = require("mongodb");

module.exports= function(req, res, next){
	    var nodeJSServerResponse = {
        "isSuccess": "false",
        "errorList": {},
        "resultArray": {}
    };


    var body = req.body;
    var jsonObj = body.jsonObject;
    var jsonParsed = JSON.parse(jsonObj);
    var username = jsonParsed.username;
    var password = jsonParsed.password;
    console.log("Inside doLogin method");
    console.log("UserName is" + username);
    console.log("Password is" + password);
    //console.log(jsonParsed.username);
    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/HotelManagement';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        var errorList = [];
        var resultArray = [];

        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established");
            var cursor = db.collection('Users').find({
                "UserName": username,
                "Password": password
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
                    nodeJSServerResponse.resultArray["Users"] = resultArray;
                }

                console.log(nodeJSServerResponse);
                res.send(nodeJSServerResponse);
            });
        }
    });
   }