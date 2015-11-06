
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#include "mailbox.h"

#define WORK_SIZE 1024
#define	MBSIZE	32

void *thread_function(void *arg);

char work_area[WORK_SIZE];

uint32_t 	mbBuffer[MBSIZE];

Mailbox mb(mbBuffer,MBSIZE);



int main(){
	
	
	pthread_t a_thread;
	int res=0;

	res = pthread_create(&a_thread, NULL, thread_function, NULL);
	if (res != 0) {
		perror("Thread creation failed");
		exit(EXIT_FAILURE);
	}
	while(1){
		printf("trying to post...");
		mb.post((uint32_t)120);
		printf("OK\r\n");
	}


	return 0;
}

void *thread_function(void *arg) {
    uint32_t msg=0;
    while(1){
        printf("trying to fetch...");
        mb.fetch(&msg);
		printf("OK got %d \r\n",msg);
    }
    pthread_exit(NULL);
}  