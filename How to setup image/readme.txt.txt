************************************************************
procedura per creare una immagine di Raspbian con processing
************************************************************

- l'immagine da usare è :
015-11-21-raspbian-jessie
/(questa immagine è specificamente suggerita da Processing e ha il software già installato)
https://github.com/processing/processing/wiki/Raspberry-Pi

- installare l'immagine sulla sd card usando 
Win32DiskImager-0.9.5

- avviare raspberry con la nuova immagine.

- connettere al network
(usare GUI in alto a dx. Rclick per scegliere SSID)

- configurare la tastiera
(menu, preferencs, keyboard)

- installare il package controlP5
(http://www.sojamo.de/libraries/controlP5/)

- caricare gli script python
off_button_Sema.py
(il sistema di autospegnimento funziona con un +3,3V dato all'input 7 - 
la configurazione pinout è a questa pagina: https://www.raspberrypi.org/documentation/usage/gpio-plus-and-raspi2/)
auto_send_email.py

- inserire nelle procedure di boot gli script:
'nano .profile' 
aggiungere qui il lancio dei batch python e di supervisor:
"bash /home/pi/Desktop/Supervisor/launcher.sh"
in questo modo parte al posto dell'ambiente grafico
					
					