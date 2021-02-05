CFLAGS = $(shell python3m-config --cflags) -fPIC
LFLAGS = $(shell python3m-config --ldflags)

all:
	gcc test.c -o test.o $(CFLAGS) $(LFLAGS)

clean:
	rm -fr call.o __pycache__ test.o
