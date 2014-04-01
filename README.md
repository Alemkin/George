George is an indie game under development! More to come soon or eventually!

TO DO:
* Find a better name than George
* Nail down the basic mechanics
* Build game engine
* Lots of storyline work, art, maps, etc.
* Add in FPS elements
* Add in action elements
* Add in MMO elements
* Add in RPG elements
* Guns
* Make many players very happy!

HOW TO BUILD
Have the lwjgl jar files in the lib/ folder, and any needed native libraries (.dll, .so, etc.) in native/.
To build, use the commmand
    javac -cp .:lib/* *.java 

To run/test, use the command
   java -cp .:lib/* -Djava.library.path=native/linux George

If you have the library files elsewhere, modify the commands as needed.
