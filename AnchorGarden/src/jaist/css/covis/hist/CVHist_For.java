package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;

public class CVHist_For extends CVHistory {

	public CVHist_For(CoVisBuffer buf, String array, String method){
		super(buf);
		StringBuffer s = new StringBuffer();
		s.append("for (int i=0;i<" + array + ".length;i++) {\n  ");
		s.append(array + "[i]." + method + ";\n}");
		setCode(s.toString(), true);
	}
}
