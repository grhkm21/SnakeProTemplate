x=$@
if [[ $x = "-all" ]]; then
	echo "Executing all tests.";
	x="BoardCellTest SnakeProDataTest_AdvancePede SnakeProDataTest_CellInDir SnakeProDataTest_GetNextCellFromBFS SnakeProDataTest_Neighbors SnakeProDataTest_Reverse";
fi

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
	echo "Try running \`./test.sh [file1] [file2] ...\` where the files are one of the following:"
	x=`ls test/*.class`;
	for file in $x; do
		prefix="test/";
		suffix=".class";
		foo=${file#"$prefix"};
		foo=${foo%"$suffix"}
		echo "${foo}";
	done
fi

