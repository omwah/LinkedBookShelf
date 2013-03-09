LinkedBookShelf
===============

A Bukkit plugin that allows bookshelves to be linked to adjacent blocks that contain 
an inventory. When a player right clicks on a bookshelf, if there is a block touching it
that has an inventory then that blocks inventory will be opened to the player.

This allows players to create usable bookshelves which can seemingly store items just
by placing, for instance, a chest behind a bookshelf.

This approach is simple and does not require the use of database to serialize objects
stored in a bookshelf. Additionally, if usage of the plugin were to be discontinued
on a server then the player's items are still safe in the chests the bookshelves
are linked with. Since the plugin does not touch the stored items, they are future
proof from loosing added meta data in future versions of Minecraft that might not
be explicitly handled by a plugin that would need to serialize inventory data.

Compilation
-----------

This plugin has a Maven 3 pom.xml and uses Maven to compile. Dependencies are 
therefore managed by Maven. You should be able to build it with Maven by running

    mvn package

a jar will be generated in the target folder. For those unfa1milliar with Maven
it is a build system, see http://maven.apache.org/ for more information.
