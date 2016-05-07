
void serialEvent(Serial p) { 

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


void SendCommands() {    // send commands to PLC
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

void SendRecipe() {    // send Cycle Receipe

  //Send Commands Matrix
  String  DataMatrix = "CycleRecipe,";
  int select=int(TestReport[0][0]);
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


void ConnectSerialPort() {
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

void CheckConnection() {

  if (millis()- int(Status[0][2])<500) Connected=true;
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


void appendDiagData(String kind, String text) {
  text=str(hour())+":"+str(minute())+":"+str(second())+" - "+kind+text;
  DiagData.append(text);
  DiagData.scroll(1);
  saveData(DiagDataLog, text, true);
}

void saveData(String fileName, String newData, boolean appendData){
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