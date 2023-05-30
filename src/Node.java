
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 2/14/17.
 */

public class Node {
    String ip;
    String mac;
    String CanonicalHostName;
    String HostName;
    String remark;
    boolean isReachable;
    int progressBar;
    List<Integer> openPorts;
    int hostNum;

    public Node() {
    	this.openPorts = new ArrayList<Integer>();
    }

    Node(String ip){
        this.ip = ip;
        openPorts = new ArrayList<Integer>();
    }

    public void setProgressBar(int progressBar) {
        this.progressBar = progressBar;
    }

    public String getIp() {
        return ip;
    }

    public String getHostName() {
        return HostName;
    }

    public String getMac() {
        return mac;
    }    

    public void setMac(String mac) {
		this.mac = mac;
	}

	public boolean isReachable() {
        return isReachable;
    }
    
    public List<Integer> getOpenPorts() {
    	return openPorts;
    }    
    

    @Override
    public String toString() {
        return "IP: " + ip + "\n" +
                "MAC: " + mac + "\n" +
                "CanonicalHostName:\t" + CanonicalHostName + "\n" +
                "HostName:\t" + HostName + "\n" +
                "\n" + remark;
    }
}
