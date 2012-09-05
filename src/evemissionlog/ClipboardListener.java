package evemissionlog;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.text.ParseException;

//Courtesy of marc weber
public class ClipboardListener extends Thread implements ClipboardOwner {  
    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();  

    private LogParser logParser;
    
    public ClipboardListener(LogParser logParser)
    {
        super();
        this.logParser = logParser;
    }
    
    @Override
    public void run() {  
        Transferable trans = sysClip.getContents(this);  
        regainOwnership(trans);  
        System.out.println("Listening to board...");  
        while(true) {}  
    }  

    @Override
    public void lostOwnership(Clipboard c, Transferable t) {
        try {  
            this.sleep(20);  
        } catch(Exception e) {  
            System.out.println("Exception: " + e);  
        }  
        Transferable contents = sysClip.getContents(this);
        processContents(contents);  
        regainOwnership(contents);  
    }  

    void processContents(Transferable trans) {  
        if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String s = (String) trans.getTransferData(DataFlavor.stringFlavor);
                logParser.parse(s);
            } catch (UnsupportedFlavorException e2) {
            } catch (IOException e2) {
            } catch (ParseException e3) {
            }
        } 
    }  

    void regainOwnership(Transferable t) {  
        sysClip.setContents(t, this);  
    }
}  
