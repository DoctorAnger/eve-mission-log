package evemissionlog;

public class Main {
    public static void main(String[] args) {  
        LogParser logParser = new LogParser();
        ClipboardListener board = new ClipboardListener(logParser);
        board.start();
    }  
}
