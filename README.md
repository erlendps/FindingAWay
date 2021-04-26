# Prosjekt-repo for TDT4100

This repo contains the .java and .fxml files for the project in TDT4100 - Object-Oriented programming.

The project is a tile-based puzzle game, where the player needs to get from one place to another. In order to do so, the player can move freely around the level. The player needs to use their surroundings and objects in the level to reach the goal. It also has an level-editor, so that the user can use their own imagination to create a custom level.

## For stud.asser og und.asser
Jeg har hentet litt inspirasjon fra eksempelet "snakebird". Dette er ikke mye, men det er noen metoder som heter det samme (men det er mest fordi jeg er lite kreativ når det kommer til navn:)). For eksempel er innkapsling av "move"-metoder ganske likt (med at man har public moveLeft/Right metoder og en private move(int dx) metode som blir kalt på av de publice), men selve implentasjonen og logikken er en god del annerledes. Hvordan spilleren beveger seg på i mitt spill for eksempel, er annerledes for hvordan spilleren i snakebird beveger seg.

Litt kode i kontroller-klassen er også litt likt, for eksempel hvordan man oversetter brettet til grafikk.

Det er brukt 2 kontrollere og fxml-filer. Implentasjonen av dette er inspirert fra todo-list eksempelet. 

Klassen FolderReaderHelper gjør ingenting for spillet og skal ikke være en del av prosjektet. Den var tidligere brukt som en del av prosjektet, men etter at jeg ble oppmerksom på at løsningen min for lagring og henting var ugyldig, falt denne klassen bort. Den ligger fortsatt i repoet grunnet jeg ikke vil slette den.

For testene brukes 3 filer, invalid_tile.txt, invalid_playermodel.txt og invalid_width.txt for å teste filbehandling. For at testene skal kjøre riktig må appen åpnes først en gang, og deretter legge til filene i ~/tdt4100/FindingAWay/saves/
