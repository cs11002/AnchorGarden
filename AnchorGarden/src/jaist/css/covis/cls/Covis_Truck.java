package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Truck extends Covis_Car {

	private static final long serialVersionUID = 2606894600998374563L;
	public static Color defaultColor = new Color(52, 229, 255);
	public static String varname3 = "�ύڗ�";

	public Covis_Truck(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
	}

	public Covis_Truck(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + �ԗ����i.value + "," + �K�\����.value + ");";
	}

	PPath sub;
	Covis_int �ύڗ�;
	PText loadageLabel;
	int loadageValue;

	public void init(boolean isAuto) {
		// �e�N���X���쐬
		super.init(true);

		// �{�̂̑傫���w��
		setPathToRectangle(0, 0, 180, 180);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup�쐬
		sub = new PPath();
		// �傫���E�`�w��
		sub.setPathToRectangle(0, 120, 180, 60);
		sub.setPaint(color);
		sub.setStroke(basicStroke);
		sub.addAttribute("moveTarget", this);
		sub.addAttribute("tooltip", this);

		// loadage�̒ǉ�
		�ύڗ� = new Covis_int(buffer, isAuto);
		�ύڗ�.addAttribute("moveTarget", this);
		�ύڗ�.addAttribute("tooltip", this);
		�ύڗ�.addAttribute("popupMenu", new ClassVarMenu(varname3,�ύڗ�,this));
		�ύڗ�.valueText.addAttribute("moveTarget", this);
		�ύڗ�.valueText.addAttribute("tooltip", this);
		�ύڗ�.valueText.addAttribute("popupMenu", new ClassVarMenu(varname3,�ύڗ�,this));
		loadageValue = 0;
		�ύڗ�.setValue(String.valueOf(loadageValue));
		// �傫���ʒu�w��
		�ύڗ�.setScale(0.8f);
		�ύڗ�.offset(140, 130);
		sub.addChild(�ύڗ�);
		
		// Label�̕\��
		loadageLabel = new PText(�ύڗ�.getClsName() + " " + varname3);
		loadageLabel.scale(1.9f);
		loadageLabel.offset(10, 135);
		loadageLabel.addAttribute("moveTarget", this);
		loadageLabel.addAttribute("tooltip", this);;
		sub.addChild(loadageLabel);
		
		addChild(sub);
		
		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Bus",
					"new Truck(�ԗ����i,�K�\����);");
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
		return new Covis_Truck(buffer, isAuto);
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
			if(a.srcVariable.cv_class instanceof Covis_Truck) {
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
	
	//���\�b�h�쐬
	public String covis_�ו���ς�(int value) {
		loadageValue += value;
		�ύڗ�.setValue(String.valueOf(loadageValue));
		return value + "kg�ς݂܂���";
	}
	
	public String covis_�ו����~�낷(int value) {
		loadageValue -= value;
		�ύڗ�.setValue(String.valueOf(loadageValue));
		return value + "kg�~�낵�܂���";
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
"public class Truck extends Car {\n"+
"   int �ύڗ�;  \n"+
"   \n"+
"   public Truck() {\n"+
"      super();\n"+
"   }\n"+
"   \n"+
"   public Truck(int _�ԗ����i,int _�K�\����) {\n"+
"      super(_�ԗ����i,_�K�\����);\n"+
"   }\n"+
"   \n"+
"   public String �ו���ς�(int ��){\n"+
"         �ύڗ� += ��;\n"+
"         return �� + \"kg�ς݂܂���\";\n"+
"   }\n"+
"   \n"+
"   public String �ו����~�낷(int ��){\n"+
"      �ύڗ� -= ��;\n"+
"      return �� + \"kg�~�낵�܂���\";\n"+
"   }\n"+
"}"; 
}
