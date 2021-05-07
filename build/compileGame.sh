<<<<<<< HEAD
cd $(dirname ${BASH_SOURCE[0]});
=======
cd $(cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd);
>>>>>>> c5db623507a54dfd6f2f5b96674b81acb913cbe4
rm -rf */*.class;
rm SnakePro.jar;
javac -d . ../src/main/java/*/*.java;
jar -cvf SnakePro.jar */*.class resources/*;
if [[ -z "$@" ]]; then
	java -cp SnakePro.jar Controller.SnakeProBrain;
fi
