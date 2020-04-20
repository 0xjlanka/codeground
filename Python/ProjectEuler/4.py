print(max([(x*y) for x in range(100,1000) for y in range(100, 1000) if((x*y) == int(str(x*y)[::-1]))]))
