package jaist.css.covis.mi;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Anchor;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.MessageManager;
import jaist.css.covis.cls.Variable;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.AbstractAction;

import edu.umd.cs.piccolo.PNode;

public class WrapMethod extends AbstractAction {
	private static final long serialVersionUID = -3933799327221553449L;
	Method method;
	Covis_Object obj;
	Variable variable;
	CoVisBuffer buffer;
	String methodname;
	Object retobj;

	public WrapMethod(Method m, Covis_Object o, String mname, Variable var, CoVisBuffer buf){
		super(mname);
		obj = o;
		method = m;
		variable = var;
		buffer = buf;
		methodname = mname;
	}
	Class<?>[] paramClses;
	HashMap<Class<?>,HashMap<String,Object>> candidates;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//System.out.println(method.toString());
		WrapMethod wm = this;
		//アニメーション用スレッド生成
		Thread thread = new Thread() {
			@Override
			public void run() {
				Object[] args = null;
				paramClses = method.getParameterTypes();
				//引数なければ，そのまま実行
				if (paramClses.length==0){
					MethodInvocationDialog mid = new MethodInvocationDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);
					//メソッドコールを文字列に
					StringBuffer sb = new StringBuffer();
					sb.append(Variable.getShortestName(variable.getVarNamesAry())+"."+method.getName().replace("covis_", "")+"();");
					//アニメーション再生
					MessageManager.sendMessage(buffer,methodname,variable,obj,false);
					//メソッド実行
					retobj = mid.invokeMethod(args, sb.toString());

					//返り値があるなら帰りのアニメーションを再生
					if(!method.getReturnType().getName().equals("void")) {
						//返り値を文字列に
						if(retobj instanceof Covis_Object) {
							//オブジェクト型ならクラス名を
							methodname = ((Covis_Object) retobj).getClsName();
						}else{
							//プリミティブ型ならその値を
							methodname = retobj.toString();
						}
						//アニメーション再生
						MessageManager.sendMessage(buffer,methodname,obj,variable,true);
					}
					return;
				}

				candidates = new HashMap<Class<?>, HashMap<String,Object>>();
				for(Class<?> c: paramClses){
					if (candidates.containsKey(c)) continue;

					//System.out.println(c.toString()+"を探索");
					//格納容器
					HashMap<String, Object> temp = new HashMap<String, Object>();
					for(PNode pn: obj.buffer.objField.getAllNodes()){
						if (c.isInstance(pn)){
							Covis_Object o = (Covis_Object)pn;
							TreeMap<String,Anchor> map = o.referenceAnchors();
							for(Entry<String,Anchor> set: map.entrySet()){
								String s = set.getKey();
								Anchor a = set.getValue();
								System.out.println("a "+a.getVarClass());
								System.out.println("c "+c.getName());
								if (c.isAssignableFrom(a.getVarClass())){
									temp.put(Variable.getShortestName(a.srcVariable.getVarNamesAry()), a.destObject);
									System.out.println(s);
								}
							}
						}
					}
					candidates.put(c,temp);
				}
				//オブジェクトの候補（参照のための変数）はあつまった
				
				//ダイアログで引数を取得
				args = MethodInvocationDialog.showDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);
				
				MethodInvocationDialog mid = new MethodInvocationDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);
				//メソッドコールを文字列に
				StringBuffer sb = new StringBuffer();
				sb.append(Variable.getShortestName(variable.getVarNamesAry())+"."+wm.method.getName().replace("covis_", "")+"(");
				for(int i=0;i<wm.paramClses.length;i++){
					sb.append(args[i].toString()+",");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append(");");
				//アニメーション再生
				MessageManager.sendMessage(buffer,methodname,variable,obj,false);
				//メソッド実行
				retobj = mid.invokeMethod(args, sb.toString());

				//返り値があるなら帰りのアニメーションを再生
				if(!method.getReturnType().getName().equals("void")) {
					//返り値を文字列に
					if(retobj instanceof Covis_Object) {
						//オブジェクト型ならクラス名を
						methodname = ((Covis_Object) retobj).getClsName();
					}else{
						//プリミティブ型ならその値を
						methodname = retobj.toString();
					}
					//アニメーション再生
					MessageManager.sendMessage(buffer,methodname,obj,variable,true);
				}
			}
		};
		thread.start();
	}	

}

