var mongodb = require("mongodb");
var nodemailer = require('nodemailer');
var random = require("node-random");
var ObjectId = require('mongodb').ObjectID;


module.exports= function(req, res, next){
	console.log("******inside postDoLogin*******");
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
			
			var readData = function(userDetails){
				console.log("******inside readData*******");
				db.collection('UserDetails').find({
						"username": userDetails.username,
						"password": userDetails.password
					}).toArray(function(err, lstUserDetails) {
						db.close();
						if (lstUserDetails.length!=0) {
							nodeJSServerResponse.isSuccess = true;
							nodeJSServerResponse.resultArray["lstUserDetails"] = lstUserDetails;
						}
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
					});
			}
			
			var createData = function(userDetails){
				console.log("******inside createData*******");
				db.collection('UserDetails').insert(userDetails, function(err, result) {
					if (result) {
						console.log("Inserted a document into the restaurants collection.");
						nodeJSServerResponse.isSuccess = true;
						nodeJSServerResponse.resultArray["lstUserDetails"] = result.ops;
						sendSignUpEmail(userDetails);
					}else{
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
					}
				});
			}

			var resetPassword = function(userDetails){
				console.log("******inside resetPassword*******");
				db.collection('UserDetails').find({
						"username": userDetails.username,
						"securityToken": userDetails.securityToken
					}).toArray(function(err, lstUserDetails) {
						if (lstUserDetails.length!=0) {
							db.collection('UserDetails').update({
								"_id": new ObjectId(lstUserDetails[0]._id)
							}, {
								$set: {
									"password": userDetails.password,
									"securityToken": ""
								}
							}, function(err, res2) {
								if (res2) {
									console.log("updated a userDetails into the restaurants collection with token=" + userDetails.securityToken);
									nodeJSServerResponse.isSuccess = true;
								}else{
									console.log(err);
								}
								console.log(nodeJSServerResponse);
								console.log('Request Finished....Sending Headers...');
								db.close();
								res.send(nodeJSServerResponse);
							});
						}else{
							nodeJSServerResponse.errorList.push("We could not find any user with the username and the provided security token. Kindly reset it again.");
							console.log(nodeJSServerResponse);
							console.log('Request Finished....Sending Headers...');
							res.send(nodeJSServerResponse);
						}
					});



			}
			
			var forgotPassword = function(userDetails){
					db.collection('UserDetails').find({
						"username": userDetails.username
					}).toArray(function(err, lstUserDetails) {
						if (lstUserDetails.length!=0) {
							// Get 20 different lowercase characters 
							random.strings({
								"length": 1,
								"number": 5,
								"upper": false,
								"digits": true
							}, function(error, data) {
							   if (error) throw error;
							   var randomString ='';
							   for(var i=0;i<data.length;i++){
								   randomString+=data[i];
							   }
							   console.log("UserId:"+lstUserDetails[0]._id);
							   sendResetPasswordEmail(userDetails.username,"Reset Your Password",randomString,lstUserDetails[0]._id);
							});
						}else{
							nodeJSServerResponse.errorList.push("We could not find any existing user details with the username provided. kindly provide a valid username or create a new user.");
							console.log(nodeJSServerResponse);
							console.log('Request Finished....Sending Headers...');
							res.send(nodeJSServerResponse);
						}
					});
			}
			
			var sendResetPasswordEmail = function(to,subject,securityToken,userId){
				var transporter = nodemailer.createTransport({
				  service: 'gmail',
				  auth: {
					user: 'aichelles1@gmail.com',
					pass: 'JadmuKadmu577268'
				  }
				});

				var mailOptions = {
				  from: 'aichelles1@gmail.com',
				  to: to,
				  subject: subject,
				  text: "Enter this code:"+securityToken
				};

				transporter.sendMail(mailOptions, function(error, info){
				  if (error) {
						nodeJSServerResponse.errorList.push("Sorry, The server is not able to send a resest password mail. Please try again.");
						console.log(nodeJSServerResponse);
						console.log('Request Finished....Sending Headers...');
						res.send(nodeJSServerResponse);
				  } else {
							console.log('Email sent: ' + info.response);
							db.collection('UserDetails').update({
								"_id": new ObjectId(userId)
							}, {
								$set: {
									"securityToken": securityToken
								}
							}, function(err, res2) {
								if (res2) {
									console.log("updated a userDetails into the restaurants collection with token=" + securityToken);
									nodeJSServerResponse.isSuccess = true;
								}else{
									console.log(err);
								}
								
								console.log(nodeJSServerResponse);
								console.log('Request Finished....Sending Headers...');
								db.close();
								res.send(nodeJSServerResponse);
							});
					}
				});
			}
			
			var sendSignUpEmail = function(userDetails){
				var transporter = nodemailer.createTransport({
				  service: 'gmail',
				  auth: {
					user: 'aichelles1@gmail.com',
					pass: 'JadmuKadmu577268'
				  }
				});

				var mailOptions = {
				  from: 'aichelles1@gmail.com',
				  to: userDetails.username,
				  subject: "Welcome User!!!",
				  text: "Hi User,<br/>Welcome to Hotel Management App.<br/>Thanks,<br/>HM Team."
				};

				transporter.sendMail(mailOptions, function(error, info){
				   if (error) {
						nodeJSServerResponse.errorList.push("Sorry, The server is not able to send a resest password mail. Please try again.");
					} else {
						console.log('Email sent: ' + info.response);
						nodeJSServerResponse.isSuccess=true;
					}
					console.log(nodeJSServerResponse);
					console.log('Request Finished....Sending Headers...');
					res.send(nodeJSServerResponse);
				});
			}			
			
			
			console.log("Database connection Established");
			var body = req.body;
			console.log("Requested Parametes are:-");
			console.log("userDetails="+body.userDetails);
			var userDetails = JSON.parse(body.userDetails);
			console.log("mode="+body.mode);
			var mode = JSON.parse(body.mode);
			if(mode=="READ"){
				readData(userDetails);
			}else if(mode=="FORGOTPASSWORD"){
				forgotPassword(userDetails);
			}else if(mode=="RESETPASSWORD"){
				resetPassword(userDetails);
			}else{
				createData(userDetails);
			}
		}
    });
}