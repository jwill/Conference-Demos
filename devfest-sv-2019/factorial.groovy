import groovy.transform.CompileStatic
import groovy.transform.TailRecursive

@CompileStatic
class Factorial {
    @TailRecursive
    static BigInteger factorial( BigInteger i, BigInteger product = 1) {
        if( i == 1) { return product }
        return factorial(i-1, product*i)
    }
}

def fact =  Factorial.factorial(50_000).toString()
println fact
println fact.size() // Big number, 213237 digits and no stack overflow
