package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.JLabelW;
import jaist.css.covis.SrcWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Animal extends Covis_Object {

	private static final long serialVersionUID = 773271700954233487L;
	public static Color defaultColor = new Color(149, 249, 227);
	public static String varname1 = "�N��";
	public static String varname2 = "�̒�";

	public Covis_Animal(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_Animal(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}
	
	Covis_int �N��;
	Covis_int �̒�;
	PText ageLabel;
	PText lenLabel;
	PPath sup;
	
	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + �N��.value + "," + �̒�.value + ");";
	}
	
	public void init(boolean isAuto) {
		// �{�̂̑傫���w��
		setPathToRectangle(0, 0, 160, 120);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup�쐬
		sup = new PPath();
		// �傫���E�`�w��
		sup.setPathToRectangle(0, 0, 160, 120);
		sup.setPaint(new Color(149, 249, 227));
		sup.setStroke(basicStroke);
		sup.addAttribute("moveTarget", this);
		sup.addAttribute("tooltip", this);

		// age�̒ǉ�
		�N�� = new Covis_int(buffer, isAuto,50);
		�N��.addAttribute("moveTarget", this);
		�N��.addAttribute("tooltip", this);
		�N��.addAttribute("popupMenu", new ClassVarMenu(varname1,�N��,this));
		�N��.valueText.addAttribute("moveTarget", this);
		�N��.valueText.addAttribute("tooltip", this);
		�N��.valueText.addAttribute("popupMenu", new ClassVarMenu(varname1,�N��,this));
		�N��.setValue("12");
		// �傫���ʒu�w��
		�N��.setScale(0.8f);
		�N��.offset(110, 10);
		sup.addChild(�N��);

		// bodyLength�̒ǉ�
		�̒� = new Covis_int(buffer, isAuto,50);
		�̒�.addAttribute("moveTarget", this);
		�̒�.addAttribute("tooltip", this);
		�̒�.addAttribute("popupMenu", new ClassVarMenu(varname2,�̒�,this));
		�̒�.valueText.addAttribute("moveTarget", this);
		�̒�.valueText.addAttribute("tooltip", this);
		�̒�.valueText.addAttribute("popupMenu", new ClassVarMenu(varname2,�̒�,this));
		�̒�.setValue("25");
		// �傫���ʒu�w��
		�̒�.setScale(0.8f);
		�̒�.offset(110, 70);
		sup.addChild(�̒�);

		// Label�̕\��
		ageLabel = new PText(�N��.getClsName() + " " + varname1);
		lenLabel = new PText(�̒�.getClsName() + " " + varname2);
		ageLabel.scale(1.9f);
		lenLabel.scale(1.9f);
		ageLabel.offset(10, 15);
		lenLabel.offset(10, 75);
		ageLabel.addAttribute("moveTarget", this);
		lenLabel.addAttribute("moveTarget", this);
		ageLabel.addAttribute("tooltip", this);
		lenLabel.addAttribute("tooltip", this);
		sup.addChild(ageLabel);
		sup.addChild(lenLabel);

		addChild(sup);

		if (!isAuto) {
			AnimalConstructorDialog dialog = AnimalConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Animal",
					"new Animal(�N��,�̒�);");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}

		// �C���f�b�N�X�l���E���ɂ͂���� �p�r�s��
		PText ptidx = new PText(varname1);
		ptidx.setScale(0.6);
		ptidx.setOffset(18, 5);
		ptidx.setTextPaint(Color.blue);
		addChild(ptidx);
		ptidx.addAttribute("moveTarget", this);
		ptidx.addAttribute("tooltip", this);

		PText ptidx2 = new PText(varname2);
		ptidx2.setScale(0.6);
		ptidx2.setOffset(77, 15);
		ptidx2.setTextPaint(Color.blue);
		addChild(ptidx2);
		ptidx2.addAttribute("moveTarget", this);
		ptidx2.addAttribute("tooltip", this);
	}
	
	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_Animal(buffer, isAuto);
	}

	public Color getClassColor() {
		return defaultColor;
	}

	public static int objCount = 0;
	public static int objAryCount = 0;

	public String getNextVarName(boolean isAry) {
		StringBuffer sb = new StringBuffer(getClsName().toLowerCase()
				.substring(0, 3));
		if (isAry) {
			objAryCount++;
			sb.append("Ary");
			sb.append(objAryCount);
		} else {
			objCount++;
			sb.append(objCount);
		}
		return sb.toString();
	}

	@Override
	public void clear_objCount() {
		objCount = 0;
		objAryCount = 0;
		System.out.println("clear count " + getClsName());
	}

	@Override
	public String getConstructorArgs() {
		return " �N��,�̒� ";
	}
	
	// ���\�b�h�쐬
	public String covis_��() {
		return "���������܂���";
	}
	
	public String toString() {
		/*
		 * if (b.anchor.link.dest != null) { return "int a = " + a.getValue() +
		 * "\n String b = \n\"" + b.anchor.link.dest.toString() + "\""; } else {
		 * return "int a = " + a.getValue(); }
		 */
		return "not implemented ";
	}
	
	public static String classdef = "" +
"public abstract class Animal {\n"+
"   int �N��;  \n"+
"   int �̒�;  \n"+
"   \n"+
"   public Animal() {\n"+
"      �N�� = 12;\n"+
"      �̒� = 25;\n"+
"   }\n"+
"   \n"+
"   public Animal(int _�N��,int _�̒�) {\n"+
"      �N�� = _�N��;\n"+
"      �̒� = _�̒�;\n"+
"   }\n"+
"   \n"+
"   public abstract String ��();\n"+
"}"; 
}

//�R���X�g���N�^�p�_�C�A���O�̃N���X
class AnimalConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfage;
	JTextField jtfbodyLength;

	JButton ok;
	boolean canceled = true;

	public AnimalConstructorDialog(JFrame p, Covis_Animal ani, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfage = new JTextField(ani.�N��.getValue());
		jtfage.setFont(SrcWindow.sans30);
		jtfage.setBackground(Covis_int.defaultColor);
		jtfage.addKeyListener(this);

		jtfbodyLength = new JTextField(ani.�̒�.getValue());
		jtfbodyLength.setFont(SrcWindow.sans30);
		jtfbodyLength.setBackground(Covis_int.defaultColor);
		jtfbodyLength.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(2, 3));
		inner.add(new JLabelW(ani.�N��.getClsName()));
		inner.add(new JLabelW(ani.varname1));
		inner.add(jtfage);
		inner.add(new JLabelW(ani.�̒�.getClsName()));
		inner.add(new JLabelW(ani.varname2));
		inner.add(jtfbodyLength);

		getContentPane().add(inner, BorderLayout.CENTER);

		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		// pack();
		setSize(350, 180);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth()) / 2,
				p.getLocation().y + (p.getHeight() - this.getHeight()) / 2);

		jtfage.setCaretPosition(jtfage.getText().length());
		jtfbodyLength.setCaretPosition(jtfbodyLength.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}
		});

		jtfage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfage = null;
					jtfbodyLength = null;
					dispose();
				}
			}
		});
		jtfbodyLength.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfage = null;
					jtfbodyLength = null;
					dispose();
				}
			}
		});

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			ok.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public boolean isCanceled() {
		return canceled;
	}

	public static AnimalConstructorDialog showDialog(JFrame parent,
			Covis_Animal ani, String title, String mes1) {
		AnimalConstructorDialog d = new AnimalConstructorDialog(parent, ani,
				title, mes1);
		d.setVisible(true);
		if (d.jtfage != null && d.jtfbodyLength != null){
			ani.�N��.setValue(String.valueOf(Integer.parseInt(d.jtfage.getText())));
			ani.�̒�.setValue(String.valueOf(Integer.parseInt(d.jtfbodyLength.getText())));
		}
		return d;
	}
}
