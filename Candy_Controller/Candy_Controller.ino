/**
 * Candy Controller
 *
 * Sistema PLC Arduino per Test Machine
 * Applicazione scritta da Giancarlo Bacchio
 */

String mess;
bool BypassValve, AirValve1, AirValve2, CompValve, WaterValve, StopValves, PumpOk, FluxOk, FilterOk;
bool Sezi[8], SuperAliveOk, MTStopOk, MTSezOk, CompOk;
float TankVolume, CompressorPressure, HpSensor, LpSensor, AirSensor, PropReg, HpCheck ;
bool AuxOk, AuxOkOld, EmgPressed, EmgPressedOld, WaterReadyOld;
unsigned long  DataRefreshMillis, AliveRefreshMillis, PressEncrMillis, PressStopMillis;
unsigned long  AutoCycleMillis, AutoPhaseMillis, ExternalTasksMillis, EmgCycleMillis, FluxMillis, WashMillis, CloseMillis, DryMillis, BlinkMillis, SuperAliveMillis;
int TestCycleSelection, TestPhaseActive, TestValveActive, lit;
int WashDurata, FluxPartic , PressMax, PressDurata, PressStep, DryDurata;
int StepPhase, StepEmg, Phase, PressPhase;
bool CompressorAuto, AuxiliaryOn, WaterLoadAuto, AutomaticOn, AutomaticOnBefore;
bool DataOKforCycle;
int TestPhaseResult, TestCycleResult;  // 0 in progress / 1 ok / 2 aborted
bool AutomaticReady;
bool AirReady, WaterReady;
bool NewCycleData;






void setup() {

  Serial.begin(9600);
  delay(3000);
  Phase = 0;
  mess.reserve(100);
  PropReg = 0;

  //set all pinout
  pinMode(13, OUTPUT);  // blink led
  for (int i = 22; i < 54; i++) pinMode(i, OUTPUT);  // set relay-boards output

  pinMode(2, INPUT_PULLUP); // AuxOk - Auxiliary On
  pinMode(3, INPUT_PULLUP); // MTStopOk - Circuit Breaker Stop Valves
  pinMode(4, INPUT_PULLUP);  // MTSezOk - Circuit Breaker Sectional Valves
  pinMode(5, INPUT_PULLUP); // EmgPressed - Emergency pressed
  pinMode(6, INPUT_PULLUP); // FluxOk - Water flowing
  pinMode(7, INPUT_PULLUP); // CompOk - Compressor On
  pinMode(8, INPUT_PULLUP); // FilterOk - Filter detection

  for (int i = 0; i < 32 ; i++) { // output to sectional valves
    digitalWrite(22 + i, HIGH);

  }
}

void loop() {

  ReadFieldData();

  SendData(); // each 250msec

  ReceiveCommands();  //interrupt

  EventRuler(); // whenever

  AutoCycle();  // each 100msec

  WriteFieldData();

  // AliveBlink();
  /*
    //routines automatiche di simulazione campo
    if (millis() > ExternalTasksMillis + 250)  {
      ExternalTasksMillis = millis();
      // EmgCycle();
      LoadWater();
      LoadAir();
      PumpWater();
      FluxWater();
      FilterWater();
      DryAir();
      LPressure();
    }
    */
}





void ReadFieldData() {

  // analog input data

  HpSensor = (float(analogRead(A2)) - 160) / (1024 - 160) * 500; // 160 bit min 0 bar, max 344 bar - Sensore linea HP
  LpSensor = (float(analogRead(A3)) - 160) / (1024 - 160) * 23.6; // 160 bit min 0 bar, max 17 bar Sensore linea LP
  AirSensor = (float(analogRead(A0)) - 204) / (1024 - 204) * 10; // Sensore linea Aria
  //   TankVolume = float(analogRead(A1));//Sensore livello acqua serbatoio

  TankVolume = (float(analogRead(A1)) - 303) / (796 - 303) * 47; // 47 litri, 303 min bit, 796 bit max - Sensore livello acqua serbatoio
  CompressorPressure = (float(analogRead(A4)) - 160) / (1024 - 160) * 23.6; // Sensore pressione a monte della proporzionale


  //  String info = "info, " + String(HpSensor);
  // Serial.println(info);

  // digital input data
  //PumpOk è unno stato logico interno al PLC
  AuxOk = digitalRead(2); //safety relay is on
  MTStopOk = !digitalRead(3);  // Circuit Breaker Stop Valves
  MTSezOk = !digitalRead(4);   // Circuit Breaker Sectional Valves
  EmgPressed = digitalRead(5);
  FluxOk = !digitalRead(6);  //FLU
  FilterOk = !digitalRead(8);
  CompOk = !digitalRead(9);
}


void WriteFieldData()  {

  // proportional valve output (propreg)
  int var = PropReg / 10 * 255;

  // digitalWrite(46,LOW);
  // digitalWrite(42,LOW);

  if (var & B00000001) digitalWrite(38, LOW);
  if (var & B00000010) digitalWrite(40, LOW);
  if (var & B00000100) digitalWrite(42, LOW);
  if (var & B00001000) digitalWrite(44, LOW);
  if (var & B00010000) digitalWrite(46, LOW);
  if (var & B00100000) digitalWrite(48, LOW);
  if (var & B01000000) digitalWrite(50, LOW);
  if (var & B10000000) digitalWrite(52, LOW);

  // digitalWrite(52, !(HIGH && (var & B10000000)));
  // digitalWrite(50, !(HIGH && (var & B01000000)));
  // digitalWrite(48, !(HIGH && (var & B00100000)));
  // digitalWrite(46, !(HIGH && (var & B00010000)));
  // digitalWrite(44, !(HIGH && (var & B00001000)));
  // digitalWrite(42, !(HIGH && (var & B00000100)));
  // digitalWrite(40, !(HIGH && (var & B00000010)));
  // digitalWrite(38, !(HIGH && (var & B00000001)));


  for (int i = 0; i < 7; i++) { // output to sectional valves
    digitalWrite(25 + 2 * i, !Sezi[i + 1]);
  }

  digitalWrite(23, !StopValves);
  digitalWrite(22, !BypassValve);
  digitalWrite(24, !AirValve1);
  digitalWrite(26, !AirValve2);
  digitalWrite(28, !CompValve);
  digitalWrite(30, !WaterValve);
  digitalWrite(32, !CompressorAuto);
  digitalWrite(34, !AuxiliaryOn);


  //  String info = "info, " + String(AuxiliaryOn);
  //   Serial.println(info);
  //      info = "info, " + String(DataRefreshMillis) + " " + String(millis());
  //    Serial.println(info);
}



void AliveBlink() { // blinks if Supervisor is connected
  int BlinkSpeed = 500; //slow if connected with Supervisor
  if (!SuperAliveOk) BlinkSpeed = BlinkSpeed / 2; //slow if connected with Supervisor
  if (millis() - BlinkMillis > BlinkSpeed)  {
    BlinkMillis = millis();
    lit = abs(lit - 1);
    digitalWrite(13, lit);
  }
}



void EventRuler() {


  // Supervisor alive check
  if (millis() - SuperAliveMillis > 1000) {
    //  AuxiliaryOn = false;  // Auxiliary off if coomunication is lost
    SuperAliveOk = false;
  }
  else SuperAliveOk = true;

  //Emergency pressed manager
  if (EmgPressed != EmgPressedOld) {
    EmgPressedOld = EmgPressed ;
    if (EmgPressed) {
      AuxiliaryOn = false;
      CompressorAuto = false;
      WaterLoadAuto = false;
      AutomaticOn = false;
      Serial.println("diagnostic, EMERGENZA Premuta");
      StopAll();
    }
    else Serial.println("info, EMERGENZA reset");
  }

  if (AuxOk) AuxiliaryOn = true;

  // if (CompOk) CompressorAuto = true;

  //Auxiliary Off manager
  if (AuxOk != AuxOkOld) {
    AuxOkOld = AuxOk;
    if (!AuxOk) {
      CompressorAuto = false;
      WaterLoadAuto = false;
      AutomaticOn = false;
      Serial.println("info, Auxiliary Off");
      StopAll();
    }
    else Serial.println("info, Auxiliary On");
  }
  //Water valve Manager
  if (WaterLoadAuto) {
    if (TankVolume < 25)  {
      WaterValve = true;
    }
    else {
      WaterValve = false;
    }
  }
  if (TankVolume > 24)  {
    WaterReady = true;
    if (WaterReady != WaterReadyOld) {
      WaterReadyOld = WaterReady;
      Serial.println("info, Livello Acqua OK");
    }
  }
  if (TankVolume < 10)  {
    WaterReady = false;
    if (WaterReady != WaterReadyOld) {
      WaterReadyOld = WaterReady;
      Serial.println("info, Livello Acqua non sufficiente");
    }
  }

  if (!WaterLoadAuto)  {
    WaterValve = false;
  }

  //Air Pressure Manager
  if (CompressorAuto) {
    //    if (CompressorPressure > 12)
    AirReady = true;
  }
  else AirReady = false;

  //Automatic Ready manager
  AutomaticReady = false;
  if (AuxiliaryOn) {
    if (WaterReady)  {
      if (AirReady)  {
        if (DataOKforCycle != 0) {
          AutomaticReady = true;
        }
      }
    }
  }

  //Automatic On manager
  if (!AutomaticReady)
    AutomaticOn = false;


  // PumpOk manager
  if (PropReg > 0)  {
    PumpOk = true;
  }
  else PumpOk = false;
}




void SendData() {
  if (millis() > AliveRefreshMillis + 250)  {
    AliveRefreshMillis = millis();
    Serial.print("Alive, ");
    Serial.println(millis());
    // ReceiveCommands();
  }

  String DataMatrix;
  if (millis() > DataRefreshMillis + 2000) {

    DataRefreshMillis = millis();

    DataMatrix = "Commands,";
    DataMatrix = DataMatrix + Append(String(EmgPressed));
    DataMatrix = DataMatrix + Append(String(AuxOk));   // was AuxiliaryOn
    DataMatrix = DataMatrix + Append(String(CompOk)); // was  CompressorAuto
    DataMatrix = DataMatrix + Append(String(WaterLoadAuto));
    DataMatrix = DataMatrix + Append(String(AutomaticReady));
    DataMatrix = DataMatrix + Append(String(AutomaticOn));
    Serial.println(DataMatrix);

    DataMatrix = "Components,";
    DataMatrix = DataMatrix + Append(String(BypassValve));
    DataMatrix = DataMatrix + Append(String(AirValve1));
    DataMatrix = DataMatrix + Append(String(AirValve2));
    DataMatrix = DataMatrix + Append(String(CompValve));
    DataMatrix = DataMatrix + Append(String(WaterValve));
    DataMatrix = DataMatrix + Append(String(MTStopOk));
    DataMatrix = DataMatrix + Append(String(MTSezOk));
    DataMatrix = DataMatrix + Append("");
    DataMatrix = DataMatrix + Append(String(PumpOk));
    DataMatrix = DataMatrix + Append(String(FluxOk));
    DataMatrix = DataMatrix + Append(String(FilterOk));
    DataMatrix = DataMatrix + Append("");
    DataMatrix = DataMatrix + Append(String(StopValves)); //comm13
    for (int i = 1; i < 8; i++)  DataMatrix = DataMatrix + Append(String(Sezi[i]));
    DataMatrix = DataMatrix + Append(String(WaterReady));
    DataMatrix = DataMatrix + Append(String(TankVolume));
    DataMatrix = DataMatrix + Append(String(HpSensor));
    DataMatrix = DataMatrix + Append(String(LpSensor));
    DataMatrix = DataMatrix + Append(String(AirSensor));
    DataMatrix = DataMatrix + Append(String(CompressorPressure));
    DataMatrix = DataMatrix + Append(String(PropReg));
    Serial.println(DataMatrix);
  }

  if (NewCycleData)  {
    DataMatrix = "CycleProgress,";
    DataMatrix = DataMatrix + Append(String(TestCycleSelection));
    DataMatrix = DataMatrix + Append(String(TestPhaseActive));
    DataMatrix = DataMatrix + Append(String(TestPhaseResult));
    DataMatrix = DataMatrix + Append(String(TestCycleResult));
    NewCycleData = false;
    Serial.println(DataMatrix);
  }
}


void ReceiveCommands()  {

  bool newdata = false;
  while (Serial.available()) {
    char inChar = (char)Serial.read();
    mess += inChar;
    if (inChar == '\n') {
      newdata = true;
      Serial.println();
      //   SuperAliveMillis = millis();
    }
  }
  if (newdata)  {
    newdata = false;
    String dataStrings[10];
    String a = "";
    int ch_count = 0;
    int i = 0;
    for (int n = 1; n < mess.length() + 1; n++) { //Search for each message start
      ch_count ++;
      a  +=  mess.substring(n - 1, n);
      if (a.substring(ch_count - 1) == ",")  {
        dataStrings[i] = a.substring(0, ch_count - 1);
        i ++;
        ch_count = 0;
        a = "";
      }
    }
    dataStrings[i] = a;
    mess = "";
    if (dataStrings[0].equals("Commands"))  {
      AuxiliaryOn = (dataStrings[1].toInt()); //Aux
      CompressorAuto = (dataStrings[2].toInt());  //comp
      WaterLoadAuto = (dataStrings[3].toInt());  //water
      AutomaticOn = (dataStrings[4].toInt());  //start cycle
      DataOKforCycle = (dataStrings[5].toInt());  //data ok for test cycle
      //Serial.println("info,Commands");
      DataRefreshMillis = millis() + 250; // delay send command to allow data elaboration
    }
    if (dataStrings[0] == "CycleRecipe")  {
      TestCycleSelection = (dataStrings[1].toInt());
      WashDurata = (dataStrings[2].toInt());
      FluxPartic = (dataStrings[3].toInt());
      PressDurata = (dataStrings[4].toInt());
      PressMax = (dataStrings[5].toInt());
      PressStep = (dataStrings[6].toInt());
      DryDurata = (dataStrings[7].toInt());
      //Serial.println("info,CycleRecipe");
    }
  }
}

void AutoCycle()  {
  if (!AutomaticOn)  {
    if (AutomaticOnBefore)  {
      NewCycleData = true;
      TestCycleResult = 2;
      Phase = 0;
      StopAll();
    }
    AutomaticOnBefore = false;
  }
  if (AutomaticOn)  {
    if (!AutomaticOnBefore) {
      AutoPhaseMillis = millis();
      NewCycleData = true;
      Phase = 0;
      TestValveActive = 1;
      TestPhaseActive = 0;
    }
  }
  if (AutomaticOn) {
    AutomaticOnBefore = true;
    if (millis() > AutoCycleMillis + 100)  {
      AutoCycleMillis = millis();
      if (TestCycleSelection == 1)  Flussaggio();
      if (TestCycleSelection == 2)  AltaPressione();
      if (TestCycleSelection == 3)  Asciugatura();
      if (TestCycleSelection == 4)  CaricoAcqua();
      if (TestCycleSelection == 5)  ScaricoAcqua();
    }
  }
}





void Flussaggio()  {
  TestPhaseResult = 0;
  TestCycleResult = 0;

  // safety checks
  if (HpSensor > 10) { // controll HP pressure, if >8 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso pressione HP - Abort");
  }
  if (LpSensor > 6) { // controll LP pressure, if >6 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso pressione LP - Abort");
  }
  switch (Phase) {
    case 0:
      //initiate the cycle procedure
      AirValve2 = true; // open air to proportional valve
      PropReg = 0.15; // pressure set to 6 bar in HP line
      if (HpSensor > 4) { // controll HP pressure, if >4 then step forward
        AutoPhaseMillis = millis();
        NewCycleData = true;
        TestPhaseResult = 1;
        Phase = 1;
        TestPhaseActive = 0;
        TestValveActive = 1; //valve to be checked
        FluxMillis = millis();
        Serial.println(String("info, Fase di Preparazione OK"));
      }
      if (millis() > AutoPhaseMillis + 20000)  {  // abort if more than 45 sec without 4 bar
        Phase = 20;
        Serial.println("diagnostic, Tempo scaduto fase di Preparazione - Abort");
      }
      break;
    case 1:   //"Fase di flussaggio acqua";
      StopValves = true;  // open stop valves
      Sezi[TestValveActive] = true;   //open the valve to be checked
      PropReg = 0.1;
      if (millis() > AutoPhaseMillis + 60000)  { // abort if more than 60 sec without Flux Sensor OK
        if (!FluxOk)  {
          Phase = 20;
          Serial.println("diagnostic, Tempo scaduto fase di Flussaggio - Abort");
        }
      }
      if (millis() > FluxMillis + 500) { // reiterate if Flux is not constant
        if (!FluxOk)  FluxMillis = millis();
      }
      if (millis() > FluxMillis + 2000) {  // ready for washing phase
        Phase = 2;
        AutoPhaseMillis = millis();
        WashMillis = millis();
        Serial.println(String("info, Flussaggio valvola " + String(TestValveActive) + " OK"));
      }
      break;
    case 2:  //"Fase di Pulizia";
      PropReg = 0.2;
      if (millis() > WashMillis + 500) { // reiterate if Flux is not constant
        if (FilterOk)  WashMillis = millis();
      }
      if (millis() > WashMillis + WashDurata * 60000) { // ready for closing phase
        Phase = 3;
        CloseMillis = millis();
        Serial.println(String("info, Pulizia valvola " + String(TestValveActive) + " OK"));
      }
      if (millis() > AutoPhaseMillis + WashDurata * 10000 * 2)  { // abort if more than 2*washtime without Filter Sensor OK
        Phase = 20;
        Serial.println("diagnostic, Time Tempo scaduto fase di Pulizia - Abort");
      }
      break;
    case 3:  //"Fase di Chiusura";
      Sezi[TestValveActive] = false;   //close the checked valve
      AirValve2 = false; // close air to proportional valve
      PropReg = 0;  //Stop the pump
      if (millis() > CloseMillis + 2000) { // keep pump off for 2 sec
        Serial.println(String("info, Chiusura valvola " + String(TestValveActive) + " OK"));
        TestPhaseActive = TestValveActive;
        TestValveActive++;
        if (TestValveActive > 7)  { // All valves are washed
          Phase = 12;
          TestPhaseResult = 1;
          NewCycleData = true;
        }
        else {
          Phase = 1; //reiterate on the next valve
          NewCycleData = true;
          TestPhaseResult = 1;
          FluxMillis = millis();
        }
      }
      break;
    case 12:  //"Fase di flussaggio Conclusa con successo";
      //Stop pump, close valves etc
      Serial.println("info, Fase di Flussaggio conclusa con successo");
      TestPhaseActive = 8;
      TestCycleResult = 1;
      TestPhaseResult = 1;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
    case 20:  //"Fase Abortita";
      // define procedure to interrupt the cycle (close vaslve stop pumps..)
      //Stop pump, close valves etc
      Serial.println("info, Fase di Flussaggio fallita");
      AirValve2 = false; // close air to proportional valve
      PropReg = 0;  //close prop valve
      StopValves = false;  // close stop valves
      TestCycleResult = 2;
      TestPhaseResult = 2;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
  }
}


void AltaPressione()  {
  TestPhaseResult = 0;
  TestCycleResult = 0;

  // safety checks
  if (HpSensor > PressMax * 1.1) { // controll HP pressure, if >130 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso pressione HP - Abort");
  }
  if (LpSensor > 6) { // controll LP pressure, if >6 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso pressione LP - Abort");
  }
  switch (Phase) {
    case 0:
      //initiate the cycle procedure
      AirValve2 = true; // open air to proportional valve
      PropReg = 0.15;
      if (HpSensor > 4) { // controll HP pressure, if >4 then step forward
        AutoPhaseMillis = millis();
        TestPhaseResult = 1;
        Phase = 1;
        TestPhaseActive = 0;
        NewCycleData = true;
        TestValveActive = 1; //valve to be checked
        FluxMillis = millis();
        Serial.println(String("info, Fase di Preparazione OK"));
      }
      if (millis() > AutoPhaseMillis + 20000)  {  // abort if more than 45 sec without 4 bar
        Phase = 20;
        Serial.println("diagnostic, Tempo Scaduto fase preparazione al ciclo - Abort");
      }
      break;
    case 1:   //"Fase di flussaggio acqua";
      StopValves = true;  // open stop valves
      Sezi[TestValveActive] = true;   //open the valve to be checked
      PropReg = 0.15;
      if (millis() > AutoPhaseMillis + 60000)  { // abort if more than 60 sec without Flux Sensor OK
        if (!FluxOk)  {
          Phase = 20;
          Serial.println("diagnostic, Tempo Scaduto fase di Flussaggio - Abort");
        }
      }
      if (millis() > FluxMillis + 500) { // reiterate if Flux is not constant
        if (!FluxOk)  FluxMillis = millis();
      }
      if (millis() > FluxMillis + 2000) {  // ready for washing phase
        Phase = 2;
        AutoPhaseMillis = millis();
        WashMillis = millis();
        Serial.println(String("info, Fase di Flussaggio - " + String(TestValveActive) + " valvola ok"));
      }
      break;
    case 2:  //"Fase di Chiusura";
      Sezi[TestValveActive] = false;   //close the checked valve
      AirValve2 = false; // close air to proportional valve
      CompValve = false; //compressor valve closed
      PropReg = 0;  //Stop the pump
      if (millis() > CloseMillis + 2000) { // keep pump off for 2 sec
        Serial.println(String("info, Chiusura valvola " + String(TestValveActive) + " ok"));
        TestValveActive++;
        if (TestValveActive > 7)  { // All valves are washed
          Phase = 3;
          TestValveActive = 1;
          PressPhase = 1;
          TestPhaseResult = 1;
          PressEncrMillis = millis();
          TestPhaseActive = 1;
          NewCycleData = true;
          CompValve = true; //open compressor valve
          for (int i = 1; i < 8; i++) Sezi[i] = true; //open all section valves
          StopValves = false; // stop valves closed
          AirValve2 = false; // air valve closed
          CompValve = false; //compressor valve closed
          PropReg = 0.1; // proportional valve closed
          PressEncrMillis = millis();
          Serial.println(String("info, Inizio fase Alta Pressione"));

        }
        else {
          Phase = 1; //reiterate on the next valve
          //  NewCycleData = true;
          // TestPhaseResult = 1;
          FluxMillis = millis();
        }
      }
      break;
    case 3: //"fase di crescita pressione   10% PressMax/PressStep
      if (millis() > PressEncrMillis + 1500) { //wait 1.5 secs each step encrease pressure
        if (HpSensor >= PropReg * 40 * 0.9) { // HP pressure > 90% of reference
          //PropReg = PropReg+(PressMax / PressStep / 10 / 40);   // controllare e sostiruire alla seguente
          PropReg = PropReg + 0.1;
          if (PropReg > 3)  { // change to compressor Valve when pressure higher than 3 bar
            AirValve2 = false;
            CompValve = true;
          }
          Serial.println(String("info, propreg:" + String(PropReg)));
          PressEncrMillis = millis();
        }
      }
      if (HpSensor > PressMax / PressStep * PressPhase) { // pressure reached the step value
        Phase = 4;
        PressStopMillis = millis();
        HpCheck = HpSensor;
        Serial.println(String("info, Mantenimento Alta Pressione: " + String(HpSensor)));

      }
      break;
    case 4: //"fase di mantenimento pressione per tempo PressDurata - tolleranza 10%
      if (millis() > PressEncrMillis + PressDurata * 60000) { //wait given minutes each step encrease pressure
        if (HpSensor > HpCheck * 0.9) { // HP pressure is still > 90%
          PressPhase++;
          if (PressPhase > PressStep) {
            Phase = 12;
            StopValves = true;
            CompValve = false;
            TestPhaseActive = 4;
            TestPhaseResult = 1;
            NewCycleData = true;
            for (int i = 1; i < 8; i++) Sezi[i] = false; //open all section valves
            PropReg = 0;
          }
          else  {
            Phase = 3;
            NewCycleData = true;
            TestPhaseResult = 1;
            TestPhaseActive ++;
          }
        }
        else {
          Phase = 20;
          Serial.println("diagnostic, Perdite eccessive, fase Alta Pressione fallita - Abort");
        }
      }
      break;
    case 12:  //"Fase di Alta pressione Conclusa con successo";
      //Stop pump, close valves etc
      Serial.println("info, Ciclo Alta Pressione terminato con successo");
      TestPhaseActive = 5;
      TestCycleResult = 1;
      TestPhaseResult = 1;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      StopValves = false;
      Phase = 0;
      break;
    case 20:  //"Fase Abortita";
      // define procedure to interrupt the cycle (close vaslve stop pumps..)
      //Stop pump, close valves etc
      Serial.println("info, Ciclo Alta Pressione abortito");
      AirValve2 = false; // close air to proportional valve
      PropReg = 0;  //close prop valve
      StopValves = false;  // close stop valves
      TestCycleResult = 2;
      TestPhaseResult = 2;
      NewCycleData = true;
      AutomaticOn = false;
      for (int i = 1; i < 8; i++) Sezi[i] = false; //close all section valves
      AutomaticOnBefore = false;
      Phase = 0;
      break;
  }
}




void Asciugatura()  {
  TestPhaseResult = 0;
  TestCycleResult = 0;

  // safety checks
  if (HpSensor > 10) { // controll HP pressure, if >8 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso Pressione HP - Abort");
  }
  if (LpSensor > 6) { // controll LP pressure, if >6 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso Pressione LP - Abort");
  }
  switch (Phase) {
    case 0:
      //initiate the cycle procedure
      AirValve1 = true; // open air to Hp line
      if (HpSensor > 4) { // controll HP pressure, if >4 then step forward
        AutoPhaseMillis = millis();
        NewCycleData = true;
        TestPhaseResult = 1;
        Phase = 1;
        TestPhaseActive = 0;
        TestValveActive = 1; //first valve to be checked
        FluxMillis = millis();
        Serial.println(String("info, Fase di Preparazione OK"));
      }
      if (millis() > AutoPhaseMillis + 20000)  {  // abort if more than 45 sec without 4 bar
        Phase = 20;
        Serial.println("diagnostic, Tempo scaduto fase di preparazione - Abort");
      }
      break;
    case 1:   //"Fase di Svuotamento";
      StopValves = true;  // open stop valves
      Sezi[TestValveActive] = true;   //open the valve to be checked
      if (millis() > AutoPhaseMillis + 60000)  { // abort if more than 60 sec without LP Sensor OK
        if (LpSensor < 1)  {
          Phase = 20;
          Serial.println("diagnostic, Tempo scaduto fase di Svuotamento - Abort");
        }
      }
      if (millis() > FluxMillis + 500) { // reiterate if Flux still detect water flow
        if (FluxOk)  FluxMillis = millis();
      }
      if (millis() > FluxMillis + 2000) {  // ready for drying phase
        Phase = 2;
        AutoPhaseMillis = millis();
        DryMillis = millis();
        Serial.println(String("info, Pulizia valvola " + String(TestValveActive) + " OK"));
      }
      break;
    case 2:  //"Fase di Asciugatura";
      if (millis() > DryMillis + 500) { // reiterate if Flux is not constant
        if (LpSensor < 1)  DryMillis  = millis();
      }
      if (millis() > DryMillis + DryDurata * 1000) { // ready for closing phase
        Phase = 3;
        CloseMillis = millis();
        Serial.println(String("info, Asciugatura valvola" + String(TestValveActive) + " OK"));
      }
      if (millis() > AutoPhaseMillis + DryDurata * 10000 * 2)  { // abort if more than 2*drytime without Filter Sensor OK
        Phase = 20;
        Serial.println("diagnostic, Tempo scaduto fase di Asciugatura - Abort");
      }
      break;
    case 3:  //"Fase di Chiusura";
      Sezi[TestValveActive] = false;   //close the checked valve
      AirValve1 = false; // close air to Hp line
      if (millis() > CloseMillis + 2000) { // keep Air flow off for 2 sec
        Serial.println(String("info, Chiusura valvola " + String(TestValveActive) + " OK"));
        TestPhaseActive = TestValveActive;
        TestValveActive++;
        if (TestValveActive > 7)  { // All valves are dry
          Phase = 12;
          TestPhaseResult = 1;
          NewCycleData = true;
        }
        else {
          Phase = 1; //reiterate on the next valve
          NewCycleData = true;
          TestPhaseResult = 1;
          AirValve1 = true;
          FluxMillis = millis();
        }
      }
      break;
    case 12:  //"Fase di Asciugatura Conclusa con successo";
      //Stop pump, close valves etc
      Serial.println("info, Fase di Asciugatura terminata con successo");
      TestPhaseActive = 8;
      TestCycleResult = 1;
      TestPhaseResult = 1;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
    case 20:  //"Fase Abortita";
      // define procedure to interrupt the cycle (close valve stop pumps..)
      //Stop pump, close valves etc
      Serial.println("info, Fase di Asciugatura fallita");
      AirValve1 = false; // close air to proportional valve
      for (int i = 1; i < 8; i++) Sezi[i] = false; //close all section valves
      PropReg = 0;  //close prop valve
      StopValves = false;  // close stop valves
      TestCycleResult = 2;
      TestPhaseResult = 2;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
  }
}




void CaricoAcqua()  {
  TestPhaseResult = 0;
  TestCycleResult = 0;

  // safety checks
  if (HpSensor > 10) { // controll HP pressure, if >8 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso pressione linea HP - Abort");
  }
  if (LpSensor > 6) { // controll LP pressure, if >6 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso di pressione linea LP - Abort");
  }
  switch (Phase) {
    case 0:
      //initiate the cycle procedure
      AirValve2 = true; // open air to proportional valve
      PropReg = 0.15;
      if (HpSensor > 4) { // controll HP pressure, if >4 then step forward
        AutoPhaseMillis = millis();
        NewCycleData = true;
        TestPhaseResult = 1;
        Phase = 1;
        TestPhaseActive = 0;
        TestValveActive = 1; //valve to be checked
        FluxMillis = millis();
        Serial.println(String("info, Fase di preparazione OK"));
      }
      if (millis() > AutoPhaseMillis + 20000)  {  // abort if more than 45 sec without 4 bar
        Phase = 20;
        Serial.println("diagnostic, Tempo scaduto fase di preparazione - Abort");
      }
      break;
    case 1:   //"Fase di flussaggio acqua";
      StopValves = true;  // open stop valves
      Sezi[TestValveActive] = true;   //open the valve to be checked
      PropReg = 0.1;
      if (millis() > AutoPhaseMillis + 60000)  { // abort if more than 60 sec without Flux Sensor OK
        if (!FluxOk)  {
          Phase = 20;
          Serial.println("diagnostic, Tempo scaduto fase di Flussaggio - Abort");
        }
      }
      if (millis() > FluxMillis + 500) { // reiterate if Flux is not constant
        if (!FluxOk)  FluxMillis = millis();
      }
      if (millis() > FluxMillis + 2000) {  // ready for washing phase
        Phase = 2;
        AutoPhaseMillis = millis();
        WashMillis = millis();
        Serial.println(String("info, Fase Flussaggio valvola " + String(TestValveActive) + " OK"));
      }
      break;
    case 2:  //"Fase di Carica";
      Phase = 3;
      CloseMillis = millis();
      Serial.println(String("info, Fase di Carico acqua valvola " + String(TestValveActive) + " OK"));
      break;
    case 3:  //"Fase di Chiusura";
      Sezi[TestValveActive] = false;   //close the checked valve
      AirValve2 = false; // close air to proportional valve
      PropReg = 0;  //Stop the pump
      if (millis() > CloseMillis + 2000) { // keep pump off for 2 sec
        Serial.println(String("info, Fase di Chiusura valvola " + String(TestValveActive) + " OK"));
        TestPhaseActive = TestValveActive;
        TestValveActive++;
        if (TestValveActive > 7)  { // All valves are loaded
          Phase = 12;
          TestPhaseResult = 1;
          NewCycleData = true;
        }
        else {
          Phase = 1; //reiterate on the next valve
          NewCycleData = true;
          TestPhaseResult = 1;
          FluxMillis = millis();
        }
      }
      break;
    case 12:  //"Fase di flussaggio Conclusa con successo";
      //Stop pump, close valves etc
      Serial.println("info, Ciclo di Caricamento Acqua concluso con successo");
      TestPhaseActive = 8;
      TestCycleResult = 1;
      TestPhaseResult = 1;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
    case 20:  //"Fase Abortita";
      // define procedure to interrupt the cycle (close vaslve stop pumps..)
      //Stop pump, close valves etc
      Serial.println("info, Ciclo di Caricamento Acqua fallito");
      AirValve2 = false; // close air to proportional valve
      PropReg = 0;  //close prop valve
      StopValves = false;  // close stop valves
      TestCycleResult = 2;
      TestPhaseResult = 2;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
  }
}



void ScaricoAcqua()  {
  TestPhaseResult = 0;
  TestCycleResult = 0;

  // safety checks
  if (HpSensor > 10) { // controll HP pressure, if >8 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso di pressione linea HP - Abort");
  }
  if (LpSensor > 6) { // controll LP pressure, if >6 then Abort
    PropReg = 0;
    Phase = 20;
    Serial.println("diagnostic, Eccesso di pressione linea LP - Abort");
  }
  switch (Phase) {
    case 0:
      //initiate the cycle procedure
      AirValve1 = true; // open air to Hp line
      if (HpSensor > 4) { // controll HP pressure, if >4 then step forward
        AutoPhaseMillis = millis();
        NewCycleData = true;
        TestPhaseResult = 1;
        Phase = 1;
        TestPhaseActive = 0;
        TestValveActive = 1; //first valve to be checked
        FluxMillis = millis();
        Serial.println(String("info, Fase di preparazione OK"));
      }
      if (millis() > AutoPhaseMillis + 20000)  {  // abort if more than 45 sec without 4 bar
        Phase = 20;
        Serial.println("diagnostic, Tempo scaduto fase di preparazione - Abort");
      }
      break;
    case 1:   //"Fase di Svuotamento";
      StopValves = true;  // open stop valves
      Sezi[TestValveActive] = true;   //open the valve to be checked
      if (millis() > AutoPhaseMillis + 60000)  { // abort if more than 60 sec without LP Sensor OK
        if (LpSensor < 1)  {
          Phase = 20;
          Serial.println("diagnostic, Tempo scaduto fase di Svuotamento - Abort");
        }
      }
      if (millis() > FluxMillis + 500) { // reiterate if Flux still detect water flow
        if (FluxOk)  FluxMillis = millis();
      }
      if (millis() > FluxMillis + 2000) {  // ready for drying phase
        Phase = 2;
        AutoPhaseMillis = millis();
        DryMillis = millis();
        Serial.println(String("info, Svuotamento valvola " + String(TestValveActive) + " OK"));
      }
      break;
    case 2:  //"Fase di Asciugatura";
      if (millis() > DryMillis) { // ready for closing phase
        Phase = 3;
        CloseMillis = millis();
        Serial.println(String("info, Dry phase " + String(TestValveActive) + " valve ok"));
      }
      if (millis() > AutoPhaseMillis + DryDurata * 10000 * 2)  { // abort if more than 2*drytime without LP Sensor OK
        Phase = 20;
        Serial.println("diagnostic, Time Over Dry Phase - Abort");
      }
      break;
    case 3:  //"Fase di Chiusura";
      Sezi[TestValveActive] = false;   //close the checked valve
      AirValve1 = false; // close air to Hp line
      if (millis() > CloseMillis + 2000) { // keep Air flow off for 2 sec
        Serial.println(String("info, Chiusura valvola " + String(TestValveActive) + " OK"));
        TestPhaseActive = TestValveActive;
        TestValveActive++;
        if (TestValveActive > 7)  { // All valves are empty
          Phase = 12;
          TestPhaseResult = 1;
          NewCycleData = true;
        }
        else {
          Phase = 1; //reiterate on the next valve
          NewCycleData = true;
          TestPhaseResult = 1;
          AirValve1 = true;
          FluxMillis = millis();
        }
      }
      break;
    case 12:  //"Fase di Asciugatura Conclusa con successo";
      //Stop pump, close valves etc
      Serial.println("info, Fase di Svuotamento conclusa con successo");
      TestPhaseActive = 8;
      TestCycleResult = 1;
      TestPhaseResult = 1;
      NewCycleData = true;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
    case 20:  //"Fase Abortita";
      // define procedure to interrupt the cycle (close valve stop pumps..)
      //Stop pump, close valves etc
      Serial.println("info, Fase di Svuotamento fallita");
      AirValve1 = false; // close air to proportional valve
      for (int i = 1; i < 8; i++) Sezi[i] = false; //close all section valves
      PropReg = 0;  //close prop valve
      StopValves = false;  // close stop valves
      TestCycleResult = 2;
      TestPhaseResult = 2;
      AutomaticOn = false;
      AutomaticOnBefore = false;
      Phase = 0;
      break;
  }
}


void StopAll() {
  Serial.println("info, Tutte le valvole in posizione sicura");
  BypassValve = false;
  AirValve1 = false; // close air to proportional valve
  AirValve2 = false; // close air to proportional valve
  CompValve = false;
  WaterValve = false;
  StopValves = false;
  for (int i = 1; i < 8; i++) Sezi[i] = false;
  PropReg = 0;  //close prop valve
  StopValves = false;  // close stop valves
}





//*******************************************************************
//***************** Field simulation functions **********************
//*******************************************************************

void EmgCycle()   {   //Field simulation
  if (millis() > EmgCycleMillis + StepEmg)  {
    AutoCycleMillis = millis();
    switch (Phase) {
      case 0:
        EmgPressed = true;
        Phase++;
        StepEmg = 3000;
        break;
      case 1:
        EmgPressed = false;
        Phase++;
        StepEmg = 10000;
        break;
      case 2:
        //  EmgPressed = true;
        Phase++;
        StepEmg = 5000;
        break;
      case 3:
        //  CompressorPressure = 4;

        Phase++;
        StepEmg = 3000;
        break;
      case 4:
        // TankVolume = 30;

        Phase ++;
        StepEmg = 1000;
        break;
    }
  }
}



void LoadWater() {  // field Simulation
  if (WaterValve)  {
    TankVolume += 3;
  }
}

void LoadAir() {  // field Simulation
  if (CompressorAuto)  {
    if (CompressorPressure < 20)
      CompressorPressure += 1;
  }
}

void DryAir() {  // field Simulation
  if (AirValve1) HpSensor = 6;
}


void PumpWater() {  // field Simulation
  if (PropReg > 0)  {
    PumpOk = true;
    TankVolume -= 3;
  }
  else PumpOk = false;
  AirSensor = PropReg;
  HpSensor = AirSensor * 40;
}

void LPressure()  {  // field Simulation
  if (HpSensor != 0)  {
    if (StopValves) {
      for (int i = 1; i < 8; i++) {
        if (Sezi[i]) {
          LpSensor = 2;
        }
      }
    }
  }
}

void FluxWater()  {  // field Simulation
  FluxOk = false;
  LpSensor = 0;
  if (PumpOk) {
    if (StopValves) {
      for (int i = 1; i < 8; i++) {
        if (Sezi[i]) {
          FluxOk = true;
          TankVolume += 1;

        }
      }
    }
  }
}

void FilterWater()  {  // field Simulation
  FilterOk = false;
  if (FluxOk) FilterOk = true;
}

String Append(String data)  {
  return data + ",";
}
