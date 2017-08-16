import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Vector;

/**
 * Created by shichen on 2017/8/16.
 */


public class Exporter {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("args error");
            System.exit(-1);
        }
        for (String f : args) {
            Handler h = new Handler(f);
            new Thread(h).start();
        }
    }
}

class Handler implements Runnable {
    private int depth = -1;
    private PSTFile pstFile;
    private String outfile;
    private boolean fileIllegal = false;
    private Document doc;
    private BaseFont baseFont;
    private Font font;
    private FileWriter fw;

    Handler(String f) throws Exception {
        if (!f.endsWith(".pst")) {
            System.out.println(f + " not end with .pst. ignore");
            fileIllegal = true;
        }
        fw = new FileWriter("./pst.log");
        pstFile = new PSTFile(f);
        outfile = f.substring(0,f.length()-4) + ".pdf";

        doc = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outfile));

        //support Chinese
        baseFont = BaseFont.createFont("C:/Windows/Fonts/msyh.ttc,1",
                BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        font = new Font(baseFont);

        doc.open();
    }

    @Override
    public void run() {
        if (!fileIllegal) {
            try {
                processFolder(pstFile.getRootFolder());
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                doc.close();
            }
        }

    }

    private void processFolder(PSTFolder folder) throws Exception {
        depth++;
        // the root folder doesn't have a display name
        if (depth > 0) {
            printDepth();
            fw.write(folder.getDisplayName() + "\n");
        }

        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processFolder(childFolder);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {
            depth++;
            PSTMessage email = (PSTMessage)folder.getNextChild();
            while (email != null) {
                printDepth();
                String subject = email.getSubject();
                String body = email.getBody();

                doc.add(new Paragraph(subject,font));
                doc.add(new Paragraph(body,font));
                fw.write("\tEmail: "+ subject + "\n");
                //fw.write("\tEmail: "+ subject  + ",body:\n" + body + "\n");
                email = (PSTMessage)folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }

    private void printDepth() throws Exception{
        for (int x = 0; x < depth-1; x++) {
            fw.write(" | ");
        }
        fw.write(" |- ");
    }
}