package abs.api.primesieves;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

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

	public void run() {
		Set<Future<?>> futures = new HashSet<>();
		for (int i = 0; i < currentList.size(); ++i) {
			int prime = i * 2 + offset;
			if (prime * prime > target * 2)
				break;
			if (currentList.get(i)) {
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
	public List<Integer> collect() {
		List<Integer> result = new ArrayList<>();
		for (Sieve s : sieves) {
			result.addAll(s.collect());
		}
		Collections.sort(result);
		return result;
	}

	public static void main(String[] args) {
		Integer n = 10000;
		Boolean print = false;
		if (args.length != 0) {
			n = Integer.parseInt(args[0]);
			if (args.length > 1) {
				print = Boolean.parseBoolean(args[1]);
			}
		}
		Generator prime = new Generator(n, Runtime.getRuntime()
				.availableProcessors() / 2);
		final long start = System.currentTimeMillis();
		prime.run();
		final long end = System.currentTimeMillis();
		List<Integer> result = prime.collect();
		if (print) {
			System.out.println(result);
		}
		long duration = end - start;
		System.out.println("Computation of " + result.size() + " primes took: "
				+ Duration.ofMillis(duration));
	}
}
