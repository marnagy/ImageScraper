import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgDownloadThread implements Runnable {
    String addr;
    public ImgDownloadThread(String addr){
        this.addr = addr;
    }
    @Override
    public void run() {
        System.out.println("Thread " + this.toString() + " started running..");
        if (Globals.files.contains(addr)){
            return;
        }
        URL url = null;
        try {
            url = new URL(addr);
        } catch (MalformedURLException e) {
            System.out.println("Error loading img from " + addr);
            return;
        }
        String[] temp = addr.split("/");
        String filename = temp[temp.length - 1];
        File directoryPath = new File(".");
        File outFile = new File(filename);
        try (FileOutputStream fos = new FileOutputStream(outFile);
             InputStream is = url.openStream()) {
            copystream(is, fos);
            Globals.files.add(addr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " + this.toString() + " stopped running");
    }
    private void copystream(InputStream is, FileOutputStream fos) {
        try (BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(fos)){
            byte[] buffer = new byte[4096];
            int count;
            while ( (count = bis.read(buffer)) != -1 ){
                bos.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
