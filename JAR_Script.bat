:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: SCRIPT CST8221 - Winter 2025
:: ---------------------------------------------------------------------
:: Begin of Script (Labs - W24)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

:: Some of the below variables will need to be changed.
:: Remember to always use RELATIVE paths.

:: If your code needs no external libraries, remove all references to LIBDIR
:: and modulepath in this script.

:: You will also need to change MAINCLASSSRC (main class src) to point to
:: the file that contains main(), and MAINCLASSBIN (main class bin) needs to
:: point to the full class name of that class.  This means fullpackage.classname

:: Finally, recall that this file must end in .bat.  It has been renamed to .txt
:: to make it easier for you to edit.

:: As always, ask your lab prof if you have any questions or issues.

SET LIBDIR=lib
SET SRCDIR=src
SET BINDIR=bin
SET BINERR=labs-javac.err
SET JARNAME=JAPLabs_UI.jar
SET JAROUT=labs-jar.out
SET JARERR=labs-jar.err
SET DOCDIR=doc
SET DOCPAC=GUI_APP
SET DOCERR=labs-javadoc.err
SET MAINCLASSSRC=src/gui_app/CrasyEigthsGame.java
SET MAINCLASSBIN=gui_app.CrasyEigthsGame
SET RESOURCEDIR=resources

@echo off

ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @         WINTER  -  2 0 2 5         @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((                                     @"
ECHO "@    ((((((((((((((((((((() ))                                      @"
ECHO "@         ((((((((((((((((()                                        @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

ECHO "[LABS SCRIPT ---------------------]"

ECHO "1. Compiling ......................"
javac -Xlint -cp ".;%SRCDIR%;%LIBDIR%/*" %MAINCLASSSRC% -d %BINDIR% 2> %BINERR%

ECHO "Copying resources to bin directory..."
xcopy /i /e /y %RESOURCEDIR% %BINDIR%\%RESOURCEDIR%

ECHO "2. Creating Jar ..................."
cd bin
jar cvfe %JARNAME% %MAINCLASSBIN% . > ../%JAROUT% 2> ../%JARERR%

ECHO "3. Creating Javadoc ..............."
cd ..
javadoc -cp ".;%BINDIR%;/*" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% 2> %DOCERR%

cd bin
ECHO "4. Running Jar ...................."
start java -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (Labs - W25)
:: ---------------------------------------------------------------------