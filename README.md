OrCamo
======
**Orca Camouflage** hides the like button in Facebook's Android Messenger.

Purpose: prevent me from accidentally touching the button when picking up the phone.

**NOTE:** tested only on Samsung Galaxy SII Plus (I9105P) with TouchPal keyboard third party app (the positions reported by the `AccesibilityNodeInfo` are not correct when the keyboard is shown, so a workaroudn was necessary and if you use another keyboard, the numbers may not add up and the camouflage may be mispositioned).

**Permission notes:**
The app needs some special permissions since I have to know which window is currently active and I have to draw over other windows (for details please see the file `Androidmanifest.xml`).

In order to use the app you have to activate the accessibility service:
(On Android 4.2.2) Go to `Settings -> My Device -> Accessibility -> OrCamo (in the Services category)` and activate it.

License (BSD 3)
===============

Copyright (c) 2015, Barbu Paul - Gheorghe
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Barbu Paul - Gheorghe nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
