# README #

Avans Pong, een simpele Android game met update/draw game loop en networking. Deze game kan als basis worden gebruikt voor andere geanimeerde games.

### What is this repository for? ###

De game demonstreert:

* een game met animatie; dit betekent dat het scherm verandert zonder user input. Dit is een andere klasse van games zoals bijvoorbeel boter, kaas en eieren, waar alleen user input een schermverandering triggert.
* update/draw game loop in game thread
* animatie m.b.v. Android's SurfaceView klasse, het meest geschikt voor snelle animatie
* domeinmodellering m.b.v. OO
* player input via touch screen
* 2-player peer2peer networking m.b.v. UDP sockets


Hoe is de structuur van de game?

* De game heeft state. De state is opgeslagen in vier domeinobjecten, Ball, de eigen Bat, de Bat van de tegenstander en het Scoreboard. Doordat domeinmodellering is gedaan m.b.v. OO is het heel gemakkelijk om bijvoorbeeld een extra bal in het spel te brengen.
* Stateveranderingen komen van (1) de game loop: de bal heeft namelijk een snelheid, waardoor de state verandert ook al is er geen user input, (2) het touch screen: de player kan de eigen bat-positie veranderen, en (3) networking: de bat-positie van de tegenstander.


Hoe werkt de game loop?

* update() en draw() worden in een oneindige loop aangeroepen. update() past de state aan van de domeinobjecten. Dit is nodig, omdat de bal snelheid heeft en dus elke loop een stukje verder is. Het kan zijn dat de bal botst met (1) een muur of (2) een bat. update() detecteert dit en past de score aan in geval van (1), of past de snelheid van de bal aan in geval van (2).
* De game loop wordt zo snel mogelijk doorlopen. Soms heeft de game loop meer werk, ook is Android niet real-time dus doet de telefoon ander werk. Dit betekent dat de game loop er telkens een andere tijd over doet. Daardoor moet de verplaatsing van de bal worden gecorrigeerd voor de tijd dat de game loop over een frame doet, om de bal met constante snelheid over het scherm te laten gaan.
* Om dit inzichtelijk te maken, wordt frames-per-second getoond.
* Normaal mag je op Android alleen vanuit de main thread op het scherm tekenen. SurfaceView is een uitzondering, omdat je vanuit een andere thread direct op het scherm mag tekenen (wel alleen maar op de view geassocieerd met de SurfaceView). Om deze reden draait de game loop in een andere thread dan de main thread.


Hoe werkt de networking?

* Er is gekozen voor peer2peer networking m.b.v. UDP sockets. Daarbij heeft de master de rol van server. De master berekent de bal-trajectorie, berekent of de bal botst met muur of bat en houdt de puntenstand bij. 
* De master stuurt de eigen bat-positie, balpositie en score naar de slave. De slave stuurt de eigen bat-positie naar de master. De master gebruikt de ontvangen batpositie als "adversary bat position", zodat de master de adversary bat-positie kan tonen. De slave gebruikt de ontvangen bat-positie als "adversary bat position" en toont deze op het scherm. Met de ontvangen bal-positie wordt de bal getoond. Met de ontvangen score, wordt de score getoond.
* Zowel de master als de slave hebben beide een send network thread en receive network thread. Beide threads zijn een oneindige loop (die volledig onafhankelijk is van de game loop!). De send loop heeft een 50 millisecond delay, zodat de receiving thread het kan bijhouden en er geen gebufferde packets zijn. Anders zou de receiving thread achter komen te lopen en outdated data op het scherm tonen. 
* Om dit inzichtelijk te maken, wordt network-sends-per-second en network-receives-per-second getoond. Zo kun je verifiëren of de receiving thread het bij kan houden en of 50 ms teveel of te weinig is.
* Je kunt dit zelf testen door de delay op 10 millisecond te zetten op een niet te snelle telefoon. Je ziet dan dat het scoreboard van de slave dan achter komt te lopen. Je ziet ook dat het aantal network-sends-per-second op de ene telefoon niet gelijk is aan het aantal network-receives-per-second op de andere telefoon.


Open eindjes:

* Collision detection gebruikt linker bovenhoek van de bal, niet het midden van de bal en er is ook geen bounding box. Dat werkt prima, maar ziet er soms wat vreemd uit.
* Master en slave ipadressen zijn hard-coded
* In hoeverre worden de ontvangende kanten (zowel master als slave) minder acuraat in wat ze tonen? heb je voordeel als je als speler de master-mobiel hebt, omdat de bal-trajectorie preciezer is?
* Hoe is het CPU-verbruik?
* Er lijkt een memory leak te zijn, als je de Android monitor bekijkt.


### How do I get set up? ###

* Minumum Android 4.4.2 (Android-SDK 19)
* Zet master en slave ipadressen correct (als je een emulator gebruikt, ).
* Als je de emulator gebruikt, zorg dat die de slave draait en zet een port forward voor UDP op port 4445. Dit doe je door "telnet localhost 5554" en dan "redir add udp:4445:4445". Als je putty gebruikt voor de telnetconnectie, gebruik dan connection type "raw", niet "telnet".


#### Gebruik van de applicatie: ####

* Je eigen bat is de rechter bat als je master bent, en de linker bat als je slave bent.
* Beweeg je eigen bat m.b.v. het touch screen
* Zorg dat de bal niet de muur achter je bat raakt, want dat kost een punt! Zorg wel dat de bal de muur achter de tegenstander raakt, want dan krijg je een punt!
* Reset van de game: scherm 90 draaien en weer terug. Dat zorgt namelijk voor het aanroepen van onPause() en onResume() van de activity.