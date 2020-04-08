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
import time
import os
import java.net
import signal

from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice, MonkeyImage, MonkeyView

# import json
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

CONNECTION_WAIT_DELAY = 5
ADB_EXE = ""

CMD_MAP = {
    'TOUCH': lambda dev, arg: dev.touch(**arg),
    'DRAG': lambda dev, arg: dev.drag(**arg),
    'PRESS': lambda dev, arg: dev.press(**arg),
    'TYPE': lambda dev, arg: dev.type(**arg),
    'WAIT': lambda dev, arg: time.sleep(float(**arg))
}


# Process a single file for the specified device.
def process_file(fp, device, app, insert_wait, sleeptime):
    data = {}
    data['views'] = []

    for line in fp:
        if not "WAIT" in line:
            os.system("%s logcat -d" % (ADB_EXE))

        if "ROTATE" in line:
            enable_rotate = "%s shell settings put system accelerometer_rotation 0" % (ADB_EXE)
            rotate_90 = "%s shell settings put system user_rotation 1" % (ADB_EXE)
            disable_rotate = "%s shell settings put system accelerometer_rotation 1" % (ADB_EXE)

            time.sleep(sleeptime)
            os.system(enable_rotate)
            os.system(rotate_90)
            time.sleep(sleeptime)
            os.system(disable_rotate)
            continue
        if "adb" in line:
            time.sleep(sleeptime)
            os.system(line)
            time.sleep(sleeptime)
            continue
        (cmd, rest) = line.split('|')

        try:
            # Parse the pydict
            rest = eval(rest)
            if cmd == "WAIT":
                time.sleep(float(rest["seconds"]))
                insert_wait = False
                continue
            elif cmd not in CMD_MAP:
                continue
        except:
            continue

        if insert_wait:
            time.sleep(sleeptime)
        CMD_MAP[cmd](device, rest)

    time.sleep(sleeptime)
    os.system("%s shell am force-stop %s" % (ADB_EXE, app))
    time.sleep(sleeptime)

    # Home button press
    device.press('KEYCODE_HOME', 'DOWN_AND_UP')
    device.shell('killall com.android.commands.monkey')


def exit_gracefully(signum, frame):
    print
    "Gracefully exiting"

    signal.signal(signal.SIGINT, signal.getsignal(signal.SIGINT))
    os.system("%s kill-server" % (ADB_EXE))
    sys.exit(1)


def main():
    global ADB_EXE

    sleep_time = 5.0
    if len(sys.argv) == 0:
        print "Usage: instructions-file package-name adb-exe-path [sleep time]"
        sys.exit(1)
    file = sys.argv[1]
    app = sys.argv[2]
    ADB_EXE = sys.argv[3]
    if len(sys.argv) > 4:
        sleep_time = float(sys.argv[4])

    print "SLEEPTIME: %f" % (sleep_time)
    insert_wait = True
    f = open(file, 'r')

    save = False
    touch_list = []
    for line in f:
        if save:
            (cmd, rest) = line.split('|')
            # Parse the pydict
            rest = eval(rest)
            if "TOUCH" in line:
                print
                "TOUCH:" + str(rest)
                touch_list.append((rest['x'], rest['y'], rest['type']))
        if "FAST" in line:
            save = True

        if "WAIT" in line:
            insert_wait = False
    f.close()
    fp = open(file, 'r')
    signal.signal(signal.SIGINT, exit_gracefully)

    os.system("%s shell am force-stop %s" % (ADB_EXE, app))

    time.sleep(1)

    os.system("%s shell monkey -p %s -c android.intent.category.LAUNCHER 1" % (ADB_EXE, app))
    time.sleep(sleep_time)
    device = MonkeyRunner.waitForConnection(CONNECTION_WAIT_DELAY)
    if device is None:
        os.system("%s kill-server" % (ADB_EXE))
        time.sleep(sleep_time)
        device = MonkeyRunner.waitForConnection(CONNECTION_WAIT_DELAY)

    try:
        device.wake()
        print "DEVICE AWAKEN"
    except java.lang.NullPointerException, e:
        print "ERROR: Couldn't connect"
        os.system("%s kill-server" % (ADB_EXE))
        time.sleep(sleep_time)
        device = MonkeyRunner.waitForConnection(CONNECTION_WAIT_DELAY)
    if save:
        for (x, y, t) in touch_list:
            print
            "Sending: %s" % (str((x, y, t)))
            time.sleep(0.5)
            device.touch(x, y, 'DOWN_AND_UP')
    else:
        process_file(fp, device, app, insert_wait, sleep_time)
    fp.close()
    sys.exit(0)


if __name__ == '__main__':
    main()
