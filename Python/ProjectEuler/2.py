import pelib

print(sum(filter(lambda x: (x%2 == 0), pelib.fib(1,2,4000000))))
