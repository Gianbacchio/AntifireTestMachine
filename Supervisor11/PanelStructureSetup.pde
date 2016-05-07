
void PanelStructureSetup() {

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
  for (int i=0; i<3; i++) {  // add here more recipes 
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
    +"  è assente il collegamento tra supervisore e controllore della macchina." 
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
    +"   Per avviare il processo di test è necessario eseguire le seguenti operazioni:"
    +"\n\n"
    +" - Verificare il corretto montaggio di tutte le parti che costituiscono \n"
    +"   l'installazione ed eseguire la check list.\n"
    +" - Confermare l'esecuzione della check list clickando il pulsante 'Check List'\n"
    +" - Attivare i servizi Ausiliari\n"
    +" - Attivare il compressore in modalità \"AUTO\"\n"
    +" - Caricare, se necessario, acqua nel serbatoio collegandolo all'impianto idrico\n"
    +"   e attivando il relativo comando 'AUTO'\n"
    +" - Impostare i dati relativi al test in esecuzione"
    +"\n\n"
    +" Quando si saranno eseguite tutte queste operazioni,\n"
    +" si potrà selezionare la fase di test da mandare in esecuzione."
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
    +"  Il sensore di particolato segnala la presenza di impurità e il filtro le raccoglie.\n"
    +"  La fase di Flussaggio si completa al termine del tempo predefinito.\n"
    +"  Il ciclo è diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
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
    +"  il TextBox giudicherà il ciclo di Alta Pressione concluso con successo.\n"
    +"  In alternativa, il TextBox segnalerà la presenza di perdite importanti e sarà necessario\n"
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
    +"  Il ciclo è diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
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
    +"  Il ciclo è diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
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
    +"  Il ciclo è diviso in 11 parti, una per ciascuna valvola di sezionamento montata\n"
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
    .setSize(35, 215)
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
    .setStrokeWeight(1.5)
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
    .setStrokeWeight(1.5)
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
    .setStrokeWeight(1.5)
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
  WaterLevel=c5.addButton("Livello_Acqua")
    .setSize(150, int(int(Component[22][1])*2.8))
    .setPosition(141, 220-int(Component[22][1])*2.8)
    .setColorBackground(color(0,100, 200))
    .setGroup("g5")
    ;
   WaterOkLit=c5.addButton("WaterOkLit")
    .setSize(150, int(int(Component[22][1])*2.8))
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