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

public class Covis_CapsuledAccount extends Covis_Object {

	private static final long serialVersionUID = 6631160996844648983L;
	public static Color defaultColor = new Color(182, 196, 84);
	public static String varname1 = "残高";
	public static String varname2 = "履歴";
	
	public Covis_CapsuledAccount(CoVisBuffer buf, boolean isAuto) {
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}

	public Covis_CapsuledAccount(Color c, CoVisBuffer buf, boolean isAuto) {
		super(c, buf, isAuto);
	}

	Covis_int 残高;
	int remainderValue;
	VariableM 履歴;
	Covis_Type history_type;
	PText remainderLabel;
	PText historyLabel;

	public String getConstructorInfo() {
		return "new " + getClsName() + "(" + 残高.value + ");";
	}

	//カプセル化済み
	public void init(boolean isAuto) {
		// 本体の大きさ指定
		setPathToRectangle(0, 0, 200, 120);
		addAttribute("moveTarget", this);
		addAttribute("tooltip", this);

		// remainderの追加
		残高 = new Covis_int(buffer, isAuto,80);
		残高.addAttribute("moveTarget", this);
		残高.addAttribute("tooltip", this);
		残高.valueText.addAttribute("moveTarget", this);
		残高.valueText.addAttribute("tooltip", this);
		remainderValue = 0;
		残高.setValue(String.valueOf(remainderValue));
		// 大きさ位置指定
		残高.setScale(0.8f);
		残高.offset(125, 5);
		addChild(残高);

		// String型追加
		// String型のクラス情報を習得
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
		履歴 = new VariableM(history_type, null, buffer, "history", this);
		// アンカー登録
		anchors_member.add(履歴.anchor);
		履歴.addAttribute("moveTarget", this);
		履歴.addAttribute("tooltip", this);
		// 位置指定
		履歴.setOffset(135, 75);
		addChild(履歴);
		buffer.putHistoryVar("var", 履歴, false);

		// Labelの表示
		remainderLabel = new PText(残高.getClsName() + " " + varname1);
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
			CapsuledAccountConstructorDialog dialog = CapsuledAccountConstructorDialog.showDialog(
					buffer.getWindow().frame, this, "Constructor of CapsuledAccount",
					"new CapsuledAccount( 残高 );");
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
	}

	public void createIns() {
		// String[]型の作成
		Covis_Array historyIns = (Covis_Array)history_type;
		// 作成したString[]型にアンカーをつける
		buffer.objField.addObject(historyIns);
		historyIns.attach(履歴.anchor);
		historyIns.setOffsetAlignment(this, 200, 150);
	}
	
	public void attach(Anchor anchor){
		super.attach(anchor);
		//履歴用アンカーは変更できないようにする
		履歴.setEnabled(false);
		履歴.anchor.anchortab.addAttribute("moveTarget", 履歴.anchor.anchortab);
		履歴.anchor.anchortab.addAttribute("tooltip", 履歴.anchor.anchortab);
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
		return " 残高 ";
	}

	// メソッド作成
	public String covis_預金する(int value) {
		if(value % 1000 == 0) {
			remainderValue += value;
			残高.setValue(String.valueOf(remainderValue));
			covis_履歴登録(value,0);
			return value + "円預金しました";
		}else{
			return "1000円単位で入力してください";
		}
	}

	public String covis_引き出す(int value) {
		if(value % 1000 == 0) {
			if(remainderValue >= value) {
				remainderValue -= value;
				残高.setValue(String.valueOf(remainderValue));
				covis_履歴登録(value,1);
				return value + "円引き出ししました";
			}else{
				return "残高が足りません";
			}
		}else{
			return "1000円単位で入力してください";
		}
	}
	
	public String covis_残高照会() {
		return "残高は" + remainderValue + "円です";
	}
	
	private void covis_履歴登録(int value,int type) {
		int i;
		Covis_String newhis = new Covis_String(buffer, true);
		if(type == 0) {
			newhis.setValues("預金 " + value);
		}else{
			newhis.setValues("引出 " + value);
		}
		buffer.objField.addObject(newhis);
		Covis_Array a = (Covis_Array)履歴.anchor.destObject;
		for(i=0;i<a.anchors_member.size();i++) {
			if(a.anchors_member.get(i).destObject == null) {
				newhis.attach(a.anchors_member.get(i));
				break;
			}
		}
		newhis.setOffsetAlignment(履歴.anchor.destObject, 20*i, 60+60*i);
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
"public class CapsuledAccount {\n"+
"   private int 残高;     \n"+
"   private String[] 履歴;\n"+
"   \n"+
"   public CapsuledAccount() {\n"+
"      残高 = 0;\n"+
"      履歴 = new String[10];\n"+
"   }\n"+
"   \n"+
"   public CapsuledAccount(int _残高) {\n"+
"      残高 = _残高;\n"+
"      履歴 = new String[10];\n"+
"   }\n"+
"   \n"+
"   public String 預金する(int 金額){\n"+
"      if(金額 % 1000 == 0) {\n"+
"         残高 += 金額;\n"+
"         履歴登録(金額,0);\n"+
"         return 金額 + \"円預金しました\";\n"+
"      }else{\n"+
"         return \"1000円単位で入力してください\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String 引き出す(int 金額){\n"+
"      if(金額 % 1000 == 0) {\n"+
"         if(残高 >= 金額){\n"+
"            残高 -= 金額;\n"+
"            履歴登録(金額,1);\n"+
"            return 金額 + \"円引き出ししました\";\n"+
"         }else{\n"+
"            return \"残高が足りません\";\n"+
"         }\n"+
"      }else{\n"+
"         return \"1000円単位で入力してください\";\n"+
"      }\n"+
"   }\n"+
"   \n"+
"   public String 残高照会(){\n"+
"      return \"残高は\"　+ 残高 + \"円です\" ;\n"+
"   }\n"+
"   \n"+
"   private void 履歴登録(int 金額,int 種類) {\n"+
"      int i;\n"+
"      for(i=0,i<履歴.length;i++){\n"+
"         if(履歴[i] == null){\n"+
"            if(種類 == 0){\n"+
"               履歴[i] = \"預金 \" + 金額;\n"+
"            }else{\n"+
"               履歴[i] = \"引出 \" + 金額;\n"+
"            }\n"+
"            break;\n"+
"         }\n"+
"      }\n"+
"   }\n"+
"}"; 
}


//コンストラクタ用ダイアログのクラス
class CapsuledAccountConstructorDialog extends JDialog implements KeyListener {

	private static final long serialVersionUID = 1L;

	JFrame parent;

	JTextField jtfremainder;

	JButton ok;
	boolean canceled = true;

	public CapsuledAccountConstructorDialog(JFrame p, Covis_CapsuledAccount acc, String title,
			String mes1) {
		super(p, title, true);
		parent = p;
		jtfremainder = new JTextField(acc.残高.getValue());
		jtfremainder.setFont(SrcWindow.sans30);
		jtfremainder.setBackground(Covis_int.defaultColor);
		jtfremainder.addKeyListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(1, 3));
		inner.add(new JLabelW(acc.残高.getClsName()));
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

	public static CapsuledAccountConstructorDialog showDialog(JFrame parent,
			Covis_CapsuledAccount acc, String title, String mes1) {
		CapsuledAccountConstructorDialog d = new CapsuledAccountConstructorDialog(parent, acc,
				title, mes1);
		d.setVisible(true);
		if (d.jtfremainder != null){
			acc.残高.setValue(String.valueOf(Integer.parseInt(d.jtfremainder.getText())));
			acc.remainderValue = Integer.parseInt(d.jtfremainder.getText());
		}
		return d;
	}
}
