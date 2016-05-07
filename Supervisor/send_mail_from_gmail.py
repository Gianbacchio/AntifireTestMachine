import smtplib
import os
import time, threading

from email.MIMEMultipart import MIMEMultipart
from email.MIMEBase import MIMEBase
from email import Encoders
from email.mime.text import MIMEText

def EmailSender():
        
    fromaddr = 'sematestmachine@gmail.com'
    #toaddrs  = ['giancarlo.bacchio@gmail.com','bacchio.giancarlo@gmail.com']
    toaddrs  = ['giancarlo.bacchio@gmail.com']


    # Credentials (if needed)
    username = fromaddr
    password = '080400Ss'

    COMMASPACE = ', '

    for dirname, dirnames, filenames in os.walk('/home/pi/Desktop/Supervisor/report'):
         # print path to all filenames.
        for filename in filenames:
            msg = MIMEMultipart()
            msg['Subject'] = 'Report delivery: '+filename
            msg['From'] = fromaddr
            msg['To'] = COMMASPACE.join(toaddrs)
            msg.preamble = 'New Report delivered'
            msg.attach(MIMEText("New Report delivered \r\n don't reply to this email"))
            print(os.path.join(dirname, filename))        
            part = MIMEBase('application', "octet-stream")
            part.set_payload(open(os.path.join(dirname, filename), "rb").read())
            Encoders.encode_base64(part)
            part.add_header('Content-Disposition', 'attachment; filename="'+filename+'"')
            msg.attach(part)
            os.remove(os.path.join(dirname, filename))
            # The actual mail send
            server = smtplib.SMTP('smtp.gmail.com:587')
            server.ehlo()
            server.starttls()
            server.login(username,password)
            server.sendmail(fromaddr, toaddrs, msg.as_string())
            server.quit()

    threading.Timer(10, EmailSender).start()

EmailSender()
