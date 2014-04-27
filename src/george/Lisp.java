package george;

public class Lisp {
    public final Lisp next;
    //TODO: Add iterators

    private Lisp() {
        next = null;
    }

    public boolean isNil() {
        return next == null;
    }

    final public class List extends Lisp {
        public final Lisp val;
        
        public List(Lisp v, Lisp next) {
            val = v;
            this.next = next;
        }

        public List(Lisp v) {
            this(v, null);
        }

        //TODO: Add iterators
    }

    final public class Variable extends Lisp {
        public final String name;

        public Variable(String nm, Lisp next) {
            name = nm;
            this.next = next;
        }

        public Variable(String nm) {
            this(nm, null);
        }
    }

    final public class Lambda extends Lisp {
        public final Variable var;
        public final List body;

        public Lambda (Variable v, List body) {
            var = v;
            this.body = body;
        }

        public Lisp exec(List l) {
            //TODO
        }
    }
}
