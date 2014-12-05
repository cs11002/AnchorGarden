package jp.ac.jaist.css.common.io;

import java.awt.Graphics;

import javax.swing.JFrame;

/**
 * シリアライズオブジェクトマネージャで管理してもらうアプリケーションのためのインタフェース
 * （このインタフェースを実装していないと，シリアライズオブジェクトマネージャで管理してもらえない）
 * @author miuramo
 *
 */
public interface SerializedObjectSupport {
	public JFrame getFrame(); // フレームウィンドウへの参照を返す（WebReposBrowserを隣に表示するため）
	public byte[] byteSerializeExport(); // システム内部のオブジェクトをバイト列に書き出す
	public void byteSerializeImport(byte[] ba); // バイト列をシステム内部に組み入れる
	
	public int getWidth(); // このコンポーネントのサイズを返す．サムネイル生成用．通常のGUI部品には実装されているため追加する必要はない．
	public int getHeight(); // このコンポーネントのサイズを返す．サムネイル生成用．通常のGUI部品には実装されているため追加する必要はない．
	public void paintComponent(Graphics g);//サムネイル生成用．通常のGUI部品には実装されているため追加する必要はない．
}
