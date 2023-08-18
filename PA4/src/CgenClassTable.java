/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Enumeration;

/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;


    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + stringclasstag);

    }
    
    private void codeClassNameTables(){
        str.print(CgenSupport.CLASSNAMETAB+CgenSupport.LABEL);
        /*for(int i=0;i<nds.size();i++){
            str.print(CgenSupport.WORD);
            ((StringSymbol)((CgenNode)nds.get(i)).getStrConst()).codeRef(str);
            str.println("");
        }*/
        codeClassNameTableHelper(root());
    }
    
    private void codeClassNameTableHelper(CgenNode node){
        str.print(CgenSupport.WORD);
        ((StringSymbol)node.getStrConst()).codeRef(str);
        str.println("");
        for(Enumeration e=node.getChildren();e.hasMoreElements();)
            codeClassNameTableHelper((CgenNode)e.nextElement());
    }
    
    private void codeClassObjTables(){
        str.print(CgenSupport.CLASSOBJTAB+CgenSupport.LABEL);
        /*for(int i=0;i<nds.size();i++){
            str.print(CgenSupport.WORD);
            CgenSupport.emitProtObjRef(((CgenNode)nds.get(i)).getName(), str);
            str.println("");
            str.print(CgenSupport.WORD);
            CgenSupport.emitInitRef(((CgenNode)nds.get(i)).getName(), str);
            str.println("");
        }*/
        codeClassObjTableHelper(root());
    }
    
    private void codeClassObjTableHelper(CgenNode node){
        str.print(CgenSupport.WORD);
        CgenSupport.emitProtObjRef(node.getName(), str);
        str.println("");
        str.print(CgenSupport.WORD);
        CgenSupport.emitInitRef(node.getName(), str);
        str.println("");
        for(Enumeration e=node.getChildren();e.hasMoreElements();)
            codeClassObjTableHelper((CgenNode)e.nextElement());
    }
    
    private void codeDispatchTables(){
        for(int i=0;i<nds.size();i++){
            AbstractSymbol sym = ((CgenNode)nds.get(i)).name;
            ArrayList<Method> methods = getMethods(sym);
            ((CgenNode)nds.get(i)).setMethodList(methods);
            
            CgenSupport.emitDispTableRef(sym, str);
            str.print(CgenSupport.LABEL);
            for(Method m: methods){
                str.print(CgenSupport.WORD);
                CgenSupport.emitMethodRef(m._class, m.m.name, str);
                str.println("");
            }
        }
    }
    
    private void codeObjectPrototypes(){
        IntSymbol sInt0 = (IntSymbol) AbstractTable.inttable.addString("0");
        StringSymbol sStr0 = (StringSymbol) AbstractTable.stringtable.addString("");
                
        for(int i=0;i<nds.size();i++){
            AbstractSymbol sym = ((CgenNode)nds.get(i)).name;
            ArrayList<Attribute> attrs = getAttributes(sym);
            ((CgenNode)nds.get(i)).copySymbols(attrs);
            
            str.println(CgenSupport.WORD + "-1");
            CgenSupport.emitProtObjRef(sym, str);
            str.print(CgenSupport.LABEL);
            
            str.println(CgenSupport.WORD+((CgenNode)nds.get(i)).index);
            str.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS+attrs.size()));
            str.print(CgenSupport.WORD); CgenSupport.emitDispTableRef(sym, str);
            str.println("");
            
            //Code attr table
            for ( Attribute attr : attrs){
                str.print(CgenSupport.WORD);
                
                if(attr.a.type_decl.equals(TreeConstants.Int)){
                    sInt0.codeRef(str);
                    str.println();
                }else if(attr.a.type_decl.equals(TreeConstants.Bool)){
                    BoolConst.falsebool.codeRef(str);
                    str.println();
                }else if(attr.a.type_decl.equals(TreeConstants.Str)){
                    sStr0.codeRef(str);
                    str.println();
                }else
                    str.println("0");
            }
        }
    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }
    
    private void codeClassInit(){
        for(int i=0;i<nds.size();i++){
            CgenNode _class = ((CgenNode)nds.get(i));
            _class.codeInit(str);
        }
    }
    
    private void codeClassMethods(){
        for(int i=0;i<nds.size();i++){
            CgenNode _class = ((CgenNode)nds.get(i));
            if(!_class.basic())
                _class.codeMethods(str);
        }
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD 
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Done constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	AbstractTable.inttable.addString("0");

	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);
    }


    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));
	
	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_ Object_class = 
	    new class_(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_ IO_class = 
	    new class_(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(IO_class, CgenNode.Basic, this));

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_ Int_class = 
	    new class_(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_ Bool_class = 
	    new class_(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_ Str_class =
	    new class_(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formal(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }
	
    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.
    
    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
	    installClass(new CgenNode((Class_)e.nextElement(), 
				       CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
        generateIndex(root(), 0);
    }
    
    public int generateIndex(CgenNode _class, int idx){
        Enumeration ch = _class.getChildren();
        _class.index = idx++;
        
        while(ch.hasMoreElements()){
            idx = generateIndex((CgenNode)ch.nextElement(), idx);
        }
        
        return idx;
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
	nds = new Vector();

	this.str = str;

	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");
	
	installBasicClasses();
	installClasses(cls);
	buildInheritanceTree();

        stringclasstag = ((CgenNode)probe(TreeConstants.Str)).index; /* Change to your String class tag here */;
	intclasstag =    ((CgenNode)probe(TreeConstants.Int)).index; /* Change to your Int class tag here */;
	boolclasstag =   ((CgenNode)probe(TreeConstants.Bool)).index; /* Change to your Bool class tag here */;
	
        //code();

	//exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

	//                 Done your code to emit
        //                   - class_nameTab
        if (Flags.cgen_debug) System.out.println("coding class_nameTab");
        codeClassNameTables();
        
        //                   - class_objTab
        if (Flags.cgen_debug) System.out.println("coding class_objTab");
        codeClassObjTables();
	//                   - dispatch tables
        if (Flags.cgen_debug) System.out.println("coding dispatch_tables");
        codeDispatchTables();
        
        //                   - prototype objects        
        if (Flags.cgen_debug) System.out.println("coding prototype objects");
        codeObjectPrototypes();

	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

	//                 Done your code to emit
	//                   - object initializer
        if (Flags.cgen_debug) System.out.println("coding class initializers");
        codeClassInit();
        
	//                   - the class methods
        if (Flags.cgen_debug) System.out.println("coding class methods");
        codeClassMethods();

	//                   - etc...
    }

    /** Gets the root of the inheritance tree */
    public CgenNode root() {
	return (CgenNode)probe(TreeConstants.Object_);
    }
    
    public int getLastChildIndex(AbstractSymbol name){
        CgenNode node = (CgenNode) probe(name);
        CgenNode child = null;
        int idx = node.index;
        
        Enumeration ch = node.getChildren();
        while(ch.hasMoreElements()){
            child = (CgenNode) ch.nextElement();
        }
        
        if(child!=null)
            idx = getLastChildIndex(child.name);
        
        return idx;
    }
    
    private ArrayList<Attribute> getAttributes(AbstractSymbol symbol){
        ArrayList<Attribute> attrs = new ArrayList<Attribute>();
        CgenNode _class = (CgenNode)probe(symbol);
        
        if(_class == null || _class.getName().equals(TreeConstants.No_class))
            return attrs;
        
        attrs.addAll(getAttributes(_class.getParent())); 
        
        for (Enumeration e = _class.features.getElements(); e.hasMoreElements();) {
	    Feature f = ((Feature)e.nextElement());
            
            if(f instanceof attr)
                attrs.add(new Attribute((attr)f, _class.getName()));
        }
        
        return attrs;
    }
    
    private ArrayList<Method> getMethods(AbstractSymbol symbol){
        ArrayList<Method> methods = new ArrayList<Method>();
        CgenNode _class = (CgenNode)probe(symbol);
        
        if(_class == null || _class.getName().equals(TreeConstants.No_class))
            return methods;
        
        methods.addAll(getMethods(_class.getParent())); 
        
        for (Enumeration e = _class.features.getElements(); e.hasMoreElements();) {
	    Feature f = ((Feature)e.nextElement());
            
            if(f instanceof method){
                Method m = new Method((method)f, _class.getName());
                if(!methods.contains(m))
                    methods.add( m );
                else
                    methods.get(methods.indexOf(m))._class = m._class;
            }
        }
        
        return methods;
    }
    
    public static class Attribute {
        attr a;
        AbstractSymbol _class;
        int index;

        public Attribute(attr a, AbstractSymbol _class) {
            this.a = a;
            this._class = _class;
            index = 0;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if(obj instanceof AbstractSymbol){
                final AbstractSymbol name = (AbstractSymbol)obj;
                return this.a != null && this.a.name.equals(name);
            }else if (getClass() != obj.getClass()) {
                return false;
            }
            final Attribute other = (Attribute) obj;
            if (this.a == null || other.a ==null || !this.a.name.equals(other.a.name)) {
                return false;
            }
            return true;
        }
    }
    
    public static class Method {
        method m;
        AbstractSymbol _class;

        public Method(method m, AbstractSymbol _class) {
            this.m = m;
            this._class = _class;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if(obj instanceof AbstractSymbol){
                final AbstractSymbol name = (AbstractSymbol)obj;
                return this.m != null && this.m.name.equals(name);
            }
            else if (getClass() != obj.getClass()) {
                return false;
            }
            final Method other = (Method) obj;
            if (this.m == null || other.m == null || !this.m.name.equals(other.m.name)) {
                return false;
            }
            return true;
        }
    }
}
			  
    
