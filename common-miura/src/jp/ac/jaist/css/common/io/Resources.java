package jp.ac.jaist.css.common.io;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Resources {

	/**
	 * Do not construct an instance of this class.
	 */
	private Resources() {

	}

	/**
	 * Return the URL of the filename under the resources directory
	 */
	public static URL getResource(String filename) {
		URL url = Resources.class.getResource(filename); 
		if (url != null) return url;
		url = getURLFromFileOrURLName(filename);
		return url;
	}

	public static URL getURLFromFileOrURLName(String name) {
		try {
			return (new URL(name));
		} catch (MalformedURLException e) {
		}
		try {
			char sep = File.separator.charAt(0);
			String file = name.replace(sep, '/');
			if (file.charAt(0) != '/') {
				String dir = System.getProperty("user.dir");
				dir = dir.replace(sep, '/') + '/';
				if (dir.charAt(0) != '/') {
					dir = "/" + dir;
				}
				file = dir + file;
			}
			return (new URL("file", "", file));
		} catch (MalformedURLException e) {
			throw (new InternalError("can't convert from filename"));
		}
	}

}