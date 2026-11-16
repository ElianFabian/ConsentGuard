// Igbo. Draft: a native speaker should check it before release. Dots and tone marks are ignored when matching

val code = "ig"
val name = "Igbo"
val status = "draft"
val conj = listOf("na")

val pack = mapOf(
    "reject" to listOf(
        "jụ ha niile", "jụ ihe niile", "jụ kuki niile", "jụ kuki", "gaa n'ihu na-enweghị ikwenye",
        "ekwena", "anaghị m ekwenye", "wepụ ikike", "mba daalụ", "gbanyụọ niile", "gbochie niile",
        "jụ ma mechie", "=jụ", "=mba", "=anaghị m ekwenye", "=ekwena"
    ),
    "necessary" to listOf(
        "naanị ndị dị mkpa", "naanị kuki ndị dị mkpa", "naanị ndị a chọrọ", "nabata naanị ndị dị mkpa",
        "gaa n'ihu na ndị dị mkpa naanị", "=kuki ndị dị mkpa", "=ndị dị mkpa", "=naanị ndị dị mkpa"
    ),
    "manage" to listOf(
        "jikwaa nhọrọ", "jikwaa ihe masịrị gị", "jikwaa kuki", "jikwaa ikike", "ntọala kuki",
        "ntọala nzuzo", "nhọrọ ndị ọzọ", "gosi nkọwa", "ndepụta ndị mmekọ", "achọrọ m ịhọrọ", "=ntọala",
        "=nhọrọ", "=hazie", "=nkọwa", "=jikwaa", "=nhọrọ ndị ọzọ"
    ),
    "confirm" to listOf(
        "chekwaa ihe masịrị gị", "chekwaa ntọala", "chekwaa nhọrọ m", "chekwaa mechie", "kwado nhọrọ m",
        "kwado nhọrọ", "tinye ntọala", "chekwaa mgbanwe", "=chekwaa", "=kwado", "=emechaala"
    ),
    "fallbackReject" to listOf("=mba", "=mba daalụ", "=ekwena", "=jụ"),
    "rejectButton" to listOf("=jụ", "=jụ ihe niile", "=mba", "=gbanyụọ"),
    "expandSection" to listOf("ebumnuche pụrụ iche", "atụmatụ pụrụ iche"),
    "anchor" to listOf(
        "anyị na-eji kuki", "weebụsaịtị a na-eji kuki", "anyị na ndị mmekọ anyị", "nzuzo gị",
        "mmasị ziri ezi", "ndepụta ndị mmekọ", "nkwenye gị", "ndị mmekọ anyị",
        "mgbasa ozi nke e mere maka gị", "iwu kuki"
    ),
    "bareAnchor" to listOf("kuki"),
    "strongAnchor" to listOf(
        "kuki ndị dị mkpa", "kuki ọrụ", "kuki ọnụ ọgụgụ", "kuki mgbasa ozi", "kuki ahịa",
        "mmasị ziri ezi", "ndepụta ndị mmekọ", "ndị mmekọ anyị", "na-arụ ọrụ mgbe niile", "ntọala kuki"
    ),
    "neverClick" to listOf(
        "debanye aha", "ịdebanye aha", "zụta", "kwụọ ụgwọ", "ịkwụ ụgwọ", "banye", "mepee akaụntụ",
        "sonye", "nye onyinye", "na-enweghị mgbasa ozi", "kwa ọnwa", "kwa afọ", "premium", "nye iwu"
    ),
    "acceptGuard" to listOf(
        "nabata niile", "nabata ihe niile", "nabata kuki niile", "nabata kuki", "kwe ka niile",
        "nabata ma mechie", "nabata ma gaa n'ihu", "ekwenyere m", "ekwenyere m niile", "ee ekwenyere m",
        "ọ dị mma aghọtara m", "=nabata", "=kwe", "=ekwenyere m", "=ọ dị mma", "=ee", "=aghọtara m",
        "=gaa n'ihu", "=niile"
    ),
    "acceptWords" to listOf("nabata", "ekwenyere", "ikike", "kwe"),
    "switchEssential" to listOf(
        "dị mkpa", "ndị dị mkpa", "a chọrọ", "na-arụ ọrụ mgbe niile", "mgbe niile ka ọ na-enwe",
        "enweghị ike imechi ya", "ka weebụsaịtị rụọ ọrụ"
    ),
    "switchNonEssential" to listOf("adịghị mkpa", "nhọrọ", "mgbakwunye", "achọghị"),
    "switchKeepOn" to listOf(
        "ere ere", "ekesala", "elegharị anya", "cheta nhọrọ m", "nọgide n'ime", "egosila ọzọ"
    ),
    "adsPurpose" to listOf(
        "mgbasa ozi", "ahịa", "ebumnuche", "ime nke onwe", "profaịlụ", "mgbasa ozi mmekọrịta"
    ),
    "statsPurpose" to listOf(
        "ọnụ ọgụgụ", "nyocha", "tụọ", "ndị na-ege ntị", "arụmọrụ", "data ojiji", "ndị ọbịa",
        "imeziwanye", "nyocha ọmụmụ"
    ),
    "topLevelWords" to listOf("niile", "naanị", "mkpa", "kuki"),
    "cookieContext" to listOf(
        "anyị na-eji", "na-eji", "nabata", "nkwenye", "nzuzo", "iwu", "ntọala", "ndị mmekọ",
        "mgbasa ozi", "data"
    ),
    "sensitiveScreen" to listOf(
        "nọmba kaadị", "kaadị ụgwọ", "ụbọchị njedebe", "koodu nchekwa", "cvv", "cvc", "iban",
        "nọmba akaụntụ", "nkọwa ịkwụ ụgwọ", "kwụọ ugbu a"
    )
)
