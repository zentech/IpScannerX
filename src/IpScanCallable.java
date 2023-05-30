import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * <h1>IpScanCallable</h1>
 * This class implement Callable interface to scan
 * ip address in a network. 
 * @author George
 * @version 1.0
 */
public class IpScanCallable implements Callable<Node> {
	private List<Node> results = new ArrayList();
	private static final String TAG = "IpScanRunnable";
	private final static int TIMEOUT = 1000;
	private static String ARP = "arp -a";
	private Node newNode;	
	private String subnet;
	private Integer startIp;
	private Integer stopIp;
	
	/**
	 * constructor for this class takes in subnet
	 * start and end ip for the range to be scan
	 * @param subnet
	 * @param start
	 * @param steps
	 */
	public IpScanCallable(String subnet, int start, int steps) {
	    this.subnet = subnet;
	    this.startIp = start;
	    this.stopIp = steps;	    
	}
	
	
	/**
	 * this method establish a socket connection
	 * to each ip address in the range.
	 * @return newNode	
	 */
	@Override
	public Node call() {				
	    for (int i = startIp; i < stopIp; i++) {
	        String ip = subnet + "." + i; 
	        try {
	        	System.out.println("trying " + ip);
	            InetAddress a = InetAddress.getByName(ip);
	            if (a.isReachable(TIMEOUT)) {
	            	System.out.println(ip +" is up!");
	            	newNode = new Node();
	    	        newNode.progressBar = i;
	            	newNode.ip = a.getHostAddress();
	            	newNode.HostName = a.getHostName();
	            	results.add(newNode);
	            }
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }          
	    }
	    return newNode;
	}
}
