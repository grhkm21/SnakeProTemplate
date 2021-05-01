rm -rf */*.class
rm SnakePro.jar
javac -d . ../src/main/java/*/*.java
jar -cvf SnakePro.jar */*.class resources/*
java -cp SnakePro.jar Controller.SnakeProBrain
