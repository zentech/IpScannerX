import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * <h1>PortScanCallable</h1>
 * This class scan range of ports for given ip
 * @author George
 * @version 1.0
 */
public class PortScanCallable implements Callable<Node>{
	
	List<Integer> portsResult;
	int startPort;
	int endPort;
	Node newNode;
	String ipAddress;
	int timeOut = 1000;
	
	/**
	 * Constructor for PortScanCallable. 
	 * @param ipAddress
	 * @param startPort
	 * @param endPort
	 */
	public PortScanCallable(String ipAddress, int startPort, int endPort) {
		this.ipAddress = ipAddress;
		this.startPort = startPort;
		this.endPort = endPort;
		newNode = new Node(ipAddress);
		portsResult = new ArrayList<Integer>();
		
	}

	/**
	 * this method establishes a socket connection
	 * to each port and if port is open, 
	 * then adds to result
	 * @return newNode
	 */
	@Override
	public Node call() throws Exception {
		for (int i = startPort; i <= endPort; i++) {
            try {
                //establishing connection to every port
                Socket socket = new Socket();
                SocketAddress address = new InetSocketAddress(ipAddress, i);
                socket.connect(address, timeOut);                
                if (socket.isConnected()) {
                	//System.out.println("CONNECTED: " + i);
                    socket.close();
                    newNode.openPorts.add(i);
                }
            } catch (UnknownHostException e) {
                //e.printStackTrace();
            } catch (SocketTimeoutException e) {
                //e.printStackTrace();
            } catch (ConnectException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
		
		return newNode;
	}

}
