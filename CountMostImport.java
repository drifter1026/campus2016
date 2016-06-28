import java.io.*;
import java.util.*;

public class CountMostImport {
    private HashMap<String , Integer> hashMap = new HashMap<String , Integer>();

    public void getfile(String filename) {
        File file = new File(filename);
        if (file.isDirectory()) {
            File []files = file.listFiles();
            for (File ff : files) {
                this.getfile(String.format("%s", ff));
            }
        }
        if (file.isFile()){
            count(String.format("%s", file));
        }
    }

    public void count(String filename) {
        try {
            FileReader fin = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fin);  //打开文件

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("import")) {
                    int begin = 6;
                    while (line.charAt(begin) == ' ') {
                        ++begin;
                    }
                    String classpath = line.substring(begin,line.length()-1);
                    if (hashMap.containsKey(classpath)) {
                        int value = hashMap.get(classpath);
                        value++;
                        hashMap.put(classpath,value);
                    } else {
                        hashMap.put(classpath,1);
                    }
                }
                //遇到类声明import语句结束
                if (line.startsWith("public") || line.startsWith("class")) {
                    break;
                }
            }
            bufferedReader.close();
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sort() {
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(hashMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o1.getValue() == o2.getValue()) {
                    return o1.getKey().compareTo(o2.getKey());
                }
                return o1.getValue() - o2.getValue();
            }
        });
        int cursor = 0;
        for (Map.Entry<String, Integer> map: list) {
            System.out.println(map.getKey()+"  :  "+map.getValue());
            cursor++;
            if (cursor >= 10) {
                break;
            }
        }
    }
    public static void main(String[] args) {
        CountMostImport countMostImport = new CountMostImport();
        countMostImport.getfile("D:/JavaPrograms/");  //输入文件目录
       // countMostImport.getfile("D:/JavaPrograms/CountMostImport/src/CountMostImport.java");
        countMostImport.sort();

    }
}
