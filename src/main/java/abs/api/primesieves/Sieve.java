package abs.api.primesieves;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import abs.api.Actor;
import abs.api.Reference;

/**
 * @author Vlad Nicolae Serbanescu
 * @author Behrooz Nobakht
 */
public class Sieve implements Actor {

	private static final long serialVersionUID = 1L;

	protected ArrayList<Boolean> currentList;
	protected int offset = 0, last;

	@Override
	public URI name() {
		try {
			return new URI("Sieve-" + offset);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(Reference r) {
		if (r instanceof Sieve)
			return this.offset - ((Sieve) r).offset;
		return 0;
	}

	public Sieve(int first_prime, int size, int pe, int modulo) {
		if (pe < modulo) {
			offset = first_prime + pe * size * 2;
		} else {
			offset = first_prime + ((pe - modulo) * size * 2)
					+ (modulo * (size + 1) * 2);
		}
		last = offset + size * 2 - 2;
		currentList = new ArrayList<Boolean>(size);
		for (int i = 0; i < size; i++) {
			currentList.add(true);
		}
		// System.out.println(name());
		// System.out.print(size + " " + modulo + ": ");
		// for (int i = 0; i < currentList.size(); i++) {
		// System.out.print(i * 2 + offset + " ");
		// }
		// System.out.println();

	}

	public boolean sieve(Integer the_prime) {
		int n = the_prime;
		int first = n * n;
		int j = 0;
		if (offset <= first) {
			j = first;
		} else {
			if (offset % n == 0) {
				j = offset;
			} else {
				j = offset / n;
				if (j % 2 == 0) {
					j = j * n + n;
				} else {
					j = j * n + 2 * n;
				}
			}
		}

		while (j <= last) {
			currentList.set((j - offset) / 2, false);
			j += (2 * n);
		}

		return true;
	}

	public List<Integer> collect() {
		List<Integer> primes = new ArrayList<>();
		for (int i = 0; i < currentList.size(); ++i) {
			if (currentList.get(i)) {
				primes.add(i * 2 + offset);
			}
		}
		return primes;
	}

	public void show() {
		for (int i = 0; i < currentList.size(); i++) {
			if (currentList.get(i))
				System.out.print(i * 2 + offset + " ");
		}
		System.out.println();
	}

}
