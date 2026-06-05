@echo off
set ANDROID_AVD_HOME=D:\Android\.android\avd
set ANDROID_HOME=D:\Android\Sdk
cd /d D:\Android\Sdk\emulator
set PATH=D:\Android\Sdk\platform-tools;D:\Android\Sdk\emulator;D:\Android\Sdk\emulator\lib64;D:\Android\Sdk\emulator\lib64\gles_swiftshader;D:\Android\Sdk\emulator\lib64\vulkan;%PATH%

echo Iniciando emulador...
start "Emulador" emulator.exe -avd Pixel_6_API_35 -no-snapshot-load -gpu swiftshader_indirect -show-kernel

echo Aguardando emulador...
adb wait-for-device

echo Instalando APK...
adb install -r "D:\Estrutura Open Code\Projeto Androids\AppCOPA\app\build\outputs\apk\debug\app-debug.apk"

echo Abrindo app...
adb shell am start -n com.exemplo.copa2026/.MainActivity

echo Pronto!
pause
