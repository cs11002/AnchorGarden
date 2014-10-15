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
		WrapMethod wm = this;//�X���b�h�Ŏg�����߂ɕϐ��ɕۑ�
		//�A�j���[�V�����p�X���b�h����
		Thread thread = new Thread() {
			@Override
			public void run() {
				MessageManager.sendMessage(buffer,methodname,variable,obj,false);

				paramClses = method.getParameterTypes();
				//�����Ȃ���΁C���̂܂܎��s
				if (paramClses.length==0){
					Object[] arg = null;
					MethodInvocationDialog mid = new MethodInvocationDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);
					//���\�b�h�R�[���𕶎����
					StringBuffer sb = new StringBuffer();
					sb.append(Variable.getShortestName(variable.getVarNamesAry())+"."+method.getName().replace("covis_", "")+"();");
					retobj = mid.invokeMethod(arg, sb.toString());
					//�Ԃ�l������Ȃ�A��̃A�j���[�V�������Đ�
					if(!method.getReturnType().getName().equals("void")) {
						//�Ԃ�l�𕶎����
						if(retobj instanceof Covis_Object) {
							//�I�u�W�F�N�g�^�Ȃ�N���X����
							methodname = ((Covis_Object) retobj).getClsName();
						}else{
							//�v���~�e�B�u�^�Ȃ炻�̒l��
							methodname = retobj.toString();
						}
						//�A�j���[�V�����p�X���b�h����
						Thread thread = new Thread() {
							@Override
							public void run() {
								MessageManager.sendMessage(buffer,methodname,obj,variable,true);
							}
						};
						thread.start();
					}
					return;
				}
				candidates = new HashMap<Class<?>, HashMap<String,Object>>();
				for(Class<?> c: paramClses){
					if (candidates.containsKey(c)) continue;

					//System.out.println(c.toString()+"��T��");
					//�i�[�e��
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
				//�I�u�W�F�N�g�̌��i�Q�Ƃ̂��߂̕ϐ��j�͂��܂���
				Object retobj = MethodInvocationDialog.showDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);
				
				//�Ԃ�l������Ȃ�A��̃A�j���[�V�������Đ�
				if(!method.getReturnType().getName().equals("void")) {
					//�Ԃ�l�𕶎����
					if(retobj instanceof Covis_Object) {
						//�I�u�W�F�N�g�^�Ȃ�N���X����
						methodname = ((Covis_Object) retobj).getClsName();
					}else{
						//�v���~�e�B�u�^�Ȃ炻�̒l��
						methodname = retobj.toString();
					}
					//�A�j���[�V�����p�X���b�h����
					Thread thread = new Thread() {
						@Override
						public void run() {
							MessageManager.sendMessage(buffer,methodname,obj,variable,true);
						}
					};
					thread.start();
				}
			}
		};
		thread.start();
	}	
}

