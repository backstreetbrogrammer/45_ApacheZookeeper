@ECHO OFF

set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202
set PWD=%cd%
set CLASSPATH="%PWD%\..\target\ApacheZookeeper-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo "====== Run Leader Reelection ======"

cd "%PWD%\..\"

REM Run `LeaderReelection`
"%JAVA_HOME%\bin\java" -cp "%CLASSPATH%" -Xms128m -Xmx1024m com.backstreetbrogrammer.leader.election.LeaderReelection
