CFLAGS = -Wall
LDFLAGS = 

LIB = myLib.so

all: $(LIB)

clean:
	@rm -fr myLib.o
	@rm -fr myLib.so

myLib.so: myLib.c
	@echo "[CC] myLib.c"
	@gcc $(CFLAGS) -fPIC -c myLib.c -o myLib.o
	@echo "[LD] myLib.so"
	@gcc -shared -o myLib.so myLib.o
