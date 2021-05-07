cd $(cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd);
rm -rf */*.class;
rm SnakePro.jar;
javac -d . ../src/main/java/*/*.java;
jar -cvf SnakePro.jar */*.class resources/*;
javac -d test -cp $(printf '"'; for i in *; do printf $i:; done; printf '"') ../src/test/java/*.java;
if [[ -z "$@" ]]; then
	java -cp SnakePro.jar Controller.SnakeProBrain;
fi
