package jp.ac.jaist.css.common.piccolo;


import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;

import jp.ac.jaist.css.common.gui.menu.FramePopup;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;

public class PNodeEditor implements PInputEventListener, Serializable {
	private static final long serialVersionUID = -3648262142103303092L;
	public PNode target;
	public boolean isSelected = false;
	transient public PCanvas canvas;
	transient public JFrame frame;
	transient public FramePopup popup;
	public ArrayList<PNode> handles;

	public static Hashtable<PNode,PNodeEditor> pn2pne = new Hashtable<PNode, PNodeEditor>();
	public static void detachAllHandles(){
		ArrayList<PNodeEditor> pneAry = new ArrayList<PNodeEditor>(pn2pne.values());
		for(PNodeEditor pne: pneAry){
			pne.setSelected(false, null);
		}
	}
	public PNodeEditor(PNode _target) {
		target = _target;
		handles = new ArrayList<PNode>();
	}

	public void attachHandle(){
		handles.clear();
		MyBoundsHandle.addStickyBoundsHandlesTo(target, canvas, this);
		if (pn2pne.get(target)==null){
			pn2pne.put(target, this);
		}
	}
	public void detachHandle(){
		for(PNode pn: handles){
			pn.removeFromParent();
		}
		if (pn2pne.get(target)!=null){
			pn2pne.remove(target);
		}
	}
	/**
	 * Selected
	 * @return
	 */
	public boolean isSelected(){
		return isSelected;
	}
	public void setSelected(boolean f, ArrayList<PNodeEditor> list){
		isSelected = f;
		if (f){
			attachHandle();
		} else {
			detachHandle();
		}
		if (f && list != null && !list.contains(this)) list.add(this);
		if (!f && list != null && list.contains(this)) list.remove(this);
	}
	public void toggleSelected(ArrayList<PNodeEditor> list){
		setSelected(!isSelected, list);
	}


	public void removeFromParent(Hashtable<PNode, PNode> trash){ // this, parent の順番
		detachHandle();
		PNode parent = target.getParent();
		if (parent != null) {
			target.removeFromParent();
			if (trash != null) trash.put(target,parent);
		}
	}
	public void toFront(){
		target.moveToFront();
	}

	public void clicked(PInputEvent e, PCanvas _canvas) {
		canvas = _canvas;
		toggleSelected(null);
	}

	public void doubleClicked(PInputEvent e, PCanvas _canvas) {
		canvas = _canvas;
	}

	public void processEvent(PInputEvent arg0, int arg1) {
	}

	/**
	 * 右クリック時に呼ばれる．デフォルトではメニュー（もしあれば）
	 * @param e
	 * @param _canvas
	 * @param _frame
	 */
	public void rightClicked(PInputEvent e, PCanvas _canvas, JFrame _frame){
		canvas = _canvas;
		frame = _frame;
		if (popup != null){
			Point2D cp = e.getPositionRelativeTo(canvas.getCamera());
			popup.showWithFrame(canvas, (int) cp.getX(), (int) cp.getY(), frame);
		}
	}
}
