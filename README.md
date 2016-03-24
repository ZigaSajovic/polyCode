# polyCode
AI driven programming language, compiling images to code and code to images.

Draw a simple polygon to test; _polyCode_ enforces symmetries in the image, while preserving it's initial structure, employing them for the usage of loops during compilation. Compiled functions are displayed in the terminal, in the form _objectN(posX, posY, rotDeg)_, where N stands for the object's ID. All such functions may be called on.

Objects drawn by the moving _poly_,  are compiled in the same manner after detachment by trail-less movement, or change of location (_setLoc(posX, posY)_ call).

####Built-in commands:

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

###The mechanism 

_polyCode_ employs the author's ongoing research on dynamic relational nets in it’s most basic form, where the _pullback-space on one-forms_  and the _vertex-space_ both contain only one element, namely the linear function and the identity operator. This model allows simple modeling of embedded graphs.

By defining appropriate equivalence relations, the set of all isomorphisms is constructed, generating all isomorphic objects under said relations. Thus, constraints may be enforced upon these objects, while preserving their initial structure, as mapped by the isomorphisms.

The interpretation of shape as a distribution is used as a metric of similarity between such objects, allowing further classification within isomorphisms through convergence of characteristic functions.

###Demos

![demoImageToCode2](/demos/demoImageToCode2.gif?raw=true "demoImageToCode2")
![demoImageToCode](/demos/demoImageToCode.gif?raw=true "demoImageToCode")

<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">polyCode</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="https://si.linkedin.com/in/zigasajovic" property="cc:attributionName" rel="cc:attributionURL">Žiga Sajovic</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
