package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.nodes.PPath;

public class Covis_Cat extends Covis_Animal {

	private static final long serialVersionUID = -98639154708225324L;
	public static Color defaultColor = new Color(255, 150, 128);

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
		return "new " + getClsName() + "(" + age.value + "," + len.value + ");";
	}
	
	PPath sub;
	
	public void init(boolean isAuto) {
		// �e�N���X���쐬
		super.init(true);

		// �{�̂̑傫���w��
		setPathToRectangle(0, 0, 160, 140);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup�쐬
		sub = new PPath();
		// �傫���E�`�w��
		sub.setPathToRectangle(0, 120, 160, 20);
		sub.setPaint(color);
		sub.setStroke(basicStroke);
		sub.addAttribute("moveTarget", this);
		sub.addAttribute("tooltip", this);
		
		addChild(sub);
		
		if (!isAuto) {
			AnimalConstructorDialog dialog = AnimalConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Cat",
					"new Cat(age,length);");
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
		return " age,lenght ";
	}
	
	// ���\�b�h�쐬
	public String covis_voice() {
		return "�~���[�~���[";
	}
	
	public String toString() {
		/*
		 * if (b.anchor.link.dest != null) { return "int a = " + a.getValue() +
		 * "\n String b = \n\"" + b.anchor.link.dest.toString() + "\""; } else {
		 * return "int a = " + a.getValue(); }
		 */
		return "not implemented ";
	}
}