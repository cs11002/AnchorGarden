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

public class Covis_Car extends Covis_Object {

	private static final long serialVersionUID = -4624235387919516738L;
	public static Color defaultColor = new Color(173, 189, 255);
	public static String varname1 = "�ԗ����i";
	public static String varname2 = "�K�\����";

	public Covis_Car(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_Car(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	Covis_int �ԗ����i;
	Covis_int �K�\����;
	int gasolineValue;
	PText carValueLabel;
	PText gasolineLabel;
	PPath sup;

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + �ԗ����i.value + "," + �K�\����.value + ");";
	}

	public void init(boolean isAuto) {
		// �{�̂̑傫���w��
		setPathToRectangle(0, 0, 190, 120);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup�쐬
		sup = new PPath();
		// �傫���E�`�w��
		sup.setPathToRectangle(0, 0, 190, 120);
		sup.setPaint( new Color(173, 189, 255));
		sup.setStroke(basicStroke);
		sup.addAttribute("moveTarget", this);
		sup.addAttribute("tooltip", this);

		// carValue�̒ǉ�
		�ԗ����i = new Covis_int(buffer, isAuto,50);
		�ԗ����i.addAttribute("moveTarget", this);
		�ԗ����i.addAttribute("tooltip", this);
		�ԗ����i.addAttribute("popupMenu", new ClassVarMenu(varname1,�ԗ����i,this));
		�ԗ����i.valueText.addAttribute("moveTarget", this);
		�ԗ����i.valueText.addAttribute("tooltip", this);
		�ԗ����i.valueText.addAttribute("popupMenu", new ClassVarMenu(varname1,�ԗ����i,this));
		�ԗ����i.setValue("90");
		// �傫���ʒu�w��
		�ԗ����i.setScale(0.8f);
		�ԗ����i.offset(140, 10);
		sup.addChild(�ԗ����i);

		// gasoline�̒ǉ�
		�K�\���� = new Covis_int(buffer, isAuto,50);
		�K�\����.addAttribute("moveTarget", this);
		�K�\����.addAttribute("tooltip", this);
		�K�\����.addAttribute("popupMenu", new ClassVarMenu(varname2,�K�\����,this));
		�K�\����.valueText.addAttribute("moveTarget", this);
		�K�\����.valueText.addAttribute("tooltip", this);
		�K�\����.valueText.addAttribute("popupMenu", new ClassVarMenu(varname2,�K�\����,this));
		gasolineValue = 40;
		�K�\����.setValue(String.valueOf(gasolineValue));
		// �傫���ʒu�w��
		�K�\����.setScale(0.8f);
		�K�\����.offset(140, 70);
		sup.addChild(�K�\����);

		// Label�̕\��
		carValueLabel = new PText(�ԗ����i.getClsName() + " " + varname1);
		gasolineLabel = new PText(�K�\����.getClsName() + " " + varname2);
		carValueLabel.scale(1.9f);
		gasolineLabel.scale(1.9f);
		carValueLabel.offset(10, 15);
		gasolineLabel.offset(10, 75);
		carValueLabel.addAttribute("moveTarget", this);
		gasolineLabel.addAttribute("moveTarget", this);
		carValueLabel.addAttribute("tooltip", this);
		gasolineLabel.addAttribute("tooltip", this);
		sup.addChild(carValueLabel);
		sup.addChild(gasolineLabel);

		addChild(sup);

		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Car",
					"new Car(�ԗ����i,�K�\����);");
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
		return new Covis_Car(buffer, isAuto);
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
		return " �ԗ����i,�K�\���� ";
	}
	
	// ���\�b�h�쐬
	public String covis_���s����() {
		if(gasolineValue < 5) {
			return "�K�\����������܂���";
		}else{
			gasolineValue -= 5;
			�K�\����.setValue(String.valueOf(gasolineValue));
			return "�K�\������5����";
		}
	}

	public String covis_��������(int value) {
		gasolineValue += value;
		�K�\����.setValue(String.valueOf(gasolineValue));
		return "�������܂���";
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
"public class Car {\n"+
"   int �ԗ����i; \n"+
"   int �K�\����;   \n"+
"   \n"+
"   public Car() {\n"+
"      �ԗ����i = 90;\n"+
"      �K�\���� = 40;\n"+
"   }\n"+
"   \n"+
"   public Car(int _�ԗ����i,int _�K�\����) {\n"+
"      �ԗ����i = _�ԗ����i;\n"+
"      �K�\���� = _�K�\����;\n"+
"   }\n"+
"   \n"+
"   public String ���s����(){\n"+
"      if (�K�\���� < 5) {\n"+
"         return \"�K�\����������܂���\";\n"+
"      } else {\n"+
"         �K�\���� -= 5;\n"+
"         return \"�K�\������5����\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String ��������(int ������){\n"+
"      �K�\���� += ������;\n"+
"      return \"�������܂���\";\n"+
"   }\n"+
"}"; 
}


//�R���X�g���N�^�p�_�C�A���O�̃N���X
class CarConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfcarValue;
	JTextField jtfgasoline;

	JButton ok;
	boolean canceled = true;

	public CarConstructorDialog(JFrame p, Covis_Car car, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfcarValue = new JTextField(car.�ԗ����i.getValue());
		jtfcarValue.setFont(SrcWindow.sans30);
		jtfcarValue.setBackground(Covis_int.defaultColor);
		jtfcarValue.addKeyListener(this);

		jtfgasoline = new JTextField(car.�K�\����.getValue());
		jtfgasoline.setFont(SrcWindow.sans30);
		jtfgasoline.setBackground(Covis_int.defaultColor);
		jtfgasoline.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(2, 3));
		inner.add(new JLabelW(car.�ԗ����i.getClsName()));
		inner.add(new JLabelW(car.varname1));
		inner.add(jtfcarValue);
		inner.add(new JLabelW(car.�K�\����.getClsName()));
		inner.add(new JLabelW(car.varname2));
		inner.add(jtfgasoline);

		getContentPane().add(inner, BorderLayout.CENTER);

		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		// pack();
		setSize(350, 180);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth()) / 2,
				p.getLocation().y + (p.getHeight() - this.getHeight()) / 2);

		jtfcarValue.setCaretPosition(jtfcarValue.getText().length());
		jtfgasoline.setCaretPosition(jtfgasoline.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}
		});

		jtfcarValue.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfcarValue = null;
					jtfgasoline = null;
					dispose();
				}
			}
		});
		jtfgasoline.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfcarValue = null;
					jtfgasoline = null;
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

	public static CarConstructorDialog showDialog(JFrame parent,
			Covis_Car car, String title, String mes1) {
		CarConstructorDialog d = new CarConstructorDialog(parent, car,
				title, mes1);
		d.setVisible(true);
		if (d.jtfcarValue != null && d.jtfgasoline != null){
			car.�ԗ����i.setValue(String.valueOf(Integer.parseInt(d.jtfcarValue.getText())));
			car.�K�\����.setValue(String.valueOf(Integer.parseInt(d.jtfgasoline.getText())));
			car.gasolineValue = Integer.parseInt(d.jtfgasoline.getText());
		}
		return d;
	}
}
