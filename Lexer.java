import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String fileName;
    public Token head = null, tail = null;
    public int position, line;

    Lexer(String fN) {
        fileName = fN;
    }

    public Token run() {

        String data = "";
        position = 0;
        line = 0;
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

                switch (t) {
                    case "(":
                        position++;
                        addToken(new Token(t, "Left Parenthesis"));
                        continue;
                    case ")":
                        position++;
                        addToken(new Token(t, "Right Parenthesis"));
                        continue;
                    case "{":
                        position++;
                        addToken(new Token(t, "Left Curly Bracket"));
                        continue;
                    case "}":
                        position++;
                        addToken(new Token(t, "Right Curly Bracket"));
                        continue;
                    case ";":
                        position++;
                        addToken(new Token(t, "Semi-colon"));
                        continue;
                    case ",":
                        position++;
                        addToken(new Token(t, "Comma"));
                        continue;
                    case "[":
                        position++;
                        addToken(new Token(t, "Left Square Bracket"));
                        continue;
                    case "]":
                        position++;
                        addToken(new Token(t, "Left Square Bracket"));
                        continue;

                }

                regex = "-|[0-9]";
                p = Pattern.compile(regex);
                m = p.matcher(t);
                if (m.find()) {
                    position++;
                    if (!addToken(scanForNumbers(position, data))) {
                        return new Token("LEXICAL ERROR");
                    }
                    continue;
                }

                if (t.equals("\"")) {
                    position++;
                    if (!addToken(scanForShortString(position, data))) {
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
            if (!(pos + count < data.length())) {
                System.out.println("LEXICAL ERROR");
                System.out.println("The string  on line " + line + " is missing an close inverted comma.");
                return null;
            }
            String regex = "([A-Z]|[0-9]|[ ]){0,15}\"";
            p = Pattern.compile(regex);
            m = p.matcher(data.substring(pos, pos + count + 1));

            if (m.find()) {
                if (data.substring(pos + count, pos + count + 1) == "\"") {
                    break;
                }
            } else {
                System.out.println("LEXICAL ERROR");
                System.out.println("The string  on line " + line + " has an illegal character for ShortStrings.");
                return null;
            }
            count++;
        }
        if (data.substring(pos, pos + count + 1).contains("\"")) {

            position = pos + count;
            return new Token(data.substring(pos - 1, pos + count + 1), "ShortString");
        }
        System.out.println("LEXICAL ERROR");
        System.out.println("The string  on line " + line + " is missing an close inverted comma.");
        return null;
    }

    public Token scanForNumbers(int pos, String data) {
        Pattern p;
        Matcher m;
        int count = 0;
        String regex = "[0-9]";
        if (!(pos < data.length())) {
            if (data.substring(pos - 1, pos) == "-") {
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the minus is not followed by any value");
            } else {
                return new Token(data.substring(pos - 1, pos), "Number");
            }
        }
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos, pos + 1));
        while (m.find()) {
            if (pos + count == data.length()) {
                break;
            } else {
                m = p.matcher(data.substring(pos + count, pos + count + 1));
            }
            count++;
        }
        regex = "^([-]?[1-9]+[0-9]*)$|^0$";
        p = Pattern.compile(regex);
        m = p.matcher(data.substring(pos - 1, pos + count));
        if (m.find()) {

            position = pos + count;
            return new Token(data.substring(pos - 1, pos + count), "ShortString");
        }
        if (data.substring(pos - 1, pos) == "0") {
            if (count > 0) {
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the zero cannot be a leading character in a number.");
                return null;
            }
        }
        if (data.substring(pos - 1, pos) == "-") {
            if (count == 0) {
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the negative sign needs a value.");
            }else{
                System.out.println("LEXICAL ERROR");
                System.out.println("On line " + line + " the negative sign cannot be followed by a zero.");
            }
            return null;
        }
        System.out.println("LEXICAL ERROR");
        System.out.println("On line " + line + " unfound error.");
        return null;
    }

}
