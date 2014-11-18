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
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Super extends Covis_Object {

	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(242, 178, 188);

	public Covis_Super(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_Super(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	Covis_int a;
	VariableM b;
	Covis_Type b_type;
	PText aLabel;
	PText bLabel;
	PPath sup;

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + a.value + ");";
	}

	public void init(boolean isAuto) {
		// 本体の大きさ指定
		setPathToRectangle(0, 0, 100, 100);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup作成
		sup = new PPath();
		// 大きさ・形指定
		sup.setPathToRectangle(0, 0, 100, 100);
		sup.setPaint(new Color(242, 178, 188));
		sup.setStroke(basicStroke);
		sup.addAttribute("moveTarget", this);
		sup.addAttribute("tooltip", this);

		// int型追加
		a = new Covis_int(buffer, isAuto);
		a.addAttribute("moveTarget", this);
		a.addAttribute("tooltip", this);
		a.addAttribute("popupMenu", new ClassVarMenu(a));
		a.valueText.addAttribute("moveTarget", this);
		a.valueText.addAttribute("tooltip", this);
		a.valueText.addAttribute("popupMenu", new ClassVarMenu(a));
		a.setValue("2");
		// 大きさ位置指定
		a.setScale(0.8f);
		a.offset(60, 5);
		sup.addChild(a);

		// String型追加
		// String型のクラス情報を習得
		try {
			Class<?> cla = Class.forName("jaist.css.covis.cls.Covis_String");
			Constructor<?> con = cla.getConstructor(CoVisBuffer.class,
					boolean.class);
			b_type = (Covis_Type) con.newInstance(buffer, true);
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
		}

		b = new VariableM(b_type, null, buffer, "b", this);
		// アンカー登録
		anchors_member.add(b.anchor);
		b.addAttribute("moveTarget", this);
		b.addAttribute("tooltip", this);
		// 位置指定
		b.setOffset(70, 65);
		sup.addChild(b);
		buffer.putHistoryVar("var", b, false);

		// Labelの表示
		aLabel = new PText(a.getClsName());
		bLabel = new PText(b_type.getClsName());
		aLabel.scale(1.8f);
		bLabel.scale(1.8f);
		aLabel.offset(10, 8);
		bLabel.offset(10, 58);
		aLabel.addAttribute("moveTarget", this);
		bLabel.addAttribute("moveTarget", this);
		aLabel.addAttribute("tooltip", this);
		bLabel.addAttribute("tooltip", this);
		sup.addChild(aLabel);
		sup.addChild(bLabel);

		addChild(sup);

		if (!isAuto) {
			SuperConstructorDialog dialog = SuperConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Super",
					"new Super( a );");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}

		// インデックス値を右肩にはりつける 用途不明
		PText ptidx = new PText("a");
		ptidx.setScale(0.6);
		ptidx.setOffset(18, 5);
		ptidx.setTextPaint(Color.blue);
		addChild(ptidx);
		ptidx.addAttribute("moveTarget", this);
		ptidx.addAttribute("tooltip", this);

		PText ptidx2 = new PText("b");
		ptidx2.setScale(0.6);
		ptidx2.setOffset(77, 15);
		ptidx2.setTextPaint(Color.blue);
		addChild(ptidx2);
		ptidx2.addAttribute("moveTarget", this);
		ptidx2.addAttribute("tooltip", this);

	}

	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_Super(buffer, isAuto);
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
		return " a ";
	}

	// メソッド作成
	public void covis_setA(int fa) {
		a.setValue(String.valueOf(fa));
	}

	public void covis_setB(String fb) {
		// String型の作成
		Covis_String newb = new Covis_String(buffer, true);
		newb.setValues(fb);
		// 作成したString型にアンカーをつける
		buffer.putHistoryNew("method", newb, false);
		buffer.objField.addObject(newb);
		Covis_String oldc = (Covis_String) b.anchor.link.dest;
		if (oldc != null) {
			// 既にアンカーが接続されているならそれを外す
			oldc.detach(b.anchor);
		}
		newb.attach(b.anchor);
		buffer.putHistoryNew("method", newb, false);
		newb.setOffsetAlignment(this, 120, 150);
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

// コンストラクタ用ダイアログのクラス
class SuperConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfa;

	JButton ok;
	boolean canceled = true;

	public SuperConstructorDialog(JFrame p, Covis_Super sup, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfa = new JTextField(sup.a.getValue());
		jtfa.setFont(SrcWindow.sans30);
		jtfa.setBackground(Covis_int.defaultColor);
		jtfa.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(1, 3));
		inner.add(new JLabelW("a"));
		inner.add(new JLabelW(sup.a.getClsName()));
		inner.add(jtfa);

		getContentPane().add(inner, BorderLayout.CENTER);

		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		// pack();
		setSize(250, 130);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth()) / 2,
				p.getLocation().y + (p.getHeight() - this.getHeight()) / 2);

		jtfa.setCaretPosition(jtfa.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}
		});

		jtfa.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfa = null;

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

	public static SuperConstructorDialog showDialog(JFrame parent,
			Covis_Super sup, String title, String mes1) {
		SuperConstructorDialog d = new SuperConstructorDialog(parent, sup,
				title, mes1);
		d.setVisible(true);
		if (d.jtfa != null) {
			sup.a.setValue(String.valueOf(Integer.parseInt(d.jtfa.getText())));
		}
		return d;
	}
}