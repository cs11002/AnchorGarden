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
		
		//�A�j���[�V�����Đ�
		animation(method, sender, receiver, isReturn);

		return null;
	}

	private static void animation(String message, PNode sender,
			PNode receiver, boolean isReturn) {
		
		try {
			//�����������o�����쐬
			MessageBoard board = new MessageBoard(message);

			// �Ԃ�l�������烁�b�Z�[�W�{�[�h�̐F��ς���
			if(isReturn){
				board.setPaint(new Color(255,204,204));
				board.triangle.setPaint(new Color(255,204,204));
			}
			
			//�����o�������C���[�ɒǉ�
			buffer.layer.addChild(board);
			
			//���M���Ǝ�M���̈ʒu���擾
			Point2D src = sender.getGlobalBounds().getCenter2D();
			Point2D dest = receiver.getGlobalBounds().getCenter2D();

			double frameNumber = 20;
			double dx = (dest.getX() - src.getX()) / frameNumber;
			double dy = (dest.getY() - src.getY()) / frameNumber;

			//�����o���̏����ʒu�ݒ�
			board.setOffset(src.getX(), src.getY()-50);
			System.out.println("src  x = " + src.getX() + " y = " + src.getY());
			System.out.println("dest x = " + dest.getX() + " y = " + dest.getY());
			
			//�A�j���[�V��������
			for (int i = 0; i < frameNumber; i++) {
				board.translate(dx, dy);
				board.repaint();
				Thread.sleep(100);
			}

			//�ǉ����������o�����폜
			buffer.layer.removeChild(board);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
