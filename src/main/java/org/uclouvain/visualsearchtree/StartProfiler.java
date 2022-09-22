/**
 * <p>VisualSearchTree is a tool that provides graphical visualization when building CP search models.
 * It can be a support for solving Constraint Programming problems like NQeensPrune, Jobshop, ...
 * </p>
 * <br>
 * To use this software we have two approach:
 * <h1>1 - Socket approach</h1>
 * <p>
 *     You'll have to start a server by lauching jar file with ' java -jar visualsearchtree.jar'
 *     or using exec files of your OS.<br>
 *     From any file that implements your search logic, regardless of the development language,
 *     you can send through socket your search data to the available port. Thus the research tree
 *     will be built in real time. To see how to send data by socket refer to this
 *     {@link org.uclouvain.visualsearchtree.bridge.ConnectorTest example}.
 * </p>
 * <br>
 * <h1>1 - Java dependency approach</h1>
 * <p>
 *     You can include this tools in your java code using maven or gradle as describe in this documentation.
 *     Look at the {@link org.uclouvain.visualsearchtree.examples.NQueensPruneVisu NQueensExample}
 *     to see the java implementation.
 * </p>
 *
 * @author minicp
 * @version 1.0.0
 * @since 2022-09-22
 */
package org.uclouvain.visualsearchtree;

import org.uclouvain.visualsearchtree.server.Server;

import static javafx.application.Application.launch;

/**
 * <h1>Start a listening server</h1>
 * This methods Allows you to start a server. It listens on a default port 6650 and waits
 * for the profiling data to be sent to it via this port to proceed with the visualization
 * of the problem.
 * <p>
 * <b>Note:</b> The port will be displayed at the bottom of the launched interface and this port
 * is not necessarily 6650. If 6650 for one reason or another is busy, the server will be
 * launched on the one immediately available.</b>
 * </p>
 */
public class StartProfiler {
    /**
     * This is the main method which make use of StartServer
     * @param args default main method args
     */
    public static void main(String[] args) {
        launch(Server.class, args);
    }
}
