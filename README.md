<a href="https://ibb.co/sRLX9NS"><img src="https://i.ibb.co/kBYP2wn/all.png" alt="all" border="0" height=0></a>



# VISUALSEARCHTREE

![](https://i.ibb.co/kBYP2wn/all.png)

## Generalities
Visualsearchtree provides visualizations of search trees for constraint programming solvers executions. It is a visualization tool developed by researchers of the Catholic University of Louvain in Belgium in the context of a MOOC on constraint programming.


## Using Visualsearchtree

The Visualsearchtree profiler can be used in two ways:

- It can be included in a java project via its maven dependency ;
- It can be used as an external software that establishes a connection with a solver by the socket.

It allows a real-time visualization of the search while offering a view of the optimization function.

### As dependency

The installation process can be found on jitpack or on the github documentation.

1. Select the latest production version of the library. At the date of this edition, it is version 1.0.3. Then select the tab corresponding to the type of project you have created: maven for maven projects for example .

2. Add this block to the pom.xml file of our project.
```java
Add block code here
```

3. Add dependencies to our pom.xml using dependencies tags
```java
Add block code here
```

4. Create the module-info file.

5. Then import visualsearchtree and the graphics module from javafx

6. Export the project with the groupId of the project that you can find in the pom.xml of the project.

7. Launch a problem and have an interface as this below:
    - In the navigation bar you have several options. <br> <br>
      ![](https://i.ibb.co/wNGj5xz/menu.png)

    - Using the key I, we can display the information about a node. <br> <br>
      ![](https://i.ibb.co/pfhRp36/info.png)

    - The 0 key displays the optimization graph.  <br> <br>
      ![](https://i.ibb.co/BPjbGfx/opt.png)

    - To add a bookmark we can use the shortcut ctrl+B. And to display the bookmarks you can use the shortcut B.  <br> <br>
      ![](https://i.ibb.co/4RHx7mz/add-bookmark.png)

### As an external software