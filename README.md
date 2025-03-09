# Konkurrens-Programozas-Beadando
2024-25-ös tanév őszi félévében készült Java beadandó Konkurrens Programozás tárgyból. 

# Konkurens programozás beadandó feladat: Juhfarm
Ebben a feladatban egy farmon pásztorkutyák és juhok mozognak. A cél egy olyan program megírása, amely szimulálja a kutyák és juhok mozgását a farmon, és amely során egy juh elmenekülhet a farmról, ha eléri a kaput.

## Alapfeladat (10 pont):
A következő osztályokat kell elkészíteni.

### 1. Farm osztály:

● A farmnak egy téglalap alakú területet kell szimulálnia, amelyet falak vesznek körül, kivéve néhány kaput, ahol a juhok el tudnak menekülni. A terület egy mátrix, amelynek minden egyes mezője más-más típusú lehet: üres mező, fal, kapu, pásztorkutya, juh. (Az Object használata megfelelő a mátrix mezőinek statikus típusához.)

● A farm hossza és szélessége is a három valamelyik többszörösénél kettővel nagyobb szám, alapértelmezett érték mindkét esetben 14. A terület a falakon belül három egyenlő zónára van osztva mindkét dimenzió mentén, vagyis összesen kilencre.

● Mind a négy fal mentén van egy-egy kapu, véletlenszerű pozícióban, de semmiképpen nem a sarokban.

● Alapértelmezésként 10 juhot és 5 kutyát indítunk.

### 2. Juh osztály:

● Minden juh saját szálon fut, a szál neve (és a toString() által visszaadott érték) egy nagybetű legyen.

● A juhok a középső kilencedben kezdenek.

● Minden két mozgás között egy adott ideig (alapértelmezett érték 200 ms) várnak.

● A juhok érzékelik a kutyák jelenlétét a közvetlen szomszédos mezőkön, és ennek hatására ellentétes irányba indulnak el: ha például a vízszintes dimenzió mentén a három szomszédos mező valamelyikében kutya van, akkor a juh ezen dimenzió mentén a másik irányba indul. Ugyanez igaz a függőleges dimenzióra is.

● Amelyik dimenzió mentén nem érzékelnek kutyát a szomszédos mezőben, véletlenszerűen választják ki a mozgásuk irányát.

● Az irány nem lehet mindkét dimenzió mentén 0, vagyis a juhnak mozognia kell mindenképpen, hacsak nem ütközne valamibe a mozgás során.

● Ha egy juh egy kapu mezőre lép, elszökik, ezzel vége a szimulációnak.

### 3. Kutya osztály

● Minden kutya saját szálon fut, a szál neve (és a toString() által visszaadott érték) egy szám legyen.

● A kutyák a külső nyolc kilenced valamelyikében kezdenek, és folyamatosan itt járőröznek.

● Minden két mozgás között egy adott ideig (alapértelmezett érték: 200 ms) várnak.

● A kutyák mindig véletlenszerűen választják ki a mozgásuk irányát.

● Az irány nem lehet mindkét dimenzió mentén 0, vagyis a kutyának mozognia kell mindenképpen, hacsak nem ütközne valamibe a mozgás során, vagy nem lépne be a középső kilencedbe.

### 4. Egyéb objektumok a farmon:

● Az üres és a kapu mezők toString() metódusa szóközt, a falaké meg egy # karaktert adjon vissza.

● Ezeknek a szálaknak semmilyen más szerepük nincs.

### 5. Program futása:

● A program addig fut, amíg egy juh el nem menekül a kapun keresztül.

### 6. Kimenet:

● A farm állapotának folyamatos frissítése konzolon (alapértelmezett érték: 200 ms), ahol látható, hogy a juhok és a kutyák hogyan mozognak. Tipp: futás elején képernyő letörlése a "\033[H\033[2J" karakterlánc kiírásával, két frissítés között pedig a "\u001B[0;0H" karakterlánc kiírásával lehet a kurzort a bal felső sarokba pozicionálni.

● Ha egy juh sikeresen megszökik, a program jelezze ki ezt a tényt, mielőtt leállna.

### 7. Szálbiztosság:

● Az alapfeladatban a farmot egészben tekintjük közös erőforrásnak, a rajta mozgó objektumok (juhok és kutyák) feladata a megfelelő zárolás.
Ismételt figyelmeztetés: a megoldást NEM ITT kell beadni, hanem a gyakorlati TMS-csoportban.

## Bővített feladat (5 pont)
### 1. Szálbiztosság:

● Az alapfeladattal ellentétben most nem az egész farmot, hanem annak mezőit tekintjük külön-külön tekintjük közös erőforrásoknak, külön-külön kell zárolni mindig a megfelelőket.

● Figyelj a holtponthelyzetek elkerülésére!

### 2. Ennyi az egész.
