// isiZulu. Draft: a native speaker should check it before release

val code = "zu"
val name = "isiZulu"
val status = "draft"
val conj = listOf("na")

val pack = mapOf(
    "reject" to listOf(
        "yenqaba konke", "yenqaba onke", "yenqaba wonke", "yenqaba onke amakhukhi", "yenqaba amakhukhi",
        "qhubeka ngaphandle kokuvuma", "ungavumi", "angivumi", "ngiyenqaba", "yeka imvume",
        "cha ngiyabonga", "vala konke", "vimbela konke", "yenqaba bese uvala", "=yenqaba", "=cha",
        "=angivumi", "=ngiyenqaba", "=ungavumi"
    ),
    "necessary" to listOf(
        "okudingekayo kuphela", "amakhukhi adingekayo kuphela", "okubalulekile kuphela",
        "amakhukhi abalulekile kuphela", "vuma okudingekayo kuphela",
        "vuma amakhukhi adingekayo kuphela", "qhubeka nokudingekayo kuphela", "=amakhukhi adingekayo",
        "=okudingekayo", "=okubalulekile", "=amakhukhi abalulekile"
    ),
    "manage" to listOf(
        "lawula izintandokazi", "lawula izinketho", "lawula amakhukhi", "lawula imvume",
        "izilungiselelo zamakhukhi", "izilungiselelo zobumfihlo", "izinketho ezengeziwe",
        "izinketho eziningi", "bonisa imininingwane", "uhlu lwabalingani", "izinhloso nabalingani",
        "ngifuna ukukhetha", "=izilungiselelo", "=izintandokazi", "=izinketho", "=lungisa",
        "=imininingwane", "=lawula"
    ),
    "confirm" to listOf(
        "londoloza izintandokazi", "londoloza izilungiselelo", "londoloza ukukhetha",
        "londoloza ukukhetha kwami", "londoloza uvale", "qinisekisa ukukhetha",
        "qinisekisa ukukhetha kwami", "qinisekisa izintandokazi", "sebenzisa izilungiselelo",
        "londoloza izinguquko", "vuma ukukhetha", "=londoloza", "=qinisekisa", "=kwenziwe"
    ),
    "fallbackReject" to listOf("yenqaba", "=cha", "=cha ngiyabonga", "=angivumi"),
    "rejectButton" to listOf("=yenqaba", "=yenqaba konke", "=cha", "=vala"),
    "expandSection" to listOf("izinhloso ezikhethekile", "izici ezikhethekile"),
    "anchor" to listOf(
        "sisebenzisa amakhukhi", "le webhusayithi isebenzisa amakhukhi", "thina nabalingani bethu",
        "ubumfihlo bakho", "isithakazelo esisemthethweni", "uhlu lwabalingani", "imvume yakho",
        "abalingani bethu", "izikhangiso ezenziwe ngokwezifiso", "inqubomgomo yamakhukhi",
        "ubuchwepheshe bokulandela"
    ),
    "bareAnchor" to listOf("amakhukhi"),
    "strongAnchor" to listOf(
        "amakhukhi adingekayo", "amakhukhi abalulekile", "amakhukhi okusebenza", "amakhukhi amanani",
        "amakhukhi okukhangisa", "isithakazelo esisemthethweni", "uhlu lwabalingani",
        "abalingani bethu", "njalo kuyasebenza", "izintandokazi zamakhukhi", "izilungiselelo zamakhukhi"
    ),
    "neverClick" to listOf(
        "ubhalisele", "ukubhalisa", "thenga", "khokha", "inkokhelo", "ngena", "bhalisa",
        "vula i-akhawunti", "joyina", "uzame mahhala", "nikela", "ngaphandle kwezikhangiso",
        "ngenyanga", "ngonyaka", "i-premium"
    ),
    "acceptGuard" to listOf(
        "yamukela konke", "yamukela onke", "yamukela wonke", "yamukela onke amakhukhi",
        "yamukela amakhukhi", "vumela konke", "vumela onke", "yamukela uvale", "yamukela uqhubeke",
        "ngiyavuma", "ngiyavuma konke", "yebo ngiyavuma", "kulungile ngiyaqonda", "=yamukela",
        "=vumela", "=ngiyavuma", "=kulungile", "=yebo", "=ngiyaqonda", "=qhubeka", "=konke"
    ),
    "acceptWords" to listOf("yamukela", "yamuk*", "ngiyavuma", "vuma", "vumela", "imvume"),
    "switchEssential" to listOf(
        "kudingekile", "okudingekayo", "adingekayo", "okubalulekile", "abalulekile",
        "njalo kuyasebenza", "njalo kuvuliwe", "kuyadingeka", "ayikwazi ukucishwa",
        "ukuze iwebhusayithi isebenze"
    ),
    "switchNonEssential" to listOf(
        "akudingekile", "akubalulekile", "okungakhethwa", "okungeziwe", "okungadingekile",
        "azidingekile"
    ),
    "switchKeepOn" to listOf(
        "ungathengisi", "ungabelani", "ungalandeli", "khumbula ukukhetha kwami", "hlala ungene",
        "ungabonisi futhi"
    ),
    "adsPurpose" to listOf(
        "isikhangiso", "izikhangiso", "ukumaketha", "ukuqondisa", "ukwenza ngokwezifiso", "iphrofayili",
        "imithombo yezokuxhumana"
    ),
    "statsPurpose" to listOf(
        "izibalo", "ukuhlaziya", "isilinganiso", "izethameli", "ukusebenza", "idatha yokusetshenziswa",
        "izivakashi", "ukuthuthukisa", "ucwaningo"
    ),
    "topLevelWords" to listOf(
        "konke", "onke", "wonke", "kuphela", "adingekayo", "okudingekayo", "abalulekile", "amakhukhi"
    ),
    "cookieContext" to listOf(
        "sisebenzisa", "isebenzisa", "yamukela", "imvume", "ubumfihlo", "inqubomgomo", "izilungiselelo",
        "abalingani", "izikhangiso", "idatha"
    ),
    "sensitiveScreen" to listOf(
        "inombolo yekhadi", "ikhadi lesikweletu", "usuku lokuphelelwa", "ikhodi yokuphepha", "cvv",
        "cvc", "iban", "inombolo ye-akhawunti", "imininingwane yenkokhelo", "khokha manje"
    )
)
