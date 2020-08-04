

@echo off
 
cd C:\Users\T Tanaka\Documents\_Written\Projects\JavaProject\NationalEconomy
echo compiling...
javac -d .\bin -encoding utf-8 .\NE\main\Main.java
echo compile finished

for /l %%n in (1,1,300) do (
    echo %%n
    java -classpath .\bin NE.main.Main
)

cmd /k