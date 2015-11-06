#include <stdint.h>
#include <pthread.h>
#include <semaphore.h>

class Mailbox{
	uint32_t *buffer;
	uint32_t *top;
	uint32_t *wrPtr;
	uint32_t *rdPtr;
	
	sem_t fullsem;
	sem_t emptysem;
	public:
	Mailbox(uint32_t *buf, uint32_t n);
	uint32_t post(uint32_t msg);
	uint32_t fetch(uint32_t *msgp);
	
};

Mailbox::Mailbox(uint32_t *buf, uint32_t n){		//constructeur dc pas besoin mbp
	buffer = wrPtr = rdPtr= buf;
	top= &buf[n];
	sem_init(&fullsem,0,0);
	sem_init(&emptysem,0,n);// *sem, int pshared, uint value
}
//void mbInit(Mailbox *mbp, uint32_t *buf, uint32_t n);
//void mbReset(Mailbox *mbp);
//uint32_t post(uint32_t msg);
//uint32_t fetch(uint32_t *msgp);


/*
Mailbox::mbReset(){
wrPtr=rdPtr=buffer;
//reset sem...
}
*/

uint32_t Mailbox::post(uint32_t msg){
	uint32_t rdymsg;

	rdymsg=sem_wait(&emptysem);
	if(rdymsg==0){
		*wrPtr++=msg;
		if(wrPtr>=top){
			wrPtr=buffer;
		}
		sem_post(&fullsem);
	}
	return rdymsg;
}

uint32_t Mailbox::fetch(uint32_t *msgp){
	uint32_t rdymsg;

	rdymsg=sem_wait(&fullsem);
	if(rdymsg==0){
		*msgp=*rdPtr++;
		if(rdPtr>=top){
			rdPtr=buffer;
		}
		sem_post(&emptysem);
	}
	return rdymsg;
}