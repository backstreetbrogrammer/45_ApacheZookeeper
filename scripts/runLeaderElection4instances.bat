@ECHO OFF

set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202
set PWD=%cd%
set CLASSPATH="%PWD%\..\target\ApacheZookeeper-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo "====== START ALL ======"

cd "%PWD%"

REM 1. Run `LeaderElection`
start runLeaderElection.bat

REM wait for 1 seconds
timeout /t 1 /nobreak >nul

REM 2. Run `LeaderElection`
start runLeaderElection.bat

REM 3. Run `LeaderElection`
start runLeaderElection.bat

REM 4. Run `LeaderElection`
start runLeaderElection.bat
