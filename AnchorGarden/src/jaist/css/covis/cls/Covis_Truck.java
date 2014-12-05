package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Truck extends Covis_Car {

	private static final long serialVersionUID = 2606894600998374563L;
	public static Color defaultColor = new Color(52, 229, 255);
	public static String varname3 = "loadage";

	public Covis_Truck(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
	}

	public Covis_Truck(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + carValue.value + "," + gasoline.value + ");";
	}

	PPath sub;
	Covis_int loadage;
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
		loadage = new Covis_int(buffer, isAuto);
		loadage.addAttribute("moveTarget", this);
		loadage.addAttribute("tooltip", this);
		loadage.addAttribute("popupMenu", new ClassVarMenu(varname3,loadage,this));
		loadage.valueText.addAttribute("moveTarget", this);
		loadage.valueText.addAttribute("tooltip", this);
		loadage.valueText.addAttribute("popupMenu", new ClassVarMenu(varname3,loadage,this));
		loadageValue = 0;
		loadage.setValue(String.valueOf(loadageValue));
		// 大きさ位置指定
		loadage.setScale(0.8f);
		loadage.offset(140, 130);
		sub.addChild(loadage);
		
		// Labelの表示
		loadageLabel = new PText(loadage.getClsName() + " " + varname3);
		loadageLabel.scale(1.9f);
		loadageLabel.offset(10, 135);
		loadageLabel.addAttribute("moveTarget", this);
		loadageLabel.addAttribute("tooltip", this);;
		sub.addChild(loadageLabel);
		
		addChild(sub);
		
		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Bus",
					"new Truck(carValue,gasoline);");
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
		return " carValue,gasoline ";
	}
	
	//メソッド作成
	public String covis_荷物を積む(int value) {
		loadageValue += value;
		loadage.setValue(String.valueOf(loadageValue));
		return value + "kg積みました";
	}
	
	public String covis_荷物を降ろす(int value) {
		loadageValue -= value;
		loadage.setValue(String.valueOf(loadageValue));
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
"   int loadage;   //積載量\n"+
"   \n"+
"   public Truck() {\n"+
"      super();\n"+
"   }\n"+
"   \n"+
"   public Truck(int _carValue,int _gasoline) {\n"+
"      super(_carValue,_gasoline);\n"+
"   }\n"+
"   \n"+
"   public String 荷物を積む(int value){\n"+
"         loadage += value;\n"+
"         return value + \"kg積みました\";\n"+
"   }\n"+
"   \n"+
"   public String 荷物を降ろす(int value){\n"+
"      loadage -= value;\n"+
"      return value + \"kg降ろしました\";\n"+
"   }\n"+
"}"; 
}
