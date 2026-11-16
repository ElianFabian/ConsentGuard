// German. Reviewed against common banner wording (OneTrust, Usercentrics, Didomi, Sourcepoint, Cookiebot)

val code = "de"
val name = "German"
val status = "reviewed"
val conj = listOf("und")

val pack = mapOf(
    "reject" to listOf(
        "alle ablehnen", "alles ablehnen", "alle cookies ablehnen", "alle optionalen ablehnen",
        "nicht notwendige ablehnen", "nicht notwendige cookies ablehnen", "optionale cookies ablehnen",
        "nicht zustimmen", "nicht akzeptieren", "nicht einverstanden", "ich stimme nicht zu",
        "ich lehne ab", "alle verweigern", "alle zurückweisen", "alles zurückweisen",
        "weiter ohne zustimmung", "ohne zustimmung fortfahren", "ohne zustimmung weiter",
        "ohne akzeptieren fortfahren", "weiter ohne akzeptieren", "ohne tracking fortfahren",
        "tracking ablehnen", "marketing ablehnen", "nein danke", "alle deaktivieren",
        "alle ausschalten", "=ablehnen", "=verweigern", "=zurückweisen", "=nein", "=widersprechen",
        "=alle widersprechen", "=ich widerspreche", "=nicht einwilligen", "=keine zustimmung",
        "nicht einwilligen", "einwilligung verweigern", "zustimmung verweigern",
        "weiter ohne einwilligung", "ohne einwilligung fortfahren", "ohne einwilligung weiter",
        "ablehnen und schließen", "ablehnen und weiter", "alle abwählen", "alles abwählen",
        "optionale ablehnen", "alle zwecke ablehnen", "=nicht erlauben", "=nicht zulassen",
        "ohne zustimmung", "ohne einwilligung", "ohne akzeptieren", "ohne annahme", "nein bitte nicht"
    ),
    "necessary" to listOf(
        "nur notwendige", "nur erforderliche", "nur essenzielle", "nur essentielle",
        "nur technisch notwendige", "nur notwendige cookies", "nur erforderliche cookies",
        "nur essenzielle cookies", "nur technisch erforderliche", "notwendige cookies akzeptieren",
        "nur notwendige akzeptieren", "nur erforderliche akzeptieren", "nur notwendige speichern",
        "nur notwendige zulassen", "nur das notwendige", "nur das erforderliche",
        "ausschließlich notwendige", "ausschliesslich notwendige", "nur funktionale cookies",
        "mit notwendigen cookies fortfahren", "weiter mit notwendigen cookies",
        "weiter nur mit notwendigen", "=notwendige cookies", "=erforderliche cookies",
        "=essenzielle cookies", "=notwendige", "=erforderliche", "=nur notwendige",
        "=nur erforderliche", "=nur essenzielle", "=technisch notwendige", "=notwendige akzeptieren",
        "=notwendige zulassen", "=nur funktionale", "nur das nötigste", "nur notwendige verwenden",
        "nur essenzielle verwenden", "erforderliche cookies zulassen", "notwendige cookies zulassen",
        "mit erforderlichen einstellungen fortfahren", "lediglich notwendige", "lediglich erforderliche"
    ),
    "manage" to listOf(
        "einstellungen verwalten", "einstellungen anpassen", "cookie einstellungen",
        "cookies verwalten", "cookie einstellungen anpassen", "cookie einstellungen verwalten",
        "datenschutzeinstellungen", "datenschutz einstellungen", "datenschutzeinstellungen verwalten",
        "privatsphäre einstellungen", "optionen verwalten", "optionen anpassen", "weitere optionen",
        "mehr optionen", "mehr einstellungen", "präferenzen verwalten", "präferenzen anpassen",
        "einwilligungen verwalten", "einwilligung verwalten", "auswahl anpassen", "auswahl treffen",
        "individuelle einstellungen", "individuelle auswahl", "erweiterte einstellungen",
        "details anzeigen", "zwecke anzeigen", "partner anzeigen", "liste der partner",
        "liste der anbieter", "anbieter anzeigen", "zwecke und partner", "cookie präferenzen",
        "cookie auswahl", "mehr erfahren und anpassen", "ich möchte auswählen", "=einstellungen",
        "=optionen", "=anpassen", "=personalisieren", "=konfigurieren", "=details", "=präferenzen",
        "=verwalten", "=mehr optionen", "=weitere optionen", "=auswahl", "=datenschutz",
        "=cookie einstellungen", "=datenschutzeinstellungen", "=einstellungen ändern", "=mehr infos"
    ),
    "confirm" to listOf(
        "auswahl speichern", "meine auswahl speichern", "auswahl bestätigen",
        "meine auswahl bestätigen", "einstellungen speichern", "meine einstellungen speichern",
        "präferenzen speichern", "speichern und schließen", "speichern und beenden",
        "speichern und fortfahren", "speichern und weiter", "bestätigen und schließen",
        "bestätigen und weiter", "änderungen speichern", "auswahl übernehmen",
        "einstellungen übernehmen", "einstellungen bestätigen", "cookie einstellungen speichern",
        "ausgewählte akzeptieren", "auswahl akzeptieren", "ausgewählte zulassen", "auswahl zulassen",
        "=speichern", "=bestätigen", "=übernehmen", "=fertig", "=speichern und schließen",
        "=auswahl speichern", "=auswahl bestätigen", "=anwenden", "=sichern"
    ),
    "fallbackReject" to listOf(
        "ablehnen", "verweigern", "zurückweisen", "widersprechen", "=nein", "=nein danke",
        "=nicht zustimmen", "=nicht akzeptieren"
    ),
    "rejectButton" to listOf(
        "=ablehnen", "=alle ablehnen", "=verweigern", "=widersprechen", "=nein", "=aus", "=deaktivieren"
    ),
    "expandSection" to listOf(
        "besondere zwecke", "besondere funktionen", "spezielle zwecke", "spezielle funktionen"
    ),
    "anchor" to listOf(
        "wir verwenden cookies", "wir nutzen cookies", "diese website verwendet cookies",
        "diese seite verwendet cookies", "diese webseite verwendet cookies", "wir und unsere partner",
        "ihre privatsphäre", "ihre daten ihre wahl", "berechtigtes interesse", "berechtigte interessen",
        "liste der partner", "datenschutzeinstellungen", "einwilligung verwalten", "ihre einwilligung",
        "unsere partner", "unsere anbieter", "drittanbieter", "personalisierte werbung",
        "informationen auf einem gerät speichern", "zweck der datenverarbeitung", "datenschutzhinweise",
        "cookie richtlinie", "cookie hinweis", "cookie banner", "tracking technologien",
        "indem sie weiter surfen", "wenn sie diese seite weiter nutzen", "ihre cookie einstellungen"
    ),
    "bareAnchor" to listOf("cookies", "kekse"),
    "strongAnchor" to listOf(
        "unbedingt erforderlich", "technisch notwendige cookies", "notwendige cookies",
        "essenzielle cookies", "funktionale cookies", "statistik cookies", "marketing cookies",
        "analyse cookies", "komfort cookies", "werbe cookies", "personalisierte werbung",
        "berechtigtes interesse", "liste der partner", "unsere partner", "unsere anbieter",
        "immer aktiv", "zwecke der datenverarbeitung", "cookie einstellungen", "cookie präferenzen",
        "kategorien von cookies", "besondere zwecke", "besondere funktionen", "tracking technologien",
        "drittanbieter", "präzise standortdaten", "geräteeigenschaften", "ihre daten verwalten"
    ),
    "neverClick" to listOf(
        "abonnieren", "abo", "abonnement", "pur abo", "jetzt kaufen", "kaufen", "bezahlen", "zahlen",
        "jetzt testen", "testen", "kostenlos testen", "anmelden", "einloggen", "login", "registrieren",
        "konto erstellen", "mitglied werden", "mitgliedschaft", "unterstützen", "spenden", "spende",
        "werbefrei", "ohne werbung", "werbung entfernen", "pro monat", "pro jahr", "monatlich",
        "jährlich", "upgrade", "zur kasse", "bestellen", "premium", "contentpass", "freechoice"
    ),
    "acceptGuard" to listOf(
        "alle akzeptieren", "alles akzeptieren", "alle cookies akzeptieren", "alle zulassen",
        "alles zulassen", "allen zustimmen", "alle annehmen", "alles annehmen", "alle erlauben",
        "alles erlauben", "akzeptieren und weiter", "akzeptieren und schließen",
        "akzeptieren und fortfahren", "zustimmen und weiter", "zustimmen und schließen",
        "zustimmen und fortfahren", "einverstanden und weiter", "ich akzeptiere alle", "=ich stimme zu",
        "=ich bin einverstanden", "=ich akzeptiere", "=cookies akzeptieren", "=cookies zulassen",
        "empfohlene einstellungen akzeptieren", "ja ich stimme zu", "ja ich bin einverstanden",
        "notwendige und optionale", "ok verstanden", "=akzeptieren", "=zustimmen", "=einverstanden",
        "=annehmen", "=zulassen", "=erlauben", "=ok", "=okay", "=ja", "=verstanden", "=weiter",
        "=fortfahren", "=alles klar", "=alle", "=aktivieren", "=einwilligen", "in alle einwilligen",
        "allem zustimmen", "=einwilligung erteilen", "einwilligen und weiter",
        "einwilligen und schließen", "mit werbung lesen", "mit werbung weiterlesen",
        "mit werbung nutzen", "mit werbung fortfahren", "weiter mit werbung",
        "empfohlene cookies zulassen", "=zustimmung", "=einwilligung"
    ),
    "acceptWords" to listOf(
        "akzept*", "zustimm*", "erlaub*", "zulass*", "einverstanden", "einwillig*", "annehm*",
        "stimme zu"
    ),
    "switchEssential" to listOf(
        "unbedingt erforderlich", "unbedingt notwendig", "notwendig", "erforderlich", "essenziell",
        "essentiell", "immer aktiv", "immer an", "immer aktiviert", "technisch notwendig",
        "technisch erforderlich", "pflicht", "kann nicht deaktiviert werden",
        "kann nicht abgewählt werden", "nicht abwählbar", "damit die website funktioniert",
        "für den betrieb der website"
    ),
    "switchNonEssential" to listOf(
        "nicht notwendig", "nicht erforderlich", "nicht essenziell", "nicht unbedingt",
        "nicht zwingend", "optional", "unnötig", "freiwillig", "zusätzlich", "nicht technisch"
    ),
    "switchKeepOn" to listOf(
        "nicht verkaufen", "nicht weitergeben", "nicht teilen", "nicht verfolgen", "nicht tracken",
        "meine auswahl merken", "meine entscheidung merken", "angemeldet bleiben",
        "nicht mehr anzeigen", "nicht erneut fragen", "widerspruch"
    ),
    "adsPurpose" to listOf(
        "werbung", "werbe", "werbeanzeigen", "marketing", "personalisier", "zielgerichtet",
        "retargeting", "interessenbasiert", "profil", "soziale medien", "anzeigenauswahl",
        "anzeigen personalisieren"
    ),
    "statsPurpose" to listOf(
        "statistik", "statistiken", "analyse", "analytisch", "messung", "reichweite", "performance",
        "nutzungsdaten", "besucher", "erkenntnisse", "verbessern", "forschung"
    ),
    "topLevelWords" to listOf(
        "alle", "alles", "allen", "nur", "ausschließlich", "ausschliesslich", "notwendige",
        "erforderliche", "essenzielle", "essentielle", "optionale", "cookies", "tracking", "technisch",
        "nötigste"
    ),
    "cookieContext" to listOf(
        "wir verwenden", "wir nutzen", "verwendet cookies", "nutzt cookies", "akzeptieren", "zustimm",
        "einwillig", "datenschutz", "richtlinie", "einstellung", "präferenz", "partner",
        "personalisier", "tracking", "werbung", "daten"
    ),
    "sensitiveScreen" to listOf(
        "kartennummer", "kreditkartennummer", "kreditkarte", "karteninhaber", "gültig bis",
        "prüfziffer", "sicherheitscode", "cvv", "cvc", "iban", "kontonummer", "pin eingeben",
        "zahlungsdaten", "zahlungsart", "jetzt bezahlen", "zahlungspflichtig bestellen"
    )
)
