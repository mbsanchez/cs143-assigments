
import java.awt.SystemColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mbsanchez
 */
public class MySemant {
    /** Reads AST from from consosle, and outputs the new AST */
    public static void main(String[] args) {
	args = Flags.handleFlags(args);
	try {
	    ASTLexer lexer = new ASTLexer(new InputStreamReader(new FileInputStream(new File(args[0]))));
	    ASTParser parser = new ASTParser(lexer);
	    Object result = parser.parse().value;
	    ((Program)result).semant();
	    ((Program)result).dump_with_types(new PrintStream(new File(args[1])), 0);
	} catch (Exception ex) {
	    ex.printStackTrace(System.err);
	}
    }
}
