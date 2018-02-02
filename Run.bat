@echo off
set ANT_HOME=C:\Ant
REM set JAVA_HOME=C:\Program Files\Java\jdk7
set JAVA_HOME=C:\Program Files\Java\jdk7
set PATH=%PATH%;%ANT_HOME%\bin;%JAVA_HOME%\bin
call ant 
pause
exit