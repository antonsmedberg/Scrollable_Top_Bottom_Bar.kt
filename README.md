# Scrollbar Flödesapplikation

Denna applikation är byggd med Jetpack Compose, Kotlins moderna verktyg för att bygga inbyggda användargränssnitt. Den demonstrerar ett scrollbart flöde med en dynamiskt uppträdande och försvinnande Top App Bar och Bottom Navigation Bar baserat på användarens scrollbeteende.

## Funktioner

- **Dynamisk Top App Bar:** Den övre fältet visas när användaren scrollar upp och försvinner när användaren scrollar ner.
- **Dynamisk Bottom Navigation Bar:** Den nedre navigationsfältet försvinner när användaren scrollar upp och visas när användaren scrollar ner.
- **Scrollbart Flöde:** En vertikalt scrollbar lista med objekt.

## Demo

<video width="320" height="240" controls>
  <source src="9104932dce39ce221035be5311ae9382.mp4" type="video/mp4">
  Din webbläsare stödjer inte videouppspelning.
</video>

## Kodöversikt

Applikationens huvudkomponenter är:

1. **ScrollableFeed:** En composable-funktion som ordnar layouten för appen, inklusive det scrollbara innehållet och de dynamiska topp- och bottenfält.
2. **ScrollableTopAppBar:** En composable-funktion för det övre fältet som justerar sin synlighet baserat på scrollstatus.
3. **ScrollableBottomNavBar:** En composable-funktion för det nedre navigationsfältet som justerar sin synlighet baserat på scrollstatus.
4. **MainScreen:** Applikationens huvudsida som använder `ScrollableFeed` för att visa innehållet.

### `ScrollableFeed`

Denna funktion tar tre composable lambdor som parametrar:
- `topAppBarContent`: Innehållet i det övre fältet.
- `bottomNavBarContent`: Innehållet i det nedre navigationsfältet.
- `content`: Huvudinnehållet i flödet.

Den använder `rememberLazyListState` för att hålla reda på scrollstatus och avgör om användaren scrollar ner eller upp. Baserat på detta justerar den listans padding för att visa eller dölja topp- och bottenfält.

### `ScrollableTopAppBar`

Denna funktion skapar ett övre fält som ändrar sin transparens och position baserat på scrollstatus. Den använder `animateFloatAsState` för mjuka övergångar.

### `ScrollableBottomNavBar`

Liknande `ScrollableTopAppBar` skapar denna funktion ett nedre navigationsfält som ändrar sin transparens och position baserat på scrollstatus.

### `MainScreen`

Denna funktion ställer in appens huvudsida genom att kalla på `ScrollableFeed` med lämpligt innehåll för topp- och bottenfält samt en lista med objekt.

## Användning

1. **Top App Bar:**
   - Det övre fältet innehåller en tillbaka-knapp, en titel och en meny-knapp.
   - Synligheten för det övre fältet ändras baserat på scrollriktningen.

2. **Bottom Navigation Bar:**
   - Den nedre navigationsfältet innehåller knappar för hem, sökning och inställningar.
   - Synligheten för den nedre navigationsfältet ändras baserat på scrollriktningen.

3. **Scrollbart Innehåll:**
   - Innehållet består av en lista med objekt som kan scrollas vertikalt.

## Hur man Kör

1. Se till att du har Android Studio installerat med nödvändiga SDKs.
2. Klona detta repository till din lokala maskin.
3. Öppna projektet i Android Studio.
4. Bygg och kör projektet på en emulator eller fysisk enhet.

## Exempel

Funktionen `MainScreen` visar hur man använder `ScrollableFeed` med anpassat innehåll för topp- och bottenfält samt en lista med objekt.

```kotlin
@Composable
fun MainScreen() {
    ScrollableFeed(
        topAppBarContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* TODO: Hantera tillbaka-knappen */ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tillbaka", tint = Color.White)
                }
                Text("Top App Bar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { /* TODO: Hantera menyn */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Meny", tint = Color.White)
                }
            }
        },
        bottomNavBarContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { /* TODO: Hantera hem-knappen */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Hem", tint = Color.White)
                }
                IconButton(onClick = { /* TODO: Hantera sökning */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Sök", tint = Color.White)
                }
                IconButton(onClick = { /* TODO: Hantera inställningar */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Inställningar", tint = Color.White)
                }
            }
        },
        content = {
            repeat(100) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFF3F4F6),
                                    Color(0xFFE0E0E0)
                                )
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Ikon",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Item $index",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    )
}
```

## Förhandsvisning

För att se en förhandsvisning av `MainScreen`, använd composable-funktionen `ScrollPreview`.

```kotlin
@Composable
@Preview(showBackground = true)
fun ScrollPreview() {
    MainScreen()
}
```
