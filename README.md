# Ex3_oopv1
In this project we use the infrastructure of the previous project- the weighted directed graph and the algorithms you can do on the graph , for more information click here: "https://github.com/ShovalTayro/Ex2_OOP/wiki".
In this project you can play a game on a weighted directed graph.
The game is similar to Pac-Man , you get the game information  from  the server (jar file included in this project) .
The game information include a weighted directed graph with fruits on him , and numbers of robots you have.
 we put robots on the graph and move them on the graph trying to collect as much fruits as we can with the robots, after eating a fruit , a new one will show up.
The communication with the server is done by reading information from the server using Json.
You can choose a scenario from the server , every scenario include weighted directed graph , fruits and their positions , num of robots.
After choosing the scenario we paint the scenario using stdDraw(for more information click here: https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html) and start the game. After the game finished you will get from the server the sum of points you achieved. 
In this project you got two options for play: manual and automatic for mor information about how it works click here: " https://github.com/ShovalTayro/Ex3_oopv1/wiki".
In this project you can also export the graph robots and fruits to KML file.
 for more about KML:  https://developers.google.com/kml/documentation/kml_tut , https://developers.google.com/kml/documentation/kmlreference
https://github.com/micromata/javaapiforkml .
Adiitionally for playing the game we add  a data base with some options , for example: you can see your stats compare to other competing, how much games you played in the game , your best result for every scenario in the data base(levels 0,1,3,5,9,11,13,16,19,20,23).
after playing you can choose  to see this information.


**The project was developed in JAVA using Eclipseworksplace.
The game(and the graph) implementation is based on an effective compact representation.**

