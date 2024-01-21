@ECHO OFF

set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202
set PWD=%cd%
set CLASSPATH="%PWD%\..\target\ApacheZookeeper-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo "====== START ALL ======"

cd "%PWD%"

REM 1. Run `LeaderReelection`
start runLeaderReelection.bat

REM wait for 1 seconds
timeout /t 1 /nobreak >nul

REM 2. Run `LeaderReelection`
start runLeaderReelection.bat

REM 3. Run `LeaderReelection`
start runLeaderReelection.bat

REM 4. Run `LeaderReelection`
start runLeaderReelection.bat
