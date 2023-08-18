import java.io.PrintStream;
import java.lang.instrument.ClassDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.  */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;

    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    
    class HierarchyNode{
        class_c classDefinition;
        HierarchyNode father;
        Set<HierarchyNode> children;

        public HierarchyNode(class_c classDefinition) {
            this.classDefinition = classDefinition;
            this.father = null;
            this.children = new HashSet<HierarchyNode>();
        }
        
        public HierarchyNode addChildNode(HierarchyNode node){
            node.father = this;
            
            if(children.add(node))
                return node;
            return null;
        }
        
        public HierarchyNode addChildNode(class_c classDefinition){
            HierarchyNode node = new HierarchyNode(classDefinition);
            
            return addChildNode(node);           
        }
        
        public boolean equals(HierarchyNode obj){
            return obj!=null && obj.classDefinition.name.getString().equals(classDefinition.name.getString());
        }
        
        /**Test if this node is ancester of obj node*/
        public boolean isAncester(HierarchyNode obj){
            if(obj == null || obj.father==null)
                return false;
            
            if(this.equals(obj.father))
                return true;
            return isAncester(obj.father);
        }
    }
    
    private Map<String, HierarchyNode> mapClasses;
    private HierarchyNode rootHierarchy;
   
    public HierarchyNode addChildClass(String baseClass, class_c childClass){
        HierarchyNode father = mapClasses.get(baseClass);
        
        if(father==null)
            return null;
        
        HierarchyNode node = father.addChildNode(childClass);
        if(node!=null)
            mapClasses.put(childClass.getName().getString(), node);
        
        return node;
    }
    
    /*public void defineClass(String _class){
        mapClasses.put(_class, null);
    }*/
    
    public boolean isDefinedClass(String _class){
        return mapClasses.containsKey(_class);
    }
    
    public class_c lookupClass(String _class){
        HierarchyNode node = mapClasses.get(_class);
        
        if(node!=null)
            return node.classDefinition;
        return null;
    }
    
    public boolean isAncesterOfClass(String _ancester, String _class){
        HierarchyNode ancesterNode = mapClasses.get(_ancester);
        HierarchyNode classNode = mapClasses.get(_class);
        
        if(ancesterNode==null || classNode==null)
            return false;
        
        return ancesterNode.isAncester(classNode);
    }
    
    public class_c lub(HierarchyNode nodeOne, HierarchyNode nodeTwo){
        if(nodeOne.father==null || nodeTwo.father==null)
            return mapClasses.get(TreeConstants.Object_.getString()).classDefinition;
        
        if (nodeOne.equals(nodeTwo))
            return nodeOne.classDefinition;
        
        if(nodeOne.isAncester(nodeTwo))
            return nodeOne.classDefinition;
        
        if(nodeTwo.isAncester(nodeOne))
            return nodeTwo.classDefinition;
        
        return lub(nodeOne.father, nodeTwo.father);
    }
    
    public class_c lub(String classOne, String classTwo){
        HierarchyNode nodeOne = mapClasses.get(classOne);
        HierarchyNode nodeTwo = mapClasses.get(classTwo);
        
        if(nodeOne==null || nodeTwo==null)
            return null;
        return lub(nodeOne, nodeTwo);
    }
    
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// The following demonstrates how to create dummy parse trees to
	// refer to basic Cool classes.  There's no need for method
	// bodies -- these are already built into the runtime system.

	// IMPORTANT: The results of the following expressions are
	// stored in local variables.  You will want to do something
	// with those variables at the end of this method to make this
	// code meaningful.

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object
        
        //Create object mapclasses
        mapClasses = new HashMap<String, HierarchyNode>();

	class_c Object_class = 
	    new class_c(0, 
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
        
        //Create and map the Object class node which is the root hierarchy class
        Object_class.buildSymbolTableWhitoutCheck(this);
        rootHierarchy = new HierarchyNode(Object_class);
        mapClasses.put(TreeConstants.Object_.getString(), rootHierarchy);
        
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_c IO_class = 
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
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
        
        //Create and map IO class node
        IO_class.buildSymbolTableWhitoutCheck(this);
        HierarchyNode node = addChildClass(TreeConstants.Object_.getString(), IO_class);

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_c Int_class = 
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);
        
        //Create and map Int class node
        Int_class.buildSymbolTableWhitoutCheck(this);
        node = addChildClass(TreeConstants.Object_.getString(), Int_class);

	// Bool also has only the "val" slot.
	class_c Bool_class = 
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);
        
        //Create and map Bool class node
        Bool_class.buildSymbolTableWhitoutCheck(this);
        node = addChildClass(TreeConstants.Object_.getString(), Bool_class);

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_c Str_class =
	    new class_c(0,
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
						  .appendElement(new formalc(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);
        
        //Create and map Str class node
        Str_class.buildSymbolTableWhitoutCheck(this);
        node = addChildClass(TreeConstants.Object_.getString(), Str_class);
    }
	


    public ClassTable(Classes cls) {
	semantErrors = 0;
	errorStream = System.err;
	
	/* fill this in */
        installBasicClasses();
    }

    /** Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(class_c c) {
	return semantError(c.getFilename(), c);
    }
    
    /** Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
	errorStream.println(filename + ":" + t.getLineNumber() + ": ");
	return semantError();
    }
    
    public PrintStream semantNoMainError(){
        errorStream.println("Class Main is not defined.");
	return semantError();
    }
    
    public PrintStream semantNoMethodMainError(class_c c){
        errorStream.println(c.getFilename() + ":1: method main is not defined.");
	return semantError();
    }

    /** Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError() {
	semantErrors++;
	return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
	return semantErrors != 0;
    }
}
			  
    
