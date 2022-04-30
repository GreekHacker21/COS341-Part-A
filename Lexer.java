import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String fileName;
    public Token head = null, tail = null;
    public int position;

    Lexer(String fN) {
        fileName = fN;
    }

    public Token run() {

        String data = "";
        int position = 0, line = 0;
        Pattern p;
        Matcher m;

        try {
            File myFile = new File(fileName);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            data = data.substring(0, data.length() - 1);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return new Token("File not found.");
        }

        while (position < data.length() - 1) {

            String t = data.substring(position, position + 1);
            String regex = "-|[a-zA-Z0-9 {}(),.;\"<>=\n]";
            p = Pattern.compile(regex);
            m = p.matcher(t);

            if (m.find()) {
                if (t.equals("\n")) {
                    // System.out.println("Newline character");
                    line++;
                    position++;
                    continue;
                }

                if (t.equals(" ")) {
                    position++;
                    continue;
                }

                if (t.equals("\"")) {
                    position++;
                    if (!addToken(scanForShortString(position, data))) {
                        System.out.println("LEXICAL ERROR");
                        System.out.println("The character " + t + " on line " + line + " is not allowed.");
                        return new Token("LEXICAL ERROR");
                    }
                    continue;
                }

                // use substring for the rest of the tokens and regular expressions.

            } else {
                System.out.println("LEXICAL ERROR");
                System.out.println("The character " + t + " on line " + line + " is not allowed.");
                return new Token("LEXICAL ERROR");
            }

        }

        return null;
    }

    public boolean addToken(Token t) {
        if (t == null) {
            return false;
        }
        if (head == null) {
            head = t;
            tail = t;
            return true;
        }
        tail.next = t;
        tail = t;
        return true;

    }

    public Token scanForShortString(int pos, String data) {
        Pattern p;
        Matcher m;
        int count = 0;
        while (count <= 15) {
            String regex = "([A-Z]|[0-9]|[ ]){0,15}\"";
            p = Pattern.compile(regex);
            m = p.matcher(data.substring(pos, pos + count + 1));

            if (m.find()) {
                if (data.substring(pos + count, pos + count + 1) == "\"") {
                    break;
                }
            } else {
                return null;
            }
        }
        if (data.substring(pos, pos + count + 1).contains("\"")) {

            position = pos + count;
            return new Token(data.substring(pos - 1, pos + count + 1), "ShortString");
        }
        return null;
    }

}
