package jp.ac.jaist.css.common.piccolo;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import jp.ac.jaist.css.common.io.SerObjManager;
import edu.umd.cs.piccolo.PLayer;

public class ImageDropTargetListener implements DropTargetListener{
	PLayer layer;
	SerObjManager som;
	public ImageDropTargetListener(PLayer l, SerObjManager som){
		layer = l;
		this.som = som;
	}
	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(dtde.getDropAction());
		try {
			Transferable trans = dtde.getTransferable();
			DataFlavor flavors[] = trans.getTransferDataFlavors();
			if (flavors[0].isFlavorJavaFileListType()) {
				List<?> list = (List<?>) trans.getTransferData(flavors[0]);
				for (int j = 0; j < list.size(); ++j) {
					File file = (File) list.get(j);
					System.out.println("(FILE) " + file.getName() + "  " + file.length()+"bytes");
					String f = file.getName().toLowerCase();
					if (f.endsWith(".png") || f.endsWith(".jpg") || f.endsWith(".gif") || f.endsWith(".bmp")){
						MyPImage pimg = new MyPImage(file.getAbsolutePath());
						layer.addChild(pimg);
						pimg.translate(dtde.getLocation().getX(), dtde.getLocation().getY());
						double rate = 1.0d;
						if (pimg.getBounds().width > 200){
							rate = 200/pimg.getBounds().width;
						}
						pimg.setBounds(0, 0, pimg.getBounds().width*rate, pimg.getBounds().height*rate);
						pimg.setScale(0.5);
					} else if (f.endsWith(".ser")){
						som.load(file.getAbsolutePath());
					}
				}
				// end of (flavors[0].isFlavorJavaFileListType())
			} else {
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
		} finally {
			dtde.dropComplete(false);
		}
	}
	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(DnDConstants.ACTION_MOVE);
	}
	public void dragOver(DropTargetDragEvent dtde) {}
	public void dropActionChanged(DropTargetDragEvent dtde) {}
	public void dragExit(DropTargetEvent dte) {}
	
}