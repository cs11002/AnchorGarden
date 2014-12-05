package jp.ac.jaist.css.common.piccolo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.umd.cs.piccolo.nodes.PText;

public class PNodeColorEditor extends JPanel implements Serializable,
ActionListener {
	private static final long serialVersionUID = -1868374676447916717L;

	public static Color[] globalColors = new Color[]{Color.red, null};

	JFrame jf;
	PText target;

	public static LinkedHashMap<String,Integer> labels = new LinkedHashMap<String, Integer>();
	static{
		labels.put("text",0);
		labels.put("fill",1);
	}

	public PNodeColorEditor(PText _target) {
		target = _target;

		JPanel textP = new JPanel();

		globalColors[0] = (Color)target.getTextPaint();
		globalColors[1] = (Color)target.getPaint();
		
		int col = 0;
		for(String l: labels.keySet()){
			JButton jb = new JButton(l);
			jb.addActionListener(this);
			textP.add(jb);
			if (col == 0) jb.setForeground(globalColors[col]);
			else jb.setBackground(globalColors[col]);
			col++;

		}

		jf = new JFrame("Text Color");
		jf.getContentPane().add(textP, BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);
		jf.setLocation(300,300);

	}

	public void actionPerformed(ActionEvent arg0) {
		int idx = labels.get(arg0.getActionCommand());
		JButton jb = (JButton)arg0.getSource();
		globalColors[idx] = JColorChooser.showDialog(null, "Choose "+arg0.getActionCommand()+" Color", globalColors[idx]);			
		if (idx == 0) jb.setForeground(globalColors[idx]);
		else jb.setBackground(globalColors[idx]);
		target.setTextPaint(globalColors[0]);
		target.setPaint(globalColors[1]);
	}
}
