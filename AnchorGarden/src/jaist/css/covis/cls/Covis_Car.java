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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Car extends Covis_Object {

	private static final long serialVersionUID = -4624235387919516738L;
	public static Color defaultColor = new Color(173, 189, 255);
	public static String varname1 = "車両価格";
	public static String varname2 = "ガソリン";

	public Covis_Car(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_Car(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	Covis_int 車両価格;
	Covis_int ガソリン;
	int gasolineValue;
	PText carValueLabel;
	PText gasolineLabel;
	PPath sup;

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + 車両価格.value + "," + ガソリン.value + ");";
	}

	public void init(boolean isAuto) {
		// 本体の大きさ指定
		setPathToRectangle(0, 0, 190, 120);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// sup作成
		sup = new PPath();
		// 大きさ・形指定
		sup.setPathToRectangle(0, 0, 190, 120);
		sup.setPaint( new Color(173, 189, 255));
		sup.setStroke(basicStroke);
		sup.addAttribute("moveTarget", this);
		sup.addAttribute("tooltip", this);

		// carValueの追加
		車両価格 = new Covis_int(buffer, isAuto,50);
		車両価格.addAttribute("moveTarget", this);
		車両価格.addAttribute("tooltip", this);
		車両価格.addAttribute("popupMenu", new ClassVarMenu(varname1,車両価格,this));
		車両価格.valueText.addAttribute("moveTarget", this);
		車両価格.valueText.addAttribute("tooltip", this);
		車両価格.valueText.addAttribute("popupMenu", new ClassVarMenu(varname1,車両価格,this));
		車両価格.setValue("90");
		// 大きさ位置指定
		車両価格.setScale(0.8f);
		車両価格.offset(140, 10);
		sup.addChild(車両価格);

		// gasolineの追加
		ガソリン = new Covis_int(buffer, isAuto,50);
		ガソリン.addAttribute("moveTarget", this);
		ガソリン.addAttribute("tooltip", this);
		ガソリン.addAttribute("popupMenu", new ClassVarMenu(varname2,ガソリン,this));
		ガソリン.valueText.addAttribute("moveTarget", this);
		ガソリン.valueText.addAttribute("tooltip", this);
		ガソリン.valueText.addAttribute("popupMenu", new ClassVarMenu(varname2,ガソリン,this));
		gasolineValue = 40;
		ガソリン.setValue(String.valueOf(gasolineValue));
		// 大きさ位置指定
		ガソリン.setScale(0.8f);
		ガソリン.offset(140, 70);
		sup.addChild(ガソリン);

		// Labelの表示
		carValueLabel = new PText(車両価格.getClsName() + " " + varname1);
		gasolineLabel = new PText(ガソリン.getClsName() + " " + varname2);
		carValueLabel.scale(1.9f);
		gasolineLabel.scale(1.9f);
		carValueLabel.offset(10, 15);
		gasolineLabel.offset(10, 75);
		carValueLabel.addAttribute("moveTarget", this);
		gasolineLabel.addAttribute("moveTarget", this);
		carValueLabel.addAttribute("tooltip", this);
		gasolineLabel.addAttribute("tooltip", this);
		sup.addChild(carValueLabel);
		sup.addChild(gasolineLabel);

		addChild(sup);

		if (!isAuto) {
			CarConstructorDialog dialog = CarConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of Car",
					"new Car(車両価格,ガソリン);");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}

		// インデックス値を右肩にはりつける 用途不明
		PText ptidx = new PText(varname1);
		ptidx.setScale(0.6);
		ptidx.setOffset(18, 5);
		ptidx.setTextPaint(Color.blue);
		addChild(ptidx);
		ptidx.addAttribute("moveTarget", this);
		ptidx.addAttribute("tooltip", this);

		PText ptidx2 = new PText(varname2);
		ptidx2.setScale(0.6);
		ptidx2.setOffset(77, 15);
		ptidx2.setTextPaint(Color.blue);
		addChild(ptidx2);
		ptidx2.addAttribute("moveTarget", this);
		ptidx2.addAttribute("tooltip", this);
	}

	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_Car(buffer, isAuto);
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
		return " 車両価格,ガソリン ";
	}
	
	// メソッド作成
	public String covis_走行() {
		if(gasolineValue < 5) {
			return "ガソリンが足りません";
		}else{
			gasolineValue -= 5;
			ガソリン.setValue(String.valueOf(gasolineValue));
			return "ガソリンを5消費";
		}
	}

	public String covis_給油(int value) {
		gasolineValue += value;
		ガソリン.setValue(String.valueOf(gasolineValue));
		return "給油しました";
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
"public class Car {\n"+
"   int 車両価格; \n"+
"   int ガソリン;   \n"+
"   \n"+
"   public Car() {\n"+
"      車両価格 = 90;\n"+
"      ガソリン = 40;\n"+
"   }\n"+
"   \n"+
"   public Car(int _車両価格,int _ガソリン) {\n"+
"      車両価格 = _車両価格;\n"+
"      ガソリン = _ガソリン;\n"+
"   }\n"+
"   \n"+
"   public String 走行(){\n"+
"      if (ガソリン < 5) {\n"+
"         return \"ガソリンが足りません\";\n"+
"      } else {\n"+
"         ガソリン -= 5;\n"+
"         return \"ガソリンを5消費\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String 給油(int 給油量){\n"+
"      ガソリン += 給油量;\n"+
"      return \"給油しました\";\n"+
"   }\n"+
"}"; 
}


//コンストラクタ用ダイアログのクラス
class CarConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfcarValue;
	JTextField jtfgasoline;

	JButton ok;
	boolean canceled = true;

	public CarConstructorDialog(JFrame p, Covis_Car car, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfcarValue = new JTextField(car.車両価格.getValue());
		jtfcarValue.setFont(SrcWindow.sans30);
		jtfcarValue.setBackground(Covis_int.defaultColor);
		jtfcarValue.addKeyListener(this);

		jtfgasoline = new JTextField(car.ガソリン.getValue());
		jtfgasoline.setFont(SrcWindow.sans30);
		jtfgasoline.setBackground(Covis_int.defaultColor);
		jtfgasoline.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(2, 3));
		inner.add(new JLabelW(car.車両価格.getClsName()));
		inner.add(new JLabelW(car.varname1));
		inner.add(jtfcarValue);
		inner.add(new JLabelW(car.ガソリン.getClsName()));
		inner.add(new JLabelW(car.varname2));
		inner.add(jtfgasoline);

		getContentPane().add(inner, BorderLayout.CENTER);

		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		// pack();
		setSize(350, 180);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth()) / 2,
				p.getLocation().y + (p.getHeight() - this.getHeight()) / 2);

		jtfcarValue.setCaretPosition(jtfcarValue.getText().length());
		jtfgasoline.setCaretPosition(jtfgasoline.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}
		});

		jtfcarValue.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfcarValue = null;
					jtfgasoline = null;
					dispose();
				}
			}
		});
		jtfgasoline.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfcarValue = null;
					jtfgasoline = null;
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

	public static CarConstructorDialog showDialog(JFrame parent,
			Covis_Car car, String title, String mes1) {
		CarConstructorDialog d = new CarConstructorDialog(parent, car,
				title, mes1);
		d.setVisible(true);
		if (d.jtfcarValue != null && d.jtfgasoline != null){
			car.車両価格.setValue(String.valueOf(Integer.parseInt(d.jtfcarValue.getText())));
			car.ガソリン.setValue(String.valueOf(Integer.parseInt(d.jtfgasoline.getText())));
			car.gasolineValue = Integer.parseInt(d.jtfgasoline.getText());
		}
		return d;
	}
}
