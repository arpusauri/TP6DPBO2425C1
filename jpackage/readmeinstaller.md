# Step by step to make installer
- build project
- build artifacts (jar)
- create folder app_input that contains assets folder and jar (basically the program itself but not the project that includes class)
- run command

# Jpackage CLI (Multiple Lines)
jpackage --input <app_path> \
         --name "Flappy Bird" \
         --main-class App \
         --main-jar FlappyBirdv1.3.jar \
         --type exe \
         --app-version 1.3 \
         --dest <dest_path> \
         --win-shortcut \
         --add-modules java.base,java.desktop
	 --vendor "Coba Cobian" \
	 --icon <icon_path>

# Jpackage CLI (Single Line)
jpackage --input<app_path> --name "Flappy Bird" --main-class App --main-jar FlappyBirdv1.3.jar --type exe --app-version 1.3 --dest <dest_path> --win-shortcut --add-modules java.base,java.desktop --vendor "Coba Cobian" --icon <icon_path>

# Show Active Modules
jdeps --print-module-deps <jar_path>