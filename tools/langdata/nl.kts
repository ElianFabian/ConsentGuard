// Dutch

val code = "nl"
val name = "Dutch"
val status = "reviewed"
val conj = listOf("en")

val pack = mapOf(
    "reject" to listOf(
        "alles weigeren", "alle weigeren", "alle cookies weigeren", "cookies weigeren",
        "optionele cookies weigeren", "niet noodzakelijke cookies weigeren", "alles afwijzen",
        "alle afwijzen", "doorgaan zonder te accepteren", "verder zonder accepteren",
        "doorgaan zonder toestemming", "niet akkoord", "niet accepteren", "ik ga niet akkoord",
        "nee bedankt", "alles uitzetten", "alles blokkeren", "weigeren en sluiten",
        "weigeren en doorgaan", "=weigeren", "=afwijzen", "=weiger", "=nee", "=bezwaar",
        "=niet toestaan", "niet toestemmen", "geen toestemming", "alles deselecteren", "weiger alle",
        "weiger alles", "wijs alles af", "wijs alle af", "ga door zonder", "verder zonder",
        "liever niet", "liever geen cookies", "geen cookies toestaan", "geen cookies", "accepteer geen"
    ),
    "necessary" to listOf(
        "alleen noodzakelijke", "alleen de noodzakelijke", "alleen noodzakelijke cookies",
        "alleen essentiële", "alleen essentiële cookies", "alleen functionele",
        "alleen functionele cookies", "alleen technische", "alleen vereiste", "enkel noodzakelijke",
        "enkel noodzakelijke cookies", "accepteer alleen noodzakelijke", "accepteer alleen essentiële",
        "alleen noodzakelijke accepteren", "alleen noodzakelijke toestaan",
        "doorgaan met noodzakelijke cookies", "alleen het noodzakelijke", "het minimum",
        "=noodzakelijke cookies", "=essentiële cookies", "=noodzakelijk", "=essentieel",
        "=alleen noodzakelijk", "=alleen essentieel", "=functionele cookies",
        "uitsluitend noodzakelijk", "alleen noodzakelijk", "enkel noodzakelijk"
    ),
    "manage" to listOf(
        "voorkeuren beheren", "instellingen beheren", "opties beheren", "keuzes beheren",
        "cookies beheren", "toestemming beheren", "cookie instellingen", "cookie voorkeuren",
        "cookies instellen", "cookies aanpassen", "privacy instellingen", "privacyinstellingen",
        "privacy voorkeuren", "meer opties", "andere opties", "aanvullende opties", "toon details",
        "details tonen", "bekijk details", "toon doeleinden", "toon partners", "lijst met partners",
        "lijst met leveranciers", "doeleinden en partners", "kies cookies", "voorkeurencentrum",
        "privacycentrum", "ik wil kiezen", "aanpassen en meer info", "=instellingen", "=voorkeuren",
        "=opties", "=aanpassen", "=personaliseren", "=configureren", "=details", "=beheren",
        "=meer opties", "=cookie instellingen"
    ),
    "confirm" to listOf(
        "voorkeuren opslaan", "instellingen opslaan", "keuzes opslaan", "mijn keuzes opslaan",
        "selectie opslaan", "opslaan en sluiten", "opslaan en doorgaan", "opslaan en afsluiten",
        "keuzes bevestigen", "mijn keuzes bevestigen", "selectie bevestigen", "voorkeuren bevestigen",
        "bevestigen en sluiten", "instellingen toepassen", "wijzigingen opslaan", "selectie accepteren",
        "selectie toestaan", "=opslaan", "=bevestigen", "=toepassen", "=klaar", "=bevestig",
        "=selectie opslaan"
    ),
    "fallbackReject" to listOf("weigeren", "afwijzen", "=nee", "=nee bedankt", "=niet akkoord"),
    "rejectButton" to listOf("=weigeren", "=afwijzen", "=alles weigeren", "=nee", "=uit"),
    "expandSection" to listOf("speciale doeleinden", "speciale functies", "bijzondere doeleinden"),
    "anchor" to listOf(
        "wij gebruiken cookies", "deze website gebruikt cookies", "deze site gebruikt cookies",
        "wij en onze partners", "jouw privacy", "uw privacy", "gerechtvaardigd belang",
        "gerechtvaardigde belangen", "lijst met partners", "privacy instellingen", "uw toestemming",
        "jouw toestemming", "onze partners", "onze leveranciers", "externe leveranciers",
        "gepersonaliseerde advertenties", "informatie opslaan en of openen",
        "doeleinden van verwerking", "cookiebeleid", "cookieverklaring", "tracking technologieën",
        "door verder te gaan met surfen", "door deze site te blijven gebruiken"
    ),
    "bareAnchor" to listOf("cookies"),
    "strongAnchor" to listOf(
        "strikt noodzakelijk", "noodzakelijke cookies", "essentiële cookies", "functionele cookies",
        "prestatiecookies", "statistische cookies", "analytische cookies", "marketing cookies",
        "marketingcookies", "advertentiecookies", "gepersonaliseerde advertenties",
        "gerechtvaardigd belang", "lijst met partners", "onze partners", "altijd actief",
        "doeleinden van verwerking", "cookie voorkeuren", "cookie instellingen", "categorieën cookies",
        "speciale doeleinden", "tracking technologieën"
    ),
    "neverClick" to listOf(
        "abonneren", "abonnement", "kopen", "betalen", "betaling", "inloggen", "aanmelden",
        "registreren", "account aanmaken", "lid worden", "gratis proberen", "proefperiode", "doneren",
        "donatie", "steun ons", "zonder advertenties", "zonder reclame", "advertenties verwijderen",
        "per maand", "per jaar", "maandelijks", "jaarlijks", "premium", "bestellen", "abonneer"
    ),
    "acceptGuard" to listOf(
        "alles accepteren", "alle accepteren", "alle cookies accepteren", "alles toestaan",
        "alle cookies toestaan", "accepteren en sluiten", "accepteren en doorgaan",
        "accepteer en ga verder", "ik ga akkoord", "akkoord en doorgaan", "ja ik accepteer",
        "ja ik ga akkoord", "ok begrepen", "=accepteren", "=toestaan", "=akkoord", "=ok", "=oké", "=ja",
        "=begrepen", "=doorgaan", "=inschakelen", "=alles", "=toestemming geven", "=toestemming",
        "=toestemmen", "=cookies toestaan", "=cookies accepteren", "=ik accepteer", "accepteer alle",
        "accepteer alles", "=is goed", "ja prima", "=prima"
    ),
    "acceptWords" to listOf(
        "accept*", "akkoord", "toestemming", "toestaan", "toesta*", "instemmen", "instem*"
    ),
    "switchEssential" to listOf(
        "strikt noodzakelijk", "noodzakelijk", "noodzakelijke", "essentieel", "essentiële", "verplicht",
        "altijd actief", "altijd aan", "vereist", "technisch noodzakelijk",
        "kan niet worden uitgeschakeld", "onmisbaar", "voor de werking van de site",
        "werking van de website"
    ),
    "switchNonEssential" to listOf(
        "niet noodzakelijk", "niet noodzakelijke", "niet essentieel", "niet essentiële", "niet vereist",
        "optioneel", "optionele", "facultatief", "extra", "aanvullend", "overbodig"
    ),
    "switchKeepOn" to listOf(
        "niet verkopen", "niet delen", "niet volgen", "mijn keuze onthouden", "mijn voorkeur onthouden",
        "ingelogd blijven", "niet meer tonen", "niet opnieuw vragen"
    ),
    "adsPurpose" to listOf(
        "advertent", "reclame", "marketing", "targeting", "personalis", "retargeting",
        "op basis van interesses", "profiel", "sociale media", "selectie van advertenties"
    ),
    "statsPurpose" to listOf(
        "statistiek", "statistieken", "statistisch", "analyse", "analytisch", "meting", "publiek",
        "prestaties", "gebruiksgegevens", "bezoekers", "verkeer", "verbeteren", "onderzoek"
    ),
    "topLevelWords" to listOf(
        "alles", "alle", "alleen", "enkel", "noodzakelijke", "noodzakelijk", "essentiële",
        "functionele", "cookies", "optionele", "tracking", "technische"
    ),
    "cookieContext" to listOf(
        "wij gebruiken", "gebruikt cookies", "accepteren", "toestemming", "privacy", "beleid",
        "instellingen", "voorkeuren", "partners", "personalis", "tracking", "advertent", "gegevens"
    ),
    "sensitiveScreen" to listOf(
        "kaartnummer", "creditcardnummer", "creditcard", "kaarthouder", "vervaldatum",
        "beveiligingscode", "cvv", "cvc", "iban", "rekeningnummer", "pincode", "betaalgegevens",
        "betaalmethode", "nu betalen"
    )
)
