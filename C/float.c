#include <stdio.h>

unsigned int calc_temp(unsigned int fval)
{
	unsigned int temp, term2, term3;
	fval += 2600;
	term2 = (169458 * fval);
	term3 = (((3 * fval) - 9248) * ((3 * fval) - 9248));

	temp = 575007350 - term2 - term3;
	printf("%d\n", (temp));
	return (temp / 100) * 100;
}
int main()
{
	int t1,t2, sl, intc, temp;
	unsigned int counter = 2505399;

	t1 = calc_temp(631);
	printf("Room Temp: %d\n", t1);
	t2 = calc_temp(274);
	printf("Hot Temp: %d\n", t2);

	sl = (int)(t1 - t2) / (16090 - 12293);
	printf("Slope: %d\n", sl);
	intc = (int)(t1 - (16090 * sl));
	printf("Intercept: %d\n", intc);
	temp = (int)(15591 * sl) + intc;
	printf("Final Temp: %d\n", temp/10000);
	t1 = (counter * 56/8999);
	printf("counter:%d\n", t1);
	t1 = (counter/161);
	printf("counter:%d\n", t1);
	return 0;
}
