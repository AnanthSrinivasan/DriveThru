# DriveThru
Order drive through 


Installation instructions:

Install Brew: </br>
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

Install Maven: </br>
brew update </br>
brew install maven </br>

Adding the maven to your PATH: </br>
Add the below line to ~/.bashrc </br>
export PATH=$PATH:/usr/local/Cellar/maven/3.3.9/bin </br>

Command to build the Alexa app and get a jar with dependencies built, </br>
mvn assembly:assembly -DdescriptorId=jar-with-dependencies package </br>

Lombok Setup in eclipse: </br>
1. Install lombok.jar from here. https://projectlombok.org/download.html </br>
2. Run java –jar lombok.jar </br>
3. It will open up a dialog and will ask for your eclipse installation folder. </br>
4. select your eclipse installation, it will be something like  /Users/sananth/Downloads/Eclipse.app/Contents/Eclipse/eclipse.ini </br>
5. Click on Install/Update </br>
6. Restart your eclipse. </br>

Note: Even after that if you see red marks then go to Projects —> Clean. This will get your Lombok setup with eclipse. 


