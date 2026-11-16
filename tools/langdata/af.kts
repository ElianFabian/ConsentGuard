// Afrikaans

val code = "af"
val name = "Afrikaans"
val status = "draft"
val conj = listOf("en")

val pack = mapOf(
    "reject" to listOf(
        "weier alles", "weier almal", "weier alle koekies", "weier koekies", "weier opsionele koekies",
        "gaan voort sonder om te aanvaar", "gaan voort sonder toestemming", "moenie aanvaar nie",
        "ek stem nie in nie", "nee dankie", "skakel alles af", "blokkeer alles", "weier en maak toe",
        "weier en gaan voort", "=weier", "=verwerp", "=nee", "=beswaar"
    ),
    "necessary" to listOf(
        "net noodsaaklike", "slegs noodsaaklike", "net noodsaaklike koekies",
        "slegs noodsaaklike koekies", "net essensiële", "slegs essensiële", "net funksionele",
        "aanvaar net noodsaaklike", "aanvaar slegs noodsaaklike", "gaan voort met noodsaaklike koekies",
        "net die noodsaaklike", "=noodsaaklike koekies", "=essensiële koekies", "=noodsaaklik",
        "=net noodsaaklik", "=slegs noodsaaklik"
    ),
    "manage" to listOf(
        "bestuur voorkeure", "bestuur opsies", "bestuur keuses", "bestuur koekies",
        "bestuur toestemming", "koekie instellings", "koekie voorkeure", "stel koekies",
        "pasmaak koekies", "privaatheidinstellings", "meer opsies", "ander opsies", "wys besonderhede",
        "wys doeleindes", "wys vennote", "lys van vennote", "doeleindes en vennote", "kies koekies",
        "ek wil kies", "=instellings", "=voorkeure", "=opsies", "=pasmaak", "=besonderhede", "=bestuur",
        "=meer opsies"
    ),
    "confirm" to listOf(
        "stoor voorkeure", "stoor instellings", "stoor keuses", "stoor my keuses", "stoor seleksie",
        "stoor en maak toe", "stoor en gaan voort", "bevestig keuses", "bevestig my keuses",
        "bevestig seleksie", "bevestig voorkeure", "pas instellings toe", "stoor veranderinge",
        "=stoor", "=bevestig", "=pas toe", "=klaar"
    ),
    "fallbackReject" to listOf("weier", "=nee", "=nee dankie"),
    "rejectButton" to listOf("=weier", "=weier alles", "=nee", "=af"),
    "expandSection" to listOf("spesiale doeleindes", "spesiale kenmerke"),
    "anchor" to listOf(
        "ons gebruik koekies", "hierdie webwerf gebruik koekies", "ons en ons vennote",
        "jou privaatheid", "u privaatheid", "wettige belang", "lys van vennote", "jou toestemming",
        "u toestemming", "ons vennote", "ons verskaffers", "gepersonaliseerde advertensies",
        "doeleindes van verwerking", "koekiebeleid", "volgtegnologieë"
    ),
    "bareAnchor" to listOf("koekies", "cookies"),
    "strongAnchor" to listOf(
        "streng noodsaaklik", "noodsaaklike koekies", "essensiële koekies", "funksionele koekies",
        "prestasie koekies", "statistiese koekies", "bemarkings koekies", "advertensie koekies",
        "gepersonaliseerde advertensies", "wettige belang", "lys van vennote", "ons vennote",
        "altyd aktief", "doeleindes van verwerking", "koekie voorkeure", "koekie instellings"
    ),
    "neverClick" to listOf(
        "teken in", "intekening", "koop", "betaal", "betaling", "meld aan", "registreer",
        "skep rekening", "word lid", "gratis proef", "skenk", "skenking", "ondersteun ons",
        "sonder advertensies", "per maand", "per jaar", "maandeliks", "jaarliks", "premium", "bestel"
    ),
    "acceptGuard" to listOf(
        "aanvaar alles", "aanvaar almal", "aanvaar alle koekies", "aanvaar koekies", "laat alles toe",
        "laat alle koekies toe", "aanvaar en maak toe", "aanvaar en gaan voort", "ek aanvaar",
        "ek stem in", "ja ek aanvaar", "ok verstaan", "=aanvaar", "=laat toe", "=stem in", "=ok", "=ja",
        "=verstaan", "=gaan voort", "=almal"
    ),
    "acceptWords" to listOf("aanvaar", "aanvaar*", "toestemming", "stem in", "toelaat", "laat toe"),
    "switchEssential" to listOf(
        "streng noodsaaklik", "noodsaaklik", "noodsaaklike", "essensieel", "essensiële", "verpligtend",
        "altyd aktief", "altyd aan", "vereis", "tegnies noodsaaklik", "kan nie afgeskakel word nie",
        "onontbeerlik", "vir die werking van die webwerf"
    ),
    "switchNonEssential" to listOf(
        "nie noodsaaklik nie", "nie essensieel nie", "nie vereis nie", "opsioneel", "opsionele",
        "ekstra", "bykomend", "oorbodig"
    ),
    "switchKeepOn" to listOf(
        "moenie verkoop nie", "moenie deel nie", "moenie volg nie", "onthou my keuse", "bly aangemeld",
        "moenie weer wys nie", "moenie weer vra nie"
    ),
    "adsPurpose" to listOf(
        "advertensie", "bemarking", "teiken", "verpersoonlik", "herteiken",
        "gebaseer op belangstelling", "profiel", "sosiale media"
    ),
    "statsPurpose" to listOf(
        "statistiek", "statistiese", "ontleding", "analitiese", "meting", "gehoor", "prestasie",
        "gebruiksdata", "besoekers", "verkeer", "verbeter", "navorsing"
    ),
    "topLevelWords" to listOf(
        "alles", "almal", "alle", "net", "slegs", "noodsaaklike", "noodsaaklik", "essensiële",
        "koekies", "opsionele", "tegniese"
    ),
    "cookieContext" to listOf(
        "ons gebruik", "gebruik koekies", "aanvaar", "toestemming", "privaatheid", "beleid",
        "instellings", "voorkeure", "vennote", "verpersoonlik", "advertensie", "data"
    ),
    "sensitiveScreen" to listOf(
        "kaartnommer", "kredietkaart", "kaarthouer", "vervaldatum", "sekuriteitskode", "cvv", "cvc",
        "iban", "rekeningnommer", "betaalbesonderhede", "betaal nou"
    )
)
