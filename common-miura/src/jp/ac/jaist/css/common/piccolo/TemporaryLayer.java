package jp.ac.jaist.css.common.piccolo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.zip.GZIPInputStream;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;

public class TemporaryLayer extends PPath {
	private static final long serialVersionUID = -5832985834501818414L;

	static BasicStroke backStroke = new BasicStroke(3, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND);

	static BasicStroke roundStroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND);

	PPath fade;

	PPath cancelbuttonppath; // 一時レイヤを破棄するボタン

	PPath unwrapbuttonppath; // 定着化ボタン

	final PText tooltipNode;

	PCamera pc;

	public TemporaryLayer(byte[] ba, PCamera pc) {
		this.pc = pc;
		ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		InputStream gzipis = null;
		ObjectInputStream ois = null;
		PNode pn = null;
		try {
			gzipis = new GZIPInputStream(bais);
			ois = new ObjectInputStream(gzipis);
			int num = ois.readInt();
			System.out.println(num + " items found.");
			for (int i = 0; i < num; i++) {
				pn = (PNode) ois.readObject();
				addChild(pn);
				// HilitOverlap.apply(pn);
			}
			ois.close();
			gzipis.close();
			bais.close();
		} catch (IOException excep) {
			excep.printStackTrace(System.err);
		} catch (ClassNotFoundException excnf) {
			System.err.println("ClassNotFound Error");
		} finally {
		}

		// setChildrenPickable(false);

		// 子どもの領域を全部足し合わせた領域を計算
		PBounds pb = new PBounds();
		pb = this.getUnionOfChildrenBounds(pb);
		setPathTo(pb);
		setPaint(Color.WHITE); // 実際にはbackppathが手前にくるため表示されないが，つまんで動かせるようにするには背景を塗らなければならないので

		// setStrokePaint(Color.blue);

		fade = new PPath();

		addChild(fade);

		PPath backppath = new PPath();// PPath.createRectangle(0,0,(float)(messageptext.getWidth()+10),(float)(titleptext.getHeight()+messageptext.getHeight()+30));
		backppath.setPaint(new Color(220, 240, 200));
		backppath.setStrokePaint(new Color(110, 125, 120));
		backppath.setTransparency(0.3f); // 背景となる四角の，初期不透明度(0.0fが完全に透けた状態)
		backppath.setStroke(backStroke);
		fade.addChild(backppath);
		backppath.setPathTo(pb);
		backppath.setPickable(false);
		// fade.setPickable(false);
		resetTransform(this);

		float size = 20.0f; // キャンセルボタンのサイズ
		cancelbuttonppath = PPath.createRectangle(0, 0, size, size); // キャンセルボタンの外側はただの四角
		cancelbuttonppath.moveTo(2, 2); // 内側の×を書いていく
		cancelbuttonppath.lineTo(size - 2, size - 2);
		cancelbuttonppath.moveTo(2, size - 2);
		cancelbuttonppath.lineTo(size - 2, 2);

		cancelbuttonppath.setTransparency(1.0f);
		cancelbuttonppath.addAttribute("tooltip", "cancel import");
		cancelbuttonppath.setPaint(Color.white);
		Dimension d = new Dimension();
		d.setSize(getBounds().x + getWidth() - size - 10, getBounds().y + 9);
		setMessagePointRelative(cancelbuttonppath, d);
		fade.addChild(cancelbuttonppath);

		cancelbuttonppath.addInputEventListener(new PInputEventListener() {
			public void processEvent(PInputEvent e, int type) {
				if (type == MouseEvent.MOUSE_CLICKED) {
					System.out.println("click");
					dispose();
				}
				if (type == MouseEvent.MOUSE_ENTERED) {
					cancelbuttonppath.setStrokePaint(Color.blue);
				}
				if (type == MouseEvent.MOUSE_EXITED) {
					cancelbuttonppath.setStrokePaint(Color.black);
					hideToolTip();
				}
				if (type == MouseEvent.MOUSE_MOVED
						|| type == MouseEvent.MOUSE_DRAGGED) {
					TemporaryLayer.this.updateToolTip(e);
				}
			}
		});
		cancelbuttonppath.addAttribute("movable", false); // ローカル・ルール：LeftButtonListenerのみで用いる．
		// 理由：pickable=falseにすると，イベントをまったく受け付けられなくなるので．

		// unwrapbuttonppath:一時レイヤを外し，内容物を親キャンパスに定着化するときのボタン
		unwrapbuttonppath = PPath.createRectangle(0, 0, size, size);
		unwrapbuttonppath.moveTo(3, 3); // 内側の▽を書いていく
		// つなげて書かない理由は，内部がマウスに反応しなくなるから
		unwrapbuttonppath.lineTo(size / 2, size - 3);
		unwrapbuttonppath.moveTo(3, 3);
		unwrapbuttonppath.lineTo(size - 3, 3);
		unwrapbuttonppath.moveTo(size / 2, size - 3);
		unwrapbuttonppath.lineTo(size - 3, 3);
		unwrapbuttonppath.setStroke(roundStroke);
		unwrapbuttonppath.addAttribute("tooltip", "fix imported object");
		unwrapbuttonppath.setPaint(Color.white);
		unwrapbuttonppath.setTransparency(1.0f);
		d = new Dimension();
		d.setSize(getBounds().x + getWidth() - (size + 10) * 2,
				getBounds().y + 9);
		setMessagePointRelative(unwrapbuttonppath, d);
		fade.addChild(unwrapbuttonppath);

		unwrapbuttonppath.addInputEventListener(new PInputEventListener() {
			public void processEvent(PInputEvent e, int type) {
				if (type == MouseEvent.MOUSE_CLICKED) {
					System.out.println("click");
					unwrap();
				}
				if (type == MouseEvent.MOUSE_ENTERED) {
					unwrapbuttonppath.setStrokePaint(Color.blue);
				}
				if (type == MouseEvent.MOUSE_EXITED) {
					unwrapbuttonppath.setStrokePaint(Color.black);
					hideToolTip();
				}
				if (type == MouseEvent.MOUSE_MOVED
						|| type == MouseEvent.MOUSE_DRAGGED) {
					TemporaryLayer.this.updateToolTip(e);
				}
			}

		});
		tooltipNode = new PText();
		tooltipNode.setPaint(Color.yellow);
		tooltipNode.setTextPaint(Color.black);

		pc.addChild(tooltipNode);
		unwrapbuttonppath.addAttribute("movable", false); // ローカル・ルール：LeftButtonListenerのみで用いる．
		
//		invalidateFullBounds();
	}

	public void updateToolTip(PInputEvent event) {
		PNode n = event.getInputManager().getMouseOver().getPickedNode();
		String tooltipString = (String) n.getAttribute("tooltip");
		Point2D p = event.getCanvasPosition();

		event.getPath().canvasToLocal(p, pc);

		tooltipNode.setText(tooltipString);
		tooltipNode.setOffset(p.getX() + 20, p.getY() - 20);
	}

	public void hideToolTip() {
		tooltipNode.setText("");
	}

	public void unwrap() {
		// get parent layer
		PNode layer = getParent();

		ListIterator<?> li = this.getChildrenIterator();
		ArrayList<PNode> li2 = new ArrayList<PNode>();
		while (li.hasNext()) {
			PNode p = (PNode) li.next();
			li2.add(p);
		}

		for (PNode p : li2) {
			removeChild(p);
			// resetTransform(p);
			layer.addChild(p);
			p.offset(this.getXOffset(), this.getYOffset());
			// resetTransform(p); // これをやるとテキストサイズが保存されない

			// PBounds pb = p.getBounds();
			// p.localToGlobal(pb);
			// layer.globalToLocal(pb);
			// p.setBounds(pb);
		}
		removeFromParent();
		cancelbuttonppath.removeFromParent();
		fade.removeFromParent();
	}

	public void dispose() {
		removeFromParent();
	}

	public void setMessagePointRelative(PNode child, Dimension d) {
		if (child == null)
			return;
		child.offset(d.getWidth(), d.getHeight());
	}

	public void resetTransform(PNode p) {
		PBounds b = new PBounds();
		p.getTransform().transform(p.getBounds(), b);
		p.setBounds(b);
		p.setTransform(new PAffineTransform());
	}
}
