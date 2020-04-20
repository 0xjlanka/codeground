/*
  # MIT License
  #
  # Copyright (c) 2020 Jitendra Lanka - https://github.com/0xjlanka
  #
  # Permission is hereby granted, free of charge, to any person obtaining a copy
  # of this software and associated documentation files (the "Software"), to deal
  # in the Software without restriction, including without limitation the rights
  # to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  # copies of the Software, and to permit persons to whom the Software is
  # furnished to do so, subject to the following conditions:

  # The above copyright notice and this permission notice shall be included in all
  # copies or substantial portions of the Software.

  # THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  # IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  # FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  # AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  # LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  # OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  # SOFTWARE.
*/

#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <poll.h>

#define TIMEOUT_MS  1000

int sockfd[2];

/*
 * Returns:
 * 0 on timeout
 * >0 on success
 * <0 on failure
 */
int recv_poll(int fd, void *data, int len)
{
    struct pollfd pfd;
    int ret, status;

    pfd.fd = fd;
    pfd.events = POLLIN;
    pfd.revents = 0;

    status = poll(&pfd, 1, TIMEOUT_MS);

    if (status > 0)
    {
        if (pfd.revents & POLLIN)
        {
            ret = recv(fd, data, len, 0);
            return ret;
        }
    }
    else if (status == 0)
    {
        // None of the fd are ready and timeout happened
        return 0;
    }
    return -errno;
}

void *tfn_tid0(void *arg)
{
    int c;
    int ret;
    do
    {
        printf("T0: waiting...\n");
        ret = recv_poll(sockfd[0], &c, sizeof(int));
        if (ret == 0)
        {
            //make sure c is > 0 for loop to continue
            c = 1;
            continue;
        }
        printf("T0: Got %d, throwing to T1 ... ", c);
        --c;
        send(sockfd[0], &c, sizeof(int), 0);
        printf("done\n");
    } while(c > 0);
    return NULL;
}

void *tfn_tid1(void *arg)
{
    int c;
    int ret;
    do
    {
        printf("T1: waiting...\n");
        ret = recv_poll(sockfd[1], &c, sizeof(int));
        if (ret == 0)
        {
            //make sure c is > 0 for loop to continue
            c = 1;
            continue;
        }
        printf("T1: Got %d, throwing to T0 ... ", c);
        --c;
        send(sockfd[1], &c, sizeof(int), 0);
        printf("done\n");
    } while(c > 0);
    return NULL;
}
int main()
{
    int times=10;
    int ret;
    pthread_t tid[2];
    ret = socketpair(AF_UNIX, SOCK_STREAM, 0, sockfd);
    if (ret < 0)
    {
        printf("Unable to create sockpair: %s\n", strerror(errno));
        return -errno;
    }
    ret = pthread_create(&tid[0], NULL, tfn_tid0, NULL);
    if (ret < 0)
    {
        printf("Unable to create thread 0: %s\n", strerror(errno));
        return -errno;
    }
    ret = pthread_create(&tid[1], NULL, tfn_tid1, NULL);
    if (ret < 0)
    {
        printf("Unable to create thread 1: %s\n", strerror(errno));
        return -errno;
    }
    sleep(5);
    printf("Tossing ball to Thread 0...\n");
    ret = send(sockfd[1], &times, sizeof(int), 0);
    if (ret < 0)
    {
        printf("Unable to send data to thread 0: %s\n", strerror(errno));
        return -errno;
    }
    pthread_join(tid[0], NULL);
    printf("Thread 0 done playing\n");
    pthread_join(tid[1], NULL);
    printf("Thread 1 done playing\n");
    close(sockfd[0]);
    close(sockfd[1]);
    return 0;
}
