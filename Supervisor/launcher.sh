
# questo script lancia le applicazioni necessarie per 
# eseguire Supervisor

cd /
cd home/pi/Desktop/Supervisor
sudo python off_button_Sema.py &
sudo python send_mail_from_gmail.py &
cd /