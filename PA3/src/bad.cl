class C {
	a : Int;
	b : Bool;
	a : Bool;
	init(x : Int, y : Bool) : C {
           {
		a <- x;
		b <- y;
		self;
           }
	};
	init(x : Int, x : Bool): C {
		self
	};
	init(x: Intx): C {
		self
	};
};

Class Main {
	main():C {
	 {
	  (new C).init(1,1);
	  (new C).init(1,true,3);
	  (new C).iinit(1,true);
	  (new C);
	 }
	};
};
