cd ~/Downloads/GarethSnakePro/build
rm -rf */*.class
rm SnakePro.jar
javac -d . ../src/main/java/*/*.java
jar -cvf SnakePro.jar */*.class resources/*
javac -d test -cp $(printf '"'; for i in *; do printf $i:; done; printf '"') ../src/test/java/*.java
java -cp SnakePro.jar Controller.SnakeProBrain
