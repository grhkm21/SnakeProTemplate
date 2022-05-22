cd $(dirname "${BASH_SOURCE[0]}");
./compileEverything.sh -;

x=$@;
if [[ $x = "-all" ]]; then
	echo "Executing all tests.";
	x=`ls test/*.class | sed -e "s/test\///" | sed -e "s/.class//"`;
fi
echo $x;

i=1;
for file in $x; do
	echo "-----------------------------------------";
	echo "Executing Test $i: $file";
	java -cp .:test:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore "$file"
	echo "-----------------------------------------";
	i=$((i + 1));
done

if [[ $i -eq 1 ]]; then
	if [[ $(ls test/*.class 2>/dev/null | wc -l) -eq 0 ]]; then
		./compileEverything.sh -;
	fi
	echo "Not sure what to do?";
	echo "Try running \`./test.sh -all\` or \`./test.sh [file1] [file2] ...\` where the files are one of the following:"
	x=`ls test/*.class`;
	for file in $x; do
		prefix="test/";
		suffix=".class";
		foo=${file#"$prefix"};
		foo=${foo%"$suffix"}
		echo "${foo}";
	done
fi

