package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Truck extends Covis_Car {

	private static final long serialVersionUID = 2606894600998374563L;
	public static Color defaultColor = new Color(52, 229, 255);
	public static String varname3 = "積載量";

	public Covis_Truck(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
	}

	public Covis_Truck(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + 車両価格.value + "," + ガソリン.value + ");";
	}

	PPath sub;
	Covis_int 積載量;
	PText loadageLabel;
	int loadageValue;

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

		// loadageの追加
		積載量 = new Covis_int(buffer, isAuto);
		積載量.addAttribute("moveTarget", this);
		積載量.addAttribute("tooltip", this);
		積載量.addAttribute("popupMenu", new ClassVarMenu(varname3,積載量,this));
		積載量.valueText.addAttribute("moveTarget", this);
		積載量.valueText.addAttribute("tooltip", this);
		積載量.valueText.addAttribute("popupMenu", new ClassVarMenu(varname3,積載量,this));
		loadageValue = 0;
		積載量.setValue(String.valueOf(loadageValue));
		// 大きさ位置指定
		積載量.setScale(0.8f);
		積載量.offset(140, 130);
		sub.addChild(積載量);
		
		// Labelの表示
		loadageLabel = new PText(積載量.getClsName() + " " + varname3);
		loadageLabel.scale(1.9f);
		loadageLabel.offset(10, 135);
		loadageLabel.addAttribute("moveTarget", this);
		loadageLabel.addAttribute("tooltip", this);;
		sub.addChild(loadageLabel);
		
		addChild(sub);
		
		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Bus",
					"new Truck(車両価格,ガソリン);");
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
		return " 車両価格,ガソリン ";
	}
	
	//メソッド作成
	public String covis_荷物を積む(int value) {
		loadageValue += value;
		積載量.setValue(String.valueOf(loadageValue));
		return value + "kg積みました";
	}
	
	public String covis_荷物を降ろす(int value) {
		loadageValue -= value;
		積載量.setValue(String.valueOf(loadageValue));
		return value + "kg降ろしました";
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
"   int 積載量;  \n"+
"   \n"+
"   public Truck() {\n"+
"      super();\n"+
"   }\n"+
"   \n"+
"   public Truck(int _車両価格,int _ガソリン) {\n"+
"      super(_車両価格,_ガソリン);\n"+
"   }\n"+
"   \n"+
"   public String 荷物を積む(int 量){\n"+
"         積載量 += 量;\n"+
"         return 量 + \"kg積みました\";\n"+
"   }\n"+
"   \n"+
"   public String 荷物を降ろす(int 量){\n"+
"      積載量 -= 量;\n"+
"      return 量 + \"kg降ろしました\";\n"+
"   }\n"+
"}"; 
}
