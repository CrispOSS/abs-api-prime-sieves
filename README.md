# abs-api-prime-sieves

# Requirements

To be able to build and run the code, you need to have:

1. [JDK 1.8][2]
2. [Apache Maven 3.2+][3]

# Quick Start

If you do not want to look into details of how to build the source and run the example, 
use the script `run-demo.sh` with the following parameters:

```
$ cd abs-api-prime-sieves
$ ./run-demo.sh N M
N = The target to calculate primes up to
M = The number of parallel processes to use
```

# Build source

The Prime Sieves uses [ABS API][4] which means that either you should have it in a local Maven repository 
or build it from source.
To completely, build the source for Prime Sieves:

1. Build ABS API

```bash
$ cd ..
$ git clone https://github.com/CrispOSS/abs-api
$ cd abs-api
$ mvn clean install
```

2. Build Prime Sieves

```bash
$ cd abs-api-prime-sieves
$ mvn clean install
```

Prime Sieves using ABS API is the implementation of [Prime Sieves][1] using ABS API.

To run the code:

1. Configure `run.sh` for the target number 
2. Execute `run.sh`

[1]: http://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
[2]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[3]: http://maven.apache.org/download.cgi
[4]: https://github.com/CrispOSS/abs-api
