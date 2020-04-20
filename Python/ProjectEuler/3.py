import pelib

print(max(list(filter(pelib.isPrime, pelib.getFactors(600851475143)))))
