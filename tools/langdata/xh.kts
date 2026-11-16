// isiXhosa. Draft: a native speaker should check it before release

val code = "xh"
val name = "isiXhosa"
val status = "draft"
val conj = listOf("kunye")

val pack = mapOf(
    "reject" to listOf(
        "yala konke", "yala zonke", "yala onke", "yala zonke iikuki", "yala iikuki",
        "qhubeka ngaphandle kokwamkela", "musa ukwamkela", "andivumi", "ndiyala", "ndiyakwala",
        "ncama imvume", "hayi enkosi", "cima konke", "thintela konke", "yala uvale", "=yala", "=hayi",
        "=andivumi", "=ndiyala", "=musa"
    ),
    "necessary" to listOf(
        "ezifunekayo kuphela", "iikuki ezifunekayo kuphela", "ezibalulekileyo kuphela",
        "iikuki ezibalulekileyo kuphela", "yamkela ezifunekayo kuphela",
        "yamkela iikuki ezifunekayo kuphela", "qhubeka nezifunekayo kuphela", "=iikuki ezifunekayo",
        "=ezifunekayo", "=ezibalulekileyo", "=iikuki ezibalulekileyo"
    ),
    "manage" to listOf(
        "lawula izinto ozikhethayo", "lawula iinketho", "lawula iikuki", "lawula imvume",
        "useto lweekuki", "useto lwabucala", "iinketho ezongezelelweyo", "iinketho ezininzi",
        "bonisa iinkcukacha", "uluhlu lwabalingane", "iinjongo nabalingane", "ndifuna ukukhetha",
        "=useto", "=izinto ozikhethayo", "=iinketho", "=lungiselela", "=iinkcukacha", "=lawula"
    ),
    "confirm" to listOf(
        "gcina izinto ozikhethayo", "gcina useto", "gcina oko ukukhethileyo", "gcina uvale",
        "qinisekisa oko ukukhethileyo", "qinisekisa useto", "sebenzisa useto", "gcina utshintsho",
        "yamkela oko ukukhethileyo", "=gcina", "=qinisekisa", "=kugqityiwe"
    ),
    "fallbackReject" to listOf("yala", "=hayi", "=hayi enkosi", "=andivumi"),
    "rejectButton" to listOf("=yala", "=yala konke", "=hayi", "=cima"),
    "expandSection" to listOf("iinjongo ezizodwa", "iimpawu ezizodwa"),
    "anchor" to listOf(
        "sisebenzisa iikuki", "eli tshaneli lisebenzisa iikuki", "eli bhlogo lisebenzisa iikuki",
        "thina nabalingane bethu", "ubumfihlo bakho", "umdla osemthethweni", "uluhlu lwabalingane",
        "imvume yakho", "abalingane bethu", "iintengiso ezenzelwe wena", "umgaqo-nkqubo weekuki",
        "iteknoloji yokulandela"
    ),
    "bareAnchor" to listOf("iikuki"),
    "strongAnchor" to listOf(
        "iikuki ezifunekayo", "iikuki ezibalulekileyo", "iikuki ezisebenzayo", "iikuki zamanani",
        "iikuki zentengiso", "umdla osemthethweni", "uluhlu lwabalingane", "abalingane bethu",
        "zihlala zisebenza", "iikuki ezikhethwayo", "useto lweekuki"
    ),
    "neverClick" to listOf(
        "bhalisela", "ukubhalisela", "thenga", "hlawula", "intlawulo", "ngena", "bhalisa",
        "vula iakhawunti", "joyina", "zama simahla", "nikela", "ngaphandle kweentengiso", "ngenyanga",
        "ngonyaka", "i-premium"
    ),
    "acceptGuard" to listOf(
        "yamkela konke", "yamkela zonke", "yamkela onke", "yamkela zonke iikuki", "=yamkela iikuki",
        "vumela konke", "vumela zonke", "yamkela uvale", "yamkela uqhubeke", "ndiyavuma",
        "ndiyavuma konke", "ewe ndiyavuma", "kulungile ndiyaqonda", "=yamkela", "=vumela", "=ndiyavuma",
        "=kulungile", "=ewe", "=ndiyaqonda", "=qhubeka", "=konke"
    ),
    "acceptWords" to listOf("yamkela", "yamk*", "ndiyavuma", "vuma", "vumela", "imvume"),
    "switchEssential" to listOf(
        "kuyafuneka", "ezifunekayo", "ezibalulekileyo", "zihlala zisebenza", "zihlala zivuliwe",
        "ayinakucinywa", "ukuze iwebhusayithi isebenze"
    ),
    "switchNonEssential" to listOf(
        "akufuneki", "akubalulekanga", "okukhethwayo", "okongezelelweyo", "ezingafunekiyo"
    ),
    "switchKeepOn" to listOf(
        "musa ukuthengisa", "musa ukwabelana", "musa ukulandela", "khumbula oko ndikhethileyo",
        "hlala ungene", "musa ukubonisa kwakhona"
    ),
    "adsPurpose" to listOf(
        "intengiso", "iintengiso", "ukuthengisa", "ukujolisa", "ukwenzela wena", "iprofayile",
        "imidiya yoluntu"
    ),
    "statsPurpose" to listOf(
        "amanani", "uhlalutyo", "ukulinganisa", "abaphulaphuli", "ukusebenza", "idatha yokusetyenziswa",
        "iindwendwe", "ukuphucula", "uphando"
    ),
    "topLevelWords" to listOf(
        "konke", "zonke", "onke", "kuphela", "ezifunekayo", "ezibalulekileyo", "iikuki"
    ),
    "cookieContext" to listOf(
        "sisebenzisa", "lisebenzisa", "yamkela", "imvume", "ubumfihlo", "umgaqo", "useto", "abalingane",
        "iintengiso", "idatha"
    ),
    "sensitiveScreen" to listOf(
        "inombolo yekhadi", "ikhadi leleta", "umhla wokuphelelwa", "ikhowudi yokhuseleko", "cvv", "cvc",
        "iban", "inombolo yeakhawunti", "iinkcukacha zentlawulo", "hlawula ngoku"
    )
)
