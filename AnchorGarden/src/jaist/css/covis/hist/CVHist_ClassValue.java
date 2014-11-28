package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Anchor;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.Covis_primitive;

public class CVHist_ClassValue extends CVHistory {
	Covis_primitive v;
	Covis_Object obj;
	String varname;
	
	public CVHist_ClassValue(String _varname, Covis_primitive _v,Covis_Object _obj, CoVisBuffer buf){
		super(buf);
		this.v = _v;
		this.obj = _obj;
		this.varname = _varname;
		
		//óöóópï∂éöóÒê∂ê¨
		StringBuffer t = new StringBuffer();
		for(Anchor a: obj.anchors_incoming){
			t.append(a.getVarName());
		}
		t.append(".");
		t.append(varname);
		t.append(" = ");
		t.append(v.value);
		t.append(";");
		
		setCode(t.toString(), true);
	}
}