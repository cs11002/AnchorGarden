package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class Covis_Cat extends Covis_Animal {

	private static final long serialVersionUID = -98639154708225324L;
	public static Color defaultColor = new Color(63, 221, 77);

	public Covis_Cat(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_Cat(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}
	
	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + 年齢.value + "," + 体長.value + ");";
	}
	
	PPath sub;
	
	public void init(boolean isAuto) {
		// 親クラス分作成
		super.init(true);

		// 本体の大きさ指定
		setPathToRectangle(0, 0, 160, 140);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup作成
		sub = new PPath();
		// 大きさ・形指定
		sub.setPathToRectangle(0, 120, 160, 20);
		sub.setPaint(color);
		sub.setStroke(basicStroke);
		sub.addAttribute("moveTarget", this);
		sub.addAttribute("tooltip", this);
		
		addChild(sub);
		
		年齢.setValue("9");
		体長.setValue("15");
		
		if (!isAuto) {
			AnimalConstructorDialog dialog = AnimalConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Cat",
					"new Cat(年齢,体長);");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}
	}
	
	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_Cat(buffer, isAuto);
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
			if(a.srcVariable.cv_class instanceof Covis_Cat) {
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
		return " 年齢,体長 ";
	}
	
	// メソッド作成
	public String covis_鳴く() {
		return "ミャーミャー";
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
"public abstract class Cat extends Animal {\n"+
"   \n"+
"   public Cat() {\n"+
"      年齢 = 9;\n"+
"      体長 = 15;\n"+
"   }\n"+
"   \n"+
"   public Cat(int _年齢,int _体長) {\n"+
"      super(_年齢,_体長);\n"+
"   }\n"+
"   \n"+
"   public String 鳴く(){\n"+
"      return \"ミャーミャー\";\n"+
"   }\n"+ 
"}"; 
}
