echo "#!/bin/sh\nexec java -jar \$0 \"\$@\" > /dev/null" > exec;
cat SnakePro.jar >> exec;
chmod +x exec;
