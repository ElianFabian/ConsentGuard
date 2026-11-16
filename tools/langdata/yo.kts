// Yoruba. Draft: a native speaker should check it before release. Dots and tone marks are ignored when matching

val code = "yo"
val name = "Yoruba"
val status = "draft"
val conj = listOf("àti", "ati")

val pack = mapOf(
    "reject" to listOf(
        "kọ gbogbo rẹ̀", "kọ gbogbo wọn", "kọ gbogbo", "kọ gbogbo kuki", "kọ kuki", "tẹ̀síwájú láìgbà",
        "má ṣe gbà", "mi ò gbà", "yọ ìyọ̀ǹda kúrò", "rárá o ṣé", "pa gbogbo rẹ̀", "kọ kí o sì pa",
        "=kọ", "=rárá", "=mi ò gbà", "=má gbà"
    ),
    "necessary" to listOf(
        "àwọn tó ṣe pàtàkì nìkan", "àwọn kuki tó ṣe pàtàkì nìkan", "àwọn tó pọndandan nìkan",
        "gba àwọn tó ṣe pàtàkì nìkan", "tẹ̀síwájú pẹ̀lú àwọn tó ṣe pàtàkì nìkan",
        "=àwọn kuki tó ṣe pàtàkì", "=àwọn tó ṣe pàtàkì", "=ṣe pàtàkì nìkan"
    ),
    "manage" to listOf(
        "ṣàkóso àwọn ààyò", "ṣàkóso àwọn àṣàyàn", "ṣàkóso àwọn kuki", "ṣàkóso ìyọ̀ǹda", "àwọn ètò kuki",
        "àwọn ètò àṣírí", "àwọn àṣàyàn mìíràn", "fi àwọn àlàyé hàn", "àtòjọ àwọn alábàáṣiṣẹ́pọ̀",
        "mo fẹ́ yàn", "=àwọn ètò", "=àwọn ààyò", "=àwọn àṣàyàn", "=ṣe àdáni", "=àwọn àlàyé", "=ṣàkóso"
    ),
    "confirm" to listOf(
        "fi àwọn ààyò pamọ́", "fi àwọn ètò pamọ́", "fi yíyàn mi pamọ́", "fi pamọ́ kí o sì pa",
        "jẹ́rìí sí yíyàn mi", "jẹ́rìí sí àwọn àṣàyàn", "lo àwọn ètò", "fi àwọn àyípadà pamọ́",
        "=fi pamọ́", "=jẹ́rìí", "=ti parí"
    ),
    "fallbackReject" to listOf("=rárá", "=rárá o ṣé", "=mi ò gbà", "=kọ"),
    "rejectButton" to listOf("=kọ", "=kọ gbogbo", "=rárá", "=pa"),
    "expandSection" to listOf("àwọn ète pàtàkì", "àwọn ẹ̀yà pàtàkì"),
    "anchor" to listOf(
        "a ń lo àwọn kuki", "ojú ìwé yìí ń lo àwọn kuki", "àwa àti àwọn alábàáṣiṣẹ́pọ̀ wa", "àṣírí rẹ",
        "ìfẹ́ tó bófin mu", "àtòjọ àwọn alábàáṣiṣẹ́pọ̀", "ìyọ̀ǹda rẹ", "àwọn alábàáṣiṣẹ́pọ̀ wa",
        "ìpolówó tí a ṣe fún ẹni", "ìlànà kuki"
    ),
    "bareAnchor" to listOf("kuki"),
    "strongAnchor" to listOf(
        "àwọn kuki tó ṣe pàtàkì", "àwọn kuki iṣẹ́", "àwọn kuki ìṣirò", "àwọn kuki ìpolówó",
        "àwọn kuki títà", "ìfẹ́ tó bófin mu", "àtòjọ àwọn alábàáṣiṣẹ́pọ̀", "àwọn alábàáṣiṣẹ́pọ̀ wa",
        "ó máa ń ṣiṣẹ́ nígbà gbogbo", "àwọn ètò kuki"
    ),
    "neverClick" to listOf(
        "forúkọ sílẹ̀", "ṣe ìforúkọsílẹ̀", "ra", "san", "sanwó", "wọlé", "ṣí àkọọ́lẹ̀", "darapọ̀",
        "ṣètọrẹ", "láìsí ìpolówó", "lóṣooṣù", "lọ́dọọdún", "premium", "pàṣẹ"
    ),
    "acceptGuard" to listOf(
        "gba gbogbo rẹ̀", "gba gbogbo wọn", "gba gbogbo", "gba gbogbo kuki", "gba kuki",
        "gba kí o sì pa", "gba kí o sì tẹ̀síwájú", "mo gbà", "mo gbà gbogbo rẹ̀", "bẹ́ẹ̀ ni mo gbà",
        "ó dára mo ti gbọ́", "=gba", "=mo gbà", "=ó dára", "=bẹ́ẹ̀ ni", "=mo ti gbọ́", "=tẹ̀síwájú",
        "=gbogbo"
    ),
    "acceptWords" to listOf("gba", "mo gbà", "ìyọ̀ǹda", "gbà"),
    "switchEssential" to listOf(
        "ṣe pàtàkì", "tó ṣe pàtàkì", "pọndandan", "ó máa ń ṣiṣẹ́ nígbà gbogbo", "nígbà gbogbo ni ó wà",
        "a kò lè pa á", "kí ojú ìwé lè ṣiṣẹ́"
    ),
    "switchNonEssential" to listOf("kò ṣe pàtàkì", "àṣàyàn", "àfikún", "kò pọndandan"),
    "switchKeepOn" to listOf(
        "má ta", "má pín", "má tọ̀ ẹ̀yìn", "ranti yíyàn mi", "máa wà ní ìwọlé", "má ṣe fihàn mọ́"
    ),
    "adsPurpose" to listOf(
        "ìpolówó", "títà", "ìfojúsùn", "ṣíṣe fún ẹni", "àkọsílẹ̀ ẹni", "ìkànnì àwùjọ"
    ),
    "statsPurpose" to listOf(
        "ìṣirò", "ìtúpalẹ̀", "wíwọ̀n", "àwọn olùwòran", "iṣẹ́ ṣíṣe", "dátà lílò", "àwọn àlejò",
        "ìmúdára", "ìwádìí"
    ),
    "topLevelWords" to listOf("gbogbo", "nìkan", "pàtàkì", "kuki"),
    "cookieContext" to listOf(
        "a ń lo", "ń lo", "gba", "ìyọ̀ǹda", "àṣírí", "ìlànà", "ètò", "alábàáṣiṣẹ́pọ̀", "ìpolówó", "dátà"
    ),
    "sensitiveScreen" to listOf(
        "nọ́mbà káàdì", "káàdì ìrajà", "ọjọ́ ìparí", "kódù ààbò", "cvv", "cvc", "iban",
        "nọ́mbà àkọọ́lẹ̀", "àlàyé ìsanwó", "san báyìí"
    )
)
