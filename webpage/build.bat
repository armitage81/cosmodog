java -Dlog4j.configuration=file:templateengine/log4j.properties -jar templateengine/TemplateEngine.jar "html\templates\template.txt" "html\variables\variables.txt" "html\bigVariables" "html\output"
xcopy html\pics html\output\pics /e /i /h
copy html\index.html html\output\index.html