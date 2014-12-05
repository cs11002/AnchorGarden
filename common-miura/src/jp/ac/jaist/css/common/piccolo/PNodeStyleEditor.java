package jp.ac.jaist.css.common.piccolo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.nodes.PText;

public class PNodeStyleEditor extends JPanel implements Serializable,
ActionListener {

	private static final long serialVersionUID = -8647155879026117213L;
	public static Font globalLastFont;

	JCheckBox isTextApplicable;
	// PPath
	JCheckBox isPathApplicable;
	JComboBox fontNameList;
	JComboBox fontStyleList;
	JTextField fontSizeField;
	JTextField styleName;

	JFrame jf;
	FontPreviewPanel fpp;
	Hashtable<String, Integer> fontStyleHash;
	PText target;
	String lastFontName;

	public PNodeStyleEditor(PText _target) {
		target = _target;
		Font lastFont = target.getFont();
		if (lastFont == null){
			lastFont = new Font("sansserif", Font.PLAIN, 20);
		}
		lastFontName = lastFont.getFamily();

		fontStyleHash = new Hashtable<String, Integer>();
		fontStyleHash.put("PLAIN", Font.PLAIN);
		fontStyleHash.put("BOLD", Font.BOLD);
		fontStyleHash.put("ITALIC", Font.ITALIC);

		setLayout(new BorderLayout());
		styleName = new JTextField(20);

		add(styleName, BorderLayout.NORTH);

		ArrayList<String> fontnamelist = getFontNames();

		String[] fontlist = new String[fontnamelist.size()];
		fontlist = fontnamelist.toArray(fontlist);
		fontNameList = new JComboBox(fontlist);
		fontNameList.addActionListener(this);




		JPanel textP = new JPanel();
		textP.add(fontNameList);
		String[] styles = { "PLAIN", "BOLD", "ITALIC" };
		fontStyleList = new JComboBox(styles);
		fontStyleList.addActionListener(this);
		textP.add(fontStyleList);
		fontSizeField = new JTextField(4);
		fontSizeField.setText("30");
		textP.add(fontSizeField);

		fontSizeField.addActionListener(this);
		add(textP, BorderLayout.SOUTH);

		fpp = new FontPreviewPanel();
		add(fpp, BorderLayout.CENTER);

		jf = new JFrame("Text Font");
		jf.getContentPane().add(this, BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);

		int idx = fontnamelist.indexOf(lastFontName);
		fontNameList.setSelectedIndex(idx);
		System.out.println(lastFontName+" "+idx);

//		fontNameList.setSelectedIndex(fontnamelist.indexOf(style.fontName));
	}

	public ArrayList<String> getFontNames() {
		ArrayList<String> alist = new ArrayList<String>();
		alist.add("Serif");
		alist.add("SansSerif");

		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getAvailableFontFamilyNames();
		for (int i = 0; i < fonts.length; i++) {
			alist.add(fonts[i]);
		}
		return alist;
	}

	public void actionPerformed(ActionEvent arg0) {
		Font f = new Font((String) fontNameList.getSelectedItem(),
				fontStyleHash.get(fontStyleList.getSelectedItem()), Integer
				.parseInt(fontSizeField.getText()));
		fpp.setFont(f);
		target.setFont(f);
		fpp.repaint();
		globalLastFont = f;
	}

	@SuppressWarnings("serial")
	class FontPreviewPanel extends JPanel {
		Font font;

		public FontPreviewPanel() {

		}

		public Dimension getPreferredSize() {
			return new Dimension(200, 50);
		}

		public void setFont(Font f) {
			font = f;
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setBackground(Color.white);
			g2.clearRect(0, 0, getWidth(), getHeight());
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2.setFont(font);
			g2.drawString("123ABCabc‚ ‚¢‚¤", 20, 40);
		}
	}

}
