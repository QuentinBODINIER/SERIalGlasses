/**
 * piGlasses : programme de démo des piGlasses
 */


/* ======================================================================
						INCLUDE SECTION
====================================================================== */

#include <getopt.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
//#include <iostream>       // std::cout
#include <thread>         // std::thread

#include "ArduiPi_SSD1306.h"
#include "Adafruit_GFX.h"
#include "Adafruit_SSD1306.h"
 
#include <wiringPi.h>
#include <wiringSerial.h>

#include "bitmaps.h"

#include "mailbox.h"

/* ======================================================================
						DEFINE SECTION
====================================================================== */
#define NUMFLAKES 10
#define XPOS 0
#define YPOS 1
#define DELTAY 2

#define LOGO16_GLCD_HEIGHT 16
#define LOGO16_GLCD_WIDTH  16

#define WORK_SIZE 1024
#define MBSIZE 32

/* ======================================================================
						GLOBAL VARIABLES
====================================================================== */

// Instantiate the display
Adafruit_SSD1306 display;

/* ======================================================================
						GRAPHICAL PRIMITIVES
====================================================================== */

void testdrawbitmap(const uint8_t *bitmap, uint8_t w, uint8_t h) {
	uint8_t icons[NUMFLAKES][3];
	srandom(666);     // whatever seed

	// initialize
	for (uint8_t f=0; f< NUMFLAKES; f++) {
		icons[f][XPOS] = random() % display.width();
		icons[f][YPOS] = 0;
		icons[f][DELTAY] = random() % 5 + 1;

		printf("x: %d", icons[f][XPOS]);
		printf("y: %d", icons[f][YPOS]);
		printf("dy: %d\n", icons[f][DELTAY]);
	}

	while (1) {
		// draw each icon
		for (uint8_t f=0; f< NUMFLAKES; f++) {
			display.drawBitmap(icons[f][XPOS], icons[f][YPOS], logo16_glcd_bmp, w, h, WHITE);
		}
		display.display();
		usleep(100000);

		// then erase it + move it
		for (uint8_t f=0; f< NUMFLAKES; f++) {
			display.drawBitmap(icons[f][XPOS], icons[f][YPOS],  logo16_glcd_bmp, w, h, BLACK);
			// move it
			icons[f][YPOS] += icons[f][DELTAY];
			// if its gone, reinit
			if (icons[f][YPOS] > display.height()) {
				icons[f][XPOS] = random() % display.width();
				icons[f][YPOS] = 0;
				icons[f][DELTAY] = random() % 5 + 1;
			}
		}
	}
}

void testdrawchar(void) {
	display.setTextSize(1);
	display.setTextColor(WHITE);
	display.setCursor(0,0);

	for (uint8_t i=0; i < 168; i++) {
		if (i == '\n') continue;
		display.write(i);
		if ((i > 0) && (i % 21 == 0))
			display.print("\n");
	}
	display.display();
}

void printchar(uint8_t c, uint8_t size){
display.setTextSize(size);
display.setTextColor(WHITE);
display.write(c);
display.display();
}

/*
======================================================================
							MAILBOX
======================================================================
*/
uint32_t mbBuffer[MBSIZE];
Mailbox mb(mbBuffer, MBSIZE);

/* ======================================================================
						THREAD SECTION
====================================================================== */



void fSerial(){

int fd ;
unsigned int nextTime ;
int pchar;

	printf("Init serial...");
	if ((fd = serialOpen ("/dev/ttyAMA0", 115200)) < 0)
	{
		fprintf (stderr, "Unable to open serial device: %s\n", strerror (errno)) ;
	}
	else
	{
		if (wiringPiSetup () == -1)
		{
		fprintf (stdout, "Unable to start wiringPi: %s\n", strerror (errno)) ;
		}
		else{
		printf("OK\n");
		// INIT OK, THREAD OPERATION FOLLOWS
		while(1){
			while (serialDataAvail (fd)){
				pchar=serialGetchar (fd);
				printf (" -> %3d", pchar);
				if(pchar!=10){
					mb.post((uint32_t)pchar);
				}
				else{
					//curY+=3;
				}
				fflush (stdout) ;
			}
		}
		//THREAD TERMINATING
		}
	}
	

}

void fScreen(uint8_t param){
// Config Option
struct s_opts
{
	int oled;
	int verbose;
} ;

// default options values
s_opts opts = {
	OLED_ADAFRUIT_I2C_128x32,	// Default oled
	false										// Not verbose
};

uint32_t msg=0;

	// INIT display
	printf("Init screen...");
	if ( !display.init(OLED_I2C_RESET,opts.oled) )
			exit(EXIT_FAILURE);
	display.begin();
	printf("OK\n");
	
	display.clearDisplay();   // clears the screen and buffer
	display.display();
	//display.drawBitmap(0, 0, logoPG , 128, 32, 1);
	display.display();
	
	while(1){
		mb.fetch(&msg);
		printchar(msg,1);
	}
	
}

/* ======================================================================
Function: main
Purpose : Main entry Point
Input 	: -
Output	: -
Comments:
====================================================================== */
int main()
{
	int i;
	int count ;
	uint8_t curX,curY;
	
	/**
	*	DECLARATION des THREADS
	*/
	std::thread thdSerial (fSerial);     // spawn new thread that calls fserial()
	std::thread thdScreen (fScreen,0);  // spawn new thread that calls fScreen(0)
	
	
	while(1){
		sleep(100);
	}
	
	  return 0;

}


