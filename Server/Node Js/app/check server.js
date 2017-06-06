var express = require('express');
var app = express();

// Get home Page
app.get('/lol', function(res, req){
  res.end('i ekc');
});


var server = app.listen(3002, function () { 
console.log("Example app listening at 3000");
});