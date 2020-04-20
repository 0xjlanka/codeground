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
#define SYSCALL1     SYS_getuid
#define SYSCALL2     SYS_getgid
#define SYSCALL3     SYS_geteuid
#define SYSCALL4     SYS_getegid
#else
#define GET_AX(reg) reg.orig_eax
#define SET_AX(reg) reg.eax
#define SYSCALL1     SYS_getuid32
#define SYSCALL2     SYS_getgid32
#define SYSCALL3     SYS_geteuid32
#define SYSCALL4     SYS_getegid32
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
    pid_t child;
    int enter=1;
    if (argc == 1) {
        printf("Usage: id_injector <exe>\n");
        exit(0);
    }
    child = fork();
    if (child == 0) {
        ptrace(PTRACE_TRACEME, 0, NULL, NULL);
        // Trace the argument
        execvp(argv[1], &argv[1]);
    }
    else {
        int status;
        struct user_regs_struct regs;
        wait(&status);
        while (1) {
            // trace all syscalls of child
            ptrace(PTRACE_SYSCALL, child, 0, 0);
            wait(&status);
            if (WIFEXITED(status))
                break;
            // Get the current syscall details. AX contains the syscall number
            // For each syscall, the following code executes twice, once on entry,
            // once on exit.
            // We are interested in exit.
            ptrace(PTRACE_GETREGS, child, 0, &regs);
            if ((GET_AX(regs) == SYSCALL1) || (GET_AX(regs) == SYSCALL2) ||
                (GET_AX(regs) == SYSCALL3) || (GET_AX(regs) == SYSCALL4)) {
                if (enter == 1) {
                    enter = 0;
                }
                else {
                    // syscall exit, AX contains the restult
                    // change it to 0.
                    ptrace(PTRACE_GETREGS, child, 0, &regs);
                    SET_AX(regs) = 0;
                    ptrace(PTRACE_SETREGS, child, 0, &regs);
                    enter = 1;
                }
            }
        }
    }
    return 0;
}
