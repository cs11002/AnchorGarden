package jp.ac.jaist.css.common.io;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**
 * SerObjManager
 * �V���A���C�Y�f�[�^�̕ۑ��E�Ǎ����T�|�[�g����N���X
 * 
 * �g�����F
 * �A�v���P�[�V�����i�����ł�ZoomPanelTest�j�ɁC
 * �V���A���C�Y�I�u�W�F�N�g�}�l�[�W���������o�[�Ƃ��ĉ�����
 * 
 * ���̃V���A���C�Y�I�u�W�F�N�g�}�l�[�W��(som)�𐶐�����ہC
 * SerializedObjectSupport�����������N���X�̃I�u�W�F�N�g
 * �i�����ł̓A�v���P�[�V�����FZoomPanelTest�j�������Ƃ��ēn���D
 * 
 * �t�@�C���Ƃ̕ۑ��E�Ǎ���som.save(), som.load()
 * Web�Ƃ̕ۑ��C�Ǎ���som.saveToHTTP(),som.openWebLoader()�����ꂼ��Ăяo��
 */

@SuppressWarnings("serial")
public class SerObjManager extends JPanel {
	SerializedObjectSupport sosapp;
	public SerObjManager(SerializedObjectSupport sos){
		sosapp = sos;
	}
	public void load(){
		// �t�@�C���_�C�A���O���J��
		String cdir = System.getProperty("user.dir");
		JFileChooser fd = new JFileChooser(cdir);
		NullFileFilter nff = new NullFileFilter(".ser");
		fd.setFileFilter(nff);
		int returnval = fd.showOpenDialog(sosapp.getFrame());
		if (returnval == JFileChooser.APPROVE_OPTION){
			// �g���q�����Ă��Ȃ��Ƃ��C�ނ������
			StringBuffer path = new StringBuffer(fd.getSelectedFile().getAbsolutePath());
			if (!path.toString().endsWith(".ser")){
				path.append(".ser");
			}
			load(path.toString());
		}else{
			return;
		}
	}
	public void load(String path){
		byte[] ba = loadFromFile(path);
		sosapp.byteSerializeImport(ba);
	}
	public void openWebLoader(){
		StringBuffer url;
		url = new StringBuffer("http://css.jaist.ac.jp/~miuramo/javaobjrepos/list.php?");
		url.append("user="+System.getProperty("user.name"));
		url.append("&sysname="+sosapp.getClass().getName());
		new WebReposBrowser(this, url.toString());
	}
	public void loadFromHTTP(String url){
		byte[] ba = loadFromURL(url);
		sosapp.byteSerializeImport(ba);
	}
	
	/**
	 * �t�@�C���֕ۑ�
	 */
	public void save(){
		String filepath;
		// �t�@�C���ۑ��_�C�A���O���J��
		String cdir = System.getProperty("user.dir");
		JFileChooser fd = new JFileChooser(cdir);
		NullFileFilter nff = new NullFileFilter(".ser");
		fd.setFileFilter(nff);
		int returnval = fd.showSaveDialog(sosapp.getFrame());
		if (returnval == JFileChooser.APPROVE_OPTION){
			// �g���q�����Ă��Ȃ��Ƃ��C�ނ������
			StringBuffer path = new StringBuffer(fd.getSelectedFile().getAbsolutePath());
			if (!path.toString().endsWith(".ser")){
				path.append(".ser");
			}
			filepath = path.toString();
		}else{
			return;
		}
		
		OutputStream fos = null;
		DataOutputStream dos = null;
		try{
			fos = new FileOutputStream(filepath);
			dos = new DataOutputStream(fos);
			byte[] ba = sosapp.byteSerializeExport();
			dos.write(ba);
			dos.close();
			fos.close();
		}catch(IOException iex){
			iex.printStackTrace(System.out);
		}
	}
	public void saveToHTTP(){
		
		BufferedImage bi = new BufferedImage(sosapp.getWidth(),
				sosapp.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.setClip(0,0,sosapp.getWidth(),sosapp.getHeight());
		sosapp.paintComponent(g);
		ByteArrayOutputStream imgbaos = new ByteArrayOutputStream();
		try{
			ImageIO.write(bi, "PNG", imgbaos);
		} catch(IOException ex){
			System.out.println("can't capture");
		}
		byte[] imgba = imgbaos.toByteArray();
		imgba = getResizedImageBytes(imgba, 100, "hoge.png");
		
		String title = (String)JOptionPane.showInputDialog(
				sosapp.getFrame(),
				"Input Title:", "Save To Web",
				JOptionPane.PLAIN_MESSAGE);
		if (title == null || title.length()==0){
			return;
		}
		
		PostMethod post = new PostMethod("http://css.jaist.ac.jp/~miuramo/javaobjrepos/postobj.php");
		try{
			byte[] ba = sosapp.byteSerializeExport();
			PartSource objbyteps = new ByteArrayPartSource("hoge.ser", ba);
			PartSource imgbyteps = new ByteArrayPartSource("hoge.png", imgba);
			
			Date d = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//���ɓ��t��note�ɂ���Ӗ��͂Ȃ��D
			Part[] parts={ new StringPart("sysname",sosapp.getClass().getName(),"UTF-8"),
					new StringPart("user",System.getProperty("user.name"),"UTF-8"),
					new StringPart("title",title,"UTF-8"),
					new StringPart("note",df.format(d),"UTF-8"),
					new StringPart("digest",MyDigest.getBytesDigest(ba),"UTF-8"),
					new FilePart("obj", objbyteps),
					new FilePart("img", imgbyteps)};
			post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
			
			HttpClient client = new HttpClient();
			int status = client.executeMethod(post);
			if (status == 200){
				JOptionPane.showMessageDialog(sosapp.getFrame(),"�f�[�^��Web�ۑ����������܂���.","Status",JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(sosapp.getFrame(),"�f�[�^��Web�ۑ��Ɏ��s���܂���.","Status",JOptionPane.ERROR_MESSAGE);
			}
			System.out.println(status);
			
			InputStream in = post.getResponseBodyAsStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final int LS = 1024;
			int b;
			byte buf[] = new byte[LS];
			while((b = in.read(buf, 0, buf.length)) != -1 ) {
				baos.write(buf,0,b) ;
			}
			baos.close();
			byte[] responseBody = baos.toByteArray();
			System.out.println(new String(responseBody));
		} catch(IOException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();			
		} finally {
			post.releaseConnection();
		}
	}
	public byte[] loadFromFile(String path){
		BufferedInputStream in;
		ByteArrayOutputStream varyBuf = new ByteArrayOutputStream();
		final int LS = 1024;
		int b;
		try {
			in = new BufferedInputStream(new FileInputStream(path));
			byte buf[] = new byte[LS];
			while((b = in.read(buf, 0, buf.length)) != -1 ) {
				varyBuf.write(buf,0,b) ;
			}
			varyBuf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("load size: "+varyBuf.size() + " bytes.");
		return varyBuf.toByteArray();
	}
	public byte[] loadFromURL(String url){
		GetMethod get = new GetMethod(url);
		try{
			HttpClient client = new HttpClient();
			int status = client.executeMethod(get);
			InputStream in = get.getResponseBodyAsStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final int LS = 1024;
			int b;
			byte buf[] = new byte[LS];
			while((b = in.read(buf, 0, buf.length)) != -1 ) {
				baos.write(buf,0,b) ;
			}
			baos.close();
			if (status == 200){
				JOptionPane.showMessageDialog(sosapp.getFrame(),"Web����̃f�[�^�Ǎ��݂ɐ������܂���.","Status",JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(sosapp.getFrame(),"Web����̃f�[�^�Ǎ��݂Ɏ��s���܂���.","Status",JOptionPane.ERROR_MESSAGE);
			}
			System.out.println(status);
			return baos.toByteArray();
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			get.releaseConnection();			
		}
		return null;
	}
	
	byte[] getResizedImageBytes(byte[] src, int width, String outfilename){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.createImage(src);
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(img,0);
		try{
			mt.waitForAll();
		}catch(Exception e){
		}
		int srcw = img.getWidth(this);
		int srch = img.getHeight(this);
		
		int height = (int)(((float)srch*(float)width)/(float)srcw);
		ImageFilter ifilter = new AreaAveragingScaleFilter(width, height);
		FilteredImageSource fis = new FilteredImageSource(img.getSource(), ifilter);
		Image target = tk.createImage(fis);
		mt.addImage(target,0);
		try{
			mt.waitForAll();
		}catch(Exception e){
		}
		int tarw = target.getWidth(this);
		int tarh = target.getHeight(this);
		
		BufferedImage bi = new BufferedImage(tarw,tarh,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.setClip(0,0,tarw, tarh);
		g.drawImage(target, 0,0,this);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			String prefix = outfilename.toLowerCase();
			if (prefix.endsWith(".png")){
				ImageIO.write(bi, "PNG", baos);
			} else if (prefix.endsWith(".jpg") || prefix.endsWith(".jpeg")){
				ImageIO.write(bi, "JPEG", baos);
			}
		} catch(IOException ex){
			System.out.println("can't capture");
		}    
		return baos.toByteArray();
	}
}
