/**
 * piGlasses : programme de démo des piGlasses
 */


/**
 * INCLUDES
 *
 * Includes all included includes...
 *
 */

#include "ArduiPi_SSD1306.h"
#include "Adafruit_GFX.h"
#include "Adafruit_SSD1306.h"

#include <getopt.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <wiringPi.h>
#include <wiringSerial.h>

#include "bitmaps.h"

// Instantiate the display
Adafruit_SSD1306 display;

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

#define NUMFLAKES 10
#define XPOS 0
#define YPOS 1
#define DELTAY 2

#define LOGO16_GLCD_HEIGHT 16
#define LOGO16_GLCD_WIDTH  16


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

void testdrawcircle(void) {
	for (int16_t i=0; i<display.height(); i+=2) {
		display.drawCircle(display.width()/2, display.height()/2, i, WHITE);
		display.display();
	}
}

void testfillrect(void) {
	uint8_t color = 1;
	for (int16_t i=0; i<display.height()/2; i+=3) {
		// alternate colors
		display.fillRect(i, i, display.width()-i*2, display.height()-i*2, color%2);
		display.display();
		color++;
	}
}

void testdrawtriangle(void) {
	for (int16_t i=0; i<min(display.width(),display.height())/2; i+=5) {
		display.drawTriangle(display.width()/2, display.height()/2-i,
				display.width()/2-i, display.height()/2+i,
				display.width()/2+i, display.height()/2+i, WHITE);
		display.display();
	}
}

void testfilltriangle(void) {
	uint8_t color = WHITE;
	for (int16_t i=min(display.width(),display.height())/2; i>0; i-=5) {
		display.fillTriangle(display.width()/2, display.height()/2-i,
				display.width()/2-i, display.height()/2+i,
				display.width()/2+i, display.height()/2+i, WHITE);
		if (color == WHITE) color = BLACK;
		else color = WHITE;
		display.display();
	}
}

void testdrawroundrect(void) {
	for (int16_t i=0; i<display.height()/2-2; i+=2) {
		display.drawRoundRect(i, i, display.width()-2*i, display.height()-2*i, display.height()/4, WHITE);
		display.display();
	}
}

void testfillroundrect(void) {
	uint8_t color = WHITE;
	for (int16_t i=0; i<display.height()/2-2; i+=2) {
		display.fillRoundRect(i, i, display.width()-2*i, display.height()-2*i, display.height()/4, color);
		if (color == WHITE) color = BLACK;
		else color = WHITE;
		display.display();
	}
}

void testdrawrect(void) {
	for (int16_t i=0; i<display.height()/2; i+=2) {
		display.drawRect(i, i, display.width()-2*i, display.height()-2*i, WHITE);
		display.display();
	}
}

void testdrawline() {
	for (int16_t i=0; i<display.width(); i+=4) {
		display.drawLine(0, 0, i, display.height()-1, WHITE);
		display.display();
	}
	for (int16_t i=0; i<display.height(); i+=4) {
		display.drawLine(0, 0, display.width()-1, i, WHITE);
		display.display();
	}
	usleep(250000);

	display.clearDisplay();
	for (int16_t i=0; i<display.width(); i+=4) {
		display.drawLine(0, display.height()-1, i, 0, WHITE);
		display.display();
	}
	for (int16_t i=display.height()-1; i>=0; i-=4) {
		display.drawLine(0, display.height()-1, display.width()-1, i, WHITE);
		display.display();
	}
	usleep(250000);

	display.clearDisplay();
	for (int16_t i=display.width()-1; i>=0; i-=4) {
		display.drawLine(display.width()-1, display.height()-1, i, 0, WHITE);
		display.display();
	}
	for (int16_t i=display.height()-1; i>=0; i-=4) {
		display.drawLine(display.width()-1, display.height()-1, 0, i, WHITE);
		display.display();
	}
	usleep(250000);

	display.clearDisplay();
	for (int16_t i=0; i<display.height(); i+=4) {
		display.drawLine(display.width()-1, 0, 0, i, WHITE);
		display.display();
	}
	for (int16_t i=0; i<display.width(); i+=4) {
		display.drawLine(display.width()-1, 0, i, display.height()-1, WHITE);
		display.display();
	}
	usleep(250000);
}

void testscrolltext(void) {
	display.setTextSize(2);
	display.setTextColor(WHITE);
	display.setCursor(10,0);
	display.clearDisplay();
	display.print("scroll");
	display.display();

	display.startscrollright(0x00, 0x0F);
	sleep(2);
	display.stopscroll();
	sleep(1);
	display.startscrollleft(0x00, 0x0F);
	sleep(2);
	display.stopscroll();
	sleep(1);
	display.startscrolldiagright(0x00, 0x07);
	sleep(2);
	display.startscrolldiagleft(0x00, 0x07);
	sleep(2);
	display.stopscroll();
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
	int fd ;
	int count ;
	unsigned int nextTime ;
	int pchar;
	uint8_t curX,curY;
	


	/**
	*	INIT screen
	*
	*/
	printf("Init screen...");
	if ( !display.init(OLED_I2C_RESET,opts.oled) )
			exit(EXIT_FAILURE);
	display.begin();
	printf("OK\n");
	
	printf("Init serial...");
	if ((fd = serialOpen ("/dev/ttyAMA0", 115200)) < 0)
	{
		fprintf (stderr, "Unable to open serial device: %s\n", strerror (errno)) ;
		return 1 ;
	}

	if (wiringPiSetup () == -1)
	{
		fprintf (stdout, "Unable to start wiringPi: %s\n", strerror (errno)) ;
		return 1 ;
	}
	printf("OK\n");

	nextTime = millis () + 300 ;

	// init done
	display.clearDisplay();   // clears the screen and buffer
	display.display();

	// miniature bitmap display
	//display.clearDisplay();
	//display.drawBitmap(0, 0, logoPG , 128, 32, 1);
	//display.display();
	//sleep(10);

	// invert the display
	curY=0;
	curX=0;
	display.setCursor(curX,curY);
	display.clearDisplay(); 
	while(1){
		
		
		while (serialDataAvail (fd))
		{
			pchar=serialGetchar (fd);
			printf (" -> %3d", pchar) ;
			if(pchar!=10){
				printchar(pchar, 2);
			}
			else{
			curY+=3;
			}
			fflush (stdout) ;
		}
	sleep(1);


	}



	// Free PI GPIO ports
	display.close();

}


