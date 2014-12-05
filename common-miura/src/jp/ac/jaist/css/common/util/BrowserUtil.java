package jp.ac.jaist.css.common.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class BrowserUtil {
	public static boolean openURL(String url){
		String javaversion = System.getProperty("java.runtime.version");
		System.out.println("Java version "+javaversion);
		if (Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI(url));
				System.out.println("Desktop Support");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			Runtime runtime = Runtime.getRuntime();
			try{
				runtime.exec("cmd.exe /c start " + url);
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return false;
	}
}
