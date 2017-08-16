import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

import java.util.Vector;

/**
 * Created by shichen on 2017/8/16.
 */


public class Exporter {
    public static Document doc;
    public static PdfWriter pdfWriter;
    public static String inputFile;
    public static String outputFile;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            //inputFile = args[0];
            //outputFile = inputFile.substring(0,inputFile.length()-)
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

    Handler(String f) throws Exception {
        pstFile = new PSTFile(f);
        System.out.println(pstFile.getMessageStore().getDisplayName());
    }

    @Override
    public void run() {
        try {
            processFolder(pstFile.getRootFolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processFolder(PSTFolder folder)
            throws PSTException, java.io.IOException {
        depth++;
        // the root folder doesn't have a display name
        if (depth > 0) {
            printDepth();
            System.out.println(folder.getDisplayName());
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
                System.out.println("\tEmail: "+email.getSubject());
                email = (PSTMessage)folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }

    private void printDepth() {
        for (int x = 0; x < depth-1; x++) {
            System.out.print(" | ");
        }
        System.out.print(" |- ");
    }
}