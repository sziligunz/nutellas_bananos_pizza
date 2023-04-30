# Repository for 2022/23/2 Adatbázis alapú rendszerek

*Created by __AQ56DX__, __BFYF5U__ and __OQQA67__ for code sharing purposes*

## Project theme: Plane ticket reservation

## Hogyan futtasuk?

1. Futtasuk a táblákat létrehozó szkriptet
2. Nyissuk meg a projectet IntellIJ IDEA-ban (igazából csak Maven miatt)
3. Az /src/main/java/com/flight/repo/DataSourceConfiguration.java-ban át kell írni a megfelelő adtokat:

    * **url**-t az adatbázis elérésére. Jelenleg úgy van beállítva, hogy az orania2 szervert áthúzzuk a localhost
      1521-es portjára ezzel a pranccsal *(h123456 helyére saját h azonosító)*:
    ```   
    ssh -L1521:orania2.inf.u-szeged.hu:1521 h123456@linux.inf.u-szeged.hu 
    ```

    * **username**-t a megfelelő C##NEPTUN-ra
    * **password**-t a megfelelő jelszóra


4. Ezután a /src/main/java/com/flight/Main.java állományt tudjuk futtatni az IDE segítségével.

