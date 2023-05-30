import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 * <h1>PortScanMain</h1>
 * this class is responsible for main logic of scanning
 * and detecting open ports in a host on the network
 * @author George
 * @version 1.0
 */
public class PortScanMain extends SwingWorker<Void, Integer>{	

	final int MAX_PORT = 10000;	
	public Scanner in;
	public String ip;
	public Future<Node> result = null;
	public int startPort = 0;
	final int TIME_OUT = 4000;
	private Node node;
	private JTable table;
	private int hostNum = 0;
	private DefaultTableModel model;
	public PortScanCallable pScan;		
	final int NUM_OF_THREAD = MAX_PORT; //divided by 4 b/c 4 processors so 256 threads 
	public ExecutorService servicePool = Executors.newFixedThreadPool(NUM_OF_THREAD);
	public List<Future<Node>> resultList = new ArrayList<>();
	public int endPort = startPort + (MAX_PORT/NUM_OF_THREAD);
	public int progress = 5;
	public JProgressBar pb;
	
	
	/**
	 * setter method to set ip address
	 * @param ip
	 */
	public PortScanMain(Node node, JTable table, int num, JProgressBar pb) {
		System.out.println("Scanning " + node.getIp());
		this.node = node;
		this.table = table;
		this.hostNum = num;
		this.pb = pb;
		this.pb.setValue(5);
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		model = (DefaultTableModel) table.getModel();
		for(int i = 0; i <= NUM_OF_THREAD/2; i++) { 
			PortScanCallable portScan = new PortScanCallable(node.getIp(), startPort, endPort); //scan 2 ports @time
			result = servicePool.submit(portScan); 
			resultList.add(result);
			startPort = endPort+1; 
			endPort = startPort + (MAX_PORT / NUM_OF_THREAD);
		}
				
		try {
			Thread.sleep(TIME_OUT);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}       
        
		//picking up results
		if(result.isDone()) {
			//print result
//			System.out.println(ip + " Open ports ");
			for(Future<Node> future : resultList) {
				for(Integer port: future.get().openPorts) {
					try {
						if(future.get() != null) {
//							node.openPorts.add(port);
							publish(port);
						}				
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			}						
		}
		return null;
	}

	@Override
	protected void process(List<Integer> chunks) {	
		for(Integer port : chunks) {
			node.openPorts.add(port);
			
		}	
		System.out.println(hostNum +" "+node.getIp() +" "+ node.getOpenPorts().toString());
		table.setValueAt(node.getOpenPorts().toString(), hostNum-1, 4);
	}

	@Override
	protected void done() {
		System.out.println(node.getIp()+ " port scan done!..");
		
	}
	
	/**
	 * this method will initiate the thread pool and send port ranges 
	 * to callable class to be scanned. then pause for 4 sec and
	 * finally display results from the scanned ip adress
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void startPortScanning() throws InterruptedException, ExecutionException {		
		servicePool.shutdown();
		try {
            servicePool.awaitTermination(3, TimeUnit.SECONDS);
            servicePool.shutdownNow();
        } catch (InterruptedException e) {
            
        }
	}	

}
