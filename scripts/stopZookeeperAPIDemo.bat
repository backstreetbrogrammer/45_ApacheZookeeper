@ECHO OFF

set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202
set PWD=%cd%
set CLASSPATH="%PWD%\..\target\ApacheZookeeper-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo "====== Stopping ZookeeperAPIDemo ======"

cd "%PWD%\..\"

REM Stop `LeaderElection`
for /f "tokens=1" %%i in ('jps -m ^| find "ZookeeperAPIDemo"') do ( taskkill /F /PID %%i )
