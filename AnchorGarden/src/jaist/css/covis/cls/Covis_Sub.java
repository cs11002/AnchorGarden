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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Sub extends Covis_Super {

	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(255, 100, 188);

	public Covis_Sub(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
	}

	public Covis_Sub(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	public String getConstructorInfo() {
		return "new " + getClsName() + "()";
	}

	Covis_char c;
	VariableM d;
	Covis_Type d_type;
	PText cLabel;
	PText dLabel;
	PPath sub;

	public void init(boolean isAuto) {
		// 親クラス分作成
		super.init(true);

		// 本体の大きさ指定
		setPathToRectangle(0, 0, 100, 200);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup作成
		sub = new PPath();
		// 大きさ・形指定
		sub.setPathToRectangle(0, 100, 100, 100);
		sub.setPaint(color);
		sub.setStroke(basicStroke);
		sub.addAttribute("moveTarget", this);
		sub.addAttribute("tooltip", this);

		// char型追加
		c = new Covis_char(buffer, isAuto);
		c.addAttribute("moveTarget", this);
		c.addAttribute("tooltip", this);
		c.valueText.addAttribute("moveTarget", this);
		c.valueText.addAttribute("tooltip", this);
		c.setValue("x");
		// 大きさ位置指定
		c.setScale(0.8f);
		c.offset(60, 105);
		sub.addChild(c);

		// Object型追加
		// Object型のクラス情報を習得
		try {
			Class<?> cla = Class.forName("jaist.css.covis.cls.Covis_Object");
			Constructor<?> con = cla.getConstructor(CoVisBuffer.class,
					boolean.class);
			d_type = (Covis_Type) con.newInstance(buffer, true);
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

		d = new VariableM(d_type, null, buffer, "d", this);
		//アンカー登録
		anchors_member.add(d.anchor);
		d.addAttribute("moveTarget", this);
		d.addAttribute("tooltip", this);
		//位置指定
		d.setOffset(70, 165);
		sub.addChild(d);
		buffer.putHistoryVar("var", d, false);

		// Labelの表示
		cLabel = new PText(c.getClsName());
		dLabel = new PText(d_type.getClsName());
		cLabel.scale(1.8f);
		dLabel.scale(1.8f);
		cLabel.offset(10, 108);
		dLabel.offset(10, 158);
		cLabel.addAttribute("moveTarget", this);
		dLabel.addAttribute("moveTarget", this);
		cLabel.addAttribute("tooltip", this);
		dLabel.addAttribute("tooltip", this);
		sub.addChild(cLabel);
		sub.addChild(dLabel);

		addChild(sub);

		if (!isAuto) {
			SubConstructorDialog dialog = SubConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Sub",
					"new Sub( a , c );");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}

	}

	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_Random(buffer, isAuto);
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
			if(a.srcVariable.cv_class instanceof Covis_Sub) {
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
		return " ";
	}

	// メソッド作成
	public void covis_setC(String fc) {
		c.setValue(fc.trim().substring(0, 1));
	}

	public void covis_setD() {
		// String型の作成
		Covis_Object newd = new Covis_Object(buffer, true);
		// 作成したString型にアンカーをつける
		buffer.putHistoryNew("method", newd, false);
		buffer.objField.addObject(newd);
		Covis_Object oldd = (Covis_Object) d.anchor.link.dest;
		if (oldd != null) {
			// 既にアンカーが接続されているならそれを外す
			oldd.detach(d.anchor);
		}
		newd.attach(d.anchor);
		newd.setOffsetAlignment(this, 120, 150);
	}

}

//コンストラクタ用ダイアログのクラス
class SubConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfa;
	JTextField jtfc;

	JButton ok;
	boolean canceled = true;

	public SubConstructorDialog(JFrame p, Covis_Sub sub, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfa = new JTextField(sub.a.getValue());
		jtfa.setFont(SrcWindow.sans30);
		jtfa.setBackground(Covis_int.defaultColor);
		jtfa.addKeyListener(this);

		jtfc = new JTextField(sub.c.getValue());
		jtfc.setFont(SrcWindow.sans30);
		jtfc.setBackground(Covis_int.defaultColor);
		jtfc.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(2, 3));
		inner.add(new JLabelW("a"));
		inner.add(new JLabelW(sub.a.getClsName()));
		inner.add(jtfa);
		inner.add(new JLabelW("c"));
		inner.add(new JLabelW(sub.c.getClsName()));
		inner.add(jtfc);

		getContentPane().add(inner, BorderLayout.CENTER);

		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		// pack();
		setSize(250, 180);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth()) / 2,
				p.getLocation().y + (p.getHeight() - this.getHeight()) / 2);

		jtfa.setCaretPosition(jtfa.getText().length());
		jtfc.setCaretPosition(jtfc.getText().length());

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

	public static SubConstructorDialog showDialog(JFrame parent,
			Covis_Sub sub, String title, String mes1) {
		SubConstructorDialog dia = new SubConstructorDialog(parent, sub,
				title, mes1);
		dia.setVisible(true);
		if (dia.jtfa != null && dia.jtfc != null) {
			sub.a.setValue(String.valueOf(Integer.parseInt(dia.jtfa.getText())));
			sub.c.setValue((dia.jtfc.getText().trim().substring(0, 1)));
		}
		return dia;
	}
}