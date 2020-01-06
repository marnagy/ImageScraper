import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static HashSet<String> files = new HashSet<String>();
    public static void main(String[] args) {
        if (args.length != 1){
            return;
        }
        String page;
        StringBuilder sb;

        URL url;
        BufferedReader br;
        String line;

        try {
            url = new URL(args[0]);
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            sb = new StringBuilder();
            while ( (line = br.readLine()) != null ){
                sb.append(line);
            }
            page = sb.toString();
            sb = null;

            Pattern imgTag = Pattern.compile("<img[^>]+>");
            Pattern sourceAtr = Pattern.compile("src=\"https[^\"]+\"");
            Pattern addrP = Pattern.compile("\"[^\"]+\"");
            Matcher tagMatcher = imgTag.matcher(page);
            String temp;
            while ( tagMatcher.find() ){
                temp = tagMatcher.group();
                System.out.println(temp);

                Matcher sourceMatcher = sourceAtr.matcher(temp);
                if ( sourceMatcher.find() ){
                    String source = sourceMatcher.group();

                    Matcher addrM = addrP.matcher(source);
                    if ( addrM.find() ) {
                        sb = new StringBuilder(addrM.group());
                        sb.deleteCharAt(0);
                        sb.deleteCharAt(sb.length() - 1);
                        downloadImg(sb.toString());
                        sb = null;
                    }
                }
                else{
                    continue;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void downloadImg(String addr) throws MalformedURLException {
        if (files.contains(addr)){
            return;
        }
        URL url = new URL(addr);
        String[] temp = addr.split("/");
        String filename = temp[temp.length - 1];
        File directoryPath = new File(".");
        File outFile = new File(filename);
        try (FileOutputStream fos = new FileOutputStream(outFile);
                InputStream is = url.openStream()) {
            copystream(is, fos);
            files.add(addr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copystream(InputStream is, FileOutputStream fos) {
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
