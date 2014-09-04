package jp.ac.jaist.css.common.io;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.HeadMethod;

/**
 * TinyWebBrowser (modified by miura)
 * http://takagi-hiromitsu.jp/diary/20060701.html
 * @author Hiromitu Takagi
 *
 */

public class WebReposBrowser {
	SerObjManager sosman;
	String initialURL;
	JFrame f;
	
	public static void main(String[] args) {
		new WebReposBrowser("http://www.jaist.ac.jp/");
	}
	public WebReposBrowser(SerObjManager ser, String url){
		this(url);
		sosman = ser;
	}
	
	public WebReposBrowser(String url) {
		initialURL = url;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
		
	}
	protected String getDefaultPage() {
		return initialURL;
	}
	protected String getWindowTitle() {
		return "Web Repository Loader";
	}
	protected Dimension getDefaultWindowSize() {
		return new Dimension(350, 500);
	}
	JPanel browserPanel;
	JTextField addressField = new JTextField();
	JButton backButton = new JButton("Back");
	JButton reloadButton = new JButton("Reload");
	JLabel statusArea = new JLabel(" ");
	Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	Page currentPage = null;
	@SuppressWarnings("unchecked")
	java.util.Stack pageStack = new java.util.Stack();
//	static final Font font = new Font("SansSerif", Font.PLAIN, 10);
	private void createAndShowGUI() {
		/*        try {
		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 } catch (Exception e) {
		 }*/
		f = new JFrame(getWindowTitle());
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		browserPanel = new JPanel();
		browserPanel.setOpaque(true);
		browserPanel.setLayout(new BorderLayout());
		JPanel addressBar = new JPanel(new BorderLayout());
		JLabel l = new JLabel("URL: ");
//		l.setFont(font);
		addressBar.add(l, BorderLayout.LINE_START);
//		addressField.setFont(font);
		setAddress(getDefaultPage());
		addressField.addActionListener(new AddressEnterAction());
		addressBar.add(addressField, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		backButton.addActionListener(new BackButtonAction());
		backButton.setEnabled(false);
		buttonPanel.add(backButton);
		reloadButton.addActionListener(new ReloadButtonAction());
		buttonPanel.add(reloadButton);
//		addressBar.add(buttonPanel, BorderLayout.LINE_END);
		browserPanel.add(addressBar, BorderLayout.PAGE_START);
//		statusArea.setFont(font);
		browserPanel.add(statusArea, BorderLayout.PAGE_END);
		f.setContentPane(browserPanel);
		f.setSize(getDefaultWindowSize());
		
		// 元ウィンドウの位置を取得する
		Rectangle rec = sosman.sosapp.getFrame().getBounds();
		f.setLocation(rec.x + rec.width, rec.y);
		f.setVisible(true);
		openURL(initialURL);
	}
	public void openURL(String url){
		setAddress(url);
		replaceLocation(url);
	}
	class Page {
		JScrollPane pane;
		JEditorPane editor;
		Page() {
			editor = new JEditorPane();
			editor.setEditable(false);
			editor.setContentType("text/html");
			pane = new JScrollPane(editor);
			pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			editor.addHyperlinkListener(new HyperlinkAction());
		}
		void setPage(String url) throws IOException {
			if (url == null || url.length() == 0) return;
			try {
				browserPanel.setCursor(waitCursor);
				editor.setPage(url);
				setAddress(editor.getPage().toString());
			} catch (IOException e) {
				showAccessError(e);
				throw e;
			} finally {
				browserPanel.setCursor(Cursor.getDefaultCursor());
			}
		}
	}
	class HyperlinkAction implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent v) {
			HyperlinkEvent.EventType t = v.getEventType();
			if (t == HyperlinkEvent.EventType.ACTIVATED) {
				String url = v.getURL().toString();
//				if (url.endsWith(".ser")){                	
//				}
				statusArea.setText(" ");
				replaceLocation(url);
			} else if (t == HyperlinkEvent.EventType.ENTERED) {
				String url = v.getURL().toString();
				statusArea.setText(url);
			} else if (t == HyperlinkEvent.EventType.EXITED) {
				statusArea.setText(" ");
			}
		}
	}
	class AddressEnterAction implements ActionListener {
		public void actionPerformed(ActionEvent v) {
			String url = addressField.getText();
			replaceLocation(url);
		}
	}
	@SuppressWarnings("unchecked")
	private void replaceLocation(String url) {
		try {
			// まず，HEADERをチェックし，text/htmlならレンダリング
			// それ以外なら，ファイルに保存
			HeadMethod head = new HeadMethod(url);
			HttpClient client = new HttpClient();
			int status = client.executeMethod(head);
			System.out.println(status);
			Header header = head.getResponseHeader("Content-Type");
			String contenttype = header.getValue();
			
			if (contenttype.equalsIgnoreCase("text/html")){
				
				Page newPage = new Page();
				newPage.setPage(url);
				if (currentPage != null) {
					pageStack.push(currentPage);
					backButton.setEnabled(true);
					browserPanel.remove(currentPage.pane);
				}
				currentPage = newPage;
				browserPanel.add(newPage.pane, BorderLayout.CENTER);
				refresh();
				setAddress(newPage.editor.getPage().toString());
			} else if (contenttype.equalsIgnoreCase("application/x-java-serialized-object")){
				sosman.loadFromHTTP(url);
				f.dispose();
			}
		} catch (IOException e) {
		}
	}
	class BackButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent v) {
			if (pageStack.isEmpty()) return;
			backButton.setEnabled(false);
			Page prevPage = (Page)pageStack.pop(); 
			browserPanel.remove(currentPage.pane);
			currentPage = prevPage;
			browserPanel.add(currentPage.pane, BorderLayout.CENTER);
			refresh();
			if (!pageStack.isEmpty()) {
				backButton.setEnabled(true);
			}
		}
	}
	class ReloadButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent v) {
			String url = currentPage.editor.getPage().toString();
			try {
				reloadButton.setEnabled(false);
				Page newPage = new Page();
				newPage.setPage(url);
				browserPanel.remove(currentPage.pane);
				currentPage = newPage;
				browserPanel.add(newPage.pane, BorderLayout.CENTER);
				refresh();
				setAddress(newPage.editor.getPage().toString());
			} catch (IOException e) {
			} finally {
				reloadButton.setEnabled(true);
			}
		}
	}
	private void refresh() {
		browserPanel.validate();
		browserPanel.repaint();
	}
	private void setAddress(String url) {
		addressField.setText(url);
		addressField.setCaretPosition(0);
	}
	private void showAccessError(Exception e) {
		JOptionPane.showMessageDialog(
				browserPanel, 
				e.toString(), 
				"Access Error", 
				JOptionPane.ERROR_MESSAGE
		);
	}
}

