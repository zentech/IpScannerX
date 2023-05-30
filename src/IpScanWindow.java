import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import net.project.netadapter.IpMacUtils;
import net.project.netadapter.NetInterface;

import javax.swing.border.MatteBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JProgressBar;

public class IpScanWindow {

	private JFrame frame;
	private JTextField startIpLabel;
	private JTextField endIpLabel;
	private JTable table;
	private IpScanMain ipScan;
	private PortScanMain portScan;
	private List<Node> allNodes;
	private Object[][] data;
	private NetInterface netAdapter;
	private Map<String, String[]> allnets;
	private static final int COLUMNS = 4;
	private JTextField portScanIp;
	private InetAddress ia;
	private String hostname;
	public JProgressBar progressBar;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IpScanWindow window = new IpScanWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IpScanWindow() throws Exception {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */

	private void initialize()  {	
//		System.out.println("interfaces");
//    	netAdapter = new NetInterface();
//    	netAdapter.getAllNetAdapters();		
		
		frame = new JFrame();
		frame.setAlwaysOnTop(true);
		frame.getContentPane().setBackground(new Color(240, 240, 240));
		frame.setBounds(100, 100, 882, 577);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIpRange = new JLabel("From");
		lblIpRange.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblIpRange.setBounds(11, 37, 55, 14);
		frame.getContentPane().add(lblIpRange);
		
		startIpLabel = new JTextField();
		startIpLabel.setText("192.168.1.1");
		startIpLabel.setBounds(87, 36, 100, 20);
		frame.getContentPane().add(startIpLabel);
		startIpLabel.setColumns(10);
		
		endIpLabel = new JTextField();
		endIpLabel.setText("192.168.1.254");
		endIpLabel.setBounds(219, 36, 100, 20);
		frame.getContentPane().add(endIpLabel);
		endIpLabel.setColumns(10);		
		String[] columns = {"Number", "Ip Address", "Hostname", "Mac"};			    

		JButton scanButton = new JButton("Ip Scan");		
		scanButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		scanButton.setBounds(329, 35, 89, 23);
		frame.getContentPane().add(scanButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(11, 109, 845, 2);
		frame.getContentPane().add(separator);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBounds(10, 122, 846, 392);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		table.setBackground(Color.WHITE);
		table.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		table.setCellSelectionEnabled(true);
		table.setFillsViewportHeight(true);
		table.setColumnSelectionAllowed(true);
		table.setModel(new DefaultTableModel(
			new Object[][] { },
			new String[] {
				"Number", "Ip Address", "Hostname", "Mac", "Ports"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(1);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(250);
		
		scrollPane.setColumnHeaderView(table);
		scrollPane.setViewportView(table);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(240, 240, 240));
		menuBar.setBounds(10, 0, 849, 26);
		frame.getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmClose = new JMenuItem("Open File");
		mnFile.add(mntmClose);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mnFile.add(mntmSaveAs);
		
		JSeparator separator_2 = new JSeparator();
		mnFile.add(separator_2);
		
		JMenuItem mntmPrint = new JMenuItem("Print...");
		mnFile.add(mntmPrint);
		
		JSeparator separator_3 = new JSeparator();
		mnFile.add(separator_3);
		
		JMenuItem mntmProperties = new JMenuItem("Settings");
		mnFile.add(mntmProperties);
		
		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);
		
		JMenuItem mntmClose_1 = new JMenuItem("Exit");
		mnFile.add(mntmClose_1);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mnEdit.add(mntmPaste);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmGuide = new JMenuItem("Guide");
		mnHelp.add(mntmGuide);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		System.out.println(Thread.currentThread().getName());
		
		//combobox network interface stuff
		NetInterface netint = null;
		JLabel lblPortScan = new JLabel("Ip Address");
		lblPortScan.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPortScan.setBounds(11, 76, 68, 14);
		frame.getContentPane().add(lblPortScan);
		
		portScanIp = new JTextField();
		portScanIp.setColumns(10);
		portScanIp.setBounds(87, 75, 232, 20);
		portScanIp.setText("192.168.1.138");
		frame.getContentPane().add(portScanIp);
		
		JButton btnPortScan = new JButton("Port Scan");
		btnPortScan.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnPortScan.setBounds(329, 75, 89, 23);
		frame.getContentPane().add(btnPortScan);
		
		JLabel lblTo = new JLabel("to");
		lblTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTo.setBounds(195, 37, 27, 14);
		frame.getContentPane().add(lblTo);
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(563, 37, 293, 63);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblLocalIpv4 = new JLabel("");
		lblLocalIpv4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLocalIpv4.setBounds(10, 13, 136, 14);
		panel.add(lblLocalIpv4);
		
		JLabel localIpv6 = new JLabel("");
		localIpv6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		localIpv6.setBounds(10, 38, 198, 14);
		panel.add(localIpv6);
		
		JLabel localMac = new JLabel("");
		localMac.setFont(new Font("Tahoma", Font.PLAIN, 14));
		localMac.setBounds(156, 13, 127, 14);
		panel.add(localMac);
		
		JLabel hostnameTxt = new JLabel("");
		hostnameTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		hostnameTxt.setBounds(215, 38, 68, 14);
		panel.add(hostnameTxt);
		
		JComboBox ifaceComboBox = new JComboBox();
		ifaceComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ifaceComboBox.setModel(new DefaultComboBoxModel(new String[] {"Interfaces"}));
		ifaceComboBox.setBounds(442, 37, 100, 20);
		frame.getContentPane().add(ifaceComboBox);
        EventQueue.invokeLater(netint);
        
        try {
			netint = new NetInterface(frame, ifaceComboBox);			
			progressBar = new JProgressBar(0, 100);
			progressBar.setBounds(11, 514, 845, 16);
			progressBar.setValue(0);
			progressBar.setVisible(false); 
			frame.getContentPane().add(progressBar);	
			
			
		} catch (SocketException | UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        EventQueue.invokeLater(netint);
        
        
		scanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();		
				progressBar.setVisible(true);
				progressBar.setStringPainted(true);
				ipScan = new IpScanMain(startIpLabel.getText(), table, progressBar);				
				ipScan.execute();
			}	
		});
        
        btnPortScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				Node node = new Node(portScanIp.getText());				
				portScan = new PortScanMain(node, table, row+1);
				System.out.println(row+1);
				portScan.execute();	
			}	
		});
        
       
        
        try {
  	      ia = InetAddress.getLocalHost();
  	      hostname = ia.getHostName();
  	    }
        catch (Exception e1) {}
        
        ifaceComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostnameTxt.setText(hostname);
				JComboBox cb = (JComboBox)e.getSource();
		        String key = (String)cb.getSelectedItem();
		        ifaceComboBox.setSelectedItem(key);
		        System.out.println(hostname);
		        ArrayList<String> ifaceInfo = NetInterface.getInterface(key);
		        for(String ip : ifaceInfo) {
		        	if(IpMacUtils.validateIpAddr(ip)) {
		        		lblLocalIpv4.setText(ip);
		        		try {
							localMac.setText(getMacAddress(ip));
						} catch (SocketException e1) {
							e1.printStackTrace();
						}
		        	}	        		
		        	if(ip.contains("fe80"))
		        		localIpv6.setText(ip); System.out.println(ip);
		        }
			}
				
		});
	}
	
	
	
	public String getMacAddress(String ipAddr) throws SocketException {
		InetAddress localIP;
		byte[] macAddress = null;
		try {
			localIP = InetAddress.getByName(ipAddr);
			NetworkInterface ni = NetworkInterface.getByInetAddress(localIP);
			macAddress = ni.getHardwareAddress();			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] hexadecimal = new String[macAddress.length];
		for (int i = 0; i < macAddress.length; i++) {
		    hexadecimal[i] = String.format("%02X", macAddress[i]);
		}
		String mac = String.join("-", hexadecimal);
		System.out.println(mac);
		return mac;
		
	}
}
