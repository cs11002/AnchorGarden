package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Bus extends Covis_Car {

	private static final long serialVersionUID = -5292402038866971611L;
	public static Color defaultColor = new Color(49, 133, 252);
	public static String varname3 = "passNum";

	public Covis_Bus(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
	}

	public Covis_Bus(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + carValue.value + "," + gasoline.value + ");";
	}

	PPath sub;
	Covis_int passNum;
	PText passNumLabel;
	int passNumValue;

	public void init(boolean isAuto) {
		// 親クラス分作成
		super.init(true);

		// 本体の大きさ指定
		setPathToRectangle(0, 0, 180, 180);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup作成
		sub = new PPath();
		// 大きさ・形指定
		sub.setPathToRectangle(0, 120, 180, 60);
		sub.setPaint(color);
		sub.setStroke(basicStroke);
		sub.addAttribute("moveTarget", this);
		sub.addAttribute("tooltip", this);

		// passengerNumの追加
		passNum = new Covis_int(buffer, isAuto);
		passNum.addAttribute("moveTarget", this);
		passNum.addAttribute("tooltip", this);
		passNum.addAttribute("popupMenu", new ClassVarMenu(varname3,passNum,this));
		passNum.valueText.addAttribute("moveTarget", this);
		passNum.valueText.addAttribute("tooltip", this);
		passNum.valueText.addAttribute("popupMenu", new ClassVarMenu(varname3,passNum,this));
		passNumValue = 0;
		passNum.setValue(String.valueOf(passNumValue));
		// 大きさ位置指定
		passNum.setScale(0.8f);
		passNum.offset(140, 130);
		sub.addChild(passNum);
		
		// Labelの表示
		passNumLabel = new PText(passNum.getClsName() + " " + varname3);
		passNumLabel.scale(1.9f);
		passNumLabel.offset(10, 135);
		passNumLabel.addAttribute("moveTarget", this);
		passNumLabel.addAttribute("tooltip", this);;
		sub.addChild(passNumLabel);
		
		addChild(sub);
		
		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Bus",
					"new Bus(carValue,gasoline);");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}
		
		// インデックス値を右肩にはりつける 用途不明
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
		return " carValue,gasoline ";
	}
	
	public String covis_get_on(int num) {
		passNumValue += num;
		passNum.setValue(String.valueOf(passNumValue));
		return num + "人乗車しました";
	}
	
	public String covis_get_off(int num) {
		passNumValue -= num;
		passNum.setValue(String.valueOf(passNumValue));
		return num + "人降車しました";
	}
	
	public String toString() {
		/*
		 * if (b.anchor.link.dest != null) { return "int a = " + a.getValue() +
		 * "\n String b = \n\"" + b.anchor.link.dest.toString() + "\""; } else {
		 * return "int a = " + a.getValue(); }
		 */
		return "not implemented ";
	}
	
	public static String classdef = "";
}
