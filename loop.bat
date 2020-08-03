

@echo off
 
for /l %%n in (1,1,300) do (
    echo %%n
    cd C:\Users\T Tanaka\Documents\_Written\Projects\JavaProject\NationalEconomy
    java -classpath .\bin NE.main.Main
 
)

cmd /k