// Somali. Draft: a native speaker should check it before release

val code = "so"
val name = "Somali"
val status = "draft"
val conj = listOf("iyo")

val pack = mapOf(
    "reject" to listOf(
        "dhammaan diid", "diid dhammaan", "diid dhammaan kukiyada", "diid kukiyada",
        "sii wad adigoon aqbalin", "ha aqbalin", "ma aqbalayo", "ma oggola", "ka laabo oggolaanshaha",
        "maya mahadsanid", "dami dhammaan", "xir dhammaan", "diid oo xir", "=diid", "=maya",
        "=ma aqbalayo", "=ma oggola"
    ),
    "necessary" to listOf(
        "kaliya kuwa lagama maarmaanka ah", "kukiyada lagama maarmaanka ah kaliya",
        "kaliya kuwa muhiimka ah", "aqbal kuwa lagama maarmaanka ah kaliya",
        "sii wad kuwa lagama maarmaanka ah kaliya", "=kukiyada lagama maarmaanka ah",
        "=lagama maarmaan", "=kuwa muhiimka ah", "=kaliya lagama maarmaanka"
    ),
    "manage" to listOf(
        "maamul doorashooyinka", "maamul dookhyada", "maamul kukiyada", "maamul oggolaanshaha",
        "dejinta kukiyada", "dejinta asturnaanta", "doorashooyin dheeraad ah", "tus faahfaahinta",
        "liiska lammaanayaasha", "waxaan rabaa inaan doorto", "=dejinta", "=doorashooyinka", "=habee",
        "=faahfaahin", "=maamul", "=doorashooyin dheeraad ah"
    ),
    "confirm" to listOf(
        "kaydi doorashooyinka", "kaydi dejinta", "kaydi doorashadayda", "kaydi oo xir",
        "xaqiiji doorashadayda", "xaqiiji doorashada", "dabaq dejinta", "kaydi isbeddellada", "=kaydi",
        "=xaqiiji", "=dhammaystir"
    ),
    "fallbackReject" to listOf("diid", "=maya", "=maya mahadsanid", "=ma aqbalayo"),
    "rejectButton" to listOf("=diid", "=diid dhammaan", "=maya", "=dami"),
    "expandSection" to listOf("ujeeddooyin gaar ah", "sifooyin gaar ah"),
    "anchor" to listOf(
        "waxaan isticmaalnaa kukiyada", "boggan wuxuu isticmaalaa kukiyada",
        "annaga iyo lammaanayaasheena", "asturnaantaada", "xiisaha sharciga ah",
        "liiska lammaanayaasha", "oggolaanshahaaga", "lammaanayaasheena", "xayeysiisyo gaar ah",
        "siyaasadda kukiyada"
    ),
    "bareAnchor" to listOf("kukiyada"),
    "strongAnchor" to listOf(
        "kukiyada lagama maarmaanka ah", "kukiyada shaqada", "kukiyada tirakoobka",
        "kukiyada xayeysiiska", "kukiyada suuqgeynta", "xiisaha sharciga ah", "liiska lammaanayaasha",
        "lammaanayaasheena", "had iyo jeer firfircoon", "dejinta kukiyada"
    ),
    "neverClick" to listOf(
        "isdiiwaangeli", "rukumo", "iibso", "bixi", "lacag bixin", "gal", "samee akoon", "ku biir",
        "tabaruc", "iyada oo aan xayeysiis lahayn", "bishii", "sannadkii", "premium", "dalbo"
    ),
    "acceptGuard" to listOf(
        "aqbal dhammaan", "aqbal dhammaan kukiyada", "aqbal kukiyada", "oggolow dhammaan",
        "aqbal oo xir", "aqbal oo sii wad", "waan aqbalay", "waan oggolahay", "haa waan aqbalay",
        "ok waan fahmay", "=aqbal", "=oggolow", "=waan oggolahay", "=ok", "=haa", "=waan fahmay",
        "=sii wad", "=dhammaan"
    ),
    "acceptWords" to listOf("aqbal*", "oggol*", "oggolaan*", "waan aqbalay"),
    "switchEssential" to listOf(
        "lagama maarmaan", "lagama maarmaanka ah", "muhiim", "muhiimka ah", "waajib",
        "had iyo jeer firfircoon", "had iyo jeer shidan", "lama damin karo", "si shabakadu u shaqeyso"
    ),
    "switchNonEssential" to listOf(
        "aan lagama maarmaan ahayn", "aan muhiim ahayn", "ikhtiyaari", "dheeraad ah", "looma baahna"
    ),
    "switchKeepOn" to listOf(
        "ha iibin", "ha wadaagin", "ha raacin", "xasuuso doorashadayda", "ku sii jir gelitaanka",
        "ha ku soo bandhigin mar kale"
    ),
    "adsPurpose" to listOf(
        "xayeysiis", "xayeysiisyada", "suuqgeyn", "bartilmaameed", "shakhsiyayn", "astaan",
        "baraha bulshada"
    ),
    "statsPurpose" to listOf(
        "tirakoob", "falanqayn", "cabbir", "dhagaystayaasha", "waxqabad", "xogta isticmaalka",
        "booqdayaasha", "hagaajin", "cilmi baaris"
    ),
    "topLevelWords" to listOf("dhammaan", "kaliya", "lagama", "maarmaanka", "muhiimka", "kukiyada"),
    "cookieContext" to listOf(
        "waxaan isticmaalnaa", "isticmaalaa", "aqbal", "oggolaansho", "asturnaan", "siyaasad", "dejin",
        "lammaanayaasha", "xayeysiis", "xog"
    ),
    "sensitiveScreen" to listOf(
        "lambarka kaarka", "kaarka amaahda", "taariikhda dhicitaanka", "koodka amniga", "cvv", "cvc",
        "iban", "lambarka akoonka", "faahfaahinta lacag bixinta", "hadda bixi"
    )
)
