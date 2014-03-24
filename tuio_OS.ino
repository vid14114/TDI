/* =====================================================================================
 * template for ACTOframework TUIO Extension
 * =====================================================================================
 * template for developing code for TUIO Extension
 *
 * see also ACTOControl.h or ACTO_framework for protocol
 *
 * date: 13. Feb 2014
 * version: v1.1
 *
 * project: ACTO
 * author: Emanuel Vonach
 * Interactive Media Systems Group, Vienna University of Technology, Austria
 * www.ims.tuwien.ac.at
 * Contact: vonach@ims.tuwien.ac.at, gerstweiler@ims.tuwien.ac.at
 * =====================================================================================
 */

#include "ACTOControl.h"

///////////////////////////////////////////////////////////////////////////////////////
// ACTO SETTINGS to be set by TUIO DEVELOPER (do not remove!)
///////////////////////////////////////////////////////////////////////////////////////
const byte tuioId = '1';
#define MOTOR_DEFAULT                // choose MOTOR_##: ALICE, BOB, CHARLIE, DAVE, ERIN, FRANK, -- MOTOR_DEFAULT (default if you are not certain)

//define pins of extension units, which should be updated and addressable by ACTO_framework
const int digitalInputPins[] = {};//{2};
const int digitalOutputPins[] = {2,4,5};//{5};
const int analogInputPins[] = {};//{A2};

//LED for error output, if available, comment out if not
//#define STATUSLED A0

//define ID range of data arrays addressable by ACTO_framework (access via setByteData(byte id, byte val) & getByteData(byte id)...)
const int sizeOfByteData = 0;
const int sizeOfCharData = 0;
const int sizeOfIntData = 0;
const int sizeOfFloatData = 0;

///////////////////////////////////////////////////////////////////////////////////////

  // developer code from here:
  ////////////////////////////




void setup()  {
  setupActoFramework();  //setup for ACTO_framework
  
  // developer code from here:
  ////////////////////////////
  
  

  
}


void loop()  {
  loopActoFramework();  //do loop stuff for ACTO_framework

  // developer code from here:
  ////////////////////////////






  //actoDelay(1000);  // !! use this instead of delay(), if necessary
}




//functions provided by ACTOControl
void onDigitalInputEvent(byte id, int val) {
}
void onAnalogInputEvent(byte id, int val) {
}
void onByteEvent(byte id, byte val) {
}
void onCharEvent(byte id, char val) {
}
void onIntEvent(byte id, int val) {
}
void onFloatEvent(byte id, float val) {
}


