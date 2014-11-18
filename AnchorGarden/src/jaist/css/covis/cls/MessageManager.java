package jaist.css.covis.cls;


import jaist.css.covis.CoVisBuffer;

import java.lang.reflect.Method;
import java.awt.Color;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;

/**
 * @author macchan EPMessageManager
 */
public class MessageManager {

	static CoVisBuffer buffer;
	
	public static Object sendMessage(CoVisBuffer buf,String method, PNode sender, PNode receiver, boolean isReturn) {

		buffer = buf;
		
		//アニメーション再生
		animation(method, sender, receiver, isReturn);

		return null;
	}

	private static void animation(String message, PNode sender,
			PNode receiver, boolean isReturn) {
		
		try {
			//動かす吹き出しを作成
			MessageBoard board = new MessageBoard(message);

			// 返り値だったらメッセージボードの色を変える
			if(isReturn){
				board.setPaint(new Color(255,204,204));
				board.triangle.setPaint(new Color(255,204,204));
			}
			
			//吹き出しをレイヤーに追加
			buffer.layer.addChild(board);
			
			//送信側と受信側の位置を取得
			Point2D src = sender.getGlobalBounds().getCenter2D();
			Point2D dest = receiver.getGlobalBounds().getCenter2D();

			double frameNumber = 20;
			double dx = (dest.getX() - src.getX()) / frameNumber;
			double dy = (dest.getY() - src.getY()) / frameNumber;

			//吹き出しの初期位置設定
			board.setOffset(src.getX(), src.getY()-50);
			System.out.println("src  x = " + src.getX() + " y = " + src.getY());
			System.out.println("dest x = " + dest.getX() + " y = " + dest.getY());
			
			//アニメーション部分
			for (int i = 0; i < frameNumber; i++) {
				board.translate(dx, dy);
				board.repaint();
				Thread.sleep(100);
			}

			//追加した吹き出しを削除
			buffer.layer.removeChild(board);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
