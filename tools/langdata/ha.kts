// Hausa. Draft: a native speaker should check it before release

val code = "ha"
val name = "Hausa"
val status = "draft"
val conj = listOf("da")

val pack = mapOf(
    "reject" to listOf(
        "ƙi duka", "ƙi dukka", "ƙi duk kukis", "ƙi kukis", "ci gaba ba tare da karɓa ba", "ba na yarda",
        "janye yarda", "a'a na gode", "kashe duka", "toshe duka", "ƙi kuma rufe", "=ƙi", "=a'a",
        "=ba na yarda", "=ƙi duka"
    ),
    "necessary" to listOf(
        "masu mahimmanci kawai", "kukis masu mahimmanci kawai", "kukis ɗin da ake buƙata kawai",
        "karɓi masu mahimmanci kawai", "ci gaba da masu mahimmanci kawai", "=kukis masu mahimmanci",
        "=masu mahimmanci", "=masu mahimmanci kawai"
    ),
    "manage" to listOf(
        "sarrafa zaɓuka", "sarrafa abubuwan da kake so", "sarrafa kukis", "sarrafa yarda",
        "saitunan kukis", "saitunan sirri", "ƙarin zaɓuka", "nuna cikakkun bayanai",
        "jerin abokan hulɗa", "ina so in zaɓa", "=saituna", "=zaɓuka", "=keɓancewa",
        "=cikakkun bayanai", "=sarrafa", "=ƙarin zaɓuka"
    ),
    "confirm" to listOf(
        "ajiye abubuwan da kake so", "ajiye saituna", "ajiye zaɓina", "ajiye ka rufe",
        "tabbatar da zaɓina", "tabbatar da zaɓuka", "aiwatar da saituna", "ajiye canje-canje", "=ajiye",
        "=tabbatar", "=an gama"
    ),
    "fallbackReject" to listOf("=a'a", "=a'a na gode", "=ba na yarda", "=ƙi"),
    "rejectButton" to listOf("=ƙi", "=ƙi duka", "=a'a", "=kashe"),
    "expandSection" to listOf("dalilai na musamman", "siffofi na musamman"),
    "anchor" to listOf(
        "muna amfani da kukis", "wannan gidan yanar gizon yana amfani da kukis",
        "mu da abokan hulɗarmu", "sirrinka", "sha'awa ta halal", "jerin abokan hulɗa", "yardarka",
        "abokan hulɗarmu", "tallace-tallace na musamman", "manufar kukis"
    ),
    "bareAnchor" to listOf("kukis"),
    "strongAnchor" to listOf(
        "kukis masu mahimmanci", "kukis na aiki", "kukis na ƙididdiga", "kukis na talla",
        "kukis na tallace-tallace", "sha'awa ta halal", "jerin abokan hulɗa", "abokan hulɗarmu",
        "koyaushe yana aiki", "saitunan kukis"
    ),
    "neverClick" to listOf(
        "yi rajista", "biyan kuɗi", "saya", "biya", "shiga", "buɗe asusu", "ba da gudummawa",
        "ba tare da talla ba", "a wata", "a shekara", "premium", "yi oda"
    ),
    "acceptGuard" to listOf(
        "karɓi duka", "karɓi dukka", "karɓi duk kukis", "karɓi kukis", "ba da izini ga duka",
        "karɓa ka rufe", "karɓa ka ci gaba", "=na yarda", "na yarda da duka", "eh na yarda",
        "to na gane", "=karɓa", "=karɓi", "=na yarda", "=to", "=eh", "=na gane", "=ci gaba", "=duka"
    ),
    "acceptWords" to listOf("karɓ*", "karba*", "yarda", "na yarda", "izini"),
    "switchEssential" to listOf(
        "mai mahimmanci", "masu mahimmanci", "wajibi", "koyaushe yana aiki", "koyaushe a kunne",
        "ba za a iya kashewa ba", "don gidan yanar gizo ya yi aiki"
    ),
    "switchNonEssential" to listOf(
        "ba mai mahimmanci ba", "ba masu mahimmanci ba", "na zaɓi", "ƙari", "ba a buƙata ba"
    ),
    "switchKeepOn" to listOf(
        "kada ka sayar", "kada ka raba", "kada ka bi diddigi", "tuna zaɓina", "ka ci gaba da shiga"
    ),
    "adsPurpose" to listOf(
        "talla", "tallace-tallace", "kasuwanci", "niyya", "keɓancewa", "bayanin martaba",
        "kafofin sada zumunta"
    ),
    "statsPurpose" to listOf(
        "ƙididdiga", "nazari", "aunawa", "masu kallo", "aiki", "bayanan amfani", "baƙi", "inganta",
        "bincike"
    ),
    "topLevelWords" to listOf("duka", "dukka", "kawai", "mahimmanci", "kukis"),
    "cookieContext" to listOf(
        "muna amfani", "yana amfani", "karɓi", "yarda", "sirri", "manufa", "saituna", "abokan hulɗa",
        "talla", "bayanai"
    ),
    "sensitiveScreen" to listOf(
        "lambar kati", "katin bashi", "ranar karewa", "lambar tsaro", "cvv", "cvc", "iban",
        "lambar asusu", "bayanan biyan kuɗi", "biya yanzu"
    )
)
