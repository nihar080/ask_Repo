mkdir target 
SET CLASSPATH=%CLASSPATH%; ..\..\lib\*
set PATH=%PATH%;C:\Program Files\Java\jdk7\bin


javac -sourcepath src -d target src\com\SeleniumFramework\test\*.java

SET CLASSPATH=%CLASSPATH%; ..\..\target
java org.junit.runner.JUnitCore com.SeleniumFramework.test.DriverClass
TIMEOUT 20
sleep 10
start lib\HelperFiles\ReleaseProcesses.vbs
sleep 5
quit