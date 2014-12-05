package jp.ac.jaist.css.common.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MultiLinerTextInputDialog extends JDialog {
	private static final long serialVersionUID = -849022194580187960L;

	JFrame parent;

	JTextArea tarea;

	JButton ok, cancel;

	public MultiLinerTextInputDialog(JFrame p, String init) {
		super(p, "Input Text", true);
		parent = p;
		tarea = new JTextArea(init, 12, 30);
		getContentPane().add(tarea, BorderLayout.CENTER);
		JPanel jp = new JPanel();
		ok = new JButton("ok");
		cancel = new JButton("cancel");
		jp.add(ok);
		jp.add(cancel);
		getContentPane().add(jp, BorderLayout.SOUTH);
		pack();
		setLocation(p.getLocation().x + 30, p.getLocation().y + 60);

		tarea.setCaretPosition(tarea.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tarea = null;
				dispose();
			}
		});

		tarea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					tarea = null;
					dispose();
				}
			}
		});
	}

	public static String showDialog(JFrame parent, String initial) {
		MultiLinerTextInputDialog d = new MultiLinerTextInputDialog(parent,
				initial);
		d.setVisible(true);
		if (d.tarea != null)
			return d.tarea.getText();
		else
			return null;
	}
}
