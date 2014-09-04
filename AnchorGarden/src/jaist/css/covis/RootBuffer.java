package jaist.css.covis;

import java.util.ArrayList;
import java.util.HashSet;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PRoot;

/**
 * ATNBuffer(�o�b�t�@)�̃X�[�p�[�N���X�D��ʕ\�����f���̃��[�g(root), ����ѕ\�����C��(layer)�����D
 * 
 * 
 */
public class RootBuffer {
	public static ArrayList<CoVisBuffer> buffers = new ArrayList<CoVisBuffer>();
	
	/**
	 * ����Buffer��\�����̃E�B���h�E���X�g
	 */
	public HashSet<CoVisWindow> showingWindows;
	public PRoot root;
	public PLayer layer;
	public String name;
	public static String bufferName = "World";
	static int bufferid;
	boolean hasModifiedUnsaved;
	public RootBuffer(){
		root = new PRoot();
		layer = new PLayer();
		root.addChild(layer);
		
		name = bufferName+" "+String.valueOf(RootBuffer.bufferid);
		RootBuffer.bufferid++;
		buffers.add((CoVisBuffer)this);
//		PText test = new PText(name);
//		layer.addChild(test);
//		test.offset(-200, -200);
		RootBuffer.updateAllBufferMenu();
	}
	public void addCamera(PCamera cam){
		root.addChild(cam);
		cam.addLayer(layer);
	}
	public void delete(){
		buffers.remove(this);
		RootBuffer.updateAllBufferMenu();
	}
	public static void updateAllBufferMenu(){
		for(RootWindow pw: RootWindow.windows){
			pw.updateBufferMenu();
		}
	}
	public static CoVisBuffer find(String s){
		for(CoVisBuffer b: buffers){
			if (b.name.equals(s)) return b;
		}
		return null;
	}
	
	public void addShowingWindow(CoVisWindow w){
		if (showingWindows == null) showingWindows = new HashSet<CoVisWindow>();
		showingWindows.add(w);
	}
	public void removeShowingWindow(CoVisWindow w){
		if (showingWindows == null) showingWindows = new HashSet<CoVisWindow>();
		showingWindows.remove(w);
//		if(showingWindows.size()==0){ // �����A�Q�Ƃ���E�B���h�E��0�ɂȂ�����
//			((ATNBuffer)this).updateContAction.setSelected(false); //DB�A���X�V��؂�
//		}
//              ������updateContAction�̃v���p�e�B��ύX����̂ł͂Ȃ��A���ۂ̍X�V�ӏ��ŃE�B���h�E�����`�F�b�N����B
		// �@�@�@�ق����֗��i�p�ɂɌJ��Ԃ��؂�ւ��邱�Ƃ����邩������Ȃ��̂ŁjDBUpdateContinuousThread.run�Ń`�F�b�N�B
	}
	public void repaint(){
		for(CoVisWindow w:showingWindows){
			w.canvas.repaint();
		}
	}
	public CoVisWindow getWindow(){
		if (showingWindows == null) return null;
		for(CoVisWindow w:showingWindows){
			return w; // �ǂ�ł���������Ԃ�
		}
		return null;
	}
}
