<<<<<<< HEAD
cd $(dirname "${BASH_SOURCE[0]}")
./compileGame.sh -;
=======
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48
echo "#!/bin/sh\nexec java -jar \$0 \"\$@\" > /dev/null" > exec;
cat SnakePro.jar >> exec;
chmod +x exec;
