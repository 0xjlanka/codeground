/*
 * Simple code injection
 * The output should be "Gotcha"
 */
#include <stdio.h>

int main()
{
    unsigned int i=0xA5A5A5A5;
    printf("val:%X\n", i);
    if (i == 0xDEADBEEF)
        printf("Gotcha\n");
    else
        printf("Nope\n");
    return 0;
}
