import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EffectiveLines {
    private int lineNum=0;
    private void count(String fileName) throws IOException {
        File file=new File(fileName);
        String currentLine=null;
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            while(true) {
                currentLine=br.readLine();
                if(currentLine==null)
                    break;
                currentLine=currentLine.trim();
                //去除空行和单行注释
                if(currentLine.equals("")||currentLine.startsWith("//")) ;
                else
                    lineNum++;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println(lineNum);
    }

    public static void main(String[] args) throws IOException{
        String file="F:/test.java";
        EffectiveLines el=new EffectiveLines();
        el.count(file);
    }
}
