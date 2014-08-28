package abs.api.primesieves;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import abs.api.Actor;
import abs.api.Configuration;
import abs.api.Context;
import abs.api.DispatchInbox;
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
	
	//defining all data structure we need for actors.
	/** The actors. Each actor is a processes whose methods are invoked asynchronously */
	private final List<Actor> actors = new LinkedList<>();
	
	/** The sieves.Used for verifying correctness */
	private final List<Sieve> sieves = new LinkedList<>();
	
	/** The context. Required by the actors to invoke methods. */
	// copy paste this to use the ABS API into the code(configuration settings)
	private static final Configuration config = Configuration
			.newConfiguration()
			.withInbox(new DispatchInbox(Executors.newWorkStealingPool()))
			.build();
	private final Context context = new LocalContext(config);

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
				//defining the object(s) and the actors(as) corresponding to the objects created.
				//standard template for defining objects is same as java type(object(o).method(m)())
				//standard template for defining actors is using (context.newActor)
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
		//logic for broadcasting messages between all actors
		for (final Actor s : actors) {
//					s.init(true);
			//this is where the magic happens for the initialization pahse,
			//composing a message in ABS API format
			//we compose a runnable message that needs to be created to send by the actor"s" to different
			//actors defined above in the for loop. once again it is typical java format
			//object.method() format.
			//but copy paste context.notary template, this is standard for ABS api to create a message
			
			Runnable msg = () -> ((Sieve) context().notary().get(s)).init(new Boolean(true));
			
			// we are sending the message"msg"from actor "as" to different actors"s".ayschnoroncly we use
			//future to save the results processes by the actor and continue the process of remaining actors
			//without stoping the process.
			Future<?> r = send(s, msg);
//            Future<?> r = invoke(s, "init", new Boolean(true)); 
            initFutures.add(r);
                                }
		this.init(true);
//		System.out.println(futures);
//		System.out.println(actors);
// synchronizing the execution of all processes by the actors so that the program comes to an halt
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
			
			if (prime * prime - 1 > target * 2)
				break;
			for (Actor s : actors) {
				//this is where the magic happens for the initialization pahse
					Runnable msg = () -> ((Sieve) context().notary().get(s)).sieve(new Integer(prime));
//					Future<?> r = invoke(s, "sieve", new Integer(prime));
					Future<?> r = send(s, msg);
					workFutures.add(r);
			}
			this.sieve(prime);
			
/*			workFutures.forEach(f -> {
                        try {
                                f.get();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
			workFutures.clear();*/
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
		System.out.println(size+""+""+currentList.cardinality()+sum);
		//System.out.println(currentList);
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
		System.out.println("Computation of" + (prime.collect()+1) +" primes ");
	}
}
