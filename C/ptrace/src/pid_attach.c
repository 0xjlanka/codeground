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
#define GET_REG(reg)    reg.orig_rax
#define SET_REG(reg)    reg.rax
#define SYSCALL         SYS_getuid
#else
#define GET_REG(reg)    reg.orig_eax
#define SET_REG(reg)    reg.eax
#define SYSCALL         SYS_getuid32
#endif

int main(int argc, char* argv[])
{
    pid_t pid;
    struct user_regs_struct regs;
    long ins;
    if (argc == 1) {
        printf("Usage: pid_attach <pid>\n");
        exit(0);
    }
    pid = atoi(argv[1]);
    if (!pid) {
        printf("Usage: pid_attach <pid>\n");
        exit(0);
    }
    if (ptrace(PTRACE_ATTACH, pid, NULL, NULL) < 0) {
        printf("Error attaching to %d: %s\n", pid, strerror(errno));
        exit(0);
    }
    wait(NULL);
    ptrace(PTRACE_GETREGS, pid, 0, &regs);
    ins = ptrace(PTRACE_PEEKTEXT, pid, regs.rip, NULL);
    printf("EIP:%llx Instruction:%lx\n",regs.rip, ins);
    ptrace(PTRACE_DETACH, pid, NULL, NULL);
    return 0;
}
