var {
  google
} = require('googleapis');
var MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
var SCOPES = [MESSAGING_SCOPE];

const express = require('express')
const path = require('path')
const PORT = process.env.PORT || 5000


var bodyParser = require('body-parser');
var router = express.Router();
var request = require('request');


var app = express();
app.use(express.static(path.join(__dirname, 'public')))
  .set('views', path.join(__dirname, 'views'))
  .set('view engine', 'ejs')
  .get('/', (req, res) => res.render('pages/index'))
  .listen(PORT, () => console.log(`Listening on ${ PORT }`))

app.use(bodyParser.urlencoded({
  extended: true
}));

app.use(bodyParser.json());

router.post('/send', function (req, res) {

  getAccessToken().then(function (access_token) {

      var title = req.body.title;
      var body = req.body.body;
      var token = req.body.token;

      request.post({
          headers: {
              Authorization: 'Bearer ' + access_token
          },
          url: "https://fcm.googleapis.com/v1/projects/portfolio-manager-256c6/messages:send",
          body: JSON.stringify({
              "message": {
                  "token": token,
                  "notification": {
                      "body": body,
                      "title": title,
                  }
              }
          })
      }, function (error, response, body) {
          res.end(body);
          console.log(body);
      });
  });
});

app.use('/api', router);

// app.listen(port, function() {
//   console.log('Server is running... ' + port);
// })

function getAccessToken() {
  return new Promise(function (resolve, reject) {
      var key = require("./serviceAccountKey.json");
      var jwtClient = new google.auth.JWT(
          key.client_email,
          null,
          key.private_key,
          SCOPES,
          null
      );
      jwtClient.authorize(function (err, tokens) {
          if (err) {
              reject(err);
              return;
          }
          resolve(tokens.access_token);
      });
  });
}