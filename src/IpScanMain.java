import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;  

/**
 * @author 
 * @version 1.0
 * <h1>IpScanMain</h1>
 * IpScanMain is a class to provide scanning of 
 * ip addresses in the network. This class uses
 * ipv4 
 */
public class IpScanMain extends SwingWorker<Void, Node>{	
	public int startIp; 
	public int endIp;
	public String subnet;
	public int ipRange;
	private JTable table;
	private DefaultTableModel model;
	public List<Node> allNodes;
	private int hostNum = 0;
	public Future<Node> result = null;
	static final String ARP_CMD = "arp -a";
	public final static int NUM_OF_THREAD_IP = 254;
	public List<Future<Node>> resultList = new ArrayList<>();
	public ExecutorService servicePool = Executors.newFixedThreadPool(NUM_OF_THREAD_IP);
	static final String mac_regex = "\\s{0,}([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})";
	private JProgressBar pb;
    public int progress = 5;
	
	/**
	 * Constructor for IpScanMain: sets subnet
	 * start and end ip. 
	 * @param ip
	 */
	public IpScanMain(String ip, JTable table, JProgressBar pb) {
		if(ip.equals(null) || ip.equals("")) {
			System.out.println("Error: no ip address!!");
			return;
		}
		if(new IpMacUtils().validateIpAddr(ip)) {
			subnet = ip.substring(0, ip.lastIndexOf('.'));
		}
		startIp = 1; endIp = 2;
		this.table = table;
		this.pb = pb;
		
		
	}	
	
	/**
	 * this method starts the threads responsible
	 * for scanning each individual host in network
	 * @throws IOException 
	 */
	@Override
	protected Void doInBackground() throws Exception {
		model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); //clear table content
		allNodes = new ArrayList<Node>();
		allNodes.clear();
		
		for(int i = 1; i < NUM_OF_THREAD_IP + 1; i++) {
			IpScanCallable ipScan = new IpScanCallable(subnet, startIp, endIp);
			result = servicePool.submit(ipScan);
			resultList.add(result);	
			startIp = endIp; endIp++;
		}
		//sleeping for 3sec	
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(result.isDone()) { //print result
		  System.out.printf("%-17s%-17s%-17s\n","IP Address","Hostname","MAC");
		  for(Future<Node> future : resultList) { 
			  try { 
				  if(future.get() != null) {
					  Node node = future.get(); allNodes.add(node);
					  node.setMac(getArpTable(node.getIp()));
					  hostNum++;
					  PortScanMain pscan = new PortScanMain(node, table, hostNum, pb);
					  pscan.execute();
					  publish(node);
				  } 
				} catch (InterruptedException | ExecutionException | IOException e) { 
					e.printStackTrace(); 
				} 
		  	 } 
		  }
		  
		return null;
	}	
	
	
	@Override
	protected void process(List<Node> chunks) {
		for(Node node : chunks) {
			pb.setValue(progress++);
			Object[] row = {hostNum+"", node.getIp(), node.getHostName(), node.getMac()};
			model.addRow(row);			
		}
		table.setModel(model);
		
	}

	@Override
	protected void done() {		
		pb.setValue(40);
		servicePool.shutdown(); 
	}	
	

	/**
	 * this method get the mac address for corresponding ip
	 * fron arp table in localhost. Using apr -a command
	 * @param ipAddr
	 * @return
	 * @throws IOException
	 */
	public String getArpTable(String ipAddr) throws IOException {
        Scanner s = new Scanner(Runtime.getRuntime().exec(ARP_CMD +" "+ ipAddr).getInputStream()).useDelimiter("\\A");
        String sysInput = s.next();
        String mac = "";
        Pattern pattern = Pattern.compile(mac_regex);
        Matcher matcher = pattern.matcher(sysInput);
        if (matcher.find()) {
            mac = mac + matcher.group().replaceAll("\\-", ":");
        } 
        return mac.strip();
	}	

}
