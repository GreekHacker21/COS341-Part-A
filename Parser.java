import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Parser {

    public Token root;
    public Token current;
    public SyntaxError error;
    public DocumentBuilderFactory docFactory;
    public DocumentBuilder docBuilder;
    public Document doc;

    Parser(Token r) {
        root = r;
        current = root;
        try {
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }

    }

    public void parse() {
        try {
            SPLProgr();
        } catch (SyntaxError e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult output = new StreamResult(new File("output.xml"));
            transformer.transform(source, output);
            System.out.println("File saved!");
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    // SPLProgr → ProcDefs main { Algorithm halt ; VarDecl }
    public void SPLProgr() throws SyntaxError {
        if (current.value.equals("proc")) {
            ProcDefs();
        }

        if (!current.value.equals("main")) {
            throw new SyntaxError("Missing main keyword on line " + current.line + ".");
        }

        if (hasNext()) {
            goToNext();
        } else {
            throw new SyntaxError("Missing { after main on line " + current.line + ".");
        }

        if (!current.value.equals("{")) {
            throw new SyntaxError("Missing { after main on line " + current.line + ".");
        }

        if (hasNext()) {
            goToNext();
        } else {
            throw new SyntaxError("Missing the keyword halt on line " + current.line + ".");
        }

        if (current.value.equals("output") || current.type.equals("userDefinedName") || current.value.equals("{")) {
            Algorithm();
        }

    }

    // ProcDefs → // nothing (nullable)
    // ProcDefs → PD , ProcDefs
    public void ProcDefs() throws SyntaxError {

    }

    // PD → proc userDefinedName { ProcDefs Algorithm return ; VarDecl }
    public void PD() throws SyntaxError {

    }

    // Algorithm → // nothing (nullable)
    // Algorithm → Instr ; Algorithm
    public void Algorithm() throws SyntaxError {

    }

    // Instr → Assign
    // Instr → Branch
    // Instr → Loop
    // Instr → PCall
    public void Instr() throws SyntaxError {

    }

    // Assign → LHS := Expr
    public void Assign() throws SyntaxError {

    }

    // Branch → if (Expr) then { Algorithm } Alternat
    public void Branch() throws SyntaxError {

    }

    // Alternat → // nothing (nullable)
    // Alternat → else { Algorithm }
    public void Alternat() throws SyntaxError {

    }

    // Loop → do { Algorithm } until (Expr)
    // Loop → while (Expr) do { Algorithm }
    public void Loop() throws SyntaxError {

    }

    // LHS → output
    // LHS → userDefinedName VarFieldChoice
    public void LHS() throws SyntaxError {

    }

    // VarFieldChoice → null // new
    // VarFieldChoice → Field
    public void VarFieldChoice() throws SyntaxError {

    }

    // Expr → Const
    // Expr → userDefinedName VarFieldChoice
    // Expr → UnOp
    // Expr → BinOp
    public void Expr() throws SyntaxError {

    }

    // PCall → call userDefinedName
    public void PCall() throws SyntaxError {

    }

    // Var → userDefinedName
    public void Var() throws SyntaxError {

    }

    // Field → [FieldIndex
    public void Field() throws SyntaxError {

    }

    // FieldIndex → FieldVar
    // FieldIndex → FieldConst
    public void FieldIndex() throws SyntaxError {

    }

    // FieldVar → Var]
    public void FieldVar() throws SyntaxError {

    }

    // FieldConst → Const]
    public void FieldConst() throws SyntaxError {

    }

    // Const → ShortString
    // Const → Number
    // Const → true
    // Const → false
    public void Const() throws SyntaxError {

    }

    // UnOp → input(Var)
    // UnOp → not(Expr)
    public void UnOp() throws SyntaxError {

    }

    // BinOp → and(Expr,Expr)
    // BinOp → or(Expr,Expr)
    // BinOp → eq(Expr,Expr)
    // BinOp → larger(Expr,Expr)
    // BinOp → add(Expr,Expr)
    // BinOp → sub(Expr,Expr)
    // BinOp → mult(Expr,Expr)
    public void BinOp() throws SyntaxError {

    }

    // VarDecl → // nothing (nullable)
    // VarDecl → Dec ; VarDecl
    public void VarDecl() throws SyntaxError {

    }

    // Dec → TYP Var
    // Dec → arr TYP[Const] Var
    public void Dec() throws SyntaxError {

    }

    // TYP → num
    // TYP → bool
    // TYP → string
    public void TYP() throws SyntaxError {

    }

    public boolean hasNext() {
        return current.next != null;
    }

    public void goToNext() {
        current = current.next;
    }

}
