package jp.ac.jaist.css.common.piccolo;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.umd.cs.piccolo.nodes.PText;

public class PTextMenu extends PNodeMenu {
	private static final long serialVersionUID = 7741402910818721975L;
	public PText target;

	public PTextMenu(PText _v) {
		super(null);
		target = _v;

		JMenuItem menuItem = new JMenuItem("font");
		add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PNodeStyleEditor(target);
			}
		});

		if (PNodeStyleEditor.globalLastFont != null){
			menuItem = new JMenuItem("font=>"+PNodeStyleEditor.globalLastFont.getFamily());
			add(menuItem);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					target.setFont(PNodeStyleEditor.globalLastFont);
				}
			});
		}
		
		menuItem = new JMenuItem("fontcolor");
		add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PNodeColorEditor(target);
			}
		});

		if (PNodeColorEditor.globalColors != null){
			menuItem = new JMenuItem("fontcolor=>lastColor");
			add(menuItem);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					target.setTextPaint(PNodeColorEditor.globalColors[0]);
					target.setPaint(PNodeColorEditor.globalColors[1]);
				}
			});
		}
		
		JMenu fcmenu = new JMenu("fontcolor");
		add(fcmenu);
		menuItem = new JMenuItem("red");
		fcmenu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				target.setTextPaint(Color.red);
			}
		});

		menuItem = new JMenuItem("blue");
		fcmenu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				target.setTextPaint(Color.blue);
			}
		});

		menuItem = new JMenuItem("green");
		fcmenu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				target.setTextPaint(new Color(0,120,0));
			}
		});

		menuItem = new JMenuItem("black");
		fcmenu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				target.setTextPaint(Color.black);
			}
		});

		
		
	}
}
