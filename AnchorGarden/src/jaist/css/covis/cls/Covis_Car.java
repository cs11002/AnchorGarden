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
	public static String varname1 = "Ô—¼‰¿Ši";
	public static String varname2 = "ƒKƒ\ƒŠƒ“";

	public Covis_Car(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_Car(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	Covis_int Ô—¼‰¿Ši;
	Covis_int ƒKƒ\ƒŠƒ“;
	int gasolineValue;
	PText carValueLabel;
	PText gasolineLabel;
	PPath sup;

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + Ô—¼‰¿Ši.value + "," + ƒKƒ\ƒŠƒ“.value + ");";
	}

	public void init(boolean isAuto) {
		// –{‘Ì‚Ì‘å‚«‚³w’è
		setPathToRectangle(0, 0, 190, 120);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// supì¬
		sup = new PPath();
		// ‘å‚«‚³EŒ`w’è
		sup.setPathToRectangle(0, 0, 190, 120);
		sup.setPaint( new Color(173, 189, 255));
		sup.setStroke(basicStroke);
		sup.addAttribute("moveTarget", this);
		sup.addAttribute("tooltip", this);

		// carValue‚Ì’Ç‰Á
		Ô—¼‰¿Ši = new Covis_int(buffer, isAuto,50);
		Ô—¼‰¿Ši.addAttribute("moveTarget", this);
		Ô—¼‰¿Ši.addAttribute("tooltip", this);
		Ô—¼‰¿Ši.addAttribute("popupMenu", new ClassVarMenu(varname1,Ô—¼‰¿Ši,this));
		Ô—¼‰¿Ši.valueText.addAttribute("moveTarget", this);
		Ô—¼‰¿Ši.valueText.addAttribute("tooltip", this);
		Ô—¼‰¿Ši.valueText.addAttribute("popupMenu", new ClassVarMenu(varname1,Ô—¼‰¿Ši,this));
		Ô—¼‰¿Ši.setValue("90");
		// ‘å‚«‚³ˆÊ’uw’è
		Ô—¼‰¿Ši.setScale(0.8f);
		Ô—¼‰¿Ši.offset(140, 10);
		sup.addChild(Ô—¼‰¿Ši);

		// gasoline‚Ì’Ç‰Á
		ƒKƒ\ƒŠƒ“ = new Covis_int(buffer, isAuto,50);
		ƒKƒ\ƒŠƒ“.addAttribute("moveTarget", this);
		ƒKƒ\ƒŠƒ“.addAttribute("tooltip", this);
		ƒKƒ\ƒŠƒ“.addAttribute("popupMenu", new ClassVarMenu(varname2,ƒKƒ\ƒŠƒ“,this));
		ƒKƒ\ƒŠƒ“.valueText.addAttribute("moveTarget", this);
		ƒKƒ\ƒŠƒ“.valueText.addAttribute("tooltip", this);
		ƒKƒ\ƒŠƒ“.valueText.addAttribute("popupMenu", new ClassVarMenu(varname2,ƒKƒ\ƒŠƒ“,this));
		gasolineValue = 40;
		ƒKƒ\ƒŠƒ“.setValue(String.valueOf(gasolineValue));
		// ‘å‚«‚³ˆÊ’uw’è
		ƒKƒ\ƒŠƒ“.setScale(0.8f);
		ƒKƒ\ƒŠƒ“.offset(140, 70);
		sup.addChild(ƒKƒ\ƒŠƒ“);

		// Label‚Ì•\¦
		carValueLabel = new PText(Ô—¼‰¿Ši.getClsName() + " " + varname1);
		gasolineLabel = new PText(ƒKƒ\ƒŠƒ“.getClsName() + " " + varname2);
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
					"new Car(Ô—¼‰¿Ši,ƒKƒ\ƒŠƒ“);");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}

		// ƒCƒ“ƒfƒbƒNƒX’l‚ğ‰EŒ¨‚É‚Í‚è‚Â‚¯‚é —p“r•s–¾
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
		return " Ô—¼‰¿Ši,ƒKƒ\ƒŠƒ“ ";
	}
	
	// ƒƒ\ƒbƒhì¬
	public String covis_‘–s‚·‚é() {
		if(gasolineValue < 5) {
			return "ƒKƒ\ƒŠƒ“‚ª‘«‚è‚Ü‚¹‚ñ";
		}else{
			gasolineValue -= 5;
			ƒKƒ\ƒŠƒ“.setValue(String.valueOf(gasolineValue));
			return "ƒKƒ\ƒŠƒ“‚ğ5Á”ï";
		}
	}

	public String covis_‹‹–û‚·‚é(int value) {
		gasolineValue += value;
		ƒKƒ\ƒŠƒ“.setValue(String.valueOf(gasolineValue));
		return "‹‹–û‚µ‚Ü‚µ‚½";
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
"   int Ô—¼‰¿Ši; \n"+
"   int ƒKƒ\ƒŠƒ“;   \n"+
"   \n"+
"   public Car() {\n"+
"      Ô—¼‰¿Ši = 90;\n"+
"      ƒKƒ\ƒŠƒ“ = 40;\n"+
"   }\n"+
"   \n"+
"   public Car(int _Ô—¼‰¿Ši,int _ƒKƒ\ƒŠƒ“) {\n"+
"      Ô—¼‰¿Ši = _Ô—¼‰¿Ši;\n"+
"      ƒKƒ\ƒŠƒ“ = _ƒKƒ\ƒŠƒ“;\n"+
"   }\n"+
"   \n"+
"   public String ‘–s‚·‚é(){\n"+
"      if (ƒKƒ\ƒŠƒ“ < 5) {\n"+
"         return \"ƒKƒ\ƒŠƒ“‚ª‘«‚è‚Ü‚¹‚ñ\";\n"+
"      } else {\n"+
"         ƒKƒ\ƒŠƒ“ -= 5;\n"+
"         return \"ƒKƒ\ƒŠƒ“‚ğ5Á”ï\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String ‹‹–û‚·‚é(int ‹‹–û—Ê){\n"+
"      ƒKƒ\ƒŠƒ“ += ‹‹–û—Ê;\n"+
"      return \"‹‹–û‚µ‚Ü‚µ‚½\";\n"+
"   }\n"+
"}"; 
}


//ƒRƒ“ƒXƒgƒ‰ƒNƒ^—pƒ_ƒCƒAƒƒO‚ÌƒNƒ‰ƒX
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
		jtfcarValue = new JTextField(car.Ô—¼‰¿Ši.getValue());
		jtfcarValue.setFont(SrcWindow.sans30);
		jtfcarValue.setBackground(Covis_int.defaultColor);
		jtfcarValue.addKeyListener(this);

		jtfgasoline = new JTextField(car.ƒKƒ\ƒŠƒ“.getValue());
		jtfgasoline.setFont(SrcWindow.sans30);
		jtfgasoline.setBackground(Covis_int.defaultColor);
		jtfgasoline.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(2, 3));
		inner.add(new JLabelW(car.Ô—¼‰¿Ši.getClsName()));
		inner.add(new JLabelW(car.varname1));
		inner.add(jtfcarValue);
		inner.add(new JLabelW(car.ƒKƒ\ƒŠƒ“.getClsName()));
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
			car.Ô—¼‰¿Ši.setValue(String.valueOf(Integer.parseInt(d.jtfcarValue.getText())));
			car.ƒKƒ\ƒŠƒ“.setValue(String.valueOf(Integer.parseInt(d.jtfgasoline.getText())));
			car.gasolineValue = Integer.parseInt(d.jtfgasoline.getText());
		}
		return d;
	}
}
