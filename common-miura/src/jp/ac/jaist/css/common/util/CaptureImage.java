package jp.ac.jaist.css.common.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CaptureImage {
	/**
	 * 引数に、コンポーネントを指定する
	 * @param c
	 */
	public static byte[] capture(Component c){
		BufferedImage bi = new BufferedImage(c.getWidth(),
				c.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics big = bi.getGraphics();
		c.paint(big);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
//			ImageIO.write(bi, "png", new File("samplecapt.png"));
			ImageIO.write(bi, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
		
	}
}
