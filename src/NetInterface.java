import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 * class to handle network interface information
 * @author George
 */

public class NetInterface extends SwingWorker<Void, Void> {
	private ArrayList<Object> netAdapters;
	private JFrame frame;
	private static Map<String, ArrayList<String>> allnets;
	private JComboBox<Object> combo;
	private Enumeration<NetworkInterface> nets;
	
	/**
	 * 
	 * @param frame
	 * @param combo
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public NetInterface(JFrame frame, JComboBox combo) throws SocketException, UnknownHostException { 
		netAdapters = new ArrayList<>();
		allnets = new HashMap<>();
		this.frame = frame;
		this.combo = combo;		
	}
	
	@Override
	protected Void doInBackground() throws Exception {	
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
        	if(!netint.isVirtual() && netint.isUp() && !netint.isLoopback()) {
        		String name = netint.getName();
                netAdapters.add(name);
                allnets.put(name, getAdatapterInfo(netint));
                System.out.println(name +" "+netint.getDisplayName());
        	}        	
        }            
		return null;
	}	
	
	@Override
	protected void done() {			
		DefaultComboBoxModel<Object> model = (DefaultComboBoxModel<Object>) combo.getModel();
        model.addAll(netAdapters);
        model.setSelectedItem("Interfaces");        
		frame.getContentPane().add(combo);    
		
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JComboBox getComboBox() {
		return combo;
	}
	
	public static ArrayList<String> getInterface(String key) {
		return allnets.get(key);
	}

	/**
	 * collecting the names of all interfaces in computer
	 * @return obj
	 */
	public ArrayList<String> getAdatapterInfo(NetworkInterface netint) {
		ArrayList<String> netinfo = new ArrayList<String>();		
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("%s-%s\n", inetAddress.getHostAddress(), netint.getName());
            netinfo.add(inetAddress.getHostAddress()); 
        }	
		return netinfo;
	}
     

}
