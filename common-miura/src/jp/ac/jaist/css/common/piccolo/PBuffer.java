package jp.ac.jaist.css.common.piccolo;

import java.util.ArrayList;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PRoot;
import edu.umd.cs.piccolo.nodes.PText;

public class PBuffer {
	public static ArrayList<PBuffer> buffers = new ArrayList<PBuffer>();
	public PRoot root;
	public PLayer layer;
	public String name;
	static int id;
	boolean hasModifiedUnsaved;
	public PBuffer(){
		root = new PRoot();
		layer = new PLayer();
		root.addChild(layer);
		
		name = "Buffer "+String.valueOf(buffers.size())+" ("+String.valueOf(PBuffer.id)+")";
		PBuffer.id++;
		buffers.add(this);
		PText test = new PText(name);
		layer.addChild(test);
		PBuffer.updateAllBufferMenu();
	}
	public void addCamera(PCamera cam){
		root.addChild(cam);
		cam.addLayer(layer);
	}
	public void delete(){
		buffers.remove(this);
		PBuffer.updateAllBufferMenu();
	}
	public static void updateAllBufferMenu(){
		for(PWindow pw: PWindow.windows){
			pw.updateBufferMenu();
		}
	}
	public static PBuffer find(String s){
		for(PBuffer b: buffers){
			if (b.name.equals(s)) return b;
		}
		return null;
	}
}
