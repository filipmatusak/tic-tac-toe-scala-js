#!/bin/bash

sbt clean fullOptJS

cd ./target/scala-2.11/classes/

git init
heroku git:remote -a tic-tac-toe-scala-js

echo '{}' > composer.json
echo '<?php include_once("home.html"); ?>' > index.php
echo 'tic_tac_toe' > .gitignore
mv index.html home.html

cp ../tic-tac-toe-opt.js ./js/

git add .
git commit -am "heroku"
git push heroku master -f

