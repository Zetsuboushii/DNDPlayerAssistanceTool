<p align="center"><br><br><img src="https://www.dnd5eapi.co/public/favicon.ico" width="300" height="300" /></p>

<h3 align="center">D&D Player Assistance Tool</h3>
<p align="center"><strong><code>DNDPlayerAssistanceTool</code></strong></p>
<p align="center">Android Applikation zur Unterstützung von Spielern in einer D&D-Kampagne</p>
<br>
<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2023" />
</p>

<p align="center"><br><br><img src="screenshot.png" height="331" width="526"/></p>

## Mitwirkende

| Mitwirkende  | GitHub                                                                                                                                                                                   |
|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Luke Grasser | <a href="https://github.com/zetsuboushii"><img src="https://avatars.githubusercontent.com/u/65507051?v=4" width="150px;" alt=""/><br/>[Zetsuboushii](https://github.com/zetsuboushii)    |
| Nick Büttner | <a href="https://github.com/knick21"><img src="https://avatars.githubusercontent.com/u/115408270?v=4" width="150px;" alt=""/><br/>[Knick21](https://github.com/knick21)                  |
| Isa Barbu    | <a href="https://github.com/isabellabarbu"><img src="https://avatars.githubusercontent.com/u/78431957?v=4" width="150px;" alt=""/><br/>[IsaBellaBarbu](https://github.com/isabellabarbu) |

## Dokumentation

### Inhalt

<!-- TOC -->

* [Mitwirkende](#mitwirkende)
* [Installation](#installation)
* [Ausführen der Applikation](#ausführen-der-applikation)
* [Dokumentation](#dokumentation)
    * [Inhalt](#inhalt)
    * [1 Einleitung](#1-einleitung)
        * [1.1 Projektumfeld](#11-projektumfeld)
        * [1.2 Projektbeschreibung](#12-projektbeschreibung)
        * [1.3 Projektanforderungen](#13-projektanforderungen)
    * [2 Planung](#2-planung)
        * [2.1 Produktbeschreibung](#21-produktbeschreibung)
        * [2.2 Projektphasen](#22-projektphasen)
    * [3 Analyse](#3-analyse)
        * [3.1 Soll-Ist-Analyse](#31-soll-ist-analyse)
        * [3.2 Fachkonzept](#32-fachkonzept)
    * [4 Entwurf](#4-entwurf)
        * [4.1 Zielplattform](#41-zielplattform)
            * [4.1.1 Technologien](#411-technologien)
            * [4.1.2 API-Referenz](#412-api-referenz)
        * [4.2 Benutzeroberfläche](#42-benutzeroberfläche)

<!-- TOC -->

### 1 Einleitung

#### 1.1 Projektumfeld

Im Rahmen der Vorlesung "Mobile Appentwicklung" des dritten Semesters an der DHBW Mannheim
im Studiengang Angewandte Informatik, soll eine angenehm nutzbare Android-Applikation umgesetzt
werden.

#### 1.2 Projektbeschreibung

Das Ziel dieses Projekts ist das Analysieren und Bearbeiten eines Problems in selbstorganisierter
Gruppenarbeit. Es wird daher von einer Struktur mit eng vorgegebenen Aufgaben abgewichen.
Ein wesentlicher Teil der Aufgabe sind Grundprinzipien von mobilen Applikationen:

- Benutzerzentrierung
- Einfache Navigation
- Performanceoptimierung
- Responsive Design
- Konsistenz im Design
- Update- und Wartbarkeit
- Datenschutz und Einhaltung von Vorschriften

#### 1.3 Projektanforderungen

Das Projekt soll...

- eine Android-Applikation einer frei wählbaren Technologie sein (Native, PWA, Cross-Platform)
- einen API-Call sinnvoll integrieren
- ein übersichtliches User-Interface (UI) besitzen.
- Einfachheit im Aufbau der Softwarearchitektur zur Erleichterung der Wartung besitzen.
- volle Basisfunktionalität besitzen

### 2 Planung

#### 2.1 Produktbeschreibung

Die Applikation stellt einen digitalen Assistenten für Spieler
des bekannten Tabletop-Rollenspiels [***Dungeons & Dragons***](https://dnd.wizards.com/de) dar.\
Der Anwender, beziehungsweise der Spieler, kann damit die Fähigkeiten (Spells, etc.) seines
Charakters verwalten.
Dazu findet eine [API](https://www.dnd5eapi.co/docs/) mit all den nötigen Inhalten für D&D
Verwendung.
Der Spieler kann zunächst nach Fähigkeiten suchen und diese dann für seinen Charakter abspeichern,
damit er
zukünftig schnelleren Zugriff auf solche hat.

#### 2.2 Projektphasen

Das Projekt teilt sich grob in folgende Projektphasen ein:

- Teambildung
- Auswahl des Themas
- Kurze Produktbeschreibung
- Auswahl des Vorgehensmodells
- Auswahl der Technologien
- Implementierung
- Quality Assurance

### 3 Analyse

#### 3.1 Soll-Ist-Analyse

Das Verwalten seines Charakters in einer D&D-Kampagne ist eine komplexe Aufgabe, die sehr schnell
durch viele Faktoren unübersichtlich werden kann. Das Wichtigste beim Spielen eines Charakters ist
der
rasche und einfache Überblick über dessen Fähigkeiten, um einen kontinuierlichen Spielfluss zu
garantieren.\
Dazu erleichtert eine darauf angepasste Applikation diesen Vorgang.

#### 3.2 Fachkonzept

Den folgenden Kriterien *muss* die Applikation entsprechen:

- Der Spieler kann nach Spells suchen und diese abspeichern
- Die API-Referenz liefert gewünschte Ergebnisse
- Eine benutzerfreundliche UI ist vorhanden
- Ein Zugriff auf dem Systemkalender zeigt die nächste D&D-Session in der App an

Den folgenden Kriterien *kann* die Applikation entsprechen:

- Der Spieler hat Zugriff auf weitere Fähigkeiten, Werte, Items, usw.
- Termine für Sessions werden über einen Webserver geladen

### 4 Entwurf

#### 4.1 Zielplattform

Die Applikation soll zunächst auf Android 13 Geräten laufen.

##### 4.1.1 Technologien

Die gewählte Technologie ist eine native Android-Applikation in der Programmiersprache Kotlin.
Kotlin ist eine logische Weiterentwicklung der bekannten Programmiersprache Java und bietet im
Zusammenspiel
mit Jetpack Compose und Material 3 eine simple Möglichkeit Business Logic und UI zu realisieren.

##### 4.1.2 API-Referenz

...

#### 4.2 Benutzeroberfläche

Um dem Anwendungsfall gerecht zu werden, wurde sich für eine möglichst simple, grafische Oberfläche
entschieden.
Die Hauptseiten der App können über eine Navigation Bottom Bar erreicht werden.
Über Material 3 wird ein kontinuierliches Design gewährleistet.
