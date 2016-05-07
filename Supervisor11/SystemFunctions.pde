


void UpdatePanelStatus() {


  if (int(Commands[1][1])==1) {  //turn to AuxON
    AuxOn.hide();
    //AuxOff.show();
    AuxLit.show();
    Commands[2][1]="0";
  }
  if (int(Commands[1][1])==0) {  //turn to AuxOFF
    AuxOff.hide();
    AuxOn.show();
    AuxLit.hide();
    Commands[2][1]="1";
    TestPhaseSelection.unlock();
  }
  if (int(Commands[3][1])==1) {  //turn Air AUTO
    // println(Commands[3][1]);
    AirAuto.hide();
    AirOff.show();
    Commands[4][1]="0";
  }
  if (int(Commands[3][1])==0) {   //turn Air OFF
    //println(Commands[3][1]);
    AirOff.hide();
    AirAuto.show();
    Commands[4][1]="1";
  }
  if (int(Commands[5][1])==1) {  //turn Water AUTO
    WaterAuto.hide();
    WaterOff.show();
    Commands[6][1]="0";
  }
  if (int(Commands[5][1])==0) {  //turn Water OFF
    WaterOff.hide();
    WaterAuto.show();
    Commands[6][1]="1";
  }
  if (int(Commands[7][1])==1) {  //Start Test Routine
    TestStart.hide();
    TestStop.show();
    Commands[8][1]="0";
    TestPhaseSelection.lock();
    ProjectSelection.lock();
    CarrozzaSelection.lock();
    Report.hide();
    Reset.hide();
  }
  if (int(Commands[7][1])==0) {  //Stop Test Routine
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
  if (int(Commands[7][2])==1) {  //Automatic Ready ok
    if (int(Commands[7][1])==0) {
      TestStop.hide();
      TestStart.show();
    }
  }
  if (int(Commands[7][2])==0) {  //Automatic Ready nok
    TestStop.hide();
    TestStart.hide();
  }

  ReportPage.setText(TestReport[1][0]+"   "+TestReport[1][1]+TestReport[1][2]+TestReport[1][3]+"\n"
    +TestReport[2][0]+"   "+TestReport[2][1]+TestReport[2][2]+TestReport[2][3]+"\n"
    +TestReport[3][0]+"   "+TestReport[3][1]+TestReport[3][2]+TestReport[3][3]
    );
  ProgressPage.setText(TestReport[int(TestPhase[0][1])][4]);  


  if (Commands[0][1].equals("1")) {
    AuxEmgLit.show();
  } else   AuxEmgLit.hide();

  if (int(Component[1][1])==1)  Valve1.show();  // BypassValve
  else Valve1.hide();
  if (int(Component[2][1])==1)  Valve2.show();  //AiraValve1
  else Valve2.hide();
  if (int(Component[3][1])==1)  Valve3.show();  //AirValve2
  else Valve3.hide();
  if (int(Component[4][1])==1)  Valve4.show();  //CompValve
  else Valve4.hide();
  if (int(Component[5][1])==1)  Valve5.show();  //WaterValve
  else Valve5.hide();

  if (int(Component[9][1])==1)  Pump.show();  
  else Pump.hide();
  if (int(Component[10][1])==1) {   //FluxOk
    Flux.show();
    WaterDetect.show();
  } else {
    Flux.hide();
    WaterDetect.hide();
  }
  if (int(Component[11][1])==1 ) { //FilterNOk
    FilterRed.show();  
    FilterDetect.show();
  } else {
    FilterRed.hide();
    FilterDetect.hide();
  }

  if (int(Component[13][1])==1)  ExtraValve.show();  //StopValves
  else ExtraValve.hide();

  if (int(Component[14][1])==1)  Sezi1.show();  //Sezi[]
  else Sezi1.hide();
  if (int(Component[15][1])==1)  Sezi2.show();
  else Sezi2.hide();
  if (int(Component[16][1])==1)  Sezi3.show();
  else Sezi3.hide();
  if (int(Component[17][1])==1)  Sezi4.show();
  else Sezi4.hide();
  if (int(Component[18][1])==1)  Sezi5.show();
  else Sezi5.hide();
  if (int(Component[19][1])==1)  Sezi6.show();
  else Sezi6.hide();
  if (int(Component[20][1])==1)  Sezi7.show();
  else Sezi7.hide();

  if (int(Component[21][1])==0) {  //Water Level NOk
    WaterOkLit.setImage(loadImage("WaterNOk.png"));
  } else WaterOkLit.setImage(loadImage("WaterOk.png"));

  WaterLevelTxt.setText(Component[22][1]);
  WaterLevel.setSize(150, int(int(Component[22][1])*2.8));
  WaterLevel.setPosition(141, 220-int(Component[22][1])*2.8);

  PressChart1.push("incomingH", float(Component[23][1]));  //HP chart
  PressChart2.push("incomingL", float(Component[24][1]));  //LP chart
  PressChart3.push("incomingA", float(Component[25][1]));  //Air chart
  HPValue.setText(Component[23][1]);
  LPValue.setText(Component[24][1]);
  AirValue.setText(Component[25][1]);
  RegValue.setText(Component[27][1]);


  InletAirValue.setText(Component[26][1]);

  if (int(Component[6][1])==1) MTStopOk.show();  //MagnetoTermico Stop Valves OK
  else MTStopOk.hide();

  if (int(Component[7][1])==1) MTSezOk.show();  //MagnetoTermico Sez Valves OK
  else MTSezOk.hide();
}


void ReportShow() {   // define if to show report button
  int ReportShow=1;
  for (int i=1; i<4; i++) {
    ReportShow *=int((TestReport[i][3].equals(" Esito POSITIVO")));
  }
  if (!c03.isVisible()) {
    if (ReportShow==1) Report.show();
  } else Report.hide();
}




String Append(String data) {
  return data + ",";
}



public void controlEvent(ControlEvent theEvent) {
  try {
    if (theEvent.isFrom("menu")) {
      if (Commands[7][1]=="0") {
        PImage[] imgs= {
          loadImage("TicOff.png"), loadImage("TicOn.png")
        };

        Map m = ((MenuList)theEvent.getController()).getItem(int(theEvent.getValue()));
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
        TestReport[int(TestPhase[0][1])][4] +=  str(hour())+":"+str(minute())+":"+str(second())+"    ";  
        TestReport[int(TestPhase[0][1])][4] += TestReport[int(TestPhase[0][1])][0]+" Iniziato\n";
        appendDiagData("info: ", TestReport[int(TestPhase[0][1])][0]+" Iniziato\n");
      }
      if (theEvent.getName()=="TestStop") {  //Stop Test Routine
        TestStop.hide();
        TestStart.show();
        Commands[7][1]="0";
        Commands[8][1]="1";
        TestReport[int(TestPhase[0][1])][4] +=  str(hour())+":"+str(minute())+":"+str(second())+"    ";  
        TestReport[int(TestPhase[0][1])][4] += "ATTENZIONE: fase di test interrotta dall'operatore\n";
        appendDiagData("info: ", "Ciclo interrotto dall'operatore\n");
      }
      if (theEvent.getName()=="Reset") {  //Reset Test data
        c01.hide();
        c03.show();
        Reset.hide();
        Report.hide();
      }

      if (theEvent.getName()=="OKReset") {  //Reset Test data
        //RecipeDataUpdate(0);
        EraseAllData();
        c01.show();
        c03.hide();
        Reset.show();
        ReportShow();
        appendDiagData("info: ", "Dati Resettati\n");
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
        appendDiagData("info: ", "Report salvato su file\n");
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
        appendDiagData("info: ", "Compressore, modalità Automatica Attiva\n");
      }
      if (theEvent.getName()=="AirOff") {   //turn Air OFF
        Commands[3][1]="0";
        Commands[4][1]="1";
        appendDiagData("info: ", "Compressore, modalità Automatica Non Attiva\n");
      }
      if (theEvent.getName()=="WaterAuto") {  //turn Water AUTO
        Commands[5][1]="1";
        Commands[6][1]="0";
        appendDiagData("info: ", "Carico Acqua, modalità Automatica Attiva\n");
      }
      if (theEvent.getName()=="WaterOff") {  //turn Water OFF
        Commands[5][1]="0";
        Commands[6][1]="1";
        appendDiagData("info: ", "Carico Acqua, modalità Automatica Non Attiva\n");
      }
    }
    if (theEvent.isAssignableFrom(ScrollableList.class)) {
      int select=int(theEvent.getController().getValue());
      println("controlEvent: accessing a string from controller '"
        +theEvent.getName()+"': "
        +select);
      if (theEvent.getName()=="Seleziona_fase_test") {  //Test cycle select 
        TestPhase[0][1]= str(int(theEvent.getController().getValue()));
        ChangeCyclePage(int(theEvent.getController().getValue()));
        appendDiagData("info: ", "Selezionata fase di test "+TestReport[int(TestPhase[0][1])][0]+ "\n");
        if (int(TestPhase[0][1])==2) {  // change chart scale
          PressChart1.setRange (0, 150);
          PressChart3.setRange (0, 10);
        } else {
          PressChart1.setRange (0, 10);
          PressChart3.setRange (0, 1);
        }
        if (int(TestPhase[0][1])>3) {  // change chart scale
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
        TestReport[0][2]= str(int(theEvent.getController().getValue()));
        appendDiagData("info: ", "Selezionato Codice Carrozza: "+TestReport[0][2]+ "\n");
      }
    }

    // routine to check if all data are available for cycling 
    Commands[9][1]="0";    //check if all data are available for cycle
    if (Checklist[0][1]=="OK") { //if checklist OK, show testphase selection
      if (int(TestPhase[0][1]) !=0) {  // if a cycle is selected
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


void StartProcedure() {  //Initializza procedura di test
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


void WriteReport () {  // write the report to file
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
  saveStrings("/home/pi/Supervisor/reports/"+filename, lines);
  saveStrings("/home/pi/Supervisor/reports-temp/"+filename, lines);
}


void RecipeDataUpdate(int select) {
  ProgettoCode.setText(Project[select][0]);
  FluxDurata.setText(Project[select][1]);
  FluxPartic.setText(Project[select][2]);
  PressMax.setText(Project[select][4]);
  PressDurata.setText(Project[select][3]);
  PressStep.setText(Project[select][5]);
  DryDurata.setText(Project[select][6]);
  TestReport[0][0]=str(select);
  appendDiagData("info: ", "Selezionata Ricetta: "+Project[select][0]+"\n");
}

void EraseAllData() {
  TestPhaseSelection.setId(1);
  ProjectSelection.setId(0);
  CarrozzaSelection.setId(0);
  for (int i=1; i<4; i++) {  // erase test reports
    for (int j=1; j<5; j++) {   // erase test data
      TestReport[i][j]="";
    }
  }
  NOKCheck.show();
  OKCheck.hide();
  Checklist[0][1]="";
  TestPhaseSelection.hide();
  ReportShow();
}

void ChangeCyclePage(int page) {
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




String mesi(int num) {
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