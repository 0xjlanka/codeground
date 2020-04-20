#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <sys/syscall.h>
#include <sys/user.h>
#include <sys/reg.h>
#include <stdio.h>
#include <stdlib.h>

#if __WORDSIZE == 64
#define GET_AX(reg) reg.orig_rax
#define SET_AX(reg) reg.rax
#define SYSCALL     SYS_read
#else
#define GET_AX(reg) reg.orig_eax
#define SET_AX(reg) reg.eax
#define SYSCALL     SYS_read
#endif

void dump_regs(struct user_regs_struct regs)
{
    printf("ORIG_AX:%llX\n", regs.orig_rax);
    printf("AX:%llX\n", regs.rax);
    printf("BX:%llX\n", regs.rbx);
    printf("CX:%llX\n", regs.rcx);
    printf("DX:%llX\n", regs.rdx);
    printf("DI:%llX\n", regs.rdi);
    printf("SI:%llX\n", regs.rsi);
    printf("BP:%llX\n", regs.rbp);
    printf("IP:%llX\n", regs.rip);
}

int main(int argc, char* argv[])
{
    pid_t pid;
    struct user_regs_struct regs;
    if (argc == 1) {
        printf("Usage: code_inject <exe>\n");
        exit(0);
    }
    pid = fork();
    if (pid == 0) {
        ptrace(PTRACE_TRACEME, 0, NULL, NULL);
        // Trace the argument
        execvp(argv[1], &argv[1]);
    }
    else {
        int status;
        // Wait till child runs first instruction
        wait(&status);
        ptrace(PTRACE_GETREGS, pid, NULL, &regs);
        // The address we are interested in:
        // This is the address of the instruction that sets the variable i
        // to 0xA5A5A5A5. 
        // Run objdump -S ../test/simple.c to get the address of assignment.
        unsigned long addr = 0x40055A;
        // The op-code for the instruction
        unsigned long data = ptrace(PTRACE_PEEKTEXT, pid, (void*)addr, 0);
        // Write trap instruction into the address 'INT 3' -> 0xCC
        unsigned long data_with_trap = (data & 0xFFFFFFFFFFFFFF00) | 0xCC;
        // Replace the current instruction with a trap
        ptrace(PTRACE_POKETEXT, pid, (void*)addr, (void*)data_with_trap);
        // Continue running the code
        ptrace(PTRACE_CONT, pid, 0, 0);
        // Wait for event in the child
        wait(&status);
        if (!WIFSTOPPED(status)) {
            printf("Error\n");
            return -1;
        }
        // Should stop at the address+1 set above, verify by reading
        // regs.rip
        ptrace(PTRACE_GETREGS, pid, NULL, &regs);
        // Print and observe the instruction op code
        // check where the data field is.
        // In this case it is the bytes 2,3,4 and 5
        // Replace them with required data
        data &= 0xFF00000000FFFFFF;
        data |= 0x00DEADBEEF000000;
        // Rewind to previous instruction
        regs.rip -= 1;
        ptrace(PTRACE_SETREGS, pid, NULL, &regs);
        // Re-set the instruction code with altered data and removed trap
        ptrace(PTRACE_POKETEXT, pid, (void*)addr, (void*)data);
        // Continue running
        ptrace(PTRACE_CONT, pid, NULL, NULL);
        wait(&status);
    }
    return 0;
}
