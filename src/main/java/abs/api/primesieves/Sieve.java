package abs.api.primesieves;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.BitSet;
import java.util.Collections;
import abs.api.Actor;
import abs.api.Reference;
import java.util.Arrays;

/**
 * @author Vlad Nicolae Serbanescu
 * @author Behrooz Nobakht
 */
public class Sieve implements Actor {

	private static final long serialVersionUID = 1L;

	protected BitSet currentList;
	protected int offset = 0, last, size=0;

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
		this.size=size;
		 //System.out.print(name() + "----");
//		 System.out.println(size + " " + modulo + ": "+ offset + " "+ last +" ");
		// for (int i = 0; i < currentList.size(); i++) {
		// System.out.print(i * 2 + offset + " ");
		// }
		// System.out.println();

	}
	public boolean init(Boolean b){
		currentList = new BitSet(size);
                System.out.println(size + " : "+ offset + " "+ last +" "+ currentList.size());
                /*for (int i = 0; i < size; i++) {
                        currentList[i]=true;
                }*/
		return true;
	}

	public Boolean sieve(Integer n) {
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
		
		//System.out.println(j +" "+(last-j)+ " "+ (2*n));
		if(j<=last){
		for(;j>0&(last-j)>=(2*n);j+=2*n){
		//	System.out.println((j - offset) / 2);
			currentList.set((j - offset) / 2);	
			//currentList[(j - offset) / 2] = false;
			
		}
				
		currentList.set((j - offset) / 2);	
		}
		return true;
	}

	public Integer collect() {
//		System.out.println(Arrays.asList(currentList));
		Integer sum = size- currentList.cardinality();
		//System.out.println(currentList);
		System.out.println(size+" "+currentList.cardinality()+" "+sum);
		return sum;
	}

	/**
	 * Show.
	 */
	public void show() {
		for (int i = 0; i < currentList.size(); i++) {
			if (!currentList.get(i))
				System.out.print(i * 2 + offset + " ");
		}
		System.out.println();
	}

}
