// French

val code = "fr"
val name = "French"
val status = "reviewed"
val conj = listOf("et")

val pack = mapOf(
    "reject" to listOf(
        "tout refuser", "refuser tout", "tout rejeter", "rejeter tout", "refuser tous les cookies",
        "refuser les cookies", "refuser les cookies optionnels", "refuser les cookies non essentiels",
        "continuer sans accepter", "poursuivre sans accepter", "continuer sans consentir",
        "continuer sans cookies", "continuer sans suivi", "je refuse", "je refuse tout",
        "je n accepte pas", "ne pas accepter", "ne pas consentir", "non merci", "tout désactiver",
        "tout bloquer", "refuser et fermer", "refuser et continuer", "refuser le suivi",
        "refuser la publicité", "=refuser", "=rejeter", "=décliner", "=non", "=je m oppose",
        "=s opposer", "=tout désactiver", "tout désélectionner", "tout décocher", "=interdire",
        "tout interdire", "je n'accepte rien", "je désactive tout", "=je désactive"
    ),
    "necessary" to listOf(
        "uniquement les cookies nécessaires", "uniquement les cookies essentiels",
        "seulement les cookies nécessaires", "cookies nécessaires uniquement",
        "cookies essentiels uniquement", "cookies obligatoires uniquement",
        "accepter uniquement les cookies nécessaires", "accepter seulement les cookies nécessaires",
        "accepter les cookies essentiels uniquement", "uniquement le nécessaire",
        "seulement le nécessaire", "uniquement les nécessaires", "uniquement les essentiels",
        "uniquement les cookies techniques", "continuer avec les cookies nécessaires",
        "continuer avec les cookies essentiels", "continuer uniquement avec les cookies nécessaires",
        "le strict nécessaire", "=cookies nécessaires", "=cookies essentiels", "=nécessaires",
        "=essentiels", "=nécessaires uniquement", "=essentiels uniquement", "=accepter les nécessaires",
        "=cookies fonctionnels uniquement", "uniquement les cookies", "accepter uniquement",
        "que les cookies techniques", "que les cookies nécessaires", "que les cookies essentiels"
    ),
    "manage" to listOf(
        "gérer les préférences", "gérer mes préférences", "gérer les options", "gérer mes choix",
        "gérer les cookies", "gérer mes cookies", "gérer le consentement", "gérer mon consentement",
        "paramètres des cookies", "paramétrer les cookies", "paramétrer mes choix",
        "personnaliser les cookies", "personnaliser mes choix", "paramètres de confidentialité",
        "préférences de confidentialité", "plus d options", "autres options", "options supplémentaires",
        "afficher les détails", "voir les détails", "afficher les finalités",
        "afficher les partenaires", "liste des partenaires", "liste des fournisseurs",
        "finalités et partenaires", "choisir mes cookies", "choisir mes options",
        "centre de préférences", "centre de confidentialité", "réglages des cookies",
        "réglages de confidentialité", "en savoir plus et personnaliser", "je choisis",
        "je veux choisir", "=paramètres", "=préférences", "=options", "=personnaliser", "=paramétrer",
        "=configurer", "=détails", "=gérer", "=réglages", "=mes choix", "=plus d options",
        "=paramétrer les cookies"
    ),
    "confirm" to listOf(
        "enregistrer mes choix", "enregistrer mes préférences", "enregistrer les préférences",
        "enregistrer mes paramètres", "enregistrer les paramètres", "enregistrer et fermer",
        "enregistrer et continuer", "enregistrer et quitter", "sauvegarder mes choix",
        "sauvegarder mes préférences", "sauvegarder et fermer", "confirmer mes choix",
        "confirmer ma sélection", "confirmer les choix", "confirmer mes préférences",
        "valider mes choix", "valider ma sélection", "valider les choix", "valider mes préférences",
        "valider et fermer", "appliquer mes choix", "appliquer les paramètres", "accepter la sélection",
        "accepter mes choix", "autoriser la sélection", "=enregistrer", "=valider", "=confirmer",
        "=appliquer", "=terminé", "=sauvegarder", "=enregistrer les choix", "=valider la sélection",
        "=confirmer la sélection"
    ),
    "fallbackReject" to listOf(
        "refuser", "rejeter", "décliner", "=non", "=non merci", "=je refuse", "=ne pas accepter"
    ),
    "rejectButton" to listOf(
        "=refuser", "=tout refuser", "=rejeter", "=non", "=désactiver", "=s opposer"
    ),
    "expandSection" to listOf(
        "finalités spéciales", "fonctionnalités spéciales", "finalités particulières"
    ),
    "anchor" to listOf(
        "nous utilisons des cookies", "ce site utilise des cookies", "ce site web utilise des cookies",
        "nous et nos partenaires", "votre vie privée", "respect de votre vie privée",
        "vos données vos choix", "intérêt légitime", "intérêts légitimes", "liste des partenaires",
        "paramètres de confidentialité", "gérer le consentement", "votre consentement",
        "nos partenaires", "nos fournisseurs", "fournisseurs tiers", "publicités personnalisées",
        "stocker et ou accéder à des informations", "finalités du traitement", "politique de cookies",
        "politique relative aux cookies", "bandeau cookies", "technologies de suivi",
        "en poursuivant votre navigation", "en continuant à naviguer",
        "vos préférences en matière de cookies", "informations sur les cookies"
    ),
    "bareAnchor" to listOf("cookies", "témoins", "traceurs"),
    "strongAnchor" to listOf(
        "strictement nécessaires", "cookies nécessaires", "cookies essentiels", "cookies fonctionnels",
        "cookies de performance", "cookies statistiques", "cookies analytiques", "cookies marketing",
        "cookies publicitaires", "cookies de ciblage", "publicités personnalisées", "intérêt légitime",
        "liste des partenaires", "nos partenaires", "nos fournisseurs", "toujours actif",
        "toujours actifs", "finalités du traitement", "préférences des cookies",
        "paramètres des cookies", "catégories de cookies", "finalités spéciales",
        "fonctionnalités spéciales", "technologies de suivi", "fournisseurs tiers"
    ),
    "neverClick" to listOf(
        "s abonner", "abonnement", "abonnez vous", "acheter", "payer", "paiement", "se connecter",
        "connexion", "s inscrire", "inscription", "créer un compte", "devenir membre", "adhérer",
        "essai gratuit", "essayer gratuitement", "faire un don", "don", "soutenir", "sans publicité",
        "sans pub", "supprimer les publicités", "par mois", "par an", "mensuel", "annuel",
        "passer à premium", "commander", "premium", "s'abonner", "abonnez-vous", "je m'abonne"
    ),
    "acceptGuard" to listOf(
        "tout accepter", "accepter tout", "accepter tous les cookies", "=accepter les cookies",
        "tout autoriser", "autoriser tout", "autoriser tous les cookies", "autoriser les cookies",
        "accepter et fermer", "accepter et continuer", "accepter et poursuivre", "accepter et naviguer",
        "j accepte tout", "=j accepte", "=je suis d accord", "=je consens", "oui j accepte",
        "oui je suis d accord", "ok j ai compris", "accepter les paramètres recommandés",
        "accepter les recommandations", "=accepter", "=autoriser", "=consentir", "=d accord", "=ok",
        "=okay", "=oui", "=compris", "=continuer", "=j ai compris", "=activer", "=tout", "=poursuivre",
        "accepter et soutenir", "=j'accepte", "oui j'accepte", "=d'accord", "fermer et accepter",
        "continuer et accepter"
    ),
    "acceptWords" to listOf(
        "accept*", "jaccept*", "autoris*", "consent*", "dacc*", "accord", "permett*", "permis"
    ),
    "switchEssential" to listOf(
        "strictement nécessaire", "strictement nécessaires", "nécessaire", "nécessaires", "essentiel",
        "essentiels", "obligatoire", "obligatoires", "toujours actif", "toujours actifs",
        "toujours activé", "toujours activés", "requis", "requise", "techniquement nécessaire",
        "ne peut pas être désactivé", "ne peuvent pas être désactivés", "indispensable",
        "indispensables", "pour le fonctionnement du site", "fonctionnement du site"
    ),
    "switchNonEssential" to listOf(
        "non essentiel", "non essentiels", "non nécessaire", "non nécessaires", "pas nécessaire",
        "pas essentiel", "facultatif", "facultatifs", "facultative", "optionnel", "optionnels",
        "optionnelle", "superflu", "supplémentaire", "supplémentaires"
    ),
    "switchKeepOn" to listOf(
        "ne pas vendre", "ne pas partager", "ne pas suivre", "ne pas pister",
        "se souvenir de mon choix", "mémoriser mon choix", "mémoriser mes choix", "rester connecté",
        "ne plus afficher", "ne plus demander", "opposition"
    ),
    "adsPurpose" to listOf(
        "publicit", "marketing", "ciblage", "cibl", "personnalis", "reciblage",
        "basée sur les centres d intérêt", "profil", "réseaux sociaux", "sélection d annonces",
        "annonces personnalisées"
    ),
    "statsPurpose" to listOf(
        "statistique", "statistiques", "analyse", "analytique", "mesure", "mesures", "audience",
        "performance", "données d utilisation", "visiteurs", "fréquentation", "améliorer", "recherche"
    ),
    "topLevelWords" to listOf(
        "tout", "tous", "toutes", "uniquement", "seulement", "nécessaires", "nécessaire", "essentiels",
        "essentiel", "cookies", "optionnels", "traceurs", "suivi", "techniques"
    ),
    "cookieContext" to listOf(
        "nous utilisons", "utilise des cookies", "utilisons des cookies", "accepter", "consent",
        "consentement", "confidentialité", "politique", "paramètres", "préférences", "partenaires",
        "personnalis", "suivi", "publicit", "données"
    ),
    "sensitiveScreen" to listOf(
        "numéro de carte", "numero de carte", "carte bancaire", "titulaire de la carte",
        "date d expiration", "cryptogramme", "code de sécurité", "cvv", "cvc", "iban",
        "numéro de compte", "mot de passe", "saisir le code pin", "informations de paiement",
        "moyen de paiement", "payer maintenant", "commande avec obligation de paiement"
    )
)
