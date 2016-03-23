# polyCode
AI driven programming language, compiling images to code and code to images.

Draw a simple polygon to test. _polyCode_ will enforce symmetries allowed by the preservation of structure, employing them to use loops during compilation. Compiled function will be displayed on the terminal, in the form “objectN(posX, posY, rotDeg)”, where N stands for the object's ID. All such functions may be called on.

Objects drawn by the moving “poly”,  are compiled in the same manner after detachment by trail-less movement, or change of location (“setLoc(posX, posY)” call).

Built-in commands:

penDown() -> movement leaves a trail

penUp() -> movement is trail-less

move(length) -> translates by length in current direction

rotate(degrees) -> rotates 

rename(oldName, newName) -> renames a compiled function

setLoc(posX, posY) -> sets location

setRot(degree) -> sets rotation by degrees

setRot(vecX,vecY) -> sets rotation by direction vector

setColor(color) -> sets drawing color

resetColor() -> resets to default color

setColorVertex(color) -> sets color of vertices 

printAll() -> prints all compiled functions

clear() -> clears the terminal

![demoImageToCode](/demos/demoImageToCode.gif?raw=true "demoImageToCode")
![demoImageToCode2](/demos/demoImageToCode2.gif?raw=true "demoImageToCode2")

