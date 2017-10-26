package experiment;

import clojure.lang.IDeref;
import clojure.lang.IFn;
import clojure.lang.LockingTransaction;
import clojure.lang.Ref;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class main {

    public static void main(String[] args) {
        System.out.println("hello");

        final Ref ref = new Ref(null);
        dosync(new Callable() {
            @Override
            public Object call() throws Exception {
                Integer i1 = Integer.valueOf(1);
                ref.set(i1);
                Integer i2 = 2;
                ref.set(i2);
                Object o = deref(ref);
                System.out.println(o);
                return null;
            }
        });


    }

    static public void dosync(Callable fn) {
        try {
            LockingTransaction.runInTransaction(fn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public Object deref(Object ref) {
        if (ref instanceof IDeref) {
            return ((IDeref)ref).deref();
        } else {
            try {
                return ((Future)ref).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static public void commute(Ref ref, IFn fn) {
        ref.commute(fn, null);
    }

    static public void alter(Ref ref, IFn fn) {
        ref.alter(fn, null);
    }

    static public void refSet(Ref ref, Object val) {
        ref.set(val);
    }

}
