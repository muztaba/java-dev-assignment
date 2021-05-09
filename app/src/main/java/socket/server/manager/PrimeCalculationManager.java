package socket.server.manager;

public class PrimeCalculationManager {
    // findPrimes finds the number of prime number inside given input n in most brute-force way (intentionally. don't optimize)
    // this method will take different execution time based on given input

    public int findPrimes(@ParamName(name = "n") int n) {
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i))
                count++;
        }
        return count;
    }

    private boolean isPrime(int n) {
        if (n <= 1)
            return false;
        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;
        return true;
    }
}
