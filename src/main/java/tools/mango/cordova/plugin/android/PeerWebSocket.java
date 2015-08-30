/**
 * Created by jbblanc on 21/07/15.
 */

package tools.mango.cordova.plugin.android;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

public class PeerWebSocket extends WebSocketServer {

    public PeerWebSocket( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    public PeerWebSocket( InetSocketAddress address ) {
        super( address );
    }



    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        System.out.println( conn + " has left the room!" );
    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
        this.sendToAll( message );
        System.out.println( conn + ": " + message );
    }


    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text
     *            The String to send across the network.
     * @throws InterruptedException
     *             When socket related I/O errors occur.
     */
    public void sendToAll( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }
}
