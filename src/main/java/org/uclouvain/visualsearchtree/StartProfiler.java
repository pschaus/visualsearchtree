/**
 * <p>VisualSearchTree is a tool that provides graphical visualization when building CP search models.
 * It can be a support for solving Constraint Programming problems like NQeensPrune, Jobshop, ...
 * </p>
 *
 * @author minicp
 * @version 1.0
 * @since 2022-08-20
 */
package org.uclouvain.visualsearchtree;

import org.uclouvain.visualsearchtree.server.Server;

import static javafx.application.Application.launch;

/**
 * <h1>Start a listening server</h1>
 * This methods Allows you to start a server. It listens on a default port 6666 and waits
 * for the profiling data to be sent to it via this port to proceed with the visualization
 * of the problem.
 * <p>
 * <b>Note:</b> The port will be displayed at the bottom of the launched interface and this port
 * is not necessarily 6666. If 6666 for one reason or another is busy, the server will be
 * launched on the one immediately available.</b>
 * </p>
 */
public class StartProfiler {
    /**
     * This is the main method which make use of StartServer
     * @param args
     * @return Nothing
     */
    public static void main(String[] args) {
        launch(Server.class, args);
    }
}
