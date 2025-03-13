I welcome everyone to my first repository.
This src code allows you to warn the player if the player has entered the server from a version where there may be significant problems, from visual to technical, which can lead to a crash from the game.

I strongly recommend that you do not use this source code unless you have a proxy server
In the event that you are already using a plugin that uses titles at the beginning of the game, then problems may arise

Installation tutorial

1. Src was targeted and works on version 1.21, Java version 21
2. Make sure to install the required library
Maven: "
<repository>
<id>viaversion-repo</id>
<url>https://repo.viaversion.com</url>
</repository>
-------------------------------
<dependency>
<groupId>com.viaversion</groupId>
<artifactId>viaversion-api</artifactId>
<version>check latest</version>
<scope>provided</scope>
</dependency>
-------------------------------
