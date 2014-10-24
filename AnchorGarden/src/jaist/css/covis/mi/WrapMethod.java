package jaist.css.covis.mi;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Anchor;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.MessageManager;
import jaist.css.covis.cls.Variable;
import jaist.css.covis.cls.VariableM;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collection;
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
	WrapMethod wm;

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
		wm = this;
		//アニメーション用スレッド生成
		Thread thread = new Thread() {
			@Override
			public void run() {
				if(variable.isArray) {
					//VariableM varM;
					PNode[] objChild = new PNode[0];
					Collection<PNode> objcol = obj.getAllNodes();
					objChild = objcol.toArray(objChild);
					for(int i=0; i<objChild.length;i++) {
						if(objChild[i] instanceof VariableM && ((Variable)objChild[i]).anchor.destObject != null) {
							variable = (Variable)objChild[i];
							obj = variable.anchor.destObject;
							animation();
						}
					}
				}else{
					animation();
				}
			}
		};
		thread.start();
	}
	
	public void animation() {
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
						Anchor a = set.getValue();
						//System.out.println("a "+a.getVarClass());
						//System.out.println("c "+c.getName());
						if (c.isAssignableFrom(a.getVarClass())){
							temp.put(Variable.getShortestName(a.srcVariable.getVarNamesAry()), a.destObject);
							//System.out.println(s);
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
		StringBuffer sb = new StringBuffer();//変数名なし用
		StringBuffer sb2 = new StringBuffer();//変数名あり用
		sb.append(wm.method.getName().replace("covis_", "")+"(");
		sb2.append(Variable.getShortestName(variable.getVarNamesAry())+".");
		//sb.append(Variable.getShortestName(variable.getVarNamesAry())+"."+wm.method.getName().replace("covis_", "")+"(");
		for(int i=0;i<paramClses.length;i++){
			if(paramClses[i].getName().equals("java.lang.String")) {
				sb.append("\"" + args[i].toString()+"\",");
			}else if(paramClses[i].getName().equals("int")){
				sb.append(args[i].toString()+",");
			}else{
				sb.append(args[i].toString()+",");
				args[i] = candidates.get(paramClses[i]).get(args[i]);//変数をその中身に変更
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(");");
		sb2.append(sb);
		//アニメーション再生
		MessageManager.sendMessage(buffer,sb.toString(),variable,obj,false);
		//メソッド実行
		retobj = mid.invokeMethod(args, sb2.toString());

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

}

