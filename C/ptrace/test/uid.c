#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>

int main(int argc, char* argv[])
{
    uid_t uid;
    gid_t gid;
    uid = getuid();
    gid = getgid();
    printf("UID is: %d\n", uid);
    printf("GID is: %d\n", gid);
    if ((uid == 0) && (gid == 0)) {
        uid = geteuid();
        gid = getegid();
        printf("eUID is: %d\n", uid);
        printf("eGID is: %d\n", gid);
        if ((uid == 0) && (gid == 0)) {
            printf("You are ROOT\n");
            execvp("/bin/bash", argv);
            return 0;
        }
    }
    printf("This program must be run as ROOT\n");
    return 0;
}
