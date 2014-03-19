package abs.api.primesieves;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.Arrays;

import abs.api.Actor;
import abs.api.Context;
import abs.api.SystemContext;

/**
 * @author Vlad Nicolae Serbanescu
 * @author Behrooz Nobakht
 */
public class Generator extends Sieve {

	private static final long serialVersionUID = 1L;

	private int target = 0;
	private final List<Actor> actors = new LinkedList<>();
	private final List<Sieve> sieves = new LinkedList<>();
	private final Context context = new SystemContext();

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
				Actor as = context.newReference(s.name().toString(), s);
				actors.add(as);
				sieves.add(s);
			} else {
				Sieve s = new Sieve(3, size, i, modulo);
				Actor as = context.newReference(s.name().toString(), s);
				actors.add(as);
				sieves.add(s);
			}
		}

	}

	public void run_par() {
		//System.out.println(actors.size());
		//System.out.println(sieves.size());
		Set<Future<?>> futures = new HashSet<>();
		for (Actor s : actors) {
					//s.init(true);
                                        Future<Object> r = invoke(s, "init", new Boolean(true));
                                        futures.add(r);
                                }
		this.init(true);
//		System.out.println(futures);
//		System.out.println(actors);

		futures.forEach(f -> {
                        try {
                                f.get();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
		futures.clear();
//		System.out.println("All arrays initialized");
		for (int i = 0; i < currentList.length; ++i) {
			
			int prime = i * 2 + offset;
			//System.out.print(prime+ " ");
			if (prime * prime > target * 2)
				break;
			if (currentList[i]) {
				for (Actor s : actors) {
					Future<Object> r = invoke(s, "sieve", new Integer(prime));
					futures.add(r);
				}
				this.sieve(prime);
			}
		}
		futures.forEach(f -> {
			try {
				f.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public Integer collect() {
//		System.out.println(Arrays.asList(currentList));
		int sum =Collections.frequency(Arrays.asList(currentList), true);
		for (Sieve s : sieves) {
			 sum+= s.collect();
		}
		
		return sum;
	}

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
		//System.out.println("Computation of" + (prime.collect()+1) +" primes ");
	}
}
