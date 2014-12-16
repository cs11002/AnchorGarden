package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Bus extends Covis_Car {

	private static final long serialVersionUID = -5292402038866971611L;
	public static Color defaultColor = new Color(49, 133, 252);
	public static String varname3 = "��Ԑl��";

	public Covis_Bus(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
	}

	public Covis_Bus(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + �ԗ����i.value + "," + �K�\����.value + ");";
	}

	PPath sub;
	Covis_int ��Ԑl��;
	PText passNumLabel;
	int passNumValue;

	public void init(boolean isAuto) {
		// �e�N���X���쐬
		super.init(true);

		// �{�̂̑傫���w��
		setPathToRectangle(0, 0, 190, 180);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup�쐬
		sub = new PPath();
		// �傫���E�`�w��
		sub.setPathToRectangle(0, 120, 190, 60);
		sub.setPaint(color);
		sub.setStroke(basicStroke);
		sub.addAttribute("moveTarget", this);
		sub.addAttribute("tooltip", this);

		// passengerNum�̒ǉ�
		��Ԑl�� = new Covis_int(buffer, isAuto,50);
		��Ԑl��.addAttribute("moveTarget", this);
		��Ԑl��.addAttribute("tooltip", this);
		��Ԑl��.addAttribute("popupMenu", new ClassVarMenu(varname3,��Ԑl��,this));
		��Ԑl��.valueText.addAttribute("moveTarget", this);
		��Ԑl��.valueText.addAttribute("tooltip", this);
		��Ԑl��.valueText.addAttribute("popupMenu", new ClassVarMenu(varname3,��Ԑl��,this));
		passNumValue = 0;
		��Ԑl��.setValue(String.valueOf(passNumValue));
		// �傫���ʒu�w��
		��Ԑl��.setScale(0.8f);
		��Ԑl��.offset(140, 130);
		sub.addChild(��Ԑl��);
		
		// Label�̕\��
		passNumLabel = new PText(��Ԑl��.getClsName() + " " + varname3);
		passNumLabel.scale(1.9f);
		passNumLabel.offset(10, 135);
		passNumLabel.addAttribute("moveTarget", this);
		passNumLabel.addAttribute("tooltip", this);;
		sub.addChild(passNumLabel);
		
		addChild(sub);
		
		�ԗ����i.setValue("800");
		gasolineValue = 200;
		�K�\����.setValue(String.valueOf(gasolineValue));
		
		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Bus",
					"new Bus(�ԗ����i,�K�\����);");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}
		
		// �C���f�b�N�X�l���E���ɂ͂���� �p�r�s��
		PText ptidx = new PText(varname3);
		ptidx.setScale(0.6);
		ptidx.setOffset(18, 5);
		ptidx.setTextPaint(Color.blue);
		addChild(ptidx);
		ptidx.addAttribute("moveTarget", this);
		ptidx.addAttribute("tooltip", this);
	}
	
	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_Bus(buffer, isAuto);
	}

	public Color getClassColor() {
		return defaultColor;
	}
	
	public void attach(Anchor anchor) {
		super.attach(anchor);
		checkAnchor();
	}

	public void detach(Anchor anchor) {
		super.detach(anchor);
		checkAnchor();
	}

	public void checkAnchor() {
		for(Anchor a: anchors_incoming){
			//if(a.type.isAssignableFrom(Covis_Sub.class)) {
			if(a.srcVariable.cv_class instanceof Covis_Bus) {
				sub.setPaint(color);
				for(PNode p: sub.getAllNodes()) {
					if(p instanceof VariableM) {
						((VariableM)p).setEnabled(true);
					}
				}
				return;
			}
		}
		sub.setPaint(Color.black);
		for(PNode p: sub.getAllNodes()) {
			if(p instanceof VariableM) {
				((VariableM)p).setEnabled(false);
			}
		}
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
	
	public String covis_��Ԃ���(int num) {
		passNumValue += num;
		��Ԑl��.setValue(String.valueOf(passNumValue));
		return num + "�l��Ԃ��܂���";
	}
	
	public String covis_�~�Ԃ���(int num) {
		passNumValue -= num;
		��Ԑl��.setValue(String.valueOf(passNumValue));
		return num + "�l�~�Ԃ��܂���";
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
"public class Bus extends Car {\n"+
"   int ��Ԑl��;�@\n"+
"   \n"+
"   public Bus() {\n"+
"      �ԗ����i = 800;\n"+
"      �K�\���� = 200;\n"+
"   }\n"+
"   \n"+
"   public Bus(int _�ԗ����i,int _�K�\����) {\n"+
"      super(_�ԗ����i,_�K�\����);\n"+
"   }\n"+
"   \n"+
"   public String ��Ԃ���(int �l��){\n"+
"         ��Ԑl�� += �l��;\n"+
"         return �l�� + \"�l��Ԃ��܂���\";\n"+
"   }\n"+
"   \n"+
"   public String �~�Ԃ���(int �l��){\n"+
"      ��Ԑl�� -= �l��;\n"+
"      return �l�� + \"�l�~�Ԃ��܂���\";\n"+
"   }\n"+
"}"; 
}
