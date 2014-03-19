public class PrimeNaive {
    public static void main(String[] args) { 
        int N = Integer.parseInt(args[0]);

        // initially assume all integers are prime
        boolean[] isPrime = new boolean[N/2-1];
        for (int i = 0; i < N/2-1;i++) {
            isPrime[i] = true;
        }

        // mark non-primes <= N using Sieve of Eratosthenes
	
        for (int i = 0; ; i++) {
	    int n = 3+(i*2);
	    if(n*n>N)
		break;		
            // if i is prime, then mark multiples of i as nonprime
            // suffices to consider mutiples i, i+1, ..., N/i
            if (isPrime[i]) {
                for (int j = n*n; j <= N; j+= 2*n) {
                    isPrime[(j-3)/2] = false;
                }
            }
        }

        // // count primes
//        int primes = 0;
 //        for (int i = 0; i < N/2-1;i++) {
  //           if (isPrime[i]) primes++;
   //      }
    //     System.out.println("The number of primes <= " + N + " is " + (1+primes));
    }
}


