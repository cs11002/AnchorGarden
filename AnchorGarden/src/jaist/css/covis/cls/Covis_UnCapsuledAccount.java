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

import edu.umd.cs.piccolo.nodes.PText;

public class Covis_UnCapsuledAccount extends Covis_Object {

	private static final long serialVersionUID = -8694257030378483023L;
	public static Color defaultColor = new Color(242, 197, 124);
	public static String varname1 = "remainder";
	public static String varname2 = "history";

	public Covis_UnCapsuledAccount(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_UnCapsuledAccount(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	Covis_int remainder;
	int remainderValue;
	VariableM history;
	Covis_Type history_type;
	PText remainderLabel;
	PText historyLabel;

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + remainder.value + ");";
	}

	public void init(boolean isAuto) {
		// –{‘Ì‚Ì‘å‚«‚³w’è
		setPathToRectangle(0, 0, 220, 120);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// remainder‚Ì’Ç‰Á
		remainder = new Covis_int(buffer, isAuto,80);
		remainder.addAttribute("moveTarget", this);
		remainder.addAttribute("tooltip", this);
		remainder.addAttribute("popupMenu", new ClassVarMenu(varname1,remainder,this));
		remainder.valueText.addAttribute("moveTarget", this);
		remainder.valueText.addAttribute("tooltip", this);
		remainder.valueText.addAttribute("popupMenu", new ClassVarMenu(varname1,remainder,this));
		remainderValue = 0;
		remainder.setValue(String.valueOf(remainderValue));
		// ‘å‚«‚³ˆÊ’uw’è
		remainder.setScale(0.8f);
		remainder.offset(145, 5);
		addChild(remainder);

		// StringŒ^’Ç‰Á
		// StringŒ^‚ÌƒNƒ‰ƒXî•ñ‚ğK“¾
		/*try {
			Class<?> cla = Class.forName("jaist.css.covis.cls.Covis_Array");
			Constructor<?> con = cla.getConstructor(CoVisBuffer.class,
					boolean.class,String.class);
			history_type = (Covis_Type) con.newInstance(buffer,true,"String");
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
		}*/
		history_type = new Covis_Array(buffer,true,10,"String");
		history = new VariableM(history_type, null, buffer, "history", this);
		// ƒAƒ“ƒJ[“o˜^
		anchors_member.add(history.anchor);
		history.addAttribute("moveTarget", this);
		history.addAttribute("tooltip", this);
		// ˆÊ’uw’è
		history.setOffset(155, 75);
		addChild(history);
		buffer.putHistoryVar("var", history, false);

		// Label‚Ì•\¦
		remainderLabel = new PText(remainder.getClsName() + " " + varname1);
		historyLabel = new PText(history_type.getClsName() + " " + varname2);
		remainderLabel.scale(1.8f);
		historyLabel.scale(1.8f);
		remainderLabel.offset(10, 15);
		historyLabel.offset(10, 75);
		remainderLabel.addAttribute("moveTarget", this);
		historyLabel.addAttribute("moveTarget", this);
		remainderLabel.addAttribute("tooltip", this);
		historyLabel.addAttribute("tooltip", this);
		addChild(remainderLabel);
		addChild(historyLabel);

		if (!isAuto) {
			UnCapsuledAccountConstructorDialog dialog = UnCapsuledAccountConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of UnCapsuledAccount",
					"new UnCapsuledAccount( remainder );");
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
	}

	public void setValue() {
		remainderValue = Integer.parseInt(remainder.value);
	}
	
	public void createIns() {
		// String[]Œ^‚Ìì¬
		Covis_Array historyIns = (Covis_Array)history_type;
		// ì¬‚µ‚½String[]Œ^‚ÉƒAƒ“ƒJ[‚ğ‚Â‚¯‚é
		buffer.objField.addObject(historyIns);
		historyIns.attach(history.anchor);
		historyIns.setOffsetAlignment(this, 200, 150);
		for(Anchor a:historyIns.anchors_member) {
			a.srcVariable.setEnabled(true);
		}
	}

	public Covis_Object Covis_clone(boolean isAuto) {
		return new Covis_UnCapsuledAccount(buffer, isAuto);
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
		return " remainder ";
	}

	// ƒƒ\ƒbƒhì¬
	public String covis_—a‹à‚·‚é(int value) {
		if(value % 1000 == 0) {
			remainderValue += value;
			remainder.setValue(String.valueOf(remainderValue));
			covis_—š—ğ“o˜^(value,0);
			return value + "‰~—a‹à‚µ‚Ü‚µ‚½";
		}else{
			return "1000‰~’PˆÊ‚Å“ü—Í‚µ‚Ä‚­‚¾‚³‚¢";
		}
	}

	public String covis_ˆøo(int value) {
		if(value % 1000 == 0) {
			if(remainderValue >= value) {
				remainderValue -= value;
				remainder.setValue(String.valueOf(remainderValue));
				covis_—š—ğ“o˜^(value,1);
				return value + "‰~ˆø‚«o‚µ‚µ‚Ü‚µ‚½";
			}else{
				return "c‚‚ª‘«‚è‚Ü‚¹‚ñ";
			}
		}else{
			return "1000‰~’PˆÊ‚Å“ü—Í‚µ‚Ä‚­‚¾‚³‚¢";
		}
	}
	
	public String covis_c‚Æ‰ï() {
		return "c‚‚Í" + remainderValue + "‰~‚Å‚·";
	}
	
	public void covis_—š—ğ“o˜^(int value,int type) {
		int i;
		Covis_String newhis = new Covis_String(buffer, true);
		if(type == 0) {
			newhis.setValues("—a‹à " + value);
		}else{
			newhis.setValues("ˆøo " + value);
		}
		buffer.objField.addObject(newhis);
		Covis_Array a = (Covis_Array)history.anchor.destObject;
		for(i=0;i<a.anchors_member.size();i++) {
			if(a.anchors_member.get(i).destObject == null) {
				newhis.attach(a.anchors_member.get(i));
				break;
			}
		}
		newhis.setOffsetAlignment(history.anchor.destObject, 20*i, 60+60*i);
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
"public class UnCapsuledAccount {\n"+
"   public int remainder;      //c‚\n"+
"   public String[] history;   //—š—ğ\n"+
"   \n"+
"   public UnCapsuledAccount() {\n"+
"      remainder = 0;\n"+
"      history = new String[10];\n"+
"   }\n"+
"   \n"+
"   public UnCapsuledAccount(int _remainder) {\n"+
"      remainder = _remainder;\n"+
"      history = new String[10];\n"+
"   }\n"+
"   \n"+
"   public String —a‹à‚·‚é(int value){\n"+
"      if(value % 1000 == 0) {\n"+
"         remainder += value;\n"+
"         —š—ğ“o˜^(value,0);\n"+
"         return value + \"‰~—a‹à‚µ‚Ü‚µ‚½\";\n"+
"      }else{\n"+
"         return \"1000‰~’PˆÊ‚Å“ü—Í‚µ‚Ä‚­‚¾‚³‚¢\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String ˆø‚«o‚·(int value){\n"+
"      if(value % 1000 == 0) {\n"+
"         if(remainder >= value){\n"+
"            remainder -= value;\n"+
"            —š—ğ“o˜^(value,1);\n"+
"            return value + \"‰~ˆø‚«o‚µ‚µ‚Ü‚µ‚½\";\n"+
"         }else{\n"+
"            return \"c‚‚ª‘«‚è‚Ü‚¹‚ñ\";\n"+
"         }\n"+
"      }else{\n"+
"         return \"1000‰~’PˆÊ‚Å“ü—Í‚µ‚Ä‚­‚¾‚³‚¢\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String c‚Æ‰ï(){\n"+
"      return \"c‚‚Í\"@+ value + \"‰~‚Å‚·\" ;\n"+
"   }\n"+
"   \n"+
"   public void —š—ğ“o˜^(int value,int type) {\n"+
"      int i;\n"+
"      for(i=0,i<history.length;i++){\n"+
"         if(history[i] == null){\n"+
"            if(type == 0){\n"+
"               history[i] = \"—a‹à \" + value;\n"+
"            }else{\n"+
"               history[i] = \"ˆøo \" + value;\n"+
"            }\n"+
"            break;\n"+
"         }\n"+
"      }\n"+
"   }\n"+
"}"; 
}


//ƒRƒ“ƒXƒgƒ‰ƒNƒ^—pƒ_ƒCƒAƒƒO‚ÌƒNƒ‰ƒX
class UnCapsuledAccountConstructorDialog extends JDialog implements KeyListener {

	private static final long serialVersionUID = 1L;

	JFrame parent;

	JTextField jtfremainder;

	JButton ok;
	boolean canceled = true;

	public UnCapsuledAccountConstructorDialog(JFrame p, Covis_UnCapsuledAccount acc, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfremainder = new JTextField(acc.remainder.getValue());
		jtfremainder.setFont(SrcWindow.sans30);
		jtfremainder.setBackground(Covis_int.defaultColor);
		jtfremainder.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(1, 3));
		inner.add(new JLabelW(acc.remainder.getClsName()));
		inner.add(new JLabelW(acc.varname1));
		inner.add(jtfremainder);

		getContentPane().add(inner, BorderLayout.CENTER);

		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
		// pack();
		setSize(350, 120);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth()) / 2,
				p.getLocation().y + (p.getHeight() - this.getHeight()) / 2);

		jtfremainder.setCaretPosition(jtfremainder.getText().length());

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}
		});

		jtfremainder.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfremainder = null;
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

	public static UnCapsuledAccountConstructorDialog showDialog(JFrame parent,
			Covis_UnCapsuledAccount acc, String title, String mes1) {
		UnCapsuledAccountConstructorDialog d = new UnCapsuledAccountConstructorDialog(parent, acc,
				title, mes1);
		d.setVisible(true);
		if (d.jtfremainder != null){
			acc.remainder.setValue(String.valueOf(Integer.parseInt(d.jtfremainder.getText())));
			acc.remainderValue = Integer.parseInt(d.jtfremainder.getText());
		}
		return d;
	}
}

