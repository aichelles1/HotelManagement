var mongodb = require("mongodb");

module.exports= function(req, res, next){//  res.end( "i am the only user" );
    var MongoClient = mongodb.MongoClient;

    var url = 'mongodb://localhost:27017/users';
    //console.log(req.params);

    MongoClient.connect(url, function(err, db) {
        if (err) {
            console.log("Nahi mila users db");
        } else {
            console.log("connection Established");
            var cursor = db.collection('users').find();
            var resultArray = [];
            cursor.forEach(function(doc, err) {
                if (err) {
                    console.log("Code fata in collection");
                } else {
                    resultArray.push(doc);
                    console.log(doc);
                    console.log('Pushed');
                }
            }, function() {
                db.close();
                console.log('Connection Ended.');
                res.send({
                    items: resultArray
                });
            });
        }
    });
   }