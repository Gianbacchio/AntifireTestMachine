import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import java.util.*; 
import java.util.Map.Entry; 
import processing.serial.*; 
import java.io.BufferedWriter; 
import java.io.FileWriter; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Supervisor11 extends PApplet {

/**
 * SUPERVISOR 
 * 
 * Sistema di controllo per PLC Arduino
 * Applicazione scritta da Giancarlo Bacchio
 */









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


public void setup() {

  /* initialize serial communication */
  ConnectSerialPort();


  f1 = createFont("Helvetica", 20);
  f2 = createFont("Helvetica", 12);
  f3 = createFont("Arial", 12);
  f4 = createFont("Arial", 14);


  /* define the background screen*/
    //(1024, 768)
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
  Status[0][1]="0";   // \u00e8 Alive
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
  Component[11][0]="Misuratore impurit\u00e0"; //bool
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


public void draw() {
  background(bg);

  CheckConnection();
}

public void serialEvent(Serial p) { 

  try {
    String inString = myPort.readString();
    String[] dataStrings = split(inString, ',');

    if (dataStrings[0].equals("Alive")) {
      //println(inString);
      Status[0][1]="1";
      Status[0][2]=str(millis());

      // Connection light - flash when connected
      if (AuxConnLit.isVisible()) AuxConnLit.hide();
      else {
        if (ConnectBlink++==2) {
          ConnectBlink=0;   
          AuxConnLit.show();
        }
      }
    }

    if (dataStrings[0].equals("Commands")) { 
      // println(inString);
      Commands[0][1]=dataStrings[1];  //Emg on/off
      Commands[1][1]=dataStrings[2];  //Aux on/off
      Commands[3][1]=dataStrings[3];  //Comp Auto/off
      Commands[5][1]=dataStrings[4];  //Water Auto/off
      Commands[7][2]=dataStrings[5];  //Automatic ready/no ready
      Commands[7][1]=dataStrings[6];  //Automatic on/off

      //    myPort.write("Alive\n"); // reply Alive
    }

    if (dataStrings[0].equals("Components")) { 
      println(inString);
      Component[1][1]=dataStrings[1];  // ByPassValve
      Component[2][1]=dataStrings[2];  // AirValve1
      Component[3][1]=dataStrings[3];  // AirValve2
      Component[4][1]=dataStrings[4];  // CompValve
      Component[5][1]=dataStrings[5];  // WaterValve
      Component[6][1]=dataStrings[6];  // MTStopOk
      Component[7][1]=dataStrings[7];  // MTSezOk
      Component[8][1]=dataStrings[8];  // ..
      Component[9][1]=dataStrings[9];  // PumpOk
      Component[10][1]=dataStrings[10];  // FluxOk
      Component[11][1]=dataStrings[11];  // FilterOk
      Component[12][1]=dataStrings[12];  // ..
      Component[13][1]=dataStrings[13];  // StopValves
      Component[14][1]=dataStrings[14];  // Sezi1
      Component[15][1]=dataStrings[15];  // Sezi2
      Component[16][1]=dataStrings[16];  // Sezi3
      Component[17][1]=dataStrings[17];  // Sezi4
      Component[18][1]=dataStrings[18];  // Sezi5
      Component[19][1]=dataStrings[19];  // Sezi6
      Component[20][1]=dataStrings[20];  // Sezi7
      Component[21][1]=dataStrings[21];  // Water level OK
      Component[22][1]=dataStrings[22];  // water level
      Component[23][1]=dataStrings[23];  // HP press
      Component[24][1]=dataStrings[24];  // LP press
      Component[25][1]=dataStrings[25];  // Air press
      Component[26][1]=dataStrings[26];  // Comp press
      Component[27][1]=dataStrings[27];  // Reg valve proportional
    }

    if (dataStrings[0].equals("info")) appendDiagData("info: ", dataStrings[1]);
    if (dataStrings[0].equals("diagnostic")) appendDiagData("warning: ", dataStrings[1]);


    if (dataStrings[0].equals("CycleProgress")) { 
      println(inString);

      if (dataStrings[4].equals("1")) {
        TestReport[Integer.parseInt(dataStrings[1])][2]="   fine: "+str(hour())+":"+str(minute());  //ora di fine ciclo
        TestReport[Integer.parseInt(dataStrings[1])][3]=" Esito POSITIVO";  // esito del ciclo
      }
      if (dataStrings[4].equals("2")) {
        TestReport[Integer.parseInt(dataStrings[1])][2]="   fine: "+str(hour())+":"+str(minute());  //ora di fine ciclo
        TestReport[Integer.parseInt(dataStrings[1])][3]=" Esito NEGATIVO";  // esito del ciclo
      }
      if  (dataStrings[3].equals("1")) {    //appendi fase al log con esito Positivo
        TestReport[Integer.parseInt(dataStrings[1])][4] +=  str(hour())+":"+str(minute())+":"+str(second())+"    ";  
        TestReport[Integer.parseInt(dataStrings[1])][4] +=  TestPhase[Integer.parseInt(dataStrings[1])][Integer.parseInt(dataStrings[2])];
        TestReport[Integer.parseInt(dataStrings[1])][4] += "   esito Positivo\r\n";
      }
      if  (dataStrings[3].equals("2")) {
        TestReport[Integer.parseInt(dataStrings[1])][4] +=  str(hour())+":"+str(minute())+":"+str(second())+"    ";  
        TestReport[Integer.parseInt(dataStrings[1])][4] +=  TestPhase[Integer.parseInt(dataStrings[1])][Integer.parseInt(dataStrings[2])];
        TestReport[Integer.parseInt(dataStrings[1])][4] += "   esito Negativo\r\n";
      }
      //  Aggiungi inizio e fine log


      NewCycleData=true;
    }

    UpdatePanelStatus();
  }
  catch (Exception e) {
    println("Caught Exception, wait for connections");
    println(e);
  }
}


public void SendCommands() {    // send commands to PLC
  try {
    if (Status[0][1]=="1") {
      Status[0][1]="0";
      //Send Commands Matrix
      String  DataMatrix = "Commands,";
      DataMatrix = DataMatrix + Append(Commands[1][1]);  //Aux
      DataMatrix = DataMatrix + Append(Commands[3][1]);  //Comp
      DataMatrix = DataMatrix + Append(Commands[5][1]);  //Water
      DataMatrix = DataMatrix + Append(Commands[7][1]);  //Start Cycle
      DataMatrix = DataMatrix + Append(Commands[9][1]);  //data ok for Test Cycle
      DataMatrix +="\n";
      myPort.write(DataMatrix);
      println(DataMatrix);
    }
  }
  catch (Exception e) {
    println("Caught Exception, unable to send data");
    println(e);
  }
}

public void SendRecipe() {    // send Cycle Receipe

  //Send Commands Matrix
  String  DataMatrix = "CycleRecipe,";
  int select=PApplet.parseInt(TestReport[0][0]);
  DataMatrix = DataMatrix + Append(TestPhase[0][1]);  //Cycle to run
  DataMatrix = DataMatrix + Append(Project[select][1]);  // FluxDurata
  DataMatrix = DataMatrix + Append(Project[select][2]);  //FluxPartic
  DataMatrix = DataMatrix + Append(Project[select][3]);  //PressMax
  DataMatrix = DataMatrix + Append(Project[select][4]);  // PressDurata
  DataMatrix = DataMatrix + Append(Project[select][5]);  //PressStep
  DataMatrix = DataMatrix + Append(Project[select][6]);  //DryDurata
  DataMatrix +="\n";
  myPort.write(DataMatrix);
  println(DataMatrix);
}


public void ConnectSerialPort() {
  try {
    String portName = Serial.list()[portIndex];
    myPort = new Serial(this, portName, 9600);
    myPort.clear();
    myPort.bufferUntil(lf);
    println(" Connected to -> " + Serial.list()[portIndex]);
  }
  catch (Exception e) {
    println("Caught Exception, lost communication");
  }
}

public void CheckConnection() {

  if (millis()- PApplet.parseInt(Status[0][2])<500) Connected=true;
  else Connected =false;
  if (!Connected)   ConnectSerialPort();

  if (Connected!=ConnectedBefore) {
    if (Connected) {
      ChangeCyclePage(0);
      c2nc.hide();
      c20.show();
      c21.show();
      c01.show();
      c02.show();
      c3.show();
      c4.show();
      c5.show();
      ConnectedBefore=Connected;
    } else {
      println("no connection");
      c20.hide();
      c21.hide();
      c01.hide();
      c02.hide();
      c3.hide();
      c4.hide();
      c5.hide();
      c23.hide();
      c2nc.show();
      ConnectedBefore=Connected;
    }
  }
}


public void appendDiagData(String kind, String text) {
  text=str(hour())+":"+str(minute())+":"+str(second())+" - "+kind+text;
  DiagData.append(text);
  DiagData.scroll(1);
  saveData(DiagDataLog, text, true);
}

public void saveData(String fileName, String newData, boolean appendData){
  BufferedWriter bw = null;
  try {  
    FileWriter fw = new FileWriter(fileName, appendData);
    bw = new BufferedWriter(fw);
  //  bw.write(newData + System.getProperty("line.separator"));
    bw.write(newData);
  } catch (IOException e) {
  } finally {
    if (bw != null){
      try { 
        bw.close(); 
      } catch (IOException e) {
      println(e);}  
    }
  }
}


/* Menu list */

/* a convenience function to build a map that contains our key-value pairs which we will 
 * then use to render each item of the menuList.
 */
public Map<String, Object> makeItem(int id, String theHeadline, String theSubline, String theCopy, PImage theImage) {
  Map m = new HashMap<String, Object>();
  m.put("headline", theHeadline);
  m.put("subline", theSubline);
  m.put("copy", theCopy);
  m.put("image", theImage);
  m.put("is_on", 0);
  m.put("id", id);
  return m;
}

public void menu(int i) {
  println("got some menu event from item with index "+i);
  Checklist[i+1][0]=str(abs(PApplet.parseInt(Checklist[i+1][0])-1));
}



/* A custom Controller that implements a scrollable menuList. Here the controller 
 * uses a PGraphics element to render customizable list items. The menuList can be scrolled
 * using the scroll-wheel, touchpad, or mouse-drag. Items are triggered by a click. clicking 
 * the scrollbar to the right makes the list scroll to the item correspoinding to the 
 * click-location. 
 */
class MenuList extends Controller<MenuList> {

  float pos, npos;
  int itemHeight = 50;
  int scrollerLength = 30;
  List< Map<String, Object>> items = new ArrayList< Map<String, Object>>();
  PGraphics menu;
  boolean updateMenu;

  MenuList(ControlP5 c, String theName, int theWidth, int theHeight) {
    super( c, theName, 0, 0, theWidth, theHeight );
    c.register( this );
    menu = createGraphics(getWidth(), getHeight() );

    setView(new ControllerView<MenuList>() {

      public void display(PGraphics pg, MenuList t ) {
        if (updateMenu) {
          updateMenu();
        }
        if (inside() ) {
          menu.beginDraw();
          int len = -(itemHeight * items.size()) + getHeight();
          int ty = PApplet.parseInt(map(pos, len, 0, getHeight() - scrollerLength - 2, 2 ) );
          //menu.fill(255 );
          menu.rect(getWidth()-4, ty, 4, scrollerLength );
          menu.endDraw();
        }
        pg.image(menu, 0, 0);
      }
    }
    );
    updateMenu();
  }

  /* only update the image buffer when necessary - to save some resources */
  public void updateMenu() {
    int len = -(itemHeight * items.size()) + getHeight();
    npos = constrain(npos, len, 0);
    pos += (npos - pos) * 0.1f;
    menu.beginDraw();
    menu.noStroke();
    menu.background(255, 0 );
    menu.textFont(cp5.getFont().getFont());
    menu.pushMatrix();
    menu.translate( 0, pos );
    menu.pushMatrix();

    int i0 = PApplet.max( 0, PApplet.parseInt(map(-pos, 0, itemHeight * items.size(), 0, items.size())));
    int range = ceil((PApplet.parseFloat(getHeight())/PApplet.parseFloat(itemHeight))+1);
    int i1 = PApplet.min( items.size(), i0 + range );

    menu.translate(0, i0*itemHeight);

    for (int i=i0; i<i1; i++) {
      Map m = items.get(i);
      menu.fill(255, 180);
      menu.rect(0, 0, getWidth(), itemHeight-1 );
      menu.fill(0);
      menu.textFont(f1);
      menu.text(m.get("headline").toString(), 10, 20 );
      menu.textFont(f2);
      menu.textLeading(12);
      menu.text(m.get("subline").toString(), 10, 35 );
      menu.text(m.get("copy").toString(), 130, 5, 310, 50 );
      menu.image(((PImage)m.get("image")), 433, 5, 40, 40 );
      menu.translate( 0, itemHeight );
    }
    menu.popMatrix();
    menu.popMatrix();
    menu.endDraw();
    updateMenu = abs(npos-pos)>0.01f ? true:false;
  }

  /* when detecting a click, check if the click happend to the far right, if yes, scroll to that position, 
   * otherwise do whatever this item of the list is supposed to do.
   */
  public void onClick() {
    if (getPointer().x()>getWidth()-10) {
      npos= -map(getPointer().y(), 0, getHeight(), 0, items.size()*itemHeight);
      updateMenu = true;
    } else {
      int len = itemHeight * items.size();
      int index = PApplet.parseInt( map( getPointer().y() - pos, 0, len, 0, items.size() ) ) ;
      setValue(index);
    }
  }

  public void onMove() {
  }

  public void onDrag() {
    npos += getPointer().dy() * 2;
    updateMenu = true;
  } 

  public void onScroll(int n) {
    npos += ( n * 4 );
    updateMenu = true;
  }

  public void addItem(Map<String, Object> m) {
    items.add(m);
    updateMenu = true;
  }

  public void setItem(int i, Map<String, Object> m) {
    items.set(i, m);
    updateMenu = true;
  }


  public Map<String, Object> getItem(int theIndex) {
    return items.get(theIndex);
  }
}

public void PanelStructureSetup() {

  // Data entry Panel
  c01 = new ControlP5( this );
  c01.enableShortcuts();
  c01.setBackground( color(255, 1 ) );

  c01.addGroup("g01")
    .setPosition(55, 50)
    .hideBar()
    ;

  TestStart=c01.addButton("TestStart")
    .setSize(80, 60)
    .setPosition( 7, 144)
    // .hide()
    .setImage(loadImage("START.png"))
    .setGroup("g01")
    ;
  TestStop=c01.addButton("TestStop")
    .setSize(80, 60)
    .setPosition( 126, 143)
    //  .hide()
    .setImage(loadImage("STOP.png"))
    .setGroup("g01")
    ;

  CarrozzaSelection=c01.addScrollableList("Codice_Carrozza")
    .setPosition(127, 98)
    .setSize(125, 125)
    .setBarHeight(22)
    .setItemHeight(22)
    .setColorBackground(color(255))
    .setColorCaptionLabel(color(0))
    .setColorForeground(color(150))
    .setColorValue(color(0))
    .setColorValueLabel(color(0))
    .close()
    .setGroup("g01")
    ;

  CarrozzaSelection.addItem("null", 0);
  for (int i=1; i<100; i++) {
    CarrozzaSelection.addItem(str(i), 1);
  } 

  TestPhaseSelection=c01.addScrollableList("Seleziona_fase_test")
    .setPosition(127, 55)
    .setSize(125, 200)
    .setBarHeight(22)
    .setItemHeight(22)
    .setColorBackground(color(255))
    .setColorCaptionLabel(color(0))
    .setColorForeground(color(150))
    .setColorValue(color(0))
    .setColorValueLabel(color(0))
    .close()
    .hide()
    .setGroup("g01")
    ;

  TestPhaseSelection.addItem(TestPhase[0][0], 0);
  TestPhaseSelection.addItem(TestReport[1][0], 1);
  TestPhaseSelection.addItem(TestReport[2][0], 2);
  TestPhaseSelection.addItem(TestReport[3][0], 3);
  TestPhaseSelection.addItem(TestPhase[4][0], 4);
  TestPhaseSelection.addItem(TestPhase[5][0], 5);



  ProjectSelection=c01.addScrollableList("seleziona_ricetta")
    .setPosition(127, 12)
    .setSize(125, 200)
    .setBarHeight(22)
    .setItemHeight(22)
    .setColorBackground(color(255))
    .setColorCaptionLabel(color(0))
    .setColorForeground(color(150))
    .setColorValue(color(0))
    .setColorValueLabel(color(0))
    .close()
    .setGroup("g01")
    ;
  for (int i=0; i<3; i++) {
    ProjectSelection.addItem(Project[i][0], i);
  }

  // Data test Panel
  c02 = new ControlP5( this );
  c02.enableShortcuts();
  c02.setBackground( color(255, 1 ) );

  c02.addGroup("g02")
    .setPosition (327, 72)
    .hideBar()
    ;

  ProgettoCode=c02.addTextarea("progetto_code")
    .setPosition(235, 5)
    .setSize(70, 18)
    .setLineHeight(15)
    .setColorBackground(color(255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(250, 0, 0))
    .setFont(f3)
    .setText("No Project")
    .hideScrollbar()
    .setGroup("g02")
    ;

  FluxDurata=c02.addTextarea("Flux_durata")
    .setPosition(235, 36)
    .setSize(70, 18)
    .setLineHeight(15)
    .setColorBackground(color(255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(250, 0, 0))
    .setFont(f3)
    .setText("")
    .hideScrollbar()
    .setGroup("g02")
    ; 
  FluxPartic=c02.addTextarea("Flux_partic")
    .setPosition(235, 60)
    .setSize(70, 18)
    .setLineHeight(15)
    .setColorBackground(color(255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(250, 0, 0))
    .setFont(f3)
    .setText("")
    .hideScrollbar()
    .setGroup("g02")
    ; 
  PressDurata=c02.addTextarea("Press_durata")
    .setPosition(235, 84)
    .setSize(70, 18)
    .setLineHeight(15)
    .setColorBackground(color(255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(250, 0, 0))
    .setFont(f3)
    .setText("")
    .hideScrollbar()
    .setGroup("g02")
    ;   
  PressMax=c02.addTextarea("Press_max")
    .setPosition(235, 107)
    .setSize(70, 18)
    .setLineHeight(15)
    .setColorBackground(color(255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(250, 0, 0))
    .setFont(f3)
    .setText("")
    .hideScrollbar()
    .setGroup("g02")
    ;    
  PressStep=c02.addTextarea("Press_step")
    .setPosition(235, 131)
    .setSize(70, 18)
    .setLineHeight(15)
    .setColorBackground(color(255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(250, 0, 0))
    .setFont(f3)
    .setText("")
    .hideScrollbar()
    .setGroup("g02")
    ;     
  DryDurata=c02.addTextarea("Dry_durata")
    .setPosition(235, 155)
    .setSize(70, 18)
    .setLineHeight(15)
    .setColorBackground(color(255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(250, 0, 0))
    .setFont(f3)
    .setText("")
    .hideScrollbar()
    .setGroup("g02")
    ;     
  Reset=c02.addButton("Reset")
    .setSize(80, 30)
    .setPosition( 9, -8)
    .setImage(loadImage("RESET.png"))
    .setGroup("g02")
    ;
  Report=c02.addButton("Report")
    .setSize(80, 30)
    .setPosition( 120, -8 )
    .hide()
    .setImage(loadImage("REPORT.png"))
    .setGroup("g02")
    ;

  // Data Question Reset Panel
  c03 = new ControlP5( this );
  c03.enableShortcuts();
  c03.setBackground( color(255 ) );
  c03.hide();
  c03.addGroup("g03")
    .setPosition (275, 110)
    .hideBar()
    ;

  c03.addTextarea("Question")
    .setPosition(0, 0)
    .setSize(310, 60)
    .setColorBackground(color(255, 255))
    .show()
    .setGroup("g03")
    ;

  OKReset=c03.addButton("OKReset")
    .setSize(155, 60)
    .setPosition( 0, 60)
    .setGroup("g03")
    ;
  CancelReset=c03.addButton("CancelReset")
    .setSize(155, 60)
    .setPosition( 156, 60 )
    .setGroup("g03")
    ;
  QuestionReset=c03.addTextarea("QuestionReset")
    .setPosition(10, 10)
    .setSize(300, 80)
    .setLineHeight(10)
    .setColorBackground(color(255, 0))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(25, 55, 94))
    .setFont((createFont("arial", 10)))
    .setText("ATTENZIONE\n"
    +"Si stanno per cancellare i dati relativi al test in corso\n"
    +"Confermare?")
    .hideScrollbar()
    .setGroup("g03")
    ;    

  // Data Question Report Panel
  c04 = new ControlP5( this );
  c04.enableShortcuts();
  c04.setBackground( color(255, 255 ) );
  c04.hide();

  c04.addGroup("g04")
    .setPosition (270, 110)
    .hideBar()
    ;

  c04.addTextarea("Question")
    .setPosition(0, 0)
    .setSize(310, 80)
    .setColorBackground(color(255, 255))
    .show()
    .setGroup("g04")
    ;

  OKReport=c04.addButton("OKReport")
    .setSize(155, 60)
    .setPosition( 0, 80)
    .setGroup("g04")
    ;
  CancelReport=c04.addButton("CancelReport")
    .setSize(155, 60)
    .setPosition( 156, 80 )
    .setGroup("g04")
    ;
  QuestionReset=c04.addTextarea("QuestionReport")
    .setPosition(10, 10)
    .setSize(300, 80)
    .setLineHeight(10)
    .setColorBackground(color(255, 0))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(25, 55, 94))
    .setFont(f2)
    .setText("ATTENZIONE\n"
    +"Si sta per scriver il report relativo alla prova ora eseguita\n"
    +"Confermare?")
    .hideScrollbar()
    .setGroup("g04")
    ;    

  // Check List Panel
  c1 = new ControlP5( this );
  c1.enableShortcuts();
  c1.setBackground( color(255, 1 ) );

  c1.addGroup("g1")
    .setPosition (25, 260)
    .hideBar()
    ;


  OKCheck=c1.addButton("OKCheck")
    .setSize(50, 40)
    .setPosition( 42, 70)
    .hide()
    .setImage(loadImage("OK.png"))
    .setGroup("g1")
    ;
  NOKCheck=c1.addButton("NOKCheck")
    .setSize(50, 40)
    .setPosition( 42, 70 )
    //.hide()
    .setImage(loadImage("NOK.png"))
    .setGroup("g1")
    ;

  /*
  CheckRet=c1.addTextarea("CheckRet")
   .setPosition(40, 64)
   .setSize(52, 35)
   .setLineHeight(15)
   .setColorBackground(color(255, 255))
   .setColorForeground(color(200, 0, 0))
   .setColor(color(25, 55, 94))
   .setFont(f4)
   .setText(Checklist[0][1])
   .hideScrollbar()
   .setGroup("g1")
   .hide()
   ;    
   */

  /*
  MenuList m = new MenuList( c1, "menu", 483, 121 );
   m.setPosition(160, 265);
   
   
   // add some items to our menuList
   PImage[] imgs= {
   loadImage("TicOff.png"), loadImage("TicOn.png")
   };
   for (int i=1; i<int (Checklist[0][0])+1; i++) {
   m.addItem(makeItem(i, "Controllo-"+i, Checklist[i][1], Checklist[i][2], imgs[Integer.parseInt(Checklist[i][0])] ));
   }
   */


  // Test Progress Panel

  c2nc = new ControlP5( this );
  c2nc.enableShortcuts();
  c2nc.setBackground( color(79, 129, 189 ) );

  c2nc.addGroup("g2nc")
    .setPosition (25, 410)
    .hideBar()
    ;

  c2nc.addTextarea("non_connessa")
    .setPosition(8, 7)
    .setSize(613, 310)
    .setFont(createFont("arial", 15))
    .setBorderColor(100)
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(255, 200, 10))
    .setTitle("ATTENZIONE")
    .setColorForeground(color(255, 255))
    .setGroup("g2nc")
    .setText("  ATTENZIONE\n\n\n"
    +"  \u00e8 assente il collegamento tra supervisore e controllore della macchina." 
    );


  c20 = new ControlP5( this );    //Summary cycles page
  c20.enableShortcuts();
  c20.setBackground( color(79, 129, 189 ) );
  c20.hide();

  c20.addGroup("g20")
    .setPosition (25, 410)
    .hideBar()
    ;


  ReportPage=c20.addTextarea("Cycle Report Page")
    .setPosition(135, -123)
    .setSize(485, 95)
    .setFont(createFont("arial", 12))
    .setBorderColor(100)
    .setLineHeight(31)
    .setColor(color(0))
    .setColorBackground(color(219, 238, 244))
    .setTitle("Cycle Report")
    .setColorForeground(color(255, 255))
    .show()
    .setText("")
    .setGroup("g20")
    .hideBar()
    ;

  DataSwitch=c20.addButton("DataSwitch")
    .setSize(70, 50)
    .setPosition( 550, 265 )
    .show()
    .setImage(loadImage("DATA.png"))
    .setGroup("g20")
    ;
  LogSwitch=c20.addButton("LogSwitch")
    .setSize(70, 50)
    .setPosition( 548, 265 )
    .hide()
    .setImage(loadImage("LOG.png"))
    .setGroup("g20")
    ;
  DiagData=c20.addTextarea("DiagData")
    .setPosition(8, 260)
    .setSize(530, 58)
    .setGroup("g20")
    .setFont(createFont("arial", 10))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(255))
    .setColorForeground(color(255, 255))
    .setText("Diagnostic\n"
    );

  c21 = new ControlP5( this );    // Deatils phases page
  c21.enableShortcuts();
  c21.setBackground( color(255, 1 ) );
  c21.hide();

  c21.addGroup("g21")
    .setPosition (25, 410)
    .hideBar()
    ;


  IntroPage=c21.addTextarea("intro")
    .setPosition(8, 7)
    .setSize(613, 245)
    .setFont(createFont("arial", 13))
    .setBorderColor(100)
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(219, 238, 244))
    .setTitle("ATTENZIONE")
    .setColorForeground(color(255, 255))
    .show()
    .setGroup("g21")
    .setText("   ATTENZIONE!!\n"
    +"   Per avviare il processo di test \u00e8 necessario eseguire le seguenti operazioni:"
    +"\n\n"
    +" - Verificare il corretto montaggio di tutte le parti che costituiscono \n"
    +"   l'installazione ed eseguire la check list.\n"
    +" - Confermare l'esecuzione della check list clickando il pulsante 'Check List'\n"
    +" - Attivare i servizi Ausiliari\n"
    +" - Attivare il compressore in modalit\u00e0 \"AUTO\"\n"
    +" - Caricare, se necessario, acqua nel serbatoio collegandolo all'impianto idrico\n"
    +"   e attivando il relativo comando 'AUTO'\n"
    +" - Impostare i dati relativi al test in esecuzione"
    +"\n\n"
    +" Quando si saranno eseguite tutte queste operazioni,\n"
    +" si potr\u00e0 selezionare la fase di test da mandare in esecuzione."
    );

  FluxPage=c21.addTextarea("Ciclo Flussaggio")
    .setPosition(8, 7)
    .setSize(613, 245)
    .setFont(createFont("arial", 13))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(219, 238, 244))
    .setColorForeground(color(255, 255))
    .setGroup("g21")
    .setText("  CICLO di FLUSSAGGIO\n\n"
    +"  Il ciclo di flussaggio prevede la circolazione di acqua nelle tubazioni per la pulizia\n"
    +"  dell'impianto.\n"
    +"  Il sensore di particolato segnala la presenza di impurit\u00e0 e il filtro le raccoglie.\n"
    +"  La fase di Flussaggio si completa al termine del tempo predefinito.\n"
    +"  Il ciclo \u00e8 diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
    +"  sulla carrozza.\n\n"
    +"  Per iniziare il ciclo, premere il pulsante 'START'\n"
    +"  Per monitorare i parametri di lavorazione, premere il pulsante 'Data'"
    );

  PressPage=c21.addTextarea("Ciclo di Alta Pressione")
    .setPosition(8, 7)
    .setSize(613, 245)
    .setFont(createFont("arial", 13))
    .setLineHeight(16)
    .setColor(color(0))
    .setGroup("g21")
    .setColorBackground(color(219, 238, 244))
    .setColorForeground(color(255, 255))
    .setText("  CICLO di ALTA PRESSIONE\n\n"
    +"  Il ciclo di ALTA PRESSIONE prevede di sottoporre le tubazioni dell'impianto in carrozza\n"
    +"  a fasi progressive di pressione crescente fino al raggiungimento della pressione\n"
    +"  massima impostata.\n"
    +"  Tra una fase e l'altra, il TextBox interrompe la crescita diella pressione e controlla\n"
    +"  che non vi siano perdite di pressione superiori al 10% all'interno dell'impianto.\n"
    +"  Se al termine della crescita di pressione, non si saranno verificate perdite significative,\n"
    +"  il TextBox giudicher\u00e0 il ciclo di Alta Pressione concluso con successo.\n"
    +"  In alternativa, il TextBox segnaler\u00e0 la presenza di perdite importanti e sar\u00e0 necessario\n"
    +"  eliminarle prima di eseguire nuovamete il ciclo di test.\n\n"
    +"  Per iniziare il ciclo, premere il pulsante 'START'\n"
    +"  Per monitorare i parametri di lavorazione, premere il pulsante 'Data'"   
    );


  DryPage=c21.addTextarea("Ciclo Asciugatura")
    .setPosition(8, 7)
    .setSize(613, 245)
    .setFont(createFont("arial", 13))
    .setLineHeight(16)
    .setColor(color(0))
    .setGroup("g21")
    .setColorBackground(color(219, 238, 244))
    .setColorForeground(color(255, 255))
    .setText("  CICLO di SVUOTAMENTO e ASCIUGATURA\n\n"
    +"  Il ciclo di Svuotamento e Asciugatura conclude la fase di test e permette di eliminare\n"
    +"  l'acqua presente nelle tubazioni e procedere alla loro asciugatura tramite aria\n  compressa.\n"
    +"  Il ciclo \u00e8 diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
    +"  sulla carrozza.\n\n"
    +"  Per iniziare il ciclo, premere il pulsante 'START'\n"
    +"  Per monitorare i parametri di lavorazione, premere il pulsante 'Data'"   
    );

  LoadPage=c21.addTextarea("Ciclo Caricamento")
    .setPosition(8, 7)
    .setSize(613, 245)
    .setGroup("g21")
    .setFont(createFont("arial", 13))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(219, 238, 244))
    .setColorForeground(color(255, 255))
    .setText("  CICLO di CARICAMENTO ACQUA\n\n"
    +"  Il ciclo di caricamento acqua permette di eseguie il riempimento di tutte le tubazioni\n  dell'impianto.\n"
    +"  Il ciclo \u00e8 diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
    +"  sulla carrozza.\n\n"
    +"  Per iniziare il ciclo, premere il pulsante 'START'\n"
    +"  Per monitorare i parametri di lavorazione, premere il pulsante 'Data'"   
    );
    
  UnloadPage=c21.addTextarea("Ciclo Svuotamento")
    .setPosition(8, 7)
    .setSize(613, 245)
    .setGroup("g21")
    .setFont(createFont("arial", 13))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(219, 238, 244))
    .setColorForeground(color(255, 255))
    .setText("  CICLO di SVUOTAMENTO ACQUA\n\n"
    +"  Il ciclo di svuotamento acqua permette di eliminare tutta l'acqua presente nelle tubazioni\n  dell'impianto.\n"
    +"  Il ciclo \u00e8 diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
    +"  sulla carrozza.\n\n"
    +"  Per iniziare il ciclo, premere il pulsante 'START'\n"
    +"  Per monitorare i parametri di lavorazione, premere il pulsante 'Data'"   
    );


  ProgressPage=c21.addTextarea("Progress data")
    .setPosition(8, 7)
    .setSize(613, 245)
    .setGroup("g21")
    .setFont(createFont("arial", 12))
    .setLineHeight(14)
    .setColor(color(0))
    .setColorBackground(color(219, 238, 244))
    .setColorForeground(color(255, 255))
    .setText("")
    ;



  c23 = new ControlP5( this );    // Data Flow
  c23.enableShortcuts();
  c23.setBackground( color(255, 1 ) );
  c23.hide();

  c23.addGroup("g23")
    .setPosition (75, 360)
    .hideBar()
    ;

  DataPage=c23.addTextarea("Data Page")
    .setPosition(-42, 57)
    .setSize(613, 245)
    .setGroup("g23")
    .setFont(createFont("arial", 12))
    .setLineHeight(14)
    .setColor(color(0))
    .setColorBackground(color(219, 238, 240))
    .setColorForeground(color(255, 255))
    .setText("")
    ;
  Schema=c23.addButton("Schema")
    .setSize(80, 60)
    .setPosition( 148, 72)
    .show()
    .setImage(loadImage("Schema.png"))
    .setGroup("g23")
    ;
  Valve1=c23.addButton("Valve1")  // bypass filtro
    .setSize(5, 5)
    .setPosition( 430, 113)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Valve2=c23.addButton("Valve2")  // air 1
    .setSize(5, 5)
    .setPosition( 428, 166)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Valve3=c23.addButton("Valve3")  // air 2
    .setSize(5, 5)
    .setPosition( 428, 201)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Valve4=c23.addButton("Valve4")  // comp
    .setSize(5, 5)
    .setPosition( 393, 227)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Valve5=c23.addButton("Valve5") // acqua
    .setSize(5, 5)
    .setPosition( 521, 134)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Pump=c23.addButton("Pump")  //pompa
    .setSize(5, 5)
    .setPosition( 459, 225)
    .show()
    .setGroup("g23")
    .setImage(loadImage("PumpOn.png"))
    ;
  Flux=c23.addButton("Flux")  //flux
    .setSize(5, 5)
    .setPosition( 453, 98)
    .show()
    .setGroup("g23")
    .setImage(loadImage("FluxOn.png"))
    ;
  FilterRed=c23.addButton("FilterRed")
    .setSize(5, 5)
    .setPosition( 396, 89)
    .hide()
    .setGroup("g23")
    .setImage(loadImage("FilterRed.png"))
    ;
  FilterGreen=c23.addButton("FilterGreen")
    .setSize(5, 5)
    .setPosition( 397, 89)
    .setGroup("g23")
    .setImage(loadImage("FilterGreen.png"))
    ;

  ExtraValve=c23.addButton("ExtraValve")
    .setSize(5, 5)
    .setPosition( 167, 109)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ExtraValveOn.png"))
    ;

  Sezi1=c23.addButton("Sezi1")
    .setSize(5, 5)
    .setPosition( 162, 91)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Sezi2=c23.addButton("Sezi2")
    .setSize(5, 5)
    .setPosition( 183, 91)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Sezi3=c23.addButton("Sezi3")
    .setSize(5, 5)
    .setPosition( 205, 91)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Sezi4=c23.addButton("Sezi4")
    .setSize(5, 5)
    .setPosition( 235, 91)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Sezi5=c23.addButton("Sezi5")
    .setSize(5, 5)
    .setPosition( 265, 91)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Sezi6=c23.addButton("Sezi6")
    .setSize(5, 5)
    .setPosition( 295, 91)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;
  Sezi7=c23.addButton("Sezi7")
    .setSize(5, 5)
    .setPosition( 318, 91)
    .show()
    .setGroup("g23")
    .setImage(loadImage("ValveOn.png"))
    ;

  MTSezOk=c23.addButton("MTSezOk")
    .setSize(5, 5)
    .setPosition( 510, 80)
    .hide()
    .setGroup("g23")
    .setImage(loadImage("MTOk.png"))
    ;
  MTStopOk=c23.addButton("MTStopOk")
    .setSize(5, 5)
    .setPosition( 530, 80)
    .hide()
    .setGroup("g23")
    .setImage(loadImage("MTOk.png"))
    ;

  PressValue1=c23.addTextarea("Valori pressione1")
    .setPosition(105, 70)
    .setSize(28, 215)
    .setGroup("g23")
    .setFont(createFont("arial", 9))
    .setLineHeight(15)
    .setColor(color(0))
    .setColorBackground(color(255))
    .setColorForeground(color(255, 255))
    .setText("(H)\n\n\n\n\n"
    +"(L)\n\n\n\n\n"
    +"(R)"
    );



  PressChart1 = c23.addChart("PressHP")  // chart High pressure
    .setPosition(-30, 70)
    .setSize(130, 65)
    .setRange(0, 10)
    .setGroup("g23")
    .setView(Chart.LINE) // use Chart.LINE, Chart.PIE, Chart.AREA, Chart.BAR_CENTERED
    .setStrokeWeight(1.5f)
    .setColorCaptionLabel(color(255, 1))
    .setColorBackground(color(255))
    .setColorLabel(color(255, 0))
    //.hide()
    ;
  PressChart1.addDataSet("incomingH");
  PressChart1.setData("incomingH", new float[1000]);

  PressChart2 = c23.addChart("PressLP")  // chart Low Pressure
    .setPosition(-30, 145)
    .setSize(130, 65)
    .setRange(0, 10)
    .setGroup("g23")
    .setView(Chart.LINE) // use Chart.LINE, Chart.PIE, Chart.AREA, Chart.BAR_CENTERED
    .setStrokeWeight(1.5f)
    .setColorCaptionLabel(color(255, 1))
    .setColorBackground(color(255))
    .setColorLabel(color(255, 0))
    ;
  PressChart2.addDataSet("incomingL");
  PressChart2.setData("incomingL", new float[1000]);

  PressChart3 = c23.addChart("PressProp")  // chart Air pressure from Proportional valve
    .setPosition(-30, 220)
    .setSize(130, 65)
    .setRange(0, 10)
    .setGroup("g23")
    .setView(Chart.LINE) // use Chart.LINE, Chart.PIE, Chart.AREA, Chart.BAR_CENTERED
    .setStrokeWeight(1.5f)
    .setColorCaptionLabel(color(255, 1))
    .setColorBackground(color(255))
    .setColorLabel(color(255, 0))
    ;
  PressChart3.addDataSet("incomingA");
  PressChart3.setData("incomingA", new float[1000]);



  // Auxiliaries Panel
  c3 = new ControlP5( this );
  c3.enableShortcuts();
  c3.setBackground( color(255, 1 ) );

  c3.addGroup("g3")
    .setPosition (690, 40)
    .hideBar()
    ;


  AuxOn=c3.addButton("AuxOn")
    .setSize(80, 60)
    .setPosition( 19, 25)
    .setImage(loadImage("ON.png"))
    .setGroup("g3")
    ;
  AuxOff=c3.addButton("AuxOff")
    .setSize(80, 60)
    .setPosition( 19, 56 )
    .hide()
    .setImage(loadImage("OFF.png"))
    .setGroup("g3")
    ;
  AuxLit=c3.addButton("AuxLit")
    .setSize(50, 50)
    .setPosition( 130, 12)
    .hide()
    .setGroup("g3")
    .setImage(loadImage("RedOn.png"))
    ;
  AuxConnLit=c3.addButton("AuxConnLit")
    .setSize(50, 50)
    .setPosition( 133, 55)
    .hide()
    .setGroup("g3")
    .setImage(loadImage("GreenOn.png"))
    ;
  AuxEmgLit=c3.addButton("AuxEmgLit")
    .setSize(100, 100)
    .setPosition( 187, 4)
    .hide()
    .setGroup("g3")
    .setImage(loadImage("EmgOn.png"))
    ;



  // Compressor Panel
  c4 = new ControlP5( this );
  c4.enableShortcuts();
  c4.setBackground( color(255, 1 ) );

  c4.addGroup("g4")
    .setPosition (695, 205)
    .hideBar()
    ;

  PressValue=c4.addTextarea("Valori pressione")
    .setPosition(130, 0)
    .setSize(162, 255)
    .setGroup("g4")
    .setFont(createFont("arial", 10))
    .setLineHeight(25)
    .setColor(color(0))
    .setColorBackground(color(255))
    .setColorForeground(color(255, 255))
    .setText("Alta Pressione (H)\n\n"
    +"Bassa Pressione (L)\n\n"
    +"Pressione Pompa (A)\n\n"
    +"Pressione Aria Ingresso (I)\n\n" 
    +"Regolazione Valvola (P)"
    );

  HPValue=c4.addTextarea("HP")
    .setPosition(135, 20)
    .setSize(130, 30)
    .setGroup("g4")
    .setFont(createFont("arial", 18))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(200))
    .setColorForeground(color(255))
    .setText("0.0")   
    .hideScrollbar()
    ;

  LPValue=c4.addTextarea("LP")
    .setPosition(135, 70)
    .setSize(130, 30)
    .setGroup("g4")
    .setFont(createFont("arial", 18))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(200))
    .setColorForeground(color(255, 255))
    .setText("0.0")
    .hideScrollbar()
    ;
  AirValue=c4.addTextarea("Air")
    .setPosition(135, 120)
    .setSize(130, 30)
    .setGroup("g4")
    .setFont(createFont("arial", 18))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(200))
    .setColorForeground(color(255, 255))
    .setText("0.0")            
    .hideScrollbar()
    ;
  InletAirValue=c4.addTextarea("InletAir")
    .setPosition(135, 170)
    .setSize(130, 30)
    .setGroup("g4")
    .setFont(createFont("arial", 18))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(200))
    .setColorForeground(color(255, 255))
    .setText("0.0")            
    .hideScrollbar()
    ;

  RegValue=c4.addTextarea("Reg")
    .setPosition(135, 220)
    .setSize(130, 30)
    .setGroup("g4")
    .setFont(createFont("arial", 18))
    .setLineHeight(16)
    .setColor(color(0))
    .setColorBackground(color(200))
    .setColorForeground(color(255, 255))
    .setText("0.0")            
    .hideScrollbar()
    ;

  AirAuto=c4.addButton("AirAuto")
    .setSize(80, 60)
    .setPosition( 15, 14)
    .hide()
    .setGroup("g4")
    .setImage(loadImage("AUTO.png"))
    ;
  AirOff=c4.addButton("AirOff")
    .setSize(80, 60)
    .setPosition( 15, 77 )
    .hide()
    .setGroup("g4")
    .setImage(loadImage("OFF.png"))
    ;
  FilterDetect=c4.addButton("FilterDetect")
    .setSize(80, 60)
    .setPosition( 19, 147 )
    .hide()
    .setGroup("g4")
    .setImage(loadImage("redlit.png"))
    ;
  WaterDetect=c4.addButton("WaterDetect")
    .setSize(80, 60)
    .setPosition( 18, 200 )
    .hide()
    .setGroup("g4")
    .setImage(loadImage("greenlit.png"))
    ;

  // Water tank Panel
  c5 = new ControlP5( this );
  c5.enableShortcuts();
  c5.setBackground( color(255, 1 ) );

  c5.addGroup("g5")
    .setPosition (690, 513)
    .hideBar()
    ;

  WaterAuto=c5.addButton("WaterAuto")
    .setSize(80, 60)
    .setPosition( 19, 12)
    .hide()
    .setGroup("g5")
    .setImage(loadImage("AUTO.png"))
    ;
  WaterOff=c5.addButton("WaterOff")
    .setSize(80, 60)
    .setPosition( 19, 75 )
    .hide()
    .setGroup("g5")
    .setImage(loadImage("OFF.png"))
    ;
  WaterLevel=c5.addButton("WaterLevel")
    .setSize(150, PApplet.parseInt(PApplet.parseInt(Component[22][1])*2.8f))
    .setPosition(141, 220-PApplet.parseInt(Component[22][1])*2.8f)
    .setColorBackground(color(0,100, 200))
    .setGroup("g5")
    ;
   WaterOkLit=c5.addButton("WaterOkLit")
    .setSize(150, PApplet.parseInt(PApplet.parseInt(Component[22][1])*2.8f))
    .setPosition(16, 138)
    .setGroup("g5")
    .setImage(loadImage("WaterNOk.png"))
    ;
  WaterLevelTxt=c5.addTextarea("WaterLevelTxt")
    .setPosition(37, 159)
    .setSize(56, 25)
    .setLineHeight(15)
    .setColorBackground(color(255, 255))
    .setColorForeground(color(200, 0, 0))
    .setColor(color(25, 55, 94))
    .setFont(f4)
    .setGroup("g5")
    .setText(Component[22][1])
    .hideScrollbar()
    ;
}



public void UpdatePanelStatus() {


  if (PApplet.parseInt(Commands[1][1])==1) {  //turn to AuxON
    AuxOn.hide();
    //AuxOff.show();
    AuxLit.show();
    Commands[2][1]="0";
  }
  if (PApplet.parseInt(Commands[1][1])==0) {  //turn to AuxOFF
    AuxOff.hide();
    AuxOn.show();
    AuxLit.hide();
    Commands[2][1]="1";
    TestPhaseSelection.unlock();
  }
  if (PApplet.parseInt(Commands[3][1])==1) {  //turn Air AUTO
    // println(Commands[3][1]);
    AirAuto.hide();
    AirOff.show();
    Commands[4][1]="0";
  }
  if (PApplet.parseInt(Commands[3][1])==0) {   //turn Air OFF
    //println(Commands[3][1]);
    AirOff.hide();
    AirAuto.show();
    Commands[4][1]="1";
  }
  if (PApplet.parseInt(Commands[5][1])==1) {  //turn Water AUTO
    WaterAuto.hide();
    WaterOff.show();
    Commands[6][1]="0";
  }
  if (PApplet.parseInt(Commands[5][1])==0) {  //turn Water OFF
    WaterOff.hide();
    WaterAuto.show();
    Commands[6][1]="1";
  }
  if (PApplet.parseInt(Commands[7][1])==1) {  //Start Test Routine
    TestStart.hide();
    TestStop.show();
    Commands[8][1]="0";
    TestPhaseSelection.lock();
    ProjectSelection.lock();
    CarrozzaSelection.lock();
    Report.hide();
    Reset.hide();
  }
  if (PApplet.parseInt(Commands[7][1])==0) {  //Stop Test Routine
    TestStop.hide();
    TestStart.show();
    Commands[7][1]="0";
    Commands[8][1]="1";
    TestPhaseSelection.unlock();
    ProjectSelection.unlock();
    CarrozzaSelection.unlock();
    ReportShow();
    if (!c03.isVisible()) {
      if (!c04.isVisible())   Reset.show();
    }
  }  
  if (PApplet.parseInt(Commands[7][2])==1) {  //Automatic Ready ok
    if (PApplet.parseInt(Commands[7][1])==0) {
      TestStop.hide();
      TestStart.show();
    }
  }
  if (PApplet.parseInt(Commands[7][2])==0) {  //Automatic Ready nok
    TestStop.hide();
    TestStart.hide();
  }

  ReportPage.setText(TestReport[1][0]+"   "+TestReport[1][1]+TestReport[1][2]+TestReport[1][3]+"\n"
    +TestReport[2][0]+"   "+TestReport[2][1]+TestReport[2][2]+TestReport[2][3]+"\n"
    +TestReport[3][0]+"   "+TestReport[3][1]+TestReport[3][2]+TestReport[3][3]
    );
  ProgressPage.setText(TestReport[PApplet.parseInt(TestPhase[0][1])][4]);  


  if (Commands[0][1].equals("1")) {
    AuxEmgLit.show();
  } else   AuxEmgLit.hide();

  if (PApplet.parseInt(Component[1][1])==1)  Valve1.show();  // BypassValve
  else Valve1.hide();
  if (PApplet.parseInt(Component[2][1])==1)  Valve2.show();  //AiraValve1
  else Valve2.hide();
  if (PApplet.parseInt(Component[3][1])==1)  Valve3.show();  //AirValve2
  else Valve3.hide();
  if (PApplet.parseInt(Component[4][1])==1)  Valve4.show();  //CompValve
  else Valve4.hide();
  if (PApplet.parseInt(Component[5][1])==1)  Valve5.show();  //WaterValve
  else Valve5.hide();

  if (PApplet.parseInt(Component[9][1])==1)  Pump.show();  
  else Pump.hide();
  if (PApplet.parseInt(Component[10][1])==1) {   //FluxOk
    Flux.show();
    WaterDetect.show();
  } else {
    Flux.hide();
    WaterDetect.hide();
  }
  if (PApplet.parseInt(Component[11][1])==1 ) { //FilterNOk
    FilterRed.show();  
    FilterDetect.show();
  } else {
    FilterRed.hide();
    FilterDetect.hide();
  }

  if (PApplet.parseInt(Component[13][1])==1)  ExtraValve.show();  //StopValves
  else ExtraValve.hide();

  if (PApplet.parseInt(Component[14][1])==1)  Sezi1.show();  //Sezi[]
  else Sezi1.hide();
  if (PApplet.parseInt(Component[15][1])==1)  Sezi2.show();
  else Sezi2.hide();
  if (PApplet.parseInt(Component[16][1])==1)  Sezi3.show();
  else Sezi3.hide();
  if (PApplet.parseInt(Component[17][1])==1)  Sezi4.show();
  else Sezi4.hide();
  if (PApplet.parseInt(Component[18][1])==1)  Sezi5.show();
  else Sezi5.hide();
  if (PApplet.parseInt(Component[19][1])==1)  Sezi6.show();
  else Sezi6.hide();
  if (PApplet.parseInt(Component[20][1])==1)  Sezi7.show();
  else Sezi7.hide();

  if (PApplet.parseInt(Component[21][1])==0) {  //Water Level NOk
    WaterOkLit.setImage(loadImage("WaterNOk.png"));
  } else WaterOkLit.setImage(loadImage("WaterOk.png"));

  WaterLevelTxt.setText(Component[22][1]);
  WaterLevel.setSize(150, PApplet.parseInt(PApplet.parseInt(Component[22][1])*2.8f));
  WaterLevel.setPosition(141, 220-PApplet.parseInt(Component[22][1])*2.8f);

  PressChart1.push("incomingH", PApplet.parseFloat(Component[23][1]));  //HP chart
  PressChart2.push("incomingL", PApplet.parseFloat(Component[24][1]));  //LP chart
  PressChart3.push("incomingA", PApplet.parseFloat(Component[25][1]));  //Air chart
  HPValue.setText(Component[23][1]);
  LPValue.setText(Component[24][1]);
  AirValue.setText(Component[25][1]);
  RegValue.setText(Component[27][1]);


  InletAirValue.setText(Component[26][1]);

  if (PApplet.parseInt(Component[6][1])==1) MTStopOk.show();  //MagnetoTermico Stop Valves OK
  else MTStopOk.hide();

  if (PApplet.parseInt(Component[7][1])==1) MTSezOk.show();  //MagnetoTermico Sez Valves OK
  else MTSezOk.hide();
}


public void ReportShow() {   // define if to show report button
  int ReportShow=1;
  for (int i=1; i<4; i++) {
    ReportShow *=PApplet.parseInt((TestReport[i][3].equals(" Esito POSITIVO")));
  }
  if (!c03.isVisible()) {
    if (ReportShow==1) Report.show();
  } else Report.hide();
}




public String Append(String data) {
  return data + ",";
}



public void controlEvent(ControlEvent theEvent) {
  try {
    if (theEvent.isFrom("menu")) {
      if (Commands[7][1]=="0") {
        PImage[] imgs= {
          loadImage("TicOff.png"), loadImage("TicOn.png")
        };

        Map m = ((MenuList)theEvent.getController()).getItem(PApplet.parseInt(theEvent.getValue()));
        println("got a menu evento from item : "+m.get("id"));


        if ((Integer)m.get("is_on")==1)
          m.put("is_on", 0);
        else
          m.put("is_on", 1);    
        m.put("image", imgs[(Integer)m.get("is_on")]);

        Checklist[(Integer)m.get("id")][0]= str((Integer)(m.get("is_on"))); 
        Checklist[0][1]="OK";

        for (int i=1; i<Integer.parseInt (Checklist[0][0])+1; i++) {
          if (Integer.parseInt(Checklist[i][0])==0) {
            Checklist[0][1]="NOK";
            break;
          }
        }

        CheckRet.setText(Checklist[0][1]);
        if (Checklist[0][1]=="OK")  //if checklist OK, show testphase selection
          TestPhaseSelection.show();
        else {
          TestPhaseSelection.hide();
        }
        ((MenuList)theEvent.getController()).updateMenu();
      }
    }

    if (theEvent.isAssignableFrom(Textfield.class)) {
      println("controlEvent: accessing a string from controller '"
        +theEvent.getName()+"': "
        +theEvent.getStringValue()
        );
    }

    if (theEvent.isAssignableFrom(Button.class)) {
      println("controlEvent: accessing a string from controller '"
        +theEvent.getName()+"': "
        +theEvent.getStringValue()
        );
      if (theEvent.getName()=="TestStart") {  //Start Test Routine
        TestStart.hide();
        TestStop.show();
        Commands[7][1]="1";
        Commands[8][1]="0";
        SendRecipe();
        StartProcedure();
        TestReport[PApplet.parseInt(TestPhase[0][1])][4] +=  str(hour())+":"+str(minute())+":"+str(second())+"    ";  
        TestReport[PApplet.parseInt(TestPhase[0][1])][4] += TestReport[PApplet.parseInt(TestPhase[0][1])][0]+" Iniziato\n";
        appendDiagData("info: ", TestReport[PApplet.parseInt(TestPhase[0][1])][0]+" Iniziato\n");
      }
      if (theEvent.getName()=="TestStop") {  //Stop Test Routine
        TestStop.hide();
        TestStart.show();
        Commands[7][1]="0";
        Commands[8][1]="1";
        TestReport[PApplet.parseInt(TestPhase[0][1])][4] +=  str(hour())+":"+str(minute())+":"+str(second())+"    ";  
        TestReport[PApplet.parseInt(TestPhase[0][1])][4] += "ATTENZIONE: fase di test interrotta dall'operatore\n";
        appendDiagData("info: ", "Ciclo interrotto dall'operatore\n");
      }
      if (theEvent.getName()=="Reset") {  //Reset Test data
        c01.hide();
        c03.show();
        Reset.hide();
        Report.hide();
      }

      if (theEvent.getName()=="OKReset") {  //Reset Test data
        RecipeDataUpdate(0);
        EraseAllData();
        c01.show();
        c03.hide();
        Reset.show();
        ReportShow();
      }
      if (theEvent.getName()=="CancelReset") {  //Reset Test data
        c01.show();
        c03.hide();
        Reset.show();
      }
      if (theEvent.getName()=="Report") {  //Report Test data
        c01.hide();
        c04.show();
        Reset.hide();
        Report.hide();
      }

      if (theEvent.getName()=="OKReport") {  //Report Test data
        WriteReport();
        c01.show();
        c04.hide();
        Reset.show();
        ReportShow();
      }
      if (theEvent.getName()=="CancelReport") {  //Report Test data
        c01.show();
        c04.hide();
        Reset.show();
        ReportShow();
      }
      if (theEvent.getName()=="NOKCheck") {  //Check list OK
        OKCheck.show();
        NOKCheck.hide();
        Checklist[0][1]="OK";
        TestPhaseSelection.show();
        appendDiagData("info: ", "Check List done\n");
      }
      if (theEvent.getName()=="OKCheck") {  //Check list NOK
        NOKCheck.show();
        OKCheck.hide();
        Checklist[0][1]="";
        TestPhaseSelection.hide();
      }
      if (theEvent.getName()=="LogSwitch") {  //Report Test data
        c21.show();
        c23.hide();
        DataSwitch.show();
        LogSwitch.hide();
      }
      if (theEvent.getName()=="DataSwitch") {  //Report Test data
        c23.show();
        c21.hide();
        DataSwitch.hide();
        LogSwitch.show();
      }

      if (theEvent.getName()=="AuxOn") {  //turn to Aux ON

        Commands[1][1]="1";
        Commands[2][1]="0";
      }
      if (theEvent.getName()=="AuxOff") {  //turn to Aux OFF

        Commands[1][1]="0";
        Commands[2][1]="1";
        TestPhaseSelection.unlock();
      }
      if (theEvent.getName()=="AirAuto") {  //turn Air AUTO

        Commands[3][1]="1";
        Commands[4][1]="0";
      }
      if (theEvent.getName()=="AirOff") {   //turn Air OFF
        Commands[3][1]="0";
        Commands[4][1]="1";
      }
      if (theEvent.getName()=="WaterAuto") {  //turn Water AUTO
        Commands[5][1]="1";
        Commands[6][1]="0";
      }
      if (theEvent.getName()=="WaterOff") {  //turn Water OFF
        Commands[5][1]="0";
        Commands[6][1]="1";
      }
    }
    if (theEvent.isAssignableFrom(ScrollableList.class)) {
      int select=PApplet.parseInt(theEvent.getController().getValue());
      println("controlEvent: accessing a string from controller '"
        +theEvent.getName()+"': "
        +select);
      if (theEvent.getName()=="Seleziona_fase_test") {  //Test cycle select 
        TestPhase[0][1]= str(PApplet.parseInt(theEvent.getController().getValue()));
        ChangeCyclePage(PApplet.parseInt(theEvent.getController().getValue()));
        if (PApplet.parseInt(TestPhase[0][1])==2) {  // change chart scale
          PressChart1.setRange (0, 150);
          PressChart3.setRange (0, 10);
        } else {
          PressChart1.setRange (0, 10);
          PressChart3.setRange (0, 1);
        }
        if (PApplet.parseInt(TestPhase[0][1])>3) {  // change chart scale
          c23.show();
          c21.hide();
          DataSwitch.hide();
          LogSwitch.show();
        }
      }
      if (theEvent.getName()=="seleziona_ricetta") {  //Recipe select 
        RecipeDataUpdate(select);
      }
      if (theEvent.getName()=="Codice_Carrozza") {  //Carrozza code select 
        TestReport[0][2]= str(PApplet.parseInt(theEvent.getController().getValue()));
      }
    }

    // routine to check if all data are available for cycling 
    Commands[9][1]="0";    //check if all data are available for cycle
    if (Checklist[0][1]=="OK") { //if checklist OK, show testphase selection
      if (PApplet.parseInt(TestPhase[0][1]) !=0) {  // if a cycle is selected
        if (ProgettoCode.getText().equals("No Project")==false) { // if a project is selected
          if (TestReport[0][2]!="") {  // is a carrozza code is selected
            Commands[9][1]="1";  // all data available!
          }
        }
      }
    }

    SendCommands();
  }
  catch (Exception e) {
    println("Caught Exception, unable to Control Events");
    println(e);
  }
}


public void StartProcedure() {  //Initializza procedura di test
  if (TestReport[0][1]=="") {    //scrivi i dati di inizio test su nuova carrozza
    TestReport[0][4]=str(day())+" "+ mesi(month())+" "+str(year());
    TestReport[0][1]=ProgettoCode.getText();
  }
  //  if (Integer.parseInt(TestPhase[0][1])<3)  {
  TestReport[Integer.parseInt(TestPhase[0][1])][1]="inizio test: "+str(hour())+":"+str(minute());  //cycle start time
  TestReport[Integer.parseInt(TestPhase[0][1])][2]="  ...in progress";  //cycle end time
  TestReport[Integer.parseInt(TestPhase[0][1])][3]="";  //delete previous result (if any)
  TestReport[Integer.parseInt(TestPhase[0][1])][4]="";  //delete previous result (if any)
  //}
  ChangeCyclePage(6); //change to the page of reports
}


public void WriteReport () {  // write the report to file
  String[] lines = new String[12];
  lines[0]="TEST REPORT\r\n";
  lines[1]="Prova di Test in pressione e pulizia Impianto Antincendio \r\n";
  lines[2]="Codice Carrozza:   "+TestReport[0][2]+"\r\n";
  lines[3]="PROCEDURA di TEST:     "+TestReport[0][1]+"";
  lines[4]="Durata fase di Flussaggio:   "+(Project[Integer.parseInt(TestReport[0][0])][1])+ " min.";
  lines[5]="Dimensioni Particolato:   "+(Project[Integer.parseInt(TestReport[0][0])][2])+ " micron";
  lines[6]="Durata Test in Pressione:   "+(Project[Integer.parseInt(TestReport[0][0])][3])+ " min.";
  lines[7]="Presione massima esercitata:   "+(Project[Integer.parseInt(TestReport[0][0])][4])+ " bar";
  lines[8]="Step incrementale di pressione:   "+(Project[Integer.parseInt(TestReport[0][0])][5])+ " bar";
  lines[9]="Durata fase di Asciugatura:   "+(Project[Integer.parseInt(TestReport[0][0])][6])+ " min\r\n\r\n";


  for (int i=1; i<4; i++) {
    String a=TestReport[i][0]+TestReport[i][3];
    lines= append(lines, a);
    a=TestReport[i][1]+TestReport[i][2];
    lines= append(lines, a);
    lines= append(lines, TestReport[i][4]);
  }
  String chiusura="Savigliano,   "+TestReport[0][4]+"\r\n";
  lines= append(lines, chiusura) ;
  lines= append(lines, "Firma Operatore:") ;
  String filename=TestReport[0][2]+"-"+str(year())+str(month())+str(day())+".txt";
  saveStrings("/reports/"+filename, lines);
}


public void RecipeDataUpdate(int select) {
  ProgettoCode.setText(Project[select][0]);
  FluxDurata.setText(Project[select][1]);
  FluxPartic.setText(Project[select][2]);
  PressMax.setText(Project[select][4]);
  PressDurata.setText(Project[select][3]);
  PressStep.setText(Project[select][5]);
  DryDurata.setText(Project[select][6]);
  TestReport[0][0]=str(select);
}

public void EraseAllData() {
  TestPhaseSelection.setId(0);
  ProjectSelection.setId(0);
  CarrozzaSelection.setId(0);
  for (int i=1; i<4; i++) {  // erase test reports
    for (int j=1; j<5; j++) {   // erase test data
      TestReport[i][j]="";
    }
  }
  ReportShow();
}

public void ChangeCyclePage(int page) {
  try {
    c2nc.hide();
    IntroPage.hide();
    FluxPage.hide();
    PressPage.hide();
    DryPage.hide();
    LoadPage.hide();
    UnloadPage.hide();
    ProgressPage.hide();
    println("CyclePage change to:    "+page);
    switch(page) {
    case  0:
      IntroPage.show();
      break;
    case  1:
      FluxPage.show();
      break;
    case  2:
      PressPage.show();
      break;
    case  3:
      DryPage.show();
      break;
    case  4:
      LoadPage.show();
      break;
    case  5:
      UnloadPage.show();
      break;
    case 6:
      ProgressPage.show();
    }
  }
  catch (Exception e) {
    println("Caught Exception, unable to change page");
    println(e);
  }
}




public String mesi(int num) {
  String mese="";
  switch (num) {
  case 1:
    mese="Gennaio";
    break;  
  case 2:
    mese="Febbraio";
    break;  
  case 3:
    mese= "Marzo";
    break;  
  case 4:
    mese= "Aprile";
    break;  
  case 5:
    mese= "Maggio";
    break;  
  case 6:
    mese= "Giugno";
    break;  
  case 7:
    mese= "Luglio";
    break;  
  case 8:
    mese= "Agosto";
    break;  
  case 9:
    mese= "Settembre";
    break;  
  case 10:
    mese= "Ottobre";
    break;  
  case 11:
    mese= "Novembre";
    break;  
  case 12:
    mese= "Dicembre";
    break;
  }
  return mese;
}
  public void settings() {  size(1015, 768); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Supervisor11" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
