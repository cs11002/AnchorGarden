package jp.ac.jaist.css.common.piccolo;

/**
 * MyBoundsHandle addStickyBoundsHandlesToの解除のときに，このインタフェースを実装していれば呼ばれる
 * @author miuramo
 *
 */
public interface HandleEditable {
	public void startHandle();
	public void endHandle();
}
