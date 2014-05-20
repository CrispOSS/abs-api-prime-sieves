package abs.api.primesieves;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import abs.api.Actor;
import abs.api.Context;
import abs.api.LocalContext;

// TODO: Auto-generated Javadoc
/**
 * The Class Generator.
 *
 * @author Vlad-Nicolae Serbanescu
 * @author Behrooz Nobakht
 */

public class Generator extends Sieve {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The target. */
	private int target = 0;
	
	/** The actors. Each actor is a processes whose methods are invoked asynchronously */
	private final List<Actor> actors = new LinkedList<>();
	
	/** The sieves.Used for verifying correctness */
	private final List<Sieve> sieves = new LinkedList<>();
	
	/** The context. Required by the actors to invoke methods. */
	private final Context context = new LocalContext();

	/**
	 * Instantiates a new generator.
	 *
	 * @param target number up to which to calculate primes
	 * @param par number of parallel processes
	 */
	public Generator(int target, int par) {
		super(3, (target % par == 0) ? target / par : target / par + 1, 0,
				target % par);
		this.target = target;
		int modulo = target % par;
		int size = target / par;
		//System.out.println(par + " limit " + size);
		for (int i = 1; i < par; ++i) {
			if (i < modulo) {
				Sieve s = new Sieve(3, size + 1, i, modulo);
				Actor as = context.newActor(s.name().toString(), s); //this is required by invoke
				actors.add(as);
				sieves.add(s);
			} else {
				Sieve s = new Sieve(3, size, i, modulo);
				Actor as = context.newActor(s.name().toString(), s);
				actors.add(as);
				sieves.add(s);
			}
		}

	}

	/**
	 * Run_par The method that runs the sieveing algorithm in parallel for each prime number.
	 */
	public void run_par() {
		//System.out.println(actors.size());
		//System.out.println(sieves.size());
		Set<Future<?>> initFutures = new HashSet<>();
		for (final Actor s : actors) {
//					s.init(true);
			//this is where the magic happens for the initialization pahse
			Runnable msg = () -> ((Sieve) context().notary().get(s)).init(new Boolean(true));
			Future<?> r = send(s, msg);
//            Future<?> r = invoke(s, "init", new Boolean(true)); 
            initFutures.add(r);
                                }
		this.init(true);
//		System.out.println(futures);
//		System.out.println(actors);

		initFutures.forEach(f -> {
                        try {
                                f.get();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
		Set<Future<?>> workFutures = new HashSet<>();
		int i=currentList.nextClearBit(0);
		while (i<currentList.size()){
			
			int prime = i * 2 + offset;
			if (prime * prime > target * 2)
				break;
			for (Actor s : actors) {
				//this is where the magic happens for the initialization pahse
					Runnable msg = () -> ((Sieve) context().notary().get(s)).sieve(new Integer(prime));
//					Future<?> r = invoke(s, "sieve", new Integer(prime));
					Future<?> r = send(s, msg);
					workFutures.add(r);
			}
			this.sieve(prime);
			i=currentList.nextClearBit(i+1);
		}
		workFutures.forEach(f -> {
			try {
				f.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/* (non-Javadoc)
	 * @see abs.api.primesieves.Sieve#collect()
	 */
	@Override
	public Integer collect() {
//		System.out.println(Arrays.asList(currentList));
		int sum =size-currentList.cardinality();
		for (Sieve s : sieves) {
			 sum+= s.collect();
		}
		
		return sum;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Integer n = 10000,p=1;
		Boolean print = false;
		if (args.length != 0) {
			n = Integer.parseInt(args[0]);
			// (args.length > 1) {
			//rint = Boolean.parseBoolean(args[1]);
			p = Integer.parseInt(args[1]);
			//}
		}
		//stem.out.println(n+" "+p);
		Generator prime = new Generator(n/2, p);
		//nal long start = System.currentTimeMillis();
		prime.run_par();
		
		//nal long end = System.currentTimeMillis();
		/*List<Integer> result = prime.collect();
		if (print) {
			System.out.println(result);
		}*/
		//ng duration = end - start;
		//stem.out.println("Computation of" + (prime.collect()+1) +" primes ");
	}
}
