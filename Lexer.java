import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String fileName;

    Lexer(String fN) {
        fileName = fN;
    }

    public String run() {

        String data = "";
        int position = 0;
        Pattern p;
        Matcher m;

        try {
            File myFile = new File(fileName);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            data = data.substring(0,data.length()-1);
            myReader.close();
        } catch (FileNotFoundException e) {
            return "File not found.";
        }

        while(position<data.length()-1){

            String t = data.substring(position, position+1);
            String regex = "-|[a-zA-Z0-9 {}(),.;\"<>=\n]";
            p = Pattern.compile(regex);
            m = p.matcher(t);

            if(m.find()){


                
            }else{
                return "LEXICAL ERROR";
            }

            position++;
        }

        return null;
    }

}
