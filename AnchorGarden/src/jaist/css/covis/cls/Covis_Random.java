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

public class Covis_Random extends Covis_Object {
	
	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(242,178,188);
	public Covis_Random(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_Random(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}
	
	Covis_int a;
	Covis_char b;
	VariableM c;
	Covis_Type c_type;
	PText aLabel;
	PText bLabel;
	PText cLabel;
	public String getConstructorInfo() {
		return "new "+getClsName()+"("+a.value+","+b.value+");";
	}
	
	public void init(boolean isAuto){
		//大きさ・形指定
		setPathToRectangle(0, 0, 100,150);
		//int型追加
		a = new Covis_int(buffer, isAuto);
		a.addAttribute("moveTarget", this);
		a.addAttribute("tooltip", this);
		a.valueText.addAttribute("moveTarget", this);
		a.valueText.addAttribute("tooltip", this);
		a.setValue("2");
		//char型追加
		b = new Covis_char(buffer, isAuto);
		b.addAttribute("moveTarget", this);
		b.addAttribute("tooltip", this);
		b.valueText.addAttribute("moveTarget", this);
		b.valueText.addAttribute("tooltip", this);
		b.setValue("a");
		
		if (!isAuto){
			RandomConstructorDialog dialog = RandomConstructorDialog.showDialog(buffer.getWindow().frame, this, "Constructor of Random", "new Random( a , b );");
			if (dialog.isCanceled()) {
				this.setVisible(false);
				return;
			}
		}
		
		//int型・char型を表示
		a.setScale(0.8f);
		b.setScale(0.8f);
		a.offset(60, 5);
		b.offset(60, 55);
		addChild(a);
		addChild(b);
		
		//String型を表示
		c_type = new Covis_String(buffer,isAuto);
		c = new VariableM(c_type,null,buffer,"c",this);
		addChild(c);
		c.setOffset(70,115);
		anchors_member.add(c.anchor);
		c.addAttribute("moveTarget", this);
		c.addAttribute("tooltip", this);
		buffer.putHistoryVar("var", c, false);
		
		//Labelの表示
		aLabel = new PText(a.getClsName());
		bLabel = new PText(b.getClsName());
		cLabel = new PText(c_type.getClsName());
		aLabel.scale(1.8f);;
		bLabel.scale(1.8f);
		cLabel.scale(1.8f);
		aLabel.offset(10, 8);
		bLabel.offset(10, 58);
		cLabel.offset(10, 108);
		addChild(aLabel);
		addChild(bLabel);
		addChild(cLabel);
		
		// インデックス値を右肩にはりつける
		PText ptidx = new PText("a");
		ptidx.setScale(0.6);
		ptidx.setOffset(18,5);
		ptidx.setTextPaint(Color.blue);
		addChild(ptidx);
		ptidx.addAttribute("moveTarget", this);
		ptidx.addAttribute("tooltip", this);

		PText ptidx2 = new PText("b");
		ptidx2.setScale(0.6);
		ptidx2.setOffset(77,15);
		ptidx2.setTextPaint(Color.blue);
		addChild(ptidx2);
		ptidx2.addAttribute("moveTarget", this);
		ptidx2.addAttribute("tooltip", this);
	}
	public Covis_Object Covis_clone(boolean isAuto){
		return new Covis_Random(buffer, isAuto);
	}
	public Color getClassColor(){
		return defaultColor;
	}
	
	public static int objCount = 0;
	public static int objAryCount = 0;
	public String getNextVarName(boolean isAry){
		StringBuffer sb = new StringBuffer(getClsName().toLowerCase().substring(0,4));
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
		System.out.println("clear count "+getClsName());
	}
	@Override
	public String getConstructorArgs() {
		return " a, b ";
	}
	
	//メソッド作成
	public void covis_setA(int fa){
		a.setValue(String.valueOf(fa));
	}
	
	public void covis_setB(String fb){
		b.setValue(fb.trim().substring(0,1));
	}
	
	public void covis_setC(String fc){
		//String型の作成
		Covis_String newc = new Covis_String(buffer,true);
		newc.setValues(fc);
		//作成したString型にアンカーをつける
		buffer.putHistoryNew("method", newc, false);
		buffer.objField.addObject(newc);
		Covis_String oldc = (Covis_String)c.anchor.link.dest;
		if(oldc != null) {
			//既にアンカーが接続されているならそれを外す
			oldc.detach(c.anchor);
		}
		newc.attach(c.anchor);
		newc.setOffsetAlignment(this, 120, 150);
	}
	
	public String toString(){
		if(c.anchor.link.dest != null) {
			return "int a = " + a.getValue() + "\n char b = " + b.getValue()+ "\n String c = " + c.anchor.link.dest.toString();
		} else {
			return "int a = " + a.getValue() + "\n char b = " + b.getValue();
		}
		
	}
}

//コンストラクタ用ダイアログのクラス
class RandomConstructorDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 1852035735398130391L;

	JFrame parent;

	JTextField jtfa, jtfb;

	JButton ok;
	boolean canceled = true;

	public RandomConstructorDialog(JFrame p, Covis_Random random, String title, String mes1) {
		super(p, title, true);
		parent = p;
		jtfa = new JTextField(random.a.getValue());
		jtfa.setFont(SrcWindow.sans30);
		jtfa.setBackground(Covis_int.defaultColor);
		jtfa.addKeyListener(this);
		jtfb = new JTextField(random.b.getValue());
		jtfb.setFont(SrcWindow.sans30);
		jtfb.setBackground(Covis_char.defaultColor);
		jtfb.addKeyListener(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabelW(mes1), BorderLayout.NORTH);
		
		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(2,3));
		inner.add(new JLabelW("a"));
		inner.add(new JLabelW(random.a.getClsName()));
		inner.add(jtfa);
		inner.add(new JLabelW("b"));
		inner.add(new JLabelW(random.b.getClsName()));
		inner.add(jtfb);
		getContentPane().add(inner, BorderLayout.CENTER);
		
		ok = new JButton("ok");
		getContentPane().add(ok, BorderLayout.SOUTH);
//		pack();
		setSize(250,180);
		setLocation(p.getLocation().x + (p.getWidth() - this.getWidth())/2, p.getLocation().y +(p.getHeight()-this.getHeight())/2);

		jtfa.setCaretPosition(jtfa.getText().length());
		jtfb.setCaretPosition(jtfb.getText().length());

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
					jtfb = null;
					dispose();
				}
			}
		});
		
		jtfb.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					jtfa = null;
					jtfb = null;
					dispose();
				}
			}
		});
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
			ok.doClick();
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {	}
	@Override
	public void keyTyped(KeyEvent arg0) {	}
	
	public boolean isCanceled(){
		return canceled;
	}

	public static RandomConstructorDialog showDialog(JFrame parent, Covis_Random random, String title, String mes1) {
		RandomConstructorDialog d = new RandomConstructorDialog(parent,	random, title, mes1);
		d.setVisible(true);
		if (d.jtfa != null && d.jtfb != null){
			random.a.setValue(String.valueOf(Integer.parseInt(d.jtfa.getText())));
			random.b.setValue((d.jtfb.getText().trim().substring(0,1)));
		}
		return d;
	}
}
