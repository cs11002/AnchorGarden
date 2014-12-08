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

import edu.umd.cs.piccolo.nodes.PText;

public class Covis_CapsuledAccount extends Covis_Object {

	private static final long serialVersionUID = 6631160996844648983L;
	public static Color defaultColor = new Color(182, 196, 84);
	public static String varname1 = "�c��";
	public static String varname2 = "����";
	
	public Covis_CapsuledAccount(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_CapsuledAccount(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	Covis_int �c��;
	int remainderValue;
	VariableM ����;
	Covis_Type history_type;
	PText remainderLabel;
	PText historyLabel;

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + �c��.value + ");";
	}

	//�J�v�Z�����ς�
	public void init(boolean isAuto) {
		// �{�̂̑傫���w��
		setPathToRectangle(0, 0, 200, 120);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// remainder�̒ǉ�
		�c�� = new Covis_int(buffer, isAuto,80);
		�c��.addAttribute("moveTarget", this);
		�c��.addAttribute("tooltip", this);
		�c��.valueText.addAttribute("moveTarget", this);
		�c��.valueText.addAttribute("tooltip", this);
		remainderValue = 0;
		�c��.setValue(String.valueOf(remainderValue));
		// �傫���ʒu�w��
		�c��.setScale(0.8f);
		�c��.offset(125, 5);
		addChild(�c��);

		// String�^�ǉ�
		// String�^�̃N���X�����K��
		/*try {
			Class<?> cla = Class.forName("jaist.css.covis.cls.Covis_Array");
			Constructor<?> con = cla.getConstructor(CoVisBuffer.class,
					boolean.class,String.class);
			history_type = (Covis_Type) con.newInstance(buffer,true,"String");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/
		history_type = new Covis_Array(buffer,true,10,"String");
		���� = new VariableM(history_type, null, buffer, "history", this);
		// �A���J�[�o�^
		anchors_member.add(����.anchor);
		����.addAttribute("moveTarget", this);
		����.addAttribute("tooltip", this);
		// �ʒu�w��
		����.setOffset(135, 75);
		addChild(����);
		buffer.putHistoryVar("var", ����, false);

		// Label�̕\��
		remainderLabel = new PText(�c��.getClsName() + " " + varname1);
		historyLabel = new PText(history_type.getClsName() + " " + varname2);
		remainderLabel.scale(1.8f);
		historyLabel.scale(1.8f);
		remainderLabel.offset(10, 15);
		historyLabel.offset(10, 75);
		remainderLabel.addAttribute("moveTarget", this);
		historyLabel.addAttribute("moveTarget", this);
		remainderLabel.addAttribute("tooltip", this);
		historyLabel.addAttribute("tooltip", this);
		addChild(remainderLabel);
		addChild(historyLabel);

		if (!isAuto) {
			CapsuledAccountConstructorDialog dialog = CapsuledAccountConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of CapsuledAccount",
					"new CapsuledAccount( �c�� );");
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
	}

	public void createIns() {
		// String[]�^�̍쐬
		Covis_Array historyIns = (Covis_Array)history_type;
		// �쐬����String[]�^�ɃA���J�[������
		buffer.objField.addObject(historyIns);
		historyIns.attach(����.anchor);
		historyIns.setOffsetAlignment(this, 200, 150);
	}
	
	public void attach(Anchor anchor){
		super.attach(anchor);
		//����p�A���J�[�͕ύX�ł��Ȃ��悤�ɂ���
		����.setEnabled(false);
		����.anchor.anchortab.addAttribute("moveTarget", ����.anchor.anchortab);
		����.anchor.anchortab.addAttribute("tooltip", ����.anchor.anchortab);
	}

	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_UnCapsuledAccount(buffer, isAuto);
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
		return " �c�� ";
	}

	// ���\�b�h�쐬
	public String covis_�a������(int value) {
		if(value % 1000 == 0) {
			remainderValue += value;
			�c��.setValue(String.valueOf(remainderValue));
			covis_����o�^(value,0);
			return value + "�~�a�����܂���";
		}else{
			return "1000�~�P�ʂœ��͂��Ă�������";
		}
	}

	public String covis_�����o��(int value) {
		if(value % 1000 == 0) {
			if(remainderValue >= value) {
				remainderValue -= value;
				�c��.setValue(String.valueOf(remainderValue));
				covis_����o�^(value,1);
				return value + "�~�����o�����܂���";
			}else{
				return "�c��������܂���";
			}
		}else{
			return "1000�~�P�ʂœ��͂��Ă�������";
		}
	}
	
	public String covis_�c���Ɖ�() {
		return "�c����" + remainderValue + "�~�ł�";
	}
	
	private void covis_����o�^(int value,int type) {
		int i;
		Covis_String newhis = new Covis_String(buffer, true);
		if(type == 0) {
			newhis.setValues("�a�� " + value);
		}else{
			newhis.setValues("���o " + value);
		}
		buffer.objField.addObject(newhis);
		Covis_Array a = (Covis_Array)����.anchor.destObject;
		for(i=0;i<a.anchors_member.size();i++) {
			if(a.anchors_member.get(i).destObject == null) {
				newhis.attach(a.anchors_member.get(i));
				break;
			}
		}
		newhis.setOffsetAlignment(����.anchor.destObject, 20*i, 60+60*i);
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
"public class CapsuledAccount {\n"+
"   private int �c��;     \n"+
"   private String[] ����;\n"+
"   \n"+
"   public CapsuledAccount() {\n"+
"      �c�� = 0;\n"+
"      ���� = new String[10];\n"+
"   }\n"+
"   \n"+
"   public CapsuledAccount(int _�c��) {\n"+
"      �c�� = _�c��;\n"+
"      ���� = new String[10];\n"+
"   }\n"+
"   \n"+
"   public String �a������(int ���z){\n"+
"      if(���z % 1000 == 0) {\n"+
"         �c�� += ���z;\n"+
"         ����o�^(���z,0);\n"+
"         return ���z + \"�~�a�����܂���\";\n"+
"      }else{\n"+
"         return \"1000�~�P�ʂœ��͂��Ă�������\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String �����o��(int ���z){\n"+
"      if(���z % 1000 == 0) {\n"+
"         if(�c�� >= ���z){\n"+
"            �c�� -= ���z;\n"+
"            ����o�^(���z,1);\n"+
"            return ���z + \"�~�����o�����܂���\";\n"+
"         }else{\n"+
"            return \"�c��������܂���\";\n"+
"         }\n"+
"      }else{\n"+
"         return \"1000�~�P�ʂœ��͂��Ă�������\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String �c���Ɖ�(){\n"+
"      return \"�c����\"�@+ �c�� + \"�~�ł�\" ;\n"+
"   }\n"+
"   \n"+
"   private void ����o�^(int ���z,int ���) {\n"+
"      int i;\n"+
"      for(i=0,i<����.length;i++){\n"+
"         if(����[i] == null){\n"+
"            if(��� == 0){\n"+
"               ����[i] = \"�a�� \" + ���z;\n"+
"            }else{\n"+
"               ����[i] = \"���o \" + ���z;\n"+
"            }\n"+
"            break;\n"+
"         }\n"+
"      }\n"+
"   }\n"+
"}"; 
}


//�R���X�g���N�^�p�_�C�A���O�̃N���X
class CapsuledAccountConstructorDialog extends JDialog implements KeyListener {

	private static final long serialVersionUID = 1L;

	JFrame parent;

	JTextField jtfremainder;

	JButton ok;
	boolean canceled = true;

	public CapsuledAccountConstructorDialog(JFrame p, Covis_CapsuledAccount acc, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfremainder = new JTextField(acc.�c��.getValue());
		jtfremainder.setFont(SrcWindow.sans30);
		jtfremainder.setBackground(Covis_int.defaultColor);
		jtfremainder.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(1, 3));
		inner.add(new JLabelW(acc.�c��.getClsName()));
		inner.add(new JLabelW(acc.varname1));
		inner.add(jtfremainder);

		getContentPane().add(inner, BorderLayout.CENTER);

		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		// pack();
		setSize(350, 120);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth()) / 2,
				p.getLocation().y + (p.getHeight() - this.getHeight()) / 2);

		jtfremainder.setCaretPosition(jtfremainder.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}
		});

		jtfremainder.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfremainder = null;
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

	public static CapsuledAccountConstructorDialog showDialog(JFrame parent,
			Covis_CapsuledAccount acc, String title, String mes1) {
		CapsuledAccountConstructorDialog d = new CapsuledAccountConstructorDialog(parent, acc,
				title, mes1);
		d.setVisible(true);
		if (d.jtfremainder != null){
			acc.�c��.setValue(String.valueOf(Integer.parseInt(d.jtfremainder.getText())));
			acc.remainderValue = Integer.parseInt(d.jtfremainder.getText());
		}
		return d;
	}
}
