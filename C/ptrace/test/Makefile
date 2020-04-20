CFLAGS = -Wall
LDFLAGS = 

SRC = $(wildcard *.c)
OBJ = $(SRC:.c=.o)

%.o: %.c
	@echo "[CC] $<"
	@gcc $(CFLAGS) -o $@ $< $(LDFLAGS)


all: $(OBJ)

clean:
	@rm -fr *.o
