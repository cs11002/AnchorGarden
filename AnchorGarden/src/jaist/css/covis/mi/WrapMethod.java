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
		//�A�j���[�V�����p�X���b�h����
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
		//�����Ȃ���΁C���̂܂܎��s
		if (paramClses.length==0){
			MethodInvocationDialog mid = new MethodInvocationDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);
			//���\�b�h�R�[���𕶎����
			StringBuffer sb = new StringBuffer();
			sb.append(Variable.getShortestName(variable.getVarNamesAry())+"."+method.getName().replace("covis_", "")+"();");
			//�A�j���[�V�����Đ�
			MessageManager.sendMessage(buffer,methodname,variable,obj,false);
			//���\�b�h���s
			retobj = mid.invokeMethod(args, sb.toString());

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
				//�A�j���[�V�����Đ�
				MessageManager.sendMessage(buffer,methodname,obj,variable,true);
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
		//�I�u�W�F�N�g�̌��i�Q�Ƃ̂��߂̕ϐ��j�͂��܂���

		//�_�C�A���O�ň������擾
		args = MethodInvocationDialog.showDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);

		MethodInvocationDialog mid = new MethodInvocationDialog(obj.buffer.getWindow().frame, "invoke method", wm, "select arguments", variable);
		//���\�b�h�R�[���𕶎����
		StringBuffer sb = new StringBuffer();//�ϐ����Ȃ��p
		StringBuffer sb2 = new StringBuffer();//�ϐ�������p
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
				args[i] = candidates.get(paramClses[i]).get(args[i]);//�ϐ������̒��g�ɕύX
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(");");
		sb2.append(sb);
		//�A�j���[�V�����Đ�
		MessageManager.sendMessage(buffer,sb.toString(),variable,obj,false);
		//���\�b�h���s
		retobj = mid.invokeMethod(args, sb2.toString());

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
			//�A�j���[�V�����Đ�
			MessageManager.sendMessage(buffer,methodname,obj,variable,true);
		}
	}

}

