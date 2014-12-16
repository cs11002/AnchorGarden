package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Bus extends Covis_Car {

	private static final long serialVersionUID = -5292402038866971611L;
	public static Color defaultColor = new Color(49, 133, 252);
	public static String varname3 = "乗車人数";

	public Covis_Bus(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
	}

	public Covis_Bus(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + 車両価格.value + "," + ガソリン.value + ");";
	}

	PPath sub;
	Covis_int 乗車人数;
	PText passNumLabel;
	int passNumValue;

	public void init(boolean isAuto) {
		// 親クラス分作成
		super.init(true);

		// 本体の大きさ指定
		setPathToRectangle(0, 0, 190, 180);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup作成
		sub = new PPath();
		// 大きさ・形指定
		sub.setPathToRectangle(0, 120, 190, 60);
		sub.setPaint(color);
		sub.setStroke(basicStroke);
		sub.addAttribute("moveTarget", this);
		sub.addAttribute("tooltip", this);

		// passengerNumの追加
		乗車人数 = new Covis_int(buffer, isAuto,50);
		乗車人数.addAttribute("moveTarget", this);
		乗車人数.addAttribute("tooltip", this);
		乗車人数.addAttribute("popupMenu", new ClassVarMenu(varname3,乗車人数,this));
		乗車人数.valueText.addAttribute("moveTarget", this);
		乗車人数.valueText.addAttribute("tooltip", this);
		乗車人数.valueText.addAttribute("popupMenu", new ClassVarMenu(varname3,乗車人数,this));
		passNumValue = 0;
		乗車人数.setValue(String.valueOf(passNumValue));
		// 大きさ位置指定
		乗車人数.setScale(0.8f);
		乗車人数.offset(140, 130);
		sub.addChild(乗車人数);
		
		// Labelの表示
		passNumLabel = new PText(乗車人数.getClsName() + " " + varname3);
		passNumLabel.scale(1.9f);
		passNumLabel.offset(10, 135);
		passNumLabel.addAttribute("moveTarget", this);
		passNumLabel.addAttribute("tooltip", this);;
		sub.addChild(passNumLabel);
		
		addChild(sub);
		
		車両価格.setValue("800");
		gasolineValue = 200;
		ガソリン.setValue(String.valueOf(gasolineValue));
		
		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Bus",
					"new Bus(車両価格,ガソリン);");
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
		return " 車両価格,ガソリン ";
	}
	
	public String covis_乗車する(int num) {
		passNumValue += num;
		乗車人数.setValue(String.valueOf(passNumValue));
		return num + "人乗車しました";
	}
	
	public String covis_降車する(int num) {
		passNumValue -= num;
		乗車人数.setValue(String.valueOf(passNumValue));
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
	
	public static String classdef = "" +
"public class Bus extends Car {\n"+
"   int 乗車人数;　\n"+
"   \n"+
"   public Bus() {\n"+
"      車両価格 = 800;\n"+
"      ガソリン = 200;\n"+
"   }\n"+
"   \n"+
"   public Bus(int _車両価格,int _ガソリン) {\n"+
"      super(_車両価格,_ガソリン);\n"+
"   }\n"+
"   \n"+
"   public String 乗車する(int 人数){\n"+
"         乗車人数 += 人数;\n"+
"         return 人数 + \"人乗車しました\";\n"+
"   }\n"+
"   \n"+
"   public String 降車する(int 人数){\n"+
"      乗車人数 -= 人数;\n"+
"      return 人数 + \"人降車しました\";\n"+
"   }\n"+
"}"; 
}
