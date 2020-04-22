#!/bin/bash

sbt fullOptJS

cd ./target/scala-2.11/classes/
echo '{}' > composer.json
echo '<?php include_once("home.html"); ?>' > index.php
mv index.html home.html
cp ../tic-tac-toe-opt.js ./js/
git add .
git commit -am "heroku"
git push heroku master -f

