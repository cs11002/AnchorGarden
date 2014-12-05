package jp.ac.jaist.css.common.piccolo;

import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimerTask;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JComponent;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;

public class PrintPrintable {
	PCanvas can;
	Document doc;
	PdfWriter wri;
	String pagestr;
	static Hashtable<String,Rectangle> paperSizeHash = new Hashtable<String,Rectangle>();
	static {
		paperSizeHash.put("A0L", PageSize.A0.rotate());
		paperSizeHash.put("A1L", PageSize.A1.rotate());
		paperSizeHash.put("A2L", PageSize.A2.rotate());
		paperSizeHash.put("A3L", PageSize.A3.rotate());
		paperSizeHash.put("A4L", PageSize.A4.rotate());
		paperSizeHash.put("A0P", PageSize.A0);
		paperSizeHash.put("A1P", PageSize.A1);
		paperSizeHash.put("A2P", PageSize.A2);
		paperSizeHash.put("A3P", PageSize.A3);
		paperSizeHash.put("A4P", PageSize.A4);
		paperSizeHash.put("A0", PageSize.A0);
		paperSizeHash.put("A1", PageSize.A1);
		paperSizeHash.put("A2", PageSize.A2);
		paperSizeHash.put("A3", PageSize.A3);
		paperSizeHash.put("A4", PageSize.A4);
	}
	/**
	 * 基本的には、static method でいけるので、オブジェクト不要。
	 * オブジェクトを作るのは、ページごとに表示を切り替えたい場合など、特殊な場合
	 */
	public PrintPrintable(){
		
	}
	public void prePrintByInstance(PCanvas canvas, String ps, String outfilename){
		can = canvas;
		pagestr = ps;
		
		doc = new Document(paperSizeHash.get(pagestr), 0, 0, 0, 0);
//		step 2: creation of the writer
		wri = null;
		try {
			wri = PdfWriter.getInstance(doc, new FileOutputStream(outfilename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// step 3: we open the document
		doc.open();

		// step 4: we grab the ContentByte and do some stuff with it

		// we create a fontMapper and read all the fonts in the font directory
		DefaultFontMapper mapper = new DefaultFontMapper();
		FontFactory.registerDirectories();
		mapper.insertDirectory("c:\\windows\\fonts");
	}
	public void printPageByInstance(){
		PdfContentByte cb = wri.getDirectContent();
		Graphics2D g2 = cb.createGraphicsShapes(paperSizeHash.get(pagestr).getWidth(), paperSizeHash.get(pagestr).getHeight());

		can.paintComponent(g2);

		g2.dispose();
		doc.newPage();
	}
	public void postPrintByInstance(){
		// step 5: we close the document
		doc.close();
	}
	
	
	
	public static void print(Printable printable){
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		// pras.add(Chromaticity.MONOCHROME);
		//pras.add(new Copies(3));
		pras.add(new JobName("piccoloprint",Locale.JAPAN));
		// pras.add(OrientationRequested.REVERSE_LANDSCAPE);//紙の右側にイメージの上がくる。
//		pras.add(new PrinterResolution(600,600,ResolutionSyntax.DPI));
		

		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor,pras);
		PrintService defaultPrintService =PrintServiceLookup.lookupDefaultPrintService();

		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.clear();
		aset.add(MediaSizeName.ISO_A4);
		MediaPrintableArea[] printableArea = (MediaPrintableArea[]) defaultPrintService.getSupportedAttributeValues(javax.print.attribute.standard.MediaPrintableArea.class, flavor, aset);
		for(int i=0;i<printableArea.length;i++){
			System.out.println(printableArea[i].getWidth(MediaPrintableArea.MM)+" "+printableArea[i].getHeight(MediaPrintableArea.MM));

		}
		pras.add(printableArea[0]);


		PrintService service = ServiceUI.printDialog(null,200,200,printService,defaultPrintService,flavor,pras);
		if (service == null) return;
		DocPrintJob job = service.createPrintJob();

		PrintJobListener pjlistener = new PrintJobAdapter() {
			public void printDataTransferCompleted(PrintJobEvent e) {
				System.out.println("Printed.");
//				System.exit(0);
			}
		};
		job.addPrintJobListener(pjlistener);


//		for(Object o : pras.keySet()){
//		System.out.println(o.getClass().getName());
//		System.out.println(o.toString());
//		}
		DocAttributeSet das = new HashDocAttributeSet();
		Doc doc = new SimpleDoc(printable, flavor, das);
		try {
			job.print(doc, pras);
		} catch (PrintException pe) {
			pe.printStackTrace();
		}
	}

	public static void printPDF(PCanvas canvas, String pdffilename) {

//		step 1: creation of a document-object
		Document document = new Document(PageSize.A0.rotate(), 0, 0, 0, 0);

		// step 2: creation of the writer
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(pdffilename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// step 3: we open the document
		document.open();

		// step 4: we grab the ContentByte and do some stuff with it

		// we create a fontMapper and read all the fonts in the font directory
		DefaultFontMapper mapper = new DefaultFontMapper();
		FontFactory.registerDirectories();
		mapper.insertDirectory("c:\\windows\\fonts");
		// we create a template and a Graphics2D object that corresponds with it
		PdfContentByte cb = writer.getDirectContent();
		Graphics2D g2 = cb.createGraphicsShapes(PageSize.A0.rotate().getWidth(), PageSize.A0.rotate().getHeight());

		canvas.paintComponent(g2);

		g2.dispose();

		// step 5: we close the document
		document.close();
	}

	public static void printPDF(JComponent canvas, String pagesizeString, String outfilename) {
		Hashtable<String,Rectangle> paperSizeHash = new Hashtable<String,Rectangle>();
		paperSizeHash.put("A0L", PageSize.A0.rotate());
		paperSizeHash.put("A1L", PageSize.A1.rotate());
		paperSizeHash.put("A2L", PageSize.A2.rotate());
		paperSizeHash.put("A3L", PageSize.A3.rotate());
		paperSizeHash.put("A4L", PageSize.A4.rotate());
		paperSizeHash.put("A0P", PageSize.A0);
		paperSizeHash.put("A1P", PageSize.A1);
		paperSizeHash.put("A2P", PageSize.A2);
		paperSizeHash.put("A3P", PageSize.A3);
		paperSizeHash.put("A4P", PageSize.A4);
		paperSizeHash.put("A0", PageSize.A0);
		paperSizeHash.put("A1", PageSize.A1);
		paperSizeHash.put("A2", PageSize.A2);
		paperSizeHash.put("A3", PageSize.A3);
		paperSizeHash.put("A4", PageSize.A4);

		Document document = new Document(paperSizeHash.get(pagesizeString), 0, 0, 0, 0);
//		step 2: creation of the writer
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(outfilename));
//			writer.setPdfVersion(PdfWriter.VERSION_1_7);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// step 3: we open the document
		document.open();

		// step 4: we grab the ContentByte and do some stuff with it

		// we create a fontMapper and read all the fonts in the font directory
		DefaultFontMapper mapper = new DefaultFontMapper();
		FontFactory.registerDirectories();
		mapper.insertDirectory("c:\\windows\\fonts");
		// we create a template and a Graphics2D object that corresponds with it
		PdfContentByte cb = writer.getDirectContent();
		
		Graphics2D g2 = cb.createGraphicsShapes(paperSizeHash.get(pagesizeString).getWidth(), paperSizeHash.get(pagesizeString).getHeight());

		canvas.paint(g2);

		g2.dispose();

		// step 5: we close the document
		document.close();
	}
	public static void printPDFinMultiPageByThread(PCanvas canvas, String string, String outfilename, ArrayList<PBounds> pnlist, boolean isOpenAfterMake){
		class PrintTimerTask extends TimerTask {
			PCanvas canvas;
			String string;
			String outfilename;
			ArrayList<PBounds> pnlist;
			boolean isOpenAfterMake;
			public PrintTimerTask(PCanvas c, String sheetsize, String out, ArrayList<PBounds> pn, boolean iso){
				canvas = c;
				string = sheetsize;
				outfilename = out;
				pnlist = pn;
				isOpenAfterMake = iso;
			}
			@Override
			public void run() {
				printPDFinMultiPageByPBounds(canvas, string, outfilename, pnlist);
				if (isOpenAfterMake) openPDF(outfilename);
			}
			void openPDF(String path){
				Runtime runtime = Runtime.getRuntime();
				String os = System.getProperty("os.name");

				try {
					if (os.startsWith("Windows"))
						runtime.exec("cmd.exe /c start " + path+"");
					else if (os.startsWith("Mac OS")){
						runtime.exec("open " + path + "");
					} else {
						runtime.exec("acroread "+path);
					}
				}
				catch(java.io.IOException ex){
					ex.printStackTrace();		
				}
			}
			
		}
		TimerTask tt = new PrintTimerTask(canvas, string, outfilename, pnlist, isOpenAfterMake);
		java.util.Timer daemon = new java.util.Timer();
		daemon.schedule(tt, 0);
	}
	
	public static void printPDFinMultiPage(PCanvas canvas, String string, String outfilename, ArrayList<PNode> pnlist) {
		Hashtable<String,Rectangle> paperSizeHash = new Hashtable<String,Rectangle>();
		paperSizeHash.put("A0L", PageSize.A0.rotate());
		paperSizeHash.put("A1L", PageSize.A1.rotate());
		paperSizeHash.put("A2L", PageSize.A2.rotate());
		paperSizeHash.put("A3L", PageSize.A3.rotate());
		paperSizeHash.put("A4L", PageSize.A4.rotate());
		paperSizeHash.put("A0P", PageSize.A0);
		paperSizeHash.put("A1P", PageSize.A1);
		paperSizeHash.put("A2P", PageSize.A2);
		paperSizeHash.put("A3P", PageSize.A3);
		paperSizeHash.put("A4P", PageSize.A4);
		paperSizeHash.put("A0", PageSize.A0);
		paperSizeHash.put("A1", PageSize.A1);
		paperSizeHash.put("A2", PageSize.A2);
		paperSizeHash.put("A3", PageSize.A3);
		paperSizeHash.put("A4", PageSize.A4);

		Document document = new Document(paperSizeHash.get(string), 0, 0, 0, 0);
//		step 2: creation of the writer
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(outfilename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// step 3: we open the document
		document.open();

		// step 4: we grab the ContentByte and do some stuff with it

		// we create a fontMapper and read all the fonts in the font directory
		DefaultFontMapper mapper = new DefaultFontMapper();
		FontFactory.registerDirectories();
		mapper.insertDirectory("c:\\windows\\fonts");

		// we create a template and a Graphics2D object that corresponds with it
		for(PNode pn: pnlist){
			canvas.getCamera().animateViewToCenterBounds(BUtil.zoomBounds(pn.getGlobalFullBounds(), 1.05), true, 0);
//			canvas.getCamera().setViewBounds(BUtil.zoomBounds(pn.getGlobalFullBounds(), 1.05));
			PdfContentByte cb = writer.getDirectContent();
			Graphics2D g2 = cb.createGraphicsShapes(paperSizeHash.get(string).getWidth(), paperSizeHash.get(string).getHeight());

			canvas.paintComponent(g2);

			g2.dispose();
			document.newPage();
		}

		// step 5: we close the document
		document.close();
	}
	
	public static void printPDFinMultiPageByPBounds(PCanvas canvas, String pagestr, String outfilename, ArrayList<PBounds> pnlist) {
		Hashtable<String,Rectangle> paperSizeHash = new Hashtable<String,Rectangle>();
		paperSizeHash.put("A0L", PageSize.A0.rotate());
		paperSizeHash.put("A1L", PageSize.A1.rotate());
		paperSizeHash.put("A2L", PageSize.A2.rotate());
		paperSizeHash.put("A3L", PageSize.A3.rotate());
		paperSizeHash.put("A4L", PageSize.A4.rotate());
		paperSizeHash.put("A0P", PageSize.A0);
		paperSizeHash.put("A1P", PageSize.A1);
		paperSizeHash.put("A2P", PageSize.A2);
		paperSizeHash.put("A3P", PageSize.A3);
		paperSizeHash.put("A4P", PageSize.A4);
		paperSizeHash.put("A0", PageSize.A0);
		paperSizeHash.put("A1", PageSize.A1);
		paperSizeHash.put("A2", PageSize.A2);
		paperSizeHash.put("A3", PageSize.A3);
		paperSizeHash.put("A4", PageSize.A4);

		Document document = new Document(paperSizeHash.get(pagestr), 0, 0, 0, 0);
//		step 2: creation of the writer
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(outfilename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// step 3: we open the document
		document.open();

		// step 4: we grab the ContentByte and do some stuff with it

		// we create a fontMapper and read all the fonts in the font directory
		DefaultFontMapper mapper = new DefaultFontMapper();
		FontFactory.registerDirectories();
		mapper.insertDirectory("c:\\windows\\fonts");

		// we create a template and a Graphics2D object that corresponds with it
		for(PBounds pn: pnlist){
			canvas.getCamera().animateViewToCenterBounds(pn, true, 0);
			PdfContentByte cb = writer.getDirectContent();
			Graphics2D g2 = cb.createGraphicsShapes(paperSizeHash.get(pagestr).getWidth(), paperSizeHash.get(pagestr).getHeight());

			canvas.paintComponent(g2);

			g2.dispose();
			document.newPage();
		}

		// step 5: we close the document
		document.close();
	}
}
