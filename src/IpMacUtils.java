import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author George
 */
public class IpMacUtils {
	static Pattern pattern;
	static Matcher matcher;
	static final String mac_regex = "\\s{0,}([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})";	
	private static final String ip_regex = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
	
	/**
	 * @param ip
	 * @return
	 */
	public static boolean validateIpAddr(String ip) {
		pattern = Pattern.compile(ip_regex);
        matcher = pattern.matcher(ip);		
		return matcher.matches();
	}
	
	/**
	 * 
	 * @param mac
	 * @return
	 */
	public static boolean validateMacAddr(String mac) {
		pattern = Pattern.compile(mac_regex);
        matcher = pattern.matcher(mac);		
		return matcher.matches();
	}

}
