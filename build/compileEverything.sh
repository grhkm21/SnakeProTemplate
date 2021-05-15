cd $(dirname "${BASH_SOURCE[0]}");
rm -rf */*.class;
rm SnakePro.jar;
javac --release 8 -d . ../src/main/java/*/*.java;
jar cfm SnakePro.jar Manifest.txt */*.class resources/*;
javac --release 8 -d test -cp $(printf '"'; for i in *; do printf $i:; done; printf '"') ../src/test/java/*.java;
if [[ -z "$@" ]]; then
	java -jar SnakePro.jar;
fi
