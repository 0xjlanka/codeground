import math

def fib(a, b, e):
    if (a > e):
        return
    yield(a)
    yield from fib(b, a+b, e)

def isPrime(n):
    if (n <= 1):
        return False
    if (n <= 11 and n in [2, 3, 5, 7, 11]) :
        return True
    if ((n%2) == 0):
        return False
    return not (0 in list(map(lambda x:(n%x), range(3,int(math.sqrt(n))+1, 2))))

def getFactors(n):
    f = set()
    i = n
    j = 1
    while (j < i):
        if ((n % j) == 0):
            i = max(j, int(n/j))
            f.add(j)
            f.add(int(n/j))
        j += 1
    return sorted(f)

