#!/usr/bin/env monkeyrunner
# Copyright 2010, The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import sys
import subprocess
import time
import os
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice, MonkeyImage, MonkeyView


import os
import java.net
import signal
#import json
# The format of the file we are parsing is very carfeully constructed.
# Each line corresponds to a single command.  The line is split into 2
# parts with a | character.  Text to the left of the pipe denotes
# which command to run.  The text to the right of the pipe is a python
# dictionary (it can be evaled into existence) that specifies the
# arguments for the command.  In most cases, this directly maps to the
# keyword argument dictionary that could be passed to the underlying
# command. 

# Lookup table to map command strings to functions that implement that
# command.

ADB_EXE = ""
# TODO format adb commands only once. Currently they are formatted many times in loops

CMD_MAP = {
    'TOUCH': lambda dev, arg: dev.touch(**arg),
    'DRAG': lambda dev, arg: dev.drag(**arg),
    'PRESS': lambda dev, arg: dev.press(**arg),
    'TYPE': lambda dev, arg: dev.type(**arg),
    'WAIT': lambda dev, arg: time.sleep(float(**arg))
    }



# Process a single file for the specified device.
def process_file(fp, device, app, outputDir, snapshot, insert_wait, sleeptime):
    index=0 
    snapshot = False
    data = {}
    data['views'] = []
    #os.system("%s logcat -c" % (ADB_EXE))
     
    
    for line in fp:
        if not "WAIT" in line:
            os.system("%s logcat -d" % (ADB_EXE))

        if "ROTATE" in line:
              print "ROTATE"
              enable_rotate = "%s shell settings put system accelerometer_rotation 0" % (ADB_EXE)
              rotate_90="%s shell settings put system user_rotation 1" % (ADB_EXE)
              disable_rotate = "%s shell settings put system accelerometer_rotation 1" % (ADB_EXE)

              time.sleep(6.0)
              #os.system("%s logcat -d" % (ADB_EXE))
              os.system(enable_rotate)
              os.system(rotate_90)
              time.sleep(2.0)
              os.system(disable_rotate)
              continue
        if "adb" in line:
              print "%s command: %s" % (ADB_EXE, line)
              time.sleep(6.0)
              os.system(line) 
              time.sleep(2.0)
              continue
        (cmd, rest) = line.split('|')
        
        try:
            # Parse the pydict
            rest = eval(rest)
            if cmd == "WAIT":
               time.sleep(float(rest["seconds"]))
               insert_wait=False
               continue 
            elif cmd not in CMD_MAP:
               print 'unknown command: ' + cmd
               continue
        except:
            print 'unable to parse options'
            continue 
        
        if insert_wait:
                 time.sleep(sleeptime)
        CMD_MAP[cmd](device, rest)
         
    time.sleep(8)
    #os.system("%s logcat -d" % (ADB_EXE))
    os.system("%s shell am force-stop %s" % (ADB_EXE, app))
    #os.system("%s logcat -c" % (ADB_EXE))
    time.sleep(3)
    # Home button press
    device.press('KEYCODE_HOME','DOWN_AND_UP')
    device.shell('killall com.android.commands.monkey')
    

def exit_gracefully(signum, frame):
    print "Gracefully exiting"

    signal.signal(signal.SIGINT, signal.getsignal(signal.SIGINT))
    os.system("%s kill-server" % (ADB_EXE))
    sys.exit(1)



def main():
    global ADB_EXE

    outputDir = ""
    sleeptime = 4.0
    if len(sys.argv)==0:
          print "usage: <apk-path> <package-name> <config-file-path> <optional: outputDir> <optional: pause-time(default is 5s)>"
          sys.exit(1)
    file = sys.argv[1]
    app = sys.argv[2]
    ADB_EXE = sys.argv[3]


    if len(sys.argv) > 5:
          sleeptime = float(sys.argv[4]) 
    if len(sys.argv) > 3:
         outputDir = sys.argv[4]
    elif len(sys.argv) > 4:
         outputDir = os.getcwd()

    print "SLEEPTIME: %f" % (sleeptime)
    insert_wait = True
    f = open(file, 'r')




    save = False
    touchlist=[]
    for line in f:
              if save:
                     (cmd, rest) = line.split('|')
                     # Parse the pydict
                     rest = eval(rest)
                     if "TOUCH" in line:
                        print "TOUCH:"+str(rest) 
                        touchlist.append((rest['x'],rest['y'],rest['type']))
              if "FAST" in line:
                   save = True
                                      
              if "WAIT" in line:
                   insert_wait= False
    f.close()
    fp = open(file, 'r')
    signal.signal(signal.SIGINT, exit_gracefully)

    os.system("%s shell am force-stop %s" % (ADB_EXE, app))

    time.sleep(1)
    #os.system("%s logcat -c" % (ADB_EXE))
    
    #os.system("%s logcat -d" % (ADB_EXE))
    
    os.system("%s shell monkey -p %s -c android.intent.category.LAUNCHER 1" % (ADB_EXE, app))
    #os.system("%s logcat -d" % (ADB_EXE))
    print "Waiting for: %s. Output in: %s" % (app, outputDir)
    time.sleep(sleeptime)
    device = MonkeyRunner.waitForConnection(10)
    if device is None:
           print "NULL DEVICE" 
           os.system("%s kill-server" % (ADB_EXE))
           time.sleep(3)
           device = MonkeyRunner.waitForConnection(15)
           
    try:
           device.wake()
           print "DEVICE AWAKEN"
    except java.lang.NullPointerException, e:
           print "ERROR: Couldn't connect"
           os.system("%s kill-server" % (ADB_EXE))
           time.sleep(3)
           device = MonkeyRunner.waitForConnection(15)
    if save:
         for (x,y,t) in touchlist:
           print "sending:" + str((x,y,t))
           time.sleep(0.5)
           device.touch(x, y, 'DOWN_AND_UP')
    else:
         #os.system("%s logcat -d" % (ADB_EXE))
         process_file(fp, device, app, outputDir, True, insert_wait, sleeptime)
    fp.close();
    sys.exit(0);
       

if __name__ == '__main__':
    main()



