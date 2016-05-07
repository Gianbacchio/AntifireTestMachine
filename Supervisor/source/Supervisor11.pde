/**
 * SUPERVISOR 
 * 
 * Sistema di controllo per PLC Arduino
 * Applicazione scritta da Giancarlo Bacchio
 */


import controlP5.*;
import java.util.*;
import java.util.Map.Entry;
import processing.serial.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

Boolean Connected=false;
Boolean ConnectedBefore=true;

int ConnectBlink;
int AuxConnLit_lap=1; 
int AuxConnLit_sec; 

String [][] Status=new String[1][3];
String [][] Checklist=new String[11][3];
String [][] Component=new String[30][3];
String [][] TestPhase=new String[7][15];
String [][] Commands=new String[10][3];
String [][] TestReport=new String[6][5];
String [][] Project=new String[3][7];



ControlP5 c01, c2, c02, c03, c04, c1, c2nc, c20, c21, c22, c23, c24, c25, c3, c4, c5;
Chart PressChart1, PressChart2, PressChart3;
Button DataSwitch, LogSwitch, TestStart, TestStop, Schema, AuxOn, AuxOff, AirAuto, AirOff, WaterAuto, WaterOff, WaterLevel, AuxLit, AuxConnLit, AuxEmgLit;
Button Reset, Report, OKReset, CancelReset, OKReport, CancelReport, OKCheck, NOKCheck; 
Button Valve1, Valve2, Valve3, Valve4, Valve5, Pump, Flux, ExtraValve, WaterOkLit, MTStopOk, MTSezOk;
Button Sezi1, Sezi2, Sezi3, Sezi4, Sezi5, Sezi6, Sezi7, FilterGreen, FilterRed, FilterDetect, WaterDetect; 
Textarea CheckRet, AirPress, WaterLevelTxt, QuestionReset, QuestionReport, QuestionWin;
Textarea ProgettoCode, FluxDurata, FluxPartic, PressMax, PressDurata, PressStep, DryDurata, PressValue, PressValue1, PressValue2, HPValue, LPValue, AirValue, InletAirValue, RegValue;
Textarea IntroPage, ReportPage, FluxPage, PressPage, DryPage, LoadPage, UnloadPage, ProgressPage, DataPage, DiagData;
ScrollableList TestPhaseSelection, ProjectSelection, CarrozzaSelection;

boolean NewCycleData;
PFont f1, f2, f3, f4;
PImage bg;
int y;

Serial myPort;      // The serial port
int inByte = -1;    // Incoming serial data
short   portIndex = 0; // Index of serial port in list (varies by computer)
int     lf = 10;       //ASCII linefeed
String DiagDataLog;  // name of the diag data log file


void setup() {

  /* initialize serial communication */
  ConnectSerialPort();


  f1 = createFont("Helvetica", 20);
  f2 = createFont("Helvetica", 12);
  f3 = createFont("Arial", 12);
  f4 = createFont("Arial", 14);


  /* define the background screen*/
  size(1015, 768);  //(1024, 768)
  bg = loadImage("sinottico_empty2.png");


  // definizione tipo di test per progetto
  Project[0][0]="Nessuna Ricetta";  //nome
  Project[0][1]="0"; //Flux durata
  Project[0][2]="0";  //Flux partic
  Project[0][3]="0";  //Press durata
  Project[0][4]="0";  //Press max
  Project[0][5]="0";  //Step prog press
  Project[0][6]="0";  //Asciugatura durata

  Project[1][0]="Z1 Veloce";  //nome
  Project[1][1]="1"; //Flux durata per valvola
  Project[1][2]="100";  //Flux partic
  Project[1][3]="8";  //Press durata
  Project[1][4]="120";  //Press max
  Project[1][5]="3";  //Step prog press
  Project[1][6]="1";  //Asciugatura durata

  Project[2][0]="Z1 Lenta";  //nome
  Project[2][1]="10";  //Flux durata
  Project[2][2]="100";  //Flux partic
  Project[2][3]="15";  //Press durata
  Project[2][4]="120";  //Press max
  Project[2][5]="20";  //Step prog press
  Project[2][6]="5";  //Asciugatura durata


  // definizione della check list
  Checklist[0] [0]="10";   //quanti items ha la check list
  Checklist[0] [1]="NOK";  //check list non completa
  Checklist[1] [0]="0";
  Checklist[1] [1]="Tubazioni";
  Checklist[1] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[2] [0]="1";
  Checklist[2] [1]="Tubazioni";
  Checklist[2] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[3] [0]="1";
  Checklist[3] [1]="Tubazioni";
  Checklist[3] [2]="Controllare il corretto montaggio di sta cippa e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[4] [0]="1";
  Checklist[4] [1]="Tubazioni";
  Checklist[4] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[5] [0]="1";
  Checklist[5] [1]="Tubazioni";
  Checklist[5] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[6] [0]="1";
  Checklist[6] [1]="Connettori";
  Checklist[6] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[7] [0]="1";
  Checklist[7] [1]="Connettori";
  Checklist[7] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";  
  Checklist[8] [0]="1";
  Checklist[8] [1]="Connettori";
  Checklist[8] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[9] [0]="1";
  Checklist[9] [1]="Tubazioni";
  Checklist[9] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";
  Checklist[10] [0]="1";
  Checklist[10] [1]="Tubazioni";
  Checklist[10] [2]="Controllare il corretto montaggio e serraggio delle tubazioni connesse al TextBox lato mandata HP";


  /* definizione dei componenti controllati dal sistema */
  Status[0][0]="Alive";
  Status[0][1]="0";   // è Alive
  Status[0][2]="0";   // tempo tra Alive


  Component[1][0]="Valvola bypass filtro";  //bool
  Component[2][0]="Valvola 1 aria compressa";  //bool
  Component[3][0]="Valvola 2 aria compressa";  //bool
  Component[4][0]="Valvola Compressore";
  Component[5][0]="Valvola ingresso acqua";  //bool
  Component[6][0]="";
  Component[7][0]="";
  Component[8][0]="";
  Component[9][0]="Pompa HP"; //bool
  Component[10][0]="flussostato"; //bool
  Component[11][0]="Misuratore impurità"; //bool
  Component[12][0]="";
  Component[13][0]="Valvole chiusura HP"; //bool
  Component[14][0]="Valvola di sezionamento 1"; //bool
  Component[15][0]="Valvola di sezionamento 2"; //bool
  Component[16][0]="Valvola di sezionamento 3"; //bool
  Component[17][0]="Valvola di sezionamento 4"; //bool
  Component[18][0]="Valvola di sezionamento 5"; //bool
  Component[19][0]="Valvola di sezionamento 6"; //bool
  Component[20][0]="Valvola di sezionamento 7"; //bool
  Component[21][0]="Livello Acqua OK";  //bool
  Component[22][0]="Trasduttore livello acqua";  //int
  Component[22][1]="0";
  Component[23][0]="Trasduttore pressione ritorno";  //int 
  Component[24][0]="Trasduttore pressione regolazione pompa HP";  //int
  Component[25][0]="Trasduttore pressione mandata HP";  //int
  Component[26][0]="Trasduttore Aria Compressa";  //int
  Component[26][1]="0";
  Component[27][0]="Valvola proporzianale";  // int


  TestPhase[0][0]="Nessuna selezione";
  TestPhase[0][1]="0";  //test cycle selected

  TestPhase[1][0]="Preparativi Test di Flussaggio";
  TestPhase[1][1]="Fase di flussaggio acqua valvola 1";
  TestPhase[1][2]="Fase di flussaggio acqua valvola 2";
  TestPhase[1][3]="Fase di flussaggio acqua valvola 3";
  TestPhase[1][4]="Fase di flussaggio acqua valvola 4";
  TestPhase[1][5]="Fase di flussaggio acqua valvola 5";
  TestPhase[1][6]="Fase di flussaggio acqua valvola 6";
  TestPhase[1][7]="Fase di flussaggio acqua valvola 7";
  TestPhase[1][8]="Test di flussaggio concluso";

  TestPhase[2][0]="Preparativi Test di Alta pressione";
  TestPhase[2][1]="Apertura valvole sezionatrici";
  TestPhase[2][2]="Fase a bassa pressione";
  TestPhase[2][3]="Fase a media pressione";
  TestPhase[2][4]="Fase ad alta pressione";
  TestPhase[2][5]="Test ad alta pressione concluso";

  TestPhase[3][0]="Preparativi Ciclo di Asciugatura";
  TestPhase[3][1]="Fase di asciugatura acqua valvola 1";
  TestPhase[3][2]="Fase di asciugatura acqua valvola 2";
  TestPhase[3][3]="Fase di asciugatura acqua valvola 3";
  TestPhase[3][4]="Fase di asciugatura acqua valvola 4";
  TestPhase[3][5]="Fase di asciugatura acqua valvola 5";
  TestPhase[3][6]="Fase di asciugatura acqua valvola 6";
  TestPhase[3][7]="Fase di asciugatura acqua valvola 7";
  TestPhase[3][8]="Fase di asciugatura conclusa"; 


  TestPhase[4][0]="Ciclo di Caricamento";
  TestPhase[4][1]="Fase di caricamento acqua valvola 1";
  TestPhase[4][2]="Fase di caricamento acqua valvola 2";
  TestPhase[4][3]="Fase di caricamento acqua valvola 3";
  TestPhase[4][4]="Fase di caricamento acqua valvola 4";
  TestPhase[4][5]="Fase di caricamento acqua valvola 5";
  TestPhase[4][6]="Fase di caricamento acqua valvola 6";
  TestPhase[4][7]="Fase di caricamento acqua valvola 7";
  TestPhase[4][8]="Caricamento acqua eseguito";


  TestPhase[5][0]="Ciclo di Svuotamento";
  TestPhase[5][1]="Fase di svuotamento acqua valvola 1";
  TestPhase[5][2]="Fase di svuotamento acqua valvola 2";
  TestPhase[5][3]="Fase di svuotamento acqua valvola 3";
  TestPhase[5][4]="Fase di svuotamento acqua valvola 4";
  TestPhase[5][5]="Fase di svuotamento acqua valvola 5";
  TestPhase[5][6]="Fase di svuotamento acqua valvola 6";
  TestPhase[5][7]="Fase di svuotamento acqua valvola 7";
  TestPhase[5][8]="Svuotamento acqua eseguito";


  // test report matrix
  TestReport[0][0]="";  // ProjectCode/project name/ carrozza code / operator code / test date
  TestReport[0][1]=""; //Project empty at beginning
  TestReport[1][0]="Ciclo di FLUSSAGGIO"; //Start / End/ Result / log  
  TestReport[2][0]="Ciclo di PRESSIONE"; //Start / End / Result / log 
  TestReport[3][0]="Ciclo di ASCIUGATURA"; //Start / End / Result / log 
  TestReport[4][0]=""; //Start / End / Result / log 
  TestReport[5][0]=""; //Start / End / Result / log 
  //TestReport[6][0]=""; //Start / End / Result / log 



  Commands[0][0]="Emergenza";
  Commands[0][1]="0";   // Emergenza off
  Commands[1][0]="Ausiliari ON";
  Commands[2][0]="Ausiliari OFF";
  Commands[3][0]="Compressore AUTO";
  Commands[4][0]="Compressore OFF";
  Commands[5][0]="Carico Acqua AUTO";
  Commands[6][0]="Carico Acqua OFF";
  Commands[7][0]="Fase Test ATTIVA";
  Commands[8][0]="Fase Test STOP";
  Commands[9][0]="Dati sufficienti per avviare ciclo";
  Commands[9][1]="0";   // dati sufficienti per iniziare il ciclo 

  PanelStructureSetup();

  EraseAllData();  // erase all report data
  
  DiagDataLog="/home/pi/Desktop/Supervisor/diagdatalog/"+year()+month()+day()+hour()+minute()+"LOG.txt";
  String init="SEMA TextBox Data LOG file\r\n"+day()+"/"+month()+"/"+year()+"  -  "+hour()+":"+minute()+":"+second()+"\r\n\r\n";
  println(DiagDataLog);
  println(init);
  saveData(DiagDataLog, init, false);
}

//*************************************
//****** DRAW ****************************
//********************************


void draw() {
  background(bg);

  CheckConnection();
}