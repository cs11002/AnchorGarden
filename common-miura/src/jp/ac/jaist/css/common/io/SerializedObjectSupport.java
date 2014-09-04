package jp.ac.jaist.css.common.io;

import java.awt.Graphics;

import javax.swing.JFrame;

/**
 * �V���A���C�Y�I�u�W�F�N�g�}�l�[�W���ŊǗ����Ă��炤�A�v���P�[�V�����̂��߂̃C���^�t�F�[�X
 * �i���̃C���^�t�F�[�X���������Ă��Ȃ��ƁC�V���A���C�Y�I�u�W�F�N�g�}�l�[�W���ŊǗ����Ă��炦�Ȃ��j
 * @author miuramo
 *
 */
public interface SerializedObjectSupport {
	public JFrame getFrame(); // �t���[���E�B���h�E�ւ̎Q�Ƃ�Ԃ��iWebReposBrowser��ׂɕ\�����邽�߁j
	public byte[] byteSerializeExport(); // �V�X�e�������̃I�u�W�F�N�g���o�C�g��ɏ����o��
	public void byteSerializeImport(byte[] ba); // �o�C�g����V�X�e�������ɑg�ݓ����
	
	public int getWidth(); // ���̃R���|�[�l���g�̃T�C�Y��Ԃ��D�T���l�C�������p�D�ʏ��GUI���i�ɂ͎�������Ă��邽�ߒǉ�����K�v�͂Ȃ��D
	public int getHeight(); // ���̃R���|�[�l���g�̃T�C�Y��Ԃ��D�T���l�C�������p�D�ʏ��GUI���i�ɂ͎�������Ă��邽�ߒǉ�����K�v�͂Ȃ��D
	public void paintComponent(Graphics g);//�T���l�C�������p�D�ʏ��GUI���i�ɂ͎�������Ă��邽�ߒǉ�����K�v�͂Ȃ��D
}
