var express = require('express');
var app = express();
var fs = require("fs");
var bodyParser = require('body-parser');
var mongodb = require("mongodb");
var urlencodedParser = bodyParser.urlencoded({ extended: false })
var ObjectId = require('mongodb').ObjectID;
//Variables



	app.use(bodyParser.json());
	app.use(bodyParser.urlencoded({extended:true}));


// Get home Page
app.get('/hi', function(req, res,next){
  res.writeHead(200, {'Content-Type': 'text/html'});
  fs.readFile('./index.html',null, function(err, data){
    if(err){
      console.log('Error aa gaya re');
    }else{
      res.write(data);
    }
    res.end();
  });
 //res.send('mar jaa');
});

app.post('/postUsers', urlencodedParser, function(req, res, next){
  var name = req.body;
  console.log('inside post method');
  console.log(name);
  res.send(name);
});
 
app.get('/getUsers', function(req, res, next){
     //  res.end( "i am the only user" );
	   var MongoClient = mongodb.MongoClient;
     
     var url = 'mongodb://localhost:27017/users';
     //console.log(req.params);

     MongoClient.connect(url, function(err, db){
      if(err){
        console.log("Nahi mila users db");
      }
      else{
        console.log("connection Established");
        var cursor = db.collection('users').find();
        var resultArray = [];
        cursor.forEach(function(doc, err){
          if(err){
            console.log("Code fata in collection");
          }
          else{
            resultArray.push(doc);
            console.log(doc);
            console.log('Pushed');
          }
        }, function(){
          db.close();
          console.log('Connection Ended.');
          res.send({items : resultArray});
        });
      }
     });
   });

app.post('/doLogin', function(req, res, next){
	
	var nodeJSServerResponse = {
		"isSuccess":"false",
		"errorList":{},
		"resultArray":{}
	}; 

	
	var body = req.body;
	var jsonObj = body.jsonObject;
	var jsonParsed = JSON.parse(jsonObj);
	var username = jsonParsed.username;
	var password = jsonParsed.password;
	console.log("Inside doLogin method");
	console.log("UserName is"+username);
	console.log("Password is"+password);
	//console.log(jsonParsed.username);
	 var MongoClient = mongodb.MongoClient;
     
     var url = 'mongodb://localhost:27017/testHotel';
     //console.log(req.params);

     MongoClient.connect(url, function(err, db){
		var errorList = [];
		var resultArray = [];

	 if(err){
        console.log("Nahi mila users db");
      }
      else{
        console.log("connection Established");
        var cursor = db.collection('Users').find({"UserName":username, "Password":password});
		cursor.forEach(function(doc, err){
          if(err){
            console.log("Code fata in collection");
			errorList.push(err);
          }
          else{
			resultArray.push(doc);
            console.log(doc);
            console.log('Pushed');
          }
        }, function(){
          db.close();
          console.log('Connection Ended.');
          //checking for errors
		  if(errorList){
			nodeJSServerResponse.errorList["Errors"]=errorList;
		  }		  
		  if(resultArray){
				nodeJSServerResponse.isSuccess = true;
				nodeJSServerResponse.resultArray["Users"]= resultArray;
		  }		  
		  
		  console.log(nodeJSServerResponse);
		  res.send(nodeJSServerResponse);
        });
        }
      });
     });
   //});


 //START function to get table details
   
app.post('/getTableDetails', function(req, res, next){
	console.log("Inside getTableDetails method");

	var nodeJSServerResponse = {
		"isSuccess":"false",
		"errorList":{},
		"resultArray":{}
	}; 

	
	
	var body = req.body;
	var hotelId = JSON.parse(body.hotelId);
	
	
	var MongoClient = mongodb.MongoClient;
     
     var url = 'mongodb://localhost:27017/testHotel';
     //console.log(req.params);

     MongoClient.connect(url, function(err, db){
		var errorList = [];
		var resultArray = [];

	 if(err){
        console.log("Nahi mila users db");
      }
      else{
        console.log("connection Established:"+hotelId);
        var cursor = db.collection('OrderTable').find({"HotelID":hotelId});
		cursor.forEach(function(doc, err){
          if(err){
            console.log("Code fata in collection");
			errorList.push(err);
          }
          else{
			resultArray.push(doc);
            console.log(doc);
            console.log('Pushed');
          }
        }, function(){
          db.close();
          console.log('Connection Ended.');
          //checking for errors
		  if(errorList){
			nodeJSServerResponse.errorList["Errors"]=errorList;
		  }		  
		  if(resultArray){
				nodeJSServerResponse.isSuccess = true;
				nodeJSServerResponse.resultArray["OrderTable"]= resultArray;
		  }		  
		  
		  console.log(nodeJSServerResponse);
		  res.send(nodeJSServerResponse);
        });
        }
      });
     });
   
 //END function to get table details
 

 //START function to getMenuItemDetails
   
app.post('/getMenuItemDetails', function(req, res, next){
	console.log("Inside getMenuItemDetails method");

	var nodeJSServerResponse = {
		"isSuccess":"false",
		"errorList":{},
		"resultArray":{}
	}; 

	
	
	var body = req.body;
	var hotelId = JSON.parse(body.hotelId);
	
	
	var MongoClient = mongodb.MongoClient;
     
     var url = 'mongodb://localhost:27017/testHotel';
     //console.log(req.params);

     MongoClient.connect(url, function(err, db){
		var errorList = [];
		var resultArray = [];

	 if(err){
        console.log("Nahi mila users db");
      }
      else{
        console.log("connection Established:"+hotelId);
        var cursor = db.collection('MenuItem').find({"HotelID":hotelId});
		cursor.forEach(function(doc, err){
          if(err){
            console.log("Code fata in collection");
			errorList.push(err);
          }
          else{
			resultArray.push(doc);
            console.log(doc);
            console.log('Pushed');
          }
        }, function(){
          db.close();
          console.log('Connection Ended.');
          //checking for errors
		  if(errorList){
			nodeJSServerResponse.errorList["Errors"]=errorList;
		  }		  
		  if(resultArray){
				nodeJSServerResponse.isSuccess = true;
				nodeJSServerResponse.resultArray["MenuItem"]= resultArray;
		  }		  
		  
		  console.log(nodeJSServerResponse);
		  res.send(nodeJSServerResponse);
        });
        }
      });
     });
   
 //END function to getMenuItemDetails

  //START function to getMenuItemDetails
   
app.post('/getMenuItemDescription', function(req, res, next){
	console.log("Inside getMenuItemDescription method");

	var nodeJSServerResponse = {
		"isSuccess":"false",
		"errorList":{},
		"resultArray":{}
	}; 

	
	
	var body = req.body;
	var menuItemId = JSON.parse(body.menuItemId);
	
	
	var MongoClient = mongodb.MongoClient;
     
     var url = 'mongodb://localhost:27017/testHotel';
     //console.log(req.params);

     MongoClient.connect(url, function(err, db){
		var errorList = [];
		var resultArray = [];

	 if(err){
        console.log("Nahi mila users db");
      }
      else{
        console.log("connection Established:"+menuItemId);
        var cursor = db.collection('MenuItem').find({"_id":new ObjectId(menuItemId)});
		cursor.forEach(function(doc, err){
          if(err){
            console.log("Code fata in collection");
			errorList.push(err);
          }
          else{
			resultArray.push(doc);
            console.log(doc);
            console.log('Pushed');
          }
        }, function(){
          db.close();
          console.log('Connection Ended.');
          //checking for errors
		  if(errorList){
			nodeJSServerResponse.errorList["Errors"]=errorList;
		  }		  
		  if(resultArray){
				nodeJSServerResponse.isSuccess = true;
				nodeJSServerResponse.resultArray["MenuItem"]= resultArray;
		  }		  
		  
		  console.log(nodeJSServerResponse);
		  res.send(nodeJSServerResponse);
        });
        }
      });
     });
   
 //END function to getMenuItemDetails

 
 
 
 
 //START function to getOrderDetails
   
app.post('/getOrderDetails', function(req, res, next){
	console.log("Inside getOrderDetails method");

	var nodeJSServerResponse = {
		"isSuccess":"false",
		"errorList":{},
		"resultArray":{}
	}; 
	
	var body = req.body;
	var tableId = JSON.parse(body.tableId);
	var mode = JSON.parse(body.mode);
	var orderId;
	
	var MongoClient = mongodb.MongoClient;
     
     var url = 'mongodb://localhost:27017/testHotel';
     //console.log(req.params);

     MongoClient.connect(url, function(err, db){
		var errorList = [];
		var resultArray = [];

	 if(err){
        console.log("Nahi mila users db");
      }
      else{
        console.log("connection Established:"+tableId);
        var cursor = db.collection('Order').find({"TableID":tableId,"OrderStatus":"In Progress"});
		cursor.forEach(function(doc, err){
          if(err){
            console.log("Code fata in collection");
			errorList.push(err);
          }
          else{
			resultArray.push(doc);
			orderId = doc._id;
            console.log("id is-->"+orderId);
          }
        }, function(){

		  if(errorList){
			nodeJSServerResponse.errorList["Errors"]=errorList;
		  }		  
		  if(resultArray){
				nodeJSServerResponse.isSuccess = true;
				nodeJSServerResponse.resultArray["Order"]= resultArray;
		  }		  
          db.close();
          console.log('Connection Ended.');
          //checking for errors
		  console.log(nodeJSServerResponse);
		  res.send(nodeJSServerResponse);
        });
        }
      });
     });
   
 //END function to getOrderDetails
   
 //START function to getOrderLineDetails
   
app.post('/getOrderLineDetails', function(req, res, next){
	console.log("Inside getOrderLineDetails method");

	var nodeJSServerResponse = {
		"isSuccess":"false",
		"errorList":{},
		"resultArray":{}
	}; 
	
	var body = req.body;
	var orderId = JSON.parse(body.orderId);
	var mode = JSON.parse(body.mode);
	
	var MongoClient = mongodb.MongoClient;
     
     var url = 'mongodb://localhost:27017/testHotel';
     //console.log(req.params);

     MongoClient.connect(url, function(err, db){
		var errorList = [];
		var resultArray = [];

	 if(err){
        console.log("Nahi mila users db");
      }
      else{
        console.log("connection Established:"+tableId);
        var cursor = db.collection('OrderLineItem').find({"OrderID":orderId});
		cursor.forEach(function(doc, err){
          if(err){
            console.log("Code fata in collection");
			errorList.push(err);
          }
          else{
			resultArray.push(doc);
          }
        }, function(){

		  if(errorList){
			nodeJSServerResponse.errorList["Errors"]=errorList;
		  }		  
		  if(resultArray){
				nodeJSServerResponse.isSuccess = true;
				nodeJSServerResponse.resultArray["OrderLineItems"]= resultArray;
		  }		  
          db.close();
          console.log('Connection Ended.');
          //checking for errors
		  console.log(nodeJSServerResponse);
		  res.send(nodeJSServerResponse);
        });
        }
      });
     });
   
 //END function to getOrderLineDetails
   
   
   

var server = app.listen(3000, function () {

  var host = 'localhost'
  var port = server.address().port

  console.log("Example app listening at http://%s:%s", host, port)
})