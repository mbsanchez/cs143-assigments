// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.Enumeration;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

class Util {
    public static AbstractSymbol getTypeOfId(Object info){
        if(info instanceof attr)
            return ((attr)info).type_decl;
        else if(info instanceof let)
            return ((let)info).type_decl;
        else if (info instanceof formalc)
            return ((formalc)info).type_decl;
        else if (info instanceof branch)
            return ((branch)info).type_decl;
        else if (info instanceof class_c)
            return ((class_c)info).name;
        return null;
    }
    
    public static boolean areSameDispatchTypes(Formals methodf, Formals dispatchf, SymbolTable env){
        if(methodf.getLength() != dispatchf.getLength())
            return false;
        
        Enumeration f1 = methodf.getElements(), f2 = dispatchf.getElements();
        
        for(;f1.hasMoreElements() && f2.hasMoreElements();){
            formalc fp1 = (formalc)f1.nextElement(), fp2 = (formalc)f2.nextElement();
            AbstractSymbol tfp1=getProcessedType(fp1.type_decl, env), tfp2=getProcessedType(fp2.type_decl, env);
            
            class_c lub = programc.classTable.lub(tfp1.getString(), tfp2.getString());
            if(lub==null || !lub.name.getString().equals(tfp1.getString()))
                return false;
        }
        
        return true;
    }
    
    public static AbstractSymbol getProcessedType(AbstractSymbol type, SymbolTable env){
        class_c result;
        if(type.equals(TreeConstants.SELF_TYPE)){
            Object info = env.lookup(TreeConstants.self);
            result = programc.classTable.lookupClass(getTypeOfId(info).getString());
        }else
            result = programc.classTable.lookupClass(type.getString());
        
        return (result==null)?null:result.name;
    }
    
    public static boolean isPrimitive(AbstractSymbol type){
        if(type.equals(TreeConstants.Bool) || type.equals(TreeConstants.Int) || type.equals(TreeConstants.Str))
            return true;
        return false;
    }
}

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Classes
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}


/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void check(SymbolTable env);
}


/** Defines list phylum Features
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}


/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Formals
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}


/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }
    private AbstractSymbol type = null;                                 
    public AbstractSymbol get_type() { return type; }           
    public Expression set_type(AbstractSymbol s) { type = s; return this; } 
    public abstract void dump_with_types(PrintStream out, int n);
    public void dump_type(PrintStream out, int n) {
        if (type != null)
            { out.println(Utilities.pad(n) + ": " + type.getString()); }
        else
            { out.println(Utilities.pad(n) + ": _no_type"); }
    }
    
    public abstract void check(SymbolTable env);
}


/** Defines list phylum Expressions
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}


/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void check(SymbolTable env);

}


/** Defines list phylum Cases
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}


/** Defines AST constructor 'programc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class programc extends Program {
    protected Classes classes;
    public static ClassTable classTable;
    /** Creates "programc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
        classTable = null;
    }
    public TreeNode copy() {
        return new programc(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
	    ((Class_)e.nextElement()).dump_with_types(out, n + 2);
        }
    }
    /** This method is the entry point to the semantic checker.  You will
        need to complete it in programming assignment 4.
	<p>
        Your checker should do the following two things:
	<ol>
	<li>Check that the program is semantically correct
	<li>Decorate the abstract syntax tree with type information
        by setting the type field in each Expression node.
        (see tree.h)
	</ol>
	<p>
	You are free to first do (1) and make sure you catch all semantic
    	errors. Part (2) can be done in a second stage when you want
	to test the complete compiler.
    */
    public void semant() {
        /* ClassTable constructor may do some semantic analysis */
	classTable = new ClassTable(classes);
        
	/* some semantic analysis code may go here */
        passOne_TestClass();

	if (classTable.errors()) {
	    System.err.println("Compilation halted due to static semantic errors.");
	    System.exit(1);
	}else {
            passTwo_CheckRest(classTable);
            if (classTable.errors()) {
                System.err.println("Compilation halted due to static semantic errors.");
                System.exit(1);
            }
        }
        
    }
    
    class ClassStatus{
        protected class_c _class;
        protected int status;
        public static final int INITIALIZED = 0;
        public static final int WAITING = 1;
        public static final int PROCESSED = 2;

        public ClassStatus(class_c _class) {
            this._class = _class;
            this.status = INITIALIZED;
        }
    }
    
    public void passOne_TestClass(){               
        Map<AbstractSymbol, ClassStatus> mclasses = new HashMap<AbstractSymbol, ClassStatus>();
        
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            class_c c = ((class_c)e.nextElement());
            
            if(!mclasses.containsKey(c.name))
                mclasses.put(c.name, new ClassStatus(c));
            else
                classTable.semantError(c);
        }
        
        //proccess classes
        for(AbstractSymbol key : mclasses.keySet()){
            processClass(mclasses.get(key), mclasses);
        }
        
        //generate error in deferred class
        /*for(class_c c : deferred){
            classTable.semantError(c);
        }*/
    }
    
    public void passTwo_CheckRest(ClassTable classTable){
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            class_c c = ((class_c)e.nextElement());
            
            c.check();
        }
        
        //Chequear main
        class_c Main = classTable.lookupClass(TreeConstants.Main.getString());
        Feature main;
        
        if(Main == null)
            classTable.semantNoMainError();
        else if((main = Main.getFeature(TreeConstants.main_meth))==null || !(main instanceof method) )
            classTable.semantNoMethodMainError(Main);                 
    }
    
    public void processClass(ClassStatus c, Map<AbstractSymbol, ClassStatus> mclasses){
        String c_name = c._class.name.getString();
        String c_parent = c._class.parent.getString();
        boolean mapClass = true;
        
        if(c.status == ClassStatus.PROCESSED || c.status == ClassStatus.WAITING)
            return;
         
        c.status = ClassStatus.WAITING;
        //if c is redefined
        if(classTable.isDefinedClass(c_name) || c._class.name.equals(TreeConstants.SELF_TYPE)){
            classTable.semantError(c._class);
            mapClass = false;
        }
        //illegar inherits
        else if(c_name.equals(c_parent) || c._class.parent.equals(TreeConstants.SELF_TYPE) || c._class.parent.equals(TreeConstants.Bool) || c._class.parent.equals(TreeConstants.Str)){
            classTable.semantError(c._class);
            c._class.parent = TreeConstants.Object_;
        }
        // if parent of c has not defined then process father first
        else {
            if (!classTable.isDefinedClass(c_parent)) {
                ClassStatus parent = mclasses.get(c._class.parent);
                
                if(parent==null || parent.status == ClassStatus.WAITING){
                    classTable.semantError(c._class);
                    mapClass = false;
                }else{
                    processClass(parent, mclasses);
                    
                    if(parent.status==ClassStatus.WAITING){
                        classTable.semantError(c._class);
                        mapClass = false;
                    }
                }
            }
        }
        if(mapClass){
            classTable.addChildClass(c_parent, c._class);
            c.status = ClassStatus.PROCESSED;
            //process deferred children
            //processDeferredChildren(c, deferred);
        }
    }
    
    /*public void processDeferredChildren(class_c parent, LinkedList<class_c> deferred){
        for(class_c c :deferred){
            if(c.parent.getString().equals(parent.name.getString())){
                deferred.remove(c);
                processClass(c, deferred);
            }
        }
    }*/
}


/** Defines AST constructor 'class_c'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;
    protected boolean checked;
    protected SymbolTable symTab;
    
    /** Creates "class_c" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
        checked = false;
        symTab = null;
    }
    
    public void buildSymbolTableWhitoutCheck(ClassTable classTable){
        checked = true;
        
        //Add father symbols
        if(!parent.equals(TreeConstants.No_class)){
            class_c cparent = classTable.lookupClass(parent.getString());
            
            if(cparent!=null)
                symTab = cparent.symTab.copy();
        }else
            symTab = new SymbolTable();
        
        symTab.enterScope();        
        //Insert attr and method into symboltable y check that the method or attr is not redefined
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
	    Feature f = (Feature)e.nextElement();
            if(f instanceof attr){
                symTab.addId(((attr)f).name, f);
            }else if( f instanceof method){
                symTab.addId(((method)f).name, f);
            }
        }
    }
    
    public Feature getFeature(AbstractSymbol s){
        if(symTab!=null)
            return (Feature)symTab.lookup(s);
        
        return findFeatureInList(this, s);
    }
    
    private Feature findFeatureInList(class_c c, AbstractSymbol s){
        for (Enumeration e = c.features.getElements(); e.hasMoreElements();) {
            Feature f = (Feature)e.nextElement();
            if(f instanceof attr){
                if(((attr)f).name.equals(s))
                    return f;
            }else if( f instanceof method){
                if(((method)f).name.equals(s))
                    return f;
            }
        }
        
        if(!c.parent.equals(TreeConstants.No_class)){
            class_c cparent = programc.classTable.lookupClass(c.parent.getString());
            return findFeatureInList(cparent, s);
        }
        
        return null;
    }
    
    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }

    
    public AbstractSymbol getFilename() { return filename; }
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
	    ((Feature)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }
    
    public void check(){
        if(checked)
            return;
        
        //First check father
        class_c cp = programc.classTable.lookupClass(parent.getString());
        if(cp!=null){
            cp.check();
            symTab = cp.symTab.copy();
        }else
            symTab = new SymbolTable();
        
        //enter to scope of current class
        symTab.enterScope();
        symTab.addId(TreeConstants.self, this);
        
        //Insert attr and method into symboltable y check that the method or attr is not redefined
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
	    Feature f = (Feature)e.nextElement();
            if(f instanceof attr){
                attr at = (attr)f;
                
                if(at.name.equals(TreeConstants.self) || symTab.lookup(at.name)!=null)
                    programc.classTable.semantError(filename, f);
                else
                    symTab.addId(at.name, f);
            }else if( f instanceof method){
                method result = (method)symTab.lookup(((method)f).name);
                method m = (method)f;
                if(m.name.equals(TreeConstants.self))
                    programc.classTable.semantError(filename, f);
                else if(result!=null){
                    if(!m.hasSameParameterList(result))
                        programc.classTable.semantError(filename, f);
                    else
                        symTab.addId(m.name, f);
                }else
                    symTab.addId(((method)f).name, f);
            }
        }
        
        //check attr expression and method sentences
        TreeConstants.currentFilename = filename;
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
            ((Feature)e.nextElement()).check(symTab);
        }
        checked = true;
    }
}


/** Defines AST constructor 'method'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;
    /** Creates "method" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for formals
      * @param a2 initial value for return_type
      * @param a3 initial value for expr
      */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }
    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals)formals.copy(), copy_AbstractSymbol(return_type), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n+2, name);
        formals.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, return_type);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
	    ((Formal)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
	expr.dump_with_types(out, n + 2);
    }
    
    public boolean hasSameParameterList(method m){
        Enumeration e1 = formals.getElements();
        Enumeration e2 = m.formals.getElements();
        
        for (; e1.hasMoreElements() && e2.hasMoreElements();) {
            formalc f1 = ((formalc)e1.nextElement()), f2 = ((formalc)e2.nextElement());
	    if (!f1.type_decl.equals(f2.type_decl))
                    return false;
            
        }
        
        return !(e1.hasMoreElements() || e2.hasMoreElements());
    }

    @Override
    public void check(SymbolTable env) {
        env.enterScope();
        AbstractSymbol preturn_type = Util.getProcessedType(return_type, env);
        
        //check return type
        if(preturn_type == null){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            preturn_type = TreeConstants.Object_;
        }
        
        //add formal parameters to scope and check that type exists and redefinition
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
            formalc f = ((formalc)e.nextElement());
            
            class_c c = programc.classTable.lookupClass(f.type_decl.getString());
            if(c==null){
                programc.classTable.semantError(TreeConstants.currentFilename, f);
                f.type_decl = TreeConstants.Object_;
            }
            
            if(env.probe(f.name)!=null)
                programc.classTable.semantError(TreeConstants.currentFilename, f);
            else if(f.name.equals(TreeConstants.self))
                programc.classTable.semantError(TreeConstants.currentFilename, f);
            else
                env.addId(f.name, f);
        }
        
        //check method body
        expr.check(env);
        AbstractSymbol expType = Util.getProcessedType(expr.get_type(), env);
        
        if(return_type.equals(TreeConstants.SELF_TYPE) && !expr.get_type().equals(TreeConstants.SELF_TYPE))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        else if(!(expType.equals(preturn_type) || programc.classTable.isAncesterOfClass(preturn_type.getString(), expType.getString())))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            
        env.exitScope();
    }
}


/** Defines AST constructor 'attr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;
    /** Creates "attr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }
    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)init.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
    }

    @Override
    public void check(SymbolTable env) {
        //check type attr exists
        if(!programc.classTable.isDefinedClass(type_decl.getString())){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            type_decl = TreeConstants.Object_;
        }       
        
        init.check(env); 
        AbstractSymbol expType = Util.getProcessedType(init.get_type(), env);
        
        if(!(init instanceof no_expr))
            if(!(expType.equals(type_decl) || programc.classTable.isAncesterOfClass(type_decl.getString(), expType.getString())))
                programc.classTable.semantError(TreeConstants.currentFilename, this);
    }
}


/** Defines AST constructor 'formalc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    /** Creates "formalc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}


/** Defines AST constructor 'branch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;
    /** Creates "branch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for expr
      */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }
    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	expr.dump_with_types(out, n + 2);
    }

    @Override
    public void check(SymbolTable env) {
        env.enterScope();
        
        class_c c = programc.classTable.lookupClass(type_decl.getString());
        
        if(c==null){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            type_decl = TreeConstants.Object_;
        }
        env.addId(name, this);
        expr.check(env);
        AbstractSymbol expType = Util.getProcessedType(expr.get_type(), env);
        
        if(!programc.classTable.lub(type_decl.getString(), expType.getString()).name.equals(type_decl))
            programc.classTable.semantError(TreeConstants.currentFilename, this);

        env.exitScope();
    }

}


/** Defines AST constructor 'assign'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;
    /** Creates "assign" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for expr
      */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }
    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n+2, name);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
	expr.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        //test if variable has been previously declared
        Object info = env.lookup(name);           
        
        //check expression
        expr.check(env);
        AbstractSymbol expType = Util.getProcessedType(expr.get_type(), env);
        set_type(expType);
        
        if(info==null || name.equals(TreeConstants.self) || info instanceof method)
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        else {
            AbstractSymbol type = Util.getTypeOfId(info);

            if(!(type!=null && (type.equals(expType) || programc.classTable.isAncesterOfClass(type.getString(), expType.getString()))))
                programc.classTable.semantError(TreeConstants.currentFilename, this);
        }
    }

}


/** Defines AST constructor 'static_dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "static_dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for type_name
      * @param a2 initial value for name
      * @param a3 initial value for actual
      */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }
    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(type_name), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, type_name);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        expr.check(env);
        AbstractSymbol expType = Util.getProcessedType(expr.get_type(), env);
        class_c parent = programc.classTable.lookupClass(type_name.getString());
        
        set_type(TreeConstants.Object_);
        if(parent==null)
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        else if(type_name.equals(expr.get_type()) || programc.classTable.isAncesterOfClass(type_name.getString(), expType.getString())){
            Feature f = parent.getFeature(name);
            
            if(f==null || !(f instanceof method))
                programc.classTable.semantError(TreeConstants.currentFilename, this);
            else{
                //check formal params expressions
                Formals formals = new Formals(lineNumber);
                method m = (method)f;
                
                for(Enumeration e=actual.getElements(); e.hasMoreElements();){
                    Expression ex = (Expression)e.nextElement();
                    ex.check(env);
                    //AbstractSymbol exType = Util.getProcessedType(ex.get_type(), env);
                   
                    formals.appendElement(new formalc(lineNumber, name, ex.get_type()));
                }
                
                if(!Util.areSameDispatchTypes(m.formals, formals, env))
                    programc.classTable.semantError(TreeConstants.currentFilename, this);
                
                if (m.return_type.equals(TreeConstants.SELF_TYPE))
                    set_type(expr.get_type());
                else {
                    //AbstractSymbol rtype = Util.getProcessedType(m.return_type, env);
                    if(programc.classTable.isDefinedClass(m.return_type.getString()))
                        set_type(m.return_type);
                    else
                        programc.classTable.semantError(TreeConstants.currentFilename, this);
                }
            }
        }else
            programc.classTable.semantError(TreeConstants.currentFilename, this);
    }

}


/** Defines AST constructor 'dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for name
      * @param a2 initial value for actual
      */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }
    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        expr.check(env);
        AbstractSymbol expType = Util.getProcessedType(expr.get_type(), env);
        
        set_type(TreeConstants.Object_);
        if(expType==null)
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        else {
            class_c exprType = programc.classTable.lookupClass(expType.getString());
            Feature f = exprType.getFeature(name);
            
            if(f==null || !(f instanceof method))
                programc.classTable.semantError(TreeConstants.currentFilename, this);
            else{
                //check formal params expressions
                Formals formals = new Formals(lineNumber);
                method m = (method)f;
                
                for(Enumeration e=actual.getElements(); e.hasMoreElements();){
                    Expression ex = (Expression)e.nextElement();
                    ex.check(env);
                    formals.appendElement(new formalc(lineNumber, name, ex.get_type()));
                }
                
                if(!Util.areSameDispatchTypes(m.formals, formals, env))
                    programc.classTable.semantError(TreeConstants.currentFilename, this);
                
                if (m.return_type.equals(TreeConstants.SELF_TYPE))
                    set_type(expr.get_type());
                else if(programc.classTable.isDefinedClass(m.return_type.getString()))
                    set_type(m.return_type);
                else
                    programc.classTable.semantError(TreeConstants.currentFilename, this);
            }
        }
    }

}


/** Defines AST constructor 'cond'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;
    /** Creates "cond" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for then_exp
      * @param a2 initial value for else_exp
      */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }
    public TreeNode copy() {
        return new cond(lineNumber, (Expression)pred.copy(), (Expression)then_exp.copy(), (Expression)else_exp.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n+2);
        then_exp.dump(out, n+2);
        else_exp.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
	pred.dump_with_types(out, n + 2);
	then_exp.dump_with_types(out, n + 2);
	else_exp.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        pred.check(env);
        
        if(!pred.get_type().equals(TreeConstants.Bool)){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        }
        
        then_exp.check(env);
        else_exp.check(env);
        
        AbstractSymbol thenType = Util.getProcessedType(then_exp.get_type(), env);
        AbstractSymbol elseType = Util.getProcessedType(else_exp.get_type(), env);
        
        class_c ltype = programc.classTable.lub(thenType.getString(), elseType.getString());
        
        set_type(ltype.name);
    }

}


/** Defines AST constructor 'loop'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;
    /** Creates "loop" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for body
      */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }
    public TreeNode copy() {
        return new loop(lineNumber, (Expression)pred.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
	pred.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        pred.check(env);
        
        if(!pred.get_type().equals(TreeConstants.Bool)){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        }
        body.check(env);
        set_type(TreeConstants.Object_);
    }
}


/** Defines AST constructor 'typcase'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;
    /** Creates "typcase" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for cases
      */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }
    public TreeNode copy() {
        return new typcase(lineNumber, (Expression)expr.copy(), (Cases)cases.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n+2);
        cases.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
	expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    ((Case)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        expr.check(env);
        AbstractSymbol slub=null;
        Set<AbstractSymbol> declared= new HashSet<AbstractSymbol>();
        
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	     branch b = ((branch)e.nextElement());
             b.check(env);
             AbstractSymbol btype = Util.getProcessedType(b.expr.get_type(), env);
             
             if (declared.contains(b.type_decl))
                 programc.classTable.semantError(TreeConstants.currentFilename, this);
             else
                 declared.add(b.type_decl);
             
             if(slub==null)
                 slub = btype;
             else 
                 slub = programc.classTable.lub(slub.getString(), btype.getString()).name;
        }
        
        set_type(slub);
    }

}


/** Defines AST constructor 'block'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class block extends Expression {
    protected Expressions body;
    /** Creates "block" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for body
      */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }
    public TreeNode copy() {
        return new block(lineNumber, (Expressions)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        Expression expr = null;
        
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    expr = ((Expression)e.nextElement());
            expr.check(env);
        }
        
        if(expr!=null)
            set_type(expr.get_type());
    }

}


/** Defines AST constructor 'let'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;
    /** Creates "let" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for identifier
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      * @param a3 initial value for body
      */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }
    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl), (Expression)init.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n+2, identifier);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
	dump_AbstractSymbol(out, n + 2, identifier);
	dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        env.enterScope();
        
        //class_c typec = programc.classTable.lookupClass(type_decl.getString());
        AbstractSymbol lType = Util.getProcessedType(type_decl, env);
        
        if(lType==null){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            type_decl = TreeConstants.Object_;
        }
        
        if(identifier.equals(TreeConstants.self)){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            type_decl = TreeConstants.Object_;
        }else      
            env.addId(identifier, this);
        
        init.check(env);
        AbstractSymbol iType = Util.getProcessedType(init.get_type(), env);
                
        if(!(init instanceof no_expr) && !(lType.equals(iType) ||  programc.classTable.isAncesterOfClass(lType.getString(), iType.getString())))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            
        body.check(env);
        set_type(body.get_type());
        
        env.exitScope();
    }
}


/** Defines AST constructor 'plus'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "plus" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new plus(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        e2.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Int);
    }

}


/** Defines AST constructor 'sub'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "sub" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new sub(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        e2.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Int);
    }

}


/** Defines AST constructor 'mul'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "mul" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new mul(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        e2.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Int);
    }

}


/** Defines AST constructor 'divide'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "divide" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new divide(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        e2.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Int);
    }

}


/** Defines AST constructor 'neg'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class neg extends Expression {
    protected Expression e1;
    /** Creates "neg" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new neg(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Int))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Int);
    }

}


/** Defines AST constructor 'lt'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "lt" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new lt(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        e2.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Bool);
    }

}


/** Defines AST constructor 'eq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "eq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new eq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        e2.check(env);
        
        if(!e1.get_type().equals(e2.get_type()) && (Util.isPrimitive(e1.get_type()) || Util.isPrimitive(e2.get_type())))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Bool);
    }

}


/** Defines AST constructor 'leq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "leq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new leq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        e2.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Bool);
    }

}


/** Defines AST constructor 'comp'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class comp extends Expression {
    protected Expression e1;
    /** Creates "comp" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new comp(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        
        if(!e1.get_type().equals(TreeConstants.Bool))
            programc.classTable.semantError(TreeConstants.currentFilename, this);
        
        set_type(TreeConstants.Bool);
    }

}


/** Defines AST constructor 'int_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class int_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "int_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
	dump_AbstractSymbol(out, n + 2, token);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        set_type(TreeConstants.Int);
    }

}


/** Defines AST constructor 'bool_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class bool_const extends Expression {
    protected Boolean val;
    /** Creates "bool_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for val
      */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }
    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n+2, val);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
	dump_Boolean(out, n + 2, val);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        set_type(TreeConstants.Bool);
    }
}


/** Defines AST constructor 'string_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class string_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "string_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
	out.print(Utilities.pad(n + 2) + "\"");
	Utilities.printEscapedString(out, token.getString());
	out.println("\"");
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        set_type(TreeConstants.Str);
    }

}


/** Defines AST constructor 'new_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class new_ extends Expression {
    protected AbstractSymbol type_name;
    /** Creates "new_" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for type_name
      */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }
    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n+2, type_name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
	dump_AbstractSymbol(out, n + 2, type_name);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        //class_c type = programc.classTable.lookupClass(type_name.getString());
        AbstractSymbol ntype = Util.getProcessedType(type_name, env);
        
        if(ntype==null){
            programc.classTable.semantError(TreeConstants.currentFilename, this);
            type_name = TreeConstants.Object_;
        }
        
        set_type(type_name);
    }
}


/** Defines AST constructor 'isvoid'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class isvoid extends Expression {
    protected Expression e1;
    /** Creates "isvoid" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        e1.check(env);
        
        set_type(TreeConstants.Bool);
    }

}


/** Defines AST constructor 'no_expr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class no_expr extends Expression {
    /** Creates "no_expr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }
    public TreeNode copy() {
        return new no_expr(lineNumber);
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        set_type(TreeConstants.No_type);
    }
}


/** Defines AST constructor 'object'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class object extends Expression {
    protected AbstractSymbol name;
    /** Creates "object" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }
    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n+2, name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
	dump_AbstractSymbol(out, n + 2, name);
	dump_type(out, n);
    }

    @Override
    public void check(SymbolTable env) {
        if(name.equals(TreeConstants.self)){
            set_type( TreeConstants.SELF_TYPE );
        }else{
            Object type = env.lookup(name);
            AbstractSymbol type_decl = Util.getTypeOfId(type);

            if(type_decl==null){
                programc.classTable.semantError(TreeConstants.currentFilename, this);
                set_type(TreeConstants.Object_);
            }else
                set_type(type_decl);
        }
    }

}


